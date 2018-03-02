package inego.evo.cards

import inego.evo.properties.AnimalProperty
import inego.evo.properties.individual.*
import inego.evo.properties.paired.asymmetric.SymbiosisProperty
import inego.evo.properties.paired.symmetric.CommunicationProperty
import inego.evo.properties.paired.symmetric.CooperationProperty

enum class ECard(
        val firstProperty: AnimalProperty<*, *>,
        val secondProperty: AnimalProperty<*, *>? = null,
        val startingQuantity: Int = 4
) {
    CAMOUFLAGE(CamouflageProperty),
    BURROWING(BurrowingProperty),
    SHARP_VISION(SharpVisionProperty),
    SYMBIOSIS(SymbiosisProperty),
    PIRACY(PiracyProperty),
    GRAZING(GrazingProperty),
    TAIL_LOSS(TailLossProperty),
    HIBERNATION(HibernationProperty),
    POISONOUS(PoisonousProperty),
    COMMUNICATION(CommunicationProperty),
    SCAVENGER(ScavengerProperty),
    RUNNING(RunningProperty),
    MIMICRY(MimicryProperty),
    SWIMMING(SwimmingProperty, startingQuantity = 8),
    PARASITE__CARNIVOROUS(ParasiteProperty, CarnivorousProperty),
    PARASITE__FAT_TISSUE(ParasiteProperty, FatTissueProperty),
    COOPERATION__CARNIVOROUS(CooperationProperty, CarnivorousProperty),
    COOPERATION__FAT_TISSUE(CooperationProperty, FatTissueProperty),
    BIG__CARNIVOROUS(BigProperty, CarnivorousProperty),
    BIG__FAT_TISSUE(BigProperty, FatTissueProperty);

    override fun toString(): String {
        return if (secondProperty == null) firstProperty.name else "${firstProperty.name} / ${secondProperty.name}"
    }
}
