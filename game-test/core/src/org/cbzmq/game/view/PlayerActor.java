/******************************************************************************
 * Spine Runtimes Software License
 * Version 2.1
 *
 * Copyright (c) 2013, Esoteric Software
 * All rights reserved.
 *
 * You are granted a perpetual, non-exclusive, non-sublicensable and
 * non-transferable license to install, execute and perform the Spine Runtimes
 * Software (the "Software") solely for internal use. Without the written
 * permission of Esoteric Software (typically granted by licensing Spine), you
 * may not (a) modify, translate, adapt or otherwise create derivative works,
 * improvements of the Software or develop new applications using the Software
 * or (b) remove, delete, alter or obscure any trademarks or any copyright,
 * trademark, patent or other intellectual property or proprietary rights notices
 * on or in the Software, including any copy thereof. Redistributions in binary
 * or source form must include this license and terms.
 *
 * THIS SOFTWARE IS PROVIDED BY ESOTERIC SOFTWARE "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL ESOTERIC SOFTARE BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *****************************************************************************/

package org.cbzmq.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.*;
import com.esotericsoftware.spine.AnimationState.AnimationStateAdapter;
import org.cbzmq.game.Assets;
import org.cbzmq.game.Assets.SoundEffect;
import org.cbzmq.game.Constants;
import org.cbzmq.game.StateAnimation;
import org.cbzmq.game.enums.CharacterState;
import org.cbzmq.game.model.Player;


/**
 * The view class for the player.
 */
public class PlayerActor extends BaseSkeletonActor<Player> {
    public Bone rearUpperArmBone, rearBracerBone, gunBone, headBone, torsoBone, frontUpperArmBone;
    public Animation shootAnimation, hitAnimation;
    public boolean canShoot;
    public float burstShots, burstTimer;
    public Vector2 temp1 = new Vector2(), temp2 = new Vector2();

    private boolean touched, jumpPressed, leftPressed, rightPressed;


    public PlayerActor(final Player player) {
        super(player);
    }

    @Override
    public void loadAssets(Assets assets) {
        super.loadAssets(assets);
        setSkeleton(new Skeleton(assets.playerSkeletonData));

        // We'll allow any of the bones or animations to be null in case someone has swapped out spineboy for a different skeleton.
        rearUpperArmBone = getSkeleton().findBone("rear_upper_arm");
        rearBracerBone = getSkeleton().findBone("rear_bracer");
        gunBone = getSkeleton().findBone("gun");
        headBone = getSkeleton().findBone("head");
        torsoBone = getSkeleton().findBone("torso");
        frontUpperArmBone = getSkeleton().findBone("front_upper_arm");
        shootAnimation = assets.playerSkeletonData.findAnimation("shoot");
        hitAnimation = assets.playerSkeletonData.findAnimation("hit");
        setAnimationState(new AnimationState(assets.playerAnimationData));


        getAnimationState().addListener(new AnimationStateAdapter() {
                                            public void event(AnimationState.TrackEntry entry, Event event) {
                                                String evenName = event.getData().getName();
                                                if (evenName.equals("footstep")) {
                                                    if (event.getInt() == 1)
                                                        SoundEffect.footstep1.play();
                                                    else
                                                        SoundEffect.footstep2.play();
                                                } else if (evenName.equals("hit")) {
                                                    SoundEffect.hurtPlayer.play();

                                                } else if (evenName.equals("jump")) {
                                                    //TODO
                                                } else if (evenName.equals("shoot")) {
                                                    SoundEffect.shoot.play();

                                                } else if (evenName.equals("death")) {
                                                    //TODO
                                                }
                                            }
                                        }
        );
    }

