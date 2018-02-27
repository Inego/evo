package inego.evo

import inego.evo.game.Game
import inego.evo.game.MoveSelection
import inego.evo.game.Player
import inego.evo.game.moves.GameStartMove
import inego.evo.game.moves.Move
import kotlin.system.measureTimeMillis


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

    var p1Wins = 0
    var ties = 0

    val elapsed = measureTimeMillis {
        for (i in 0..100000) {

            if (i % 10000 == 0) {
                println(i)
            }

            val game = Game.new(2, false)

            var nextMove: Move = GameStartMove

            do {
                val moveSelection = game.next(nextMove) ?: break
                nextMove = moveSelection.moves.randomElement
            } while (true)

            val winner = game.winner
            when (winner) {
                game.players[0] -> p1Wins++
                null -> ties++
            }
        }
    }
    println("Elapsed time: $elapsed ms")
    println("Wins: $p1Wins, ties: $ties")
}
