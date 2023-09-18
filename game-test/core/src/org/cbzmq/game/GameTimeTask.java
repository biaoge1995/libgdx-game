package org.cbzmq.game;

import org.cbzmq.game.logic.GameLogicEngine;
import org.cbzmq.game.model.Player;
import org.cbzmq.game.net.GameOne;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimeTask {
   static class TimerRecord{
       long period;
       long lastUpdateTime;
   }
    public static void main(String[] args) {
        GameLogicEngine me = GameLogicEngine.me();
        Timer timer = new Timer("");
        GameOne gameOne = new GameOne();
        gameOne.start();
        me.restart();
        me.join(new Player());
        TimerRecord timerRecord = new TimerRecord();
        long period = 8;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                timerRecord.period = System.currentTimeMillis() - timerRecord.lastUpdateTime;
                me.update(period/1000f);
                long end = System.currentTimeMillis();
                timerRecord.lastUpdateTime = end;
                System.out.println("update"+(end - start));
                System.out.println("period"+timerRecord.period);


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
