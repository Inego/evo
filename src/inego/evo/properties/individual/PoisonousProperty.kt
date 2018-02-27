package inego.evo.properties.individual

import inego.evo.properties.IndividualProperty

object PoisonousProperty : IndividualProperty("Poisonous") {
    override val enumValue: IndividualPropertyEnum
        get() = IndividualPropertyEnum.POISONOUS
}
