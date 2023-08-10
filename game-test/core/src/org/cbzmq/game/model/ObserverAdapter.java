package org.cbzmq.game.model;

/**
 * @ClassName CharacterAdapter
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/27 4:30 下午
 * @Version 1.0
 **/
public class ObserverAdapter implements Observer {


    @Override
    public boolean onOneObserverEvent(Event.OneCharacterEvent event) {
        return true;
    }

    @Override
    public boolean onTwoObserverEvent(Event.TwoObserverEvent event) {
        return true;
    }
}
