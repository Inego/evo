package inego.evo

import inego.evo.game.Game
import inego.evo.game.MoveSelection
import inego.evo.game.Player
import inego.evo.game.moves.GameStartMove
import inego.evo.game.moves.Move
import java.util.*
import java.util.concurrent.CompletableFuture
import kotlin.system.measureTimeMillis


interface GameFlowSubscriber {

    fun onChoicePoint(moveSelection: MoveSelection<*>, forAI: Boolean)

    fun onGameOver()

}


class GameManager(val game: Game) {

    private val engines: MutableMap<Player, AsyncEngine> = mutableMapOf()

    private var subscriber: GameFlowSubscriber? = null

    var decidingPlayer = game[0]

    fun setEngine(idx: Int, engine: AsyncEngine) {
        engines[game.players[idx]] = engine
    }

    fun next(player: Player, moveToMake: Move) {

        val nextMoveSelection = game.next(player, moveToMake)

        if (nextMoveSelection == null) {
            subscriber?.onGameOver()
        } else {
            decidingPlayer = nextMoveSelection.decidingPlayer
            val engine = engines[decidingPlayer]

            subscriber?.onChoicePoint(nextMoveSelection, engine != null)

            engine?.let {
                it.selectMove(game, nextMoveSelection).thenAccept {
                    next(decidingPlayer, it)
                }
            }
        }
    }

    fun isAI(player: Player) = engines.containsKey(player)

    fun subscribe(subscriber: GameFlowSubscriber) {
        this.subscriber = subscriber
    }
}

interface SyncEngine {
    fun selectMove(game: Game, moveSelection: MoveSelection<*>): Move
}

interface AsyncEngine {
    fun selectMove(game: Game, moveSelection: MoveSelection<*>): CompletableFuture<Move>
    fun forceMove()
}


class RandomSyncEngine(private val random: Random) : SyncEngine {
    override fun selectMove(game: Game, moveSelection: MoveSelection<*>): Move {
        return random.from(moveSelection)
    }
}


fun main(args: Array<String>) {

    var p1Wins = 0
    var ties = 0

    val random = Random()

    val elapsed = measureTimeMillis {
        for (i in 0..100000) {

            if (i % 10000 == 0) {
                println(i)
            }

            val game = Game.new(3, random, false)

            var nextMove: Move = GameStartMove
            var nextPlayer: Player = game.currentPlayer

            do {
                val moveSelection = game.next(nextPlayer, nextMove) ?: break
                nextPlayer = moveSelection.decidingPlayer
                nextMove = random.from(moveSelection)
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
