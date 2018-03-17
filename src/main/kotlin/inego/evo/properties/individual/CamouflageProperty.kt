package inego.evo.properties.individual

import inego.evo.game.Animal
import inego.evo.properties.IndividualProperty

object CamouflageProperty : IndividualProperty("Camouflage") {
    override val enumValue: IndividualPropertyEnum
        get() = IndividualPropertyEnum.CAMOUFLAGE

    override fun mayBeAttackedBy(victim: Animal, attacker: Animal): Boolean {
        if (!attacker.has(SharpVisionProperty)) {
            return false
        }
        return super.mayBeAttackedBy(victim, attacker)
    }

}