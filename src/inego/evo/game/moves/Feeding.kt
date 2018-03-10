package inego.evo.game.moves

import inego.evo.game.*

abstract class FeedingMove : Move() {
    override fun Game.applyMove() {
        phase = doFeeding(this)
    }

    abstract fun doFeeding(game: Game): GamePhase
}


abstract class FeedingAnimalMove(val animal: Animal) : FeedingMove()


class GetRedTokenMove(animal: Animal) : FeedingAnimalMove(animal) {

    override fun clone(c: GameCopier) = GetRedTokenMove(c[animal])

    override val logMessage: String
        get() = "${animal.fullName} feeds from the base."

    override fun toString(player: Player) = "$animal: take 1 food from base"
    override fun doFeeding(game: Game): GamePhase {
        animal.gainRedToken(game)
        return GamePhase.FOOD_PROPAGATION
    }
}

class BurnFatMove(animal: Animal, private val fatToBurn: Int) : FeedingAnimalMove(animal) {

    override fun clone(c: GameCopier) = BurnFatMove(c[animal], fatToBurn)

    override val logMessage: String
        get() = "${animal.fullName} converts $fatToBurn fat to food."

    override fun doFeeding(game: Game): GamePhase {
        animal.fat -= fatToBurn
        // Fat burning is a special action and as such does not lead to food propagation.
        animal.hasFood += fatToBurn
        return GamePhase.GRAZING
    }

    override fun toString(player: Player) = "$animal: convert $fatToBurn fat to food"
}


data class FeedingPassMove(val player: Player) : FeedingMove() {

    override fun clone(c: GameCopier) = FeedingPassMove(c[player])

    override fun doFeeding(game: Game): GamePhase {
        player.passed = true
        return GamePhase.FEEDING
    }

    override val logMessage: String
        get() = "$player passed from feeding."

    override fun toString(player: Player) = "Pass from feeding"
}


class FeedingMoveSelection(decidingPlayer: Player, moves: List<FeedingMove>)
    : MoveSelection<FeedingMove>(decidingPlayer, moves)
