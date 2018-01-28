package properties

import game.Animal
import game.Connection
import game.GameState

sealed class AnimalProperty<P: AnimalProperty<P, T>, T: PropertyTarget<T, P>>(val name: String) {
    abstract fun getTargets(gameState: GameState): List<T>
    abstract fun applyTo(target: T, gameState: GameState)
}

abstract class IndividualProperty(name: String) : AnimalProperty<IndividualProperty, SingleTarget>(name) {

    protected open fun mayAttachTo(animal: Animal) = !animal.has(this)

    override fun getTargets(gameState: GameState): List<SingleTarget> =
            gameState.currentPlayer.animals.filter { mayAttachTo(it) }.map { SingleTarget(it) }

    override fun applyTo(target: SingleTarget, gameState: GameState) {
        target.animal.addProperty(this)
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
}


sealed class PropertyTarget<T: PropertyTarget<T, P>, P: AnimalProperty<P, T>> {
    abstract fun applyWith(property: P, gameState: GameState)
}

data class SingleTarget(val animal: Animal) : PropertyTarget<SingleTarget, IndividualProperty>() {
    override fun applyWith(property: IndividualProperty, gameState: GameState) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

data class PairedTarget(val firstAnimal: Animal, val secondAnimal: Animal) : PropertyTarget<PairedTarget, PairedProperty>() {
    override fun applyWith(property: PairedProperty, gameState: GameState) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}