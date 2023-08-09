package org.cbzmq.game.model;

import com.badlogic.gdx.math.Vector2;
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
        private float floatData;

        private Vector2 vector;


        public static OneObserverEvent createEvent(OneBodyEventType eventType, Character character ) {
            return new OneObserverEvent(new Date().getTime(), eventType, character);
        }

        private OneObserverEvent(long timeStamp, OneBodyEventType eventType, Character character) {
            this.timeStamp = timeStamp;
            this.eventType = eventType;
            this.character = character;
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
        private final long timeStamp;
        private final TwoBodyEventType eventType;
        private final Character a;
        private final Character b;
        private  float delta;

        public static TwoObserverEvent createEvent(TwoBodyEventType eventType, Character a, Character b ) {
            return new TwoObserverEvent(new Date().getTime(), eventType, a, b);
        }

        private TwoObserverEvent(long timeStamp, TwoBodyEventType eventType, Character a, Character b) {
            this.timeStamp = timeStamp;
            this.eventType = eventType;
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
