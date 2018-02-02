package game

import cards.*
import each
import game.moves.CreateAnimal
import game.moves.DevelopmentPass
import game.moves.Move
import java.util.concurrent.ThreadLocalRandom

class GameState(val numberOfPlayers: Int) {

    var deck: MutableList<Card> = mutableListOf()

    val players: List<PlayerState> = List(numberOfPlayers) { PlayerState() }

    var firstPlayerIdx = 0

    var currentPlayerIdx = 0

    var computeNextPlayer = false

    val currentPlayer
        get() = players[currentPlayerIdx]

    var phase: GamePhase = GamePhase.DEVELOPMENT

    var foodBase = 0

    constructor(src: GameState) : this(src.numberOfPlayers) {
    }

    fun next(move: Move) {
        move.applyTo(this)
    }

    fun next() {

        when (phase) {

            GamePhase.DEVELOPMENT -> this.performDevelopment()

            GamePhase.FEEDING_BASE_DETERMINATION -> this.performFeedingBaseDetermination()

            GamePhase.FEEDING -> this.performFeeding()


            GamePhase.EXTINCTION -> this.processExtinction()
            GamePhase.END -> {
                // Do nothing
            }
        }
    }

    private fun performDevelopment() {

        if (computeNextPlayer) {
            val nextDevelopingPlayer = this.getNextDevelopingPlayer()

            if (nextDevelopingPlayer == null) {
                phase = GamePhase.FEEDING_BASE_DETERMINATION
                return
            }
            currentPlayerIdx = nextDevelopingPlayer
        } else {
            computeNextPlayer = true
        }

        val player = currentPlayer

        // Gather development moves (pass is always an option)
        val moves: MutableList<Move> = mutableListOf(DevelopmentPass)

        for (card in player.hand.toSet()) {
            moves.add(CreateAnimal(card))
            when (card) {
                is SingleCard -> {
                    moves += card.property.getDevelopmentMoves(this, card)
                }
                is DoubleCard -> {
                    moves += card.firstProperty.getDevelopmentMoves(this, card)
                    moves += card.secondProperty.getDevelopmentMoves(this, card)
                }
            }
        }
    }


    private fun getNextDevelopingPlayer(): Int? {
        var cur = currentPlayerIdx

        while (true) {
            cur++
            if (cur == players.size) {
                cur = 0
            } else if (cur == currentPlayerIdx) {
                break
            }
            val p = players[cur]
            if (p.passed || p.hand.isEmpty()) {
                continue
            }
            return cur
        }
        return null
    }

    private fun performFeedingBaseDetermination() {

        foodBase = when (players.size) {
            2 -> dice() + 2
            3 -> dice() + dice()
            4 -> dice() + dice() + 2
            else -> throw IllegalStateException()
        }

        phase = GamePhase.FEEDING

    }

    private fun performFeeding() {
        // TODO
        phase = GamePhase.EXTINCTION
    }

    private fun processExtinction() {
        for (player in players) {
            player.passed = false

            // Remove dead animals
            player.animals.removeIf { it.starves }

            // Clean the state of player's animals
            player.animals.each {
                baseFood = 0
                additionalFood = 0
            }
        }

        if (deck.isEmpty()) {
            phase = GamePhase.END

        } else {
            // Hand out cards one by one and start a new turn

            var toHandOut = fromFirstPlayer().asSequence().map { Pair(it, it.cardsToHandOut) }

            loop@ while (true) {
                val newToHandOut: MutableList<Pair<PlayerState, Int>> = mutableListOf()
                for (pair in toHandOut) {
                    val card = deck.removeAt(0)
                    pair.first.hand.add(card)
                    if (deck.isEmpty()) {
                        break@loop
                    }
                    if (pair.second > 1) {
                        newToHandOut.add(pair.copy(second = pair.second - 1))
                    }
                }
                if (newToHandOut.isEmpty()) break
                toHandOut = newToHandOut.asSequence()
            }

            firstPlayerIdx++

            if (firstPlayerIdx >= players.size) {
                firstPlayerIdx = 0
            }

            currentPlayerIdx = firstPlayerIdx

            phase = GamePhase.DEVELOPMENT
        }
    }


    fun addCard(card: Card, number: Int = 4) {
        repeat(number) {
            this.deck.add(card)
        }
    }

    fun addCards(vararg cards: Card) {
        cards.forEach { addCard(it) }
    }

    private fun fromFirstPlayer() = object : Iterator<PlayerState> {
        var idx = 0

        override fun next(): PlayerState {
            var collIdx = firstPlayerIdx + idx
            if (collIdx >= players.size)
                collIdx -= players.size
            return players[collIdx]
        }

        override fun hasNext() = idx < players.size - 1
    }

    companion object {

        fun new(numberOfPlayers: Int): GameState {

            return GameState(numberOfPlayers).apply {
                addCards(
                        CamouflageCard,
                        BurrowingCard,
                        SharpVisionCard,
                        SymbiosisCard,
                        PiracyCard,
                        GrazingCard,
                        TailLossCard,
                        HibernationCard,
                        PoisonousCard,
                        CommunicationCard,
                        ScavengerCard,
                        RunningCard,
                        MimicryCard,
                        ParasiteCarnivorousCard,
                        ParasiteFatTissueCard,
                        CooperationCarnivorousCard,
                        CooperationFatTissueCard,
                        BigCarnivorousCard,
                        BigFatTissueCard
                )
                addCard(SwimmingCard, 8)
                deck.shuffle()
            }
        }
    }
}

enum class GamePhase {
    DEVELOPMENT,
    FEEDING_BASE_DETERMINATION,
    FEEDING,
    EXTINCTION,
    END
}

fun dice() = ThreadLocalRandom.current().nextInt(6) + 1
