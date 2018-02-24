package inego.evo.properties.individual

import inego.evo.game.Animal
import inego.evo.game.GameState
import inego.evo.game.MoveSelection
import inego.evo.game.PlayerState
import inego.evo.game.moves.Move
import inego.evo.properties.IndividualProperty

object ScavengerProperty : IndividualProperty("Scavenger") {
    override fun mayAttachTo(animal: Animal): Boolean {
        return super.mayAttachTo(animal) && !animal.has(CarnivorousProperty)
    }
}

class FeedTheScavengerMove(private val scavenger: Animal) : Move() {
    override fun GameState.applyMove() {
        scavenger.gainBlueTokens(1)
    }

    override fun toString(gameState: GameState, player: PlayerState) = "Feed $scavenger (Scavenger)"
}

class ScavengerSelection(decidingPlayer: PlayerState, moves: List<FeedTheScavengerMove>)
    : MoveSelection<FeedTheScavengerMove>(decidingPlayer, moves)