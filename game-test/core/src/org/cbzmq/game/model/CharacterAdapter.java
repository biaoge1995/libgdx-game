package org.cbzmq.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.esotericsoftware.spine.Event;

/**
 * @ClassName CharacterAdapter
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/27 4:30 下午
 * @Version 1.0
 **/
public  class CharacterAdapter extends Observer<Character> {
    @Override
    public void update(float delta) {

    }

    @Override
    public void born(Character character) {
        Gdx.app.log("Character",character+" born");
    }

    @Override
    public void hit(Character character,Character hitCharacter) {
        Gdx.app.log("Character",character+" be hit");

    }

    @Override
    public void death(Character character,Character killer) {
        Gdx.app.log("Character",character+" death");
    }

    @Override
    public void beRemove(Character character) {
        Gdx.app.log("Character",character+" be remove");
    }

    @Override
    public void dispose(Character character) {
        Gdx.app.log("Character",character+" be dispose");
    }

    @Override
    public void collisionMap(Character character, Rectangle tile) {
//        Gdx.app.log("Character","碰撞到了地图");
    }



    @Override
    public void collisionObserver(Character character, Character other) {
        Gdx.app.log("Character",character+" collision character "+other);
    }

    @Override
    public void attack(Character character) {
        Gdx.app.log("Character",character+" attack");
    }

    @Override
    public void event(Character character, Event event) {
        Gdx.app.log("Character","event :"+character+" "+event.getData().getName());
    }

    @Override
    public  void frameEnd(Character root,float time) {
//        Gdx.app.log("Group",root+" frame end "+time+"s"+" "+1/time+"fps");
    }

}
