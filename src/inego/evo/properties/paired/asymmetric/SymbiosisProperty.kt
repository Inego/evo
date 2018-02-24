package inego.evo.properties.paired.asymmetric

import inego.evo.game.Animal
import inego.evo.properties.AsymmetricProperty
import inego.evo.properties.paired.PairedPropertySide

object SymbiosisProperty : AsymmetricProperty("Symbiosis", SymbiosisHost, SymbiosisGuest)

object SymbiosisHost : PairedPropertySide("Host")


object SymbiosisGuest : PairedPropertySide("Guest") {

    override fun mayBeAttackedBy(attacker: Animal, other: Animal) = false

    override fun mayEat(other: Animal) = other.isFed
}