package inego.evo.cards

class CardQuantities(initialCount: (ECard) -> Int) {
    private val quantities: Array<Int> = Array(ECard.values().size) { initialCount(ECard.values()[it]) }

    operator fun plusAssign(eCard: ECard) {
        quantities[eCard.ordinal]++
    }

    operator fun get(eCard: ECard) = quantities[eCard.ordinal]
}

