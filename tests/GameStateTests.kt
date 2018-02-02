import game.GameState
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GameStateTests {

    @Test
    fun `New game deck size`() {
        val gs = GameState.new(0)
        Assertions.assertEquals(84, gs.deck.size)
    }

}