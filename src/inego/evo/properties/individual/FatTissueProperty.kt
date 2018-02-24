package inego.evo.properties.individual

import inego.evo.game.Animal
import inego.evo.properties.IndividualProperty
import inego.evo.properties.StatModifier

object FatTissueProperty : IndividualProperty("Fat Tissue"), StatModifier {
    override fun onAttach(animal: Animal) {
        animal.fatCapacity++
    }

    override fun onDetach(animal: Animal) {
        animal.fatCapacity--
    }

    override fun mayAttachTo(animal: Animal) = true

}