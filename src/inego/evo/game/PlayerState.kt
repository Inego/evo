package inego.evo.game

import inego.evo.cards.Card
import inego.evo.game.moves.FoodPropagationMove
import inego.evo.properties.PairedProperty
import inego.evo.properties.paired.FoodPropagator

class PlayerState(val name: String) {

    var passed = false

    val eligibleForDevelopment
        get() = !passed && hand.isNotEmpty()

    val hand: MutableList<Card> = mutableListOf()

    val animals: MutableList<Animal> = mutableListOf()

    val connections: MutableList<Connection> = mutableListOf()

    var foodPropagationSet: MutableSet<ConnectionMembership> = mutableSetOf()

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

    fun targetAnimalToString(targetAnimal: Animal) =
            if (targetAnimal.owner == this) targetAnimal.toString() else targetAnimal.fullName

    fun removeAnimal(animal: Animal) {
        animal.connections.map { it.connection }.forEach { removeConnection(it) }
        animals.remove(animal)
    }

    fun getFoodPropagationMoves(gameState: GameState): List<FoodPropagationMove> {
        if (foodPropagationSet.isEmpty()) {
            return emptyList()
        }

        val result: MutableList<FoodPropagationMove> = mutableListOf()

        val iterator = foodPropagationSet.iterator()

        for (connectionMembership in iterator) {
            with(connectionMembership.sideProperty as FoodPropagator) {
                val otherAnimal = connectionMembership.other
                if (otherAnimal.mayEat && !connectionMembership.isUsed && isApplicable(gameState)) {
                    result.add(createPropagationMove(connectionMembership))
                } else {
                    // This connection membership is no longer valid for food propagation - remove it
                    iterator.remove()
                }
            }
        }

        return result
    }
}