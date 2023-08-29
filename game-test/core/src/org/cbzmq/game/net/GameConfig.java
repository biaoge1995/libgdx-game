package org.cbzmq.game.net;

public interface GameConfig {
    /** 对外服务器 port */
    int externalPort = 10100;
    /** 对外服务器 ip */
    String externalIp = "127.0.0.1";
    /** http 升级 websocket 协议地址 */
    String websocketPath = "/websocket";
}
