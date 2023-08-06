package org.cbzmq.game.netty;

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
import org.cbzmq.game.MathUtils;
import org.cbzmq.game.enums.CharacterState;
import org.cbzmq.game.enums.MsgHeader;
import org.cbzmq.game.model.Character;
import org.cbzmq.game.model.CharacterAdapter;
import org.cbzmq.game.model.Group;
import org.cbzmq.game.proto.CharacterIntProto;
import org.cbzmq.game.proto.CharacterProto;
import org.cbzmq.game.proto.MsgByte;
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


public final class UdpServer extends CharacterAdapter {

    private static final int PORT = Integer.parseInt(System.getProperty("port", "7686"));
    private final Array<Character> all = new Array<>();
    private Channel ch;


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
    public void frameEnd(Character root, float time) {
        all.clear();
        ((Group)root).flat(all);
//        MsgByte msgByte = new MsgByte(MsgHeader.SYNC_CHARACTERS_INFO, new Date().getTime());
//        msgByte.setCharacters(all);
        Array<CharacterProto.Character> characterProtos = new Array<>();
//        Array<Byte> bytes = new Array<>();
        for (Character character : all) {
            if(character.state == CharacterState.death) continue;
            CharacterProto.Character proto = character.toCharacterProto().build();
            characterProtos.add(proto);
//            bytes.addAll(character.toCharacterBytes().getBytes());
        }


        MsgProto.Msg.Builder builder = MsgProto.Msg.newBuilder();
        MsgProto.Msg msg = builder
                .setHeader(MsgHeader.SYNC_CHARACTERS_INFO)
                .addAllCharacterData(characterProtos)
                .setTimeStamp(new Date().getTime())
                .build();

        byte[] bytes = msg.toByteArray();

//        byte[] bytes = msgByte.toByteArray();

        //二次压缩
        MathUtils.CompressData compress = MathUtils.compress( bytes);
        ByteBuf byteBuf = Unpooled.copiedBuffer(compress.getOutput());
//        System.out.println(msg);
        System.out.println("元素数量" + msg.getCharacterDataList().size());
//        System.out.println("protobuf消息长度" + bytes.length + "byte");
//        System.out.println("string消息长度" + msg.toString().getBytes(StandardCharsets.UTF_8).length + "byte\n");

//            Iterator<Channel> iterator = null;
//            while (iterator.hasNext()) {
//                Channel channel = iterator.next();
//                channel.writeAndFlush(new DatagramPacket(
//                        byteBuf,
//                        SocketUtils.socketAddress("127.0.0.1", 8088))).sync();
//            }

        try {
            ch.writeAndFlush(new DatagramPacket(
                    byteBuf,
                    SocketUtils.socketAddress("127.0.0.1", 8088)
//                    SocketUtils.socketAddress("192.168.2.145", 8088)
            )).sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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