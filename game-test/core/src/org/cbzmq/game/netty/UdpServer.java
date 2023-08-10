package org.cbzmq.game.netty;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.internal.SocketUtils;
import org.cbzmq.game.Utils;
import org.cbzmq.game.enums.CharacterState;
import org.cbzmq.game.enums.CharacterType;
import org.cbzmq.game.enums.MsgHeader;
import org.cbzmq.game.model.*;
import org.cbzmq.game.model.Character;
import org.cbzmq.game.proto.CharacterProto;
import org.cbzmq.game.proto.MsgProto;

import java.net.InetSocketAddress;
import java.util.Date;

/**
 * @ClassName UdpServer
 * A UDP server that responds to the QOTM (quote of the moment) request to a {@link }.
 * <p>
 * Inspired by <a href="https://docs.oracle.com/javase/tutorial/networking/datagrams/clientServer.html">the official
 * Java tutorial</a>.
 * @Author chenbiao
 * @Date 2023/6/28 6:10 下午
 * @Version 1.0
 **/


public final class UdpServer extends ObserverAdapter {

    private static final int PORT = Integer.parseInt(System.getProperty("port", "7686"));
    private Channel ch;
    private static long counter = 0;
    private static final Array<CharacterProto.Character> characterProtos = new Array<>();

    private final Array<Character> container = new Array<>();
    private final Array<MsgProto.Event> protoEvents = new Array<>();


    public UdpServer() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new UdpServerHandler());
        ChannelFuture f = b.bind(PORT).sync();//绑定端口
        ch = f.channel();//获取ch
        f.channel().closeFuture();//如果端口连接不上就关闭监听


    }

    @Override
    public boolean onTwoObserverEvent(Event.TwoObserverEvent event) {
        if (!(event.getA() instanceof Character && event.getB() instanceof Character)) return false;
        switch (event.getEventType()) {
            case hit:
            case beKilled:
            case collisionCharacter:
                break;
        }
        return false;
    }

    @Override
    public boolean onOneObserverEvent(Event.OneCharacterEvent event) {

        switch (event.getEventType()) {
            case aimPoint:
            case attack:
            case born:
            case jump:
            case beDeath:
            case beRemove:
            case win:
            case moveLeft:
            case moveRight:
            case bloodUpdate:
            case stateUpdate:
                if(event.getCharacter().getCharacterType()== CharacterType.unknown)return false;
                protoEvents.add(event.toMsgProtoEvent());
                break;
            case frameEnd:
                Group root = (Group) (event.getCharacter());
                byte[] bytes=null;
                //没60个轮训给客户端同步一次数据
//                counter % 60 == 0
                if (false) {
                    syncAllCharacter(root);
                    MsgProto.Msg.Builder builder = MsgProto.Msg.newBuilder();
                    MsgProto.Msg msg = builder
                            .setId(counter)
                            .setHeader(MsgHeader.SYNC_CHARACTERS_INFO)
                            .addAllCharacterData(characterProtos)
                            .setTimeStamp(new Date().getTime())
                            .build();

                    bytes = msg.toByteArray();
                } else if(protoEvents.size>0) {
                    MsgProto.Msg.Builder builder = MsgProto.Msg.newBuilder();
                    MsgProto.Msg msg = builder
                            .setId(counter)
                            .setHeader(MsgHeader.SYNC_CHARACTERS_EVENT)
                            .addAllEvents(protoEvents)
                            .setTimeStamp(new Date().getTime())
                            .build();

                    bytes = msg.toByteArray();
//                    Gdx.app.log("events", msg.toString());
                }

                if(bytes==null) return false;
                //二次压缩
                Utils.CompressData compress = Utils.compress(bytes);
                ByteBuf byteBuf = Unpooled.copiedBuffer(compress.getOutput());

//              System.out.println("元素数量" + msg.getCharacterDataList().size());
//              System.out.println("protobuf消息长度" + bytes.length + "byte");


                try {
                    ch.writeAndFlush(new DatagramPacket(
                            byteBuf,
                            SocketUtils.socketAddress("127.0.0.1", 8088)
//                            SocketUtils.socketAddress("192.168.2.145", 8088)
                    )).sync();
                    if (counter == Long.MAX_VALUE) {

                        counter = 0;
                    } else {
                        counter++;
                        protoEvents.clear();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

        }
        return true;
    }

    public void syncAllCharacter(Group root) {
        container.clear();
        characterProtos.clear();
        root.flat(container);

        for (Character character : container) {
            if (character.state == CharacterState.death) continue;
            CharacterProto.Character proto = character.toCharacterProto().build();
            characterProtos.add(proto);
        }
    }
}


class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {


    public UdpServerHandler() {

    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        byte[] bytes = ByteBufUtil.getBytes(packet.content());
        MsgProto.Msg msgProto = MsgProto.Msg.parseFrom(bytes);
        InetSocketAddress recipient = packet.recipient();
//        System.out.println(packet.recipient().getAddress());
//        System.out.println(packet.recipient().getHostName());
//        System.out.println(packet.recipient().getPort());
//        System.out.println(packet.recipient().getHostString());
        System.out.println("服务端收到的消息\n" + msgProto);
//        game.handlerProtoCommands(ctx.channel(), msgProto, packet.sender());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        // We don't close the channel because we can keep serving requests.
    }
}