package inego.evo.game.moves

import inego.evo.game.*

abstract class FeedingMove : Move() {
    override fun GameState.applyMove() {
        phase = doFeeding(this)
    }

    abstract fun doFeeding(gameState: GameState): GamePhase
}


abstract class FeedingAnimalMove(val animal: Animal) : FeedingMove()


class GetRedTokenMove(animal: Animal) : FeedingAnimalMove(animal) {
    override val logMessage: String
        get() = "${animal.fullName} feeds from the base."

    override fun toString(player: PlayerState) = "$animal: take 1 food from base"
    override fun doFeeding(gameState: GameState): GamePhase {
        animal.gainRedToken(gameState)
        return GamePhase.FOOD_PROPAGATION
    }
}

class BurnFatMove(animal: Animal, private val fatToBurn: Int) : FeedingAnimalMove(animal) {
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


data class FeedingPassMove(val player: PlayerState) : FeedingMove() {
    override fun doFeeding(gameState: GameState): GamePhase {
        player.passed = true
        return GamePhase.FEEDING
    }

    override val logMessage: String
        get() = "$player passed from feeding."

    override fun toString(player: PlayerState) = "Pass from feeding"
}


class FeedingMoveSelection(decidingPlayer: PlayerState, moves: List<FeedingMove>)
    : MoveSelection<FeedingMove>(decidingPlayer, moves)
