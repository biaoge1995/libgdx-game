package org.cbzmq.game.net;

import com.iohao.game.bolt.broker.client.AbstractBrokerClientStartup;
import com.iohao.game.bolt.broker.core.client.BrokerAddress;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.external.core.ExternalServer;
import com.iohao.game.external.core.config.ExternalJoinEnum;
import com.iohao.game.external.core.netty.DefaultExternalServer;
import com.iohao.game.external.core.netty.DefaultExternalServerBuilder;
import com.iohao.game.external.core.netty.simple.NettyRunOne;
import java.util.List;

public class GameOne {


    public void start(){
        GameStartUp gameStartUp = new GameStartUp();
        new NettyRunOne().setExternalServerList(List.of(
//                createExternalServer(ExternalJoinEnum.TCP),
                createExternalServer(ExternalJoinEnum.WEBSOCKET)
//                createExternalServer(ExternalJoinEnum.UDP)
        )).setLogicServerList(List.of(gameStartUp)).startup();
    }

    static ExternalServer createExternalServer(ExternalJoinEnum joinEnum){

        int port = GameConfig.externalPort;
        port = joinEnum.cocPort(port);
        DefaultExternalServerBuilder builder = DefaultExternalServer
                .newBuilder(port)
                // 连接方式
                .externalJoinEnum(joinEnum)
                // 与 Broker （游戏网关）的连接地址
                .brokerAddress(new BrokerAddress("127.0.0.1", IoGameGlobalConfig.brokerPort));
        return builder.build();
    }
}
