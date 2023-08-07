package org.cbzmq.game.model;

/**
 * @ClassName Observer
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/8/7 10:10 下午
 * @Version 1.0
 **/
public interface Observer {
    void onOneObserverEvent(Event.OneObserverEvent event);
    void onTwoObserverEvent(Event.TwoObserverEvent event);
}
