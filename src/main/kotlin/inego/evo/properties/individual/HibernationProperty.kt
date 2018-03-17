package inego.evo.properties.individual

import inego.evo.game.*
import inego.evo.game.moves.FeedingAnimalMove
import inego.evo.game.moves.FeedingMove
import inego.evo.properties.FeedingAction
import inego.evo.properties.IndividualProperty

object HibernationProperty : IndividualProperty("Hibernation"), FeedingAction {
    override val enumValue: IndividualPropertyEnum
        get() = IndividualPropertyEnum.HIBERNATION

    override fun gatherFeedingMoves(animal: Animal, game: Game): List<FeedingMove> {
        if (!(animal.hibernatedLastTurn || game.isLastTurn)) {
            return listOf(HibernationMove(animal))
        }
        return emptyList()
    }
}

class HibernationMove(animal: Animal) : FeedingAnimalMove(animal) {
    override fun clone(c: GameCopier) = HibernationMove(c[animal])

    override fun logMessage(player: Player) = "${animal.fullName} falls asleep"

    override fun doFeeding(game: Game, player: Player): GamePhase {
        animal.isHibernating = true

        game.inactivePlayers = 0

        // There is no food propagation, so go directly to grazing
        return GamePhase.GRAZING
    }

    override fun toString(player: Player) = "$animal falls asleep"
}
