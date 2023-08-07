package org.cbzmq.game.model;

import com.badlogic.gdx.Gdx;

/**
 * @ClassName CharacterAdapter
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/27 4:30 下午
 * @Version 1.0
 **/
public  class CharacterAdapter implements Observer {


    @Override
    public void onOneObserverEvent(Event.OneObserverEvent event) {
//        switch (event.getEventType()) {
//            case frameEnd:
//            case collisionMap:
//                return;
//            default:
//                Gdx.app.log(event.getEventType().toString(),event.getBody2D().toString());
//        }
    }

    @Override
    public void onTwoObserverEvent(Event.TwoObserverEvent event) {
//        Gdx.app.log(event.getEventType().toString(),event.getA().toString()+"->"+event.getB().toString());
    }
}
