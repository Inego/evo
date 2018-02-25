package inego.evo.properties.individual

import inego.evo.game.GameState
import inego.evo.game.MoveSelection
import inego.evo.game.PlayerState
import inego.evo.game.moves.Move
import inego.evo.properties.IndividualProperty

object GrazingProperty : IndividualProperty("Grazing")


class GrazeFood(private val player: PlayerState, private val foodToGraze: Int) : Move() {
    override val logMessage: String
        get() = "$player grazed $foodToGraze food."

    override fun toString(player: PlayerState) = "Graze $foodToGraze food"

    override fun GameState.applyMove() {
        foodBase -= foodToGraze
        log { "Food left: $foodBase." }
    }
}

class GrazeFoodSelection(decidingPlayer: PlayerState, moves: List<GrazeFood>)
    : MoveSelection<GrazeFood>(decidingPlayer, moves)
