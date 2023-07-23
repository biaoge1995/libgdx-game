// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: enums.proto

package awt.proto.enums;

/**
 * Protobuf enum {@code GameRole}
 *
 * <pre>
 *游戏角色
 * </pre>
 */
public enum GameRole
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>PLAYER = 0;</code>
   *
   * <pre>
   *玩家控制
   * </pre>
   */
  PLAYER(0, 0),
  /**
   * <code>NPC = 1;</code>
   *
   * <pre>
   *系统玩家
   * </pre>
   */
  NPC(1, 1),
  /**
   * <code>SCENE = 3;</code>
   *
   * <pre>
   *系统场景
   * </pre>
   */
  SCENE(2, 3),
  /**
   * <code>DERIVATIVE = 4;</code>
   *
   * <pre>
   *衍生物
   * </pre>
   */
  DERIVATIVE(3, 4),
  ;

  /**
   * <code>PLAYER = 0;</code>
   *
   * <pre>
   *玩家控制
   * </pre>
   */
  public static final int PLAYER_VALUE = 0;
  /**
   * <code>NPC = 1;</code>
   *
   * <pre>
   *系统玩家
   * </pre>
   */
  public static final int NPC_VALUE = 1;
  /**
   * <code>SCENE = 3;</code>
   *
   * <pre>
   *系统场景
   * </pre>
   */
  public static final int SCENE_VALUE = 3;
  /**
   * <code>DERIVATIVE = 4;</code>
   *
   * <pre>
   *衍生物
   * </pre>
   */
  public static final int DERIVATIVE_VALUE = 4;


  public final int getNumber() { return value; }

  public static GameRole valueOf(int value) {
    switch (value) {
      case 0: return PLAYER;
      case 1: return NPC;
      case 3: return SCENE;
      case 4: return DERIVATIVE;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<GameRole>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static com.google.protobuf.Internal.EnumLiteMap<GameRole>
      internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<GameRole>() {
          public GameRole findValueByNumber(int number) {
            return GameRole.valueOf(number);
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
    return awt.proto.enums.Enums.getDescriptor().getEnumTypes().get(3);
  }

  private static final GameRole[] VALUES = values();

  public static GameRole valueOf(
      com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    return VALUES[desc.getIndex()];
  }

  private final int index;
  private final int value;

  private GameRole(int index, int value) {
    this.index = index;
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:GameRole)
}

