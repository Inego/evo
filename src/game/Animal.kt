package game

import properties.IndividualProperty

class Animal {
    private val individualProperties: MutableList<IndividualProperty> = mutableListOf()
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
    }
}