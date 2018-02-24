package inego.evo.game

import inego.evo.cards.*
import inego.evo.each
import inego.evo.game.moves.*
import inego.evo.properties.individual.*
import inego.evo.removeLast
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.min

class GameState private constructor(val numberOfPlayers: Int) {

    var deck: MutableList<Card> = mutableListOf()

    val players: List<PlayerState> = List(numberOfPlayers) { index -> PlayerState("p${index + 1}") }

    private val moveSelections: Deque<MoveSelection<*>> = LinkedList()

    var firstPlayerIdx = 0

    var currentPlayerIdx = 0

    private var computeNextPlayer = false

    inline val currentPlayer
        get() = players[currentPlayerIdx]

    var phase: GamePhase = GamePhase.DEVELOPMENT

    lateinit var attackingAnimal: Animal
    lateinit var defendingAnimal: Animal

    var foodBase = 0


    constructor(src: GameState) : this(src.numberOfPlayers) {
        // TODO copy constructor
    }

    fun next(move: Move): MoveSelection<*>? {

        applyMove(move)

        // After a move was applied, other move selections may appear
        handleTrivialMoveSelections()
        val selection = moveSelections.peek()
        if (selection != null) {
            return selection
        }

        do {

            when (phase) {

                GamePhase.DEVELOPMENT -> this.performDevelopment()
                GamePhase.FEEDING_BASE_DETERMINATION -> this.performFeedingBaseDetermination()
                GamePhase.FEEDING -> this.performFeeding()
                GamePhase.DEFENSE -> this.performDefense()
                GamePhase.EXTINCTION -> this.processExtinction()
                GamePhase.END -> {
                    // Do nothing
                }
            }

            handleTrivialMoveSelections()

        } while (moveSelections.isEmpty() && phase != GamePhase.END)

        return moveSelections.peek()
    }

    private fun handleTrivialMoveSelections() {
        do {
            val nextMoves = moveSelections.peek()?.moves
            if (nextMoves?.size == 1) {
                applyMove(nextMoves[0])
                moveSelections.remove()
            }
        } while (true)
    }

    private fun performDefense() {
        val defenseMoves = defendingAnimal.gatherDefenseMoves(attackingAnimal, this)

        when {
            defenseMoves.isEmpty() -> {
                eat(attackingAnimal, defendingAnimal)
                phase = GamePhase.FEEDING
            }
            defenseMoves.size == 1 -> defenseMoves[0].applyTo(this)
            else -> {
                // Several moves - let the defending animal's owner select
                moveSelections.add(DefenseMoveSelection(defendingAnimal.owner, defenseMoves))
            }
        }
    }

