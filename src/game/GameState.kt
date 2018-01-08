package game

import cards.*

class GameState() {

    var deck: MutableList<Card> = mutableListOf()

    val players: MutableList<PlayerState> = mutableListOf()

    lateinit var firstPlayer: PlayerState

    lateinit var currentPlayer: PlayerState

    var phase: GamePhase = GamePhase.DEVELOPMENT

    constructor(src: GameState) : this() {

        when (phase) {

            GamePhase.DEVELOPMENT -> {

            }

            GamePhase.FEEDING -> TODO()
        }

    }

    fun next() {

    }

    fun addCard(card: Card, number: Int = 4) {
        repeat(number) {
            this.deck.add(card)
        }
    }

    companion object {

        fun new(): GameState {
            val gs = GameState()

            gs.addCard(CamouflageCard)
            gs.addCard(BurrowingCard)
            gs.addCard(SharpVisionCard)
            gs.addCard(SymbiosisCard)
            gs.addCard(PiracyCard)
            gs.addCard(GrazingCard)
            gs.addCard(TailLossCard)
            gs.addCard(HibernationCard)
            gs.addCard(PoisonousCard)
            gs.addCard(CommunicationCard)
            gs.addCard(ScavengerCard)
            gs.addCard(RunningCard)
            gs.addCard(MimicryCard)
            gs.addCard(SwimmingCard, 8)
            gs.addCard(ParasiteCarnivorousCard)
            gs.addCard(ParasiteFatTissueCard)
            gs.addCard(CooperationCarnivorousCard)
            gs.addCard(CooperationFatTissueCard)
            gs.addCard(BigCarnivorousCard)
            gs.addCard(BigFatTissueCard)

            return gs
        }

    }

}

enum class GamePhase {
    DEVELOPMENT,
    FEEDING
}