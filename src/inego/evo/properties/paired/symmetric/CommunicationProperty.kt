package inego.evo.properties.paired.symmetric

import inego.evo.properties.paired.PairedPropertySide

object CommunicationProperty : SymmetricProperty("Communication", Communicator) {
}

object Communicator : PairedPropertySide("Communicator")