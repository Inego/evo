package inego.evo.properties.individual

import inego.evo.game.Game
import inego.evo.game.GameCopier
import inego.evo.game.MoveSelection
import inego.evo.game.Player
import inego.evo.game.moves.Move
import inego.evo.properties.IndividualProperty

object GrazingProperty : IndividualProperty("Grazing") {
    override val enumValue: IndividualPropertyEnum
        get() = IndividualPropertyEnum.GRAZING
}


class GrazeFood(private val foodToGraze: Int) : Move() {
    override fun clone(c: GameCopier) = GrazeFood(foodToGraze)

    override fun logMessage(player: Player) =
            if (foodToGraze > 0) "$player grazed $foodToGraze food." else "$player decided not to graze."

    override fun toString(player: Player) = "Graze $foodToGraze food"

    override fun Game.applyMove(player: Player) {
        if (foodToGraze > 0) {
            foodBase -= foodToGraze
            inactivePlayers = 0
            log { "Food left: $foodBase." }
        }
        // Otherwise food is not grazed and this does not reset the inactive player count
        // (since this is a form of pass)
    }
}

class GrazeFoodSelection(decidingPlayer: Player, moves: List<GrazeFood>)
    : MoveSelection<GrazeFood>(decidingPlayer, moves)
