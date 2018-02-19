package inego.evo.properties.individual

import inego.evo.game.GameState
import inego.evo.properties.IndividualProperty
import inego.evo.properties.PostFeedingAction

object GrazingProperty : IndividualProperty("Grazing"), PostFeedingAction {
    override fun performPostFeedingAction(gameState: GameState) {
        TODO("Implement optional food spoiling")
    }
}