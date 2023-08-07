package org.cbzmq.game.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import org.cbzmq.game.enums.CharacterType;

/**
 * @ClassName Observe
 * @Description 多线程观察者
 * @Author chenbiao
 * @Date 2023/8/4 2:39 下午
 * @Version 1.0
 **/
public class Body2D implements Observer {

    private CharacterType characterType;

    public String name;

    //地面时间
    public static float groundedTime = 0.15f;

    //地板上的 控制的x奔跑速度
    public static float runGroundX = 80, runAirSame = 45, runAirOpposite = 45;

    //空中的时间
    public float airTime;

    public float stateTime;

    //角色的矩阵
    //TODO view会用到
    public Rectangle rect = new Rectangle();

    //TODO view会用到
    public Vector2 position = new Vector2();

    //目标位置向量
    public Vector2 targetPosition = new Vector2();

    //速度向量
    //TODO view会用到
    public Vector2 velocity = new Vector2();

    public float collisionOffsetY;


    public float collisionTimer;

    public Body2D() {
    }

    public Object getParent(){
        return null;
    }

    public boolean isNeedCheckCollision(){
        return true;
    }



    public boolean isGrounded() {
        // The character is considered grounded for a short time after leaving the ground, making jumping over gaps easier.
        //角色离开地面后会被视为短暂停飞，从而更容易跳过空隙
        return airTime < groundedTime;
    }

    public void setGrounded(boolean grounded) {
        airTime = grounded ? 0 : groundedTime;
    }

    public void beCollide() {
    }

    public void collideMapX() {
    }

    public void collideMapY() {

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

    }

    @Override
    public void onTwoObserverEvent(Event.TwoObserverEvent event) {

    }
}





