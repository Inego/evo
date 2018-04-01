package inego.evo

import inego.evo.cards.CardQuantities
import inego.evo.cards.ECard
import inego.evo.game.Animal
import inego.evo.game.Game
import inego.evo.game.GamePhase
import inego.evo.game.Player
import inego.evo.properties.PairedProperty
import inego.evo.properties.individual.IndividualPropertyEnum
import inego.evo.properties.paired.asymmetric.SymbiosisProperty
import inego.evo.properties.paired.symmetric.CommunicationProperty
import inego.evo.properties.paired.symmetric.CooperationProperty
import inego.evo.proto.GameProtos

fun ECard.toProto(): GameProtos.Card = when (this) {
    ECard.CAMOUFLAGE -> GameProtos.Card.CAMOUFLAGE
    ECard.BURROWING -> GameProtos.Card.BURROWING
    ECard.SHARP_VISION -> GameProtos.Card.SHARP_VISION
    ECard.SYMBIOSIS -> GameProtos.Card.SYMBIOSIS
    ECard.PIRACY -> GameProtos.Card.PIRACY
    ECard.GRAZING -> GameProtos.Card.GRAZING
    ECard.TAIL_LOSS -> GameProtos.Card.TAIL_LOSS
    ECard.HIBERNATION -> GameProtos.Card.HIBERNATION
    ECard.POISONOUS -> GameProtos.Card.POISONOUS
    ECard.COMMUNICATION -> GameProtos.Card.COMMUNICATION
    ECard.SCAVENGER -> GameProtos.Card.SCAVENGER
    ECard.RUNNING -> GameProtos.Card.RUNNING
    ECard.MIMICRY -> GameProtos.Card.MIMICRY
    ECard.SWIMMING -> GameProtos.Card.SWIMMING
    ECard.PARASITE__CARNIVOROUS -> GameProtos.Card.PARASITE__CARNIVOROUS
    ECard.PARASITE__FAT_TISSUE -> GameProtos.Card.PARASITE__FAT_TISSUE
    ECard.COOPERATION__CARNIVOROUS -> GameProtos.Card.COOPERATION__CARNIVOROUS
    ECard.COOPERATION__FAT_TISSUE -> GameProtos.Card.COOPERATION__FAT_TISSUE
    ECard.BIG__CARNIVOROUS -> GameProtos.Card.BIG__CARNIVOROUS
    ECard.BIG__FAT_TISSUE -> GameProtos.Card.BIG__FAT_TISSUE
}

fun IndividualPropertyEnum.toProto(): GameProtos.IndividualProperty = when (this) {
    IndividualPropertyEnum.BIG -> GameProtos.IndividualProperty.IP_BIG
    IndividualPropertyEnum.BURROWING -> GameProtos.IndividualProperty.IP_BURROWING
    IndividualPropertyEnum.CAMOUFLAGE -> GameProtos.IndividualProperty.IP_CAMOUFLAGE
    IndividualPropertyEnum.CARNIVOROUS -> GameProtos.IndividualProperty.IP_CARNIVOROUS
    IndividualPropertyEnum.FAT_TISSUE -> GameProtos.IndividualProperty.IP_FAT_TISSUE
    IndividualPropertyEnum.GRAZING -> GameProtos.IndividualProperty.IP_GRAZING
    IndividualPropertyEnum.HIBERNATION -> GameProtos.IndividualProperty.IP_HIBERNATION
    IndividualPropertyEnum.MIMICRY -> GameProtos.IndividualProperty.IP_MIMICRY
    IndividualPropertyEnum.PARASITE -> GameProtos.IndividualProperty.IP_PARASITE
    IndividualPropertyEnum.PIRACY -> GameProtos.IndividualProperty.IP_PIRACY
    IndividualPropertyEnum.POISONOUS -> GameProtos.IndividualProperty.IP_POISONOUS
    IndividualPropertyEnum.RUNNING -> GameProtos.IndividualProperty.IP_RUNNING
    IndividualPropertyEnum.SCAVENGER -> GameProtos.IndividualProperty.IP_SCAVENGER
    IndividualPropertyEnum.SHARP_VISION -> GameProtos.IndividualProperty.IP_SHARP_VISION
    IndividualPropertyEnum.SWIMMING -> GameProtos.IndividualProperty.IP_SWIMMING
    IndividualPropertyEnum.TAIL_LOSS -> GameProtos.IndividualProperty.IP_TAIL_LOSS
}


