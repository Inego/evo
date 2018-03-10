package inego.evo.game.moves

import inego.evo.game.Game
import inego.evo.game.GameCopier
import inego.evo.game.Player
import inego.evo.removeLast

abstract class Move {

    abstract fun logMessage(player: Player): String

    fun applyTo(game: Game, player: Player) {
        game.log { logMessage(player) }
        game.applyMove(player)
    }

    protected abstract fun Game.applyMove(player: Player)

    abstract fun toString(player: Player): String

    abstract fun clone(c: GameCopier): Move
}

object GameStartMove : Move() {
    override fun clone(c: GameCopier) = this

    override fun logMessage(player: Player) = "The game has started."

    override fun toString(player: Player) = "Start game"

    override fun Game.applyMove(player: Player) {
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

    override fun logMessage(player: Player) = "..."

    override fun Game.applyMove(player: Player) {
        // Do nothing
    }

    override fun toString(player: Player) = "Do nothing"
}
