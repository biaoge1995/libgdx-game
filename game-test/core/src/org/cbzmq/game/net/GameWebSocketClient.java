package org.cbzmq.game.net;

import com.iohao.game.action.skeleton.core.CmdKit;
import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.common.kit.StrKit;
import com.iohao.game.external.core.message.ExternalMessage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.cbzmq.game.logic.CharacterOnMsg;
import org.cbzmq.game.logic.OnMessage;
import org.cbzmq.game.proto.UserLogin;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
@Getter
@Slf4j
public class GameWebSocketClient {

    final Map<Integer, OnMessage> onMessageMap = new ConcurrentHashMap<>();

    final Map<Integer, Runnable> actionMap = new ConcurrentHashMap<>();

    private static WebSocketClient webSocketClient;


    private void initOnMessage() {
        put(CharacterOnMsg.LoginVerifyOnMessage.me());
        put(CharacterOnMsg.MoveOnMessage.me());
        put(CharacterOnMsg.broadCastOnMessage.me());

    }

    private void put(OnMessage onMessage){
        onMessageMap.put(onMessage.getCmdMerge(),onMessage);
    }


    public void request(ExternalMessage externalMessage) {
        byte[] bytes = DataCodecKit.encode(externalMessage);

        webSocketClient.send(bytes);
    }

    public void start() throws URISyntaxException {
        String url = "ws://{}:{}" + GameConfig.websocketPath;
        String wsUrl = StrKit.format(url, GameConfig.externalIp, GameConfig.externalPort);
        initOnMessage();
        webSocketClient = new WebSocketClient(new URI(wsUrl),new Draft_6455()) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                UserLogin userLogin = new UserLogin();
                userLogin.name = "biao ge";
                CharacterOnMsg.LoginVerifyOnMessage.me().request(userLogin);
            }

            @Override
            public void onMessage(String message) {

            }

            @Override
            public void onMessage(ByteBuffer bytes) {
                byte[] dataContent = bytes.array();

                ExternalMessage message = DataCodecKit.decode(dataContent, ExternalMessage.class);
                int cmdMerge = message.getCmdMerge();
                int cmd = CmdKit.getCmd(cmdMerge);
                int subCmd = CmdKit.getSubCmd(cmdMerge);

                if (message.getResponseStatus() != 0) {
                    log.error("错误：cmd[{}-{}] - [{}] [{}]",
                            cmd,
                            subCmd,
                            message.getResponseStatus(),
                            message.getValidMsg());
                    return;
                }
                OnMessage onMessage = onMessageMap.get(cmdMerge);
                if(Objects.nonNull(onMessage)){
                    Object response = onMessage.response(message, message.getData());

                    String onMessageName = onMessage.getClass().getSimpleName();
//                    System.out.println(response);
                }else {
                    log.debug("不存在处理类 onMessage: ");
                }



            }

            @Override
            public void onClose(int code, String reason, boolean remote) {

            }

            @Override
            public void onError(Exception ex) {

            }
        };

        webSocketClient.connect();

    }

    private GameWebSocketClient() {
    }
    public static GameWebSocketClient me() {
        return Holder.ME;
    }

    /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
    private static class Holder {
        static final GameWebSocketClient ME = new GameWebSocketClient();
    }

}
