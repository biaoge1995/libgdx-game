package org.cbzmq.game.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import org.cbzmq.game.enums.OneBodyEventType;
import org.cbzmq.game.enums.TwoBodyEventType;

public class EventQueue {
    private final Array<Object> observerEvents = new Array<>();
    private boolean drainDisabled = false;


    private Array<? extends Observer> observers;

    public EventQueue() {

    }

    public void setObservers(Array<? extends Observer> observers) {
        this.observers = observers;
    }

    public void onOneObserverEvent(OneBodyEventType eventType, Character character,float delta) {
        Event.OneObserverEvent event = Event.OneObserverEvent.createEvent(eventType, character,delta);
        observerEvents.add(event);
    }


    public void onTwoObserverEvent(TwoBodyEventType eventType, Character character, Character other,float delta) {
        Event.TwoObserverEvent event = Event.TwoObserverEvent.createEvent(eventType, character, other,delta);
        observerEvents.add(event);
    }


    public void born(Character character,float delta) {
        onOneObserverEvent(OneBodyEventType.born, character,delta);
    }

    public void jump(Character character,float delta) {
        onOneObserverEvent(OneBodyEventType.jump, character,delta);
    }
    public void moveRight(Character character,float delta) {
        onOneObserverEvent(OneBodyEventType.moveRight, character,delta);
    }
    public void moveLeft(Character character,float delta) {
        onOneObserverEvent(OneBodyEventType.moveLeft, character,delta);
    }
    public void bloodUpdate(Character character,float delta) {
        onOneObserverEvent(OneBodyEventType.bloodUpdate, character,delta);
    }
    public void hit(Character character, Character hitCharacter,float delta) {
        onTwoObserverEvent(TwoBodyEventType.hit, character, hitCharacter,delta);
    }

    public void beDeath(Character character,float delta) {
        onOneObserverEvent(OneBodyEventType.beDeath, character,delta);
    }

    public void death(Character character, Character killer,float delta) {
        onTwoObserverEvent(TwoBodyEventType.beKilled, character, killer,delta);
    }

    public void beRemove(Character character,float delta) {
        onOneObserverEvent(OneBodyEventType.beRemove, character,delta);
    }

    public void collisionMap(Character character, Rectangle tile,float delta) {
        onOneObserverEvent(OneBodyEventType.collisionMap, character,delta);
    }

    public void collisionCharacter(Character character, Character other,float delta) {
        onTwoObserverEvent(TwoBodyEventType.collisionCharacter, character, other,delta);
    }

    public void attack(Character character,float delta) {
        onOneObserverEvent(OneBodyEventType.attack, character,delta);
    }

    public void dispose(Character character,float delta) {
        onOneObserverEvent(OneBodyEventType.dispose, character,delta);
    }

    public void lose(Character character,float delta) {
        onOneObserverEvent(OneBodyEventType.lose, character,delta);
    }

    public void win(Character character,float delta) {
        onOneObserverEvent(OneBodyEventType.win, character,delta);
    }

    public void frameEnd(Group root,float delta) {
        onOneObserverEvent(OneBodyEventType.frameEnd, root,delta);
    }

    public void drain() {
//        if (this.engine2DObservers == null) return;
        if(observers==null) return;
        if(observerEvents.size==0) return;
        if (drainDisabled) return; // Not reentrant.
        drainDisabled = true;



        for (int i = 0; i < observerEvents.size; i++) {
            Object obj = observerEvents.get(i);
            if(obj instanceof Event.OneObserverEvent){
                for (Observer observer : observers) {
                    observer.onOneObserverEvent((Event.OneObserverEvent)obj );
                }
            }else if(obj instanceof Event.TwoObserverEvent){
                for (Observer observer : observers) {
                    observer.onTwoObserverEvent((Event.TwoObserverEvent)obj );
                }
            }

        }

        clear();
        drainDisabled = false;
    }

    private void clear() {
        observerEvents.clear();
    }



}


