package inego.evo.properties.individual

import inego.evo.game.*
import inego.evo.game.moves.FeedingAnimalMove
import inego.evo.game.moves.FeedingMove
import inego.evo.properties.FeedingAction
import inego.evo.properties.IndividualProperty

object PiracyProperty : IndividualProperty("Piracy"), FeedingAction {
    override val enumValue: IndividualPropertyEnum
        get() = IndividualPropertyEnum.PIRACY

    override fun gatherFeedingMoves(animal: Animal, game: Game): List<FeedingMove> =
            if (animal.usedPiracy) emptyList()
            else game.players
                    .flatMap { it.animals }
                    .filter { it != animal && !it.isFed && it.hasFood > 0 }
                    .map { StealFoodMove(animal, it) }
}


class StealFoodMove(animal: Animal, private val victim: Animal) : FeedingAnimalMove(animal) {
    override fun clone(c: GameCopier) = StealFoodMove(c[animal], c[victim])

    override fun logMessage(player: Player) = "${animal.fullName} pirated 1 food from ${victim.fullName}"

    override fun doFeeding(game: Game, player: Player): GamePhase {
        victim.hasFood--
        animal.gainBlueTokens(1)

        animal.usedPiracy = true
        return GamePhase.FOOD_PROPAGATION
    }

    override fun toString(player: Player): String {
        return "$animal steals food from ${player.targetAnimalToString(victim)} (Piracy)"
    }

}

