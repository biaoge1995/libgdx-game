package org.cbzmq.game.model;

import org.cbzmq.game.Map;
import org.cbzmq.game.view.BulletActor;

public class Bullet extends Character{


    public final static float width=1;
    public final static float height=1;
    public final static float distance = 25;

    public float damage=1;
    public float distanceCounter=25;
//    public BulletActor view;
    public Player player;




    public Bullet(Player player,Map map,float startX, float startY, float vx, float vy){
        super(map,"bullet");
        position.set(startX,startY);
        velocity.set(vx,vy);
        hp=1;
        rect.width = width;
        rect.height = height;
        this.player = player;

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
