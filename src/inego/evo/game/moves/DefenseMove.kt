package inego.evo.game.moves

import inego.evo.game.Animal
import inego.evo.game.MoveSelection
import inego.evo.game.PlayerState

abstract class DefenseMove(val defender: Animal, val attacker: Animal) : Move()


class DefenseMoveSelection(decidingPlayer: PlayerState, moves: List<DefenseMove>)
    : MoveSelection<DefenseMove>(decidingPlayer, moves)
