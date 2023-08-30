package org.cbzmq.game.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;
import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import org.cbzmq.game.proto.CharacterType;
import org.cbzmq.game.logic.AbstractLogicEngine;
import org.cbzmq.game.proto.CharacterState;
import org.cbzmq.game.proto.Move;


/**
 * The model class for an enemy or player that moves around the map.
 */
@ProtobufClass
public class Character implements Observer {


    @Protobuf(fieldType= FieldType.INT32, order=1)
    public int id;
    //角色的矩阵
    //TODO view会用到
    public Rectangle rect = new Rectangle();

    //TODO view会用到
    @Protobuf(fieldType= FieldType.OBJECT, order=2)
    public MyVector2 position = new MyVector2();

    //目标位置向量
    @Protobuf(fieldType= FieldType.OBJECT, order=3)
    public MyVector2 targetPosition = new MyVector2();

    //速度向量
    //TODO view会用到
    @Protobuf(fieldType= FieldType.OBJECT, order=4)
    public MyVector2 velocity = new MyVector2();

    //瞄准点的位置位置
    @Protobuf(fieldType= FieldType.OBJECT, order=5)
    public MyVector2 aimPoint = new MyVector2();
    //默认的动画状态
    //TODO view会用到
    @Protobuf(fieldType= FieldType.OBJECT, order=6)
    public CharacterState state = CharacterState.idle;



    //角色模型面朝的方向 1右边 -1左边
    //TODO view会用到
    @Protobuf(fieldType= FieldType.FLOAT, order=7)
    public float dir;
    //TODO view会用到
    @Protobuf(fieldType= FieldType.FLOAT, order=8)
    public float hp;

    @Protobuf(fieldType= FieldType.OBJECT, order=9)
    public CharacterType characterType = CharacterType.unknown;
    public String name;

    //地面时间
    public static float groundedTime = 0.15f;

    //地板上的 控制的x奔跑速度
    public static float runGroundX = 80, runAirSame = 45, runAirOpposite = 45;

    //空中的时间
    public float airTime;

    public float collisionOffsetY;


    public float collisionTimer;

    @Null
    Group<Character> parent;

    @Null
    AbstractLogicEngine abstractLogicEngine;

    //是否状态改变
    //TODO view会用到
    public boolean stateChanged;
    //雪条

    //最大x方向上的位移
    public float maxVelocityX;
    //碰撞Y的偏移量

    //跳的速度在Y上
    public float jumpVelocity;

//    public boolean isWin = false;

    public float damage;

    private final EventQueue queue;

    private final Array<Observer> observers;

    private boolean isWin = false;


    public Character() {
        this("Character");
    }

    public Character(String name) {
        this.name = name;
        this.queue = new EventQueue();
        this.observers = new Array<>();
        queue.setObservers(observers);

    }

    public void update(float delta) {
        if (hp <= 0) {
            beDeath();
        }

        if (velocity.y < 0 && state != CharacterState.jumping && state != CharacterState.falling) {
            setState(CharacterState.falling);
            setGrounded(false);
        }
        if (velocity.x == 0 && isGrounded()) {
            setState(CharacterState.idle);

        }

//        if (dir*velocity.x > 0 && velocity.y == 0 && isGrounded()) {
//            setState(CharacterState.running);
//
//        }

        if (isGrounded() && state == CharacterState.jumping) {
            setState(CharacterState.idle);
        }
        if (isWin) {
            win();
        }
        queue.drain();
    }

    public boolean updateHp(float hp) {
        this.hp = hp;
        return true;
    }

    public boolean isWin() {
        return isWin;
    }

    public void setWin(boolean win) {
        isWin = win;
    }

    /**
     * 用于从服务器更新运动状态和位置
     * @param move
     */
    public void updateMoveState(Move move) {
        position.set(move.position.x, move.position.y);
        velocity.set(move.velocity.x, move.velocity.y);
        switch (move.moveType) {
            case moveLeft:
                running();
                turnLeft();
                break;
            case moveRight:
                running();
                turnRight();
                break;
            case jump:
                jump();
                break;
        }
    }


    public Move move(Move move) {
        switch (move.moveType) {
            case moveLeft:
                moveLeft(move.getTime());
                break;
            case moveRight:
                moveRight(move.getTime());
                break;
            case jump:
                jump();
                break;
        }
        move.position.x = position.x;
        move.position.y = position.y;
        move.velocity.x = velocity.x;
        move.velocity.y = velocity.y;
        return move;
    }

