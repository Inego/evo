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


class CooperationFoodPropagationMove(connectionMembership: ConnectionMembership) :
        FoodPropagationMove(connectionMembership) {

    override fun clone(c: GameCopier) = CooperationFoodPropagationMove(c[connectionMembership])

    override fun logMessage(player: Player) = "${connectionMembership.other.fullName} gets 1 blue token " +
                "from ${connectionMembership.thisAnimal} with Cooperation"

    override fun onPropagation(animal: Animal, game: Game) {
        animal.gainBlueTokens(1)
    }

    override fun toString(player: Player): String =
            "${connectionMembership.other}: get 1 blue token from ${connectionMembership.thisAnimal} with Cooperation"
}