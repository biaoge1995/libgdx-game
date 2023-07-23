package decode;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * @ClassName StringMessageEncoder
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/6/29 12:05 上午
 * @Version 1.0
 **/
public class StringMessageEncoder extends MessageToMessageEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, List out) throws Exception {
        System.out.println("StringMessageEncoder 消息正在进行编码....");
        //将 String转换为 ByteBuf，传递到下一个handler
        out.add(Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
    }
}

