package inego.evo.properties.individual

import inego.evo.game.Animal
import inego.evo.game.GameState
import inego.evo.game.PlayerState
import inego.evo.game.moves.FeedingMove
import inego.evo.properties.FeedingAction
import inego.evo.properties.IndividualProperty

object HibernationProperty : IndividualProperty("Hibernation"), FeedingAction {
    override fun gatherFeedingMoves(animal: Animal, gameState: GameState): List<FeedingMove> {
        if (!(animal.hibernatedLastTurn || gameState.isLastTurn)) {
            return listOf(HibernationMove(animal))
        }
        return emptyList()
    }
}

class HibernationMove(animal: Animal) : FeedingMove(animal) {
    override fun doFeeding(gameState: GameState) {
        animal.isHibernating = true
    }

    override fun toString(gameState: GameState, player: PlayerState) = "$animal falls asleep"
}
