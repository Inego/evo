package inego.evo.game

import inego.evo.cards.Card
import inego.evo.properties.PairedProperty

class PlayerState(val name: String) {

    var passed = false

    val hand: MutableList<Card> = mutableListOf()

    val animals: MutableList<Animal> = mutableListOf()

    private val connections: MutableList<Connection> = mutableListOf()

    val cardsToHandOut
        inline get() = if (hand.isEmpty() and animals.isEmpty()) 6 else animals.size + 1

    fun addConnection(pairedProperty: PairedProperty, firstAnimal: Animal, secondAnimal: Animal) {
        val connection = Connection(pairedProperty, firstAnimal, secondAnimal)
        connections.add(connection)
        firstAnimal.connections.add(ConnectionMembership(connection, true))
        secondAnimal.connections.add(ConnectionMembership(connection, false))
    }

    fun removeConnection(connection: Connection) {
        connection.animal1.connections.removeIf { it.connection == connection }
        connection.animal2.connections.removeIf { it.connection == connection }
        connections.remove(connection)
    }

    fun clearAnimals() {
        animals.clear()
        connections.clear()
    }

    fun newAnimal() = Animal(this).also { animals.add(it) }

    override fun toString() = name

    fun targetAnimalToString(targetAnimal: Animal) = if (targetAnimal.owner == this) targetAnimal.toString() else "${targetAnimal.owner}'s $targetAnimal"
}