package inego.evo.test

import inego.evo.game.Game
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class GameTests {

    @Test
    fun newGameDeckSize() {
        val g = Game.new(0, Random(), false)
        assertEquals(84, g.deck.size)
    }

    @Test
    fun fromFirstPlayerSequence() {
        val g = Game.new(2, Random(), false)
        val iterator = g.fromFirstPlayer()

        iterator.assertNext(g.players[0])
        iterator.assertNext(g.players[1])
        iterator.assertNext(null)
    }

    @Test
    fun fromFirstPlayerSequence2() {
        val g = Game.new(2, Random(), false)
        g.firstPlayerIdx = 1
        val iterator = g.fromFirstPlayer()

        iterator.assertNext(g.players[1])
        iterator.assertNext(g.players[0])
        iterator.assertNext(null)
    }

}