    @Override
    public boolean onOneObserverEvent(Event.OneCharacterEvent event) {
        //判断当前事件是
        if (!(event.getCharacter() == this)) return false;
        if (hp == 0) return false;
        switch (event.getEventType()) {
            case jump:
                if (isGrounded()) {
                    return jump();
                }
                return false;
            case moveLeft:
                return moveLeft(event.getFloatData());
            case moveRight:
                return moveRight(event.getFloatData());
            case bloodUpdate:
                return updateHp(event.getFloatData());
            case attack:
                return attack();
            case aimPoint:
                aimPoint.set(event.getVector());
                return true;
            case beDeath:
                beDeath();
                return true;
            case jumpDamping:
                jumpDamping();
                return true;
            case born:
            case win:
            case dispose:
            case beRemove:
            case lose:
            case frameEnd:
            case collisionMap:
                return true;
        }
        return true;
    }

    @Override
    public boolean onTwoObserverEvent(Event.TwoObserverEvent event) {
        //a就是自己
        Character a = event.getA();
        Character b = event.getB();
        if (event.getA() != this) return false;
        switch (event.getEventType()) {
            case collisionCharacter:
                beCollide();
                break;
            case hit:
                if (beHit()) queue.pushTwoCharacterEvent(event);
                break;
        }
//        Gdx.app.log("监听者" + this + "| 事件" + event.getEventType().toString(), event.getA() + "->" + event.getB());
        return true;
    }


    public boolean beHit() {
        return true;
    }

    //是否可以被从parent中清除掉
    public boolean isCanBeRemove() {
        if (state == CharacterState.death) {
            return true;
        }
        return false;
    }

    public boolean remove() {
        if (parent != null) {
            return parent.removeCharacter(this, true);
        }
        return false;
    }


    public void beDeath() {
        state = CharacterState.death;
    }

    public boolean attack() {
        return false;
    }

    public void setState(CharacterState newState) {
        if ((state == newState && state != CharacterState.falling) || state == CharacterState.death) return;
        state = newState;
//        stateTime = 0;
        stateChanged = true;
    }

    public void running() {
        if (isGrounded()) {
            setState(CharacterState.running);
        }
    }

    public void turnLeft() {
        dir = -1;
    }

    public void turnRight() {
        dir = 1;
    }

    public boolean moveLeft(float delta) {
        float adjust;
        if (isGrounded()) {
            adjust = runGroundX;
            running();
        } else
            adjust = velocity.x <= 0 ? runAirSame : runAirOpposite;
        if (velocity.x > -maxVelocityX) velocity.x = Math.max(velocity.x - adjust * delta, -maxVelocityX);
        turnLeft();
        return true;
    }

    public boolean moveRight(float delta) {
        float adjust;
        if (isGrounded()) {
            adjust = runGroundX;
            running();
        } else
            adjust = velocity.x >= 0 ? runAirSame : runAirOpposite;
        if (velocity.x < maxVelocityX) velocity.x = Math.min(velocity.x + adjust * delta, maxVelocityX);
        turnRight();
        return true;
    }

    /**
     * 跳跃事件
     */
    public boolean jump() {
        if (isGrounded()) {
            velocity.y += jumpVelocity;
            setState(CharacterState.jumping);
            setGrounded(false);
            return true;
        }
        return false;
    }

    public void jumpDamping() {
        if (velocity.y > 0) velocity.y *= Player.jumpDamping;
    }


