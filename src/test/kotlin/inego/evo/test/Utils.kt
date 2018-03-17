package inego.evo.test

import inego.evo.game.Animal
import inego.evo.game.Game
import inego.evo.game.MoveSelection
import inego.evo.game.Player
import inego.evo.game.moves.EmptyMove
import inego.evo.properties.IndividualProperty
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


fun <T> Iterator<T>.assertNext(nextValue: T?) {

    if (nextValue == null) {
        assertFalse(hasNext())
    }
    else {
        assertTrue(hasNext())
        assertEquals(nextValue, next())
    }
}


/**
 * Plays the game and returns a move selection or `null`
 * if the game has been played until the end.
 */
fun Game.next(): MoveSelection<*>? = next(currentPlayer, EmptyMove)


/**
 * Creates an animal with the specified individual property.
 */
fun Player.newAnimal(individualProperty: IndividualProperty, fatCapacity: Int = 0): Animal =
        newAnimal().apply {
            addProperty(individualProperty)
            this.fatCapacity = fatCapacity
        }
