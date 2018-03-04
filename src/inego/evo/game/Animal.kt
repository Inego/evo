package inego.evo.game

import inego.evo.game.moves.BurnFatMove
import inego.evo.game.moves.DefenseMove
import inego.evo.game.moves.FeedingMove
import inego.evo.game.moves.GetRedTokenMove
import inego.evo.properties.*
import inego.evo.properties.individual.FatTissueProperty
import inego.evo.properties.individual.IndividualPropertyEnum
import inego.evo.properties.paired.asymmetric.SymbiosisGuest
import inego.evo.properties.paired.symmetric.CommunicationProperty
import inego.evo.properties.paired.symmetric.CooperationProperty
import java.util.*
import kotlin.math.min

class Animal private constructor(
        val owner: Player,
        val individualProperties: EnumSet<IndividualPropertyEnum>,
        var fatCapacity: Int,
        var fat: Int,
        var foodRequirement: Int,
        var hasFood: Int,
        var usedPiracy: Boolean,
        var usedMimicry: Boolean,
        var usedAttack: Boolean,
        var usedRunningAway: Boolean,
        var isHibernating: Boolean,
        var hibernatedLastTurn: Boolean,
        var isPoisoned: Boolean

) {
    inline val propertyCount
        get() = individualProperties.size + connections.size + if (fatCapacity > 0) 1 else 0

    val connections: MutableList<ConnectionMembership> = mutableListOf()

    val isFed: Boolean
        inline get() = isHibernating || foodRequirement <= hasFood

    val mayEat: Boolean
        inline get() {
            if (isFull)
                return false
            return !connections.any { it.sideProperty == SymbiosisGuest && !it.other.isFed }
        }

    fun has(individualProperty: IndividualProperty) = individualProperties.contains(individualProperty.enumValue)

    fun addProperty(individualProperty: IndividualProperty) {
        // Thread-unsafe, but any game state is supposed to be modified from a single thread
        if (individualProperty != FatTissueProperty)
            individualProperties.add(individualProperty.enumValue)

        if (individualProperty is StatModifier) {
            individualProperty.onAttach(this)
        }
    }

    fun removeProperty(individualProperty: IndividualProperty) {
        if (individualProperty != FatTissueProperty) {
            individualProperties.remove(individualProperty.enumValue)
        }

        if (individualProperty is StatModifier) {
            individualProperty.onDetach(this)
        }
    }

    override fun toString(): String {
        return (owner.animals.indexOf(this) + 1).toString()
    }

    val fullName
        inline get() = "$owner's $this"


    val isFull: Boolean
        inline get() = isHibernating || isFed && fatCapacity == fat

    fun gatherFeedingMoves(game: Game): List<FeedingMove> {

        if (!mayEat)
            return emptyList()

        val result: MutableList<FeedingMove> = mutableListOf()

        if (game.foodBase > 0) {
            result.add(GetRedTokenMove(this))
        }

        if (!isFed && fat > 0) {
            val maxFatToBurn = min(foodRequirement - hasFood, fat)
            for (fatToBurn in 1..maxFatToBurn) {
                result.add(BurnFatMove(this, fatToBurn))
            }
        }

        individualProperties
                .map { it.individualProperty }
                .filterIsInstance<FeedingAction>()
                .flatMapTo(result) { it.gatherFeedingMoves(this, game) }

        return result
    }

    fun gatherDefenseMoves(attacker: Animal, game: Game): List<DefenseMove> = individualProperties
            .map { it.individualProperty }
            .filterIsInstance<DefenseAction>()
            .flatMap { it.gatherDefenseMoves(this, attacker, game) }

    fun gainBlueTokens(numberOfTokens: Int) {

        val foodToGain = min(numberOfTokens, foodRequirement - hasFood + fatCapacity - fat)

        if (foodToGain == 0) {
            return
        }

        gainFood(foodToGain)

        propagateCooperation()
    }

    private fun gainFood(quantity: Int) {
        repeat(quantity) {
            if (foodRequirement > hasFood)
                hasFood++
            else {
                assert(fatCapacity > fat)
                fat++
            }
        }
    }

    private fun propagateCooperation() {
        connections.filterTo(owner.foodPropagationSet) {
            it.property == CooperationProperty && !it.isUsed && it.other.mayEat
        }
    }

    fun gainRedToken(game: Game) {
        assert(game.foodBase > 0) { "Trying to get a red token from an empty base" }

        game.foodBase--

        game.log { "Food left: ${game.foodBase}." }

        gainFood(1)

        if (game.foodBase > 0) {
            connections.filterTo(owner.foodPropagationSet) {
                it.property == CommunicationProperty && !it.isUsed && it.other.mayEat
            }
        }

        propagateCooperation()
    }

    fun clone(targetPlayer: Player) = Animal(
            targetPlayer,
            individualProperties.clone(),
            fatCapacity,
            fat,
            foodRequirement,
            hasFood,
            usedPiracy,
            usedMimicry,
            usedAttack,
            usedRunningAway,
            isHibernating,
            hibernatedLastTurn,
            isPoisoned
    )

    companion object {
        fun new(owner: Player): Animal = Animal(
                owner,
                EnumSet.noneOf(IndividualPropertyEnum::class.java),
                0,
                0,
                1,
                0,
                false,
                false,
                false,
                false,
                false,
                false,
                false
        )
    }
}
