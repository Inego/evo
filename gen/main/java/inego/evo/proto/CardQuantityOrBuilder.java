// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: messages.proto

package inego.evo.proto;

public interface CardQuantityOrBuilder extends
    // @@protoc_insertion_point(interface_extends:game.CardQuantity)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>required .game.Card card = 1;</code>
   * @return Whether the card field is set.
   */
  boolean hasCard();
  /**
   * <code>required .game.Card card = 1;</code>
   * @return The card.
   */
  inego.evo.proto.Card getCard();

  /**
   * <code>required int32 quantity = 2;</code>
   * @return Whether the quantity field is set.
   */
  boolean hasQuantity();
  /**
   * <code>required int32 quantity = 2;</code>
   * @return The quantity.
   */
  int getQuantity();
}
