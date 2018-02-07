package inego.evo

import inego.evo.game.GameState
import inego.evo.game.moves.GameStartMove
import inego.evo.game.moves.Move

fun main(args: Array<String>) {
    val gameState = GameState.new(2)

    var nextMove: Move = GameStartMove

    do {
        val moves = gameState.next(nextMove)

        if (moves.isEmpty()) break

        nextMove = moves.getRandomElement()

    } while (true)
}
