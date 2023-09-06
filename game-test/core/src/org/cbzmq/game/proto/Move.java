package org.cbzmq.game.proto;


import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.cbzmq.game.model.MyVector2;

@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
@ToString
@Setter
@Getter
public class Move implements Cloneable {
    /**
     * 操作物体的id
     **/
    int id;

    /**
     * 运动类型
     **/

    MoveType moveType;

    /**
     * 运动时间
     **/
    float time;

    /**
     * 位置
     **/
    MyVector2 position;

    /**
     * 速度
     **/
    MyVector2 velocity;


    MyVector2 deltaDict;


    float deltaTime;

    long requestTime;

    public Move() {
    }

    public Move(int id, MoveType moveType, float time, MyVector2 position, MyVector2 velocity) {
        this.id = id;
        this.moveType = moveType;
        this.time = time;
        this.position = position;
        this.velocity = velocity;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}


