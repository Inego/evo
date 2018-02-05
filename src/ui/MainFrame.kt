package ui

import game.GameState
import game.moves.GameStartMove
import game.moves.Move
import java.awt.*
import javax.swing.*


object MainFrame {

    private val logList = JList<String>()

    private val choicesListModel = DefaultListModel<Move>()

    private val choicesList = JList<Move>(choicesListModel).apply {
        selectionMode = ListSelectionModel.SINGLE_SELECTION
    }

    private fun setCurrentMoves(moves: List<Move>) {
        choicesListModel.clear()
        moves.forEach { choicesListModel.addElement(it) }
        choicesList.selectedIndex = 0
    }

    private val gameState = GameState.new(2)

    private fun handleNextMove(move: Move) {
        val nextMoves = gameState.next(move)
        setCurrentMoves(nextMoves)
    }

    init {
        handleNextMove(GameStartMove)
    }

    private val gameBoard = GameBoardComponent(gameState).apply {
        preferredSize = Dimension(800, 600)
    }

    private val nextButton = JButton("Next").apply {

        addActionListener {
            handleNextMove(choicesList.selectedValue)
        }

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

        add(nextButton, 1, 1) {
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