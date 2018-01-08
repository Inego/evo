package properties.paired.asymmetric

import properties.paired.PairedPropertySide

object SymbiosisProperty : AsymmetricProperty("Symbiosis", SymbiosisHost, SymbiosisGuest) {

}

object SymbiosisHost : PairedPropertySide("Host") {

}

object SymbiosisGuest : PairedPropertySide("Guest") {

}