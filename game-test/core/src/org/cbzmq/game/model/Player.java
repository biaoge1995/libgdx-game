package org.cbzmq.game.model;
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



import org.cbzmq.game.logic.Constants;
import org.cbzmq.game.proto.CharacterState;
import org.cbzmq.game.proto.CharacterType;


/** The model class for the player. */
public class Player extends Character{
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

	//控制回血时间
	public float hpTimer;

	private final MyVector2 temp = new MyVector2();



	// This is here for convenience, the model should never touch the view.
	// 这是为了方便起见，模型永远不应该接触到视图。

	public Player () {
		super("spine boy");
		rect.width = width;
		rect.height = height;
		hp = hpStart;
		jumpVelocity = playerJumpVelocity;
		//控制碰撞时的无敌时间和闪烁
		collisionTimer=0.07f;
		damage=1;
		this.characterType = CharacterType.player;
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


	public void beCollide(){
		collisionTimer = Player.collisionDelay;
		if (hp > 0){
			state = CharacterState.falling;
		}else {
			setState(CharacterState.death);
			velocity.y *= 0.5f;
		}
	}

	public float attack(float gunX, float gunY, float burstShots) {
		temp.set(aimPoint);
		float angle = temp.sub(gunX, gunY).angle();
		angle += Player.kickbackAngle * Math.min(1, burstShots / Player.kickbackShots) *  dir;
		float variance = Player.kickbackVariance * Math.min(1, burstShots / Player.kickbackVarianceShots);
		angle += com.badlogic.gdx.math.MathUtils.random(-variance, variance);
		float cos = com.badlogic.gdx.math.MathUtils.cosDeg(angle),
				sin = com.badlogic.gdx.math.MathUtils.sinDeg(angle);
		float vx = cos * Player.bulletSpeed + velocity.x * Player.bulletInheritVelocity;
		float vy = sin * Player.bulletSpeed + velocity.y * Player.bulletInheritVelocity;

		gunY+= Player.shootOffsetY * Constants.scale;

		gunX += cos * Player.shootOffsetX * Constants.scale;
		gunY += sin * Player.shootOffsetX * Constants.scale;

		if(parent!=null) parent.addCharacter(new Bullet(this,gunX,gunY,vx,vy));
		//后坐力
		velocity.x -= Player.kickback * dir;
		return Math.min(Player.kickbackShots, burstShots + 1);
	}


//	public static Player parserProto(CharacterProto.Character proto) {
//		Player player = new Player();
//		Character father = Character.parserProto(proto);
//		Character.copyToSon(father,player);
//		player.shootTimer = proto.getShootTimer();
//		return player;
//	}




//	public  CharacterProto.Character.Builder toCharacterProto() {
//		CharacterProto.Character.Builder builder = super.toCharacterProto();
//
//		return builder.setType(CharacterType.player)
//				.setShootTimer(this.shootTimer)
////				.setHpTimer(player.hpTimer)
//				;
//	}








}
