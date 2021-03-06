package inego.evo.game

import inego.evo.properties.AsymmetricProperty
import inego.evo.properties.PairedProperty
import inego.evo.properties.SymmetricProperty
import inego.evo.properties.paired.PairedPropertySide

data class Connection(
        val property: PairedProperty,
        val animal1: Animal,
        val animal2: Animal,
        var isUsed: Boolean
)

data class ConnectionMembership(val connection: Connection, val host: Boolean) {
    inline val property: PairedProperty
        get() = connection.property

    inline val other: Animal
        get() = if (host) connection.animal2 else connection.animal1

    inline val thisAnimal: Animal
        get() = if (host) connection.animal1 else connection.animal2

    inline val sideProperty: PairedPropertySide
        get() {
            return when (val p = connection.property) {
                is SymmetricProperty -> p.side
                is AsymmetricProperty -> if (host) p.host else p.guest
            }
        }

    var isUsed: Boolean
        inline get() = connection.isUsed
        inline set(value) {
            connection.isUsed = value
        }
}
