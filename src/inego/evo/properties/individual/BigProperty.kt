package inego.evo.properties.individual

import inego.evo.game.Animal
import inego.evo.properties.IndividualProperty

object BigProperty : IndividualProperty("Big") {

    override fun mayBeAttackedBy(victim: Animal, attacker: Animal): Boolean {
        if (!attacker.has(BigProperty)) {
            return false
        }

        return super.mayBeAttackedBy(victim, attacker)
    }
}