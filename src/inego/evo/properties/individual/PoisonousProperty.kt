package inego.evo.properties.individual

import inego.evo.game.Animal
import inego.evo.game.GameState
import inego.evo.properties.BeingEatenAction
import inego.evo.properties.IndividualProperty

object PoisonousProperty : IndividualProperty("Poisonous"), BeingEatenAction {
    override fun performBeingEatenAction(gameState: GameState, attacker: Animal) {
        // TODO implement checking for this property after successful attack
        TODO("Implement poisoned marker")
    }
}