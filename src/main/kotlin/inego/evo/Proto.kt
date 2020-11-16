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
import inego.evo.proto.*
import inego.evo.proto.Animal as ProtoAnimal
import inego.evo.proto.Game as ProtoGame
import inego.evo.proto.GamePhase as ProtoGamePhase
import inego.evo.proto.PairedProperty as ProtoPairedProperty
import inego.evo.proto.Player as ProtoPlayer

fun ECard.toProto(): Card = when (this) {
    ECard.CAMOUFLAGE -> Card.CAMOUFLAGE
    ECard.BURROWING -> Card.BURROWING
    ECard.SHARP_VISION -> Card.SHARP_VISION
    ECard.SYMBIOSIS -> Card.SYMBIOSIS
    ECard.PIRACY -> Card.PIRACY
    ECard.GRAZING -> Card.GRAZING
    ECard.TAIL_LOSS -> Card.TAIL_LOSS
    ECard.HIBERNATION -> Card.HIBERNATION
    ECard.POISONOUS -> Card.POISONOUS
    ECard.COMMUNICATION -> Card.COMMUNICATION
    ECard.SCAVENGER -> Card.SCAVENGER
    ECard.RUNNING -> Card.RUNNING
    ECard.MIMICRY -> Card.MIMICRY
    ECard.SWIMMING -> Card.SWIMMING
    ECard.PARASITE__CARNIVOROUS -> Card.PARASITE__CARNIVOROUS
    ECard.PARASITE__FAT_TISSUE -> Card.PARASITE__FAT_TISSUE
    ECard.COOPERATION__CARNIVOROUS -> Card.COOPERATION__CARNIVOROUS
    ECard.COOPERATION__FAT_TISSUE -> Card.COOPERATION__FAT_TISSUE
    ECard.BIG__CARNIVOROUS -> Card.BIG__CARNIVOROUS
    ECard.BIG__FAT_TISSUE -> Card.BIG__FAT_TISSUE
}

fun IndividualPropertyEnum.toProto(): IndividualProperty = when (this) {
    IndividualPropertyEnum.BIG -> IndividualProperty.IP_BIG
    IndividualPropertyEnum.BURROWING -> IndividualProperty.IP_BURROWING
    IndividualPropertyEnum.CAMOUFLAGE -> IndividualProperty.IP_CAMOUFLAGE
    IndividualPropertyEnum.CARNIVOROUS -> IndividualProperty.IP_CARNIVOROUS
    IndividualPropertyEnum.FAT_TISSUE -> IndividualProperty.IP_FAT_TISSUE
    IndividualPropertyEnum.GRAZING -> IndividualProperty.IP_GRAZING
    IndividualPropertyEnum.HIBERNATION -> IndividualProperty.IP_HIBERNATION
    IndividualPropertyEnum.MIMICRY -> IndividualProperty.IP_MIMICRY
    IndividualPropertyEnum.PARASITE -> IndividualProperty.IP_PARASITE
    IndividualPropertyEnum.PIRACY -> IndividualProperty.IP_PIRACY
    IndividualPropertyEnum.POISONOUS -> IndividualProperty.IP_POISONOUS
    IndividualPropertyEnum.RUNNING -> IndividualProperty.IP_RUNNING
    IndividualPropertyEnum.SCAVENGER -> IndividualProperty.IP_SCAVENGER
    IndividualPropertyEnum.SHARP_VISION -> IndividualProperty.IP_SHARP_VISION
    IndividualPropertyEnum.SWIMMING -> IndividualProperty.IP_SWIMMING
    IndividualPropertyEnum.TAIL_LOSS -> IndividualProperty.IP_TAIL_LOSS
}


fun Animal.toProto(): ProtoAnimal {
    val b = ProtoAnimal.newBuilder()

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


fun CardQuantities.toProto(): List<CardQuantity> {

    return quantities.withIndex()
            .filter { it.value > 0 }
            .map {
                CardQuantity.newBuilder()
                        .setCard(ECard.array[it.index].toProto())
                        .setQuantity(it.value)
                        .build()
            }
}

fun Player.toProto(): ProtoPlayer {
    val b = ProtoPlayer.newBuilder()

    b.passed = passed
    b.discardSize = discardSize

    val protoAnimals = animals.map { it.toProto() }

    b.addAllHand(hand.map { it.toProto() })

    b.addAllCardsPlayedAsAnimals(cardsPlayedAsAnimals.toProto())
    b.addAllAnimals(protoAnimals)

    val protoConnections = connections.map {
        val cb = Connection.newBuilder()
                .setFirstAnimal(animals.indexOf(it.animal1))
                .setSecondAnimal(animals.indexOf(it.animal2))
                .setProperty(it.property.toProto())
        if (it.isUsed)
            cb.isUsed = true
        cb.build()
    }

    b.addAllConnections(protoConnections)

    val protoFoodPropagation = foodPropagationSet.map {
        ConnectionMembership.newBuilder()
                .setConnection(connections.indexOf(it.connection))
                .setHost(it.host).build()
    }

    b.addAllFoodPropagationSet(protoFoodPropagation)

    return b.build()
}

private fun PairedProperty.toProto(): ProtoPairedProperty = when (this) {
    CommunicationProperty -> ProtoPairedProperty.PP_COMMUNICATION
    CooperationProperty -> ProtoPairedProperty.PP_COOPERATION
    SymbiosisProperty -> ProtoPairedProperty.PP_SYMBIOSIS
    else -> throw AssertionError()
}

fun Game.toProto(): ProtoGame {
    val b = ProtoGame.newBuilder()

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

private fun GamePhase.toProto(): ProtoGamePhase = when (this) {
    GamePhase.DEVELOPMENT -> ProtoGamePhase.PH_DEVELOPMENT
    GamePhase.FEEDING_BASE_DETERMINATION -> ProtoGamePhase.PH_FEEDING_BASE_DETERMINATION
    GamePhase.FEEDING -> ProtoGamePhase.PH_FEEDING
    GamePhase.DEFENSE -> ProtoGamePhase.PH_DEFENSE
    GamePhase.FOOD_PROPAGATION -> ProtoGamePhase.PH_FOOD_PROPAGATION
    GamePhase.GRAZING -> ProtoGamePhase.PH_GRAZING
    GamePhase.EXTINCTION -> ProtoGamePhase.PH_EXTINCTION
    GamePhase.END -> ProtoGamePhase.PH_END
}

fun Game.getProtoReference(animal: Animal): AnimalReference =
        AnimalReference.newBuilder()
                .setOwner(players.indexOf(animal.owner))
                .setAnimal(animal.owner.animals.indexOf(animal))
                .build()