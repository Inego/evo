package inego.evo.game

import inego.evo.cards.ECard


class GameCopier(private val src: Game, val forPlayer: Player) {

    val unseenCards = ECard.createInitialQuantities().let {
        it -= src.seenCards
        it -= forPlayer.cardsPlayedAsAnimals
        it -= forPlayer.hand
        it.toListOfCards().iterator()
    }

    fun takeUnseenCards(size: Int): MutableList<ECard> = ArrayList<ECard>(size).apply {
        repeat(size) {
            add(unseenCards.next())
        }
    }

    private val animals: MutableMap<Animal, Animal> = mutableMapOf()
    private val memberships: MutableMap<ConnectionMembership, ConnectionMembership> = mutableMapOf()

    private val players = src.players.associateBy({ it }) { it.clone(this) }

    val copiedGame = Game(this, src)

    val copiedPlayers: List<Player>
        get() = players.values.toList()

    operator fun get(animal: Animal) = animals.getValue(animal)
    operator fun set(srcAnimal: Animal, copiedAnimal: Animal) = animals.put(srcAnimal, copiedAnimal)

    operator fun get(player: Player) = players.getValue(player)

    operator fun get(membership: ConnectionMembership) = memberships.getValue(membership)
    operator fun set(srcMembership: ConnectionMembership, copiedMembership: ConnectionMembership) =
            memberships.put(srcMembership, copiedMembership)

}
