package inego.evo.test

import inego.evo.cards.CardQuantities
import inego.evo.cards.ECard
import inego.evo.game.Game
import inego.evo.getProtoReference
import inego.evo.proto.Card
import inego.evo.proto.GameProtos
import inego.evo.toProto
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class ProtoTests {

    @Test
    fun cardQuantitiesToProtos() {
        val cq = CardQuantities.new()
        cq += ECard.GRAZING
        cq += ECard.GRAZING

        val p = cq.toProto()

        assertEquals(p.size, 1)
        assertEquals(Card.GRAZING, p[0].card)
        assertEquals(2, p[0].quantity)
    }

    @Test
    fun gameAnimalProtoReference() {
        val g = Game.new(2, Random())
        val p2 = g[1]
        p2.newAnimal()
        p2.newAnimal()
        val a3 = p2.newAnimal()

        val ref = g.getProtoReference(a3)

        assertEquals(1, ref.owner)
        assertEquals(2, ref.animal)
    }

}