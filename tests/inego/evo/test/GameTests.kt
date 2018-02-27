package inego.evo.test

import inego.evo.game.Game
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GameTests {

    @Test
    fun `New game deck size`() {
        val g = Game.new(0)
        Assertions.assertEquals(84, g.deck.size)
    }

    @Test
    fun fromFirstPlayerSequence() {
        val g = Game.new(2)
        val iterator = g.fromFirstPlayer()

        iterator.assertNext(g.players[0])
        iterator.assertNext(g.players[1])
        iterator.assertNext(null)
    }

    @Test
    fun fromFirstPlayerSequence2() {
        val g = Game.new(2)
        g.firstPlayerIdx = 1
        val iterator = g.fromFirstPlayer()

        iterator.assertNext(g.players[1])
        iterator.assertNext(g.players[0])
        iterator.assertNext(null)
    }

}