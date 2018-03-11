package inego.evo.test.moves

import inego.evo.game.Game
import inego.evo.game.GamePhase
import inego.evo.game.moves.FeedingMoveSelection
import inego.evo.game.moves.FoodPropagationMoveSelection
import inego.evo.properties.individual.AttackMove
import inego.evo.properties.individual.CarnivorousProperty
import inego.evo.properties.individual.ScavengerProperty
import inego.evo.properties.paired.symmetric.CooperationProperty
import inego.evo.test.newAnimal
import inego.evo.test.next
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

class FoodPropagationTest {

    @Test
    fun testTwoPlayerFoodPropagation() {

        val g = Game.new(2, true, Random())

        val p1 = g[0]
        val p2 = g[1]

        val a11 = p1.newAnimal(CarnivorousProperty)
        val a12 = p1.newAnimal()
        val a13 = p1.newAnimal()
        p1.addConnection(CooperationProperty, a11, a12)
        p1.addConnection(CooperationProperty, a11, a13)

        val victim = p1.newAnimal()

        val a21 = p2.newAnimal(ScavengerProperty)
        val a22 = p2.newAnimal()
        val a23 = p2.newAnimal()
        p2.addConnection(CooperationProperty, a21, a22)
        p2.addConnection(CooperationProperty, a21, a23)

        g.phase = GamePhase.FEEDING

        var selection = g.next()

        val attackVictim = (selection as FeedingMoveSelection).first {
            it is AttackMove && it.victim == victim
        }

        selection = g.next(p1, attackVictim)

        if (selection !is FoodPropagationMoveSelection)
            throw AssertionError()

        assertEquals(2, selection.size)
        assertEquals(p1, selection.decidingPlayer)

        selection = g.next(p1, selection[0])

        if (selection !is FoodPropagationMoveSelection)
            throw AssertionError()

        assertEquals(2, selection.size)
        assertEquals(p2, selection.decidingPlayer)
    }
}