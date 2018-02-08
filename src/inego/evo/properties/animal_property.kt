package inego.evo.properties

import inego.evo.cards.Card
import inego.evo.game.Animal
import inego.evo.game.GameState
import inego.evo.game.moves.DevelopmentAddIndividualProperty
import inego.evo.game.moves.DevelopmentAddPairedProperty
import inego.evo.game.moves.Move

sealed class AnimalProperty<P : AnimalProperty<P, T>, T : PropertyTarget<T, P>>(val name: String) {

    fun getDevelopmentMoves(gameState: GameState, card: Card): List<Move> {
        return getTargets(gameState).map { createDevelopmentMove(card, it) }
    }

    abstract fun getTargets(gameState: GameState): List<T>

    protected abstract fun createDevelopmentMove(card: Card, target: T): Move

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

    override fun createDevelopmentMove(card: Card, target: SingleTarget): Move {
        return DevelopmentAddIndividualProperty(card, this, target)
    }
}


abstract class PairedProperty(name: String) : AnimalProperty<PairedProperty, PairedTarget>(name) {

    abstract val isDirected: Boolean

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

                    if (isDirected)
                        result.add(PairedTarget(animal2, animal1))
                }
            }
        }

        return result
    }

    override fun applyTo(target: PairedTarget, gameState: GameState) {
        val player = gameState.currentPlayer
        player.addConnection(this, target.firstAnimal, target.secondAnimal)
    }

    override fun createDevelopmentMove(card: Card, target: PairedTarget): Move {
        return DevelopmentAddPairedProperty(card, this, target)
    }
}


sealed class PropertyTarget<T : PropertyTarget<T, P>, P : AnimalProperty<P, T>>


data class SingleTarget(val animal: Animal) : PropertyTarget<SingleTarget, IndividualProperty>()


data class PairedTarget(val firstAnimal: Animal, val secondAnimal: Animal) : PropertyTarget<PairedTarget, PairedProperty>()
