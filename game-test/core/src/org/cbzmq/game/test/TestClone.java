package org.cbzmq.game.test;

import com.badlogic.gdx.utils.Array;
import org.cbzmq.game.enums.CharacterState;
import org.cbzmq.game.model.Character;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName TestClone
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/8/8 4:51 下午
 * @Version 1.0
 **/
public class TestClone implements Cloneable {
    private int age;
    private String Name;
    private Date date = new Date();
    private CharacterState state=CharacterState.death;

    @Override
    public String toString() {
        return "TestClone{" +
                "age=" + age +
                ", Name='" + Name + '\'' +
                '}';
    }

    public TestClone(int age, String name) {
        this.age = age;
        Name = name;
//        for (int i = 0; i < 100; i++) {
//            characters.add(new Character("unkown"));
//        }
    }

    @Override
    protected TestClone clone() throws CloneNotSupportedException {
        TestClone clone = (TestClone) super.clone();
        clone.date.clone();
        return clone;

    }

    public static void main(String[] args) throws CloneNotSupportedException {
        TestClone testClone = new TestClone(10,"陈彪");
        CharacterState state = CharacterState.idle;
        CharacterState state2 = CharacterState.idle;

//        long l = System.currentTimeMillis();
//        for (int i = 0; i < 10000; i++) {
//            testClone.clone();
//        }
//        long l2 = System.currentTimeMillis();
//        System.out.println(l2-l);

        TestClone clone = testClone.clone();
        clone.state = CharacterState.idle;
        clone.Name = "shabi";
        System.out.println(testClone);
        System.out.println(clone);
    }
}