    public static <T extends Character> void copyToSon(Character father, T son) {
        son.id = father.id;
//        son.name = father.name;
        son.position = father.position;
//        son.targetPosition = father.targetPosition;
        son.velocity = father.velocity;
        son.state = father.state;
//        son.stateTime = father.stateTime;
        son.dir = father.dir;
//        son.airTime = father.airTime;
        son.rect = father.rect;
        son.stateChanged = father.stateChanged;
        son.hp = father.hp;
//        son.maxVelocityX = father.maxVelocityX;
//        son.collisionOffsetY = father.collisionOffsetY;
//        son.jumpVelocity = father.jumpVelocity;
//        son.isWin = father.isWin;
        son.damage = father.damage;
//        son.collisionTimer = father.collisionTimer;
//        son.setQueue(father.getQueue());
//        son.parent = father.parent;
    }

//    public static Character parserProto(CharacterProto.Character proto) {
//        Character character = new Character("unknown");
//        character.setId(proto.getId());
//        character.position.set(proto.getPosition().getX(), proto.getPosition().getY());
//        character.velocity.set(proto.getVelocity().getX(), proto.getVelocity().getY());
////        character.setState(proto.getState());
//        ;
//        character.dir = proto.getDir();
//        character.rect.set(proto.getRect().getX(), proto.getRect().getY(), proto.getRect().getWidth(), proto.getRect().getHeight());
//        character.hp = proto.getHp();
//        character.collisionTimer = proto.getCollisionTimer();
//        return character;
//
//    }


//    public CharacterProto.Character.Builder toCharacterProto() {
//        CharacterProto.Character.Builder builder = CharacterProto.Character.newBuilder();
//        CharacterProto.Vector2.Builder positionProto = CharacterProto.Vector2.newBuilder();
//        CharacterProto.Vector2.Builder velocityProto = CharacterProto.Vector2.newBuilder();
//        CharacterProto.Rectangle.Builder rectProto = CharacterProto.Rectangle.newBuilder();
//        builder.setType(CharacterType.unknown)
//                .setId(this.getId())
//                .setPosition(positionProto
//                        .setX(this.position.x)
//                        .setY(this.position.y)
//                        .build())
//                .setVelocity(velocityProto
//                        .setX(this.velocity.x)
//                        .setY(this.velocity.y)
//                        .build())
////                .setState(this.state)
//                .setDir(this.dir)
//                .setRect(rectProto
//                        .setX(this.rect.x)
//                        .setY(this.rect.y)
//                        .setWidth(this.rect.width)
//                        .setHeight(this.rect.height))
//                .setHp(this.hp)
//                .setCollisionTimer(this.collisionTimer);
//
//        return builder;
//    }


    public <T extends Character> void updateByCharacter(T character) {

//        this.id = character.id;
//        this.name = character.name;
        this.position = character.position;
//        this.targetPosition = character.targetPosition;
        this.velocity = character.velocity;
        this.setState(character.state);
//        this.stateTime = character.stateTime;
        this.dir = character.dir;
//        this.airTime = character.airTime;
        this.rect = character.rect;
//        this.stateChanged = character.stateChanged;
        this.hp = character.hp;
//        this.maxVelocityX = character.maxVelocityX;
//        this.collisionOffsetY = character.collisionOffsetY;
//        this.jumpVelocity = character.jumpVelocity;
//        this.isWin = character.isWin;
        this.damage = character.damage;
//        this.collisionTimer = character.collisionTimer;
//        this.setQueue(character.getQueue());
//        this.parent = character.parent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Group getParent() {
        return parent;
    }

    public void setParent(Group parent) {
        this.parent = parent;
    }

    public AbstractLogicEngine getModel() {
        return abstractLogicEngine;
    }

    public void setTargetPosition(MyVector2 targetPosition) {
        this.targetPosition = targetPosition;
    }


    public void setModel(AbstractLogicEngine abstractLogicEngine) {
        this.abstractLogicEngine = abstractLogicEngine;
    }


    public void win() {

    }


    @Override
    public String toString() {
        return name + getId();
    }


    public CharacterType getCharacterType() {
        return characterType;
    }

    public void setCharacterType(CharacterType characterType) {
        this.characterType = characterType;
    }


    public boolean isNeedCheckCollision() {
        if (state == CharacterState.death) return false;
        return true;
    }


    public boolean isGrounded() {
        // The character is considered grounded for a short time after leaving the ground, making jumping over gaps easier.
        //角色离开地面后会被视为短暂停飞，从而更容易跳过空隙
        return airTime < groundedTime;
    }

    public void addListen(Observer observer) {
        this.observers.add(observer);
    }

    public void setGrounded(boolean grounded) {
        airTime = grounded ? 0 : groundedTime;
    }

    public void beCollide() {
    }

    public void collideMapX() {
    }

    public void collideMapY() {

    }

    public MyVector2 getAimPoint() {
        return aimPoint;
    }

    public void setAimPoint(MyVector2 aimPoint) {
        this.aimPoint = aimPoint;
    }
}




