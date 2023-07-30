// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: avro/enums.proto

package org.cbzmq.game.enums;

/**
 * Protobuf enum {@code CharacterType}
 */
public enum CharacterType
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>player = 0;</code>
   */
  player(0, 0),
  /**
   * <code>enemy = 1;</code>
   */
  enemy(1, 1),
  /**
   * <code>bullet = 2;</code>
   */
  bullet(2, 2),
  /**
   * <code>unknown = 3;</code>
   */
  unknown(3, 3),
  ;

  /**
   * <code>player = 0;</code>
   */
  public static final int player_VALUE = 0;
  /**
   * <code>enemy = 1;</code>
   */
  public static final int enemy_VALUE = 1;
  /**
   * <code>bullet = 2;</code>
   */
  public static final int bullet_VALUE = 2;
  /**
   * <code>unknown = 3;</code>
   */
  public static final int unknown_VALUE = 3;


  public final int getNumber() { return value; }

  public static CharacterType valueOf(int value) {
    switch (value) {
      case 0: return player;
      case 1: return enemy;
      case 2: return bullet;
      case 3: return unknown;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<CharacterType>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static com.google.protobuf.Internal.EnumLiteMap<CharacterType>
      internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<CharacterType>() {
          public CharacterType findValueByNumber(int number) {
            return CharacterType.valueOf(number);
          }
        };

  public final com.google.protobuf.Descriptors.EnumValueDescriptor
      getValueDescriptor() {
    return getDescriptor().getValues().get(index);
  }
  public final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptorForType() {
    return getDescriptor();
  }
  public static final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptor() {
    return org.cbzmq.game.enums.Enums.getDescriptor().getEnumTypes().get(1);
  }

  private static final CharacterType[] VALUES = values();

  public static CharacterType valueOf(
      com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    return VALUES[desc.getIndex()];
  }

  private final int index;
  private final int value;

  private CharacterType(int index, int value) {
    this.index = index;
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:CharacterType)
}

