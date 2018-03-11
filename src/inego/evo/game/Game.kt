package inego.evo.game

import inego.evo.cards.CardQuantities
import inego.evo.cards.ECard
import inego.evo.each
import inego.evo.game.moves.*
import inego.evo.properties.individual.*
import inego.evo.removeLast
import java.util.*
import kotlin.math.min

class Game private constructor(
        val numberOfPlayers: Int,
        val logging: Boolean,
        val deck: MutableList<ECard>,
        val seenCards: CardQuantities,
        val players: List<Player>,
        private val random: Random
) {

    var turnNumber = 1

    val logMessages: MutableList<String> = mutableListOf()

    private val moveSelections: Deque<MoveSelection<*>> = LinkedList()

    var firstPlayerIdx = 0

    var currentPlayerIdx = 0

    private var computeNextPlayer = false

    inline val currentPlayer
        get() = players[currentPlayerIdx]

    var phase: GamePhase = GamePhase.DEVELOPMENT
        set(value) {
            if (field == GamePhase.DEFENSE) {
                // Clear defending animal's runaway flag to be able to escape from the next predator
                defendingAnimal!!.usedRunningAway = false
                defendingAnimal = null
                attackingAnimal = null
            }
            field = value
        }

    var attackingAnimal: Animal? = null
    var defendingAnimal: Animal? = null

    var foodBase = 0


    constructor(c: GameCopier, src: Game, random: Random) : this(
            src.numberOfPlayers,
            false,
            c.takeUnseenCards(src.deck.size),
            src.seenCards.clone(),
            c.copiedPlayers,
            random
    ) {
        turnNumber = src.turnNumber

        if (moveSelections.isNotEmpty())
            throw AssertionError("Non-empty move selection on copying")

        firstPlayerIdx = src.firstPlayerIdx
        currentPlayerIdx = src.currentPlayerIdx
        computeNextPlayer = src.computeNextPlayer
        phase = src.phase
        foodBase = src.foodBase

        val srcAttackingAnimal = src.attackingAnimal

        if (srcAttackingAnimal != null) {
            attackingAnimal = c[srcAttackingAnimal]
            defendingAnimal = c[src.defendingAnimal!!]
        }
    }

    /**
     * Applies the specified move and plays the game either until a non-trivial move selection
     * has to be made by any player, or until the end.
     *
     * @return The next move selection or `null` if the game has been played to the end.
     */
    fun next(player: Player, move: Move): MoveSelection<*>? {

        move.applyTo(this, player)
        // After a move was applied, other move selections may arise
        var selection: MoveSelection<*>? = getNextNonTrivialMoveSelection()
        if (selection != null) {
            return selection
        }

        do {
            when (phase) {

                GamePhase.DEVELOPMENT -> this.performDevelopment()
                GamePhase.FEEDING_BASE_DETERMINATION -> this.performFeedingBaseDetermination()
                GamePhase.FEEDING -> this.performFeeding()
                GamePhase.DEFENSE -> this.performDefense()
                GamePhase.FOOD_PROPAGATION -> this.performFoodPropagation()
                GamePhase.GRAZING -> this.performGrazing()
                GamePhase.EXTINCTION -> this.processExtinction()
                GamePhase.END -> {
                    // May even not get here actually.
                }
            }

            selection = getNextNonTrivialMoveSelection()
            if (selection != null) {
                return selection
            }

        } while (phase != GamePhase.END)

        log {
            val computedWinner = winner
            if (computedWinner == null) "A draw! No one won the game."
            else "The winner is: $computedWinner"
        }

        return null
    }

    fun dice() = random.nextInt(6) + 1

    private fun performGrazing() {
        val player = currentPlayer
        val grazingAnimals = player.animals.count { it.has(GrazingProperty) }

        val maxToGraze = min(grazingAnimals, foodBase)

        if (maxToGraze > 0) {
            val grazeFoodMoves = (0..maxToGraze).map { GrazeFood(it) }
            moveSelections.add(GrazeFoodSelection(player, grazeFoodMoves))
        }

        // Go to next player
        incCurrentPlayer()

        phase = GamePhase.FEEDING
    }

    private fun incCurrentPlayer() {
        currentPlayerIdx++
        if (currentPlayerIdx == players.size)
            currentPlayerIdx = 0
    }

    private fun performFoodPropagation() {

        for (player in fromPlayer(currentPlayerIdx)) {
            do {
                val foodPropagationMoves: List<FoodPropagationMove> = player.getFoodPropagationMoves(this)
                if (foodPropagationMoves.isEmpty()) {
                    break
                } else {
                    if (foodPropagationMoves.size == 1) {
                        foodPropagationMoves[0].applyTo(this, player)
                    } else {
                        moveSelections.add(FoodPropagationMoveSelection(player, foodPropagationMoves))
                        return
                    }
                }
            } while (true)
        }

        // TODO think about optimization since this may be called quite often
        for (player in players) {
            for (connection in player.connections) {
                connection.isUsed = false
            }
        }

        phase = GamePhase.GRAZING
    }

    /**
     * Automatically handles all trivial (i.e. containing a single move) move selections and returns
     * either the next non-trivial one, or `null` if there are no move selections left
     */
    private fun getNextNonTrivialMoveSelection(): MoveSelection<*>? {
        do {
            val moveSelection: MoveSelection<*>? = moveSelections.poll()
            if (moveSelection == null) {
                break
            } else {
                if (moveSelections.isNotEmpty()) {
                    throw AssertionError()
                }

                if (moveSelection.size == 1) {
                    moveSelection[0].applyTo(this, moveSelection.decidingPlayer)
                } else {
                    return moveSelection
                }
            }
        } while (true)
        return null
    }

    private fun performDefense() {

        val defenseMoves = defendingAnimal!!.gatherDefenseMoves(attackingAnimal!!, this)

        when {
            defenseMoves.isEmpty() -> {
                eat(attackingAnimal!!, defendingAnimal!!)
                phase = GamePhase.FOOD_PROPAGATION
            }
            defenseMoves.size == 1 -> defenseMoves[0].applyTo(this, defendingAnimal!!.owner)
            else -> {
                // Several moves - let the defending animal's owner select
                moveSelections.add(DefenseMoveSelection(defendingAnimal!!.owner, defenseMoves))
            }
        }
    }

    private fun performDevelopment() {

        if (computeNextPlayer) {
            val nextDevelopingPlayer = this.getNextDevelopingPlayer()

            if (nextDevelopingPlayer == null) {
                phase = GamePhase.FEEDING_BASE_DETERMINATION
                return
            }
            currentPlayerIdx = nextDevelopingPlayer

        } else {
            computeNextPlayer = true
        }

        val player = currentPlayer

        val moves: MutableList<DevelopmentMove> = mutableListOf()

        // Gather development moves (pass is always an option)
        moves.add(DevelopmentPass)

        for (card in player.hand.toSet()) {
            moves.add(CreateAnimal(card))
            card.firstProperty.getDevelopmentMoves(this, card).toCollection(moves)
            if (card.secondProperty != null)
                card.secondProperty.getDevelopmentMoves(this, card).toCollection(moves)
        }

        moveSelections.add(DevelopmentMoveSelection(player, moves))
    }

    private fun getNextDevelopingPlayer(): Int? {
        var cur = currentPlayerIdx

        while (true) {
            cur++
            if (cur == players.size) {
                cur = 0
            }
            if (cur == currentPlayerIdx) {
                break
            }
            val p = players[cur]
            if (!p.eligibleForDevelopment) {
                continue
            }
            return cur
        }
        return if (currentPlayer.eligibleForDevelopment) currentPlayerIdx else null
    }

    private fun performFeedingBaseDetermination() {
        foodBase = when (players.size) {
            2 -> dice() + 2
            3 -> {
                val d1 = dice()
                val d2 = dice()
                d1 + d2
            }
            4 -> dice() + dice() + 2
            else -> throw IllegalStateException()
        }

        log { "Food base for this move: $foodBase." }

        phase = GamePhase.FEEDING
        currentPlayerIdx = firstPlayerIdx

        // Since `Player.passed` flag is reused during Feeding, clear it
        players.forEach { it.passed = false }
    }

    private fun performFeeding() {

        var entryPlayerIdx = -1

        while (true) {

            if (entryPlayerIdx == -1) {
                entryPlayerIdx = currentPlayerIdx
            } else {
                if (entryPlayerIdx == currentPlayerIdx)
                    break
            }

            // Collect feeding moves

            val player = currentPlayer

            if (player.passed) continue

            var moves = player.animals.flatMap { it.gatherFeedingMoves(this) }

            if (!moves.isEmpty()) {

                if (player.animals.all { it.isFed } && foodBase == 0) {
                    // All available moves are connected with killing other animals for fat.
                    // At this stage, the player may pass out from feeding.

                    val extendedMoves = moves.toMutableList()
                    extendedMoves.add(FeedingPassMove)

                    moves = extendedMoves
                }

                moveSelections.add(FeedingMoveSelection(player, moves))
                return
            }

            incCurrentPlayer()
        }


        phase = GamePhase.EXTINCTION
    }

    private fun processExtinction() {
        for (player in players) {
            player.passed = false

            // Remove dead animals
            val dyingAnimals = player.animals.filter { !it.isFed || it.isPoisoned }
            // First log, then remove
            if (logging) {
                dyingAnimals.mapTo(logMessages) { "${it.fullName} dies." }
            }
            for (dyingAnimal in dyingAnimals) {
                player.removeAnimal(dyingAnimal)
            }

            // Clean the state of player's animals
            player.animals.each {
                hasFood = 0

                if (hibernatedLastTurn) {
                    hibernatedLastTurn = false
                } else {
                    if (isHibernating) {
                        isHibernating = false
                        hibernatedLastTurn = true
                    }
                }

                usedPiracy = false
                usedMimicry = false
                usedAttack = false
            }
        }

        if (isLastTurn) {
            log { "Game over." }
            phase = GamePhase.END
        } else {
            // Hand out cards one by one and start a new turn

            var toHandOut = fromFirstPlayer().asSequence().map { Pair(it, it.cardsToHandOut) }

            loop@ while (true) {
                val newToHandOut: MutableList<Pair<Player, Int>> = mutableListOf()
                for (pair in toHandOut) {
                    val card = deck.removeLast()
                    pair.first.hand.add(card)
                    if (deck.isEmpty()) {
                        break@loop
                    }
                    if (pair.second > 1) {
                        newToHandOut.add(pair.copy(second = pair.second - 1))
                    }
                }
                if (newToHandOut.isEmpty()) break
                toHandOut = newToHandOut.asSequence()
            }

            if (isLastTurn) {
                log { "The deck is empty. This is the last turn!" }
            }

            turnNumber++

            firstPlayerIdx++

            if (firstPlayerIdx >= players.size) {
                firstPlayerIdx = 0
            }

            currentPlayerIdx = firstPlayerIdx

            logTurnStart()

            computeNextPlayer = false

            phase = GamePhase.DEVELOPMENT
        }
    }

    fun logTurnStart() {
        log { "===== TURN $turnNumber =====   First player: $currentPlayer" }
    }

    private fun fromPlayer(playerIdx: Int) = object : Iterator<Player> {
        var idx = 0

        override fun next(): Player {
            var collIdx = playerIdx + idx
            idx++
            if (collIdx >= players.size)
                collIdx -= players.size
            return players[collIdx]
        }

        override fun hasNext() = idx < players.size
    }

    private fun fromPlayer(player: Player) = fromPlayer(players.indexOf(player))

    fun fromFirstPlayer() = fromPlayer(firstPlayerIdx)

    fun canAttack(attacker: Animal, victim: Animal): Boolean {

        return victim != attacker
                && attacker.individualProperties.all { it.individualProperty.mayAttack(victim) }
                && victim.individualProperties.all { it.individualProperty.mayBeAttackedBy(victim, attacker) }
                && victim.connections.all { it.sideProperty.mayBeAttackedBy(attacker, it.other) }
    }

    private fun eat(predator: Animal, victim: Animal) {
        log { "${predator.fullName} kills and eats ${victim.fullName}." }
        if (victim.has(PoisonousProperty)) {
            log { "${predator.fullName} is POISONED!" }
            predator.isPoisoned = true
        }
        victim.owner.removeAnimal(victim)
        predator.gainBlueTokens(2)

        // Feed the scavengers
        for (scavengerCandidate in fromPlayer(predator.owner)) {
            val scavengers = scavengerCandidate.animals
                    .filter { it.has(ScavengerProperty) && !it.isFull }

            if (!scavengers.isEmpty()) {
                val feedScavengerMoves = scavengers.map(::FeedTheScavengerMove)
                moveSelections.add(ScavengerSelection(scavengerCandidate, feedScavengerMoves))
                break
            }
        }
    }

    inline fun log(messageProducer: () -> String) {
        if (logging) {
            logMessages.add(messageProducer())
        }
    }

    fun takeFromLog(): List<String> {
        val result = logMessages.toList()
        logMessages.clear()
        return result
    }

    operator fun get(playerIdx: Int) = players[playerIdx]

    companion object {

        private val DEFAULT_PLAYER_NAMES = listOf("A", "B", "C", "D", "E")

        fun new(
                numberOfPlayers: Int,
                logging: Boolean = false,
                random: Random
        ): Game = Game(
                numberOfPlayers,
                logging,
                ECard.readOnlyInitialQuantities.toListOfCards(random),
                CardQuantities.new(),
                List(numberOfPlayers) { index -> Player.new(DEFAULT_PLAYER_NAMES[index]) },
                random
        )
    }

    val isLastTurn: Boolean
        get() = deck.isEmpty()

    val winner: Player?
        get() {
            if (phase != GamePhase.END)
                throw IllegalStateException()

            val sorted = players.map { it.result }.sortedDescending()
            val winner = sorted[0]
            val runnerUp = sorted[1]

            return if (winner > runnerUp) winner.player else null
        }
}

enum class GamePhase {
    DEVELOPMENT,
    FEEDING_BASE_DETERMINATION,
    FEEDING,
    DEFENSE,
    FOOD_PROPAGATION,
    GRAZING,
    EXTINCTION,
    END
}
