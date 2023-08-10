package org.cbzmq.game.model;

import com.badlogic.gdx.math.MathUtils;
import org.cbzmq.game.Constants;
import org.cbzmq.game.Utils;
import org.cbzmq.game.enums.CharacterState;
import org.cbzmq.game.enums.CharacterType;
import org.cbzmq.game.enums.EnemyType;
import org.cbzmq.game.proto.ByteArray;
import org.cbzmq.game.proto.CharacterIntProto;
import org.cbzmq.game.proto.CharacterProto;


/**
 * The model class for an enemy.
 */
public class Enemy extends Character {
    public static float heightSource = 398, width = 105 * Constants.scale, height = 200 * Constants.scale;
    public static float maxVelocityMinX = 4f, maxVelocityMaxX = 8.5f, maxVelocityAirX = 19f;
    public static float hpWeak = 1, hpSmall = 2, hpNormal = 3, hpStrong = 5, hpBecomesBig = 8, hpBig = 20;
    public static float damageWeak = 0.2f, damageSmall = 0.4f, damageNormal = 0.6f, damageStrong = 1f, damageBecomesBig = 0.4f, damageBig = 1f;
    public static int hitCountWeak = 0, hitSmall = 0, hitNormal = 1, hitStrong = 2, hitBecomesBig = 2, hitBig = 4;
    public static float corpseTime = 5, fadeTime = 3;
    public static float jumpDistanceNormal = 20, jumpDelayNormal = 1.6f, jumpVelocityNormal = 12, jumpVelocityBig = 18;
    public static float sizeSmall = 0.5f, sizeBig = 2.5f, sizeStrong = 1.3f, bigDuration = 2, smallCount = 14;
    public static float normalKnockbackX = 19, normalKnockbackY = 9, bigKnockbackX = 12, bigKnockbackY = 6;
    public static float collisionDelay = 0.3f;
    //TODO view会用到
    public float deathTimer = corpseTime;


    public float maxVelocityGroundX;
    public float jumpDelayTimer, jumpDistance, jumpDelay;
    //TODO view会用到
    public EnemyType enemyType;
    //TODO view会用到
    public float size = 1;

    //TODO view会用到
    public float bigTimer;
    public float spawnSmallsTimer;
    public boolean move;
    public boolean forceJump;
    public int collisions;
    public float knockbackX = normalKnockbackX;
    public float knockbackY = normalKnockbackY;

    public Utils.Counter counter;

    // This is here for convenience, the model should never touch the view.
//	public EnemyActor view;

    public Enemy(EnemyType enemyType) {
        super("alien-" + enemyType);
        this.enemyType = enemyType;
        this.characterType = CharacterType.enemy;
        rect.width = width;
        rect.height = height;

        maxVelocityGroundX = MathUtils.random(maxVelocityMinX, maxVelocityMaxX);
        maxVelocityX = maxVelocityGroundX;
        jumpVelocity = jumpVelocityNormal;
        jumpDelay = jumpDelayNormal;
        jumpDistance = jumpDistanceNormal;

        if (enemyType == EnemyType.big) {
            size = sizeBig;
            rect.width = width * size * 0.7f;
            rect.height = height * size * 0.7f;
            hp = hpBig;
            knockbackX = normalKnockbackX;
            knockbackY = normalKnockbackY;
            damage = damageBig;
            counter = Utils.createCounter(hitBig);
        } else if (enemyType == EnemyType.small) {
            size = sizeSmall;
            rect.width = width * size;
            rect.height = height * size;
            hp = hpSmall;
            damage = damageSmall;
            counter = Utils.createCounter(hitSmall);

        } else if (enemyType == EnemyType.weak) {
            hp = hpWeak;
            damage = damageWeak;
            counter = Utils.createCounter(hitCountWeak);

        } else if (enemyType == EnemyType.becomesBig) {
            hp = hpBecomesBig;
            damage = damageBecomesBig;
            counter = Utils.createCounter(hitBecomesBig);

        } else if (enemyType == EnemyType.strong) {
            hp = hpStrong;
            size = sizeStrong;
            jumpVelocity *= 1.5f;
            jumpDistance *= 1.4f;
            damage = damageStrong;
            counter = Utils.createCounter(hitStrong);

        } else {
            hp = hpNormal;
            damage = 0.5f;
            counter = Utils.createCounter(hitNormal);

        }

        jumpDelayTimer = MathUtils.random(0, jumpDelay);
    }

