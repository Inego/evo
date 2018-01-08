package properties.paired.asymmetric

import properties.PairedProperty
import properties.paired.PairedPropertySide

abstract class AsymmetricProperty(
        name: String,
        val host: PairedPropertySide,
        val guest: PairedPropertySide
) : PairedProperty(name) {


}