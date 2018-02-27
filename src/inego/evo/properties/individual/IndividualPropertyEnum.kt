package inego.evo.properties.individual

import inego.evo.properties.IndividualProperty

enum class IndividualPropertyEnum(val individualProperty: IndividualProperty) {
    BIG(BigProperty),
    BURROWING(BurrowingProperty),
    CAMOUFLAGE(CamouflageProperty),
    CARNIVOROUS(CarnivorousProperty),
    FAT_TISSUE(FatTissueProperty),
    GRAZING(GrazingProperty),
    HIBERNATION(HibernationProperty),
    MIMICRY(MimicryProperty),
    PARASITE(ParasiteProperty),
    PIRACY(PiracyProperty),
    POISONOUS(PoisonousProperty),
    RUNNING(RunningProperty),
    SCAVENGER(ScavengerProperty),
    SHARP_VISION(SharpVisionProperty),
    SWIMMING(SwimmingProperty),
    TAIL_LOSS(TailLossProperty)
}