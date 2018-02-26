package inego.evo.properties.individual

import inego.evo.game.Animal
import inego.evo.game.GamePhase
import inego.evo.game.GameState
import inego.evo.game.PlayerState
import inego.evo.game.moves.FeedingAnimalMove
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

class HibernationMove(animal: Animal) : FeedingAnimalMove(animal) {
    override val logMessage: String
        get() = "${animal.fullName} falls asleep"

    override fun doFeeding(gameState: GameState): GamePhase {
        animal.isHibernating = true

        // There is no food propagation, so go directly to grazing
        return GamePhase.GRAZING
    }

    override fun toString(player: PlayerState) = "$animal falls asleep"
}
