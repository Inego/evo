package inego.evo.properties.individual

import inego.evo.game.*
import inego.evo.properties.IndividualProperty
import inego.evo.properties.DefenseAction
import inego.evo.properties.DefenseMove

object TailLossProperty : IndividualProperty("Tail Loss"), DefenseAction {
    override fun gatherDefenseMoves(defender: Animal, attacker: Animal, gameState: GameState): List<DefenseMove> {
        val result : MutableList<DefenseMove> = mutableListOf()

        if (defender.fatCapacity > 0)
            result.add(LoseIndividualProperty(defender, attacker, FatTissueProperty))

        defender.individualProperties.mapTo(result) { LoseIndividualProperty(defender, attacker, it) }

        defender.connections.mapTo(result) { LosePairedProperty(defender, attacker, it) }

        return result
    }

}

class LoseIndividualProperty(defender: Animal, attacker: Animal, val property: IndividualProperty) :
        DefenseMove(defender, attacker) {

    override fun GameState.applyMove() {
        defendingAnimal.removeProperty(property)

        // Attack fails
        phase = GamePhase.FEEDING
    }

    override fun toString(gameState: GameState, player: PlayerState) =
            "$defender loses $property (tail loss)"

}

class LosePairedProperty(defender: Animal, attacker: Animal, private val connectionMembership: ConnectionMembership)
    : DefenseMove(defender, attacker) {
    override fun GameState.applyMove() {
        defender.owner.removeConnection(connectionMembership.connection)
    }

    override fun toString(gameState: GameState, player: PlayerState): String {
        return "$defender loses ${connectionMembership.sideProperty} to ${connectionMembership.other} (tail loss)"
    }
}