package org.cbzmq.game.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.cbzmq.game.Assets;
import org.cbzmq.game.Constants;
import org.cbzmq.game.actor.BaseSkeletonActor;
import org.cbzmq.game.domain.Bullet;
import org.cbzmq.game.domain.Character;

public class BulletActor extends BaseSkeletonActor {
    static float bulletHitTime = 0.2f;
    Bullet bullet;
    float bulletWidth;
    float bulletHeight;
    TextureRegion bulletRegion;

    TextureRegion hitRegion;

    float time;

    public boolean isHitSound=false;

    public BulletActor(Assets assets, Bullet bullet) {
        super(assets, bullet);
        this.bullet = bullet;
        bulletRegion = assets.bulletRegion;
        hitRegion = assets.hitRegion;
        bulletWidth = bulletRegion.getRegionWidth() * Constants.scale;
        bulletHeight = bulletRegion.getRegionHeight() * Constants.scale / 2;
        time = bulletHitTime;
    }

    @Override
    public void act(float delta) {
        if(bullet.hp==0){
            time = time - delta;
            if(!isHitSound){
                Assets.SoundEffect.hit.play();
                isHitSound=true;
            }
            if (time < 0){
                remove();
            }
        }


    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

        float x = bullet.position.x, y = bullet.position.y;
        float angle = bullet.velocity.angleDeg();
        float vx = MathUtils.cosDeg(angle);
        float vy = MathUtils.sinDeg(angle);
        if(bullet.hp>0){

            // Adjust position so bullet region is drawn with the bullet position in the center of the fireball.
            x -= vx * bulletWidth * 0.65f;
            y -= vy * bulletWidth * 0.65f;
            x += vy * bulletHeight / 2;
            y += -vx * bulletHeight / 2;
            batch.draw(bulletRegion, x, y, 0, 0, bulletWidth, bulletHeight, 1, 1, angle);
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
