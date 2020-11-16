package inego.evo.cards

import java.util.*
import java.util.Collections.shuffle
import kotlin.collections.ArrayList

/**
 * A wrapper around an `IntArray` representing counts of game cards in some context with useful helper methods.
 */
data class CardQuantities(val quantities: IntArray) {
    val totalCount: Int
        get() = quantities.sum()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CardQuantities

        if (!quantities.contentEquals(other.quantities)) return false

        return true
    }

    override fun hashCode(): Int {
        return quantities.contentHashCode()
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

    fun toListOfCards(random: Random): MutableList<ECard> {
        val result = ArrayList<ECard>(quantities.sum())
        for (i in ECard.array.indices) {
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

