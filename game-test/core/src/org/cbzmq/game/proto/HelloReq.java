package org.cbzmq.game.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * @ClassName HelloReq
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/8/17 5:58 下午
 * @Version 1.0
 **/

@ProtobufClass
@FieldDefaults(level = AccessLevel.PUBLIC)
public class HelloReq {
    String name;
}
