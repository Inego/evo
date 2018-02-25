package inego.evo.game.moves

import inego.evo.game.*

abstract class FeedingMove(val animal: Animal) : Move() {
    override fun GameState.applyMove() {
        phase = doFeeding(this)
    }

    abstract fun doFeeding(gameState: GameState): GamePhase
}


class GetRedTokenMove(animal: Animal) : FeedingMove(animal) {
    override val logMessage: String
        get() = "${animal.fullName} feeds from the base."

    override fun toString(player: PlayerState) = "$animal: take 1 food from base"
    override fun doFeeding(gameState: GameState): GamePhase {
        animal.gainRedToken(gameState)
        return GamePhase.FOOD_PROPAGATION
    }
}

class BurnFatMove(animal: Animal, private val fatToBurn: Int) : FeedingMove(animal) {
    override val logMessage: String
        get() = "${animal.fullName} converts $fatToBurn fat to food."

    override fun doFeeding(gameState: GameState): GamePhase {
        animal.fat -= fatToBurn
        // Fat burning is a special action and as such does not lead to food propagation.
        animal.hasFood += fatToBurn
        return GamePhase.GRAZING
    }

    override fun toString(player: PlayerState) = "$animal: convert $fatToBurn fat to food"
}


class FeedingMoveSelection(decidingPlayer: PlayerState, moves: List<FeedingMove>)
    : MoveSelection<FeedingMove>(decidingPlayer, moves)