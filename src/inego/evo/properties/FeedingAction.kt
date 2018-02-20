package inego.evo.properties

import inego.evo.game.Animal
import inego.evo.game.GameState
import inego.evo.game.moves.FeedingMove

/**
 * This property allows to perform a specific action instead of standard feeding from the food base.
 */
// TODO check this interface among individual and paired props to collect additional actions
interface FeedingAction {

    fun gatherMoves(animal: Animal, gameState: GameState): List<FeedingMove>

    // TODO remove ---- redundant, feeding logic contained in the feeding move
    fun performFeedingAction(gameState: GameState)

}