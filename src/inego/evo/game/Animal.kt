package inego.evo.game

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
    var baseFood = 0
    var additionalFood = 0

    val starves: Boolean
        inline get() = foodRequirement < baseFood + additionalFood

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
}
