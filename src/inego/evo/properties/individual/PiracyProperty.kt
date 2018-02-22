package inego.evo.properties.individual

import inego.evo.game.Animal
import inego.evo.game.GameState
import inego.evo.game.PlayerState
import inego.evo.game.moves.FeedingMove
import inego.evo.properties.FeedingAction
import inego.evo.properties.IndividualProperty

object PiracyProperty : IndividualProperty("Piracy"), FeedingAction {

    override fun gatherMoves(animal: Animal, gameState: GameState): List<FeedingMove> {

        if (animal.hasPirated)
            return emptyList()

        return gameState.players
                .flatMap { it.animals }
                .filter { !it.isFed && it.hasFood > 0 }
                .map { StealFoodMove(animal, it) }
    }

}


class StealFoodMove(animal: Animal, val victim: Animal) : FeedingMove(animal) {
    override fun doFeeding(gameState: GameState) {
        animal.hasFood++
        victim.hasFood--

        animal.hasPirated = true

    }

    override fun toString(gameState: GameState, player: PlayerState): String {
        return "$animal steals food from ${player.targetAnimalToString(victim)} (Piracy)"
    }

}

