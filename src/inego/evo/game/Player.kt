package inego.evo.game

import inego.evo.cards.Card
import inego.evo.game.moves.FoodPropagationMove
import inego.evo.properties.PairedProperty
import inego.evo.properties.paired.FoodPropagator

class Player(val name: String) {

    var passed = false

    val eligibleForDevelopment
        get() = !passed && hand.isNotEmpty()

    val hand: MutableList<Card> = mutableListOf()

    val animals: MutableList<Animal> = mutableListOf()

    val connections: MutableList<Connection> = mutableListOf()

    var foodPropagationSet: MutableSet<ConnectionMembership> = mutableSetOf()

    val cardsToHandOut
        inline get() = if (hand.isEmpty() and animals.isEmpty()) 6 else animals.size + 1

    var discardSize = 0

    private val score
        get() = animals.sumBy { 2 + it.individualProperties.sumBy { it.score } + it.fatCapacity } +
                connections.size

    val result
        get() = PlayerResult(this, score, discardSize)

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
        discardSize++
    }

    fun newAnimal() = Animal(this).also { animals.add(it) }

    override fun toString() = name

    fun targetAnimalToString(targetAnimal: Animal) =
            if (targetAnimal.owner == this) targetAnimal.toString() else targetAnimal.fullName

    fun removeAnimal(animal: Animal) {
        discardSize += 1 + animal.individualProperties.size + animal.fatCapacity

        animal.connections.map { it.connection }.forEach { removeConnection(it) }
        animals.remove(animal)
    }

    fun getFoodPropagationMoves(game: Game): List<FoodPropagationMove> {
        if (foodPropagationSet.isEmpty()) {
            return emptyList()
        }

        val result: MutableList<FoodPropagationMove> = mutableListOf()

        val iterator = foodPropagationSet.iterator()

        for (connectionMembership in iterator) {
            with(connectionMembership.sideProperty as FoodPropagator) {
                val otherAnimal = connectionMembership.other
                if (otherAnimal.mayEat && !connectionMembership.isUsed && isApplicable(game)) {
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


class PlayerResult(val player: Player, val score: Int, val discard: Int): Comparable<PlayerResult> {
    override fun compareTo(other: PlayerResult) = compareValuesBy(this, other, { it.score }, { it.discard })
}
