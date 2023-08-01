package org.cbzmq.game.model;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;
import org.cbzmq.game.Map;
import org.cbzmq.game.Constants;
import org.cbzmq.game.enums.CharacterState;
import org.cbzmq.game.enums.CharacterType;
import org.cbzmq.game.proto.CharacterProto;
import org.cbzmq.game.stage.EventQueue;
import org.cbzmq.game.stage.Model;


/**
 * The model class for an enemy or player that moves around the map.
 */
public class Character<T extends Character>{


    public static float minVelocityX = 0.001f, maxVelocityY = 20f;
    //地面时间
    public static float groundedTime = 0.15f;
    //x方向 地板阻尼 空气阻尼 碰撞阻尼
    public static float dampingGroundX = 36, dampingAirX = 15, collideDampingX = 0.7f;
    //地板上的 控制的x奔跑速度
    public static float runGroundX = 80, runAirSame = 45, runAirOpposite = 45;
//    //游戏主逻辑引用
//    public Map map;

    public CharacterType characterType=CharacterType.unknown;
    //事件
    public CharacterListener listener ;
//            = new CharacterAdapter();

    @Null
    Group<Character> parent;

    @Null
    Model model;

    public int id;

    public String name;

    //位置向量
    //TODO view会用到
    public Vector2 position = new Vector2();

    //目标位置向量
    public Vector2 targetPosition = new Vector2();

    //速度向量
    //TODO view会用到
    public Vector2 velocity = new Vector2();
    //默认的动画状态
    //TODO view会用到
    public CharacterState state = CharacterState.idle;
    //开始时间
    public float stateTime;
    //方向
    //TODO view会用到
    public float dir;
    //空中的时间
    public float airTime;
    //角色的矩阵
    //TODO view会用到
    public Rectangle rect = new Rectangle();
    //是否状态改变
    //TODO view会用到
    public boolean stateChanged;
    //雪条
    //TODO view会用到
    public float hp;
    //最大x方向上的位移
    public float maxVelocityX;
    //碰撞Y的偏移量
    public float collisionOffsetY;
    //跳的速度在Y上
    public float jumpVelocity;
    //
    public boolean isWin = false;

    public float damage;
    public float collisionTimer;

    private EventQueue queue;

    public Array<Character> childs = new Array<>();

    //是否可以被碰撞
    public boolean isCanBeCollision=true;

    public float resilience=0.8f;


    public Character() {
    }

    public Character(String name) {
        this.name = name;

    }



    public void update(float delta) {

        if(hp<=0){
            beDeath();
        }

    }

    //是否可以被从parent中清除掉
    public boolean isCanBeRemove(){
        if(state==CharacterState.death){
            return true;
        }
        return false;
    }

    public boolean remove(){
        if(parent!=null){
            return parent.removeCharacter(this,true);
        }
        return false;
    }



    public void beDeath(){
        if(this.state!=CharacterState.death){
            this.hp = 0;
            this.state = CharacterState.death;
            if(queue!=null) queue.death(this);
        }
    }

    public void setState(CharacterState newState) {
        if ((state == newState && state != CharacterState.fall) || state == CharacterState.death) return;
        state = newState;
        stateTime = 0;
        stateChanged = true;
    }

    public boolean isGrounded() {
        // The character is considered grounded for a short time after leaving the ground, making jumping over gaps easier.
        //角色离开地面后会被视为短暂停飞，从而更容易跳过空隙
        return airTime < groundedTime;
    }

    public void setGrounded(boolean grounded) {
        airTime = grounded ? 0 : groundedTime;
    }

    public void beCollide(){
    }

    public void collideMapX() {
    }

    public void collideMapY() {

    }

    public void moveLeft(float delta) {
        float adjust;
        if (isGrounded()) {
            adjust = runGroundX;
            setState(CharacterState.run);
        } else
            adjust = velocity.x <= 0 ? runAirSame : runAirOpposite;
        if (velocity.x > -maxVelocityX) velocity.x = Math.max(velocity.x - adjust * delta, -maxVelocityX);
        dir = -1;
    }

    public void moveRight(float delta) {
        float adjust;
        if (isGrounded()) {
            adjust = runGroundX;
            setState(CharacterState.run);
        } else
            adjust = velocity.x >= 0 ? runAirSame : runAirOpposite;
        if (velocity.x < maxVelocityX) velocity.x = Math.min(velocity.x + adjust * delta, maxVelocityX);
        dir = 1;
    }

    public void jump() {
        velocity.y += jumpVelocity;
        setState(CharacterState.jump);
        setGrounded(false);
    }

    public void jumpDamping() {
        if (velocity.y > 0) velocity.y *= Player.jumpDamping;
    }





