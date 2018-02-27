package inego.evo.properties.individual

import inego.evo.game.Animal
import inego.evo.game.Game
import inego.evo.game.MoveSelection
import inego.evo.game.Player
import inego.evo.game.moves.Move
import inego.evo.properties.IndividualProperty

object ScavengerProperty : IndividualProperty("Scavenger") {
    override val enumValue: IndividualPropertyEnum
        get() = IndividualPropertyEnum.SCAVENGER

    override fun mayAttachTo(animal: Animal): Boolean {
        return super.mayAttachTo(animal) && !animal.has(CarnivorousProperty)
    }
}

class FeedTheScavengerMove(private val scavenger: Animal) : Move() {
    override val logMessage: String
        get() = "${scavenger.fullName} scavenged 1 food."

    override fun Game.applyMove() {
        scavenger.gainBlueTokens(1)
    }

    override fun toString(player: Player) = "Feed $scavenger (Scavenger)"
}

class ScavengerSelection(decidingPlayer: Player, moves: List<FeedTheScavengerMove>)
    : MoveSelection<FeedTheScavengerMove>(decidingPlayer, moves)