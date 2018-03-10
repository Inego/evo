package inego.evo

import inego.evo.game.Game
import inego.evo.game.GameCopier
import inego.evo.game.MoveSelection
import inego.evo.game.Player
import inego.evo.game.moves.Move
import java.util.concurrent.Executors
import kotlin.math.ln
import kotlin.math.sqrt


val EXPLORATION_COEFFICIENT = sqrt(2.0)


data class PlayoutTask(val game: Game, val player: Player, val move: Move)


class PlayoutStats(var wins: Int, var playouts: Int) {
    fun clone() = PlayoutStats(wins, playouts)
}


class MoveWithStats(val move: Move, val stats: PlayoutStats, var isBest: Boolean)


class PlayoutManager(val syncEngine: SyncEngine) {

    private val numberOfWorkers = Runtime.getRuntime().availableProcessors()
//    private val numberOfWorkers = max(Runtime.getRuntime().availableProcessors() - 1, 1)
//    private val numberOfWorkers = 1


    private val threadPool = Executors.newFixedThreadPool(numberOfWorkers)

    private var lock = Object()

    private var game: Game? = null
    private var decidingPlayer: Player? = null
    private val moves: MutableMap<Move, PlayoutStats> = mutableMapOf()

    private var playoutCount: Int = 0

    fun stop() {
        game = null
    }

    fun start(game: Game, moveSelection: MoveSelection<*>) {
        synchronized(lock) {
            // TODO check for running and stop

            this.game = game

            moves.clear()
            moveSelection.moves.associateByTo(moves, {it}) { PlayoutStats(0, 0) }

            decidingPlayer = moveSelection.decidingPlayer

            playoutCount = 0

            repeat(numberOfWorkers) {
                threadPool.execute(PlayoutWorker(this))
            }
        }
    }

    fun getCurrentMoveStats(): Map<Move, PlayoutStats> {
        synchronized(lock) {
            return moves.entries.associateBy({ it.key }) {it.value.clone()}
        }
    }

    fun getNextPlayoutTask(): PlayoutTask? {

        synchronized(lock) {

            if (game == null) {
                return null
            }

            if (moves.isEmpty()) {
                throw IllegalStateException("Move list is empty")
            }

            var bestScore = 0.0

            val lnOfTotalPlayouts = ln((playoutCount + 1).toDouble())

            var bestMove: Move? = null
            var bestStats: PlayoutStats? = null

            for ((move, stats) in moves) {
                if (stats.playouts == 0) {
                    bestMove = move
                    bestStats = stats
                    break
                }

                val score = stats.wins.toDouble() / stats.playouts + EXPLORATION_COEFFICIENT * sqrt(lnOfTotalPlayouts / stats.playouts)

                if (score > bestScore) {
                    bestScore = score
                    bestMove = move
                    bestStats = stats
                }
            }

            bestStats!!.playouts++
            playoutCount++

            return PlayoutTask(game!!, decidingPlayer!!, bestMove!!)
        }
    }

    fun registerWin(game: Game, move: Move) {
        synchronized(lock) {
            // TODO handle when current playouts were stopped
            if (game == this.game)
                moves.getValue(move).wins++
        }
    }
}


class PlayoutWorker(private val manager: PlayoutManager) : Runnable {
    override fun run() {

        do {
            val task = manager.getNextPlayoutTask() ?: break

            val (game, player, move) = task

            val copier = GameCopier(game, player)
            val copiedGame = copier.copiedGame
            val copiedMove = move.clone(copier)
            val copiedPlayer = copier[player]

            val winner = playout(copiedGame, copiedPlayer, copiedMove, manager.syncEngine)
            if (winner == copier[player]) {
                manager.registerWin(game, move)
            }
        } while (true)
    }
}


fun playout(game: Game, initialPlayer: Player, initialMove: Move, syncEngine: SyncEngine): Player? {
    var nextMove: Move = initialMove
    var player = initialPlayer
    do {
        val moveSelection = game.next(initialPlayer, nextMove) ?: break
        nextMove = syncEngine.selectMove(game, moveSelection)
        player = moveSelection.decidingPlayer
    } while (true)
    return game.winner
}