    private fun applyMove(move: Move) {
        move.applyTo(this)
        moveSelections.remove()
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
            when (card) {
                is SingleCard -> {
                    moves += card.property.getDevelopmentMoves(this, card)
                }
                is DoubleCard -> {
                    moves += card.firstProperty.getDevelopmentMoves(this, card)
                    moves += card.secondProperty.getDevelopmentMoves(this, card)
                }
            }
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
            if (p.passed || p.hand.isEmpty()) {
                continue
            }
            return cur
        }
        return null
    }

    private fun performFeedingBaseDetermination() {

        foodBase = when (players.size) {
            2 -> dice() + 2
            3 -> dice() + dice()
            4 -> dice() + dice() + 2
            else -> throw IllegalStateException()
        }

        phase = GamePhase.FEEDING
        currentPlayerIdx = firstPlayerIdx

    }

    private fun performFeeding() {

        var entryPlayerIdx = -1

        while (true) {

            if (entryPlayerIdx == -1) {
                entryPlayerIdx = currentPlayerIdx
            }
            else {
                if (entryPlayerIdx == currentPlayerIdx)
                    break
            }

            // Collect feeding moves

            val player = currentPlayer

            val moves = player.animals.flatMap { it.gatherFeedingMoves(this) }

            if (!moves.isEmpty()) {
                moveSelections.add(FeedingMoveSelection(player, moves))
                return
            }

            afterPlayerFeeding()

            if (!moveSelections.isEmpty()) {
                return
            }
        }

        phase = GamePhase.EXTINCTION
    }

    fun afterPlayerFeeding() {

        // Check grazing animals
        val player = currentPlayer
        val grazingAnimals = player.animals.count { it.has(GrazingProperty) }

        val maxToGraze = min(grazingAnimals, foodBase)

        if (maxToGraze > 0) {
            val grazeFoodMoves = (0..maxToGraze).map(::GrazeFood)
            moveSelections.add(GrazeFoodSelection(player, grazeFoodMoves))
        }

        // Go to next player

        currentPlayerIdx++

        if (currentPlayerIdx == players.size)
            currentPlayerIdx = 0
    }

    private fun processExtinction() {
        for (player in players) {
            player.passed = false

            // Remove dead animals
            val dyingAnimals = player.animals.filter { !it.isFed || it.isPoisoned }
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

                hasPirated = false

            }

            for (connection in player.connections) {
                connection.isUsed = false
            }
        }

        if (isLastTurn) {
            phase = GamePhase.END
        } else {
            // Hand out cards one by one and start a new turn

            var toHandOut = fromFirstPlayer().asSequence().map { Pair(it, it.cardsToHandOut) }

            loop@ while (true) {
                val newToHandOut: MutableList<Pair<PlayerState, Int>> = mutableListOf()
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

            firstPlayerIdx++

            if (firstPlayerIdx >= players.size) {
                firstPlayerIdx = 0
            }

            currentPlayerIdx = firstPlayerIdx

            phase = GamePhase.DEVELOPMENT
        }
    }


    fun addCard(card: Card, number: Int = 4) {
        repeat(number) {
            this.deck.add(card)
        }
    }

    fun addCards(vararg cards: Card) {
        cards.forEach { addCard(it) }
    }

    private fun fromPlayer(playerIdx: Int) = object : Iterator<PlayerState> {
        var idx = 0

        override fun next(): PlayerState {
            var collIdx = playerIdx + idx
            idx++
            if (collIdx >= players.size)
                collIdx -= players.size
            return players[collIdx]
        }

        override fun hasNext() = idx < players.size
    }

    private fun fromPlayer(player: PlayerState) = fromPlayer(players.indexOf(player))

    fun fromFirstPlayer() = fromPlayer(firstPlayerIdx)

    fun canAttack(attacker: Animal, victim: Animal): Boolean {

        return attacker.individualProperties.all { it.mayAttack(victim) }
            && victim.individualProperties.all { it.mayBeAttackedBy(victim, attacker) }
            && victim.connections.all { it.sideProperty.mayBeAttackedBy(attacker, it.other) }
    }

    private fun eat(predator: Animal, victim: Animal) {
        predator.gainBlueTokens(2)
        if (victim.has(PoisonousProperty)) {
            predator.isPoisoned = true
        }
        victim.owner.removeAnimal(victim)

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

    companion object {

        fun new(numberOfPlayers: Int): GameState {

            return GameState(numberOfPlayers).apply {
                addCards(
                        CamouflageCard,
                        BurrowingCard,
                        SharpVisionCard,
                        SymbiosisCard,
                        PiracyCard,
                        GrazingCard,
                        TailLossCard,
                        HibernationCard,
                        PoisonousCard,
                        CommunicationCard,
                        ScavengerCard,
                        RunningCard,
                        MimicryCard,
                        ParasiteCarnivorousCard,
                        ParasiteFatTissueCard,
                        CooperationCarnivorousCard,
                        CooperationFatTissueCard,
                        BigCarnivorousCard,
                        BigFatTissueCard
                )
                addCard(SwimmingCard, 8)
                deck.shuffle()
            }
        }
    }

    val isLastTurn: Boolean
        get() = deck.isEmpty()
}

enum class GamePhase {
    DEVELOPMENT,
    FEEDING_BASE_DETERMINATION,
    FEEDING,
    DEFENSE,
    EXTINCTION,
    END
}

fun dice() = ThreadLocalRandom.current().nextInt(6) + 1
