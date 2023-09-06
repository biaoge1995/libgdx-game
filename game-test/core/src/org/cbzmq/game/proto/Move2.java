package org.cbzmq.game.proto;


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
public class Move2 implements Cloneable {
    /**
     * 操作物体的id
     **/
    int id;

    /**
     * 运动类型
     **/

    /**
     * 运动时间
     **/
    float time;

    /**
     * 位置
     */
    MyVector2 sourcePosition = new MyVector2();

    /**
     * 位置
     **/
    MyVector2 targetPosition = new MyVector2();

    /**
     * 速度
     **/
    MyVector2 velocity = new MyVector2();

    long requestTime;

    public Move2() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public MyVector2 getSourcePosition() {
        return sourcePosition;
    }

    public void setSourcePosition(MyVector2 sourcePosition) {
        this.sourcePosition.set(sourcePosition);
    }

    public MyVector2 getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(MyVector2 targetPosition) {
        this.targetPosition.set(targetPosition);
    }

    public MyVector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(MyVector2 velocity) {
        this.velocity.set(velocity);
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}


