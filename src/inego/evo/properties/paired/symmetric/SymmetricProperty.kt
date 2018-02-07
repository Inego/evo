package inego.evo.properties.paired.symmetric

import inego.evo.properties.PairedProperty
import inego.evo.properties.paired.PairedPropertySide

abstract class SymmetricProperty(name: String, val side: PairedPropertySide) : PairedProperty(name)
{
    override val isDirected: Boolean = false
}