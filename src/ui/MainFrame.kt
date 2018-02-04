package ui

import game.GameState
import game.moves.GameStartMove
import game.moves.Move
import java.awt.*
import javax.swing.*


object MainFrame {

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