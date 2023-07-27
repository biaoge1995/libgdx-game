package org.cbzmq.game.domain;
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



import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import org.cbzmq.game.Map;
import org.cbzmq.game.actor.PlayerActor;
import org.cbzmq.game.Constants;


/** The model class for the player. */
public class Player extends Character {
	public static float heightSource = 625, width = 67 * Constants.scale, height = 285 * Constants.scale;
	public static float hpStart = 4, hpDuration = 15;
	public static float maxVelocityGroundX = 12.5f, maxVelocityAirX = 13.5f;
	public static float playerJumpVelocity = 22f, jumpDamping = 0.5f, jumpOffsetVelocity = 10, jumpOffsetY = 120 * Constants.scale;
	public static float airJumpTime = 0.1f;
	public static float shootDelay = 0.1f, shootOffsetX = 160, shootOffsetY = 11;
	public static float bulletSpeed = 34, bulletInheritVelocity = 0.4f, burstDuration = 0.18f;
	public static float kickbackShots = 33, kickbackAngle = 30, kickbackVarianceShots = 11, kickbackVariance = 6, kickback = 1.6f;
	public static float knockbackX = 14, knockbackY = 5, collisionDelay = 2.5f, flashTime = 0.07f;
	public static float headBounceX = 12, headBounceY = 20;
	//计时器
	//通过timer控制射击的时间间隔
	public float shootTimer;
	//控制碰撞时的无敌时间和闪烁
	public float collisionTimer;
	//控制回血时间
	public float hpTimer;

	Array<Bullet> bullets2 = new Array<>();

	FloatArray bullets = new FloatArray();

	// This is here for convenience, the model should never touch the view.
	public PlayerActor view;

	public Player (Map map) {
		super(map);
		rect.width = width;
		rect.height = height;
		hp = hpStart;
		jumpVelocity = playerJumpVelocity;
	}

	public void update (float delta) {

		stateChanged = false;
		shootTimer -= delta;
		//如果没有状态改变则一定时间后持续回血
		if (hp > 0) {
			hpTimer -= delta;
			if (hpTimer < 0) {
				hpTimer = hpDuration;
				if (hp < hpStart) hp++;
			}
		}

		collisionTimer -= delta;
		rect.height = height - collisionOffsetY;
		maxVelocityX = isGrounded() ? maxVelocityGroundX : maxVelocityAirX;
		super.update(delta);
	}

	public FloatArray getBullets() {
		return bullets;
	}

	public void setBullets(FloatArray bullets) {
		this.bullets = bullets;
	}

	public void clearBullets(){
		if(bullets!=null){
			bullets.clear();
		}
	}

	public void addBullet (float startX, float startY, float vx, float vy) {
//		bullets.add(vx);
//		bullets.add(vy);
//		bullets.add(startX);
//		bullets.add(startY);
//		bullets.add(0);
		bullets2.add(new Bullet(map,startX,startY,vx,vy));
	}

	public Array<Bullet> getBullets2() {
		return bullets2;
	}
}
