package org.cbzmq.game.net;

import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.BarSkeletonBuilder;
import com.iohao.game.action.skeleton.core.BarSkeletonBuilderParamConfig;
import com.iohao.game.action.skeleton.core.CmdInfo;
import com.iohao.game.action.skeleton.core.commumication.BroadcastContext;
import com.iohao.game.action.skeleton.core.flow.interal.DebugInOut;
import com.iohao.game.bolt.broker.client.AbstractBrokerClientStartup;
import com.iohao.game.bolt.broker.client.BrokerClientApplication;
import com.iohao.game.bolt.broker.core.client.BrokerAddress;
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.client.BrokerClientBuilder;
import com.iohao.game.bolt.broker.core.client.BrokerClientHelper;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.common.kit.NetworkKit;
import org.cbzmq.game.logic.GameCmd;
import org.cbzmq.game.logic.GameLogicEngine;
import org.cbzmq.game.logic.action.MyAction;
import org.cbzmq.game.model.Character;
import org.cbzmq.game.proto.Move;

public class GameStartUp extends AbstractBrokerClientStartup {

    public static void main(String[] args) {
        // 启动坦克游戏逻辑服
        BrokerClientApplication.start(new GameStartUp());
    }
    @Override
    public BarSkeleton createBarSkeleton() {
        BarSkeletonBuilder builder = new BarSkeletonBuilderParamConfig()
                .scanActionPackage(MyAction.class)
//                .setBroadcastLog(true)
                .createBuilder();
        builder.addInOut(new DebugInOut());
        BarSkeleton build = builder.build();
        return build;
    }

    @Override
    public BrokerClientBuilder createBrokerClientBuilder() {
        BrokerClientBuilder builder = BrokerClient.newBuilder();
        builder.appName("游戏逻辑服务器");
        return builder;
    }

    @Override
    public BrokerAddress createBrokerAddress() {
        // 类似 127.0.0.1 ，但这里是本机的 ip
        String localIp = NetworkKit.LOCAL_IP;
        // broker （游戏网关）默认端口
        int brokerPort = IoGameGlobalConfig.brokerPort;
        return new BrokerAddress(localIp, brokerPort);
    }





}
