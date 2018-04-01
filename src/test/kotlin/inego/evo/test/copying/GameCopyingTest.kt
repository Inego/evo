package inego.evo.test.copying

import inego.evo.cards.ECard
import inego.evo.game.Game
import inego.evo.game.GameCopier
import inego.evo.game.GamePhase
import inego.evo.properties.individual.CarnivorousProperty
import inego.evo.properties.paired.asymmetric.SymbiosisProperty
import inego.evo.test.newAnimal
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class GameCopyingTest {

    @Test
    fun complexGameCopy() {
        val random = Random()
        val g = Game.new(2, random, false)
        g.phase = GamePhase.DEFENSE
        g.seenCards += ECard.HIBERNATION.from(g)
        g.turnNumber = 10

        val p = g.players[0]

        p.passed = true
        p.discardSize = 100

        p.hand.add(ECard.BURROWING.from(g))
        p.hand.add(ECard.BIG__CARNIVOROUS.from(g))

        p.cardsPlayedAsAnimals += ECard.CAMOUFLAGE.from(g)
        p.cardsPlayedAsAnimals += ECard.CAMOUFLAGE.from(g)
        p.cardsPlayedAsAnimals += ECard.GRAZING.from(g)

        val a1 = p.newAnimal(CarnivorousProperty)

        a1.fatCapacity = 10
        a1.fat = 8
        a1.hasFood = 20
        a1.usedPiracy = true
        a1.usedMimicry = true
        a1.usedAttack = true
        a1.usedRunningAway = true
        a1.isHibernating = true
        a1.hibernatedLastTurn = true
        a1.isPoisoned = true

        val a2 = p.newAnimal()

        g.attackingAnimal = a1
        g.defendingAnimal = a2

        g.seenCards += ECard.SYMBIOSIS.from(g)
        p.addConnection(SymbiosisProperty, a1, a2, true)

        p.foodPropagationSet.add(a1.connections[0])

        // COPY

        val copier = GameCopier(g, p, random)
        val cg = copier.copiedGame

        g.phase = GamePhase.FEEDING
        assertEquals(GamePhase.DEFENSE, cg.phase)

        g.seenCards += ECard.HIBERNATION
        assertEquals(2, g.seenCards[ECard.HIBERNATION])
        assertEquals(1, cg.seenCards[ECard.HIBERNATION])

        g.turnNumber++
        assertEquals(10, cg.turnNumber)

        val deckSize = g.deck.size

        g.deck.clear()

        assertEquals(deckSize, cg.deck.size)

        assertEquals(2, cg.players.size)

        val cp = cg.players[0]
        assertNotEquals(p, cp)

        p.passed = false
        assertTrue(cp.passed)

        p.discardSize = 0
        assertEquals(100, cp.discardSize)

        p.hand.clear()
        assertEquals(2, cp.hand.size)
        assertEquals(ECard.BURROWING, cp.hand[0])
        assertEquals(ECard.BIG__CARNIVOROUS, cp.hand[1])

        p.cardsPlayedAsAnimals += ECard.CAMOUFLAGE
        p.cardsPlayedAsAnimals += ECard.GRAZING

        assertEquals(3, p.cardsPlayedAsAnimals[ECard.CAMOUFLAGE])
        assertEquals(2, p.cardsPlayedAsAnimals[ECard.GRAZING])

        assertEquals(2, cp.cardsPlayedAsAnimals[ECard.CAMOUFLAGE])
        assertEquals(1, cp.cardsPlayedAsAnimals[ECard.GRAZING])


        assertEquals(2, cp.animals.size)

        val ca1 = cp.animals[0]
        val ca2 = cp.animals[1]

        assertEquals(cp, ca1.owner)

        a1.fatCapacity = 7
        assertEquals(10, ca1.fatCapacity)
        a1.fat = 3
        assertEquals(8, ca1.fat)
        a1.hasFood = 19
        assertEquals(20, ca1.hasFood)

        a1.usedPiracy = false
        assertTrue(ca1.usedPiracy)
        assertFalse(ca2.usedPiracy)

        a1.usedMimicry = false
        assertTrue(ca1.usedMimicry)
        assertFalse(ca2.usedMimicry)

        a1.usedAttack = false
        assertTrue(ca1.usedAttack)
        assertFalse(ca2.usedAttack)

        a1.usedRunningAway = false
        assertTrue(ca1.usedRunningAway)
        assertFalse(ca2.usedRunningAway)

        a1.isHibernating = false
        assertTrue(ca1.isHibernating)
        assertFalse(ca2.isHibernating)

        a1.hibernatedLastTurn = false
        assertTrue(ca1.hibernatedLastTurn)
        assertFalse(ca2.hibernatedLastTurn)

        a1.isPoisoned = false
        assertTrue(ca1.isPoisoned)
        assertFalse(ca2.isPoisoned)

        p.removeConnection(p.connections[0])
        assertEquals(1, cp.connections.size)
        assertEquals(1, ca1.connections.size)
        assertEquals(1, ca2.connections.size)

        val copiedConnection = cp.connections[0]
        assertTrue(copiedConnection.isUsed)
        assertEquals(SymbiosisProperty, copiedConnection.property)
        val ca1m = ca1.connections[0]
        assertEquals(copiedConnection, ca1m.connection)
        assertEquals(copiedConnection, ca2.connections[0].connection)
        assertTrue(ca1m.host)
        assertFalse(ca2.connections[0].host)

        p.foodPropagationSet.clear()
        assertTrue(cp.foodPropagationSet.contains(ca1m))

        a1.removeProperty(CarnivorousProperty)
        assertFalse(a1.has(CarnivorousProperty))
        assertEquals(1, a1.foodRequirement)

        assertTrue(ca1.has(CarnivorousProperty))
        assertEquals(2, ca1.foodRequirement)

        assertEquals(ca1, cg.attackingAnimal)
        assertEquals(ca2, cg.defendingAnimal)
    }
}

fun ECard.from(game: Game) = this.also { game.deck.remove(it) }
