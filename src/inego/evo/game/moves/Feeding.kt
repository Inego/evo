package inego.evo.game.moves

import inego.evo.game.Animal
import inego.evo.game.GameState
import inego.evo.game.MoveSelection
import inego.evo.game.PlayerState

abstract class FeedingMove(val animal: Animal) : Move() {
    override fun GameState.applyMove() {
        doFeeding(this)
        afterPlayerFeeding()
    }

    abstract fun doFeeding(gameState: GameState)
}


class GetRedTokenMove(animal: Animal) : FeedingMove(animal) {
    override fun toString(gameState: GameState, player: PlayerState) = "$animal takes 1 food"

    override fun doFeeding(gameState: GameState) {
        gameState.foodBase--

        animal.gainRedToken()
    }
}

class BurnFatMove(animal: Animal, private val fatToBurn: Int) : FeedingMove(animal) {
    override fun doFeeding(gameState: GameState) {
        animal.fat -= fatToBurn
        animal.hasFood += fatToBurn
    }

    override fun toString(gameState: GameState, player: PlayerState) = "$animal converts $fatToBurn fat to food"
}


class FeedingMoveSelection(decidingPlayer: PlayerState, moves: List<FeedingMove>)
    : MoveSelection<FeedingMove>(decidingPlayer, moves)