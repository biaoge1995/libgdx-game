package org.cbzmq.game.utils;

public class GameTimer {
    private float time;
    private final float initTimer;
    private int counter;
    private boolean isStart;
    private long current;

    public GameTimer(float initTimer) {
        this.initTimer = initTimer;
        this.time = initTimer;
    }

    public void start() {
        if (!isStart) {
            current = System.currentTimeMillis();
            isStart=true;
        };
    }

    synchronized public boolean update() {
        float delta = (System.currentTimeMillis() - current) / 1000f;
        if (update(delta)) {
            current = System.currentTimeMillis();
            return true;
        } else {
            return false;
        }
    }

    synchronized public boolean update(float delta) {
        if (time <= 0) {
            return false;
        } else {
            counter++;
            time -= delta;
            return true;
        }
    }

    public int getCounter() {
        return counter;
    }

    synchronized public void reset() {
        this.time = initTimer;
        this.isStart =false;
        this.counter=0;
    }
}
