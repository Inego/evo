package inego.evo.game.moves

import inego.evo.cards.Card
import inego.evo.game.GameState
import inego.evo.properties.*

class CreateAnimal(private val card: Card) : Move() {
    override fun GameState.applyMove() {
        currentPlayer.hand.remove(card)
        currentPlayer.newAnimal()
    }

    override fun toString() = "Create animal from $card"
}

object DevelopmentPass : Move() {
    override fun GameState.applyMove() {
        currentPlayer.passed = true
    }

    override fun toString() = "Pass from development"
}

abstract class DevelopmentAddProperty<P : AnimalProperty<P, T>, T : PropertyTarget<T, P>>(
        private val card: Card,
        private val property: P,
        private val target: T
) : Move() {
    override fun GameState.applyMove() {
        currentPlayer.hand.remove(card)
        property.applyTo(target, this)
    }
}

class DevelopmentAddIndividualProperty(card: Card, property: IndividualProperty, target: SingleTarget)
    : DevelopmentAddProperty<IndividualProperty, SingleTarget>(card, property, target)

class DevelopmentAddPairedProperty(card: Card, property: PairedProperty, target: PairedTarget)
    : DevelopmentAddProperty<PairedProperty, PairedTarget>(card, property, target)
