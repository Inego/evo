package inego.evo.properties.individual

import inego.evo.game.Animal
import inego.evo.game.GamePhase
import inego.evo.game.Game
import inego.evo.game.Player
import inego.evo.game.moves.FeedingAnimalMove
import inego.evo.game.moves.FeedingMove
import inego.evo.properties.FeedingAction
import inego.evo.properties.IndividualProperty
import java.util.*

object CarnivorousProperty : IndividualProperty("Carnivorous"), FeedingAction {
    override val enumValue: IndividualPropertyEnum
        get() = IndividualPropertyEnum.CARNIVOROUS

    override fun gatherFeedingMoves(animal: Animal, game: Game): List<FeedingMove>
    {
        if (animal.usedAttack) {
            return emptyList()
        }
        // A predator can attack any animal on the board independent on its owner
        return game.players
                .flatMap { it.animals }
                .filter { game.canAttack(animal, it) }
                .map { AttackMove(animal, it) }
    }

    override fun mayAttachTo(animal: Animal): Boolean {
        return super.mayAttachTo(animal) && !animal.has(ScavengerProperty)
    }
}

class AttackMove(animal: Animal, private val victim: Animal) : FeedingAnimalMove(animal) {

    override val logMessage: String
        get() = "${animal.fullName} attacks ${victim.fullName}."

    override fun toString(player: Player) = "$animal attacks ${player.targetAnimalToString(victim)}"

    override fun doFeeding(game: Game): GamePhase {
        animal.usedAttack = true
        game.attackingAnimal = animal
        game.defendingAnimal = victim
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
