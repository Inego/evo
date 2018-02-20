package inego.evo.properties

import inego.evo.game.Animal

/**
 * A property which modifies stats of the animal it attaches to.
 */
interface StatModifier {

    fun onAttach(animal: Animal)

    // TODO call detach on detaching the individual property from the animal (Tail Loss)
    fun onDetach(animal: Animal)

}