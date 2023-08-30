package org.cbzmq.game.proto;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

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
