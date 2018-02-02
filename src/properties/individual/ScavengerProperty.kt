package properties.individual

import game.Animal
import properties.IndividualProperty

object ScavengerProperty : IndividualProperty("Scavenger") {

    override fun mayAttachTo(animal: Animal): Boolean {
        return super.mayAttachTo(animal) && !animal.has(CarnivorousProperty)
    }

}