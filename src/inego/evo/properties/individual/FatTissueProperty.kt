package inego.evo.properties.individual

import inego.evo.game.Animal
import inego.evo.game.GameState
import inego.evo.properties.IndividualProperty
import inego.evo.properties.SingleTarget

object FatTissueProperty : IndividualProperty("Fat Tissue") {

    override fun mayAttachTo(animal: Animal) = true

    override fun applyTo(target: SingleTarget, gameState: GameState) {
        target.animal.fatCapacity++
    }
}