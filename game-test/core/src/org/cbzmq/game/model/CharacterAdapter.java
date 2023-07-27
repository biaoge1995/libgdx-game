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
public  class CharacterAdapter implements CharacterListener{
    @Override
    public void born(Character character) {
        Gdx.app.log("Character",character+"诞生");
    }

    @Override
    public void hit(Character character,Character hitCharacter) {
        Gdx.app.log("Character",character+"被攻击");

    }

    @Override
    public void death(Character character) {
        Gdx.app.log("Character",character+"死亡");
    }

    @Override
    public void beRemove(Character character) {
        Gdx.app.log("Character",character+"被移除");
    }

    @Override
    public void dispose(Character character) {
        Gdx.app.log("Character",character+"销毁");
    }

    @Override
    public void collisionMap(Character character, Rectangle tile) {
//        Gdx.app.log("Character","碰撞到了地图");
    }

    @Override
    public void collisionCharacter(Character character, Character other) {
        Gdx.app.log("Character",character+" 碰撞到了其他角色 "+other);
    }

    @Override
    public void attack(Character character) {
        Gdx.app.log("Character",character+"攻击");
    }

    @Override
    public void event(Character character, Event event) {
        Gdx.app.log("Character","event:"+event.getData().getName());
    }

}
