package inego.evo.properties.individual

import inego.evo.game.Animal
import inego.evo.properties.IndividualProperty

object BurrowingProperty : IndividualProperty("Burrowing") {
    override val enumValue: IndividualPropertyEnum
        get() = IndividualPropertyEnum.BURROWING

    override fun mayBeAttackedBy(victim: Animal, attacker: Animal): Boolean {
        if (victim.isFed) {
            return false
        }
        return super.mayBeAttackedBy(victim, attacker)
    }

}