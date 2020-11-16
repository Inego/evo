package inego.evo.engines

import inego.evo.AsyncEngine
import inego.evo.PlayoutManager
import inego.evo.RandomSyncEngine
import inego.evo.game.Game
import inego.evo.game.MoveSelection
import inego.evo.game.moves.Move
import java.util.*
import java.util.concurrent.CompletableFuture
import kotlin.concurrent.schedule


/**
 * An async engine based on purely random playouts for the specified amount of time.
 */
class PlayoutStatsEngine(private val timeToThink: Long) : AsyncEngine {

    private val random = Random()

    private var playoutManager = PlayoutManager {
        val seed = random.nextLong()
        val engineRandom = Random(seed)
        RandomSyncEngine(engineRandom)
    }

    private var result: CompletableFuture<Move>? = null

    override fun selectMove(game: Game, moveSelection: MoveSelection<*>): CompletableFuture<Move> {
        if (result?.isDone == false) {
            throw AssertionError()
        }

        playoutManager.start(game, moveSelection)

        Timer().schedule(timeToThink) {
            complete()
        }

        return CompletableFuture<Move>().also {
            result = it
        }
    }

    override fun forceMove() {
        complete()
    }

    private fun complete() {
        result?.let { moveFuture ->
            val currentMoveStats = playoutManager.getCurrentMoveStats()
            playoutManager.stop()
            val bestMove = currentMoveStats.maxByOrNull { it.value.playouts }!!.key

            moveFuture.complete(bestMove)
        }
    }

}
