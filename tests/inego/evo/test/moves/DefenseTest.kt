package inego.evo.test.moves

import inego.evo.game.GamePhase
import inego.evo.game.GameState
import inego.evo.game.moves.DevelopmentMoveSelection
import inego.evo.game.moves.FeedingMoveSelection
import inego.evo.properties.individual.AttackMove
import inego.evo.properties.individual.CarnivorousProperty
import inego.evo.properties.individual.RunningProperty
import inego.evo.test.newAnimal
import inego.evo.test.next
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DefenseTest {

    @Test
    fun killOwnAnimalsForFat() {

        val gameState = GameState.new(2)

        val p1 = gameState.players[0]

        val a1 = p1.newAnimal(CarnivorousProperty).apply {
            hasFood = 1  // 1/1
            fatCapacity = 4  // Enough fat capacity to eat the other two animals
        }

        val a2 = p1.newAnimal()
        val a3 = p1.newAnimal()

        gameState.phase = GamePhase.FEEDING

        val moveSelection = gameState.next()!!

        assertTrue(moveSelection is FeedingMoveSelection)
        assertEquals(2, moveSelection.moves.size)
        assertEquals(moveSelection.moves[0], AttackMove(a1, a2))
        assertEquals(moveSelection.moves[1], AttackMove(a1, a3))

        gameState.next(moveSelection.moves[0])!!

        // After a1 attacked a2, there are no feeding moves left, because a1 has already attacked.
        // Game goes to next move.

        assertEquals(2, gameState.turnNumber)
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

            val gameState = GameState.new(2)
            gameState.phase = GamePhase.FEEDING


            val p1 = gameState.players[0]

            p1.newAnimal(CarnivorousProperty)
            p1.newAnimal(RunningProperty)

            val moveSelection = gameState.next()!!

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