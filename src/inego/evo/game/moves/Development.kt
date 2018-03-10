package inego.evo.game.moves

import inego.evo.cards.ECard
import inego.evo.game.Game
import inego.evo.game.GameCopier
import inego.evo.game.MoveSelection
import inego.evo.game.Player
import inego.evo.properties.*


abstract class DevelopmentMove : Move()


class CreateAnimal(private val player: Player, private val card: ECard) : DevelopmentMove() {

    override fun clone(c: GameCopier) = CreateAnimal(c[player], card)

    override val logMessage: String
        get() = "$player creates an animal."

    override fun Game.applyMove() {
        currentPlayer.hand.remove(card)
        currentPlayer.cardsPlayedAsAnimals += card
        currentPlayer.newAnimal()
    }

    override fun toString(player: Player) = "$card ⇨ Animal"
}


class DevelopmentPass(private val player: Player) : DevelopmentMove() {

    override fun clone(c: GameCopier) = DevelopmentPass(c[player])

    override val logMessage: String
        get() = "$player passes from development."

    override fun Game.applyMove() {
        currentPlayer.passed = true
    }

    override fun toString(player: Player) = "Pass from development"
}

abstract class DevelopmentAddProperty<P : AnimalProperty<P, T>, T : PropertyTarget<T, P>>(
        val card: ECard,
        val property: P,
        val target: T
) : DevelopmentMove() {
    override fun Game.applyMove() {
        currentPlayer.hand.remove(card)
        seenCards += card
        property.applyTo(target, this)
    }
}

class DevelopmentAddIndividualProperty(card: ECard, property: IndividualProperty, target: SingleTarget) :
        DevelopmentAddProperty<IndividualProperty, SingleTarget>(card, property, target) {

    override fun clone(c: GameCopier) =
            DevelopmentAddIndividualProperty(card, property, SingleTarget(c[target.animal]))

    override val logMessage: String
        get() = "${target.animal.owner} adds $property to ${target.animal}."

    override fun toString(player: Player) =
            "$property ⇨ ${player.targetAnimalToString(target.animal)}"

}

class DevelopmentAddPairedProperty(card: ECard, property: PairedProperty, target: PairedTarget)
    : DevelopmentAddProperty<PairedProperty, PairedTarget>(card, property, target) {

    override fun clone(c: GameCopier) =
            DevelopmentAddPairedProperty(card, property, PairedTarget(c[target.firstAnimal], c[target.secondAnimal]))

    override val logMessage: String
        get() = "${target.firstAnimal.owner} adds $property from ${target.firstAnimal} to ${target.secondAnimal}."

    override fun toString(player: Player): String {
        val first = player.targetAnimalToString(target.firstAnimal)
        val second = player.targetAnimalToString(target.secondAnimal)
        return if (property is AsymmetricProperty) "$first $property→ $second" else "$first ←$property→ $second"
    }
}


class DevelopmentMoveSelection(decidingPlayer: Player, moves: List<DevelopmentMove>)
    : MoveSelection<DevelopmentMove>(decidingPlayer, moves)