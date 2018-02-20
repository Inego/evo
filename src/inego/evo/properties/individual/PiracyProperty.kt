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

        gameState.players
                .flatMap { it.animals }
                .filter { !it.isFed && it.hasFood > 0 }
                .map {}


    }

    override fun performFeedingAction(gameState: GameState) {
        // to be deleted
    }
}


class StealFoodMove(val pirate: Animal, val victim: Animal) : FeedingMove() {
    override fun doFeeding(gameState: GameState) {
        pirate.hasFood++
        victim.hasFood--

        pirate.hasPirated = true

    }

    override fun toString(gameState: GameState, player: PlayerState): String {
        return "$pirate steals food from ${player.targetAnimalToString(victim)} (Piracy)"
    }

}

