package decode;

import awt.game.Game;
import awt.model.domain.Player;
import awt.model.msg.Msg;
import awt.netty.ClientContext;
import awt.proto.enums.MsgAction;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @ClassName ClientHandler
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/6/26 6:16 下午
 * @Version 1.0
 **/
public class ClientHandler extends ChannelInboundHandlerAdapter {
    private final ClientContext clientContext = new ClientContext();
    private Game client;

    private ChannelHandlerContext ctx;

    public ClientHandler() throws UnknownHostException {
        InetAddress localHost = InetAddress.getLocalHost();
        System.out.println("本机IP地址：" + localHost.getHostAddress());
        clientContext.setServerHost(localHost.getHostAddress());
        clientContext.setServerPort(8081);
        clientContext.setClientHost(localHost.getHostAddress());
        clientContext.setPlayer(new Player(localHost.getHostAddress(), 0));
    }


//    public void send(MsgAction action) {
//        Msg sendMsg = null;
//        switch (action) {
//            case SYNC_GAME_STATUS:
//                sendMsg = new Msg(MsgAction.SYNC_GAME_STATUS, client.getGameStatus());
//                sendMsg.setDateTime();
//                break;
//
//            case HEART:
//                break;
//        }
//        if (sendMsg != null)
//            ctx.channel().writeAndFlush(sendMsg);
//    }





    public String getChannelRemoteAddress(Channel channel){
        String hostAndPort = channel.remoteAddress().toString().substring(1);
        return hostAndPort;
    }

    public String getChannelLocalAddress(Channel channel){
        String localAndPort = channel.localAddress().toString().substring(1);
        return localAndPort;
    }

    public void startClient() throws Exception {
        client = new Game(false);
    }

    public void write(Msg msg) {
        ctx.channel().writeAndFlush(Unpooled.copiedBuffer(msg.toString().getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 渠道注册成功后登陆
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.ctx = ctx;

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        System.err.println(clientContext.getPlayer().toString() + "退出登陆");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
//            ByteBuf bb = (ByteBuf) msg;
//            int i = bb.readableBytes();
//            byte[] respByte = new byte[bb.readableBytes()];
//            bb.readBytes(respByte);
//            String respStr = new String(respByte, StandardCharsets.UTF_8);



        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
