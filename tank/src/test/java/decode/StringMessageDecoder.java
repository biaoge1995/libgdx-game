package decode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * @ClassName StringMessageDecoder
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/6/29 12:04 上午
 * @Version 1.0
 **/
public class StringMessageDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        System.out.println("StringMessageDecoder 消息正在进行解码...");
        //将 ByteBuf转换为 String，传递到下一个handler
        out.add(msg.toString(CharsetUtil.UTF_8));
    }

}

