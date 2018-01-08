package properties.paired.symmetric

import properties.PairedProperty
import properties.paired.PairedPropertySide

abstract class SymmetricProperty(name: String, val side: PairedPropertySide) : PairedProperty(name)
{
}