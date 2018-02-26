package inego.evo.properties.individual

import inego.evo.game.Animal
import inego.evo.game.GamePhase
import inego.evo.game.GameState
import inego.evo.game.PlayerState
import inego.evo.game.moves.FeedingAnimalMove
import inego.evo.game.moves.FeedingMove
import inego.evo.properties.FeedingAction
import inego.evo.properties.IndividualProperty
import java.util.*

object CarnivorousProperty : IndividualProperty("Carnivorous"), FeedingAction {

    override fun gatherFeedingMoves(animal: Animal, gameState: GameState): List<FeedingMove>
    {
        if (animal.usedAttack) {
            return emptyList()
        }
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

class AttackMove(animal: Animal, private val victim: Animal) : FeedingAnimalMove(animal) {

    override val logMessage: String
        get() = "${animal.fullName} attacks ${victim.fullName}."

    override fun toString(player: PlayerState) = "$animal attacks ${player.targetAnimalToString(victim)}"

    override fun doFeeding(gameState: GameState): GamePhase {
        animal.usedAttack = true
        gameState.attackingAnimal = animal
        gameState.defendingAnimal = victim
        return GamePhase.DEFENSE
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AttackMove

        return victim == other.victim && animal == other.animal

    }

    override fun hashCode(): Int {
        return Objects.hash(animal, victim)
    }
}
