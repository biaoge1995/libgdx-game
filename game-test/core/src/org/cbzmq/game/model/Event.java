package org.cbzmq.game.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import org.cbzmq.game.enums.CharacterState;
import org.cbzmq.game.enums.OneBodyEventType;
import org.cbzmq.game.enums.TwoBodyEventType;
import org.cbzmq.game.proto.CharacterProto;
import org.cbzmq.game.proto.MsgProto;

import java.util.Date;

/**
 * @ClassName EventType
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/8/7 3:32 下午
 * @Version 1.0
 **/
public class Event {

    public static OneCharacterEvent stateUpdate(String sourceName, Character character, CharacterState state) {
        OneCharacterEvent event = OneCharacterEvent.createEvent(sourceName, OneBodyEventType.stateUpdate, character);
        event.state = state;
        return event;
    }

    public static OneCharacterEvent born(String sourceName, Character character) {
        return OneCharacterEvent.createEvent(sourceName, OneBodyEventType.born, character);
    }

    public static OneCharacterEvent jump(String sourceName, Character character) {
        return OneCharacterEvent.createEvent(sourceName, OneBodyEventType.jump, character);
    }

    public static OneCharacterEvent jumpDamping(String sourceName, Character character) {
        return OneCharacterEvent.createEvent(sourceName, OneBodyEventType.jumpDamping, character);
    }

    public static OneCharacterEvent moveRight(String sourceName, Character character, float delta) {
        OneCharacterEvent event = OneCharacterEvent.createEvent(sourceName, OneBodyEventType.moveRight, character);
        event.setFloatData(delta);
        return event;
    }

    public static OneCharacterEvent moveLeft(String sourceName, Character character, float delta) {
        OneCharacterEvent event = OneCharacterEvent.createEvent(sourceName, OneBodyEventType.moveLeft, character);
        event.setFloatData(delta);
        return event;
    }

    public static OneCharacterEvent bloodUpdate(String sourceName, Character character, float hp) {
        OneCharacterEvent event = OneCharacterEvent.createEvent(sourceName, OneBodyEventType.bloodUpdate, character);
        event.setFloatData(hp);
        return event;
    }


    public static OneCharacterEvent beDeath(String sourceName, Character character) {
        return OneCharacterEvent.createEvent(sourceName, OneBodyEventType.jump, character);
    }

    public static Event.TwoObserverEvent bekill(String sourceName, Character character, Character killer) {
        return Event.TwoObserverEvent.createEvent(sourceName, TwoBodyEventType.beKilled, character, killer);
    }

    public static OneCharacterEvent beRemove(String sourceName, Character character) {
        return OneCharacterEvent.createEvent(sourceName, OneBodyEventType.beRemove, character);
    }

    public static OneCharacterEvent collisionMap(String sourceName, Character character, Rectangle tile) {
        return OneCharacterEvent.createEvent(sourceName, OneBodyEventType.collisionMap, character);
    }

    public static OneCharacterEvent attack(String sourceName, Character character) {
        return OneCharacterEvent.createEvent(sourceName, OneBodyEventType.attack, character);
    }

    public static OneCharacterEvent aimPoint(String sourceName, Character character, Vector2 aimPoint) {
        OneCharacterEvent event = OneCharacterEvent.createEvent(sourceName, OneBodyEventType.aimPoint, character);
        event.setVector(aimPoint);
        return event;
    }

    public static OneCharacterEvent dispose(String sourceName, Character character) {
        return OneCharacterEvent.createEvent(sourceName, OneBodyEventType.dispose, character);
    }

    public static OneCharacterEvent lose(String sourceName, Character character) {
        return OneCharacterEvent.createEvent(sourceName, OneBodyEventType.lose, character);
    }

    public static OneCharacterEvent win(String sourceName, Character character) {
        return OneCharacterEvent.createEvent(sourceName, OneBodyEventType.win, character);
    }

    public static OneCharacterEvent frameEnd(String sourceName, Group root, float delta) {
        OneCharacterEvent event = OneCharacterEvent.createEvent(sourceName, OneBodyEventType.frameEnd, root);
        event.setFloatData(delta);
        return event;
    }

    public static Event.TwoObserverEvent hit(String sourceName, Character character, Character hitCharacter) {
        return Event.TwoObserverEvent.createEvent(sourceName, TwoBodyEventType.hit, character, hitCharacter);
    }

    public static Event.TwoObserverEvent collisionCharacter(String sourceName, Character character, Character other) {
        return Event.TwoObserverEvent.createEvent(sourceName, TwoBodyEventType.collisionCharacter, character, other);
    }


    public static class OneCharacterEvent {
        //
        private final String sourceName;
        private final long timeStamp;
        private final OneBodyEventType eventType;
        private final Character character;
        private float floatData = -1;
        private Vector2 vector;
        private CharacterState state;


        public static OneCharacterEvent createEvent(String sourceName, OneBodyEventType eventType, Character character) {
            return new OneCharacterEvent(sourceName, new Date().getTime(), eventType, character);
        }

        public OneCharacterEvent(String sourceName, long timeStamp, OneBodyEventType eventType, Character character) {
            this.timeStamp = timeStamp;
            this.eventType = eventType;
            this.character = character;
            this.sourceName = sourceName;
        }

        public MsgProto.Event toMsgProtoEvent() {
            MsgProto.Event.Builder builder = MsgProto.Event.newBuilder();
            builder.setOne(character.toCharacterProto().build());
            builder.setOneBodyEvent(eventType);
            if (floatData != -1) {
                builder.setFloatData(floatData);
            }
            if(vector!=null){
                CharacterProto.Vector2.Builder builder1 = CharacterProto.Vector2.newBuilder();
                builder1.setX(vector.x);
                builder1.setY(vector.y);
                builder.setVector(builder1);
            }
            if(state!=null){
                builder.setState(state);
            }
            return builder.build();

        }


        public void setFloatData(float floatData) {
            this.floatData = floatData;
        }

        public void setVector(Vector2 vector) {
            this.vector = vector;
        }

        public Vector2 getVector() {
            return vector;
        }

        public CharacterState getState() {
            return state;
        }

        public void setState(CharacterState state) {
            this.state = state;
        }

        public long getTimeStamp() {
            return timeStamp;
        }

        public OneBodyEventType getEventType() {
            return eventType;
        }

        public float getFloatData() {
            return floatData;
        }

        public Character getCharacter() {
            return character;
        }
    }

    public static class TwoObserverEvent {
        private final String sourceName;
        private final long timeStamp;
        private final TwoBodyEventType eventType;
        private final Character a;
        private final Character b;
        private float delta;

        public static TwoObserverEvent createEvent(String sourceName, TwoBodyEventType eventType, Character a, Character b) {
            return new TwoObserverEvent(sourceName, new Date().getTime(), eventType, a, b);
        }

        private TwoObserverEvent(String sourceName, long timeStamp, TwoBodyEventType eventType, Character a, Character b) {
            this.timeStamp = timeStamp;
            this.eventType = eventType;
            this.sourceName = sourceName;
            this.a = a;
            this.b = b;
        }

        public void setDelta(float delta) {
            this.delta = delta;
        }

        public long getTimeStamp() {
            return timeStamp;
        }

        public TwoBodyEventType getEventType() {
            return eventType;
        }

        public Character getA() {
            return a;
        }

        public Character getB() {
            return b;
        }

        public float getDelta() {
            return delta;
        }
    }
}
