package inego.evo.game.moves

import inego.evo.cards.ECard
import inego.evo.game.Game
import inego.evo.game.GameCopier
import inego.evo.game.MoveSelection
import inego.evo.game.Player
import inego.evo.properties.*


abstract class DevelopmentMove : Move()


class CreateAnimal(private val card: ECard) : DevelopmentMove() {

    override fun clone(c: GameCopier) = CreateAnimal(card)

    override fun logMessage(player: Player) = "$player creates an animal."

    override fun Game.applyMove(player: Player) {
        player.hand.remove(card)
        player.cardsPlayedAsAnimals += card
        player.newAnimal()
    }

    override fun toString(player: Player) = "$card ⇨ Animal"
}


object DevelopmentPass : DevelopmentMove() {

    override fun clone(c: GameCopier) = this

    override fun logMessage(player: Player) = "$player passes from development."

    override fun Game.applyMove(player: Player) {
        player.passed = true
    }

    override fun toString(player: Player) = "Pass from development"
}

abstract class DevelopmentAddProperty<P : AnimalProperty<P, T>, T : PropertyTarget<T, P>>(
        val card: ECard,
        val property: P,
        val target: T
) : DevelopmentMove() {
    override fun Game.applyMove(player: Player) {
        player.hand.remove(card)
        seenCards += card
        property.applyTo(target, this)
    }
}

class DevelopmentAddIndividualProperty(card: ECard, property: IndividualProperty, target: SingleTarget) :
        DevelopmentAddProperty<IndividualProperty, SingleTarget>(card, property, target) {

    override fun clone(c: GameCopier) =
            DevelopmentAddIndividualProperty(card, property, SingleTarget(c[target.animal]))

    override fun logMessage(player: Player) = "$player adds $property to ${target.animal.fullName}."

    override fun toString(player: Player) =
            "$property ⇨ ${player.targetAnimalToString(target.animal)}"

}

class DevelopmentAddPairedProperty(card: ECard, property: PairedProperty, target: PairedTarget)
    : DevelopmentAddProperty<PairedProperty, PairedTarget>(card, property, target) {

    override fun clone(c: GameCopier) =
            DevelopmentAddPairedProperty(card, property, PairedTarget(c[target.firstAnimal], c[target.secondAnimal]))

    override fun logMessage(player: Player) = "$player adds $property from ${target.firstAnimal} to ${target.secondAnimal}."

    override fun toString(player: Player): String {
        val first = player.targetAnimalToString(target.firstAnimal)
        val second = player.targetAnimalToString(target.secondAnimal)
        return if (property is AsymmetricProperty) "$first $property→ $second" else "$first ←$property→ $second"
    }
}


class DevelopmentMoveSelection(decidingPlayer: Player, moves: List<DevelopmentMove>)
    : MoveSelection<DevelopmentMove>(decidingPlayer, moves)