package inego.evo.properties.individual

import inego.evo.game.Animal
import inego.evo.game.GameState
import inego.evo.properties.IndividualProperty
import inego.evo.properties.SingleTarget
import inego.evo.properties.StatModifier

object ParasiteProperty : IndividualProperty("Parasite"), StatModifier {

    override fun getTargets(gameState: GameState): List<SingleTarget> {

        val currentPlayer = gameState.currentPlayer

        return gameState.players
                .filter { it != currentPlayer }
                .flatMap { it.animals.filter { mayAttachTo(it) } }
                .map { SingleTarget(it) }
    }

    override fun onAttach(animal: Animal) {
        animal.foodRequirement += 2
    }

    override fun onDetach(animal: Animal) {
        animal.foodRequirement -= 2
    }
}