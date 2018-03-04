package inego.evo.cards

import java.util.*

data class CardQuantities(private val quantities: ByteArray) {

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

    operator fun get(eCard: ECard) = quantities[eCard.ordinal]

    fun clone() = CardQuantities(quantities.clone())

    companion object {
        fun new(): CardQuantities {
            val q = ByteArray(ECard.values().size)
            return CardQuantities(q)
        }
    }
}

