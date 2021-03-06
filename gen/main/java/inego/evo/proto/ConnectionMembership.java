// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: messages.proto

package inego.evo.proto;

/**
 * Protobuf type {@code game.ConnectionMembership}
 */
public final class ConnectionMembership extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:game.ConnectionMembership)
    ConnectionMembershipOrBuilder {
private static final long serialVersionUID = 0L;
  // Use ConnectionMembership.newBuilder() to construct.
  private ConnectionMembership(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private ConnectionMembership() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new ConnectionMembership();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private ConnectionMembership(
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
            bitField0_ |= 0x00000001;
            connection_ = input.readInt32();
            break;
          }
          case 16: {
            bitField0_ |= 0x00000002;
            host_ = input.readBool();
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
    return inego.evo.proto.GameProtos.internal_static_game_ConnectionMembership_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return inego.evo.proto.GameProtos.internal_static_game_ConnectionMembership_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            inego.evo.proto.ConnectionMembership.class, inego.evo.proto.ConnectionMembership.Builder.class);
  }

  private int bitField0_;
  public static final int CONNECTION_FIELD_NUMBER = 1;
  private int connection_;
  /**
   * <code>required int32 connection = 1;</code>
   * @return Whether the connection field is set.
   */
  @java.lang.Override
  public boolean hasConnection() {
    return ((bitField0_ & 0x00000001) != 0);
  }
  /**
   * <code>required int32 connection = 1;</code>
   * @return The connection.
   */
  @java.lang.Override
  public int getConnection() {
    return connection_;
  }

  public static final int HOST_FIELD_NUMBER = 2;
  private boolean host_;
  /**
   * <code>required bool host = 2;</code>
   * @return Whether the host field is set.
   */
  @java.lang.Override
  public boolean hasHost() {
    return ((bitField0_ & 0x00000002) != 0);
  }
  /**
   * <code>required bool host = 2;</code>
   * @return The host.
   */
  @java.lang.Override
  public boolean getHost() {
    return host_;
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    if (!hasConnection()) {
      memoizedIsInitialized = 0;
      return false;
    }
    if (!hasHost()) {
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
      output.writeInt32(1, connection_);
    }
    if (((bitField0_ & 0x00000002) != 0)) {
      output.writeBool(2, host_);
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
        .computeInt32Size(1, connection_);
    }
    if (((bitField0_ & 0x00000002) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(2, host_);
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
    if (!(obj instanceof inego.evo.proto.ConnectionMembership)) {
      return super.equals(obj);
    }
    inego.evo.proto.ConnectionMembership other = (inego.evo.proto.ConnectionMembership) obj;

    if (hasConnection() != other.hasConnection()) return false;
    if (hasConnection()) {
      if (getConnection()
          != other.getConnection()) return false;
    }
    if (hasHost() != other.hasHost()) return false;
    if (hasHost()) {
      if (getHost()
          != other.getHost()) return false;
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
    if (hasConnection()) {
      hash = (37 * hash) + CONNECTION_FIELD_NUMBER;
      hash = (53 * hash) + getConnection();
    }
    if (hasHost()) {
      hash = (37 * hash) + HOST_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
          getHost());
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static inego.evo.proto.ConnectionMembership parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static inego.evo.proto.ConnectionMembership parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static inego.evo.proto.ConnectionMembership parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static inego.evo.proto.ConnectionMembership parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static inego.evo.proto.ConnectionMembership parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static inego.evo.proto.ConnectionMembership parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static inego.evo.proto.ConnectionMembership parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static inego.evo.proto.ConnectionMembership parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static inego.evo.proto.ConnectionMembership parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static inego.evo.proto.ConnectionMembership parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static inego.evo.proto.ConnectionMembership parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static inego.evo.proto.ConnectionMembership parseFrom(
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
  public static Builder newBuilder(inego.evo.proto.ConnectionMembership prototype) {
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
   * Protobuf type {@code game.ConnectionMembership}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:game.ConnectionMembership)
      inego.evo.proto.ConnectionMembershipOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return inego.evo.proto.GameProtos.internal_static_game_ConnectionMembership_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return inego.evo.proto.GameProtos.internal_static_game_ConnectionMembership_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              inego.evo.proto.ConnectionMembership.class, inego.evo.proto.ConnectionMembership.Builder.class);
    }

    // Construct using inego.evo.proto.ConnectionMembership.newBuilder()
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
      connection_ = 0;
      bitField0_ = (bitField0_ & ~0x00000001);
      host_ = false;
      bitField0_ = (bitField0_ & ~0x00000002);
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return inego.evo.proto.GameProtos.internal_static_game_ConnectionMembership_descriptor;
    }

    @java.lang.Override
    public inego.evo.proto.ConnectionMembership getDefaultInstanceForType() {
      return inego.evo.proto.ConnectionMembership.getDefaultInstance();
    }

    @java.lang.Override
    public inego.evo.proto.ConnectionMembership build() {
      inego.evo.proto.ConnectionMembership result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public inego.evo.proto.ConnectionMembership buildPartial() {
      inego.evo.proto.ConnectionMembership result = new inego.evo.proto.ConnectionMembership(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.connection_ = connection_;
        to_bitField0_ |= 0x00000001;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.host_ = host_;
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
      if (other instanceof inego.evo.proto.ConnectionMembership) {
        return mergeFrom((inego.evo.proto.ConnectionMembership)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(inego.evo.proto.ConnectionMembership other) {
      if (other == inego.evo.proto.ConnectionMembership.getDefaultInstance()) return this;
      if (other.hasConnection()) {
        setConnection(other.getConnection());
      }
      if (other.hasHost()) {
        setHost(other.getHost());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      if (!hasConnection()) {
        return false;
      }
      if (!hasHost()) {
        return false;
      }
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      inego.evo.proto.ConnectionMembership parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (inego.evo.proto.ConnectionMembership) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private int connection_ ;
    /**
     * <code>required int32 connection = 1;</code>
     * @return Whether the connection field is set.
     */
    @java.lang.Override
    public boolean hasConnection() {
      return ((bitField0_ & 0x00000001) != 0);
    }
    /**
     * <code>required int32 connection = 1;</code>
     * @return The connection.
     */
    @java.lang.Override
    public int getConnection() {
      return connection_;
    }
    /**
     * <code>required int32 connection = 1;</code>
     * @param value The connection to set.
     * @return This builder for chaining.
     */
    public Builder setConnection(int value) {
      bitField0_ |= 0x00000001;
      connection_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>required int32 connection = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearConnection() {
      bitField0_ = (bitField0_ & ~0x00000001);
      connection_ = 0;
      onChanged();
      return this;
    }

    private boolean host_ ;
    /**
     * <code>required bool host = 2;</code>
     * @return Whether the host field is set.
     */
    @java.lang.Override
    public boolean hasHost() {
      return ((bitField0_ & 0x00000002) != 0);
    }
    /**
     * <code>required bool host = 2;</code>
     * @return The host.
     */
    @java.lang.Override
    public boolean getHost() {
      return host_;
    }
    /**
     * <code>required bool host = 2;</code>
     * @param value The host to set.
     * @return This builder for chaining.
     */
    public Builder setHost(boolean value) {
      bitField0_ |= 0x00000002;
      host_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>required bool host = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearHost() {
      bitField0_ = (bitField0_ & ~0x00000002);
      host_ = false;
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


    // @@protoc_insertion_point(builder_scope:game.ConnectionMembership)
  }

  // @@protoc_insertion_point(class_scope:game.ConnectionMembership)
  private static final inego.evo.proto.ConnectionMembership DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new inego.evo.proto.ConnectionMembership();
  }

  public static inego.evo.proto.ConnectionMembership getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  @java.lang.Deprecated public static final com.google.protobuf.Parser<ConnectionMembership>
      PARSER = new com.google.protobuf.AbstractParser<ConnectionMembership>() {
    @java.lang.Override
    public ConnectionMembership parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new ConnectionMembership(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<ConnectionMembership> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ConnectionMembership> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public inego.evo.proto.ConnectionMembership getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

