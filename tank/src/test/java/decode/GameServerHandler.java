package decode;

import awt.game.Game;
import awt.netty.ServerContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.text.SimpleDateFormat;

/**
 * @ClassName ClientHandler
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/6/26 6:16 下午
 * @Version 1.0
 **/
public class GameServerHandler extends ChannelInboundHandlerAdapter {

    private final Game game = new Game(true);
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private ServerContext serverContext;
    public GameServerHandler(ServerContext serverContext) throws Exception {
        this.serverContext = serverContext;
    }

    /**
     * 处理客户端请求以及回应
     * @param channel
     */
    public String getChannelRemoteAddress(Channel channel){
        String hostAndPort = channel.remoteAddress().toString().substring(1);
        return hostAndPort;
    }

    public String getChannelLocalAddress(Channel channel){
        String localAndPort = channel.localAddress().toString().substring(1);
        return localAndPort;
    }



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
//            ByteBuf bb = (ByteBuf) msg;
//            int i = bb.readableBytes();
//            byte[] respByte = new byte[i];
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
