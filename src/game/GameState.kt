package game

import cards.*
import java.util.concurrent.ThreadLocalRandom

class GameState() {

    var deck: MutableList<Card> = mutableListOf()

    val players: MutableList<PlayerState> = mutableListOf()

    var firstPlayerIdx = 0

    var currentPlayerIdx = 0

    val currentPlayer
        get() = players[currentPlayerIdx]

    var phase: GamePhase = GamePhase.DEVELOPMENT

    var foodBase = 0

    constructor(src: GameState) : this() {
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
        // TODO collect actions

        val nextDevelopingPlayer = this.getNextDevelopingPlayer()

        phase = GamePhase.FEEDING_BASE_DETERMINATION
    }

    private fun getNextDevelopingPlayer(): Int? {
        var cur = currentPlayerIdx

        while (true) {
            cur++
            if (cur == players.size) {
                cur = 0
            }
            else if (cur == currentPlayerIdx) {
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
            for (animal in player.animals) {
                animal.baseFood = 0
                animal.additionalFood = 0
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

        fun new(): GameState {

            return GameState().apply {
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