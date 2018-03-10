package inego.evo.properties.individual

import inego.evo.game.*
import inego.evo.game.moves.DefenseMove
import inego.evo.properties.DefenseAction
import inego.evo.properties.IndividualProperty

object TailLossProperty : IndividualProperty("Tail Loss"), DefenseAction {
    override val enumValue: IndividualPropertyEnum
        get() = IndividualPropertyEnum.TAIL_LOSS

    override fun gatherDefenseMoves(defender: Animal, attacker: Animal, game: Game): List<DefenseMove> {
        val result: MutableList<DefenseMove> = mutableListOf()

        if (defender.fatCapacity > 0)
            result.add(LoseIndividualProperty(defender, attacker, FatTissueProperty))

        defender.individualProperties
                .mapTo(result) { LoseIndividualProperty(defender, attacker, it.individualProperty) }

        defender.connections.mapTo(result) { LosePairedProperty(defender, attacker, it) }

        return result
    }

}

class LoseIndividualProperty(defender: Animal, attacker: Animal, val property: IndividualProperty) :
        DefenseMove(defender, attacker) {

    override fun clone(c: GameCopier) = LoseIndividualProperty(c[defender], c[attacker], property)

    override fun logMessage(player: Player) = "${defender.fullName} loses $property as a tail"

    override fun Game.applyMove(player: Player) {
        defendingAnimal!!.removeProperty(property)

        // Attack fails, but the attacker has "the tail" (1 blue token)
        attacker.gainBlueTokens(1)
        phase = GamePhase.FOOD_PROPAGATION
    }

    override fun toString(player: Player) =
            "$defender loses $property (tail loss)"

}

class LosePairedProperty(defender: Animal, attacker: Animal, private val connectionMembership: ConnectionMembership)
    : DefenseMove(defender, attacker) {
    override fun clone(c: GameCopier) = LosePairedProperty(
            c[defender],
            c[attacker],
            c[connectionMembership]
    )

    override fun logMessage(player: Player) = "${defender.fullName} loses ${connectionMembership.sideProperty} " +
                "to ${connectionMembership.other.fullName} as a tail."

    override fun Game.applyMove(player: Player) {
        defender.owner.removeConnection(connectionMembership.connection)
    }

    override fun toString(player: Player): String {
        return "$defender loses ${connectionMembership.sideProperty} to ${connectionMembership.other} (tail loss)"
    }
}