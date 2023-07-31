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
import org.cbzmq.game.enums.CharacterState;
import org.cbzmq.game.model.Bullet;
import org.cbzmq.game.model.Character;
import org.cbzmq.game.model.CharacterListener;
import org.cbzmq.game.model.Enemy;
import org.cbzmq.game.model.Player;
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
public class RemoteModel implements Model {

    Player player;
    Map map;

    Array<Bullet> bullets = new Array<>();
    Array<Enemy> enemies = new Array<>();

    Set<Integer> existsId = new HashSet<>();

    java.util.Map<Integer,Character> characterMap= new HashMap<>();

    Array<CharacterListener> listeners = new Array<>();
    EventQueue queue = new EventQueue(listeners);
    Assets assets;
    boolean isPlayerWin = false;
    boolean isGameOver = false;
    Channel ch;

    public static void main(String[] args) throws InterruptedException {
        new RemoteModel();
    }

    public RemoteModel() throws InterruptedException {
        this.assets = new Assets();
        map = new Map(assets.tiledMap);
        player = new Player(map);
        player.position.set(4, 8);

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

    public void sync(MsgProto.Msg msgProto) {



        for (CharacterProto.Character proto : msgProto.getCharacterDataList()) {
            existsId.add(proto.getId());

            Character character = null;


            switch (proto.getType()) {
                case player:
                    Player player = Player.parserProto(proto);
                    setPlayer(player);
                    break;
                case bullet:
                    character = Bullet.parserProto(proto);
//                    getBullets().add(bullet);
                    break;
                case enemy:
                    character = Enemy.parserProto(proto);
//                    getEnemies().add(enemy);
                    break;
            }
            if(character!=null && !characterMap.containsKey(proto.getId())){
                characterMap.put(proto.getId(),character);
            }
           else if(character!=null){
               characterMap.get(proto.getId()).updateByCharacter(character);;

            }
        }
        for (Character value : characterMap.values()) {
            if(!existsId.contains(value.getId())){
                value.state = CharacterState.death;
            }
            switch (value.characterType) {
                case player:
                    break;
                case bullet:

                    getBullets().add((Bullet) value);
                    break;
                case enemy:
                    getEnemies().add((Enemy) value);
                    break;
            }

        }

    }

    @Override
    public void update(float delta) {

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
    public Array<CharacterListener> getListeners() {
        return listeners;
    }

    @Override
    public void addListener(CharacterListener listener) {

    }

    @Override
    public Array<Bullet> getBullets() {
        return bullets;
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
    public EventQueue queue() {
        return queue;
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
    public int generalId() {
        return 0;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}


class UdpClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private RemoteModel model;


    public UdpClientHandler(RemoteModel model) {
        this.model = model;

    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        byte[] bytes = ByteBufUtil.getBytes(msg.content());
        MsgProto.Msg msgProto = MsgProto.Msg.parseFrom(bytes);
        model.sync(msgProto);
//        gameClient.acceptMsg(msgProto);
        if (msgProto.getCharacterDataList().size() > 0) {
//            System.out.println("消息大小" + msgProto.toByteArray().length);
//            System.err.println("客户端接收到消息: \nheader:" + msgProto.getHeader() + "\n" + msgProto.getCharacterDataList().toString());
//            System.out.println("element数量：" + msgProto.getCharacterDataList().size());


        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}