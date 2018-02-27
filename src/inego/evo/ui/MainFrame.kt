package inego.evo.ui

import inego.evo.GameManager
import inego.evo.RandomEngine
import inego.evo.game.Game
import inego.evo.game.MoveSelection
import inego.evo.game.moves.GameStartMove
import inego.evo.game.moves.Move
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*


object MainFrame {

    private val nextButton = JButton("Next").apply {

        addActionListener {
            handleNextMove(choicesList.selectedValue)
        }

    }

    private val game = Game.new(2, true)

    private val gameManager = GameManager(game).apply {
        setEngine(0, RandomEngine)
    }


    private val gameBoard = GameBoardComponent(gameManager).apply {
        preferredSize = Dimension(0, 0)
    }

    private val logListModel = DefaultListModel<String>()

    private val logList = JList<String>(logListModel)

    private val logListScrollPane = JScrollPane().apply {
        preferredSize = Dimension(0, 0)
        setViewportView(logList)
    }

    private var moveSelection: MoveSelection<*>? = null

    private val choicesListModel = DefaultListModel<Move>()

    private val choicesList = JList<Move>(choicesListModel).apply {
        selectionMode = ListSelectionModel.SINGLE_SELECTION

    }

    private val choicesListScrollPane = JScrollPane().apply {
        preferredSize = Dimension(0, 0)
        setViewportView(choicesList)
    }

    private fun setCurrentMoves(newMoveSelection: MoveSelection<*>?) {
        choicesListModel.clear()
        if (newMoveSelection == null) {
            nextButton.text = "Next"
        }
        else {
            newMoveSelection.moves.forEach { choicesListModel.addElement(it) }
            nextButton.text = "${newMoveSelection.decidingPlayer}: Next"
        }
        moveSelection = newMoveSelection
        choicesList.selectedIndex = 0
        gameBoard.repaint()
    }

    private fun handleNextMove(move: Move) {
        val nextMoves = gameManager.next(move)
        val newLogMessages = game.takeFromLog()
        for (newLogMessage in newLogMessages) {
            logListModel.addElement(newLogMessage)
        }

        SwingUtilities.invokeLater {
            // Scroll to the bottom
            with (logListScrollPane.verticalScrollBar) {
                value = maximum
            }
        }

        setCurrentMoves(nextMoves)
    }

    init {
        choicesList.cellRenderer = MoveListCellRenderer {
            it.toString(moveSelection!!.decidingPlayer)
        }

        choicesList.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (e.clickCount == 2) {
                    nextButton.doClick()
                }
            }
        })

        handleNextMove(GameStartMove)
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
            weightx = 0.9
            fill = GridBagConstraints.BOTH
            gridheight = 3
        }

        add(logListScrollPane, 1, 0) {
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

        add(choicesListScrollPane, 1, 2) {
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