package org.cbzmq.game.domain;

import org.cbzmq.game.Map;
import org.cbzmq.game.actor.BulletActor;

public class Bullet extends Character{
    public final static float damage=1;
    public final static float width=1;
    public final static float height=1;
    public final static float distance = 25;

    public float distanceCounter=25;
    public BulletActor view;




    public Bullet(Map map,float startX, float startY, float vx, float vy){
        super(map);
        position.set(startX,startY);
        velocity.set(vx,vy);
        hp=1;
        rect.width = width;
        rect.height = height;

    }

    @Override
    public void update(float delta) {

        if(hp>0) super.update(delta);
        else return;

//        distanceCounter+=velocity.x*delta;
//        if(distanceCounter>=25){
//            hp=0;
//        }
    }

    @Override
    public boolean collideX() {
        if(super.collideX()){
            hp = 0;
            return true;
        }
        return false;
    }

    @Override
    public boolean collideY() {
        if(super.collideY()){
            hp = 0;
            return true;
        }
        return false;
    }
}
