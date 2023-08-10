package org.cbzmq.game.model;

import com.badlogic.gdx.utils.Array;

public class EventQueue {
    private final Array<Object> observerEvents = new Array<>();
    private boolean drainDisabled = false;


    private Array<? extends Observer> observers;

    public EventQueue() {

    }



    public void setObservers(Array<? extends Observer> observers) {
        this.observers = observers;
    }

    public void pushCharacterEvent(Event.OneCharacterEvent event) {
        observerEvents.add(event);
    }

    public void pushTwoCharacterEvent(Event.TwoObserverEvent event) {
        observerEvents.add(event);
    }


//    public void born(Character character) {
//        Event.OneObserverEvent event = Event.OneObserverEvent.createEvent(eventType, character);
//        pushCharacterEvent(OneBodyEventType.born, character);
//    }
//
//    public void jump(Character character) {
//        pushCharacterEvent(OneBodyEventType.jump, character);
//    }
//    public void moveRight(Character character,float delta) {
//        Event.OneObserverEvent oneObserverEvent = pushCharacterEvent(OneBodyEventType.moveRight, character);
//        oneObserverEvent.setFloatData(delta);
//    }
//    public void moveLeft(Character character,float delta) {
//        pushCharacterEvent(OneBodyEventType.moveLeft, character).setFloatData(delta);
//    }
//    public void bloodUpdate(Character character,float hp) {
//        pushCharacterEvent(OneBodyEventType.bloodUpdate, character).setFloatData(hp);
//    }
//    public void hit(Character character, Character hitCharacter) {
//        pushTwoCharacterEvent(TwoBodyEventType.hit, character, hitCharacter);
//    }
//
//    public void beDeath(Character character) {
//        pushCharacterEvent(OneBodyEventType.beDeath, character);
//    }
//
//    public void death(Character character, Character killer) {
//        pushTwoCharacterEvent(TwoBodyEventType.beKilled, character, killer);
//    }
//
//    public void beRemove(Character character) {
//        pushCharacterEvent(OneBodyEventType.beRemove, character);
//    }
//
//    public void collisionMap(Character character, Rectangle tile) {
//        pushCharacterEvent(OneBodyEventType.collisionMap, character);
//    }
//
//    public void collisionCharacter(Character character, Character other) {
//        pushTwoCharacterEvent(TwoBodyEventType.collisionCharacter, character,other);
//    }
//
//    public void attack(Character character) {
//        pushCharacterEvent(OneBodyEventType.attack, character);
//    }
//
//    public void aimPoint(Character character, Vector2 aimPoint) {
//        pushCharacterEvent(OneBodyEventType.aimPoint, character).setVector(aimPoint);
//    }
//
//    public void dispose(Character character) {
//        pushCharacterEvent(OneBodyEventType.dispose, character);
//    }
//
//    public void lose(Character character) {
//        pushCharacterEvent(OneBodyEventType.lose, character);
//    }
//
//    public void win(Character character) {
//        pushCharacterEvent(OneBodyEventType.win, character);
//    }
//
//    public void frameEnd(Group root,float delta) {
//        pushCharacterEvent(OneBodyEventType.frameEnd, root).setFloatData(delta);
//    }

    public void drain() {
//        if (this.engine2DObservers == null) return;
        if(observers==null) return;
        if(observerEvents.size==0) return;
        if (drainDisabled) return; // Not reentrant.
        drainDisabled = true;



        for (int i = 0; i < observerEvents.size; i++) {
            Object obj = observerEvents.get(i);
            if(obj instanceof Event.OneCharacterEvent){
                for (Observer observer : observers) {
                    observer.onOneObserverEvent((Event.OneCharacterEvent)obj );
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


