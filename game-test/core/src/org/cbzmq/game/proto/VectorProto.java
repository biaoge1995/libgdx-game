package org.cbzmq.game.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.AccessLevel;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
@ToString
public class VectorProto {
    float x;
    float y;

    public VectorProto() {
    }

    public VectorProto(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
