package inego.evo.properties

import inego.evo.game.GameState

interface ResolvableAttack {

    fun onResolve(gameState: GameState)

}