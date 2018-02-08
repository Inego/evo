package inego.evo.game.moves

import inego.evo.game.GameState
import inego.evo.game.PlayerState
import inego.evo.removeLast

abstract class Move
{
    fun applyTo(gameState: GameState) = gameState.applyMove()
    protected abstract fun GameState.applyMove()
    abstract fun toString(gameState: GameState, player: PlayerState): String
}

object GameStartMove : Move() {
    override fun toString(gameState: GameState, player: PlayerState) = "Start game"

    override fun GameState.applyMove() {
        // Hand out 6 cards for each player
        for (player in players) {
            repeat(6) {
                player.hand.add(deck.removeLast())
            }
        }
    }
}