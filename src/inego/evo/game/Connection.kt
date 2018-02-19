package inego.evo.game

import inego.evo.properties.AsymmetricProperty
import inego.evo.properties.PairedProperty
import inego.evo.properties.SymmetricProperty
import inego.evo.properties.paired.PairedPropertySide

data class Connection(val property: PairedProperty, val animal1: Animal, val animal2: Animal)

data class ConnectionMembership(val connection: Connection, val host: Boolean) {
    inline val property: PairedProperty
        get() = connection.property
    inline val other: Animal
        get() = if (host) connection.animal2 else connection.animal1

    inline val sideProperty: PairedPropertySide
        get() {
            val p = connection.property
            return when (p) {
                is SymmetricProperty -> p.side
                is AsymmetricProperty -> if (host) p.host else p.guest
            }
        }
}
