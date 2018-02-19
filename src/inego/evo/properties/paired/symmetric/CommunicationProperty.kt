package inego.evo.properties.paired.symmetric

import inego.evo.game.GameState
import inego.evo.properties.PostFeedingAction
import inego.evo.properties.SymmetricProperty
import inego.evo.properties.paired.PairedPropertySide

object CommunicationProperty : SymmetricProperty("Communication", Communicator) {
}

object Communicator : PairedPropertySide("Communicator"), PostFeedingAction {

    override fun performPostFeedingAction(gameState: GameState) {
        TODO("Give the other communicator one red food out of turn")
    }
}