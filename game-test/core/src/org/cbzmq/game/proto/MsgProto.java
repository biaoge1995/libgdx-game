// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: avro/msg.proto

package org.cbzmq.game.proto;

public final class MsgProto {
  private MsgProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface MsgOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    // required .MsgHeader header = 1;
    /**
     * <code>required .MsgHeader header = 1;</code>
     *
     * <pre>
     *自身属性
     * </pre>
     */
    boolean hasHeader();
    /**
     * <code>required .MsgHeader header = 1;</code>
     *
     * <pre>
     *自身属性
     * </pre>
     */
    org.cbzmq.game.enums.MsgHeader getHeader();

    // required int64 timeStamp = 2;
    /**
     * <code>required int64 timeStamp = 2;</code>
     */
    boolean hasTimeStamp();
    /**
     * <code>required int64 timeStamp = 2;</code>
     */
    long getTimeStamp();

    // optional .Code code = 3;
    /**
     * <code>optional .Code code = 3;</code>
     */
    boolean hasCode();
    /**
     * <code>optional .Code code = 3;</code>
     */
    org.cbzmq.game.enums.Code getCode();

    // repeated .Character characterData = 4;
    /**
     * <code>repeated .Character characterData = 4;</code>
     */
    java.util.List<org.cbzmq.game.proto.CharacterProto.Character> 
        getCharacterDataList();
    /**
     * <code>repeated .Character characterData = 4;</code>
     */
    org.cbzmq.game.proto.CharacterProto.Character getCharacterData(int index);
    /**
     * <code>repeated .Character characterData = 4;</code>
     */
    int getCharacterDataCount();
    /**
     * <code>repeated .Character characterData = 4;</code>
     */
    java.util.List<? extends org.cbzmq.game.proto.CharacterProto.CharacterOrBuilder> 
        getCharacterDataOrBuilderList();
    /**
     * <code>repeated .Character characterData = 4;</code>
     */
    org.cbzmq.game.proto.CharacterProto.CharacterOrBuilder getCharacterDataOrBuilder(
        int index);

