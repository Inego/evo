package inego.evo.properties

import inego.evo.game.Animal
import inego.evo.game.Game
import inego.evo.game.moves.DefenseMove

interface DefenseAction {
    fun gatherDefenseMoves(defender: Animal, attacker: Animal, game: Game): List<DefenseMove>
}
