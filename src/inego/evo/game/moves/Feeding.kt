package inego.evo.game.moves

import inego.evo.game.Animal
import inego.evo.game.GameState
import inego.evo.game.PlayerState

abstract class FeedingMove() : Move() {
    override fun GameState.applyMove() {
        doFeeding(this)
        incCurrentPlayer()
    }

    abstract fun doFeeding(gameState: GameState)

}


class GetRedTokenMove(private val taker: Animal) : FeedingMove() {
    override fun toString(gameState: GameState, player: PlayerState) = "$taker takes 1 food"

    override fun doFeeding(gameState: GameState) {
        gameState.foodBase--
        taker.hasFood++

        // TODO feed connected animals! take into account the possibility of combinations + using every property only once per round

    }

}