import game.Animal
import game.GameState
import game.moves.GameStartMove
import game.moves.Move
import java.awt.*
import javax.swing.*


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

object GridBagLayoutDemo {

    private val logList = JList<String>()
    private val choicesList = JList<Move>()

    private val gameState = GameState.new(2).apply {
        next(GameStartMove)
    }

    private val gameBoard = GameBoardComponent(gameState).apply {
        preferredSize = Dimension(800, 600)
    }

    private fun addComponentsToPane(pane: Container) {

        pane.layout = GridBagLayout()

        fun add(component: Component, x: Int, y: Int, constraintsModifier: GridBagConstraints.() -> Unit) {
            with(GridBagConstraints()) {
                gridx = x
                gridy = y
                constraintsModifier()
                pane.add(component, this)
            }
        }

        add(gameBoard, 0, 0) {
            weightx = 1.0
            fill = GridBagConstraints.BOTH
            gridheight = 3
        }

        add(logList, 1, 0) {
            weightx = 0.2
            weighty = 0.5
            gridwidth = 2
            fill = GridBagConstraints.BOTH
        }

        add(JButton("Next"), 1, 1) {
            weightx = 0.1
            anchor = GridBagConstraints.CENTER
        }

        add(JButton("Skip"), 2, 1) {
            weightx = 0.1
            anchor = GridBagConstraints.CENTER
        }

        add(choicesList, 1, 2) {
            weighty = 0.5
            gridwidth = 2
            fill = GridBagConstraints.BOTH
        }

    }

    private fun createAndShowGUI() {
        with(JFrame("Evolution")) {
            extendedState = extendedState or JFrame.MAXIMIZED_BOTH

            defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            addComponentsToPane(contentPane)
            pack()
            isVisible = true
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        SwingUtilities.invokeLater { createAndShowGUI() }
    }
}