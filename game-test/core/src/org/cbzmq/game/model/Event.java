package org.cbzmq.game.model;

import org.cbzmq.game.model.Body2D;

import java.util.Date;

/**
 * @ClassName EventType
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/8/7 3:32 下午
 * @Version 1.0
 **/
public class Event {
   public enum OneBodyEventType{
       //这些事件需要通知客户端
       born,
       death,
       attack,
       frameEnd,
       hit,
       dispose,
       beRemove,
       lose,
       win,
       //

       event,
       collisionCharacter,
       collisionMap,

   }

   public enum TwoBodyEventType{
       collisionCharacter,attack,hit,beKilled;
   }
    public static class OneObserverEvent {
        private final long timeStamp;
        private final OneBodyEventType eventType;
        private final Body2D body2D;


        public static OneObserverEvent createEvent(OneBodyEventType eventType, Body2D body2D) {
            return new OneObserverEvent(new Date().getTime(), eventType, body2D);
        }

        private OneObserverEvent(long timeStamp, OneBodyEventType eventType, Body2D body2D) {
            this.timeStamp = timeStamp;
            this.eventType = eventType;
            this.body2D = body2D;
        }

        public long getTimeStamp() {
            return timeStamp;
        }

        public OneBodyEventType getEventType() {
            return eventType;
        }


        public Body2D getBody2D() {
            return body2D;
        }
    }

    public static class TwoObserverEvent {
        private long timeStamp;
        private TwoBodyEventType eventType;
        private Body2D a;
        private Body2D b;

        public static TwoObserverEvent createEvent(TwoBodyEventType eventType, Body2D a, Body2D b) {
            return new TwoObserverEvent(new Date().getTime(), eventType, a, b);
        }

        private TwoObserverEvent(long timeStamp, TwoBodyEventType eventType, Body2D a, Body2D b) {
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

        public Body2D getA() {
            return a;
        }

        public void setA(Body2D a) {
            this.a = a;
        }

        public Body2D getB() {
            return b;
        }

        public void setB(Body2D b) {
            this.b = b;
        }
    }
}
