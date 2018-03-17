package inego.evo.test

import inego.evo.game.Game
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class GameTests {

    @Test
    fun newGameDeckSize() {
        val g = Game.new(0, false, Random())
        assertEquals(84, g.deck.size)
    }

    @Test
    fun fromFirstPlayerSequence() {
        val g = Game.new(2, false, Random())
        val iterator = g.fromFirstPlayer()

        iterator.assertNext(g.players[0])
        iterator.assertNext(g.players[1])
        iterator.assertNext(null)
    }

    @Test
    fun fromFirstPlayerSequence2() {
        val g = Game.new(2, false, Random())
        g.firstPlayerIdx = 1
        val iterator = g.fromFirstPlayer()

        iterator.assertNext(g.players[1])
        iterator.assertNext(g.players[0])
        iterator.assertNext(null)
    }

}