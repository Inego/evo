package game

import cards.Card
import properties.PairedProperty

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

    fun clearAnimals() {
        animals.clear()
        connections.clear()
    }

    fun newAnimal() = Animal(this).also { animals.add(it) }

    override fun toString() = name
}