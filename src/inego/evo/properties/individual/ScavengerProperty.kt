package inego.evo.properties.individual

import inego.evo.game.*
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
    override fun clone(c: GameCopier) = FeedTheScavengerMove(c[scavenger])

    override fun logMessage(player: Player) = "${scavenger.fullName} scavenged 1 food."

    override fun Game.applyMove(player: Player) {
        scavenger.gainBlueTokens(1)
    }

    override fun toString(player: Player) = "Feed $scavenger (Scavenger)"
}

class ScavengerSelection(decidingPlayer: Player, moves: List<FeedTheScavengerMove>)
    : MoveSelection<FeedTheScavengerMove>(decidingPlayer, moves)