package inego.evo.game

import inego.evo.game.moves.BurnFatMove
import inego.evo.game.moves.DefenseMove
import inego.evo.game.moves.FeedingMove
import inego.evo.game.moves.GetRedTokenMove
import inego.evo.properties.*
import inego.evo.properties.individual.FatTissueProperty
import inego.evo.properties.paired.symmetric.CommunicationProperty
import inego.evo.properties.paired.symmetric.CooperationProperty
import kotlin.math.min

class Animal(val owner: PlayerState) {
    inline val propertyCount
        get() = individualProperties.size + connections.size + if (fatCapacity > 0) 1 else 0

    val individualProperties: MutableList<IndividualProperty> = mutableListOf()

    val connections: MutableList<ConnectionMembership> = mutableListOf()

    var fatCapacity = 0
    var fat = 0

    var foodRequirement = 1
    var hasFood = 0

    // Special property flags

    var usedPiracy = false
    var usedMimicry = false

    var isHibernating = false
    var hibernatedLastTurn = false

    var isPoisoned = false

    val isFed: Boolean
        inline get() = isHibernating || foodRequirement <= hasFood

    fun has(individualProperty: IndividualProperty) = individualProperties.contains(individualProperty)

    fun addProperty(individualProperty: IndividualProperty) {
        // Thread-unsafe, but any game state is supposed to be modified from a single thread
        individualProperties.add(individualProperty)

        if (individualProperty is StatModifier) {
            individualProperty.onAttach(this)
        }
    }

    fun removeProperty(individualProperty: IndividualProperty) {
        if (individualProperty != FatTissueProperty) {
            individualProperties.remove(individualProperty)
        }

        if (individualProperty is StatModifier) {
            individualProperty.onDetach(this)
        }
    }

    override fun toString(): String {
        return (owner.animals.indexOf(this) + 1).toString()
    }

    val isFull: Boolean
        inline get() = isHibernating || isFed && fatCapacity == fat

    fun gatherFeedingMoves(gameState: GameState): List<FeedingMove> {

        if (isFull)
            return emptyList()

        val result: MutableList<FeedingMove> = mutableListOf()

        if (gameState.foodBase > 0) {
            result.add(GetRedTokenMove(this))
        }

        if (!isFed && fat > 0) {
            val maxFatToBurn = min(foodRequirement - hasFood, fat)
            for (fatToBurn in 1..maxFatToBurn) {
                result.add(BurnFatMove(this, fatToBurn))
            }
        }

        individualProperties
                .filterIsInstance<FeedingAction>()
                .flatMapTo(result) { it.gatherFeedingMoves(this, gameState) }

        // TODO if all animals don't starve AND there is no base food left, the user may pass as an option

        return result
    }

    fun gatherDefenseMoves(attacker: Animal, gameState: GameState): List<DefenseMove> = individualProperties
            .filterIsInstance<DefenseAction>()
            .flatMap { it.gatherDefenseMoves(this, attacker, gameState) }

    fun gainBlueTokens(numberOfTokens: Int) {

        val foodToGain = min(numberOfTokens, foodRequirement - hasFood)

        if (foodToGain == 0) {
            return
        }

        hasFood += foodToGain

        propagateCooperation()
    }


    private fun propagateCooperation() {
        connections.filterTo(owner.foodPropagationSet) {
            it.property == CooperationProperty && !it.isUsed && !it.other.isFed
        }
    }

    fun gainRedToken(gameState: GameState) {

        assert(gameState.foodBase > 0) { "Trying to get a red token from an empty base" }

        gameState.foodBase--
        hasFood++

        if (gameState.foodBase > 0) {
            connections.filterTo(owner.foodPropagationSet) {
                it.property == CommunicationProperty && !it.isUsed && !it.other.isFed
            }
        }

        propagateCooperation()
    }
}
