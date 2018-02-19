package inego.evo.properties.individual

import inego.evo.game.GameState
import inego.evo.properties.FeedingAction
import inego.evo.properties.IndividualProperty

object PiracyProperty : IndividualProperty("Piracy"), FeedingAction {

    override fun performFeedingAction(gameState: GameState) {
        TODO("Steal food from an unfed animal")
    }
}