package properties.paired.symmetric

import properties.paired.PairedPropertySide

object CommunicationProperty : SymmetricProperty("Communication", Communicator) {
}

object Communicator : PairedPropertySide("Communicator")