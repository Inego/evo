package ui

import cards.DoubleCard
import cards.SingleCard
import game.GameState
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JPanel

class GameBoardComponent(var gameState: GameState) : JPanel() {
    companion object {
        const val SPACER_HEIGHT = 2
        const val PREFERRED_ROW_HEIGHT = 30.0
        const val HORIZONTAL_MARGIN = 10
        const val CARD_SPACER_RATIO = 0.1
        const val PREFERRED_CARD_WIDTH = 100.0
        const val PREFERRED_CARD_SPACER = 10.0

    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2 = g as Graphics2D

        val metrics = g2.fontMetrics
        val fontHeight = metrics.ascent + metrics.descent

        val numberOfPlayers = gameState.numberOfPlayers

        val playerHeight = height.toDouble() / numberOfPlayers

        for ((playerIndex, player) in gameState.players.withIndex()) {

            /* The structure of the player's space:

            Info row
            <spacer>
            Hand
            <spacer>
            Animals w/ their properties

            */

            val playerSpaceStart = height.toDouble() * playerIndex / numberOfPlayers

            val playerRows = 2 * SPACER_HEIGHT + 3 + (player.animals.map { it.propertyCount }.max() ?: 0)
            val rowHeight = Math.min(playerHeight / playerRows, PREFERRED_ROW_HEIGHT)

            // Count card width (incl. spacing)

            val cardColumns = Math.max(player.hand.size, player.animals.size)

            val rawColumnWidth = (width - 2 * HORIZONTAL_MARGIN)

            val cardWidth = Math.min((rawColumnWidth * (1.0 - CARD_SPACER_RATIO)), PREFERRED_CARD_WIDTH)
            val cardSpacer = Math.min(rawColumnWidth * CARD_SPACER_RATIO, PREFERRED_CARD_SPACER)

            val cardColumnWidth = cardWidth + cardSpacer

            fun cardIdxToScreenX(idx: Int): Int = (HORIZONTAL_MARGIN + cardColumnWidth * idx).toInt()


            val rowTextBase = rowHeight * metrics.ascent / fontHeight

            val topRowBase = (playerSpaceStart + rowTextBase).toInt()

            g2.drawString(player.name, HORIZONTAL_MARGIN, topRowBase)

            // Draw hand

            val handRowStart = playerSpaceStart + rowHeight * (1 + SPACER_HEIGHT)
            val handRowBase = (handRowStart + rowTextBase).toInt()

            for ((index, card) in player.hand.withIndex()) {
                val cardStartX = cardIdxToScreenX(index)

                when (card) {
                    is SingleCard -> g2.drawString(card.name, cardStartX, handRowBase)
                    is DoubleCard -> {
                        g2.drawString(card.firstProperty.name, cardStartX, handRowBase)
                        g2.drawString(card.secondProperty.name, cardStartX, handRowBase + fontHeight)
                    }
                }


            }

        }

    }
}