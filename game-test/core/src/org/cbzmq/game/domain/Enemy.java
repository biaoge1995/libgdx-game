package org.cbzmq.game.domain;

import com.badlogic.gdx.math.MathUtils;
import org.cbzmq.game.CharacterState;
import org.cbzmq.game.actor.EnemyActor;
import org.cbzmq.game.Map;
import org.cbzmq.game.Constants;





/** The model class for an enemy. */
public class Enemy extends Character {
	public static float heightSource = 398, width = 105 * Constants.scale, height = 200 * Constants.scale;
	public static float maxVelocityMinX = 4f, maxVelocityMaxX = 8.5f, maxVelocityAirX = 19f;
	public static float hpWeak = 1, hpSmall = 2, hpNormal = 3, hpStrong = 5, hpBecomesBig = 8, hpBig = 20;
	public static float corpseTime = 5 * 60, fadeTime = 3;
	public static float jumpDistanceNormal = 20, jumpDelayNormal = 1.6f, jumpVelocityNormal = 12, jumpVelocityBig = 18;
	public static float sizeSmall = 0.5f, sizeBig = 2.5f, sizeStrong = 1.3f, bigDuration = 2, smallCount = 14;
	public static float normalKnockbackX = 19, normalKnockbackY = 9, bigKnockbackX = 12, bigKnockbackY = 6;
	public static float collisionDelay = 0.3f;
	public float deathTimer = corpseTime;
	public float maxVelocityGroundX;
	public float collisionTimer;
	public float jumpDelayTimer, jumpDistance, jumpDelay;
	public Type type;
	public float size = 1;
	public float bigTimer;
	public float spawnSmallsTimer;
	public boolean move;
	public boolean forceJump;
	public int collisions;
	public float knockbackX = normalKnockbackX, knockbackY = normalKnockbackY;

	// This is here for convenience, the model should never touch the view.
	public EnemyActor view;

	public Enemy (Map map, Type type) {
		super(map);
		this.type = type;

		rect.width = width;
		rect.height = height;

		maxVelocityGroundX = MathUtils.random(maxVelocityMinX, maxVelocityMaxX);
		maxVelocityX = maxVelocityGroundX;
		jumpVelocity = jumpVelocityNormal;
		jumpDelay = jumpDelayNormal;
		jumpDistance = jumpDistanceNormal;

		if (type == Type.big) {
			size = sizeBig;
			rect.width = width * size * 0.7f;
			rect.height = height * size * 0.7f;
			hp = hpBig;
			knockbackX = normalKnockbackX;
			knockbackY = normalKnockbackY;
		} else if (type == Type.small) {
			size = sizeSmall;
			rect.width = width * size;
			rect.height = height * size;
			hp = hpSmall;
		} else if (type == Type.weak)
			hp = hpWeak;
		else if (type == Type.becomesBig)
			hp = hpBecomesBig;
		else if (type == Type.strong) {
			hp = hpStrong;
			size = sizeStrong;
			jumpVelocity *= 1.5f;
			jumpDistance *= 1.4f;
		} else
			hp = hpNormal;

		jumpDelayTimer = MathUtils.random(0, jumpDelay);
	}

	public void update (float delta) {
		stateChanged = false;
		//死亡时
		if (state == CharacterState.death) {
			if (type == Type.becomesBig && size == 1) {
				bigTimer = bigDuration;
				collisionTimer = bigDuration;
				state = CharacterState.run;
				hp = hpBig;
				knockbackX = bigKnockbackX;
				knockbackY = bigKnockbackY;
				type = Type.big;
				jumpVelocity = jumpVelocityBig;
			} else if (type == Type.big) {
				spawnSmallsTimer = 0.8333f;
				type = Type.normal;
			}
		}

		// Enemy grows to a big enemy.
		//怪物变大尺寸跟变大时间挂钩
		if (bigTimer > 0) {
			bigTimer -= delta;
			size = 1 + (sizeBig - 1) * (1 - Math.max(0, bigTimer / bigDuration));
			rect.width = width * size * 0.7f;
			rect.height = height * size * 0.7f;
		}

		// Big enemy explodes into small ones.
		//大怪物产生小怪物
		if (spawnSmallsTimer > 0) {
			spawnSmallsTimer -= delta;
			if (spawnSmallsTimer < 0) {
				for (int i = 0; i < smallCount; i++) {
					Enemy small = new Enemy(this.map,Type.small);
					small.position.set(position.x, position.y + 2);
					small.velocity.x = MathUtils.random(5, 15) * (MathUtils.randomBoolean() ? 1 : -1);
					small.velocity.y = MathUtils.random(10, 25);
					small.setGrounded(false);
					addChild(small);
					//TODO 增加到游戏主逻辑中去
//					model.enemies.add(small);
				}
			}
		}

		// Nearly dead enemies jump at the player right away.
		//濒临死亡的敌人设置他的跳跃时间
		if (hp == 1 && type != Type.weak && type != Type.small) jumpDelayTimer = 0;

		// Kill enemies stuck in the map or those that have somehow fallen out of the map.
		//将hp小于0的怪物变成死亡的状态，以及那些卡在地图上的怪物
		if (state != CharacterState.death && (hp <= 0 || position.y < -100 || collisions > 100)) {
			state = CharacterState.death;
			hp = 0;
		}

		// Simple enemy AI.
		boolean grounded = isGrounded();
		if (grounded) move = true;
		collisionTimer -= delta;
		maxVelocityX = grounded ? maxVelocityGroundX : maxVelocityAirX;
		if (state == CharacterState.death)
			deathTimer -= delta;
		else if (collisionTimer < 0) {
			//TODO  胜利的条件
			//model.player.hp == 0
			if (isWin) {
				// Enemies win, jump for joy!
				//如果怪物赢了
				win();
			} else {
				//跳向目标方向
				if (grounded && (forceJump || Math.abs(targetPosition.x - position.x) < jumpDistance)) {
					jumpDelayTimer -= delta;
					//跳跃的定时器
					if (state != CharacterState.jump && jumpDelayTimer < 0 && position.y <= targetPosition.y) {
						jump();
						jumpDelayTimer = MathUtils.random(0, jumpDelay);
						forceJump = false;
					}
				}
				//朝着目标的方向移动
				if (move) {
					if (targetPosition.x > position.x) {
						if (velocity.x >= 0) moveRight(delta);
					} else if (velocity.x <= 0) //
						moveLeft(delta);
				}
			}
		}

		int previousCollision = collisions;
		super.update(delta);
		if (!grounded || collisions == previousCollision) collisions = 0;
	}

	public void win(){
		if (isGrounded() && velocity.x == 0) {
			jumpVelocity = jumpVelocityNormal / 2;
			dir = -dir;
			jump();
		}
	}

	public boolean collideX () {
		boolean result = super.collideX();
		if (result) {
			// If grounded and collided with the map, jump to avoid the obstacle.
			//如果接地并与地图相撞，跳跃以避开障碍物。
			if (isGrounded()) forceJump = true;
			collisions++;
		}
		return result;
	}

	public enum Type {
		weak, normal, strong, becomesBig, big, small
	}
}
