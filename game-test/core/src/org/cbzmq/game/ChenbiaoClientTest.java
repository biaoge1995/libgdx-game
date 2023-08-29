package org.cbzmq.game;

import com.iohao.game.external.client.AbstractInputCommandRegion;
import com.iohao.game.external.client.InputCommandRegion;
import com.iohao.game.external.client.join.ClientRunOne;
import com.iohao.game.external.core.config.ExternalJoinEnum;
import org.cbzmq.game.model.Player;
import org.cbzmq.game.net.ChenbiaoLogicServer;
import org.cbzmq.game.net.GameConfig;
import org.cbzmq.game.proto.Move;
import org.cbzmq.game.proto.Msg;
import org.cbzmq.game.proto.VectorProto;

import java.util.List;

/**
 * @ClassName ChenbiaoClient
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/8/18 11:23 上午
 * @Version 1.0
 **/
public class ChenbiaoClientTest extends AbstractInputCommandRegion {

    @Override
    public void initInputCommand() {
        inputCommandCreate.cmd = 2;
        Player player = new Player();
        player.setId(2);
        long start = System.currentTimeMillis();
        Move move = new Move(player.id
                , Move.MoveType.moveRight
                , 1f
                , new VectorProto(player.position.x,player.position.y)
                , new VectorProto(player.velocity.x,player.velocity.y));

        ofCommand(1).callback(Move.class, result -> {
            Move back = result.getValue();
            long end = System.currentTimeMillis();
            System.out.println("耗时"+(end-start));
            System.out.println(back);

        }).setDescription("请求 move").setRequestData(move);

        ChenbiaoLogicServer.Msg msg = new ChenbiaoLogicServer.Msg();
        msg.name = "彪哥";
        ofCommand(9).callback(ChenbiaoLogicServer.Msg.class, result -> {
            ChenbiaoLogicServer.Msg msg2 = result.getValue();
            long end = System.currentTimeMillis();
            System.out.println("耗时"+(end-start));
            System.out.println(msg2.name);

        }).setDescription("请求 login").setRequestData(msg);
    }



    @Override
    public void loginSuccessCallback() {
        super.loginSuccessCallback();
    }

    public static void main(String[] args) {
        List<InputCommandRegion> chenbiaoClients = List.of(new ChenbiaoClientTest());
        new ClientRunOne()
                .setInputCommandRegions(chenbiaoClients )
                .setConnectPort(GameConfig.externalPort)
                .setJoinEnum(ExternalJoinEnum.WEBSOCKET)
//                .setWebsocketPath("ws://127.0.0.1:10100/websocket")
//                .setConnectAddress()
                .startup();
    }
}
