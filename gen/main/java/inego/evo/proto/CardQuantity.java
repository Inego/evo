// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: messages.proto

package inego.evo.proto;

/**
 * Protobuf type {@code game.CardQuantity}
 */
public final class CardQuantity extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:game.CardQuantity)
    CardQuantityOrBuilder {
private static final long serialVersionUID = 0L;
  // Use CardQuantity.newBuilder() to construct.
  private CardQuantity(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private CardQuantity() {
    card_ = 1;
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new CardQuantity();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private CardQuantity(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 8: {
            int rawValue = input.readEnum();
              @SuppressWarnings("deprecation")
            inego.evo.proto.Card value = inego.evo.proto.Card.valueOf(rawValue);
            if (value == null) {
              unknownFields.mergeVarintField(1, rawValue);
            } else {
              bitField0_ |= 0x00000001;
              card_ = rawValue;
            }
            break;
          }
          case 16: {
            bitField0_ |= 0x00000002;
            quantity_ = input.readInt32();
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return inego.evo.proto.GameProtos.internal_static_game_CardQuantity_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return inego.evo.proto.GameProtos.internal_static_game_CardQuantity_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            inego.evo.proto.CardQuantity.class, inego.evo.proto.CardQuantity.Builder.class);
  }

  private int bitField0_;
  public static final int CARD_FIELD_NUMBER = 1;
  private int card_;
  /**
   * <code>required .game.Card card = 1;</code>
   * @return Whether the card field is set.
   */
  @java.lang.Override public boolean hasCard() {
    return ((bitField0_ & 0x00000001) != 0);
  }
  /**
   * <code>required .game.Card card = 1;</code>
   * @return The card.
   */
  @java.lang.Override public inego.evo.proto.Card getCard() {
    @SuppressWarnings("deprecation")
    inego.evo.proto.Card result = inego.evo.proto.Card.valueOf(card_);
    return result == null ? inego.evo.proto.Card.CAMOUFLAGE : result;
  }

  public static final int QUANTITY_FIELD_NUMBER = 2;
  private int quantity_;
  /**
   * <code>required int32 quantity = 2;</code>
   * @return Whether the quantity field is set.
   */
  @java.lang.Override
  public boolean hasQuantity() {
    return ((bitField0_ & 0x00000002) != 0);
  }
  /**
   * <code>required int32 quantity = 2;</code>
   * @return The quantity.
   */
  @java.lang.Override
  public int getQuantity() {
    return quantity_;
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    if (!hasCard()) {
      memoizedIsInitialized = 0;
      return false;
    }
    if (!hasQuantity()) {
      memoizedIsInitialized = 0;
      return false;
    }
    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (((bitField0_ & 0x00000001) != 0)) {
      output.writeEnum(1, card_);
    }
    if (((bitField0_ & 0x00000002) != 0)) {
      output.writeInt32(2, quantity_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (((bitField0_ & 0x00000001) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeEnumSize(1, card_);
    }
    if (((bitField0_ & 0x00000002) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(2, quantity_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof inego.evo.proto.CardQuantity)) {
      return super.equals(obj);
    }
    inego.evo.proto.CardQuantity other = (inego.evo.proto.CardQuantity) obj;

    if (hasCard() != other.hasCard()) return false;
    if (hasCard()) {
      if (card_ != other.card_) return false;
    }
    if (hasQuantity() != other.hasQuantity()) return false;
    if (hasQuantity()) {
      if (getQuantity()
          != other.getQuantity()) return false;
    }
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (hasCard()) {
      hash = (37 * hash) + CARD_FIELD_NUMBER;
      hash = (53 * hash) + card_;
    }
    if (hasQuantity()) {
      hash = (37 * hash) + QUANTITY_FIELD_NUMBER;
      hash = (53 * hash) + getQuantity();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static inego.evo.proto.CardQuantity parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static inego.evo.proto.CardQuantity parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static inego.evo.proto.CardQuantity parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static inego.evo.proto.CardQuantity parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static inego.evo.proto.CardQuantity parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static inego.evo.proto.CardQuantity parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static inego.evo.proto.CardQuantity parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static inego.evo.proto.CardQuantity parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static inego.evo.proto.CardQuantity parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static inego.evo.proto.CardQuantity parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static inego.evo.proto.CardQuantity parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static inego.evo.proto.CardQuantity parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(inego.evo.proto.CardQuantity prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code game.CardQuantity}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:game.CardQuantity)
      inego.evo.proto.CardQuantityOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return inego.evo.proto.GameProtos.internal_static_game_CardQuantity_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return inego.evo.proto.GameProtos.internal_static_game_CardQuantity_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              inego.evo.proto.CardQuantity.class, inego.evo.proto.CardQuantity.Builder.class);
    }

    // Construct using inego.evo.proto.CardQuantity.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      card_ = 1;
      bitField0_ = (bitField0_ & ~0x00000001);
      quantity_ = 0;
      bitField0_ = (bitField0_ & ~0x00000002);
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return inego.evo.proto.GameProtos.internal_static_game_CardQuantity_descriptor;
    }

    @java.lang.Override
    public inego.evo.proto.CardQuantity getDefaultInstanceForType() {
      return inego.evo.proto.CardQuantity.getDefaultInstance();
    }

    @java.lang.Override
    public inego.evo.proto.CardQuantity build() {
      inego.evo.proto.CardQuantity result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public inego.evo.proto.CardQuantity buildPartial() {
      inego.evo.proto.CardQuantity result = new inego.evo.proto.CardQuantity(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        to_bitField0_ |= 0x00000001;
      }
      result.card_ = card_;
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.quantity_ = quantity_;
        to_bitField0_ |= 0x00000002;
      }
      result.bitField0_ = to_bitField0_;
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof inego.evo.proto.CardQuantity) {
        return mergeFrom((inego.evo.proto.CardQuantity)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(inego.evo.proto.CardQuantity other) {
      if (other == inego.evo.proto.CardQuantity.getDefaultInstance()) return this;
      if (other.hasCard()) {
        setCard(other.getCard());
      }
      if (other.hasQuantity()) {
        setQuantity(other.getQuantity());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      if (!hasCard()) {
        return false;
      }
      if (!hasQuantity()) {
        return false;
      }
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      inego.evo.proto.CardQuantity parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (inego.evo.proto.CardQuantity) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private int card_ = 1;
    /**
     * <code>required .game.Card card = 1;</code>
     * @return Whether the card field is set.
     */
    @java.lang.Override public boolean hasCard() {
      return ((bitField0_ & 0x00000001) != 0);
    }
    /**
     * <code>required .game.Card card = 1;</code>
     * @return The card.
     */
    @java.lang.Override
    public inego.evo.proto.Card getCard() {
      @SuppressWarnings("deprecation")
      inego.evo.proto.Card result = inego.evo.proto.Card.valueOf(card_);
      return result == null ? inego.evo.proto.Card.CAMOUFLAGE : result;
    }
    /**
     * <code>required .game.Card card = 1;</code>
     * @param value The card to set.
     * @return This builder for chaining.
     */
    public Builder setCard(inego.evo.proto.Card value) {
      if (value == null) {
        throw new NullPointerException();
      }
      bitField0_ |= 0x00000001;
      card_ = value.getNumber();
      onChanged();
      return this;
    }
    /**
     * <code>required .game.Card card = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearCard() {
      bitField0_ = (bitField0_ & ~0x00000001);
      card_ = 1;
      onChanged();
      return this;
    }

    private int quantity_ ;
    /**
     * <code>required int32 quantity = 2;</code>
     * @return Whether the quantity field is set.
     */
    @java.lang.Override
    public boolean hasQuantity() {
      return ((bitField0_ & 0x00000002) != 0);
    }
    /**
     * <code>required int32 quantity = 2;</code>
     * @return The quantity.
     */
    @java.lang.Override
    public int getQuantity() {
      return quantity_;
    }
    /**
     * <code>required int32 quantity = 2;</code>
     * @param value The quantity to set.
     * @return This builder for chaining.
     */
    public Builder setQuantity(int value) {
      bitField0_ |= 0x00000002;
      quantity_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>required int32 quantity = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearQuantity() {
      bitField0_ = (bitField0_ & ~0x00000002);
      quantity_ = 0;
      onChanged();
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:game.CardQuantity)
  }

  // @@protoc_insertion_point(class_scope:game.CardQuantity)
  private static final inego.evo.proto.CardQuantity DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new inego.evo.proto.CardQuantity();
  }

  public static inego.evo.proto.CardQuantity getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  @java.lang.Deprecated public static final com.google.protobuf.Parser<CardQuantity>
      PARSER = new com.google.protobuf.AbstractParser<CardQuantity>() {
    @java.lang.Override
    public CardQuantity parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new CardQuantity(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<CardQuantity> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<CardQuantity> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public inego.evo.proto.CardQuantity getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

