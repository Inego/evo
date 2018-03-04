package inego.evo.test.properties

import inego.evo.game.Game
import inego.evo.properties.paired.asymmetric.SymbiosisProperty
import inego.evo.properties.paired.symmetric.CooperationProperty
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PairedPropertyTest {

    private val game = Game.new(1, false)

    private val p = game.players[0]

    @BeforeEach
    fun setup() {
        p.apply {
            animals.clear()
            connections.clear()
        }
    }

    @Test
    fun testSimple() {
        val a1 = p.newAnimal()
        val a2 = p.newAnimal()
        val a3 = p.newAnimal()

        p.addConnection(CooperationProperty, a2, a3, false)

        val targets = CooperationProperty.getTargets(game).toList()
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
        val targets = SymbiosisProperty.getTargets(game).toList()
        assertEquals(2, targets.size)
        assertEquals(a1, targets[0].firstAnimal)
        assertEquals(a2, targets[0].secondAnimal)
        assertEquals(a1, targets[1].secondAnimal)
        assertEquals(a2, targets[1].firstAnimal)
    }

}