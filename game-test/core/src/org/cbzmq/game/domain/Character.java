package org.cbzmq.game.domain;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.cbzmq.game.CharacterState;
import org.cbzmq.game.Map;
import org.cbzmq.game.Constants;



/** The model class for an enemy or player that moves around the map. */
public class Character {


	public static float minVelocityX = 0.001f, maxVelocityY = 20f;
	//地面时间
	public static float groundedTime = 0.15f;
	//x方向 地板阻尼 空气阻尼 碰撞阻尼
	public static float dampingGroundX = 36, dampingAirX = 15, collideDampingX = 0.7f;
	//地板上的 控制的x奔跑速度
	public static float runGroundX = 80, runAirSame = 45, runAirOpposite = 45;
	//游戏主逻辑引用
	public Map map;

	//位置向量
	public Vector2 position = new Vector2();

	//目标位置向量
	public Vector2 targetPosition = new Vector2();

	//速度向量
	public Vector2 velocity = new Vector2();
	//默认的动画状态
	public CharacterState state = CharacterState.idle;
	//开始时间
	public float stateTime;
	//方向
	public float dir;
	//空中的时间
	public float airTime;
	//角色的矩阵
	public Rectangle rect = new Rectangle();
	//是否状态改变
	public boolean stateChanged;
	//雪条
	public float hp;
	//最大x方向上的位移
	public float maxVelocityX;
	//碰撞Y的偏移量
	public float collisionOffsetY;
	//跳的速度在Y上
	public float jumpVelocity;
	//
	public boolean isWin=false;


	public void setTargetPosition(Vector2 targetPosition) {
		this.targetPosition = targetPosition;
	}

	public Array<Character> childs = new Array<>();

	public Character(Map map) {
		this.map = map;
	}

	public Character explore(){
		if(childs.size>0){
			return childs.pop();
		}
		else {
			return null;
		}
	}

	public void addChild(Character child){
		childs.add(child);
	}

	public void win(){

	}

	public void setState (CharacterState newState) {
		if ((state == newState && state != CharacterState.fall) || state == CharacterState.death) return;
		state = newState;
		stateTime = 0;
		stateChanged = true;
	}

	public void update (float delta) {
		stateTime += delta;

		// If moving downward, change state to fall.
		//如果角色在往下移动则设置该角色的状态为fll
		if (velocity.y < 0 && state != CharacterState.jump && state != CharacterState.fall) {
			setState(CharacterState.fall);
			setGrounded(false);
		}

		// Apply gravity.
		//设置重力加速度
		velocity.y -= Constants.gravity * delta;
		//如果速度超过了最大值则给他赋予默认的最大速度
		if (velocity.y < 0 && -velocity.y > maxVelocityY) velocity.y = Math.signum(velocity.y) * maxVelocityY;

		boolean grounded = isGrounded();

		// Damping reduces velocity so the character eventually comes to a complete stop.
		//空中的阻尼和地板的阻尼
		float damping = (grounded ? dampingGroundX : dampingAirX) * delta;

		if (velocity.x > 0)
			velocity.x = Math.max(0, velocity.x - damping);
		else
			velocity.x = Math.min(0, velocity.x + damping);
		if (Math.abs(velocity.x) < minVelocityX && grounded) {
			velocity.x = 0;
			setState(CharacterState.idle);
		}
		//s=v * delta 该段时间内位移的距离
		velocity.scl(delta); // Change velocity from units/sec to units since last frame.
		collideX();
		collideY();
		position.add(velocity);
		velocity.scl(1 / delta); // Change velocity back.
	}

	public boolean isGrounded () {
		// The character is considered grounded for a short time after leaving the ground, making jumping over gaps easier.
		//角色离开地面后会被视为短暂停飞，从而更容易跳过空隙
		return airTime < groundedTime;
	}

	public void setGrounded (boolean grounded) {
		airTime = grounded ? 0 : groundedTime;
	}

	public boolean collideX () {
		rect.x = position.x + velocity.x;
		rect.y = position.y + collisionOffsetY;

		int x;
		if (velocity.x >= 0)
			x = (int)(rect.x + rect.width);
		else
			x = (int)rect.x;
		int startY = (int)rect.y;
		int endY = (int)(rect.y + rect.height);
		for (Rectangle tile : map.getCollisionTiles(x, startY, x, endY)) {
			if (!rect.overlaps(tile)) continue;
			if (velocity.x >= 0)
				position.x = tile.x - rect.width;
			else
				position.x = tile.x + tile.width;
			velocity.x *= collideDampingX;
			return true;
		}
		return false;
	}

	public boolean collideY () {
		rect.x = position.x;
		rect.y = position.y + velocity.y + collisionOffsetY;

		int y;
		if (velocity.y > 0)
			y = (int)(rect.y + rect.height);
		else
			y = (int)rect.y;
		int startX = (int)rect.x;
		int endX = (int)(rect.x + rect.width);
		Array<Rectangle> collisionTiles = map.getCollisionTiles(startX, y, endX, y);
		for (Rectangle tile : collisionTiles) {
			if (!rect.overlaps(tile)) continue;
			if (velocity.y > 0)
				position.y = tile.y - rect.height;
			else {
				position.y = tile.y + tile.height;
				if (state == CharacterState.jump) setState(CharacterState.idle);
				setGrounded(true);
			}
			velocity.y = 0;
			return true;
		}
		return false;
	}

	public void moveLeft (float delta) {
		float adjust;
		if (isGrounded()) {
			adjust = runGroundX;
			setState(CharacterState.run);
		} else
			adjust = velocity.x <= 0 ? runAirSame : runAirOpposite;
		if (velocity.x > -maxVelocityX) velocity.x = Math.max(velocity.x - adjust * delta, -maxVelocityX);
		dir = -1;
	}

	public void moveRight (float delta) {
		float adjust;
		if (isGrounded()) {
			adjust = runGroundX;
			setState(CharacterState.run);
		} else
			adjust = velocity.x >= 0 ? runAirSame : runAirOpposite;
		if (velocity.x < maxVelocityX) velocity.x = Math.min(velocity.x + adjust * delta, maxVelocityX);
		dir = 1;
	}

	public void jump () {
		velocity.y += jumpVelocity;
		setState(CharacterState.jump);
		setGrounded(false);
	}
	public void jumpDamping () {
		if (velocity.y > 0) velocity.y *= Player.jumpDamping;
	}

}
