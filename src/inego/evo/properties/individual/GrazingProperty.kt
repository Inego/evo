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
            log { "Food left: $foodBase." }
        }
    }
}

class GrazeFoodSelection(decidingPlayer: Player, moves: List<GrazeFood>)
    : MoveSelection<GrazeFood>(decidingPlayer, moves)
