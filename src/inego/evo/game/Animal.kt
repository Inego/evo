package inego.evo.game

import inego.evo.game.moves.BurnFatMove
import inego.evo.game.moves.FeedingMove
import inego.evo.game.moves.GetRedTokenMove
import inego.evo.properties.FeedingAction
import inego.evo.properties.IndividualProperty
import inego.evo.properties.StatModifier
import inego.evo.properties.individual.FatTissueProperty
import kotlin.math.min

class Animal(val owner: PlayerState) {
    inline val propertyCount
        get() = individualProperties.size + connections.size + if (fatCapacity > 0) 1 else 0

    // TODO !!! refactor to a universal mechanism to track and reset used properties

    val individualProperties: MutableList<IndividualProperty> = mutableListOf()
    val connections: MutableList<ConnectionMembership> = mutableListOf()

    var fatCapacity = 0
    var fat = 0

    var foodRequirement = 1
    var hasFood = 0

    // Special property flags
    // TODO consider to moving them to a bit map
    var hasPirated = false

    var isHibernating = false
    var hibernatedLastTurn = false

    val isFed: Boolean
        inline get() = isHibernating || foodRequirement == hasFood

    fun has(individualProperty: IndividualProperty): Boolean =
            individualProperties.contains(individualProperty)

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
                .flatMapTo(result) { it.gatherMoves(this, gameState) }

        // TODO if all animals don't starve AND there is no base food left, the user may pass as an option

        return result
    }


}
