package inego.evo.properties.individual

import inego.evo.game.Animal
import inego.evo.properties.IndividualProperty
import inego.evo.properties.StatModifier

object BigProperty : IndividualProperty("Big"), StatModifier {

    override fun mayBeAttackedBy(victim: Animal, attacker: Animal): Boolean {
        if (!attacker.has(BigProperty)) {
            return false
        }

        return super.mayBeAttackedBy(victim, attacker)
    }

    override fun onAttach(animal: Animal) {
        animal.foodRequirement++
    }

    override fun onDetach(animal: Animal) {
        animal.foodRequirement--
    }
}