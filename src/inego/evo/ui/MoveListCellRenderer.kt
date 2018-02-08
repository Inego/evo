package inego.evo.ui

import inego.evo.game.moves.Move
import java.awt.Component
import javax.swing.DefaultListCellRenderer
import javax.swing.JLabel
import javax.swing.JList

class MoveListCellRenderer(private val moveToString: (move: Move) -> String) : DefaultListCellRenderer() {

    override fun getListCellRendererComponent(
            list: JList<*>?,
            value: Any?,
            index: Int,
            isSelected: Boolean,
            cellHasFocus: Boolean
    ): Component {

        val component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus) as JLabel

        component.text = moveToString(value as Move)

        return component
    }
}