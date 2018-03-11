package inego.evo.ui

import inego.evo.*
import inego.evo.engines.PlayoutStatsEngine
import inego.evo.game.Game
import inego.evo.game.MoveSelection
import inego.evo.game.moves.GameStartMove
import inego.evo.game.moves.Move
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.*
import javax.swing.*
import javax.swing.Timer
import javax.swing.plaf.metal.MetalLookAndFeel


object MainFrame : GameFlowSubscriber {

    private val random = Random()

    private val playoutManager: PlayoutManager = PlayoutManager { RandomSyncEngine(Random(random.nextLong())) }

    private val statRefreshTimer = Timer(1000) {
        val newMoveStats = playoutManager.getCurrentMoveStats()

        val best = newMoveStats.values.map { it.playouts }.max() ?: throw AssertionError()

        var bestAssigned = false

        for (i in 0 until choicesListModel.size()) {
            val row = choicesListModel[i]
            val move = row.move
            val stats = row.stats

            val newRowStats = newMoveStats.getValue(move)

            stats.playouts = newRowStats.playouts
            stats.wins = newRowStats.wins
            row.isBest = best == newRowStats.playouts
            if (row.isBest) {
                bestAssigned = true
            }
        }

        if (!bestAssigned) {
            throw AssertionError()
        }

        choicesList.repaint()
    }

    private val nextButton = JButton("Next").apply {
        addActionListener {
            handleNextMove(choicesList.selectedValue.move)
        }
    }

    private val game = Game.new(3, true, Random())

    private val gameManager = GameManager(game).apply {
        setEngine(0, PlayoutStatsEngine(4000))
        setEngine(1, PlayoutStatsEngine(4000))
//        setEngine(2, PlayoutStatsEngine(300))
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

    private val choicesListModel = DefaultListModel<MoveWithStats>()

    private val choicesList = JList<MoveWithStats>(choicesListModel).apply {
        selectionMode = ListSelectionModel.SINGLE_SELECTION

    }

    private val choicesListScrollPane = JScrollPane().apply {
        preferredSize = Dimension(0, 0)
        setViewportView(choicesList)
    }

    private fun setCurrentMoves(newMoveSelection: MoveSelection<*>) {

        newMoveSelection.forEach { choicesListModel.addElement(MoveWithStats(
                it,
                PlayoutStats(0, 0), false)
        ) }

        moveSelection = newMoveSelection
        choicesList.selectedIndex = 0

        playoutManager.start(game, moveSelection!!)
        statRefreshTimer.start()

    }

    private fun handleNextMove(move: Move) {
        playoutManager.stop()
        statRefreshTimer.stop()

        gameManager.next(moveSelection?.decidingPlayer ?: game[0], move)
    }

    override fun onChoicePoint(moveSelection: MoveSelection<*>, forAI: Boolean) {

        refreshGameBoard()

        val nextButtonCaption = if (forAI) "Force!" else "Next"

        nextButton.text = "${moveSelection.decidingPlayer}: $nextButtonCaption"

        if (forAI) {
            // TODO Next button to "Force Move"

        }
        else {
            setCurrentMoves(moveSelection)
        }
    }

    private fun refreshGameBoard() {
        gameBoard.repaint()

        for (newLogMessage in game.takeFromLog()) {
            logListModel.addElement(newLogMessage)
        }

        SwingUtilities.invokeLater {
            // Scroll to the bottom
            with(logListScrollPane.verticalScrollBar) {
                value = maximum
            }
        }

        choicesListModel.clear()
    }

    override fun onGameOver() {
        refreshGameBoard()
    }

    init {

        gameManager.subscribe(this)

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

        // turn off bold fonts
        UIManager.put("swing.boldMetal", java.lang.Boolean.FALSE)

        // re-install the Metal Look and Feel
        UIManager.setLookAndFeel(MetalLookAndFeel())

        SwingUtilities.invokeLater { createAndShowGUI() }
    }
}