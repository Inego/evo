package inego.evo.properties.individual

import inego.evo.game.Animal
import inego.evo.game.GamePhase
import inego.evo.game.GameState
import inego.evo.game.PlayerState
import inego.evo.game.moves.FeedingMove
import inego.evo.properties.FeedingAction
import inego.evo.properties.IndividualProperty

object PiracyProperty : IndividualProperty("Piracy"), FeedingAction {

    override fun gatherFeedingMoves(animal: Animal, gameState: GameState): List<FeedingMove> =
            if (animal.usedPiracy) emptyList()
            else gameState.players
                    .flatMap { it.animals }
                    .filter { !it.isFed && it.hasFood > 0 }
                    .map { StealFoodMove(animal, it) }
}


class StealFoodMove(animal: Animal, private val victim: Animal) : FeedingMove(animal) {
    override val logMessage: String
        get() = "${animal.fullName} pirated 1 food from ${victim.fullName}"

    override fun doFeeding(gameState: GameState): GamePhase {
        victim.hasFood--
        animal.gainBlueTokens(1)

        animal.usedPiracy = true
        return GamePhase.FOOD_PROPAGATION
    }

    override fun toString(player: PlayerState): String {
        return "$animal steals food from ${player.targetAnimalToString(victim)} (Piracy)"
    }

}