    @Override
    public void act(float delta) {

        // When not shooting, reset the number of burst shots.
        if (!touched && burstTimer > 0) {
            burstTimer -= delta;
            if (burstTimer < 0) burstShots = 0;
        }

        // If jump was pressed in the air, jump as soon as grounded.
        if (jumpPressed) jump();

        getSkeleton().setX(getModel().position.x + Player.width / 2);
        getSkeleton().setY(getModel().position.y);

        if (!setAnimation(getAssets().playerStates.get(getModel().state), getModel().stateChanged))
            getAnimationState().update(delta);
        getAnimationState().apply(getSkeleton());

        Vector2 mouse = temp1.set(Gdx.input.getX(), Gdx.input.getY());
        //将指定的屏幕坐标系转换为世界坐标系
        getViewport().unproject(mouse);

        // Determine if the player can shoot at the mouse position.
        //确定player是否可以开始射击
        canShoot = false;
        if (rearUpperArmBone == null || rearBracerBone == null || gunBone == null)
            canShoot = true;
        else if (getModel().hp > 0
//				&& !spineBoyStage.ui.hasSplash
                //骨骼距离鼠标的x和y轴距离要超过一定的数值
                && (Math.abs(getSkeleton().getY() - mouse.y) > 2.7f || Math.abs(getSkeleton().getX() - mouse.x) > 0.75f)) {
            // Store bone rotations from the animation that was applied.
            float rearUpperArmRotation = rearUpperArmBone.getRotation();
            float rearBracerRotation = rearBracerBone.getRotation();
            float gunRotation = gunBone.getRotation();
            // Straighten the arm and don't flipX, so the arm can more easily point at the mouse.
            rearUpperArmBone.setRotation(0);
            float shootRotation = 11;
            if (getAnimationState().getCurrent(1) == null) {
                rearBracerBone.setRotation(0);
                gunBone.setRotation(0);
            } else
                shootRotation += 25; // Use different rotation when shoot animation was applied.
            getSkeleton().setScaleX(1);
            getSkeleton().updateWorldTransform();

            // Compute the arm's angle to the mouse, flipping it based on the direction the player faces.
            Vector2 bonePosition = temp2.set(rearUpperArmBone.getWorldX(), rearUpperArmBone.getWorldY());
            float angle = bonePosition.sub(mouse).angle();
            float behind = (angle < 90 || angle > 270) ? -1 : 1;
            if (behind == -1) angle = -angle;
            if (getModel().state == CharacterState.idle || (touched && (getModel().state == CharacterState.jumping || getModel().state == CharacterState.falling)))
                getModel().dir = behind;
            if (behind != getModel().dir) angle = -angle;
            if (getModel().state != CharacterState.idle && behind != getModel().dir) {
                // Don't allow the player to shoot behind themselves unless idle. Use the rotations stored earlier from the animation.
                rearBracerBone.setRotation(rearBracerRotation);
                rearUpperArmBone.setRotation(rearUpperArmRotation);
                gunBone.setRotation(gunRotation);
            } else {
                if (behind == 1) angle += 180;
                // Adjust the angle upward based on the number of shots in the current burst.
                angle += Player.kickbackAngle * Math.min(1, burstShots / Player.kickbackShots) * (burstTimer / Player.burstDuration);
                float gunArmAngle = angle - shootRotation;
                // Compute the head, torso and front arm angles so the player looks up or down.
                float headAngle;
                if (getModel().dir == -1) {
                    angle += 360;
                    if (angle < 180)
                        headAngle = 25 * Interpolation.pow2In.apply(Math.min(1, angle / 50f));
                    else
                        headAngle = -15 * Interpolation.pow2In.apply(1 - Math.max(0, angle - 310) / 50f);
                } else {
                    if (angle < 360)
                        headAngle = -15 * Interpolation.pow2In.apply(1 - Math.max(0, (angle - 310) / 50f));
                    else
                        headAngle = 25 * Interpolation.pow2In.apply(1 - Math.max(0, (410 - angle) / 50f));
                }
                float torsoAngle = headAngle * 0.75f;
                if (headBone != null) headBone.setRotation(headBone.getRotation() + headAngle);
                if (torsoBone != null) torsoBone.setRotation(torsoBone.getRotation() + torsoAngle);
                if (frontUpperArmBone != null)
                    frontUpperArmBone.setRotation(frontUpperArmBone.getRotation() - headAngle * 1.4f);
                rearUpperArmBone.setRotation(gunArmAngle - torsoAngle - rearUpperArmBone.getWorldRotationX());
                canShoot = true;
            }
        }

        getSkeleton().setScaleX(getModel().dir);
        getSkeleton().updateWorldTransform();
//		super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        int i = (int) (getModel().collisionTimer / Player.flashTime * 1.5f) % 2;
        //在遭受碰撞时持续闪烁
        if (getModel().collisionTimer < 0 || i != 0)
            getRenderer().draw(batch, getSkeleton());
//			super.draw(batch, parentAlpha);
    }


