// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: avro/enums.proto

package org.cbzmq.game.enums;

/**
 * Protobuf enum {@code TwoBodyEventType}
 *
 * <pre>
 *两个物体交互事件
 * </pre>
 */
public enum TwoBodyEventType
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>collisionCharacter = 1;</code>
   *
   * <pre>
   *碰撞到其他角色
   * </pre>
   */
  collisionCharacter(0, 1),
  /**
   * <code>hit = 2;</code>
   *
   * <pre>
   *被击打
   * </pre>
   */
  hit(1, 2),
  /**
   * <code>beKilled = 3;</code>
   */
  beKilled(2, 3),
  ;

  /**
   * <code>collisionCharacter = 1;</code>
   *
   * <pre>
   *碰撞到其他角色
   * </pre>
   */
  public static final int collisionCharacter_VALUE = 1;
  /**
   * <code>hit = 2;</code>
   *
   * <pre>
   *被击打
   * </pre>
   */
  public static final int hit_VALUE = 2;
  /**
   * <code>beKilled = 3;</code>
   */
  public static final int beKilled_VALUE = 3;


  public final int getNumber() { return value; }

  public static TwoBodyEventType valueOf(int value) {
    switch (value) {
      case 1: return collisionCharacter;
      case 2: return hit;
      case 3: return beKilled;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<TwoBodyEventType>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static com.google.protobuf.Internal.EnumLiteMap<TwoBodyEventType>
      internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<TwoBodyEventType>() {
          public TwoBodyEventType findValueByNumber(int number) {
            return TwoBodyEventType.valueOf(number);
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
    return org.cbzmq.game.enums.Enums.getDescriptor().getEnumTypes().get(7);
  }

  private static final TwoBodyEventType[] VALUES = values();

  public static TwoBodyEventType valueOf(
      com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    return VALUES[desc.getIndex()];
  }

  private final int index;
  private final int value;

  private TwoBodyEventType(int index, int value) {
    this.index = index;
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:TwoBodyEventType)
}
