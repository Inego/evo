package inego.evo.test.moves

import inego.evo.game.GamePhase
import inego.evo.game.Game
import inego.evo.game.moves.DevelopmentMoveSelection
import inego.evo.game.moves.FeedingMoveSelection
import inego.evo.properties.individual.AttackMove
import inego.evo.properties.individual.CarnivorousProperty
import inego.evo.properties.individual.RunningProperty
import inego.evo.test.newAnimal
import inego.evo.test.next
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DefenseTest {

    @Test
    fun killOwnAnimalsForFat() {

        val game = Game.new(2, false, Random())

        val p1 = game.players[0]

        val a1 = p1.newAnimal(CarnivorousProperty).apply {
            hasFood = 1  // 1/1
            fatCapacity = 4  // Enough fat capacity to eat the other two animals
        }

        val a2 = p1.newAnimal()
        val a3 = p1.newAnimal()

        game.phase = GamePhase.FEEDING

        val moveSelection = game.next()!!

        assertTrue(moveSelection is FeedingMoveSelection)
        assertEquals(2, moveSelection.size)
        assertEquals(moveSelection[0], AttackMove(a1, a2))
        assertEquals(moveSelection[1], AttackMove(a1, a3))

        game.next(p1, moveSelection[0])!!

        // After a1 attacked a2, there are no feeding moves left, because a1 has already attacked.
        // Game goes to next move.

        assertEquals(2, game.turnNumber)
    }

    @Test
    fun checkRunawayOutcomes() {

        var escaped = false
        var gotCaught = false

        var safetyCounter = 0

        do {
            if (safetyCounter == 100) {
                throw AssertionError()
            }

            val game = Game.new(2, false, Random())
            game.phase = GamePhase.FEEDING


            val p1 = game.players[0]

            p1.newAnimal(CarnivorousProperty)
            p1.newAnimal(RunningProperty)

            val moveSelection = game.next()!!

            assertTrue(moveSelection is DevelopmentMoveSelection)

            when (p1.animals.size) {
                0 -> escaped = true
                1 -> gotCaught = true
                else -> throw AssertionError()
            }

            safetyCounter++

        } while (!(escaped && gotCaught))
    }
}