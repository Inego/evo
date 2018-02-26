package inego.evo.game.moves

import inego.evo.game.GameState
import inego.evo.game.PlayerState
import inego.evo.removeLast

abstract class Move {

    abstract val logMessage: String

    fun applyTo(gameState: GameState) {
        gameState.log { logMessage }
        gameState.applyMove()
    }

    protected abstract fun GameState.applyMove()

    abstract fun toString(player: PlayerState): String
}

object GameStartMove : Move() {
    override val logMessage: String
        get() = "The game has started."

    override fun toString(player: PlayerState) = "Start game"

    override fun GameState.applyMove() {
        // Hand out 6 cards for each player
        for (player in players) {
            repeat(6) {
                player.hand.add(deck.removeLast())
            }
        }

        logTurnStart()
    }
}


object EmptyMove : Move() {
    override val logMessage: String
        get() = "An empty move did nothing."

    override fun GameState.applyMove() {
        // Do nothing
    }

    override fun toString(player: PlayerState) = "Do nothing"
}
