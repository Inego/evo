package inego.evo

import inego.evo.game.Game
import inego.evo.game.GameCopier
import inego.evo.game.MoveSelection
import inego.evo.game.Player
import inego.evo.game.moves.Move
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.ln
import kotlin.math.sqrt


val EXPLORATION_COEFFICIENT = sqrt(2.0)


data class PlayoutTask(val gameCopier: GameCopier, val moveSelection: MoveSelection<*>, val move: Move)


class PlayoutStats(var wins: Int, var playouts: Int) {
    fun clone() = PlayoutStats(wins, playouts)
}


class MoveWithStats(val move: Move, val stats: PlayoutStats, var isBest: Boolean)


class PlayoutManager(private val syncEngineFactory: (Int) -> SyncEngine) {

    private val numberOfWorkers = Runtime.getRuntime().availableProcessors()
//    private val numberOfWorkers = max(Runtime.getRuntime().availableProcessors() - 1, 1)
//    private val numberOfWorkers = 1

    private val threadPool = Executors.newFixedThreadPool(numberOfWorkers)

    private var lock = Object()

    private var game: Game? = null
    private var currentMoveSelection: MoveSelection<*>? = null

    private var decidingPlayer: Player? = null
    private val moves: MutableMap<Move, PlayoutStats> = mutableMapOf()

    private var playoutCount: Int = 0

    fun stop() {
        synchronized(lock) {
            game = null
        }
    }

    fun start(game: Game, moveSelection: MoveSelection<*>) {
        synchronized(lock) {
            // TODO check for running and stop

            this.game = game

            moves.clear()
            moveSelection.moves.associateByTo(moves, {it}) { PlayoutStats(0, 0) }

            currentMoveSelection = moveSelection

            decidingPlayer = moveSelection.decidingPlayer

            playoutCount = 0

            // Use a thread-local random to seed RNG that will be used in the workers
            val random = ThreadLocalRandom.current()

            for (i in 0 until numberOfWorkers) {
                val syncEngine = syncEngineFactory(i)
                threadPool.execute(PlayoutWorker(this, syncEngine, Random(random.nextLong())))
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

            val copier = GameCopier(game!!, decidingPlayer!!, ThreadLocalRandom.current())

            return PlayoutTask(copier, currentMoveSelection!!, bestMove!!)
        }
    }

    fun registerWin(moveSelection: MoveSelection<*>, move: Move) {
        synchronized(lock) {
            // TODO handle when current playouts were stopped
            if (moveSelection == currentMoveSelection)
                moves.getValue(move).wins++
        }
    }
}


class PlayoutWorker(private val manager: PlayoutManager, private val syncEngine: SyncEngine, private val random: Random) : Runnable {
    override fun run() {

        do {
            val task = manager.getNextPlayoutTask() ?: break

            val (copier, moveSelection, move) = task

            val copiedGame = copier.copiedGame
            val copiedMove = move.clone(copier)
            val copiedPlayer = copier[moveSelection.decidingPlayer]

            val winner = playout(copiedGame, copiedPlayer, copiedMove, syncEngine)
            if (winner == copiedPlayer) {
                manager.registerWin(moveSelection, move)
            }
        } while (true)
    }
}


fun playout(game: Game, initialPlayer: Player, initialMove: Move, syncEngine: SyncEngine): Player? {
    var nextMove: Move = initialMove
    var player = initialPlayer
    do {
        val moveSelection = game.next(player, nextMove) ?: break
        nextMove = syncEngine.selectMove(game, moveSelection)
        player = moveSelection.decidingPlayer
    } while (true)
    return game.winner
}
