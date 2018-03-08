package inego.evo.game

import inego.evo.game.moves.Move

abstract class MoveSelection<out T : Move>(val decidingPlayer: Player, val moves: List<T>)
    : List<T> by moves
