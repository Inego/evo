package inego.evo.properties.paired.symmetric

import inego.evo.game.*
import inego.evo.game.moves.FoodPropagationMove
import inego.evo.properties.SymmetricProperty
import inego.evo.properties.paired.FoodPropagator
import inego.evo.properties.paired.PairedPropertySide


object CooperationProperty : SymmetricProperty("Cooperation", Cooperator)


object Cooperator : PairedPropertySide("Cooperator"), FoodPropagator {
    override fun createPropagationMove(connectionMembership: ConnectionMembership) =
            CooperationFoodPropagationMove(connectionMembership)
}


class CooperationFoodPropagationMove(connectionMembership: ConnectionMembership) : FoodPropagationMove(connectionMembership) {
    override fun onPropagation(animal: Animal, gameState: GameState) {
        animal.gainBlueTokens(1)
    }

    override fun toString(gameState: GameState, player: PlayerState): String =
            "${connectionMembership.other} gets 1 blue token (Cooperation)"
}