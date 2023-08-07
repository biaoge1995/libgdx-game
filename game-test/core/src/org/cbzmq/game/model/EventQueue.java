package org.cbzmq.game.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class EventQueue {
    private final Array<Object> observerEvents = new Array<>();
    boolean drainDisabled = false;

//    Array<? extends Engine2DObserver> engine2DObservers;
//
//    Array<? extends GameLogicObserver> gameLogicObservers;

    Array<? extends Observer> observers;

    public EventQueue() {

    }

    public void setObservers(Array<? extends Observer> observers) {
        this.observers = observers;
    }

//    public void setEngine2DObservers(Array<? extends Engine2DObserver> engine2DObservers) {
//        this.engine2DObservers = engine2DObservers;
//
//    }
//
//    public void setGameLogicObservers(Array<? extends GameLogicObserver> gameLogicObservers) {
//        this.gameLogicObservers = gameLogicObservers;
//    }

    public void onOneObserverEvent(Event.OneBodyEventType eventType, Body2D body2D) {
        Event.OneObserverEvent event = Event.OneObserverEvent.createEvent(eventType, body2D);
        observerEvents.add(event);
    }


    public void onTwoObserverEvent(Event.TwoBodyEventType eventType, Body2D body2D, Body2D other) {
        Event.TwoObserverEvent event = Event.TwoObserverEvent.createEvent(eventType, body2D, other);
        observerEvents.add(event);
    }


    public void born(Body2D body2D) {
        onOneObserverEvent(Event.OneBodyEventType.born, body2D);
    }

    public void hit(Body2D character, Body2D hitCharacter) {
        onTwoObserverEvent(Event.TwoBodyEventType.hit, character, hitCharacter);
    }

    public void death(Body2D character) {
        onOneObserverEvent(Event.OneBodyEventType.death, character);
    }

    public void death(Body2D character, Body2D killer) {
        onTwoObserverEvent(Event.TwoBodyEventType.beKilled, character, killer);
    }

    public void beRemove(Body2D body2D) {
        onOneObserverEvent(Event.OneBodyEventType.beRemove, body2D);
    }

    public void collisionMap(Body2D character, Rectangle tile) {
        onOneObserverEvent(Event.OneBodyEventType.collisionMap, character);
    }

    public void collisionCharacter(Body2D character, Body2D other) {
        onTwoObserverEvent(Event.TwoBodyEventType.collisionCharacter, character, other);
    }

    public void attack(Body2D character) {
        onOneObserverEvent(Event.OneBodyEventType.attack, character);
    }

    public void dispose(Body2D character) {
        onOneObserverEvent(Event.OneBodyEventType.dispose, character);
    }

    public void lose(Body2D character) {
        onOneObserverEvent(Event.OneBodyEventType.lose, character);
    }

    public void win(Body2D character) {
        onOneObserverEvent(Event.OneBodyEventType.win, character);
    }

    public void frameEnd(Group root) {
        onOneObserverEvent(Event.OneBodyEventType.frameEnd, root);
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

//        for (ObjectsAndEventType objectAndEvent : objects) {
//            Event.EventType enemyType = objectAndEvent.eventType;
//            Object obj = objectAndEvent.objects.get(0);
//            Body2D body2D = null;
//            Character character = null;
//            if (obj instanceof Character) {
//                character = (Character) obj;
//            } else if (obj instanceof Body2D) {
//                body2D = (Body2D) obj;
//            }
//            switch (enemyType) {
//                case born:
//                    for (int ii = 0; ii < gameLogicObservers.size; ii++)
//                        gameLogicObservers.get(ii).born(character);
//                    break;
//                case death:
//                    for (int ii = 0; ii < gameLogicObservers.size; ii++)
//                        gameLogicObservers.get(ii).death(character, (Character) objectAndEvent.objects.get(1));
//                    break;
//                case hit:
//                    Character hitCharacter = (Character) objectAndEvent.objects.get(1);
//                    for (int ii = 0; ii < gameLogicObservers.size; ii++)
//                        gameLogicObservers.get(ii).hit(character, hitCharacter);
//                case dispose:
//                    for (int ii = 0; ii < gameLogicObservers.size; ii++)
//                        gameLogicObservers.get(ii).dispose(character);
//                    break;
//                case beRemove:
//                    for (int ii = 0; ii < gameLogicObservers.size; ii++)
//                        gameLogicObservers.get(ii).beRemove(character);
//                    break;
//                case attack:
//                    for (int ii = 0; ii < gameLogicObservers.size; ii++)
//                        gameLogicObservers.get(ii).attack(character);
//                    break;
//                case frameEnd:
//                    float delta = (float) objectAndEvent.objects.get(1);
//                    Array<Character> all = new Array<>();
//                    all.clear();
//                    ((Group) obj).flat(all);
//                    for (int ii = 0; ii < gameLogicObservers.size; ii++)
//                        gameLogicObservers.get(ii).frameEnd(all, delta);
//                    break;
//                case collisionCharacter:
//                    Body2D other = (Body2D) objectAndEvent.objects.get(1);
//                    for (int ii = 0; ii < engine2DObservers.size; ii++)
//                        engine2DObservers.get(ii).collisionObserver(body2D, other);
//                    break;
//                case collisionMap:
//                    Rectangle tile = (Rectangle) objectAndEvent.objects.get(1);
//                    for (int ii = 0; ii < engine2DObservers.size; ii++)
//                        engine2DObservers.get(ii).collisionMap(body2D, tile);
//                    break;
//                case event:
//                    Event.OneObserverEvent event = (Event.OneObserverEvent) objectAndEvent.objects.get(1);
//                    for (int ii = 0; ii < engine2DObservers.size; ii++)
//                        engine2DObservers.get(ii).event(body2D, event);
//                    break;
//
//            }
//        }
        clear();

        drainDisabled = false;
    }

    public void clear() {
        observerEvents.clear();
    }



}


