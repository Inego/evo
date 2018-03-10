package inego.evo.properties.individual

import inego.evo.game.*
import inego.evo.game.moves.DefenseMove
import inego.evo.properties.DefenseAction
import inego.evo.properties.IndividualProperty

object RunningProperty : IndividualProperty("Running"), DefenseAction {
    override val enumValue: IndividualPropertyEnum
        get() = IndividualPropertyEnum.RUNNING

    override fun gatherDefenseMoves(defender: Animal, attacker: Animal, game: Game): List<DefenseMove> =
            if (defender.usedRunningAway) emptyList() else listOf(RunawayAttempt(defender, attacker))
}

class RunawayAttempt(defender: Animal, attacker: Animal) : DefenseMove(defender, attacker) {
    override fun clone(c: GameCopier) = RunawayAttempt(c[defender], c[attacker])

    override fun logMessage(player: Player) = "${defender.fullName} is trying to run away from ${attacker.fullName}."

    override fun Game.applyMove(player: Player) {
        val outcome = dice()
        log { "Dice = $outcome" }
        if (outcome >= 4) {
            log { "Success! A life has been saved." }
            // Defender escaped successfully, effectively ending the attack
            // Skip food propagation
            phase = GamePhase.GRAZING
        } else {
            log { "${defender.fullName} failed to run away from ${attacker.fullName}." }
            defendingAnimal!!.usedRunningAway = true
            // Otherwise, the attack goes on
        }
    }

    override fun toString(player: Player) = "$defender tries to run away"
}