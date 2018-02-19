package inego.evo.properties.paired

import inego.evo.game.Animal


abstract class PairedPropertySide(val name: String) {
    /**
     * Checks whether the animal possessing this paired property side may be attacked by an animal.
     *
     * @param attacker The animal whose attack is checked
     * @param other The other animal of the paired property
     */
    open fun mayBeAttackedBy(attacker: Animal, other: Animal) = true


    /**
     * Checks whether the animal possessing this paired property side may eat (receive red or blue food tokens)
     */
    // TODO call this from all appropriate places
    open fun mayEat(other: Animal) = true
}