    public static <T extends Character> void copyToSon(Character father,T son){
        son.id = father.id;
//        son.map = father.map;
        son.listener = father.listener;
        son.name = father.name;
        son.position = father.position;
        son.targetPosition = father.targetPosition;
        son.velocity = father.velocity;
        son.state = father.state;
        son.stateTime = father.stateTime;
        son.dir = father.dir;
        son.airTime = father.airTime;
        son.rect = father.rect;
        son.stateChanged = father.stateChanged;
        son.hp = father.hp;
        son.maxVelocityX = father.maxVelocityX;
        son.collisionOffsetY = father.collisionOffsetY;
        son.jumpVelocity = father.jumpVelocity;
        son.isWin = father.isWin;
        son.damage = father.damage;
        son.collisionTimer = father.collisionTimer;
        son.setQueue(father.getQueue());
        son.childs = father.childs;
        son.parent = father.parent;
    }

    public static  Character parserProto(CharacterProto.Character proto) {
        Character character = new Character("unknown");
        character.setId(proto.getId());
        character.position.set(proto.getPosition().getX(), proto.getPosition().getY());
        character.velocity.set(proto.getVelocity().getX(), proto.getVelocity().getY());
        character.state = proto.getState();
        character.dir = proto.getDir();
        character.rect.set(proto.getRect().getX(), proto.getRect().getY(), proto.getRect().getWidth(), proto.getRect().getHeight());
        character.stateChanged = proto.getStateChanged();
        character.hp = proto.getHp();
        character.collisionTimer = proto.getCollisionTimer();
        return character;

    }

    public  CharacterProto.Character.Builder toCharacterProto() {
        CharacterProto.Character.Builder builder = CharacterProto.Character.newBuilder();
        CharacterProto.Vector2.Builder positionProto = CharacterProto.Vector2.newBuilder();
        CharacterProto.Vector2.Builder velocityProto = CharacterProto.Vector2.newBuilder();
//        CharacterProto.Vector2.Builder tartgetPositionProto = CharacterProto.Vector2.newBuilder();
        CharacterProto.Rectangle.Builder rectProto = CharacterProto.Rectangle.newBuilder();
        builder.setType(CharacterType.unknown)
                .setId(this.getId())
                .setPosition(positionProto
                        .setX(this.position.x)
                        .setY(this.position.y)
                        .build())
                .setVelocity(velocityProto
                        .setX(this.velocity.x)
                        .setY(this.velocity.y)
                        .build())
                .setState(this.state)
//                .setStateTime(character.stateTime)
                .setDir(this.dir)
//                .setAirTime(character.airTime)
                .setRect(rectProto
                        .setX(this.rect.x)
                        .setY(this.rect.y)
                        .setWidth(this.rect.width)
                        .setHeight(this.rect.height))
                .setStateChanged(this.stateChanged)
                .setHp(this.hp)
//                .setMaxVelocityX(character.maxVelocityX)
//                .setCollisionOffsetY(character.collisionOffsetY)
//                .setJumpVelocity(character.jumpVelocity)
//                .setDamage(character.damage)
                .setCollisionTimer(this.collisionTimer);
//                .setTargetPosition(tartgetPositionProto
//                        .setX(character.targetPosition.x)
//                        .setY(character.targetPosition.y)
//                        .build());
        return builder;
    }



    public void updateByCharacter(T character) {

        this.id = character.id;
//        this.map = character.map;
        this.listener = character.listener;
        this.name = character.name;
        this.position = character.position;
        this.targetPosition = character.targetPosition;
        this.velocity = character.velocity;
        this.state = character.state;
        this.stateTime = character.stateTime;
        this.dir = character.dir;
        this.airTime = character.airTime;
        this.rect = character.rect;
        this.stateChanged = character.stateChanged;
        this.hp = character.hp;
        this.maxVelocityX = character.maxVelocityX;
        this.collisionOffsetY = character.collisionOffsetY;
        this.jumpVelocity = character.jumpVelocity;
        this.isWin = character.isWin;
        this.damage = character.damage;
        this.collisionTimer = character.collisionTimer;
        this.setQueue(character.getQueue());
        this.childs = character.childs;
        this.parent = character.parent;
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

    public Model getModel() {
        return model;
    }

    public void setTargetPosition(Vector2 targetPosition) {
        this.targetPosition = targetPosition;
    }


    public void setModel(Model model) {
        this.model = model;
    }

    public void addChild(Character child) {
        childs.add(child);
    }

    public void win() {

    }


    @Override
    public String toString() {
        return name+getId();
    }

    public EventQueue getQueue() {
        return queue;
    }

    public void setQueue(EventQueue queue) {
        this.queue = queue;
    }
}




