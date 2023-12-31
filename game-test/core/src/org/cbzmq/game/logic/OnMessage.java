package org.cbzmq.game.logic;

import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.external.core.config.ExternalGlobalConfig;
import com.iohao.game.external.core.message.ExternalMessage;
import com.iohao.game.external.core.message.ExternalMessageCmdCode;
import org.cbzmq.game.net.GameWebSocketClient;

/**
 * @ClassName CharacterOnMessage
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/8/18 5:48 下午
 * @Version 1.0
 **/

public interface OnMessage {

    int getCmdMerge();

    Object response(ExternalMessage externalMessage, byte[] data);

    default void request(Object data) {
        this.request(data, null);
    }

    default void request(Object data, Runnable runnable) {
        ExternalMessage externalMessage = this.createExternalMessage();

        byte[] bytes = DataCodecKit.encode(data);
        // 业务数据
        externalMessage.setData(bytes);

        GameWebSocketClient.me().request(externalMessage);

        if (runnable != null) {
            GameWebSocketClient.me().getActionMap().put(this.getCmdMerge(), runnable);
        }
    }


    default ExternalMessage createExternalMessage() {

        ExternalMessage request = new ExternalMessage();
        request.setCmdCode(ExternalMessageCmdCode.biz);
        // 协议开关，用于一些协议级别的开关控制，比如 安全加密校验等。 : 0 不校验
        request.setProtocolSwitch(ExternalGlobalConfig.protocolSwitch);
        request.setCmdMerge(this.getCmdMerge());
        return request;
    }


}