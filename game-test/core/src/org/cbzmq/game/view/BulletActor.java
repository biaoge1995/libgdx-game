package org.cbzmq.game.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import org.cbzmq.game.Assets;
import org.cbzmq.game.Constants;
import org.cbzmq.game.model.Bullet;

public class BulletActor extends BaseSkeletonActor<Bullet> {
    static float bulletHitTime = 0.2f;
    float bulletWidth;
    float bulletHeight;
    TextureRegion bulletRegion;

    TextureRegion hitRegion;

    float time;

    public boolean isHitSound=false;

    public BulletActor(Assets assets, Bullet bullet) {
        super(assets, bullet);
        bulletRegion = assets.bulletRegion;
        hitRegion = assets.hitRegion;
        bulletWidth = bulletRegion.getRegionWidth() * Constants.scale;
        bulletHeight = bulletRegion.getRegionHeight() * Constants.scale / 2;
        time = bulletHitTime;
    }



    @Override
    public void act(float delta) {
        if(model.hp==0){
            time = time - delta;
            if(!isHitSound){
                Assets.SoundEffect.hit.play();
                isHitSound=true;
            }
            if (time < 0){
                remove();
            }
        }

//        super.act(delta);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

        float x = model.position.x, y = model.position.y;
        float angle = model.velocity.angleDeg();
        float vx = MathUtils.cosDeg(angle);
        float vy = MathUtils.sinDeg(angle);
        if(model.hp>0){

            // Adjust position so bullet region is drawn with the bullet position in the center of the fireball.
            x -= vx * bulletWidth * 0.65f;
            y -= vy * bulletWidth * 0.65f;
            x += vy * bulletHeight / 2;
            y += -vx * bulletHeight / 2;
            batch.draw(bulletRegion, x, y, 0, 0, bulletWidth, bulletHeight, 1, 1, angle);
//            System.out.println("actor"+ model +":"+x+","+y);
        }else {
//            Vector2 temp = new Vector2();
//            Vector2 offset = temp.set(vx, vy).nor().scl(15 * Constants.scale);
//            x = x + offset.x;
//            y = y + offset.y;
//            angle = temp.angle() + 90;

            Color color = batch.getColor().set(1, 1, 1, 1);
            float hitWidth = hitRegion.getRegionWidth() * Constants.scale;
            float hitHeight = hitRegion.getRegionWidth() * Constants.scale;


                color.a = time / bulletHitTime;
                batch.setColor(color);

                // Adjust position so bullet region is drawn with the bullet position in the center of the fireball.
                x += vy * bulletWidth * 0.2f;
                y += -vx * bulletHeight * 0.2f;
                batch.draw(hitRegion, x - hitWidth / 2, y, hitWidth / 2, 0, hitWidth, hitHeight, 1, 1, 0 );
            }
        batch.setColor(Color.WHITE);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        }



}
