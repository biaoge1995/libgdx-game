package com.mygdx.game;


public class Test {

    public static Father father;

    public static void main(String[] args) {
        BallTest ballTest = new BallTest();
        Test.father.print();
    }
}


class Father {
    private int a;
    private int b;

    public void print(){
        System.out.println("father");
    }
}

class Son extends Father{

    public void print(){
        System.out.println("son");
    }
}

class BallTest {
    private Father father;
    public BallTest() {
        this.father = new Son();
        Test.father = father;
    }
}