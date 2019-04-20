package inego.evo.ui

import inego.evo.GameManager
import inego.evo.game.Animal
import inego.evo.properties.AsymmetricProperty
import inego.evo.properties.SymmetricProperty
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JPanel

class GameBoardComponent(private val gameManager: GameManager) : JPanel() {

    val game = gameManager.game

    companion object {
        const val SPACER_HEIGHT = 2
        const val PREFERRED_ROW_HEIGHT = 30.0
        const val HORIZONTAL_MARGIN = 10
        const val CARD_SPACER_RATIO = 0.1
        const val PREFERRED_CARD_WIDTH = 120.0
        const val PREFERRED_CARD_SPACER = 10.0
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2 = g as Graphics2D

        val defaultColor = g2.color

        val metrics = g2.fontMetrics
        val fontHeight = metrics.ascent + metrics.descent

        val numberOfPlayers = game.numberOfPlayers

        val playerHeight = height.toDouble() / numberOfPlayers

        for ((playerIndex, player) in game.players.withIndex()) {

            val isAI = gameManager.isAI(player)

            /* The structure of the player's space:

            Info row
            <spacer>
            Hand
            <spacer>
            Animals w/ their properties

            */

            val playerSpaceStart = height.toDouble() * playerIndex / numberOfPlayers

            if (player == gameManager.decidingPlayer) {
                g2.color = Color.BLUE
                g2.drawRect(1, playerSpaceStart.toInt(), width - 4, playerHeight.toInt() - 4)
                g2.color = defaultColor
            }

            val maxAnimalHeight = player.animals.map { it.propertyCount }.max() ?: 0
            val playerRows = 2 * SPACER_HEIGHT + 3 + maxAnimalHeight
            val rowHeight = Math.min(playerHeight / playerRows, PREFERRED_ROW_HEIGHT)

            // Count card width (incl. spacing)

            val cardColumns = Math.max(player.hand.size, player.animals.size)

            val columnWidth = (width - 2 * HORIZONTAL_MARGIN).toDouble() / cardColumns

            val cardWidth = Math.min((columnWidth * (1.0 - CARD_SPACER_RATIO)), PREFERRED_CARD_WIDTH)
            val cardWidthInt = cardWidth.toInt()

            val cardSpacer = Math.min(columnWidth * CARD_SPACER_RATIO, PREFERRED_CARD_SPACER)

            val cardColumn = cardWidth + cardSpacer

            fun cardIdxToScreenX(idx: Int): Int = (HORIZONTAL_MARGIN + cardColumn * idx).toInt()

            fun rowIdxToScreenY(idx: Int): Int = (playerSpaceStart + idx * rowHeight).toInt()


            val rowTextBase = (rowHeight - fontHeight) / 2 + metrics.ascent

            val topRowBase = (playerSpaceStart + rowTextBase).toInt()

            g2.drawString("${player.name} - ${player.score}  (discard: ${player.discardSize})", HORIZONTAL_MARGIN, topRowBase)

            // Common logic to draw a card
            fun drawCard(row: Int, col: Int, bgColor: Color? = null, insideDrawer: CardDrawingInfo.() -> Unit) {
                val xStart = cardIdxToScreenX(col)
                val yStart = rowIdxToScreenY(row)

                if (bgColor != null) {
                    g2.color = bgColor
                    g2.fillRect(xStart, yStart, cardWidthInt, rowHeight.toInt())
                    g2.color = defaultColor
                }
                g2.drawRect(xStart, yStart, cardWidthInt, rowHeight.toInt())

                CardDrawingInfo(xStart, yStart, rowTextBase).insideDrawer()

                g2.color = defaultColor
            }

            // Draw hand

            val handRow = 1 + SPACER_HEIGHT

            for ((index, card) in player.hand.withIndex()) {

                drawCard(handRow, index) {
                    if (!isAI) {
                        if (card.secondProperty == null) {
                            g2.drawString(card.firstProperty.name, textStartX, textStartY)
                        } else {
                            val middleBase = yStart + rowHeight / 2
                            g2.drawString(card.firstProperty.name, textStartX, (middleBase - metrics.descent).toInt())
                            g2.drawString(card.secondProperty.name, textStartX, (middleBase + metrics.ascent).toInt())
                        }
                    }
                }
            }

            // Draw animals w/ properties
            val animalRow = playerRows - 1
            for ((idx, animal) in player.animals.withIndex()) {
                drawCard(animalRow, idx, Color.DARK_GRAY) {
                    g2.color = Color.WHITE
                    g2.drawString("${idx + 1} ${animal.hasFood}/${animal.foodRequirement}", textStartX, textStartY)
                }

                for ((propIdx, drawableProperty) in animal.drawableProperties().withIndex()) {
                    drawCard(animalRow - 1 - propIdx, idx, drawableProperty.bgColor) {

                        g2.color = drawableProperty.fgColor

                        when (drawableProperty) {
                            is IndividualDrawableProperty ->
                                g2.drawString(drawableProperty.individualProperty.name, textStartX, textStartY)
                            is ConnectionMembershipDrawableProperty -> {
                                val membership = drawableProperty.connectionMembership
                                val side = when (val property = membership.property) {
                                    is SymmetricProperty -> property.side
                                    is AsymmetricProperty -> if (membership.host) property.host else property.guest
                                }
                                g2.drawString("${side.name} to ${membership.other}", textStartX, textStartY)
                            }
                            is FatTissueDrawableProperty ->
                                g2.drawString("FAT ${animal.fat}/${drawableProperty.size}", textStartX, textStartY)
                        }
                    }
                }
            }
        }
    }
}

class CardDrawingInfo(
        xStart: Int,
        val yStart: Int,
        rowTextBase: Double
) {
    val textStartX = 3 + xStart
    val textStartY = (yStart + rowTextBase).toInt()
}

fun Animal.drawableProperties() = sequence {
    if (fatCapacity > 0) {
        yield(FatTissueDrawableProperty(fatCapacity))
    }
    yieldAll(individualProperties.map { IndividualDrawableProperty(it.individualProperty) })
    yieldAll(connections.map { ConnectionMembershipDrawableProperty(it) })
}