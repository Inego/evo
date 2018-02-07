package inego.evo.cards

import inego.evo.properties.AnimalProperty
import inego.evo.properties.individual.*
import inego.evo.properties.paired.asymmetric.SymbiosisProperty
import inego.evo.properties.paired.symmetric.CommunicationProperty
import inego.evo.properties.paired.symmetric.CooperationProperty

sealed class Card {
    abstract val name: String
    override fun toString() = name
}

abstract class SingleCard(val property: AnimalProperty<*, *>) : Card() {

    override val name: String
        get() = property.name
}

abstract class DoubleCard(
        val firstProperty: AnimalProperty<*, *>,
        val secondProperty: AnimalProperty<*, *>
) : Card() {

    override val name: String
        get() = "${firstProperty.name} / ${secondProperty.name}"

}

object CamouflageCard : SingleCard(CamouflageProperty)
object BurrowingCard : SingleCard(BurrowingProperty)
object SharpVisionCard : SingleCard(SharpVisionProperty)
object SymbiosisCard : SingleCard(SymbiosisProperty)
object PiracyCard : SingleCard(PiracyProperty)
object GrazingCard : SingleCard(GrazingProperty)
object TailLossCard : SingleCard(TailLossProperty)
object HibernationCard : SingleCard(HibernationProperty)
object PoisonousCard : SingleCard(PoisonousProperty)
object CommunicationCard : SingleCard(CommunicationProperty)
object ScavengerCard : SingleCard(ScavengerProperty)
object RunningCard : SingleCard(RunningProperty)
object MimicryCard : SingleCard(MimicryProperty)
object SwimmingCard : SingleCard(SwimmingProperty)
object ParasiteCarnivorousCard : DoubleCard(ParasiteProperty, CarnivorousProperty)
object ParasiteFatTissueCard : DoubleCard(ParasiteProperty, FatTissueProperty)
object CooperationCarnivorousCard : DoubleCard(CooperationProperty, CarnivorousProperty)
object CooperationFatTissueCard : DoubleCard(CooperationProperty, FatTissueProperty)
object BigCarnivorousCard : DoubleCard(BigProperty, CarnivorousProperty)
object BigFatTissueCard : DoubleCard(BigProperty, FatTissueProperty)
