package inego.evo.game

import inego.evo.game.moves.FeedingMove
import inego.evo.game.moves.GetRedTokenMove
import inego.evo.properties.FeedingAction
import inego.evo.properties.IndividualProperty
import inego.evo.properties.StatModifier

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
    // TODO consider to moving them to a bit map
    // TODO clear hasPirated flag
    var hasPirated = false

    val starves: Boolean
        inline get() = foodRequirement < hasFood

    fun has(individualProperty: IndividualProperty): Boolean =
            individualProperties.contains(individualProperty)

    fun addProperty(individualProperty: IndividualProperty) {
        // Thread-unsafe, but any game state is supposed to be modified from a single thread
        individualProperties.add(individualProperty)

        if (individualProperty is StatModifier) {
            individualProperty.onAttach(this)
        }
    }

    override fun toString(): String {
        return (owner.animals.indexOf(this) + 1).toString()
    }

    val isFed: Boolean
        inline get() = !starves && fatCapacity == fat

    fun gatherFeedingMoves(gameState: GameState): List<FeedingMove> {
        if (isFed)
            return emptyList()
        // TODO gather feeding moves
        val result: MutableList<FeedingMove> = mutableListOf()

        if (gameState.foodBase > 0) {
            result.add(GetRedTokenMove(this))
        }

        individualProperties
                .filterIsInstance<FeedingAction>()
                .flatMapTo(result) { it.gatherMoves(this, gameState) }

        // TODO if all animals don't starve AND there is no base food left, the user may pass as an option

        return result
    }
}
