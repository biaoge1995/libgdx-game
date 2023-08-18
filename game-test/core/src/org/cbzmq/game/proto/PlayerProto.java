package org.cbzmq.game.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.AccessLevel;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
@ToString
public class PlayerProto {
    int id;

    CharacterState state;
    /** 位置 **/
    VectorProto position;

    /** 速度 **/
    VectorProto velocity;

    /** 瞄准准心未知 **/
    VectorProto aim;

    float hp;


}
