package com.cbzmq.game;



import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3FileHandle;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Files;
import com.badlogic.gdx.math.Vector2;
import org.cbzmq.game.netty.UdpServer;
import org.cbzmq.game.stage.*;

import java.util.Timer;
import java.util.TimerTask;

/**
 * The controller class for the game. It knows about both the model and view and provides a way for the view to know about events
 * that occur in the model.
 */


public class GameServer2 {


//    public static void main(String[] args) throws InterruptedException {
//        Gdx.files = new Lwjgl3Files();
//        final LocalModel localModel = new LocalModel();
//        final UdpServer udpServer = new UdpServer(localModel);
//
//        Timer timer = new Timer();
//        final long[] delta = {0};
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                long start = System.currentTimeMillis();
//                try {
//                    localModel.update(delta[0]);
//                    udpServer.update();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                long end = System.currentTimeMillis();
//                delta[0] = end-start;
//            }
//        },1000,20);
//    }


}
