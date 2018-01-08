package game

import cards.Card

class PlayerState {

    var passed = false

    val hand: MutableList<Card> = mutableListOf()

    val animals: MutableList<Animal> = mutableListOf()

    val connections: MutableList<Connection> = mutableListOf()

}