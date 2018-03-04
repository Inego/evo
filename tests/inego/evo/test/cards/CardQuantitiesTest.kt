package inego.evo.test.cards

import inego.evo.cards.CardQuantities
import inego.evo.cards.ECard
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CardQuantitiesTest {

    @Test
    fun test() {
        val quantities = CardQuantities.new()
        assertEquals(0, quantities[ECard.BURROWING])
        quantities += ECard.BURROWING
        assertEquals(1, quantities[ECard.BURROWING])
    }
}