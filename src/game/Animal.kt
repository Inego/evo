package game

import properties.IndividualProperty

class Animal {
    val individualProperties: MutableList<IndividualProperty>? = null
    val connections: MutableList<ConnectionMembership>? = null

    var fatCapacity = 0
    var fat = 0

    var foodRequirement = 1
    var baseFood = 0
    var additionalFood = 0
}