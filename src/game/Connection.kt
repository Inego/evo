package game

import properties.PairedProperty

data class Connection(val property: PairedProperty, val animal1: Animal, val animal2: Animal)

data class ConnectionMembership(val connection: Connection, val host: Boolean)


