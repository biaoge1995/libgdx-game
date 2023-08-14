package org.cbzmq.game.stage;

import com.badlogic.gdx.math.Vector2;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.cbzmq.game.Utils;
import org.cbzmq.game.enums.OneBodyEventType;
import org.cbzmq.game.model.Character;
import org.cbzmq.game.model.*;
import org.cbzmq.game.proto.CharacterProto;
import org.cbzmq.game.proto.MsgProto;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName RemoteModel
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/28 1:03 上午
 * @Version 1.0
 **/
public class Client extends AbstractEngine {

    final Set<Integer> existsId = new HashSet<>();
    final Map<Integer, Character> characterMap = new HashMap<>();
    Channel ch;
    private int msgMaxId = 0;


    public Client() throws InterruptedException {
        super();
        restart();
        EventLoopGroup group = new NioEventLoopGroup();
//        try {
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new UdpClientHandler(this));
        ChannelFuture f = b.bind(8088).sync();

        ch = f.channel();
        f.channel().closeFuture();
        //如果端口连接不上就关闭监听
        //            ch.closeFuture().await();
        //        }  finally {
        //            group.shutdownGracefully();
        //        }

    }



    public void upsertCharacter(Character character,boolean isUpdateDetail) {
        if (character == null) return;
        if (!characterMap.containsKey(character.getId())) {
            characterMap.put(character.getId(), character);
            character.setModel(this);

            switch (character.getCharacterType()) {
                case player:
                case bullet:
                    playerGroup.addCharacter(character);
                    break;
                case enemy:
                    enemyGroup.addCharacter((Enemy) character);
                    break;
            }

        } else if(isUpdateDetail){
            characterMap.get(character.getId()).updateByCharacter(character);
        }


    }

    public Character getCharacterByIndex(int index) throws Exception {
        if(!characterMap.containsKey(index)){
            throw new Exception("can not find index "+index);
        }
        return characterMap.get(index);
    }

    public void syncEvent(MsgProto.Msg msgProto) throws Exception {
        switch (msgProto.getHeader()) {
            case SYNC_CHARACTERS_EVENT:
                for (MsgProto.Event event : msgProto.getEventsList()) {
                    Character character = parse(event.getOne());


                    upsertCharacter(character,true);

                    character = getCharacterByIndex(event.getOne().getId());

                    Event.OneCharacterEvent oneCharacterEvent
                            = new Event.OneCharacterEvent(Client.class.getName()
                            , msgProto.getTimeStamp()
                            , event.getOneBodyEvent()
                            , character);
                    oneCharacterEvent.setVector(new Vector2(event.getVector().getX(), event.getVector().getY()));
                    oneCharacterEvent.setFloatData(event.getFloatData());
                    oneCharacterEvent.setState(event.getState());

                    updateByEvent(oneCharacterEvent);
                }
        }
    }


    public Character parse(CharacterProto.Character proto) {
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
        return character;
    }


    public void sync(MsgProto.Msg msgProto) {
        //如果消息是乱序到达则丢弃掉id小于当前id的消息
        if (msgProto.getId() < msgMaxId) return;
        existsId.clear();
        for (CharacterProto.Character proto : msgProto.getCharacterDataList()) {
            existsId.add(proto.getId());
            Character parse = parse(proto);
            upsertCharacter(parse,true);
        }

        //默认没有从服务器更新的元素全部置为死亡
        for (Character value : container) {
            switch (value.characterType) {
                case enemy:
                case bullet:
                    if (!existsId.contains(value.getId())) {
                        value.beDeath();
                    }
                    break;
            }


        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        super.frameEnd(delta);
    }

    @Override
    public void restart() {
        super.restart();
        characterMap.clear();
        characterMap.put(player.getId(),player);
    }



    @Override
    public void updateByEvent(Event.OneCharacterEvent event) {
        super.updateByEvent(event);
    }

    @Override
    public void updateBy2CharacterEvent(Event.TwoObserverEvent event) {
        super.updateBy2CharacterEvent(event);
    }

    @Override
    public void save() {

    }

    @Override
    public void quit() {

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

        byte[] decompress = Utils.decompress(bytes);

//        System.out.println("msg byte length :" + bytes.length + "byte");
//        MsgProto.Msg msgProto = MsgProto.Msg.parseFrom(decompress);
        MsgProto.Msg msg1 = MsgProto.Msg.parseFrom(decompress);


        model.syncEvent(msg1);


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}