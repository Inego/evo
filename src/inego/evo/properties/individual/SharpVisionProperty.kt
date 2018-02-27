package inego.evo.properties.individual

import inego.evo.properties.IndividualProperty

object SharpVisionProperty : IndividualProperty("Sharp vision") {
    override val enumValue: IndividualPropertyEnum
        get() = IndividualPropertyEnum.SHARP_VISION
}