    public void jump() {
        if (getModel().isGrounded()) {
            getModel().jump();
            StateAnimation stateAnimation = getAssets().playerStates.get(CharacterState.jumping);
            setAnimation(stateAnimation, true);
        }

    }

    public void shoot() {
        if (!canShoot || getModel().shootTimer >= 0) return;
        getModel().shootTimer = Player.shootDelay;
        burstTimer = Player.burstDuration;

        // Compute the position and velocity to spawn a new bullet.
        float x = 0, y = 0;
        if (rearUpperArmBone != null && rearBracerBone != null && gunBone != null) {
            x += rearUpperArmBone.getWorldX();
            y += rearUpperArmBone.getWorldY();
        } else {
            x += Player.width / 2;
            y += Player.height / 2;
        }
        float mouseX = Gdx.input.getX(), mouseY = Gdx.input.getY();
        //将屏幕坐标转换为世界坐标
        Vector2 unproject = getViewport().unproject(temp1.set(mouseX, mouseY));

        float angle = unproject.sub(x, y).angle();
        angle += Player.kickbackAngle * Math.min(1, burstShots / Player.kickbackShots) * getModel().dir;
        float variance = Player.kickbackVariance * Math.min(1, burstShots / Player.kickbackVarianceShots);
        angle += MathUtils.random(-variance, variance);

        float cos = MathUtils.cosDeg(angle), sin = MathUtils.sinDeg(angle);
        float vx = cos * Player.bulletSpeed + getModel().velocity.x * Player.bulletInheritVelocity;
        float vy = sin * Player.bulletSpeed + getModel().velocity.y * Player.bulletInheritVelocity;
        if (rearUpperArmBone != null && rearBracerBone != null && gunBone != null) {
            x = gunBone.getWorldX();
            y = gunBone.getWorldY() + Player.shootOffsetY * Constants.scale;
            x += cos * Player.shootOffsetX * Constants.scale;
            y += sin * Player.shootOffsetX * Constants.scale;
        }
        getModel().shoot(x, y, vx, vy);
        //开枪时的镜头抖动设置
        if (shootAnimation != null) getAnimationState().setAnimation(1, shootAnimation, false);
        //镜头抖动设置
        getCamera().shackCamera();

        getModel().velocity.x -= Player.kickback * getModel().dir;
//		SoundEffect.shoot.play();

        burstShots = Math.min(Player.kickbackShots, burstShots + 1);
    }

    /**
     * @param
     */
    public void beHit() {

        if (getModel().hp > 0 && hitAnimation != null) {
            AnimationState.TrackEntry entry = getAnimationState().setAnimation(1, hitAnimation, false);
            entry.setTrackEnd(hitAnimation.getDuration());
        }
    }


    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    public void setJumpPressed(boolean jumpPressed) {
        this.jumpPressed = jumpPressed;
    }

    public void setLeftPressed(boolean leftPressed) {
        this.leftPressed = leftPressed;
    }

    public void setRightPressed(boolean rightPressed) {
        this.rightPressed = rightPressed;
    }

    public boolean isTouched() {
        return touched;
    }

    public boolean isJumpPressed() {
        return jumpPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }
}
