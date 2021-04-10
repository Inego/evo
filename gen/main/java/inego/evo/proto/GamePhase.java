// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: messages.proto

package inego.evo.proto;

/**
 * Protobuf enum {@code game.GamePhase}
 */
public enum GamePhase
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>PH_DEVELOPMENT = 1;</code>
   */
  PH_DEVELOPMENT(1),
  /**
   * <code>PH_FEEDING_BASE_DETERMINATION = 2;</code>
   */
  PH_FEEDING_BASE_DETERMINATION(2),
  /**
   * <code>PH_FEEDING = 3;</code>
   */
  PH_FEEDING(3),
  /**
   * <code>PH_DEFENSE = 4;</code>
   */
  PH_DEFENSE(4),
  /**
   * <code>PH_FOOD_PROPAGATION = 5;</code>
   */
  PH_FOOD_PROPAGATION(5),
  /**
   * <code>PH_GRAZING = 6;</code>
   */
  PH_GRAZING(6),
  /**
   * <code>PH_EXTINCTION = 7;</code>
   */
  PH_EXTINCTION(7),
  /**
   * <code>PH_END = 8;</code>
   */
  PH_END(8),
  ;

  /**
   * <code>PH_DEVELOPMENT = 1;</code>
   */
  public static final int PH_DEVELOPMENT_VALUE = 1;
  /**
   * <code>PH_FEEDING_BASE_DETERMINATION = 2;</code>
   */
  public static final int PH_FEEDING_BASE_DETERMINATION_VALUE = 2;
  /**
   * <code>PH_FEEDING = 3;</code>
   */
  public static final int PH_FEEDING_VALUE = 3;
  /**
   * <code>PH_DEFENSE = 4;</code>
   */
  public static final int PH_DEFENSE_VALUE = 4;
  /**
   * <code>PH_FOOD_PROPAGATION = 5;</code>
   */
  public static final int PH_FOOD_PROPAGATION_VALUE = 5;
  /**
   * <code>PH_GRAZING = 6;</code>
   */
  public static final int PH_GRAZING_VALUE = 6;
  /**
   * <code>PH_EXTINCTION = 7;</code>
   */
  public static final int PH_EXTINCTION_VALUE = 7;
  /**
   * <code>PH_END = 8;</code>
   */
  public static final int PH_END_VALUE = 8;


  public final int getNumber() {
    return value;
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   * @deprecated Use {@link #forNumber(int)} instead.
   */
  @java.lang.Deprecated
  public static GamePhase valueOf(int value) {
    return forNumber(value);
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   */
  public static GamePhase forNumber(int value) {
    switch (value) {
      case 1: return PH_DEVELOPMENT;
      case 2: return PH_FEEDING_BASE_DETERMINATION;
      case 3: return PH_FEEDING;
      case 4: return PH_DEFENSE;
      case 5: return PH_FOOD_PROPAGATION;
      case 6: return PH_GRAZING;
      case 7: return PH_EXTINCTION;
      case 8: return PH_END;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<GamePhase>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      GamePhase> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<GamePhase>() {
          public GamePhase findValueByNumber(int number) {
            return GamePhase.forNumber(number);
          }
        };

  public final com.google.protobuf.Descriptors.EnumValueDescriptor
      getValueDescriptor() {
    return getDescriptor().getValues().get(ordinal());
  }
  public final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptorForType() {
    return getDescriptor();
  }
  public static final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptor() {
    return inego.evo.proto.GameProtos.getDescriptor().getEnumTypes().get(3);
  }

  private static final GamePhase[] VALUES = values();

  public static GamePhase valueOf(
      com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    return VALUES[desc.getIndex()];
  }

  private final int value;

  private GamePhase(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:game.GamePhase)
}
