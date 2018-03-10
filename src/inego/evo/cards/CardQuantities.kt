package inego.evo.cards

import java.util.*
import java.util.Collections.shuffle
import java.util.concurrent.ThreadLocalRandom
import kotlin.collections.ArrayList

data class CardQuantities(private val quantities: IntArray) {
    val totalCount: Int
        get() = quantities.sum()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CardQuantities

        if (!Arrays.equals(quantities, other.quantities)) return false

        return true
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(quantities)
    }

    operator fun plusAssign(eCard: ECard) {
        quantities[eCard.ordinal]++
    }

    operator fun minusAssign(eCard: ECard) {
        quantities[eCard.ordinal]--
    }

    operator fun get(eCard: ECard) = quantities[eCard.ordinal]

    fun clone() = CardQuantities(quantities.clone())

    operator fun minusAssign(other: CardQuantities) {
        for ((index, quantity) in other.quantities.withIndex()) {
            quantities[index] -= quantity
        }
    }

    operator fun minusAssign(cards: List<ECard>) {
        for (card in cards) {
            this -= card
        }
    }

    fun toListOfCards(random: Random = ThreadLocalRandom.current()): MutableList<ECard> {
        val result = ArrayList<ECard>(quantities.sum())
        for (i in 0 until ECard.array.size) {
            val quantity = quantities[i]
            if (quantity > 0) {
                val eCard = ECard.array[i]
                repeat(quantity) {
                    result.add(eCard)
                }
            }
        }
        shuffle(result, random)
        return result
    }

    companion object {
        fun new(): CardQuantities {
            val zeroes = IntArray(ECard.initialQuantities.size)
            return CardQuantities(zeroes)
        }
    }
}

