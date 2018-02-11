package inego.evo.properties

import inego.evo.game.GameState
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import inego.evo.properties.paired.asymmetric.SymbiosisProperty
import inego.evo.properties.paired.symmetric.CooperationProperty
import org.junit.jupiter.api.Assertions.assertEquals

internal class PairedPropertyTest {

    private val gameState = GameState.new(1)

    private val p = gameState.players[0]

    @BeforeEach
    fun setup() {
        p.clearAnimals()
    }

    @Test
    fun testSimple() {
        val a1 = p.newAnimal()
        val a2 = p.newAnimal()
        val a3 = p.newAnimal()

        p.addConnection(CooperationProperty, a2, a3)

        val targets = CooperationProperty.getTargets(gameState)
        assertEquals(2, targets.size)
        assertEquals(a1, targets[0].firstAnimal)
        assertEquals(a2, targets[0].secondAnimal)
        assertEquals(a1, targets[1].firstAnimal)
        assertEquals(a3, targets[1].secondAnimal)
    }

    @Test
    fun testSimpleAsymmetric() {
        val a1 = p.newAnimal()
        val a2 = p.newAnimal()
        val targets = SymbiosisProperty.getTargets(gameState)
        assertEquals(2, targets.size)
        assertEquals(a1, targets[0].firstAnimal)
        assertEquals(a2, targets[0].secondAnimal)
        assertEquals(a1, targets[1].secondAnimal)
        assertEquals(a2, targets[1].firstAnimal)
    }

}