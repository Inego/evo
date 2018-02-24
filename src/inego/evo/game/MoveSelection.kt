package inego.evo.game

import inego.evo.game.moves.Move

abstract class MoveSelection<out T : Move>(val decidingPlayer: PlayerState, val moves: List<T>)
