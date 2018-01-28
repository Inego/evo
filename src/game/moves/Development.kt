package game.moves

import cards.Card
import game.Animal
import game.GameState
import properties.AnimalProperty
import properties.IndividualProperty
import properties.PropertyTarget

class CreateAnimal(private val card: Card) : Move() {
    override fun GameState.applyMove() {
        currentPlayer.hand.remove(card)
        currentPlayer.animals.add(Animal())
    }
}

object DevelopmentPass : Move() {
    override fun GameState.applyMove() {
        currentPlayer.passed = true
    }
}

class DevelopmentAddProperty<P: AnimalProperty<P, T>, T: PropertyTarget<T, P>>(
        private val card: Card,
        private val property: P,
        private val target: T
): Move() {
    override fun GameState.applyMove() {
        currentPlayer.hand.remove(card)
        property.applyTo(target, this)
    }
}
