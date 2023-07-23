package decode;

import awt.netty.ServerContext;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author chenbiao
 * @date 2020-12-19 10:24
 */
public class TcpServer {
    public static void main(String[] args) {
        // 我们要创建两个EventLoopGroup，
        // 一个是boss专门用来接收连接，可以理解为处理accept事件，
        // 另一个是worker，可以关注除了accept之外的其它事件，处理子任务。

        //上面注意，boss线程一般设置一个线程，设置多个也只会用到一个，而且多个目前没有应用场景，
        // worker线程通常要根据服务器调优，如果不写默认就是cpu的两倍。

        ServerContext serverContext = new ServerContext();
        serverContext.setHost("localhost");
        serverContext.setPort(8081);
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        //服务端要启动，需要创建ServerBootStrap，
        // 在这里面netty把nio的模板式的代码都给封装好了
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(bossGroup,workerGroup)
                .childOption(ChannelOption.TCP_NODELAY,true)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                //配置Server的通道，相当于NIO中的ServerSocketChannel
                .channel(NioServerSocketChannel.class)
                //childHandler表示给worker那些线程配置了一个处理器，
                // 配置初始化channel，也就是给worker线程配置对应的handler，当收到客户端的请求时，分配给指定的handler处理
                .childHandler(new ChannelInitializer<SocketChannel>(){

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast("stringMessageDecoder", new StringMessageDecoder())
                                .addLast("stringMessageEncoder", new StringMessageEncoder())
                                .addLast("",new GameServerHandler( serverContext));
                    }
                });

        System.out.println("服务端启动.......");
        serverBootstrap.bind(8081);
    }
}

