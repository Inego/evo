package inego.evo

import inego.evo.game.Game
import inego.evo.game.MoveSelection
import inego.evo.game.Player
import inego.evo.game.moves.GameStartMove
import inego.evo.game.moves.Move
import java.util.*
import java.util.concurrent.CompletableFuture
import kotlin.system.measureTimeMillis


/**
 * An entity reacting to changes in a game state.
 */
interface GameFlowSubscriber {
    /**
     * Notified when a game choice has to be made by a player.
     *
     * @param moveSelection Move selection.
     * @param forAI If `true`, the move selection has to be handled by an AI / engine.
     */
    fun onChoicePoint(moveSelection: MoveSelection<*>, forAI: Boolean)

    /**
     * Notified when the game is over.
     */
    fun onGameOver()
}


/**
 * An instance handling the process of game flow.
 * It supports attaching engines to player sides to make moves. Other players will have to rely
 * on an external subscriber which will be notified of game events and asked for choices
 * at appropriate times.
 */
class GameManager(val game: Game) {

    private val engines: MutableMap<Player, AsyncEngine> = mutableMapOf()

    private var subscriber: GameFlowSubscriber? = null

    var decidingPlayer = game[0]

    /**
     * Sets an async engine to a specified player index.
     */
    fun setEngine(idx: Int, engine: AsyncEngine) {
        engines[game.players[idx]] = engine
    }

    /**
     * Proceeds the game state by a specified move made by a player.
     */
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

    /**
     * Checks if the specified player is controlled by AI.
     */
    fun isAI(player: Player) = engines.containsKey(player)

    /**
     * Sets the game flow subscriber.
     */
    fun subscribe(subscriber: GameFlowSubscriber) {
        this.subscriber = subscriber
    }
}


/**
 * An engine making synchronous moves.
 */
interface SyncEngine {
    fun selectMove(game: Game, moveSelection: MoveSelection<*>): Move
}

/**
 * An engine making asynchronous moves with force-move support.
 */
interface AsyncEngine {
    /**
     * Returns a completable future with a move selected by the engine from the specified move selection.
     */
    fun selectMove(game: Game, moveSelection: MoveSelection<*>): CompletableFuture<Move>

    /**
     * Forces the engine to make the move instantly. Currently available results will have to be used
     * to choose the best move.
     */
    fun forceMove()
}


/**
 * A purely random engine.
 */
class RandomSyncEngine(private val random: Random) : SyncEngine {
    override fun selectMove(game: Game, moveSelection: MoveSelection<*>): Move {
        return random.from(moveSelection)
    }
}


// Used for crude benchmarking purposes
fun main() {

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

            when (game.winner) {
                game.players[0] -> p1Wins++
                null -> ties++
            }
        }
    }
    println("Elapsed time: $elapsed ms")
    println("Wins: $p1Wins, ties: $ties")
}
