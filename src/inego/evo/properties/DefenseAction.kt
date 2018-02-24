package inego.evo.properties

import inego.evo.game.Animal
import inego.evo.game.GameState
import inego.evo.game.moves.DefenseMove

interface DefenseAction {
    fun gatherDefenseMoves(defender: Animal, attacker: Animal, gameState: GameState): List<DefenseMove>
}
