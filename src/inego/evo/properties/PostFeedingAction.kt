package inego.evo.properties

import inego.evo.game.GameState

interface PostFeedingAction {

    // TODO call this after feeding from base
    fun performPostFeedingAction(gameState: GameState)

}