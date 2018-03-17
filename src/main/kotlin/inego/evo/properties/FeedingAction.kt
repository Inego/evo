package inego.evo.properties

import inego.evo.game.Animal
import inego.evo.game.Game
import inego.evo.game.moves.FeedingMove

/**
 * This property allows to perform a specific action instead of standard feeding from the food base.
 */
interface FeedingAction {
    fun gatherFeedingMoves(animal: Animal, game: Game): List<FeedingMove>
}