package org.cbzmq.game.netty;


import io.netty.channel.Channel;
import java.util.Date;


/**
 * @ClassName ClientContext
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/6/26 7:08 下午
 * @Version 1.0
 **/

public class ClientContext {
    private String serverHost;
    private int serverPort;
    private String clientHost;
    private int clientPort;
    private Channel channel;
    //最后一次心跳时间
    private Long lastHeart;

    /**
     * 更新最后一次心跳时间
     */
    public void updateLastHeart(){
        lastHeart = new Date().getTime();
    }

    /**
     * 最后一次心跳时间距现在有多少秒
     * @return
     */
    public int getLastHeartToNowDelay(){
        return (int)(new Date().getTime() - lastHeart)/1000;
    }
}
