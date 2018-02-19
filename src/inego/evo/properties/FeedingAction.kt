package inego.evo.properties

import inego.evo.game.GameState

/**
 * This property allows to perform a specific action instead of standard feeding from the food base.
 */
// TODO check this interface among individual and paired props to collect additional actions
interface FeedingAction {


    fun performFeedingAction(gameState: GameState)

}