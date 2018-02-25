package inego.evo.game.moves

import inego.evo.cards.Card
import inego.evo.game.GameState
import inego.evo.game.MoveSelection
import inego.evo.game.PlayerState
import inego.evo.properties.*

class CreateAnimal(private  val player: PlayerState, private val card: Card) : DevelopmentMove() {
    override val logMessage: String
        get() = "$player creates an animal."

    override fun GameState.applyMove() {
        currentPlayer.hand.remove(card)
        currentPlayer.newAnimal()
    }

    override fun toString(player: PlayerState) = "$card ⇨ Animal"
}

abstract class DevelopmentMove : Move()


class DevelopmentPass(private val player: PlayerState) : DevelopmentMove() {
    override val logMessage: String
        get() = "$player passes from development."

    override fun GameState.applyMove() {
        currentPlayer.passed = true
    }

    override fun toString(player: PlayerState) = "Pass from development"
}

abstract class DevelopmentAddProperty<P : AnimalProperty<P, T>, T : PropertyTarget<T, P>>(
        private val card: Card,
        val property: P,
        val target: T
) : DevelopmentMove() {
    override fun GameState.applyMove() {
        currentPlayer.hand.remove(card)
        property.applyTo(target, this)
    }
}

class DevelopmentAddIndividualProperty(card: Card, property: IndividualProperty, target: SingleTarget)
    : DevelopmentAddProperty<IndividualProperty, SingleTarget>(card, property, target) {
    override val logMessage: String
        get() = "${target.animal.owner} adds $property to ${target.animal}."

    override fun toString(player: PlayerState) =
            "$property ⇨ ${player.targetAnimalToString(target.animal)}"

}

class DevelopmentAddPairedProperty(card: Card, property: PairedProperty, target: PairedTarget)
    : DevelopmentAddProperty<PairedProperty, PairedTarget>(card, property, target) {
    override val logMessage: String
        get() = "${target.firstAnimal.owner} adds $property from ${target.firstAnimal} to ${target.secondAnimal}."

    override fun toString(player: PlayerState): String {
        val first = player.targetAnimalToString(target.firstAnimal)
        val second = player.targetAnimalToString(target.secondAnimal)
        return if (property is AsymmetricProperty) "$first $property→ $second" else "$first ←$property→ $second"
    }
}


class DevelopmentMoveSelection(decidingPlayer: PlayerState, moves: List<DevelopmentMove>)
    : MoveSelection<DevelopmentMove>(decidingPlayer, moves)