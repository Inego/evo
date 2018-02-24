package inego.evo.properties.individual

import inego.evo.game.Animal
import inego.evo.game.GamePhase
import inego.evo.game.GameState
import inego.evo.game.PlayerState
import inego.evo.game.moves.FeedingMove
import inego.evo.properties.FeedingAction
import inego.evo.properties.IndividualProperty

object CarnivorousProperty : IndividualProperty("Carnivorous"), FeedingAction {

    override fun gatherMoves(animal: Animal, gameState: GameState): List<FeedingMove> {

        // A predator can attack any animal on the board independent on its owner
        return gameState.players
                .flatMap { it.animals }
                .filter { gameState.canAttack(animal, it) }
                .map { AttackMove(animal, it) }
    }

    override fun mayAttachTo(animal: Animal): Boolean {
        return super.mayAttachTo(animal) && !animal.has(ScavengerProperty)
    }

}

class AttackMove(animal: Animal, val victim: Animal) : FeedingMove(animal) {
    override fun toString(gameState: GameState, player: PlayerState) = "$animal attacks ${player.targetAnimalToString(victim)}"

    override fun doFeeding(gameState: GameState) {
        gameState.attackingAnimal = animal
        gameState.defendingAnimal = victim
        gameState.phase = GamePhase.DEFENSE
    }

}