// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: messages.proto

package inego.evo.proto;

public interface GameOrBuilder extends
    // @@protoc_insertion_point(interface_extends:game.Game)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>repeated .game.Player players = 1;</code>
   */
  java.util.List<inego.evo.proto.Player> 
      getPlayersList();
  /**
   * <code>repeated .game.Player players = 1;</code>
   */
  inego.evo.proto.Player getPlayers(int index);
  /**
   * <code>repeated .game.Player players = 1;</code>
   */
  int getPlayersCount();
  /**
   * <code>repeated .game.Player players = 1;</code>
   */
  java.util.List<? extends inego.evo.proto.PlayerOrBuilder> 
      getPlayersOrBuilderList();
  /**
   * <code>repeated .game.Player players = 1;</code>
   */
  inego.evo.proto.PlayerOrBuilder getPlayersOrBuilder(
      int index);

  /**
   * <code>repeated .game.CardQuantity seen_cards = 2;</code>
   */
  java.util.List<inego.evo.proto.CardQuantity> 
      getSeenCardsList();
  /**
   * <code>repeated .game.CardQuantity seen_cards = 2;</code>
   */
  inego.evo.proto.CardQuantity getSeenCards(int index);
  /**
   * <code>repeated .game.CardQuantity seen_cards = 2;</code>
   */
  int getSeenCardsCount();
  /**
   * <code>repeated .game.CardQuantity seen_cards = 2;</code>
   */
  java.util.List<? extends inego.evo.proto.CardQuantityOrBuilder> 
      getSeenCardsOrBuilderList();
  /**
   * <code>repeated .game.CardQuantity seen_cards = 2;</code>
   */
  inego.evo.proto.CardQuantityOrBuilder getSeenCardsOrBuilder(
      int index);

  /**
   * <code>optional int32 inactive_players = 3;</code>
   * @return Whether the inactivePlayers field is set.
   */
  boolean hasInactivePlayers();
  /**
   * <code>optional int32 inactive_players = 3;</code>
   * @return The inactivePlayers.
   */
  int getInactivePlayers();

  /**
   * <code>required int32 turn_number = 4;</code>
   * @return Whether the turnNumber field is set.
   */
  boolean hasTurnNumber();
  /**
   * <code>required int32 turn_number = 4;</code>
   * @return The turnNumber.
   */
  int getTurnNumber();

  /**
   * <code>required int32 first_player_idx = 5;</code>
   * @return Whether the firstPlayerIdx field is set.
   */
  boolean hasFirstPlayerIdx();
  /**
   * <code>required int32 first_player_idx = 5;</code>
   * @return The firstPlayerIdx.
   */
  int getFirstPlayerIdx();

  /**
   * <code>required int32 current_player_idx = 6;</code>
   * @return Whether the currentPlayerIdx field is set.
   */
  boolean hasCurrentPlayerIdx();
  /**
   * <code>required int32 current_player_idx = 6;</code>
   * @return The currentPlayerIdx.
   */
  int getCurrentPlayerIdx();

  /**
   * <code>required bool compute_next_player = 7;</code>
   * @return Whether the computeNextPlayer field is set.
   */
  boolean hasComputeNextPlayer();
  /**
   * <code>required bool compute_next_player = 7;</code>
   * @return The computeNextPlayer.
   */
  boolean getComputeNextPlayer();

  /**
   * <code>required .game.GamePhase phase = 8;</code>
   * @return Whether the phase field is set.
   */
  boolean hasPhase();
  /**
   * <code>required .game.GamePhase phase = 8;</code>
   * @return The phase.
   */
  inego.evo.proto.GamePhase getPhase();

  /**
   * <code>optional .game.AnimalReference attacking_animal = 9;</code>
   * @return Whether the attackingAnimal field is set.
   */
  boolean hasAttackingAnimal();
  /**
   * <code>optional .game.AnimalReference attacking_animal = 9;</code>
   * @return The attackingAnimal.
   */
  inego.evo.proto.AnimalReference getAttackingAnimal();
  /**
   * <code>optional .game.AnimalReference attacking_animal = 9;</code>
   */
  inego.evo.proto.AnimalReferenceOrBuilder getAttackingAnimalOrBuilder();

  /**
   * <code>optional .game.AnimalReference defending_animal = 10;</code>
   * @return Whether the defendingAnimal field is set.
   */
  boolean hasDefendingAnimal();
  /**
   * <code>optional .game.AnimalReference defending_animal = 10;</code>
   * @return The defendingAnimal.
   */
  inego.evo.proto.AnimalReference getDefendingAnimal();
  /**
   * <code>optional .game.AnimalReference defending_animal = 10;</code>
   */
  inego.evo.proto.AnimalReferenceOrBuilder getDefendingAnimalOrBuilder();

  /**
   * <code>optional int32 food_base = 11;</code>
   * @return Whether the foodBase field is set.
   */
  boolean hasFoodBase();
  /**
   * <code>optional int32 food_base = 11;</code>
   * @return The foodBase.
   */
  int getFoodBase();
}