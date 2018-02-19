package inego.evo.properties.individual

import inego.evo.game.GameState
import inego.evo.properties.FeedingAction
import inego.evo.properties.IndividualProperty

object HibernationProperty : IndividualProperty("Hibernation"), FeedingAction {
    override fun performFeedingAction(gameState: GameState) {
        TODO("Set isFed flag, take into account that this action may not be taken twice in a row")
        // TODO may not be used in the last turn
    }
}