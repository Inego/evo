package inego.evo.properties

import inego.evo.cards.Card
import inego.evo.game.Animal
import inego.evo.game.GameState
import inego.evo.game.moves.DevelopmentAddIndividualProperty
import inego.evo.game.moves.DevelopmentAddPairedProperty
import inego.evo.game.moves.DevelopmentMove
import inego.evo.properties.paired.PairedPropertySide

sealed class AnimalProperty<P : AnimalProperty<P, T>, T : PropertyTarget<T, P>>(val name: String) {

    fun getDevelopmentMoves(gameState: GameState, card: Card): List<DevelopmentMove> {
        return getTargets(gameState).map { createDevelopmentMove(card, it) }
    }

    abstract fun getTargets(gameState: GameState): List<T>

    protected abstract fun createDevelopmentMove(card: Card, target: T): DevelopmentMove

    abstract fun applyTo(target: T, gameState: GameState)

    override fun toString() = name
}

abstract class IndividualProperty(name: String) : AnimalProperty<IndividualProperty, SingleTarget>(name) {

    protected open fun mayAttachTo(animal: Animal) = !animal.has(this)

    override fun getTargets(gameState: GameState): List<SingleTarget> =
            gameState.currentPlayer.animals.filter { mayAttachTo(it) }.map { SingleTarget(it) }

    override fun applyTo(target: SingleTarget, gameState: GameState) {
        target.animal.addProperty(this)
    }

    override fun createDevelopmentMove(card: Card, target: SingleTarget): DevelopmentMove {
        return DevelopmentAddIndividualProperty(card, this, target)
    }

    open fun mayAttack(victim: Animal) = true

    open fun mayBeAttackedBy(victim: Animal, attacker: Animal) = true
}

class IndividualPropertyPossession(val individualProperty: IndividualProperty) {
    var isUsed = false
}


sealed class PairedProperty(name: String) : AnimalProperty<PairedProperty, PairedTarget>(name) {

    override fun getTargets(gameState: GameState): List<PairedTarget> {

        val result = mutableListOf<PairedTarget>()

        val player = gameState.currentPlayer
        val animals = player.animals
        for (i in 0 until animals.size - 1) {
            val animal1 = animals[i]
            for (j in i + 1 until animals.size) {
                val animal2 = animals[j]

                // animal1 and animal2 may NOT have this property,
                // if they are already connected by it (regardless of direction)
                if (animal1.connections.filter {
                            it.property == this
                            && it.other == animal2
                        }.none()) {

                    result.add(PairedTarget(animal1, animal2))

                    if (this is AsymmetricProperty) {
                        result.add(PairedTarget(animal2, animal1))
                    }
                }
            }
        }

        return result
    }

    override fun applyTo(target: PairedTarget, gameState: GameState) {
        val player = gameState.currentPlayer
        player.addConnection(this, target.firstAnimal, target.secondAnimal)
    }

    override fun createDevelopmentMove(card: Card, target: PairedTarget): DevelopmentMove {
        return DevelopmentAddPairedProperty(card, this, target)
    }
}

abstract class SymmetricProperty(name: String, val side: PairedPropertySide) : PairedProperty(name)


abstract class AsymmetricProperty(
        name: String,
        val host: PairedPropertySide,
        val guest: PairedPropertySide
) : PairedProperty(name)


sealed class PropertyTarget<T : PropertyTarget<T, P>, P : AnimalProperty<P, T>>


data class SingleTarget(val animal: Animal) : PropertyTarget<SingleTarget, IndividualProperty>()


data class PairedTarget(val firstAnimal: Animal, val secondAnimal: Animal) : PropertyTarget<PairedTarget, PairedProperty>()
