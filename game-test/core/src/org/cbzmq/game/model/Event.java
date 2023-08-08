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


        public static OneObserverEvent createEvent(OneBodyEventType eventType, Character character) {
            return new OneObserverEvent(new Date().getTime(), eventType, character);
        }

        private OneObserverEvent(long timeStamp, OneBodyEventType eventType, Character character) {
            this.timeStamp = timeStamp;
            this.eventType = eventType;
            this.character = character;
        }

        public long getTimeStamp() {
            return timeStamp;
        }

        public OneBodyEventType getEventType() {
            return eventType;
        }


        public Character getCharacter() {
            return character;
        }
    }

    public static class TwoObserverEvent {
        private long timeStamp;
        private TwoBodyEventType eventType;
        private Character a;
        private Character b;

        public static TwoObserverEvent createEvent(TwoBodyEventType eventType, Character a, Character b) {
            return new TwoObserverEvent(new Date().getTime(), eventType, a, b);
        }

        private TwoObserverEvent(long timeStamp, TwoBodyEventType eventType, Character a, Character b) {
            this.timeStamp = timeStamp;
            this.eventType = eventType;
            this.a = a;
            this.b = b;
        }

        public long getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(long timeStamp) {
            this.timeStamp = timeStamp;
        }

        public TwoBodyEventType getEventType() {
            return eventType;
        }

        public void setEventType(TwoBodyEventType eventType) {
            this.eventType = eventType;
        }

        public Character getA() {
            return a;
        }

        public void setA(Character a) {
            this.a = a;
        }

        public Character getB() {
            return b;
        }

        public void setB(Character b) {
            this.b = b;
        }
    }
}
