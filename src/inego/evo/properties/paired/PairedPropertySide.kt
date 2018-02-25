package inego.evo.properties.paired

import inego.evo.game.Animal
import inego.evo.game.ConnectionMembership
import inego.evo.game.GameState
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
    open fun mayBeAttackedBy(attacker: Animal, other: Animal) = false

    /**
     * Checks whether the animal possessing this paired property side may eat (receive red or blue food tokens)
     */
    // TODO call this from all appropriate places
    open fun mayEat(other: Animal) = true
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
    fun isApplicable(gameState: GameState) = true

    /**
     * Creates a food propagation move based on a specified connection membership.
     */
    fun createPropagationMove(connectionMembership: ConnectionMembership): FoodPropagationMove
}