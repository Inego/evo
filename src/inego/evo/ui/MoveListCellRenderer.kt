package inego.evo.ui

import inego.evo.MoveWithStats
import inego.evo.game.moves.Move
import java.awt.Component
import java.awt.Font
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

        value as MoveWithStats

        val stats = value.stats

        val winRate = 100 * stats.wins.toDouble() / stats.playouts

        if (!value.isBest) {
            font = component.font
            component.font = font.deriveFont(Font.PLAIN)
        }

        component.text = "<html>${moveToString(value.move)} : ${winRate.format(4)} [${stats.wins}/${stats.playouts}] </html>"

        return component
    }
}