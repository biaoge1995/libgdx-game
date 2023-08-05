package org.cbzmq.game.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Event;

public class EventQueue {
    private final Array<ObjectsAndEventType> objects = new Array<>();
    boolean drainDisabled=false;

    Array<? extends Observer> listeners;

    public EventQueue(Array<? extends Observer> listeners) {
        this.listeners = listeners;
    }

    public void born(Observer character) {

        ObjectsAndEventType objectsAndEventType = new ObjectsAndEventType();
        objectsAndEventType.objects.add(character);
        objectsAndEventType.eventType = EventType.born;
        objects.add(objectsAndEventType);

    }

    public void hit(Observer character,Observer hitCharacter) {

        ObjectsAndEventType objectsAndEventType = new ObjectsAndEventType();
        objectsAndEventType.objects.add(character);
        objectsAndEventType.objects.add(hitCharacter);
        objectsAndEventType.eventType = EventType.hit;
        objects.add(objectsAndEventType);
    }

    public void death(Observer character,Observer killer) {
        ObjectsAndEventType objectsAndEventType = new ObjectsAndEventType();
        objectsAndEventType.objects.add(character);
        objectsAndEventType.objects.add(killer);
        objectsAndEventType.eventType = EventType.death;
        objects.add(objectsAndEventType);
    }

    public void beRemove(Observer character) {
        ObjectsAndEventType objectsAndEventType = new ObjectsAndEventType();
        objectsAndEventType.objects.add(character);
        objectsAndEventType.eventType = EventType.beRemove;
        objects.add(objectsAndEventType);
    }

    public void collisionMap(Observer character, Rectangle tile) {
        //TODO
        ObjectsAndEventType objectsAndEventType = new ObjectsAndEventType();
        objectsAndEventType.objects.add(character);
        objectsAndEventType.objects.add(tile);
        objectsAndEventType.eventType = EventType.collisionMap;
        objects.add(objectsAndEventType);
    }

    public void collisionCharacter(Observer character, Observer other) {
        //TODO
        ObjectsAndEventType objectsAndEventType = new ObjectsAndEventType();
        objectsAndEventType.objects.add(character);
        objectsAndEventType.objects.add(other);
        objectsAndEventType.eventType = EventType.collisionCharacter;
        objects.add(objectsAndEventType);
    }

    public void attack(Observer character) {
        //TODO
        ObjectsAndEventType objectsAndEventType = new ObjectsAndEventType();
        objectsAndEventType.objects.add(character);
        objectsAndEventType.eventType = EventType.attack;
        objects.add(objectsAndEventType);
    }

    public void dispose(Observer character) {
        //TODO
        ObjectsAndEventType objectsAndEventType = new ObjectsAndEventType();
        objectsAndEventType.objects.add(character);
        objectsAndEventType.eventType = EventType.dispose;
        objects.add(objectsAndEventType);
    }

    public void event(Observer character, Event event) {
        ObjectsAndEventType objectsAndEventType = new ObjectsAndEventType();
        objectsAndEventType.objects.add(character);
        objectsAndEventType.objects.add(event);
        objectsAndEventType.eventType = EventType.event;
        objects.add(objectsAndEventType);
    }

    public void frameEnd(Group root,float delta) {
        ObjectsAndEventType objectsAndEventType = new ObjectsAndEventType();
        objectsAndEventType.objects.add(root);
        objectsAndEventType.objects.add(delta);
        objectsAndEventType.eventType = EventType.frameEnd;
        objects.add(objectsAndEventType);
    }

    public void drain() {
        if(this.listeners==null)return;
        if (drainDisabled) return; // Not reentrant.
        drainDisabled = true;

        Array<ObjectsAndEventType> objects = this.objects;
        Array<? extends Observer> listeners = this.listeners;
        for (ObjectsAndEventType objectAndEvent : objects) {
            EventType enemyType = objectAndEvent.eventType;
            Object obj = objectAndEvent.objects.get(0);
            Observer character =null;
            if(obj instanceof Observer){
                character = (Observer) obj;
            }
            switch (enemyType) {
                case born:
//                    if (character.listener != null) character.listener.born(character);
                    for (int ii = 0; ii < listeners.size; ii++)
                        listeners.get(ii).born(character);
                    break;
                case death:
//                    if (character.listener != null) character.listener.death(character);
                    for (int ii = 0; ii < listeners.size; ii++)
                        listeners.get(ii).death(character,(Observer)objectAndEvent.objects.get(1));
                    break;
                case hit:
                    Observer hitCharacter = (Observer) objectAndEvent.objects.get(1);
//                    if (character.listener != null) character.listener.hit(character,hitCharacter);
                    for (int ii = 0; ii < listeners.size; ii++)
                        listeners.get(ii).hit(character,hitCharacter);
                    // Fall through.
                case dispose:
//                    if (character.listener != null) character.listener.dispose(character);
                    for (int ii = 0; ii < listeners.size; ii++)
                        listeners.get(ii).dispose(character);
//						trackEntryPool.free(entry);
                    break;
                case beRemove:
//                    if (character.listener != null) character.listener.beRemove(character);
                    for (int ii = 0; ii < listeners.size; ii++)
                        listeners.get(ii).beRemove(character);
                    break;
                case attack:
//                    if (character.listener != null) character.listener.attack(character);
                    for (int ii = 0; ii < listeners.size; ii++)
                        listeners.get(ii).attack(character);
                    break;
                case collisionCharacter:
                    Observer other = (Observer) objectAndEvent.objects.get(1);
//                    if (character.listener != null) character.listener.collisionCharacter(character, other);
                    for (int ii = 0; ii < listeners.size; ii++)
                        listeners.get(ii).collisionObserver(character, other);
                    break;
                case collisionMap:
                    Rectangle tile = (Rectangle) objectAndEvent.objects.get(1);
//                    if (character.listener != null) character.listener.collisionMap(character, tile);
                    for (int ii = 0; ii < listeners.size; ii++)
                        listeners.get(ii).collisionMap(character, tile);
                    break;
                case event:
                    Event event = (Event) objectAndEvent.objects.get(1);
//                    if (character.listener != null) character.listener.event(character, event);
                    for (int ii = 0; ii < listeners.size; ii++)
                        listeners.get(ii).event(character, event);
                    break;
                case frameEnd:
                    float delta = (float) objectAndEvent.objects.get(1);
//                    if (character.listener != null) character.listener.frameEnd((Group) obj,delta);
                    for (int ii = 0; ii < listeners.size; ii++)
//                        listeners.get(ii).frameEnd((Group)obj,delta);
                    break;
            }
        }
        clear();

        drainDisabled = false;
    }

    public void clear() {
        objects.clear();
    }

    public enum EventType {
        born, hit, death, dispose, beRemove, attack, collisionCharacter, collisionMap, event,frameEnd
    }

    public static class ObjectsAndEventType {
        Array<Object> objects = new Array<>();
        EventType eventType;


    }

    public Array<? extends Observer> getListeners() {
        return listeners;
    }
}


