package inego.evo

import inego.evo.game.Game
import inego.evo.game.MoveSelection
import inego.evo.game.Player
import inego.evo.game.moves.GameStartMove
import inego.evo.game.moves.Move


class GameManager(val game: Game) {

    private val engines: MutableMap<Player, Engine> = mutableMapOf()

    fun setEngine(idx: Int, engine: Engine) {
        engines[game.players[idx]] = engine
    }

    fun next(move: Move): MoveSelection<*>? {

        var moveToMake = move

        while (true) {
            val nextMoveSelection = game.next(moveToMake) ?: return null
            val decidingPlayer = nextMoveSelection.decidingPlayer
            val engine = engines[decidingPlayer] ?: return nextMoveSelection
            moveToMake = engine.selectMove(game, nextMoveSelection)
        }
    }

    fun isAI(player: Player) = engines.containsKey(player)
}

interface Engine {
    fun selectMove(game: Game, moveSelection: MoveSelection<*>): Move

}

object RandomEngine : Engine {
    override fun selectMove(game: Game, moveSelection: MoveSelection<*>): Move {
        return moveSelection.moves.randomElement
    }
}


fun main(args: Array<String>) {
    val game = Game.new(2)

    var nextMove: Move = GameStartMove

    do {
        val moveSelection = game.next(nextMove) ?: break
        nextMove = moveSelection.moves.randomElement
    } while (true)
}
