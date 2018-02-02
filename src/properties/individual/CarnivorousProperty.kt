package properties.individual

import game.Animal
import properties.IndividualProperty

object CarnivorousProperty : IndividualProperty("Carnivorous") {

    override fun mayAttachTo(animal: Animal): Boolean {
        return super.mayAttachTo(animal) && !animal.has(ScavengerProperty)
    }

}