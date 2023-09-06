package org.cbzmq.game;

import org.cbzmq.game.logic.GameLogicEngine;
import org.cbzmq.game.model.Player;
import org.cbzmq.game.net.GameOne;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimeTask {
    public static void main(String[] args) {
        GameLogicEngine me = GameLogicEngine.me();
        Timer timer = new Timer("");
        GameOne gameOne = new GameOne();
        gameOne.start();
        me.restart();
        me.join(new Player());
        final long[] delays = {0};
        long period = 8;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
//                long start = System.currentTimeMillis();
//                long delay = delays[0];
                me.update(period/1000f);
//                long end = System.currentTimeMillis();
//                delays[0] = end - start;
            }
        },0,period);

//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                me.broadcast();
//            }
//        },0,period);



    }
}
