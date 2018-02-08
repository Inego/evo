package inego.evo.properties.individual

import inego.evo.game.GameState
import inego.evo.properties.IndividualProperty
import inego.evo.properties.SingleTarget

object ParasiteProperty : IndividualProperty("Parasite") {

    override fun getTargets(gameState: GameState): List<SingleTarget> {

        val currentPlayer = gameState.currentPlayer

        return gameState.players
                .filter { it != currentPlayer }
                .flatMap { it.animals.filter { mayAttachTo(it) } }
                .map { SingleTarget(it) }
    }
}