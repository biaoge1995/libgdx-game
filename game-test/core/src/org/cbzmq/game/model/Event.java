package org.cbzmq.game.model;

import org.cbzmq.game.enums.OneBodyEventType;
import org.cbzmq.game.enums.TwoBodyEventType;

import java.util.Date;

/**
 * @ClassName EventType
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/8/7 3:32 下午
 * @Version 1.0
 **/
public class Event {

    public static class OneObserverEvent {
        private final long timeStamp;
        private final OneBodyEventType eventType;
        private final Character character;
        private final float delta;


        public static OneObserverEvent createEvent(OneBodyEventType eventType, Character character,float delta) {
            return new OneObserverEvent(new Date().getTime(), eventType, character,delta);
        }

        private OneObserverEvent(long timeStamp, OneBodyEventType eventType, Character character,float delta) {
            this.timeStamp = timeStamp;
            this.eventType = eventType;
            this.character = character;
            this.delta = delta;
        }

        public long getTimeStamp() {
            return timeStamp;
        }
        public OneBodyEventType getEventType() {
            return eventType;
        }

        public float getDelta() {
            return delta;
        }

        public Character getCharacter() {
            return character;
        }
    }

    public static class TwoObserverEvent {
        private final long timeStamp;
        private final TwoBodyEventType eventType;
        private final Character a;
        private final Character b;
        private final float delta;

        public static TwoObserverEvent createEvent(TwoBodyEventType eventType, Character a, Character b,float delta) {
            return new TwoObserverEvent(new Date().getTime(), eventType, a, b, delta);
        }

        private TwoObserverEvent(long timeStamp, TwoBodyEventType eventType, Character a, Character b,float delta) {
            this.timeStamp = timeStamp;
            this.eventType = eventType;
            this.a = a;
            this.b = b;
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
