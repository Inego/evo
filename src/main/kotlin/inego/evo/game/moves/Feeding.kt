package inego.evo.game.moves

import inego.evo.game.*

abstract class FeedingMove : Move() {
    override fun Game.applyMove(player: Player) {
        phase = doFeeding(this, player)
    }

    abstract fun doFeeding(game: Game, player: Player): GamePhase
}


abstract class FeedingAnimalMove(val animal: Animal) : FeedingMove()


class GetRedTokenMove(animal: Animal) : FeedingAnimalMove(animal) {

    override fun clone(c: GameCopier) = GetRedTokenMove(c[animal])

    override fun logMessage(player: Player) = "${animal.fullName} feeds from the base."

    override fun toString(player: Player) = "$animal: take 1 food from base"
    override fun doFeeding(game: Game, player: Player): GamePhase {
        animal.gainRedToken(game)
        game.inactivePlayers = 0
        return GamePhase.FOOD_PROPAGATION
    }
}

class BurnFatMove(animal: Animal, private val fatToBurn: Int) : FeedingAnimalMove(animal) {

    override fun clone(c: GameCopier) = BurnFatMove(c[animal], fatToBurn)

    override fun logMessage(player: Player) = "${animal.fullName} converts $fatToBurn fat to food."

    override fun doFeeding(game: Game, player: Player): GamePhase {
        animal.fat -= fatToBurn
        // Fat burning is a special action and as such does not lead to food propagation.
        animal.hasFood += fatToBurn
        game.inactivePlayers = 0
        return GamePhase.GRAZING
    }

    override fun toString(player: Player) = "$animal: convert $fatToBurn fat to food"
}


object FeedingPassMove : FeedingMove() {

    override fun clone(c: GameCopier) = this

    override fun doFeeding(game: Game, player: Player): GamePhase {
        // Note that this is the only feeding move that does not reset inactivePlayers
        return GamePhase.GRAZING
    }

    override fun logMessage(player: Player) = "$player passed from feeding."

    override fun toString(player: Player) = "Pass from feeding"
}


class FeedingMoveSelection(decidingPlayer: Player, moves: List<FeedingMove>)
    : MoveSelection<FeedingMove>(decidingPlayer, moves)
