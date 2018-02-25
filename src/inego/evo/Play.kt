package inego.evo

import inego.evo.game.GameState
import inego.evo.game.MoveSelection
import inego.evo.game.PlayerState
import inego.evo.game.moves.GameStartMove
import inego.evo.game.moves.Move


class GameManager(val gameState: GameState) {

    private val engines: MutableMap<PlayerState, Engine> = mutableMapOf()

    fun setEngine(idx: Int, engine: Engine) {
        engines[gameState.players[idx]] = engine
    }

    fun next(move: Move): MoveSelection<*>? {

        var moveToMake = move

        while (true) {
            val nextMoveSelection = gameState.next(moveToMake) ?: return null
            val decidingPlayer = nextMoveSelection.decidingPlayer
            val engine = engines[decidingPlayer] ?: return nextMoveSelection
            moveToMake = engine.selectMove(gameState, nextMoveSelection)
        }
    }

    fun isAI(player: PlayerState) = engines.containsKey(player)
}

interface Engine {
    fun selectMove(gameState: GameState, moveSelection: MoveSelection<*>): Move

}

object RandomEngine : Engine {
    override fun selectMove(gameState: GameState, moveSelection: MoveSelection<*>): Move {
        return moveSelection.moves.randomElement
    }
}


fun main(args: Array<String>) {
    val gameState = GameState.new(2)

    var nextMove: Move = GameStartMove

    do {
        val moveSelection = gameState.next(nextMove) ?: break
        nextMove = moveSelection.moves.randomElement
    } while (true)
}
