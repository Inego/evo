package inego.evo.game

import inego.evo.cards.CardQuantities
import inego.evo.cards.ECard
import inego.evo.game.moves.FoodPropagationMove
import inego.evo.properties.PairedProperty
import inego.evo.properties.individual.GrazingProperty
import inego.evo.properties.paired.FoodPropagator


/**
 * An active model of player state providing some of the game logic.
 */
class Player private constructor(
        val name: String,
        var passed: Boolean,
        var discardSize: Int,
        val hand: MutableList<ECard>,
        val cardsPlayedAsAnimals: CardQuantities,
        var grazingPower: Int
) {
    val animals: MutableList<Animal> = mutableListOf()
    val connections: MutableList<Connection> = mutableListOf()
    var foodPropagationSet: MutableSet<ConnectionMembership> = mutableSetOf()

    val eligibleForDevelopment
        get() = !passed && hand.isNotEmpty()

    val cardsToHandOut
        inline get() = if (hand.isEmpty() and animals.isEmpty()) 6 else animals.size + 1

    /**
     * Returns the current game score of the player.
     */
    val score
        get() = animals.sumBy { animal ->
            2 + animal.individualProperties.sumBy {
                it.individualProperty.score
            } + animal.fatCapacity
        } + connections.size

    val result
        get() = PlayerResult(this, score, discardSize)

    /**
     * Creates a clone of this player using the provided copier.
     */
    fun clone(gameCopier: GameCopier): Player {

        val exactCopy = this == gameCopier.forPlayer

        val copiedHand =
                if (exactCopy) hand.toMutableList()
                else gameCopier.takeUnseenCards(hand.size)

        val copiedCardsPlayedAsAnimals =
                if (exactCopy) cardsPlayedAsAnimals.clone()
                else {
                    val result = CardQuantities.new()
                    val count: Int = cardsPlayedAsAnimals.totalCount
                    repeat(count) {
                        result += gameCopier.unseenCards.next()
                    }
                    result
                }

        val copiedPlayer = Player(
                name,
                passed,
                discardSize,
                copiedHand,
                copiedCardsPlayedAsAnimals,
                grazingPower
        )

        for (srcAnimal in animals) {
            val copiedAnimal = srcAnimal.clone(copiedPlayer)
            copiedPlayer.animals.add(copiedAnimal)
            gameCopier[srcAnimal] = copiedAnimal
        }

        // Second pass: copiedGame connections and memberships

        val copiedConnections: MutableMap<Connection, Connection> = mutableMapOf()

        for (srcAnimal in animals) {
            for (srcMembership in srcAnimal.connections) {
                val copiedAnimal = gameCopier[srcAnimal]

                val srcConnection = srcMembership.connection

                var copiedConnection = copiedConnections[srcConnection]

                if (copiedConnection == null) {

                    copiedConnection = if (srcMembership.host) {
                        Connection(
                                srcConnection.property,
                                copiedAnimal,
                                gameCopier[srcConnection.animal2],
                                srcConnection.isUsed
                        )
                    } else {
                        Connection(
                                srcConnection.property,
                                gameCopier[srcConnection.animal1],
                                copiedAnimal,
                                srcConnection.isUsed
                        )
                    }
                    copiedPlayer.connections.add(copiedConnection)
                    copiedConnections[srcConnection] = copiedConnection
                }

                val copiedMembership = ConnectionMembership(copiedConnection, srcMembership.host)
                copiedAnimal.connections.add(copiedMembership)
                gameCopier[srcMembership] = copiedMembership
            }
        }

        foodPropagationSet.mapTo(copiedPlayer.foodPropagationSet) { gameCopier[it] }

        return copiedPlayer
    }

    fun addConnection(
            pairedProperty: PairedProperty,
            firstAnimal: Animal,
            secondAnimal: Animal,
            isUsed: Boolean = false
    ) {
        val connection = Connection(pairedProperty, firstAnimal, secondAnimal, isUsed)
        connections.add(connection)
        val m1 = ConnectionMembership(connection, true)
        val m2 = ConnectionMembership(connection, false)
        firstAnimal.connections.add(m1)
        secondAnimal.connections.add(m2)
    }

    fun removeConnection(connection: Connection) {
        connection.animal1.connections.removeIf { it.connection == connection }
        connection.animal2.connections.removeIf { it.connection == connection }
        foodPropagationSet.removeIf { it.connection == connection }
        connections.remove(connection)
        discardSize++
    }

    fun newAnimal() = Animal.new(this).also { animals.add(it) }

    override fun toString() = name

    fun targetAnimalToString(targetAnimal: Animal) =
            if (targetAnimal.owner == this) targetAnimal.toString() else targetAnimal.fullName

    fun removeAnimal(animal: Animal) {
        discardSize += 1 + animal.individualProperties.size + animal.fatCapacity

        animal.connections.map { it.connection }.forEach { removeConnection(it) }

        if (animal.has(GrazingProperty)) {
            grazingPower--
        }

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

    companion object {
        /**
         * Factory method to create player instances.
         */
        fun new(name: String): Player = Player(
                name,
                false,
                0,
                mutableListOf(),
                CardQuantities.new(),
                0
        )
    }
}

/**
 * Data of a given player sufficient to determine game winners by comparing it other players' results.
 */
class PlayerResult(val player: Player, private val score: Int, private val discard: Int) : Comparable<PlayerResult> {
    override fun compareTo(other: PlayerResult) = compareValuesBy(this, other, { it.score }, { it.discard })
}
