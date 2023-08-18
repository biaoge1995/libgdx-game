package org.cbzmq.game.proto;


import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
@ToString
@Setter
@Getter
public class Move {
    /** 操作物体的id **/
    int id;

    /** 运动类型 **/

    MoveType moveType;

    /** 运动时间 **/
    float time;

    /** 位置 **/
    VectorProto position;

    /** 速度 **/
    VectorProto velocity;

    public Move() {
    }

    public Move(int id, MoveType moveType, float time, VectorProto position, VectorProto velocity) {
        this.id = id;
        this.moveType = moveType;
        this.time = time;
        this.position = position;
        this.velocity = velocity;
    }

    @ProtobufClass
    @FieldDefaults(level = AccessLevel.PUBLIC)
    public enum MoveType {
        /**
         * <code>jump = 10;</code>
         *
         * <pre>
         *跳跃
         * </pre>
         */
        jump,
        /**
         * <code>jumpDamping = 16;</code>
         *
         * <pre>
         *跳跃阻尼
         * </pre>
         */
        moveRight,
        moveLeft;
    }
}


