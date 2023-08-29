package org.cbzmq.game.net;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.iohao.game.action.skeleton.annotation.ActionController;
import com.iohao.game.action.skeleton.annotation.ActionMethod;
import com.iohao.game.action.skeleton.core.BarSkeleton;
import com.iohao.game.action.skeleton.core.BarSkeletonBuilder;
import com.iohao.game.action.skeleton.core.BarSkeletonBuilderParamConfig;
import com.iohao.game.action.skeleton.core.flow.interal.DebugInOut;
import com.iohao.game.bolt.broker.client.AbstractBrokerClientStartup;
import com.iohao.game.bolt.broker.core.client.BrokerAddress;
import com.iohao.game.bolt.broker.core.client.BrokerClient;
import com.iohao.game.bolt.broker.core.client.BrokerClientBuilder;
import com.iohao.game.bolt.broker.core.common.IoGameGlobalConfig;
import com.iohao.game.common.kit.NetworkKit;
import com.iohao.game.common.kit.log.IoGameLoggerFactory;
import com.iohao.game.external.core.ExternalServer;
import com.iohao.game.external.core.config.ExternalJoinEnum;
import com.iohao.game.external.core.netty.DefaultExternalServer;
import com.iohao.game.external.core.netty.DefaultExternalServerBuilder;
import com.iohao.game.external.core.netty.simple.NettyRunOne;
import org.slf4j.Logger;

import java.util.List;

/**
 * @ClassName ChenbiaoLogicServer
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/8/18 11:02 上午
 * @Version 1.0
 **/
public class ChenbiaoLogicServer extends AbstractBrokerClientStartup {
    static final Logger log = IoGameLoggerFactory.getLoggerCommonStdout();
    static final int externalCorePort = 10100;
    @Override
    public BarSkeleton createBarSkeleton() {

        //定义逻辑服务器用哪个控制器
        BarSkeletonBuilder builder = new BarSkeletonBuilderParamConfig()
                .scanActionPackage(ChenbiaoAction.class)
                .createBuilder();
        builder.addInOut(new DebugInOut());
        return builder.build();
    }

    @Override
    public BrokerClientBuilder createBrokerClientBuilder() {
        BrokerClientBuilder builder = BrokerClient.newBuilder();
        builder.appName("逻辑服务器");
        return builder;
    }

    @Override
    public BrokerAddress createBrokerAddress() {
        String localIp = NetworkKit.LOCAL_IP;
        // broker （游戏网关）默认端口
        int brokerPort = 10200;
        return new BrokerAddress(localIp, brokerPort);
    }

    @ActionController(2)
    public static class ChenbiaoAction{

        @ActionMethod(2)
        public Msg login(Msg msg){
            log.info("welcome "+msg.name);
            msg.name="逻辑服务已经收到你的请求，感谢";
            return msg;
        }
    }

    @ProtobufClass
    public static class Msg{
        public String name;
    }

    static ExternalServer createExternalServer(ExternalJoinEnum joinEnum){

        int port = externalCorePort;
        port = joinEnum.cocPort(port);
        DefaultExternalServerBuilder builder = DefaultExternalServer
                .newBuilder(port)
                // 连接方式
                .externalJoinEnum(joinEnum)
                // 与 Broker （游戏网关）的连接地址
                .brokerAddress(new BrokerAddress("127.0.0.1", IoGameGlobalConfig.brokerPort));

//        ExternalServer externalServer = ExternalServerCreateKit.createExternalServer(externalCorePort, joinEnum);
        return builder.build();
    }



    public static void main(String[] args) {
        ChenbiaoLogicServer chenbiaoLogicServer = new ChenbiaoLogicServer();
        new NettyRunOne().setExternalServerList(List.of(
//                createExternalServer(ExternalJoinEnum.TCP),
                createExternalServer(ExternalJoinEnum.WEBSOCKET)
//                createExternalServer(ExternalJoinEnum.UDP)
        )).setLogicServerList(List.of(chenbiaoLogicServer)).startup();

//        NettySimpleHelper.run(10100, List.of(chenbiaoLogicServer));
    }
}


