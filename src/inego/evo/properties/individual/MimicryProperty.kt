package inego.evo.properties.individual

import inego.evo.game.GameState
import inego.evo.properties.IndividualProperty
import inego.evo.properties.ResolvableAttack

object MimicryProperty : IndividualProperty("Mimicry"), ResolvableAttack {
    override fun onResolve(gameState: GameState) {
        TODO("Implement attack redirection")
    }
}