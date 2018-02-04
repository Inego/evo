package ui

import game.GameState
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JPanel

class GameBoardComponent(var gameState: GameState) : JPanel() {
    companion object {
        const val SPACER_HEIGHT = 2
        const val PREFERRED_ROW_HEIGHT = 30
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2 = g as Graphics2D

        val metrics = g2.fontMetrics
        val fontHeight = metrics.ascent + metrics.descent

        val numberOfPlayers = gameState.numberOfPlayers

        val playerHeight = height / numberOfPlayers

        for ((playerIndex, player) in gameState.players.withIndex()) {

            /* The structure of the player's space:

            Info row
            <spacer>
            Hand
            <spacer>
            Animals w/ their properties

            */

            val playerSpaceStart = height * playerIndex / numberOfPlayers

            val playerRows = 2 * SPACER_HEIGHT + 3 + (player.animals.map { it.propertyCount }.max() ?: 0)
            val rowHeight = Math.min(playerHeight / playerRows, PREFERRED_ROW_HEIGHT)

            val rowTextBase = rowHeight * metrics.ascent / fontHeight

            g2.drawString(player.name, 0, playerSpaceStart + rowTextBase)

        }

    }
}