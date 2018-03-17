package inego.evo.ui

import inego.evo.game.ConnectionMembership
import inego.evo.properties.IndividualProperty
import inego.evo.properties.individual.CarnivorousProperty
import inego.evo.properties.individual.SwimmingProperty
import java.awt.Color

val BROWN = Color(137, 67, 27)
val FAT_TISSUE_BG = Color(255, 231, 168)

sealed class DrawableProperty {
    open val fgColor: Color
        get() = Color.BLACK

    open val bgColor: Color
        get() = Color.WHITE
}

class IndividualDrawableProperty(val individualProperty: IndividualProperty) : DrawableProperty() {
    override val fgColor: Color
        get() = when (individualProperty) {
            CarnivorousProperty -> Color.WHITE
            SwimmingProperty -> Color.CYAN
            else -> super.fgColor
        }

    override val bgColor: Color
        get() = when (individualProperty) {
            CarnivorousProperty -> Color.RED
            SwimmingProperty -> Color.BLUE
            else -> super.bgColor
        }
}

class ConnectionMembershipDrawableProperty(val connectionMembership: ConnectionMembership) : DrawableProperty()

class FatTissueDrawableProperty(val size: Int) : DrawableProperty() {
    override val fgColor: Color
        get() = BROWN
    override val bgColor: Color
        get() = FAT_TISSUE_BG
}
