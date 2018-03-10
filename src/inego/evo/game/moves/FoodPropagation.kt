package inego.evo.game.moves

import inego.evo.game.*

abstract class FoodPropagationMove(
        protected val connectionMembership: ConnectionMembership
) : Move() {
    final override fun Game.applyMove(player: Player) {
        connectionMembership.isUsed = true
        onPropagation(connectionMembership.other, this)
    }

    abstract fun onPropagation(animal: Animal, game: Game)
}


class FoodPropagationMoveSelection(decidingPlayer: Player, moves: List<FoodPropagationMove>)
    : MoveSelection<FoodPropagationMove>(decidingPlayer, moves)