fun Animal.toProto(): GameProtos.Animal {
    val b = GameProtos.Animal.newBuilder()

    b.addAllIndividualProperties(individualProperties.map { it.toProto() })
    if (fatCapacity > 0)
        b.fatCapacity = fatCapacity

    if (fat > 0)
        b.fat = fat

    b.foodRequirement = foodRequirement

    if (hasFood > 0)
        b.hasFood = hasFood

    if (usedPiracy)
        b.usedPiracy = usedPiracy

    if (usedAttack)
        b.usedAttack = usedAttack

    if (usedRunningAway)
        b.usedRunningAway = usedRunningAway

    if (isHibernating)
        b.isHibernating = isHibernating

    if (hibernatedLastTurn)
        b.hibernatedLastTurn = hibernatedLastTurn

    if (isPoisoned)
        b.isPoisoned = isPoisoned

    return b.build()
}


fun CardQuantities.toProto(): List<GameProtos.CardQuantity> {

    return quantities.withIndex()
            .filter { it.value > 0 }
            .map {
                GameProtos.CardQuantity.newBuilder()
                        .setCard(ECard.array[it.index].toProto())
                        .setQuantity(it.value)
                        .build()
            }
}

fun Player.toProto(): GameProtos.Player {
    val b = GameProtos.Player.newBuilder()

    b.passed = passed
    b.discardSize = discardSize

    val protoAnimals = animals.map { it.toProto() }

    b.addAllHand(hand.map { it.toProto() })

    b.addAllCardsPlayedAsAnimals(cardsPlayedAsAnimals.toProto())
    b.addAllAnimals(protoAnimals)

    val protoConnections = connections.map {
        val cb = GameProtos.Connection.newBuilder()
                .setFirstAnimal(animals.indexOf(it.animal1))
                .setSecondAnimal(animals.indexOf(it.animal2))
                .setProperty(it.property.toProto())
        if (it.isUsed)
            cb.isUsed = true
        cb.build()
    }

    b.addAllConnections(protoConnections)

    val protoFoodPropagation = foodPropagationSet.map {
        GameProtos.ConnectionMembership.newBuilder()
                .setConnection(connections.indexOf(it.connection))
                .setHost(it.host).build()
    }

    b.addAllFoodPropagationSet(protoFoodPropagation)

    return b.build()
}

private fun PairedProperty.toProto(): GameProtos.PairedProperty = when (this) {
    CommunicationProperty -> GameProtos.PairedProperty.PP_COMMUNICATION
    CooperationProperty -> GameProtos.PairedProperty.PP_COOPERATION
    SymbiosisProperty -> GameProtos.PairedProperty.PP_SYMBIOSIS
    else -> throw AssertionError()
}

fun Game.toProto(): GameProtos.Game {
    val b = GameProtos.Game.newBuilder()

    val protoPlayers = players.map { it.toProto() }
    b.addAllPlayers(protoPlayers)

    b.addAllSeenCards(seenCards.toProto())

    if (inactivePlayers > 0)
        b.inactivePlayers = inactivePlayers

    b.turnNumber = turnNumber
    b.firstPlayerIdx = firstPlayerIdx
    b.currentPlayerIdx = currentPlayerIdx
    b.computeNextPlayer = computeNextPlayer
    b.phase = phase.toProto()

    val attackingAnimal = attackingAnimal

    if (attackingAnimal != null) {
        b.attackingAnimal = getProtoReference(attackingAnimal)
        b.defendingAnimal = getProtoReference(defendingAnimal!!)
    }

    b.foodBase = foodBase

    return b.build()
}

private fun GamePhase.toProto(): GameProtos.GamePhase = when (this) {
    GamePhase.DEVELOPMENT -> GameProtos.GamePhase.PH_DEVELOPMENT
    GamePhase.FEEDING_BASE_DETERMINATION -> GameProtos.GamePhase.PH_FEEDING_BASE_DETERMINATION
    GamePhase.FEEDING -> GameProtos.GamePhase.PH_FEEDING
    GamePhase.DEFENSE -> GameProtos.GamePhase.PH_DEFENSE
    GamePhase.FOOD_PROPAGATION -> GameProtos.GamePhase.PH_FOOD_PROPAGATION
    GamePhase.GRAZING -> GameProtos.GamePhase.PH_GRAZING
    GamePhase.EXTINCTION -> GameProtos.GamePhase.PH_EXTINCTION
    GamePhase.END -> GameProtos.GamePhase.PH_END
}

fun Game.getProtoReference(animal: Animal): GameProtos.AnimalReference =
        GameProtos.AnimalReference.newBuilder()
                .setOwner(players.indexOf(animal.owner))
                .setAnimal(animal.owner.animals.indexOf(animal))
                .build()