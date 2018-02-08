package inego.evo.game.moves

import inego.evo.cards.Card
import inego.evo.game.GameState
import inego.evo.game.PlayerState
import inego.evo.properties.*

class CreateAnimal(private val card: Card) : Move() {
    override fun GameState.applyMove() {
        currentPlayer.hand.remove(card)
        currentPlayer.newAnimal()
    }

    override fun toString(gameState: GameState, player: PlayerState) = "$card ⇨ Animal"
}

object DevelopmentPass : Move() {
    override fun GameState.applyMove() {
        currentPlayer.passed = true
    }

    override fun toString(gameState: GameState, player: PlayerState) = "Pass from development"
}

abstract class DevelopmentAddProperty<P : AnimalProperty<P, T>, T : PropertyTarget<T, P>>(
        private val card: Card,
        val property: P,
        val target: T
) : Move() {
    override fun GameState.applyMove() {
        currentPlayer.hand.remove(card)
        property.applyTo(target, this)
    }
}

class DevelopmentAddIndividualProperty(card: Card, property: IndividualProperty, target: SingleTarget)
    : DevelopmentAddProperty<IndividualProperty, SingleTarget>(card, property, target) {
    override fun toString(gameState: GameState, player: PlayerState) =
            "$property ⇨ ${player.targetAnimalToString(target.animal)}"

}

class DevelopmentAddPairedProperty(card: Card, property: PairedProperty, target: PairedTarget)
    : DevelopmentAddProperty<PairedProperty, PairedTarget>(card, property, target) {
    override fun toString(gameState: GameState, player: PlayerState): String {
        val first = player.targetAnimalToString(target.firstAnimal)
        val second = player.targetAnimalToString(target.secondAnimal)
        return if (property.isDirected) "$first $property→ $second" else "$first ←$property→ $second"
    }
}
