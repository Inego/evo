package game.moves

import game.GameState

abstract class Move
{
    fun applyTo(gameState: GameState) = gameState.applyMove()
    protected abstract fun GameState.applyMove()
}
