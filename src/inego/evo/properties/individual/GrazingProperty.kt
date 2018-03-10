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


class GrazeFood(private val player: Player, private val foodToGraze: Int) : Move() {
    override fun clone(c: GameCopier) = GrazeFood(c[player], foodToGraze)

    override val logMessage: String
        get() = "$player grazed $foodToGraze food."

    override fun toString(player: Player) = "Graze $foodToGraze food"

    override fun Game.applyMove() {
        foodBase -= foodToGraze
        log { "Food left: $foodBase." }
    }
}

class GrazeFoodSelection(decidingPlayer: Player, moves: List<GrazeFood>)
    : MoveSelection<GrazeFood>(decidingPlayer, moves)
