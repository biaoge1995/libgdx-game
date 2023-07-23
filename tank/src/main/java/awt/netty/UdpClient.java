package awt.netty;


import awt.game.Game;
import awt.game.GameClient;
import awt.game.GameJFrame;
import awt.proto.Element;
import awt.proto.Msg;
import awt.proto.command.Command;
import awt.proto.enums.MsgAction;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.internal.SocketUtils;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @ClassName UdpClient
 * A UDP broadcast client that asks for a quote of the moment (QOTM) to {@link UdpServer}.
 * <p>
 * Inspired by <a href="https://docs.oracle.com/javase/tutorial/networking/datagrams/clientServer.html">the official
 * Java tutorial</a>.
 * @Author chenbiao
 * @Date 2023/6/28 6:10 下午
 * @Version 1.0
 **/


public final class UdpClient {

    static final int PORT = Integer.parseInt(System.getProperty("port", "7686"));

    public static void main(String[] args) throws Exception {
        Game game = new Game(false);
        game.loading();
        GameClient gameClient = new GameClient(game, "127.0.0.1", PORT);
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new UdpClientHandler(gameClient));

            Channel ch = b.bind(8088).sync().channel();
            gameClient.setChannel(ch);
            GameJFrame gameJFrame = new GameJFrame(game.gameContext().getGameMapContext(), "客户端", gameClient, gameClient.Player());

            Timer time = new Timer();
            time.schedule(new TimerTask() {
                @Override
                public void run() {
                    game.run();
                    gameJFrame.rePaintPanel();

                }
            }, 1000, 10);

////             Broadcast the QOTM request to port 8080.
//            awt.proto.Msg msg = awt.proto.Msg.newBuilder()
//                    .setHeader(MsgAction.SYNC_GAME_MAP)
//                    .setTimeStamp((int) (new Date().getTime()))
//                    .build();
//            byte[] bytes = msg.toByteArray();
//
//            ch.writeAndFlush(new DatagramPacket(
//                    Unpooled.copiedBuffer(bytes),
//                    SocketUtils.socketAddress("127.0.0.1", PORT))).sync();
            ch.closeFuture().await();
        } finally {
            group.shutdownGracefully();
        }
    }
}


class UdpClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private Game game;
    private GameClient gameClient;


    public UdpClientHandler(GameClient gameClient) throws Exception {
        this.gameClient = gameClient;
        this.game = gameClient.Game();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        byte[] bytes = ByteBufUtil.getBytes(msg.content());
        Msg msgProto = Msg.parseFrom(bytes);

        gameClient.acceptMsg(msgProto);
        if(msgProto.getCommandsList().size()>0){
            System.out.println("消息大小"+msgProto.toByteArray().length);
            System.err.println("客户端接收到消息: \nheader:"+msgProto.getHeader()+"\n"+ msgProto.getCommandsList().toString());
            System.out.println("element数量："+msgProto.getElementsList().size());
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
