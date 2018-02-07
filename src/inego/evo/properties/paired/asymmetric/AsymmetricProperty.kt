package inego.evo.properties.paired.asymmetric

import inego.evo.properties.PairedProperty
import inego.evo.properties.paired.PairedPropertySide

abstract class AsymmetricProperty(
        name: String,
        val host: PairedPropertySide,
        val guest: PairedPropertySide
) : PairedProperty(name) {

    override val isDirected: Boolean = true
}