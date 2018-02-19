package inego.evo.properties.individual

import inego.evo.game.Animal
import inego.evo.properties.IndividualProperty

object SwimmingProperty : IndividualProperty("Swimming") {

    override fun mayAttack(victim: Animal): Boolean {
        if (!victim.has(SwimmingProperty)) {
            return false
        }
        return super.mayAttack(victim)
    }

    override fun mayBeAttackedBy(victim: Animal, attacker: Animal): Boolean {
        if (!attacker.has(SwimmingProperty)) {
            return false
        }
        return super.mayBeAttackedBy(victim, attacker)
    }
}
