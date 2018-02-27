package inego.evo.properties.paired

import inego.evo.game.Animal
import inego.evo.game.ConnectionMembership
import inego.evo.game.Game
import inego.evo.game.moves.FoodPropagationMove


/**
 * A side of a paired property.
 */
abstract class PairedPropertySide(val name: String) {
    /**
     * Checks whether the animal possessing this paired property side may be attacked by an animal.
     *
     * @param attacker The animal whose attack is checked
     * @param other The other animal of the paired property
     */
    open fun mayBeAttackedBy(attacker: Animal, other: Animal) = true
}


/**
 * Implements game logic for food propagation possibility and execution in the form of moves.
 */
interface FoodPropagator {
    /**
     * Checks whether food propagation advertised by this propagator is applicable to the specified game state.
     *
     * It's true by default, but some propagators may perform additional checks.
     */
    fun isApplicable(game: Game) = true

    /**
     * Creates a food propagation move based on a specified connection membership.
     */
    fun createPropagationMove(connectionMembership: ConnectionMembership): FoodPropagationMove
}