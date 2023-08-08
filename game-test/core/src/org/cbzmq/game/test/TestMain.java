package org.cbzmq.game.test;

import com.google.protobuf.InvalidProtocolBufferException;
import org.cbzmq.game.enums.MsgHeader;
import org.cbzmq.game.proto.CharacterProto;
import org.cbzmq.game.proto.MsgProto;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

/**
 * @ClassName TestMain
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/8/3 9:57 上午
 * @Version 1.0
 **/
public class TestMain {

    static class Father {
        int age;
        String name;

        public Father(int age, String name) {
            this.age = age;
            this.name = name;
        }

        void say() {
            System.out.println("father :i am" + name + ", and " + age + " year old");
        }
    }

    static class Son extends Father {
        String job;

        public Son(int age, String name) {
            super(age, name);
        }

        @Override
        void say() {
            System.out.println("son :i am" + name + ", and " + age + " year old");
        }
    }

    static class Lives {
        public void getName() {
            System.out.println("Lives");
        }
    }

    static class Fruit extends Lives {
        public void getName() {
            System.out.println("Fruit");
        }
    }


    static class Apple extends Fruit {
        @Override
        public void getName() {
            System.out.println("Apple");
        }
    }

    static class Container<T extends Lives> implements Collection<T> {
        Object[] data;
        int capacity = 100;
        int size;

        public Container() {
            data = new Object[capacity];
        }

        @Override
        public void add(T o) {
            if (size < data.length) {
                data[size++] = o;
            }
        }

        @Override
        public T get(int index) {
            if (index < data.length) {
                return (T)data[index];
            }
            return null;
        }

        @Override
        public void addAll(Collection<T> collection) {
            for (int i = 0; i < collection.getSize(); i++) {
                add(collection.get(i));
            }
        }

        public void addAll2(Collection<? extends Lives> collection) {
            for (int i = 0; i < collection.getSize(); i++) {
                Lives lives = collection.get(i);
                if (size < data.length) {
                    data[size++] = lives;
                }
            }
        }


        @Override
        public int getSize() {
            return data.length;
        }
    }

    interface Collection<T> {
        void add(T t);

        T get(int index);

        void addAll(Collection<T> collection);

        int getSize();
    }


    public static void main(String[] args) throws InvalidProtocolBufferException {
//        Container<Lives> livesContainer = new Container<>();
//        Container<Apple> livesContainer2 = new Container<>();
//        livesContainer.add(new Apple());
//        livesContainer.add(new Lives());
//        livesContainer2.add(new Apple());
//        livesContainer.addAll2(livesContainer2);
//        Lives lives = livesContainer.get(0);
//        lives.getName();
//
//        Apple apple = new Apple();
//        if(apple instanceof Lives){
//            System.out.println("Lives");
//        }
//        if(apple instanceof Apple){
//            System.out.println("Apple");
//        }
        long time = new Date().getTime();
        LinkedList<Object> objects = new LinkedList<>();
        MsgProto.Msg.Builder builder = MsgProto.Msg.newBuilder();
        MsgProto.Msg msg = builder
                .setHeader(MsgHeader.SYNC_CHARACTERS_INFO)
                .addAllCharacterData(new ArrayList<CharacterProto.Character>())
                .setTimeStamp(time)
                .build();
        byte[] bytes = msg.toByteArray();
        MsgProto.Msg msg1 = MsgProto.Msg.parseFrom(bytes);
        System.out.println(time);
        System.out.println(msg1.getTimeStamp());


    }


}





