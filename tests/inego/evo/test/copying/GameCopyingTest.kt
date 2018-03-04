package inego.evo.test.copying

import inego.evo.from
import inego.evo.game.Game
import inego.evo.game.GameCopier
import inego.evo.game.MoveSelection
import inego.evo.game.moves.GameStartMove
import inego.evo.game.moves.Move
import org.junit.jupiter.api.Test
import java.util.*

class GameCopyingTest {

    @Test
    fun testGameCopyAndModification() {

        val r = Random()
        r.setSeed(1)

        val oldGame = Game.new(2, false, random = r)

        val p1 = oldGame.players[0]
        val p2 = oldGame.players[1]

        var move: Move = GameStartMove
        var selection: MoveSelection<*>? = null

        var counter = 0

        do {
            selection = oldGame.next(move)
            if (selection == null) break
            println("$counter. T${oldGame.turnNumber} ${selection.decidingPlayer}: $selection")
            move = r.from(selection.moves)
            counter++
        } while (counter < 100)

        val copier = GameCopier(oldGame)

        val copy = copier.copy


        println("P1 = ${p1.score}")
        println("P2 = ${p2.score}")
    }
}