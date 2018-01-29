package properties

import cards.Card
import game.Animal
import game.GameState
import game.moves.DevelopmentAddIndividualProperty
import game.moves.DevelopmentAddPairedProperty
import game.moves.Move

sealed class AnimalProperty<P : AnimalProperty<P, T>, T : PropertyTarget<T, P>>(val name: String) {

    fun getDevelopmentMoves(gameState: GameState, card: Card): List<Move> {
        return getTargets(gameState).map { createDevelopmentMove(card, it) }
    }

    abstract fun getTargets(gameState: GameState): List<T>

    protected abstract fun createDevelopmentMove(card: Card, target: T): Move

    abstract fun applyTo(target: T, gameState: GameState)
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

abstract class MutuallyExclusiveIndividualProperty(
        name: String,
        private val otherProperty: IndividualProperty
) : IndividualProperty(name) {
    override fun mayAttachTo(animal: Animal): Boolean {
        return super.mayAttachTo(animal) && !animal.has(otherProperty)
    }
}

abstract class PairedProperty(name: String) : AnimalProperty<PairedProperty, PairedTarget>(name) {
    override fun getTargets(gameState: GameState): List<PairedTarget> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
