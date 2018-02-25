package inego.evo.game.moves

import inego.evo.game.*

abstract class FoodPropagationMove(
        protected val connectionMembership: ConnectionMembership
) : Move() {
    final override fun GameState.applyMove() {
        connectionMembership.isUsed = true
        onPropagation(connectionMembership.other, this)
    }

    abstract fun onPropagation(animal: Animal, gameState: GameState)
}


class FoodPropagationMoveSelection(decidingPlayer: PlayerState, moves: List<FoodPropagationMove>)
    : MoveSelection<FoodPropagationMove>(decidingPlayer, moves)
