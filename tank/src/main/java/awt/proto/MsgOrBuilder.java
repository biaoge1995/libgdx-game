// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: msg.proto

package awt.proto;

public interface MsgOrBuilder
    extends com.google.protobuf.MessageOrBuilder {

  // required .MsgAction header = 1;
  /**
   * <code>required .MsgAction header = 1;</code>
   *
   * <pre>
   *自身属性
   * </pre>
   */
  boolean hasHeader();
  /**
   * <code>required .MsgAction header = 1;</code>
   *
   * <pre>
   *自身属性
   * </pre>
   */
  awt.proto.enums.MsgAction getHeader();

  // repeated .Element elements = 2;
  /**
   * <code>repeated .Element elements = 2;</code>
   */
  java.util.List<awt.proto.Element> 
      getElementsList();
  /**
   * <code>repeated .Element elements = 2;</code>
   */
  awt.proto.Element getElements(int index);
  /**
   * <code>repeated .Element elements = 2;</code>
   */
  int getElementsCount();
  /**
   * <code>repeated .Element elements = 2;</code>
   */
  java.util.List<? extends awt.proto.ElementOrBuilder> 
      getElementsOrBuilderList();
  /**
   * <code>repeated .Element elements = 2;</code>
   */
  awt.proto.ElementOrBuilder getElementsOrBuilder(
      int index);

  // required int64 timeStamp = 3;
  /**
   * <code>required int64 timeStamp = 3;</code>
   */
  boolean hasTimeStamp();
  /**
   * <code>required int64 timeStamp = 3;</code>
   */
  long getTimeStamp();

  // optional .Code code = 4;
  /**
   * <code>optional .Code code = 4;</code>
   */
  boolean hasCode();
  /**
   * <code>optional .Code code = 4;</code>
   */
  awt.proto.enums.Code getCode();

  // repeated .Command commands = 5;
  /**
   * <code>repeated .Command commands = 5;</code>
   */
  java.util.List<awt.proto.command.Command> 
      getCommandsList();
  /**
   * <code>repeated .Command commands = 5;</code>
   */
  awt.proto.command.Command getCommands(int index);
  /**
   * <code>repeated .Command commands = 5;</code>
   */
  int getCommandsCount();
  /**
   * <code>repeated .Command commands = 5;</code>
   */
  java.util.List<? extends awt.proto.command.CommandOrBuilder> 
      getCommandsOrBuilderList();
  /**
   * <code>repeated .Command commands = 5;</code>
   */
  awt.proto.command.CommandOrBuilder getCommandsOrBuilder(
      int index);

  // optional .Player player = 6;
  /**
   * <code>optional .Player player = 6;</code>
   */
  boolean hasPlayer();
  /**
   * <code>optional .Player player = 6;</code>
   */
  awt.proto.Player getPlayer();
  /**
   * <code>optional .Player player = 6;</code>
   */
  awt.proto.PlayerOrBuilder getPlayerOrBuilder();
}
