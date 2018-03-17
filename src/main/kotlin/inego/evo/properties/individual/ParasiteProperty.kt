package inego.evo.properties.individual

import inego.evo.game.Animal
import inego.evo.game.Game
import inego.evo.properties.IndividualProperty
import inego.evo.properties.SingleTarget
import inego.evo.properties.StatModifier

object ParasiteProperty : IndividualProperty("Parasite"), StatModifier {
    override val enumValue: IndividualPropertyEnum
        get() = IndividualPropertyEnum.PARASITE

    override fun getTargets(game: Game): List<SingleTarget> {

        val currentPlayer = game.currentPlayer

        return game.players
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

    override val score: Int
        get() = 3
}