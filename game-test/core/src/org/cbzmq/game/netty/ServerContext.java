package org.cbzmq.game.netty;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * @ClassName ServerContext
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/6/26 7:42 下午
 * @Version 1.0
 **/

public class ServerContext {
    private String host;
    private int port;
    private HashMap<String,ClientContext>  clientContextMap = new HashMap<>();

    /**
     *
     * @param address
     * @param context
     */
    public void addClient(String address,ClientContext context){
        clientContextMap.put(address,context);
    }

    /**
     *
     * @param address
     * @return
     */
    public ClientContext getClientContextByAddress(String address){
        return clientContextMap.get(address);
    }
}
