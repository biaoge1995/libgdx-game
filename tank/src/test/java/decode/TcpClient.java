package decode;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.SimpleClassResolver;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @author chenbiao
 * @date 2020-12-19 10:24
 */
public class TcpClient {


    public static void main(String[] args) throws InterruptedException, UnsupportedEncodingException {

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        // 使用river作为marshalling的方式
        MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("river");
        MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(4);
        configuration.setClassCount(10);
        configuration.setBufferSize(8096);
        configuration.setInstanceCount(100);
//        configuration.setExceptionListener(new MarshallingException());
        configuration.setClassResolver(new SimpleClassResolver(TcpClient.class.getClassLoader()));


        //服务端要启动，需要创建ServerBootStrap，
        // 在这里面netty把nio的模板式的代码都给封装好了
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(
                        new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                // 序列化对象的解码
                                socketChannel.pipeline()
                                        .addLast("stringMessageDecoder", new StringMessageDecoder())
                                        .addLast("stringMessageEncoder", new StringMessageEncoder())
                                        .addLast(new ClientHandler());
                            }
                        });
        ChannelFuture cf = bootstrap.connect("localhost", 8081).sync();

        cf.channel().writeAndFlush(Unpooled.copiedBuffer("test".getBytes(StandardCharsets.UTF_8)));
        System.out.println("客户端启动");


//
//        // 等待直到连接中断了
//        cf.channel().closeFuture().sync();
    }
}

