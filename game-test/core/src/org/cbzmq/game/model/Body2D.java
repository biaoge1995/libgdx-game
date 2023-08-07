package org.cbzmq.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import org.cbzmq.game.enums.CharacterType;
import org.cbzmq.game.stage.Engine2D;

/**
 * @ClassName Observe
 * @Description 多线程观察者
 * @Author chenbiao
 * @Date 2023/8/4 2:39 下午
 * @Version 1.0
 **/
public class Body2D implements Observer {
    //任务中心
    protected Engine2D engine2D;
    private CharacterType characterType;

    public String name;

    //地面时间
    public static float groundedTime = 0.15f;

    //地板上的 控制的x奔跑速度
    public static float runGroundX = 80, runAirSame = 45, runAirOpposite = 45;

    //TODO view会用到
    public Vector2 position = new Vector2();

    //目标位置向量
    public Vector2 targetPosition = new Vector2();

    //速度向量
    //TODO view会用到
    public Vector2 velocity = new Vector2();

    public Body2D() {
    }







    public CharacterType getCharacterType() {
        return characterType;
    }

    public void setCharacterType(CharacterType characterType) {
        this.characterType = characterType;
    }

    @Override
    public String toString() {
        return name ;
    }

    @Override
    public void onOneObserverEvent(Event.OneObserverEvent event) {
        Gdx.app.log(event.getEventType().toString(),event.getBody2D().toString());
    }

    @Override
    public void onTwoObserverEvent(Event.TwoObserverEvent event) {
        Gdx.app.log(event.getEventType().toString(),event.getA().toString()+"->"+event.getB().toString());
    }
}





