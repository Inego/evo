package inego.evo.properties.individual

import inego.evo.game.Animal
import inego.evo.game.GameState
import inego.evo.game.PlayerState
import inego.evo.properties.IndividualProperty
import inego.evo.properties.DefenseAction
import inego.evo.game.moves.DefenseMove

object MimicryProperty : IndividualProperty("Mimicry"), DefenseAction {
    override fun gatherDefenseMoves(defender: Animal, attacker: Animal, gameState: GameState): List<DefenseMove> =
            defender.owner.animals
                    .filter { it != defender && gameState.canAttack(attacker, it) }
                    .map { RedirectAttack(defender, attacker, it) }
}

class RedirectAttack(defender: Animal, attacker: Animal, private val anotherVictim: Animal) : DefenseMove(defender, attacker) {
    override fun GameState.applyMove() {
        defendingAnimal = anotherVictim
        // ...and the attack goes on...
    }

    override fun toString(gameState: GameState, player: PlayerState) =
            "$defender redirects attack to $anotherVictim"

}