package org.cbzmq.game.model;

import org.cbzmq.game.stage.Engine2D;


/**
 * @ClassName World
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/8/4 2:42 下午
 * @Version 1.0
 **/
public class World {
    //一个任务里面有一堆的Observe
    final private Group<Character> root;
    //定义一个发生变化的东西(状态)
    private int state;

    final private Engine2D  engine2D;

    public World() {
        this.root = new Group<>();
        engine2D = new Engine2D();

    }

    public void update(float delta){
        engine2D.update(delta);
    }

    //附上到那Observer 也就是说添加那个Observer
    public void attach(Character character) {
        root.addCharacter(character);
    }

    public int getState() {
        return state;
    }


    public static void main(String[] args) {

    }

}
