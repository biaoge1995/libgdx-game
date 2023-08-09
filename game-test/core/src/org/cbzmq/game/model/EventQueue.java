package org.cbzmq.game.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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

    public Event.OneObserverEvent onOneObserverEvent(OneBodyEventType eventType, Character character) {
        Event.OneObserverEvent event = Event.OneObserverEvent.createEvent(eventType, character);
        observerEvents.add(event);
        return event;
    }


    public Event.TwoObserverEvent  onTwoObserverEvent(TwoBodyEventType eventType, Character character, Character other ) {
        Event.TwoObserverEvent event = Event.TwoObserverEvent.createEvent(eventType, character, other);
        observerEvents.add(event);
        return event;
    }


    public void born(Character character,float delta) {
        onOneObserverEvent(OneBodyEventType.born, character);
    }

    public void jump(Character character,float delta) {
        onOneObserverEvent(OneBodyEventType.jump, character);
    }
    public void moveRight(Character character,float delta) {
        Event.OneObserverEvent oneObserverEvent = onOneObserverEvent(OneBodyEventType.moveRight, character);
        oneObserverEvent.setFloatData(delta);
    }
    public void moveLeft(Character character,float delta) {
        onOneObserverEvent(OneBodyEventType.moveLeft, character).setFloatData(delta);
    }
    public void bloodUpdate(Character character,float hp) {
        onOneObserverEvent(OneBodyEventType.bloodUpdate, character).setFloatData(hp);
    }
    public void hit(Character character, Character hitCharacter) {
        onTwoObserverEvent(TwoBodyEventType.hit, character, hitCharacter);
    }

    public void beDeath(Character character) {
        onOneObserverEvent(OneBodyEventType.beDeath, character);
    }

    public void death(Character character, Character killer) {
        onTwoObserverEvent(TwoBodyEventType.beKilled, character, killer);
    }

    public void beRemove(Character character,float delta) {
        onOneObserverEvent(OneBodyEventType.beRemove, character);
    }

    public void collisionMap(Character character, Rectangle tile) {
        onOneObserverEvent(OneBodyEventType.collisionMap, character);
    }

    public void collisionCharacter(Character character, Character other) {
        onTwoObserverEvent(TwoBodyEventType.collisionCharacter, character,other);
    }

    public void attack(Character character) {
        onOneObserverEvent(OneBodyEventType.attack, character);
    }

    public void aimPoint(Character character, Vector2 aimPoint) {
        onOneObserverEvent(OneBodyEventType.aimPoint, character).setVector(aimPoint);
    }

    public void dispose(Character character) {
        onOneObserverEvent(OneBodyEventType.dispose, character);
    }

    public void lose(Character character) {
        onOneObserverEvent(OneBodyEventType.lose, character);
    }

    public void win(Character character) {
        onOneObserverEvent(OneBodyEventType.win, character);
    }

    public void frameEnd(Group root,float delta) {
        onOneObserverEvent(OneBodyEventType.frameEnd, root).setFloatData(delta);
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


