package inego.evo.properties.individual

import inego.evo.game.Animal
import inego.evo.properties.IndividualProperty

object ScavengerProperty : IndividualProperty("Scavenger") {

    override fun mayAttachTo(animal: Animal): Boolean {
        return super.mayAttachTo(animal) && !animal.has(CarnivorousProperty)
    }

    // TODO find and feed one scavenger in the "attack succeeded" logic in GameState + "decide whom" choice

}