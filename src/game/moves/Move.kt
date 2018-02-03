package game.moves

import game.GameState
import removeLast

abstract class Move
{
    fun applyTo(gameState: GameState) = gameState.applyMove()
    protected abstract fun GameState.applyMove()
}

object GameStartMove : Move() {
    override fun GameState.applyMove() {
        // Hand out 6 cards for each player
        for (player in players) {
            repeat(6) {
                player.hand.add(deck.removeLast())
            }
        }
    }
}