package inego.evo.game.moves

import inego.evo.game.Game
import inego.evo.game.GameCopier
import inego.evo.game.Player
import inego.evo.removeLast

abstract class Move {

    abstract val logMessage: String

    fun applyTo(game: Game) {
        game.log { logMessage }
        game.applyMove()
    }

    protected abstract fun Game.applyMove()

    abstract fun toString(player: Player): String

    abstract fun clone(c: GameCopier): Move
}

object GameStartMove : Move() {
    override fun clone(c: GameCopier) = this

    override val logMessage: String
        get() = "The game has started."

    override fun toString(player: Player) = "Start game"

    override fun Game.applyMove() {
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

    override fun clone(c: GameCopier) = this

    override val logMessage: String
        get() = "An empty move did nothing."

    override fun Game.applyMove() {
        // Do nothing
    }

    override fun toString(player: Player) = "Do nothing"
}
