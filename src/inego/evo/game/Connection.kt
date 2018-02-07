package inego.evo.game

import inego.evo.properties.PairedProperty

data class Connection(val property: PairedProperty, val animal1: Animal, val animal2: Animal)

data class ConnectionMembership(val connection: Connection, val host: Boolean) {
    inline val property: PairedProperty
        get() = connection.property
    inline val other: Animal
        get() = if (host) connection.animal2 else connection.animal1
}