    public void update(float delta) {
        stateChanged = false;

        //死亡时
        if (state == CharacterState.death) {
            if (enemyType == EnemyType.becomesBig && size == 1) {
                bigTimer = bigDuration;
                collisionTimer = bigDuration;
                state = CharacterState.running;
                hp = hpBig;
                knockbackX = bigKnockbackX;
                knockbackY = bigKnockbackY;
                enemyType = EnemyType.big;
                jumpVelocity = jumpVelocityBig;
            } else if (enemyType == EnemyType.big) {
                spawnSmallsTimer = 0.8333f;
                enemyType = EnemyType.normal;
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

        collisionTimer -= delta;

        if (state == CharacterState.death) deathTimer -= delta;

        super.update(delta);

    }

    public boolean beHit(){
        if(counter.reduce()){
            counter.reset();
            return true;
        }else {
            return false;
        }
    }

    public void beCollide() {
        collisionTimer = Enemy.collisionDelay;
        if (hp > 0) {
            state = CharacterState.falling;
            jumpDelayTimer = MathUtils.random(0, jumpDelay);
        }
    }

    @Override
    public boolean isCanBeRemove() {
        if (state == CharacterState.death && this.deathTimer < 0) {
            return true;
        }
        return false;
    }

    public void win() {
        if (isGrounded() && velocity.x == 0) {
            jumpVelocity = jumpVelocityNormal / 2;
            dir = -dir;
            jump();
        }
    }

    /**
     * 到达地图边界时做的动作
     */
    public void collideMapX() {
        // If grounded and collided with the map, jump to avoid the obstacle.
        //如果接地并与地图相撞，跳跃以避开障碍物。
        if (isGrounded()) forceJump = true;
        collisions++;
    }

    public static Enemy parserProto(CharacterProto.Character proto) {
        Enemy enemy = new Enemy(proto.getEnemyType());
        Character father = Character.parserProto(proto);
        Character.copyToSon(father, enemy);
        enemy.deathTimer = proto.getDeathTimer();
        enemy.size = proto.getSize();
        enemy.bigTimer = proto.getBigTimer();
        return enemy;
    }

    public static Enemy parserProto(CharacterIntProto.Character proto) {
        Enemy enemy = new Enemy(proto.getEnemyType());
        Character father = Character.parserProto(proto);
        Character.copyToSon(father, enemy);
        enemy.deathTimer = proto.getDeathTimer() / 100f;
        enemy.size = proto.getSize() / 100f;
        enemy.bigTimer = proto.getBigTimer() / 100f;
        return enemy;
    }

    public static Enemy parseFromBytes(byte[] bytes) throws Exception {
        Character father = Character.parseFromBytes(bytes);
        Enemy enemy = new Enemy(EnemyType.valueOf(bytes[27]));
        Character.copyToSon(father, enemy);
        byte[] deathTimer = {bytes[23], bytes[24]};
        enemy.deathTimer = Utils.byteArrayToShort(deathTimer) / 100f;
        byte[] bigTimer = {bytes[25], bytes[26]};
        enemy.bigTimer = Utils.byteArrayToShort(bigTimer) / 100f;
        enemy.size = bytes[28] / 100f;
        return enemy;
    }


    public CharacterIntProto.Character.Builder toCharacterIntProto() {
        CharacterIntProto.Character.Builder builder = super.toCharacterIntProto();

        return builder.setType(CharacterType.enemy)
                .setDeathTimer((int) (this.deathTimer * 100f))
//				.setMaxVelocityGroundX(enemy.maxVelocityGroundX)
//				.setJumpDelayTimer(enemy.jumpDelayTimer)
                .setEnemyType(this.enemyType)
                .setSize((int) (this.size * 100f))
                .setBigTimer((int) (this.bigTimer * 100f))
//				.setSpawnSmallsTimer(enemy.spawnSmallsTimer)
//				.setMove(enemy.move)
//				.setForceJump(enemy.forceJump)
//				.setCollisions(enemy.collisions)
//				.setKnockbackX(enemy.knockbackX)
//				.setKnockbackY(enemy.knockbackY)
                ;
    }


    public CharacterProto.Character.Builder toCharacterProto() {
        CharacterProto.Character.Builder builder = super.toCharacterProto();

        return builder.setType(CharacterType.enemy)
                .setDeathTimer(this.deathTimer)
//				.setMaxVelocityGroundX(enemy.maxVelocityGroundX)
//				.setJumpDelayTimer(enemy.jumpDelayTimer)
                .setEnemyType(this.enemyType)
                .setSize(this.size)
                .setBigTimer(this.bigTimer)
//				.setSpawnSmallsTimer(enemy.spawnSmallsTimer)
//				.setMove(enemy.move)
//				.setForceJump(enemy.forceJump)
//				.setCollisions(enemy.collisions)
//				.setKnockbackX(enemy.knockbackX)
//				.setKnockbackY(enemy.knockbackY)
                ;
    }

    //29
    public ByteArray toCharacterBytes() {
        ByteArray byteArray = super.toCharacterBytes();

        byteArray.addShort((short) (this.deathTimer * 100));
        byteArray.addShort((short) (this.bigTimer * 100));
        byteArray.addByte((byte) this.enemyType.getNumber());
        byteArray.addByte((byte) (this.size * 100));

//        byte[] deathTimer = org.cbzmq.game.MathUtils.shortToByteArray((short) (this.deathTimer*100));
//        byte[] bigTimer = org.cbzmq.game.MathUtils.shortToByteArray((short) (this.bigTimer*100));
//        byte enemyType = (byte) this.enemyType.getNumber();
//        byte size = (byte) (this.size*100);
//        bytes.add(deathTimer[0],deathTimer[1]);
//        bytes.add(bigTimer[0],bigTimer[1]);
//        bytes.add(enemyType);
//        bytes.add(size);

        return byteArray;

    }


    public void updateByCharacter(Enemy father) {
        super.updateByCharacter(father);
        this.deathTimer = father.deathTimer;
        this.maxVelocityGroundX = father.maxVelocityGroundX;
        this.jumpDelayTimer = father.jumpDelayTimer;
        this.jumpDistance = father.jumpDistance;
        this.jumpDelay = father.jumpDelay;
        this.enemyType = father.enemyType;
        this.size = father.size;
        this.bigTimer = father.bigTimer;
        this.spawnSmallsTimer = father.spawnSmallsTimer;
        this.move = father.move;
        this.forceJump = father.forceJump;
        this.collisions = father.collisions;
        this.knockbackX = father.knockbackX;
        this.knockbackY = father.knockbackY;
    }

    public Enemy copy(Character father, Character son) {
        return null;
    }
}
