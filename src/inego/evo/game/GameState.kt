package inego.evo.game

import inego.evo.cards.*
import inego.evo.each
import inego.evo.game.moves.CreateAnimal
import inego.evo.game.moves.DevelopmentPass
import inego.evo.game.moves.Move
import inego.evo.removeLast
import java.util.concurrent.ThreadLocalRandom

class GameState private constructor(val numberOfPlayers: Int) {

    var deck: MutableList<Card> = mutableListOf()

    val players: List<PlayerState> = List(numberOfPlayers) { index -> PlayerState("p${index + 1}") }

    private val moves: MutableList<Move> = mutableListOf()

    var firstPlayerIdx = 0

    var currentPlayerIdx = 0

    var computeNextPlayer = false

    inline val currentPlayer
        get() = players[currentPlayerIdx]

    var phase: GamePhase = GamePhase.DEVELOPMENT

    var foodBase = 0


    constructor(src: GameState) : this(src.numberOfPlayers) {
        // TODO copy constructor
    }

    fun next(move: Move): List<Move> {

        applyMove(move)

        do {

            when (phase) {

                GamePhase.DEVELOPMENT -> this.performDevelopment()

                GamePhase.FEEDING_BASE_DETERMINATION -> this.performFeedingBaseDetermination()

                GamePhase.FEEDING -> this.performFeeding()


                GamePhase.EXTINCTION -> this.processExtinction()
                GamePhase.END -> {
                    // Do nothing
                }
            }

            if (moves.size == 1) {
                applyMove(moves[0])
            }

        } while (moves.isEmpty() && phase != GamePhase.END)

        return moves

    }

    private fun applyMove(move: Move) {
        move.applyTo(this)

        moves.clear()
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
        moves.add(DevelopmentPass)

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
            }
            if (cur == currentPlayerIdx) {
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
        currentPlayerIdx = firstPlayerIdx

    }

    private fun performFeeding() {

        var entryPlayerIdx = -1

        while (true) {

            if (entryPlayerIdx == -1) {
                entryPlayerIdx = currentPlayerIdx
            }
            else {
                if (entryPlayerIdx == currentPlayerIdx)
                    break
            }

            // Collect feeding moves

            val player = currentPlayer

            val moves = player.animals.flatMap { it.gatherFeedingMoves(this) }

            if (!moves.isEmpty()) {
                return
            }

            incCurrentPlayer()
        }


        phase = GamePhase.EXTINCTION
    }

    fun incCurrentPlayer() {
        currentPlayerIdx++

        if (currentPlayerIdx == players.size)
            currentPlayerIdx = 0
    }

    private fun processExtinction() {
        for (player in players) {
            player.passed = false

            // Remove dead animals
            player.animals.removeIf { it.starves }

            // Clean the state of player's animals
            player.animals.each {
                hasFood = 0

                if (hibernatedLastTurn) {
                    hibernatedLastTurn = false
                } else {
                    if (isHibernating) {
                        isHibernating = false
                        hibernatedLastTurn = true
                    }
                }
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
                    val card = deck.removeLast()
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

    fun fromFirstPlayer() = object : Iterator<PlayerState> {
        var idx = 0

        override fun next(): PlayerState {
            var collIdx = firstPlayerIdx + idx
            idx++
            if (collIdx >= players.size)
                collIdx -= players.size
            return players[collIdx]
        }

        override fun hasNext() = idx < players.size
    }

    fun canAttack(attacker: Animal, victim: Animal): Boolean {

        for (individualProperty in attacker.individualProperties) {
            if (!individualProperty.mayAttack(victim))
                return false
        }

        for (individualProperty in victim.individualProperties) {
            if (!individualProperty.mayBeAttackedBy(victim, attacker)) {
                return false
            }
        }

        for (membership in victim.connections) {
            if (!membership.sideProperty.mayBeAttackedBy(attacker, membership.other)) {
                return false
            }
        }

        return true
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

    val isLastTurn: Boolean
        get() = TODO("Implement determination of the last turn")


}

enum class GamePhase {
    DEVELOPMENT,
    FEEDING_BASE_DETERMINATION,
    FEEDING,
    EXTINCTION,
    END
}

fun dice() = ThreadLocalRandom.current().nextInt(6) + 1
