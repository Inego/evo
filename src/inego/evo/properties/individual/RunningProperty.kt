package inego.evo.properties.individual

import inego.evo.game.GameState
import inego.evo.properties.IndividualProperty
import inego.evo.properties.ResolvableAttack

object RunningProperty : IndividualProperty("Running"), ResolvableAttack {

    override fun onResolve(gameState: GameState) {
        TODO("Implement probabilistic runaway")
    }
}