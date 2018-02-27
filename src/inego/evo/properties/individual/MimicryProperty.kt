package inego.evo.properties.individual

import inego.evo.game.Animal
import inego.evo.game.Game
import inego.evo.game.Player
import inego.evo.properties.IndividualProperty
import inego.evo.properties.DefenseAction
import inego.evo.game.moves.DefenseMove

object MimicryProperty : IndividualProperty("Mimicry"), DefenseAction {
    override fun gatherDefenseMoves(defender: Animal, attacker: Animal, game: Game): List<DefenseMove> =
            if (defender.usedMimicry) emptyList()
            else defender.owner.animals
                    .filter { it != defender && game.canAttack(attacker, it) }
                    .map { RedirectAttack(defender, attacker, it) }
}

class RedirectAttack(defender: Animal, attacker: Animal, private val anotherVictim: Animal) : DefenseMove(defender, attacker) {
    override val logMessage: String
        get() = "${defender.fullName} redirects attack to $anotherVictim"

    override fun Game.applyMove() {
        defender.usedMimicry = true
        defendingAnimal = anotherVictim
        // ...and the attack goes on...
    }

    override fun toString(player: Player) =
            "$defender: redirect attack to $anotherVictim"
}