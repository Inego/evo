package inego.evo.properties.paired.symmetric

import inego.evo.properties.SymmetricProperty
import inego.evo.properties.paired.PairedPropertySide

object CooperationProperty : SymmetricProperty("Cooperation", Cooperator) {
}

object Cooperator : PairedPropertySide("Cooperator")
