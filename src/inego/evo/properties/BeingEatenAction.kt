package inego.evo.properties

import inego.evo.game.Animal
import inego.evo.game.GameState

interface BeingEatenAction {

    fun performBeingEatenAction(gameState: GameState, attacker: Animal)

}