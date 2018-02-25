package inego.evo.properties.paired.symmetric

import inego.evo.game.*
import inego.evo.game.moves.FoodPropagationMove
import inego.evo.properties.SymmetricProperty
import inego.evo.properties.paired.FoodPropagator
import inego.evo.properties.paired.PairedPropertySide

object CommunicationProperty : SymmetricProperty("Communication", Communicator)

object Communicator : PairedPropertySide("Communicator"), FoodPropagator {
    // Food must be present in the base in order to propagate it via communication
    override fun isApplicable(gameState: GameState) = gameState.foodBase > 0

    override fun createPropagationMove(connectionMembership: ConnectionMembership) =
            CommunicationFoodPropagationMove(connectionMembership)

}

class CommunicationFoodPropagationMove(connectionMembership: ConnectionMembership)
    : FoodPropagationMove(connectionMembership) {
    override val logMessage: String
        get() = "${connectionMembership.other.fullName} gets 1 red token " +
                "from ${connectionMembership.thisAnimal} by Communication."

    override fun onPropagation(animal: Animal, gameState: GameState) {
        animal.gainRedToken(gameState)
    }

    override fun toString(player: PlayerState) =
            "${connectionMembership.other}: get 1 red token from ${connectionMembership.thisAnimal} by Communication"
}