package inego.evo.properties.paired.symmetric

import inego.evo.game.GameState
import inego.evo.properties.PostFeedingAction
import inego.evo.properties.SymmetricProperty
import inego.evo.properties.paired.PairedPropertySide

object CooperationProperty : SymmetricProperty("Cooperation", Cooperator) {
}

object Cooperator : PairedPropertySide("Cooperator"), PostFeedingAction {
    override fun performPostFeedingAction(gameState: GameState) {
        TODO("Hand out blue token to the other animal when this animal receives ANY token")
    }

}
