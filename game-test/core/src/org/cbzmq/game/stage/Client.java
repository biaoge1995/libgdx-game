package org.cbzmq.game.stage;

import com.badlogic.gdx.utils.Array;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.cbzmq.game.Assets;
import org.cbzmq.game.Map;
import org.cbzmq.game.MathUtils;
import org.cbzmq.game.model.*;
import org.cbzmq.game.model.Character;
import org.cbzmq.game.proto.CharacterProto;
import org.cbzmq.game.proto.MsgProto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName RemoteModel
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/28 1:03 上午
 * @Version 1.0
 **/
public class Client implements Model {

    final Player player;
    final Map map;
    final Set<Integer> existsId = new HashSet<>();
    final Array<Character> orderCharacterList = new Array<>();
    final java.util.Map<Integer, Character> characterMap = new HashMap<>();
    final Array<Enemy> enemies = new Array<>();
    final Array<Observer> listeners = new Array<>();
    final EventQueue queue = new EventQueue();
    final Assets assets;
    final Physic2DEngine physic2DEngine;
    boolean isPlayerWin = false;
    boolean isGameOver = false;
    Channel ch;
    boolean isStartSynced = false;
    private int msgMaxId=0;



    public Client() throws InterruptedException {
        this.assets = new Assets();
        this.map = new Map(assets.tiledMap);
        this.player = new Player();
        this.player.position.set(4, 8);
        this.queue.setObservers(listeners);

        this.physic2DEngine = Physic2DEngine
                .newBuilder()
                .setAssets(assets)
                .setMap(map)
                .setCollisionLayer(map.collisionLayer)
                .setQueue(queue)
                .setRoot(orderCharacterList)
                .build();

        EventLoopGroup group = new NioEventLoopGroup();
//        try {
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new UdpClientHandler(this));
        ChannelFuture f = b.bind(8088).sync();

        ch = f.channel();
        f.channel().closeFuture();//如果端口连接不上就关闭监听
//            ch.closeFuture().await();
//        }  finally {
//            group.shutdownGracefully();
//        }

    }

    public boolean isStartSynced() {
        return isStartSynced;
    }

    public void setStartSynced(boolean startSynced) {
        isStartSynced = startSynced;
    }

    public void sync(MsgProto.Msg msgProto) {
        //如果消息是乱序到达则丢弃掉id小于当前id的消息
        if(msgProto.getId()<msgMaxId) return;

        isStartSynced = false;
        existsId.clear();

        for (CharacterProto.Character proto : msgProto.getCharacterDataList()) {
            existsId.add(proto.getId());
            Character character = null;


            switch (proto.getType()) {
                case player:
                    character = Player.parserProto(proto);
                    break;
                case bullet:
                    character = Bullet.parserProto(proto);
                    break;
                case enemy:
                    character = Enemy.parserProto(proto);
                    break;
            }
            if (character != null && !characterMap.containsKey(character.getId())) {
                characterMap.put(proto.getId(), character);
                orderCharacterList.add(character);
            } else if (character != null) {
                characterMap.get(character.getId()).updateByCharacter(character);

            }
        }

        //默认没有从服务器更新的元素全部置为死亡
        for (Character value : orderCharacterList) {
            if (!existsId.contains(value.getId())) {
                value.beDeath();
            }
//            switch (value.characterType) {
//                case player:
//                    player.updateByCharacter((Player) value);
//                    break;
//                case enemy:
//                    enemies.add((Enemy) value);
//                    break;
//            }

        }


    }

    @Override
    public void update(float delta) {
        physic2DEngine.update(delta);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Map getMap() {
        return map;
    }


    @Override
    public void addListener(Observer listener) {

    }


    @Override
    public Array<Enemy> getEnemies() {
        return enemies;
    }

    @Override
    public Assets getAssets() {
        return assets;
    }


    @Override
    public float getTimeScale() {
        return 0;
    }

    @Override
    public void setTimeScale(float timeScale) {

    }

    @Override
    public void restart() {

    }


    @Override
    public boolean isPlayerWin() {
        return isPlayerWin;
    }

    @Override
    public boolean isGameOver() {
        return isGameOver;
    }

    @Override
    public Array<Character> getAll() {
        return orderCharacterList;
    }

    @Override
    public Array<Observer> getListeners() {
        return listeners;
    }

    @Override
    public EventQueue getQueue() {
        return null;
    }

}


class UdpClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private final Client model;


    public UdpClientHandler(Client model) {
        this.model = model;

    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        byte[] bytes = ByteBufUtil.getBytes(msg.content());

        byte[] decompress = MathUtils.decompress(bytes);

        System.out.println("msg byte length :" + bytes.length + "byte");
//        MsgProto.Msg msgProto = MsgProto.Msg.parseFrom(decompress);
        MsgProto.Msg msg1 = MsgProto.Msg.parseFrom(decompress);


        model.sync(msg1);


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}