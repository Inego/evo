package inego.evo.test

import inego.evo.game.GameState
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GameStateTests {

    @Test
    fun `New game deck size`() {
        val gs = GameState.new(0)
        Assertions.assertEquals(84, gs.deck.size)
    }

    @Test
    fun fromFirstPlayerSequence() {
        val gs = GameState.new(2)
        val iterator = gs.fromFirstPlayer()

        iterator.assertNext(gs.players[0])
        iterator.assertNext(gs.players[1])
        iterator.assertNext(null)
    }

    @Test
    fun fromFirstPlayerSequence2() {
        val gs = GameState.new(2)
        gs.firstPlayerIdx = 1
        val iterator = gs.fromFirstPlayer()

        iterator.assertNext(gs.players[1])
        iterator.assertNext(gs.players[0])
        iterator.assertNext(null)
    }

}