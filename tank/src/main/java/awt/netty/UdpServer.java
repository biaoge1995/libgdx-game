package awt.netty;

import awt.game.Game;
import awt.game.GameJFrame;
import awt.proto.Msg;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.internal.SocketUtils;

import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @ClassName UdpServer
 * A UDP server that responds to the QOTM (quote of the moment) request to a {@link UdpClient}.
 * <p>
 * Inspired by <a href="https://docs.oracle.com/javase/tutorial/networking/datagrams/clientServer.html">the official
 * Java tutorial</a>.
 * @Author chenbiao
 * @Date 2023/6/28 6:10 下午
 * @Version 1.0
 **/


public final class UdpServer {

    private static final int PORT = Integer.parseInt(System.getProperty("port", "7686"));

    private Channel ch;
    private Game game;
    private GameJFrame gameJFrame;

    public static void main(String[] args) throws Exception {
        UdpServer udpServer = new UdpServer();
        udpServer.startUdpServer(new Game(true));

        Timer time = new Timer();

        time.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    udpServer.game.run();
                    Queue<Msg> msgQueue = udpServer.game.msgQueue;
                    udpServer.gameJFrame.rePaintPanel();
                    while (msgQueue.size() > 0) {
                        Msg msg = msgQueue.remove();

                        byte[] bytes = msg.toByteArray();
                        ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
//                        System.out.println("命令队列大小" + msg.getCommandsList().size());
//                        System.out.println("元素数量" + msg.getElementsList().size());
//                        System.out.println("protobuf消息长度" + bytes.length + "byte");
//                        System.out.println("string消息长度" + msg.toString().getBytes(StandardCharsets.UTF_8).length + "byte\n");

                        Iterator<Channel> iterator = udpServer.game.gameContext().getAllPlayerChannels().iterator();
                        while (iterator.hasNext()) {
                            Channel channel = iterator.next();
                            channel.writeAndFlush(new DatagramPacket(
                                    byteBuf,
                                    SocketUtils.socketAddress("127.0.0.1", 8088))).sync();
                        }

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }
    ,1000,10);


//        udpServer.f.closeFuture().await();
}


    public void startUdpServer(Game game) throws Exception {
        this.game = game;
        this.game.loading();
        gameJFrame = new GameJFrame(game.gameContext().getGameMapContext(), "服务端", null, game.systemPlayer);
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new UdpServerHandler(game));
            ChannelFuture f = b.bind(PORT).sync();//绑定端口
            ch = f.channel();//获取ch
            f.channel().closeFuture();//如果端口连接不上就关闭监听


        } catch (Exception e) {
            e.printStackTrace();
        }
//        finally {
//            group.shutdownGracefully();
//        }
    }
}


class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private Game game;


    public UdpServerHandler(Game game) {
        this.game = game;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        byte[] bytes = ByteBufUtil.getBytes(packet.content());
        Msg msgProto = Msg.parseFrom(bytes);
        InetSocketAddress recipient = packet.recipient();
//        System.out.println(packet.recipient().getAddress());
//        System.out.println(packet.recipient().getHostName());
//        System.out.println(packet.recipient().getPort());
//        System.out.println(packet.recipient().getHostString());
        System.out.println("服务端收到的消息\n" + msgProto);
        game.handlerProtoCommands(ctx.channel(), msgProto, packet.sender());
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