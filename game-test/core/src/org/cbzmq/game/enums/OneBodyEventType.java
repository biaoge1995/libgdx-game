// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: avro/enums.proto

package org.cbzmq.game.enums;

/**
 * Protobuf enum {@code OneBodyEventType}
 *
 * <pre>
 *单个物体事件（一个瞬间发生的事情）
 * </pre>
 */
public enum OneBodyEventType
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>born = 1;</code>
   *
   * <pre>
   *这些事件需要通知客户端
   * </pre>
   */
  born(0, 1),
  /**
   * <code>beDeath = 2;</code>
   *
   * <pre>
   *死亡
   * </pre>
   */
  beDeath(1, 2),
  /**
   * <code>attack = 3;</code>
   *
   * <pre>
   *攻击
   * </pre>
   */
  attack(2, 3),
  /**
   * <code>jump = 10;</code>
   *
   * <pre>
   *跳跃
   * </pre>
   */
  jump(3, 10),
  /**
   * <code>moveRight = 11;</code>
   *
   * <pre>
   *向右边
   * </pre>
   */
  moveRight(4, 11),
  /**
   * <code>moveLeft = 12;</code>
   *
   * <pre>
   *向左边
   * </pre>
   */
  moveLeft(5, 12),
  /**
   * <code>bloodUpdate = 13;</code>
   *
   * <pre>
   *血条更新
   * </pre>
   */
  bloodUpdate(6, 13),
  /**
   * <code>dispose = 4;</code>
   *
   * <pre>
   *销毁
   * </pre>
   */
  dispose(7, 4),
  /**
   * <code>beRemove = 5;</code>
   *
   * <pre>
   *移除
   * </pre>
   */
  beRemove(8, 5),
  /**
   * <code>lose = 6;</code>
   *
   * <pre>
   *失败
   * </pre>
   */
  lose(9, 6),
  /**
   * <code>win = 7;</code>
   *
   * <pre>
   *胜利
   * </pre>
   */
  win(10, 7),
  /**
   * <code>collisionMap = 8;</code>
   *
   * <pre>
   *碰撞到地图
   * </pre>
   */
  collisionMap(11, 8),
  /**
   * <code>frameEnd = 9;</code>
   *
   * <pre>
   *帧结束
   * </pre>
   */
  frameEnd(12, 9),
  ;

  /**
   * <code>born = 1;</code>
   *
   * <pre>
   *这些事件需要通知客户端
   * </pre>
   */
  public static final int born_VALUE = 1;
  /**
   * <code>beDeath = 2;</code>
   *
   * <pre>
   *死亡
   * </pre>
   */
  public static final int beDeath_VALUE = 2;
  /**
   * <code>attack = 3;</code>
   *
   * <pre>
   *攻击
   * </pre>
   */
  public static final int attack_VALUE = 3;
  /**
   * <code>jump = 10;</code>
   *
   * <pre>
   *跳跃
   * </pre>
   */
  public static final int jump_VALUE = 10;
  /**
   * <code>moveRight = 11;</code>
   *
   * <pre>
   *向右边
   * </pre>
   */
  public static final int moveRight_VALUE = 11;
  /**
   * <code>moveLeft = 12;</code>
   *
   * <pre>
   *向左边
   * </pre>
   */
  public static final int moveLeft_VALUE = 12;
  /**
   * <code>bloodUpdate = 13;</code>
   *
   * <pre>
   *血条更新
   * </pre>
   */
  public static final int bloodUpdate_VALUE = 13;
  /**
   * <code>dispose = 4;</code>
   *
   * <pre>
   *销毁
   * </pre>
   */
  public static final int dispose_VALUE = 4;
  /**
   * <code>beRemove = 5;</code>
   *
   * <pre>
   *移除
   * </pre>
   */
  public static final int beRemove_VALUE = 5;
  /**
   * <code>lose = 6;</code>
   *
   * <pre>
   *失败
   * </pre>
   */
  public static final int lose_VALUE = 6;
  /**
   * <code>win = 7;</code>
   *
   * <pre>
   *胜利
   * </pre>
   */
  public static final int win_VALUE = 7;
  /**
   * <code>collisionMap = 8;</code>
   *
   * <pre>
   *碰撞到地图
   * </pre>
   */
  public static final int collisionMap_VALUE = 8;
  /**
   * <code>frameEnd = 9;</code>
   *
   * <pre>
   *帧结束
   * </pre>
   */
  public static final int frameEnd_VALUE = 9;


  public final int getNumber() { return value; }

  public static OneBodyEventType valueOf(int value) {
    switch (value) {
      case 1: return born;
      case 2: return beDeath;
      case 3: return attack;
      case 10: return jump;
      case 11: return moveRight;
      case 12: return moveLeft;
      case 13: return bloodUpdate;
      case 4: return dispose;
      case 5: return beRemove;
      case 6: return lose;
      case 7: return win;
      case 8: return collisionMap;
      case 9: return frameEnd;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<OneBodyEventType>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static com.google.protobuf.Internal.EnumLiteMap<OneBodyEventType>
      internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<OneBodyEventType>() {
          public OneBodyEventType findValueByNumber(int number) {
            return OneBodyEventType.valueOf(number);
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
    return org.cbzmq.game.enums.Enums.getDescriptor().getEnumTypes().get(6);
  }

  private static final OneBodyEventType[] VALUES = values();

  public static OneBodyEventType valueOf(
      com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    return VALUES[desc.getIndex()];
  }

  private final int index;
  private final int value;

  private OneBodyEventType(int index, int value) {
    this.index = index;
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:OneBodyEventType)
}
