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
import org.cbzmq.game.proto.MsgByte;
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


    Set<Integer> existsId = new HashSet<>();


    final Array<Character> orderCharacterList = new Array<>();

    final java.util.Map<Integer,Character> characterMap = new HashMap<>();

    final Array<Enemy> enemies = new Array<>();

    Array<Character> listeners = new Array<>();
    EventQueue queue = new EventQueue(listeners);
    Assets assets;
    boolean isPlayerWin = false;
    boolean isGameOver = false;
    Channel ch;

    boolean isStartSynced = false;

    public static void main(String[] args) throws InterruptedException {
        new RemoteModel();
    }

    public RemoteModel() throws InterruptedException {
        this.assets = new Assets();
        map = new Map(assets.tiledMap);
        player = new Player();
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

    public boolean isStartSynced() {
        return isStartSynced;
    }

    public void setStartSynced(boolean startSynced) {
        isStartSynced = startSynced;
    }

    public void sync(MsgByte msgProto) {

        isStartSynced = false;
        existsId.clear();

        for (Character character : msgProto.getCharacters()) {
            existsId.add(character.getId());




            int index = character.getId() - 1;
            if (character != null && !characterMap.containsKey(character.getId())) {
                characterMap.put(character.getId(), character);
                orderCharacterList.add(character);
            } else if (character != null) {
                characterMap.get(character.getId()).updateByCharacter(character);
                ;

            }
        }


        for (Character value : orderCharacterList) {
            if (!existsId.contains(value.getId())) {
                value.beDeath();
            }
            switch (value.characterType) {
                case player:
                    player = (Player) value;
                    break;
//                case bullet:
//                    getBullets().add((Bullet) value);
//                    break;
                case enemy:
                    getEnemies().add((Enemy) value);
                    break;
            }

        }


    }

    @Override
    public void update(float delta) {
//        for (Character value : characterMap.values()) {
//            value.update(delta);
//        }
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
    public Array<Observer> getListeners() {
        return null;
    }


    @Override
    public void addListener(Observer listener) {

    }




    @Override
    public Array<Bullet> getBullets() {
        return null;
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
    public Array<Character> getAll() {
        return orderCharacterList;
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

        byte[] decompress = MathUtils.decompress(bytes);

        System.out.println("msg byte length :" + bytes.length + "byte");
//        MsgProto.Msg msgProto = MsgProto.Msg.parseFrom(decompress);
        MsgByte msgByte = MsgByte.parseFromBytes(decompress);


        model.sync(msgByte);



    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}