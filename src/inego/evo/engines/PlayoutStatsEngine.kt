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

class PlayoutStatsEngine(private val timeToThink: Long) : AsyncEngine {

    private var playoutManager = PlayoutManager(RandomSyncEngine)

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
        result?.let {
            val currentMoveStats = playoutManager.getCurrentMoveStats()
            playoutManager.stop()
            val bestMove = currentMoveStats.maxBy { it.value.playouts }!!.key

            it.complete(bestMove)
        }
    }

}