    // optional .GameAction gameAction = 5;
    /**
     * <code>optional .GameAction gameAction = 5;</code>
     */
    boolean hasGameAction();
    /**
     * <code>optional .GameAction gameAction = 5;</code>
     */
    org.cbzmq.game.enums.GameAction getGameAction();
  }
  /**
   * Protobuf type {@code Msg}
   */
  public static final class Msg extends
      com.google.protobuf.GeneratedMessage
      implements MsgOrBuilder {
    // Use Msg.newBuilder() to construct.
    private Msg(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private Msg(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final Msg defaultInstance;
    public static Msg getDefaultInstance() {
      return defaultInstance;
    }

    public Msg getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private Msg(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
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
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 8: {
              int rawValue = input.readEnum();
              org.cbzmq.game.enums.MsgHeader value = org.cbzmq.game.enums.MsgHeader.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(1, rawValue);
              } else {
                bitField0_ |= 0x00000001;
                header_ = value;
              }
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              timeStamp_ = input.readInt64();
              break;
            }
            case 24: {
              int rawValue = input.readEnum();
              org.cbzmq.game.enums.Code value = org.cbzmq.game.enums.Code.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(3, rawValue);
              } else {
                bitField0_ |= 0x00000004;
                code_ = value;
              }
              break;
            }
            case 34: {
              if (!((mutable_bitField0_ & 0x00000008) == 0x00000008)) {
                characterData_ = new java.util.ArrayList<org.cbzmq.game.proto.CharacterProto.Character>();
                mutable_bitField0_ |= 0x00000008;
              }
              characterData_.add(input.readMessage(org.cbzmq.game.proto.CharacterProto.Character.PARSER, extensionRegistry));
              break;
            }
            case 40: {
              int rawValue = input.readEnum();
              org.cbzmq.game.enums.GameAction value = org.cbzmq.game.enums.GameAction.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(5, rawValue);
              } else {
                bitField0_ |= 0x00000008;
                gameAction_ = value;
              }
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000008) == 0x00000008)) {
          characterData_ = java.util.Collections.unmodifiableList(characterData_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.cbzmq.game.proto.MsgProto.internal_static_Msg_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.cbzmq.game.proto.MsgProto.internal_static_Msg_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.cbzmq.game.proto.MsgProto.Msg.class, org.cbzmq.game.proto.MsgProto.Msg.Builder.class);
    }

    public static com.google.protobuf.Parser<Msg> PARSER =
        new com.google.protobuf.AbstractParser<Msg>() {
      public Msg parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Msg(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<Msg> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    // required .MsgHeader header = 1;
    public static final int HEADER_FIELD_NUMBER = 1;
    private org.cbzmq.game.enums.MsgHeader header_;
    /**
     * <code>required .MsgHeader header = 1;</code>
     *
     * <pre>
     *自身属性
     * </pre>
     */
    public boolean hasHeader() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>required .MsgHeader header = 1;</code>
     *
     * <pre>
     *自身属性
     * </pre>
     */
    public org.cbzmq.game.enums.MsgHeader getHeader() {
      return header_;
    }

    // required int64 timeStamp = 2;
    public static final int TIMESTAMP_FIELD_NUMBER = 2;
    private long timeStamp_;
    /**
     * <code>required int64 timeStamp = 2;</code>
     */
    public boolean hasTimeStamp() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>required int64 timeStamp = 2;</code>
     */
    public long getTimeStamp() {
      return timeStamp_;
    }

    // optional .Code code = 3;
    public static final int CODE_FIELD_NUMBER = 3;
    private org.cbzmq.game.enums.Code code_;
    /**
     * <code>optional .Code code = 3;</code>
     */
    public boolean hasCode() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional .Code code = 3;</code>
     */
    public org.cbzmq.game.enums.Code getCode() {
      return code_;
    }

    // repeated .Character characterData = 4;
    public static final int CHARACTERDATA_FIELD_NUMBER = 4;
    private java.util.List<org.cbzmq.game.proto.CharacterProto.Character> characterData_;
    /**
     * <code>repeated .Character characterData = 4;</code>
     */
    public java.util.List<org.cbzmq.game.proto.CharacterProto.Character> getCharacterDataList() {
      return characterData_;
    }
    /**
     * <code>repeated .Character characterData = 4;</code>
     */
    public java.util.List<? extends org.cbzmq.game.proto.CharacterProto.CharacterOrBuilder> 
        getCharacterDataOrBuilderList() {
      return characterData_;
    }
    /**
     * <code>repeated .Character characterData = 4;</code>
     */
    public int getCharacterDataCount() {
      return characterData_.size();
    }
    /**
     * <code>repeated .Character characterData = 4;</code>
     */
    public org.cbzmq.game.proto.CharacterProto.Character getCharacterData(int index) {
      return characterData_.get(index);
    }
    /**
     * <code>repeated .Character characterData = 4;</code>
     */
    public org.cbzmq.game.proto.CharacterProto.CharacterOrBuilder getCharacterDataOrBuilder(
        int index) {
      return characterData_.get(index);
    }

    // optional .GameAction gameAction = 5;
    public static final int GAMEACTION_FIELD_NUMBER = 5;
    private org.cbzmq.game.enums.GameAction gameAction_;
    /**
     * <code>optional .GameAction gameAction = 5;</code>
     */
    public boolean hasGameAction() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    /**
     * <code>optional .GameAction gameAction = 5;</code>
     */
    public org.cbzmq.game.enums.GameAction getGameAction() {
      return gameAction_;
    }

    private void initFields() {
      header_ = org.cbzmq.game.enums.MsgHeader.SYNC_CHARACTERS_INFO;
      timeStamp_ = 0L;
      code_ = org.cbzmq.game.enums.Code.OK;
      characterData_ = java.util.Collections.emptyList();
      gameAction_ = org.cbzmq.game.enums.GameAction.JUMP;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      if (!hasHeader()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasTimeStamp()) {
        memoizedIsInitialized = 0;
        return false;
      }
      for (int i = 0; i < getCharacterDataCount(); i++) {
        if (!getCharacterData(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeEnum(1, header_.getNumber());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt64(2, timeStamp_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeEnum(3, code_.getNumber());
      }
      for (int i = 0; i < characterData_.size(); i++) {
        output.writeMessage(4, characterData_.get(i));
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeEnum(5, gameAction_.getNumber());
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(1, header_.getNumber());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(2, timeStamp_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(3, code_.getNumber());
      }
      for (int i = 0; i < characterData_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(4, characterData_.get(i));
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(5, gameAction_.getNumber());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static org.cbzmq.game.proto.MsgProto.Msg parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static org.cbzmq.game.proto.MsgProto.Msg parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static org.cbzmq.game.proto.MsgProto.Msg parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static org.cbzmq.game.proto.MsgProto.Msg parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static org.cbzmq.game.proto.MsgProto.Msg parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static org.cbzmq.game.proto.MsgProto.Msg parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static org.cbzmq.game.proto.MsgProto.Msg parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static org.cbzmq.game.proto.MsgProto.Msg parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static org.cbzmq.game.proto.MsgProto.Msg parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static org.cbzmq.game.proto.MsgProto.Msg parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(org.cbzmq.game.proto.MsgProto.Msg prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code Msg}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements org.cbzmq.game.proto.MsgProto.MsgOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return org.cbzmq.game.proto.MsgProto.internal_static_Msg_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return org.cbzmq.game.proto.MsgProto.internal_static_Msg_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                org.cbzmq.game.proto.MsgProto.Msg.class, org.cbzmq.game.proto.MsgProto.Msg.Builder.class);
      }

      // Construct using org.cbzmq.game.proto.MsgProto.Msg.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getCharacterDataFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        header_ = org.cbzmq.game.enums.MsgHeader.SYNC_CHARACTERS_INFO;
        bitField0_ = (bitField0_ & ~0x00000001);
        timeStamp_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000002);
        code_ = org.cbzmq.game.enums.Code.OK;
        bitField0_ = (bitField0_ & ~0x00000004);
        if (characterDataBuilder_ == null) {
          characterData_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000008);
        } else {
          characterDataBuilder_.clear();
        }
        gameAction_ = org.cbzmq.game.enums.GameAction.JUMP;
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return org.cbzmq.game.proto.MsgProto.internal_static_Msg_descriptor;
      }

      public org.cbzmq.game.proto.MsgProto.Msg getDefaultInstanceForType() {
        return org.cbzmq.game.proto.MsgProto.Msg.getDefaultInstance();
      }

      public org.cbzmq.game.proto.MsgProto.Msg build() {
        org.cbzmq.game.proto.MsgProto.Msg result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public org.cbzmq.game.proto.MsgProto.Msg buildPartial() {
        org.cbzmq.game.proto.MsgProto.Msg result = new org.cbzmq.game.proto.MsgProto.Msg(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.header_ = header_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.timeStamp_ = timeStamp_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.code_ = code_;
        if (characterDataBuilder_ == null) {
          if (((bitField0_ & 0x00000008) == 0x00000008)) {
            characterData_ = java.util.Collections.unmodifiableList(characterData_);
            bitField0_ = (bitField0_ & ~0x00000008);
          }
          result.characterData_ = characterData_;
        } else {
          result.characterData_ = characterDataBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000008;
        }
        result.gameAction_ = gameAction_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof org.cbzmq.game.proto.MsgProto.Msg) {
          return mergeFrom((org.cbzmq.game.proto.MsgProto.Msg)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(org.cbzmq.game.proto.MsgProto.Msg other) {
        if (other == org.cbzmq.game.proto.MsgProto.Msg.getDefaultInstance()) return this;
        if (other.hasHeader()) {
          setHeader(other.getHeader());
        }
        if (other.hasTimeStamp()) {
          setTimeStamp(other.getTimeStamp());
        }
        if (other.hasCode()) {
          setCode(other.getCode());
        }
        if (characterDataBuilder_ == null) {
          if (!other.characterData_.isEmpty()) {
            if (characterData_.isEmpty()) {
              characterData_ = other.characterData_;
              bitField0_ = (bitField0_ & ~0x00000008);
            } else {
              ensureCharacterDataIsMutable();
              characterData_.addAll(other.characterData_);
            }
            onChanged();
          }
        } else {
          if (!other.characterData_.isEmpty()) {
            if (characterDataBuilder_.isEmpty()) {
              characterDataBuilder_.dispose();
              characterDataBuilder_ = null;
              characterData_ = other.characterData_;
              bitField0_ = (bitField0_ & ~0x00000008);
              characterDataBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getCharacterDataFieldBuilder() : null;
            } else {
              characterDataBuilder_.addAllMessages(other.characterData_);
            }
          }
        }
        if (other.hasGameAction()) {
          setGameAction(other.getGameAction());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        if (!hasHeader()) {
          
          return false;
        }
        if (!hasTimeStamp()) {
          
          return false;
        }
        for (int i = 0; i < getCharacterDataCount(); i++) {
          if (!getCharacterData(i).isInitialized()) {
            
            return false;
          }
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        org.cbzmq.game.proto.MsgProto.Msg parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (org.cbzmq.game.proto.MsgProto.Msg) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      // required .MsgHeader header = 1;
      private org.cbzmq.game.enums.MsgHeader header_ = org.cbzmq.game.enums.MsgHeader.SYNC_CHARACTERS_INFO;
      /**
       * <code>required .MsgHeader header = 1;</code>
       *
       * <pre>
       *自身属性
       * </pre>
       */
      public boolean hasHeader() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>required .MsgHeader header = 1;</code>
       *
       * <pre>
       *自身属性
       * </pre>
       */
      public org.cbzmq.game.enums.MsgHeader getHeader() {
        return header_;
      }
      /**
       * <code>required .MsgHeader header = 1;</code>
       *
       * <pre>
       *自身属性
       * </pre>
       */
      public Builder setHeader(org.cbzmq.game.enums.MsgHeader value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000001;
        header_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required .MsgHeader header = 1;</code>
       *
       * <pre>
       *自身属性
       * </pre>
       */
      public Builder clearHeader() {
        bitField0_ = (bitField0_ & ~0x00000001);
        header_ = org.cbzmq.game.enums.MsgHeader.SYNC_CHARACTERS_INFO;
        onChanged();
        return this;
      }

      // required int64 timeStamp = 2;
      private long timeStamp_ ;
      /**
       * <code>required int64 timeStamp = 2;</code>
       */
      public boolean hasTimeStamp() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>required int64 timeStamp = 2;</code>
       */
      public long getTimeStamp() {
        return timeStamp_;
      }
      /**
       * <code>required int64 timeStamp = 2;</code>
       */
      public Builder setTimeStamp(long value) {
        bitField0_ |= 0x00000002;
        timeStamp_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required int64 timeStamp = 2;</code>
       */
      public Builder clearTimeStamp() {
        bitField0_ = (bitField0_ & ~0x00000002);
        timeStamp_ = 0L;
        onChanged();
        return this;
      }

      // optional .Code code = 3;
      private org.cbzmq.game.enums.Code code_ = org.cbzmq.game.enums.Code.OK;
      /**
       * <code>optional .Code code = 3;</code>
       */
      public boolean hasCode() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>optional .Code code = 3;</code>
       */
      public org.cbzmq.game.enums.Code getCode() {
        return code_;
      }
      /**
       * <code>optional .Code code = 3;</code>
       */
      public Builder setCode(org.cbzmq.game.enums.Code value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000004;
        code_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional .Code code = 3;</code>
       */
      public Builder clearCode() {
        bitField0_ = (bitField0_ & ~0x00000004);
        code_ = org.cbzmq.game.enums.Code.OK;
        onChanged();
        return this;
      }

      // repeated .Character characterData = 4;
      private java.util.List<org.cbzmq.game.proto.CharacterProto.Character> characterData_ =
        java.util.Collections.emptyList();
      private void ensureCharacterDataIsMutable() {
        if (!((bitField0_ & 0x00000008) == 0x00000008)) {
          characterData_ = new java.util.ArrayList<org.cbzmq.game.proto.CharacterProto.Character>(characterData_);
          bitField0_ |= 0x00000008;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          org.cbzmq.game.proto.CharacterProto.Character, org.cbzmq.game.proto.CharacterProto.Character.Builder, org.cbzmq.game.proto.CharacterProto.CharacterOrBuilder> characterDataBuilder_;

      /**
       * <code>repeated .Character characterData = 4;</code>
       */
      public java.util.List<org.cbzmq.game.proto.CharacterProto.Character> getCharacterDataList() {
        if (characterDataBuilder_ == null) {
          return java.util.Collections.unmodifiableList(characterData_);
        } else {
          return characterDataBuilder_.getMessageList();
        }
      }
      /**
       * <code>repeated .Character characterData = 4;</code>
       */
      public int getCharacterDataCount() {
        if (characterDataBuilder_ == null) {
          return characterData_.size();
        } else {
          return characterDataBuilder_.getCount();
        }
      }
      /**
       * <code>repeated .Character characterData = 4;</code>
       */
      public org.cbzmq.game.proto.CharacterProto.Character getCharacterData(int index) {
        if (characterDataBuilder_ == null) {
          return characterData_.get(index);
        } else {
          return characterDataBuilder_.getMessage(index);
        }
      }
      /**
       * <code>repeated .Character characterData = 4;</code>
       */
      public Builder setCharacterData(
          int index, org.cbzmq.game.proto.CharacterProto.Character value) {
        if (characterDataBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureCharacterDataIsMutable();
          characterData_.set(index, value);
          onChanged();
        } else {
          characterDataBuilder_.setMessage(index, value);
        }
        return this;
      }
      /**
       * <code>repeated .Character characterData = 4;</code>
       */
      public Builder setCharacterData(
          int index, org.cbzmq.game.proto.CharacterProto.Character.Builder builderForValue) {
        if (characterDataBuilder_ == null) {
          ensureCharacterDataIsMutable();
          characterData_.set(index, builderForValue.build());
          onChanged();
        } else {
          characterDataBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      /**
       * <code>repeated .Character characterData = 4;</code>
       */
      public Builder addCharacterData(org.cbzmq.game.proto.CharacterProto.Character value) {
        if (characterDataBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureCharacterDataIsMutable();
          characterData_.add(value);
          onChanged();
        } else {
          characterDataBuilder_.addMessage(value);
        }
        return this;
      }
      /**
       * <code>repeated .Character characterData = 4;</code>
       */
      public Builder addCharacterData(
          int index, org.cbzmq.game.proto.CharacterProto.Character value) {
        if (characterDataBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureCharacterDataIsMutable();
          characterData_.add(index, value);
          onChanged();
        } else {
          characterDataBuilder_.addMessage(index, value);
        }
        return this;
      }
      /**
       * <code>repeated .Character characterData = 4;</code>
       */
      public Builder addCharacterData(
          org.cbzmq.game.proto.CharacterProto.Character.Builder builderForValue) {
        if (characterDataBuilder_ == null) {
          ensureCharacterDataIsMutable();
          characterData_.add(builderForValue.build());
          onChanged();
        } else {
          characterDataBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      /**
       * <code>repeated .Character characterData = 4;</code>
       */
      public Builder addCharacterData(
          int index, org.cbzmq.game.proto.CharacterProto.Character.Builder builderForValue) {
        if (characterDataBuilder_ == null) {
          ensureCharacterDataIsMutable();
          characterData_.add(index, builderForValue.build());
          onChanged();
        } else {
          characterDataBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      /**
       * <code>repeated .Character characterData = 4;</code>
       */
      public Builder addAllCharacterData(
          java.lang.Iterable<? extends org.cbzmq.game.proto.CharacterProto.Character> values) {
        if (characterDataBuilder_ == null) {
          ensureCharacterDataIsMutable();
          super.addAll(values, characterData_);
          onChanged();
        } else {
          characterDataBuilder_.addAllMessages(values);
        }
        return this;
      }
      /**
       * <code>repeated .Character characterData = 4;</code>
       */
      public Builder clearCharacterData() {
        if (characterDataBuilder_ == null) {
          characterData_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000008);
          onChanged();
        } else {
          characterDataBuilder_.clear();
        }
        return this;
      }
      /**
       * <code>repeated .Character characterData = 4;</code>
       */
      public Builder removeCharacterData(int index) {
        if (characterDataBuilder_ == null) {
          ensureCharacterDataIsMutable();
          characterData_.remove(index);
          onChanged();
        } else {
          characterDataBuilder_.remove(index);
        }
        return this;
      }
      /**
       * <code>repeated .Character characterData = 4;</code>
       */
      public org.cbzmq.game.proto.CharacterProto.Character.Builder getCharacterDataBuilder(
          int index) {
        return getCharacterDataFieldBuilder().getBuilder(index);
      }
      /**
       * <code>repeated .Character characterData = 4;</code>
       */
      public org.cbzmq.game.proto.CharacterProto.CharacterOrBuilder getCharacterDataOrBuilder(
          int index) {
        if (characterDataBuilder_ == null) {
          return characterData_.get(index);  } else {
          return characterDataBuilder_.getMessageOrBuilder(index);
        }
      }
      /**
       * <code>repeated .Character characterData = 4;</code>
       */
      public java.util.List<? extends org.cbzmq.game.proto.CharacterProto.CharacterOrBuilder> 
           getCharacterDataOrBuilderList() {
        if (characterDataBuilder_ != null) {
          return characterDataBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(characterData_);
        }
      }
      /**
       * <code>repeated .Character characterData = 4;</code>
       */
      public org.cbzmq.game.proto.CharacterProto.Character.Builder addCharacterDataBuilder() {
        return getCharacterDataFieldBuilder().addBuilder(
            org.cbzmq.game.proto.CharacterProto.Character.getDefaultInstance());
      }
      /**
       * <code>repeated .Character characterData = 4;</code>
       */
      public org.cbzmq.game.proto.CharacterProto.Character.Builder addCharacterDataBuilder(
          int index) {
        return getCharacterDataFieldBuilder().addBuilder(
            index, org.cbzmq.game.proto.CharacterProto.Character.getDefaultInstance());
      }
      /**
       * <code>repeated .Character characterData = 4;</code>
       */
      public java.util.List<org.cbzmq.game.proto.CharacterProto.Character.Builder> 
           getCharacterDataBuilderList() {
        return getCharacterDataFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          org.cbzmq.game.proto.CharacterProto.Character, org.cbzmq.game.proto.CharacterProto.Character.Builder, org.cbzmq.game.proto.CharacterProto.CharacterOrBuilder> 
          getCharacterDataFieldBuilder() {
        if (characterDataBuilder_ == null) {
          characterDataBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              org.cbzmq.game.proto.CharacterProto.Character, org.cbzmq.game.proto.CharacterProto.Character.Builder, org.cbzmq.game.proto.CharacterProto.CharacterOrBuilder>(
                  characterData_,
                  ((bitField0_ & 0x00000008) == 0x00000008),
                  getParentForChildren(),
                  isClean());
          characterData_ = null;
        }
        return characterDataBuilder_;
      }

      // optional .GameAction gameAction = 5;
      private org.cbzmq.game.enums.GameAction gameAction_ = org.cbzmq.game.enums.GameAction.JUMP;
      /**
       * <code>optional .GameAction gameAction = 5;</code>
       */
      public boolean hasGameAction() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      /**
       * <code>optional .GameAction gameAction = 5;</code>
       */
      public org.cbzmq.game.enums.GameAction getGameAction() {
        return gameAction_;
      }
      /**
       * <code>optional .GameAction gameAction = 5;</code>
       */
      public Builder setGameAction(org.cbzmq.game.enums.GameAction value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000010;
        gameAction_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional .GameAction gameAction = 5;</code>
       */
      public Builder clearGameAction() {
        bitField0_ = (bitField0_ & ~0x00000010);
        gameAction_ = org.cbzmq.game.enums.GameAction.JUMP;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:Msg)
    }

    static {
      defaultInstance = new Msg(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:Msg)
  }

  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_Msg_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_Msg_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\016avro/msg.proto\032\024avro/character.proto\032\020" +
      "avro/enums.proto\"\215\001\n\003Msg\022\032\n\006header\030\001 \002(\016" +
      "2\n.MsgHeader\022\021\n\ttimeStamp\030\002 \002(\003\022\023\n\004code\030" +
      "\003 \001(\0162\005.Code\022!\n\rcharacterData\030\004 \003(\0132\n.Ch" +
      "aracter\022\037\n\ngameAction\030\005 \001(\0162\013.GameAction" +
      "B\"\n\024org.cbzmq.game.protoB\010MsgProtoP\000"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_Msg_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_Msg_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_Msg_descriptor,
              new java.lang.String[] { "Header", "TimeStamp", "Code", "CharacterData", "GameAction", });
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          org.cbzmq.game.proto.CharacterProto.getDescriptor(),
          org.cbzmq.game.enums.Enums.getDescriptor(),
        }, assigner);
  }

  // @@protoc_insertion_point(outer_class_scope)
}
