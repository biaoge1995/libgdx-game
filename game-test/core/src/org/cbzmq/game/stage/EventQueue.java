package org.cbzmq.game.stage;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Event;
import org.cbzmq.game.model.Character;
import org.cbzmq.game.model.CharacterListener;

public class EventQueue {
    private final Array<ObjectsAndEventType> objects = new Array<>();
    boolean drainDisabled=false;

    Array<CharacterListener> listeners;

    public EventQueue(Array<CharacterListener> listeners) {
        this.listeners = listeners;
    }

    public void born(Character character) {

        ObjectsAndEventType objectsAndEventType = new ObjectsAndEventType();
        objectsAndEventType.objects.add(character);
        objectsAndEventType.eventType = EventType.born;
        objects.add(objectsAndEventType);

    }

    public void hit(Character character,Character hitCharacter) {

        ObjectsAndEventType objectsAndEventType = new ObjectsAndEventType();
        objectsAndEventType.objects.add(character);
        objectsAndEventType.objects.add(hitCharacter);
        objectsAndEventType.eventType = EventType.hit;
        objects.add(objectsAndEventType);
    }

    public void death(Character character) {
        ObjectsAndEventType objectsAndEventType = new ObjectsAndEventType();
        objectsAndEventType.objects.add(character);
        objectsAndEventType.eventType = EventType.death;
        objects.add(objectsAndEventType);
    }

    public void beRemove(Character character) {
        ObjectsAndEventType objectsAndEventType = new ObjectsAndEventType();
        objectsAndEventType.objects.add(character);
        objectsAndEventType.eventType = EventType.beRemove;
        objects.add(objectsAndEventType);
    }

    public void collisionMap(Character character, Rectangle tile) {
        //TODO
        ObjectsAndEventType objectsAndEventType = new ObjectsAndEventType();
        objectsAndEventType.objects.add(character);
        objectsAndEventType.objects.add(tile);
        objectsAndEventType.eventType = EventType.collisionMap;
        objects.add(objectsAndEventType);
    }

    public void collisionCharacter(Character character, Character other) {
        //TODO
        ObjectsAndEventType objectsAndEventType = new ObjectsAndEventType();
        objectsAndEventType.objects.add(character);
        objectsAndEventType.objects.add(other);
        objectsAndEventType.eventType = EventType.collisionCharacter;
        objects.add(objectsAndEventType);
    }

    public void attack(Character character) {
        //TODO
        ObjectsAndEventType objectsAndEventType = new ObjectsAndEventType();
        objectsAndEventType.objects.add(character);
        objectsAndEventType.eventType = EventType.attack;
        objects.add(objectsAndEventType);
    }

    public void dispose(Character character) {
        //TODO
        ObjectsAndEventType objectsAndEventType = new ObjectsAndEventType();
        objectsAndEventType.objects.add(character);
        objectsAndEventType.eventType = EventType.dispose;
        objects.add(objectsAndEventType);
    }

    public void event(Character character, Event event) {
        ObjectsAndEventType objectsAndEventType = new ObjectsAndEventType();
        objectsAndEventType.objects.add(character);
        objectsAndEventType.objects.add(event);
        objectsAndEventType.eventType = EventType.event;
        objects.add(objectsAndEventType);
    }

    public void drain() {
        if (drainDisabled) return; // Not reentrant.
        drainDisabled = true;

        Array<ObjectsAndEventType> objects = this.objects;
        Array<CharacterListener> listeners = this.listeners;
        for (ObjectsAndEventType objectAndEvent : objects) {
            EventType type = objectAndEvent.eventType;
            Character character = (Character) objectAndEvent.objects.get(0);
            switch (type) {
                case born:
                    if (character.listener != null) character.listener.born(character);
                    for (int ii = 0; ii < listeners.size; ii++)
                        listeners.get(ii).born(character);
                    break;
                case death:
                    if (character.listener != null) character.listener.death(character);
                    for (int ii = 0; ii < listeners.size; ii++)
                        listeners.get(ii).death(character);
                    break;
                case hit:
                    Character hitCharacter = (Character) objectAndEvent.objects.get(1);
                    if (character.listener != null) character.listener.hit(character,hitCharacter);
                    for (int ii = 0; ii < listeners.size; ii++)
                        listeners.get(ii).hit(character,hitCharacter);
                    // Fall through.
                case dispose:
                    if (character.listener != null) character.listener.dispose(character);
                    for (int ii = 0; ii < listeners.size; ii++)
                        listeners.get(ii).dispose(character);
//						trackEntryPool.free(entry);
                    break;
                case beRemove:
                    if (character.listener != null) character.listener.beRemove(character);
                    for (int ii = 0; ii < listeners.size; ii++)
                        listeners.get(ii).beRemove(character);
                    break;
                case attack:
                    if (character.listener != null) character.listener.attack(character);
                    for (int ii = 0; ii < listeners.size; ii++)
                        listeners.get(ii).attack(character);
                    break;
                case collisionCharacter:
                    Character other = (Character) objectAndEvent.objects.get(1);
                    if (character.listener != null) character.listener.collisionCharacter(character, other);
                    for (int ii = 0; ii < listeners.size; ii++)
                        listeners.get(ii).collisionCharacter(character, other);
                    break;
                case collisionMap:
                    Rectangle tile = (Rectangle) objectAndEvent.objects.get(1);
                    if (character.listener != null) character.listener.collisionMap(character, tile);
                    for (int ii = 0; ii < listeners.size; ii++)
                        listeners.get(ii).collisionMap(character, tile);
                    break;
                case event:
                    Event event = (Event) objectAndEvent.objects.get(1);
                    if (character.listener != null) character.listener.event(character, event);
                    for (int ii = 0; ii < listeners.size; ii++)
                        listeners.get(ii).event(character, event);
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
        born, hit, death, dispose, beRemove, attack, collisionCharacter, collisionMap, event
    }

    public static class ObjectsAndEventType {
        Array<Object> objects = new Array<>();
        EventType eventType;


    }
}


