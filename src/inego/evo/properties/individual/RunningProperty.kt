package inego.evo.properties.individual

import inego.evo.game.*
import inego.evo.properties.IndividualProperty
import inego.evo.properties.DefenseAction
import inego.evo.game.moves.DefenseMove
import java.util.concurrent.ThreadLocalRandom

object RunningProperty : IndividualProperty("Running"), DefenseAction {
    override fun gatherDefenseMoves(defender: Animal, attacker: Animal, gameState: GameState): List<DefenseMove> {
        return listOf(RunawayAttempt(defender, attacker))
    }
}

class RunawayAttempt(defender: Animal, attacker: Animal) : DefenseMove(defender, attacker) {
    override fun GameState.applyMove() {
        // TODO log outcome
        if (dice() >= 4) {
            // Defender escaped successfully, effectively ending the attack
            phase = GamePhase.FEEDING
        } else {
            // Otherwise, the attack proceeds
        }
    }

    override fun toString(gameState: GameState, player: PlayerState) = "$defender tries to run away"
}