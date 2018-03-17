package inego.evo.test.moves

import inego.evo.game.GamePhase
import inego.evo.game.Game
import inego.evo.game.moves.DevelopmentMoveSelection
import inego.evo.game.moves.FeedingMoveSelection
import inego.evo.game.moves.FeedingPassMove
import inego.evo.properties.individual.CarnivorousProperty
import inego.evo.test.newAnimal
import inego.evo.test.next
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FeedingTest {

    @Test
    fun passingOutOfFeeding() {

        val game = Game.new(2, false, Random())
        game.phase = GamePhase.FEEDING

        // P1 has two fed carnivorous animals with unfilled fat capacity.
        // They may eat each other (2 feeding moves), but also the player has an option to pass from feeding.

        val p1 = game.players[0]

        p1.newAnimal(CarnivorousProperty).apply {
            hasFood = 2
            fatCapacity = 2
        }

        p1.newAnimal(CarnivorousProperty).apply {
            hasFood = 2
            fatCapacity = 2
        }

        var next = game.next()!!

        assertTrue(next is FeedingMoveSelection)
        assertEquals(3, next.size)

        val pass = FeedingPassMove

        assertTrue(next.contains(pass))

        next = game.next(p1, pass)!!

        assertTrue(next is DevelopmentMoveSelection)
        assertFalse(p1.passed)
    }

}