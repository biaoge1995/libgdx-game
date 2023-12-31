// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: element.proto

package awt.proto;

public interface ElementOrBuilder
    extends com.google.protobuf.MessageOrBuilder {

  // optional int32 playerId = 1;
  /**
   * <code>optional int32 playerId = 1;</code>
   *
   * <pre>
   *游戏玩家id
   * </pre>
   */
  boolean hasPlayerId();
  /**
   * <code>optional int32 playerId = 1;</code>
   *
   * <pre>
   *游戏玩家id
   * </pre>
   */
  int getPlayerId();

  // optional .ElementType elementType = 2;
  /**
   * <code>optional .ElementType elementType = 2;</code>
   */
  boolean hasElementType();
  /**
   * <code>optional .ElementType elementType = 2;</code>
   */
  awt.proto.enums.ElementType getElementType();

  // optional int32 xCoordinate = 3;
  /**
   * <code>optional int32 xCoordinate = 3;</code>
   *
   * <pre>
   * 物体中心位置在窗口的X坐标
   * </pre>
   */
  boolean hasXCoordinate();
  /**
   * <code>optional int32 xCoordinate = 3;</code>
   *
   * <pre>
   * 物体中心位置在窗口的X坐标
   * </pre>
   */
  int getXCoordinate();

  // optional int32 yCoordinate = 4;
  /**
   * <code>optional int32 yCoordinate = 4;</code>
   *
   * <pre>
   * 物体中心位置在窗口的Y坐标
   * </pre>
   */
  boolean hasYCoordinate();
  /**
   * <code>optional int32 yCoordinate = 4;</code>
   *
   * <pre>
   * 物体中心位置在窗口的Y坐标
   * </pre>
   */
  int getYCoordinate();

  // optional int32 mapXCoordinate = 5;
  /**
   * <code>optional int32 mapXCoordinate = 5;</code>
   *
   * <pre>
   * 物体中心位置地图中的X坐标
   * </pre>
   */
  boolean hasMapXCoordinate();
  /**
   * <code>optional int32 mapXCoordinate = 5;</code>
   *
   * <pre>
   * 物体中心位置地图中的X坐标
   * </pre>
   */
  int getMapXCoordinate();

  // optional int32 mapYCoordinate = 6;
  /**
   * <code>optional int32 mapYCoordinate = 6;</code>
   *
   * <pre>
   * 物体中心位置地图中的的Y坐标
   * </pre>
   */
  boolean hasMapYCoordinate();
  /**
   * <code>optional int32 mapYCoordinate = 6;</code>
   *
   * <pre>
   * 物体中心位置地图中的的Y坐标
   * </pre>
   */
  int getMapYCoordinate();

  // optional .Direct direct = 7;
  /**
   * <code>optional .Direct direct = 7;</code>
   */
  boolean hasDirect();
  /**
   * <code>optional .Direct direct = 7;</code>
   */
  awt.proto.enums.Direct getDirect();

  // optional bool isMoved = 8;
  /**
   * <code>optional bool isMoved = 8;</code>
   *
   * <pre>
   *是否处于移动状态
   * </pre>
   */
  boolean hasIsMoved();
  /**
   * <code>optional bool isMoved = 8;</code>
   *
   * <pre>
   *是否处于移动状态
   * </pre>
   */
  boolean getIsMoved();

  // required bool isLive = 9;
  /**
   * <code>required bool isLive = 9;</code>
   *
   * <pre>
   *是否存活
   * </pre>
   */
  boolean hasIsLive();
  /**
   * <code>required bool isLive = 9;</code>
   *
   * <pre>
   *是否存活
   * </pre>
   */
  boolean getIsLive();

  // optional int32 blood = 10;
  /**
   * <code>optional int32 blood = 10;</code>
   *
   * <pre>
   *当前血条
   * </pre>
   */
  boolean hasBlood();
  /**
   * <code>optional int32 blood = 10;</code>
   *
   * <pre>
   *当前血条
   * </pre>
   */
  int getBlood();

  // required int32 increaseId = 11;
  /**
   * <code>required int32 increaseId = 11;</code>
   *
   * <pre>
   *自增id
   * </pre>
   */
  boolean hasIncreaseId();
  /**
   * <code>required int32 increaseId = 11;</code>
   *
   * <pre>
   *自增id
   * </pre>
   */
  int getIncreaseId();

  // optional int32 distance = 12;
  /**
   * <code>optional int32 distance = 12;</code>
   *
   * <pre>
   *已走射程计数器
   * </pre>
   */
  boolean hasDistance();
  /**
   * <code>optional int32 distance = 12;</code>
   *
   * <pre>
   *已走射程计数器
   * </pre>
   */
  int getDistance();

  // optional .Direct canMoveDirect = 13;
  /**
   * <code>optional .Direct canMoveDirect = 13;</code>
   *
   * <pre>
   *多个方向
   * </pre>
   */
  boolean hasCanMoveDirect();
  /**
   * <code>optional .Direct canMoveDirect = 13;</code>
   *
   * <pre>
   *多个方向
   * </pre>
   */
  awt.proto.enums.Direct getCanMoveDirect();
}
