package org.cbzmq.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;
import org.cbzmq.game.enums.CharacterState;
import org.cbzmq.game.enums.CharacterType;
import org.cbzmq.game.proto.ByteArray;
import org.cbzmq.game.proto.CharacterIntProto;
import org.cbzmq.game.proto.CharacterProto;
import org.cbzmq.game.stage.Model;


/**
 * The model class for an enemy or player that moves around the map.
 */
public class Character implements Observer {

    private static final Array<ByteArray.Type> types;
    public CharacterType characterType = CharacterType.unknown;
    public int id;

    public String name;

    //地面时间
    public static float groundedTime = 0.15f;

    //地板上的 控制的x奔跑速度
    public static float runGroundX = 80, runAirSame = 45, runAirOpposite = 45;

    //空中的时间
    public float airTime;


    //角色的矩阵
    //TODO view会用到
    public Rectangle rect = new Rectangle();

    //TODO view会用到
    public Vector2 position = new Vector2();

    //目标位置向量
    public Vector2 targetPosition = new Vector2();

    //速度向量
    //TODO view会用到
    public Vector2 velocity = new Vector2();

    public float collisionOffsetY;


    public float collisionTimer;

    @Null
    Group<Character> parent;

    @Null
    Model model;

    //默认的动画状态
    //TODO view会用到
    public CharacterState state = CharacterState.idle;
    //开始时间

    //角色模型面朝的方向 1右边 -1左边
    //TODO view会用到
    public float dir;

    //是否状态改变
    //TODO view会用到
    public boolean stateChanged;
    //雪条
    //TODO view会用到
    public float hp;
    //最大x方向上的位移
    public float maxVelocityX;
    //碰撞Y的偏移量

    //跳的速度在Y上
    public float jumpVelocity;

//    public boolean isWin = false;

    public float damage;


    private EventQueue queue;





    public Character() {
    }

    public Character(String name) {
        this.name = name;

    }

    public void update(float delta) {
        if (hp <= 0) {
            beDeath();
        }

        if (velocity.y < 0 && state != CharacterState.jumping && state != CharacterState.falling) {
            setState(CharacterState.falling);
            setGrounded(false);
        }
        if(velocity.x==0 && isGrounded()){
            setState(CharacterState.idle);

        }
        if( isGrounded() && state == CharacterState.jumping){
            setState(CharacterState.idle);
        }
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
        if (this.state != CharacterState.death) {
            this.hp = 0;
            this.state = CharacterState.death;
            if (queue != null) queue.beDeath(this);
        }
    }

    public void setState(CharacterState newState) {
        if ((state == newState && state != CharacterState.falling) || state == CharacterState.death) return;
        state = newState;
//        stateTime = 0;
        stateChanged = true;
    }


    public void moveLeft(float delta) {
        float adjust;
        if (isGrounded()) {
            adjust = runGroundX;
            setState(CharacterState.running);
        } else
            adjust = velocity.x <= 0 ? runAirSame : runAirOpposite;
        if (velocity.x > -maxVelocityX) velocity.x = Math.max(velocity.x - adjust * delta, -maxVelocityX);
        dir = -1;
        this.queue.moveLeft(this);
    }

    public void moveRight(float delta) {
        float adjust;
        if (isGrounded()) {
            adjust = runGroundX;
            setState(CharacterState.running);
        } else
            adjust = velocity.x >= 0 ? runAirSame : runAirOpposite;
        if (velocity.x < maxVelocityX) velocity.x = Math.min(velocity.x + adjust * delta, maxVelocityX);
        dir = 1;
        this.queue.moveRight(this);
    }

    /**
     * 跳跃事件
     */
    public void jump() {
        velocity.y += jumpVelocity;
        setState(CharacterState.jumping);
        setGrounded(false);
        this.queue.jump(this);
    }

    public void jumpDamping() {
        if (velocity.y > 0) velocity.y *= Player.jumpDamping;
    }


    public static Character parseFromBytes(byte[] bytes) throws Exception {

        Array<Byte> bytes1 = new Array<>();
        for (byte aByte : bytes) {
            bytes1.add(aByte);
        }
        ByteArray byteArray = new ByteArray(bytes1, types);
        Character character = new Character("unknown");
        character.characterType = CharacterType.valueOf(byteArray.popByte());
        character.state = CharacterState.valueOf(byteArray.popByte());
        character.dir = byteArray.popByte();
        character.hp = byteArray.popByte();
        character.id = byteArray.popShort();
        character.position.set(byteArray.popShort(),byteArray.popShort());
        character.collisionTimer = byteArray.popShort()/100f;
        character.velocity.set(byteArray.popShort(),byteArray.popShort());
        character.rect.set(byteArray.popShort(),byteArray.popShort(),byteArray.popByte(),byteArray.popByte());
        character.stateChanged = byteArray.popBoolean();
        return character;
    }

    static {
        types = Array.with(ByteArray.Type.BYTE,
                ByteArray.Type.BYTE,
                ByteArray.Type.BYTE,
                ByteArray.Type.BYTE,
                ByteArray.Type.SHORT,
                ByteArray.Type.SHORT,
                ByteArray.Type.SHORT,
                ByteArray.Type.SHORT,
                ByteArray.Type.SHORT,
                ByteArray.Type.SHORT,
                ByteArray.Type.SHORT,
                ByteArray.Type.SHORT,
                ByteArray.Type.BYTE,
                ByteArray.Type.BYTE,
                ByteArray.Type.BOOLEAN
        );
    }

    //23个字节
    public ByteArray toCharacterBytes() {
        ByteArray byteArray = new ByteArray();
        byteArray.addByte((byte) this.characterType.getNumber());
        byteArray.addByte((byte) this.state.getNumber());
        byteArray.addByte((byte) this.dir);
        byteArray.addByte((byte) this.hp);
        byteArray.addShort((short) this.getId());
        byteArray.addShort((short) this.position.x);
        byteArray.addShort((short) this.position.y);
        byteArray.addShort((short) (this.collisionTimer * 100));
        byteArray.addShort((short) this.velocity.x);
        byteArray.addShort((short) this.velocity.y);
        byteArray.addShort((short) this.rect.x);
        byteArray.addShort((short) this.rect.y);
        byteArray.addByte((byte) this.rect.width);
        byteArray.addByte((byte) this.rect.height);
        byteArray.addBool(this.stateChanged);
        return byteArray;

    }


    public static <T extends Character> void copyToSon(Character father, T son) {
        son.id = father.id;
        son.name = father.name;
        son.position = father.position;
        son.targetPosition = father.targetPosition;
        son.velocity = father.velocity;
        son.state = father.state;
//        son.stateTime = father.stateTime;
        son.dir = father.dir;
        son.airTime = father.airTime;
        son.rect = father.rect;
        son.stateChanged = father.stateChanged;
        son.hp = father.hp;
        son.maxVelocityX = father.maxVelocityX;
        son.collisionOffsetY = father.collisionOffsetY;
        son.jumpVelocity = father.jumpVelocity;
//        son.isWin = father.isWin;
        son.damage = father.damage;
        son.collisionTimer = father.collisionTimer;
        son.setQueue(father.getQueue());
        son.parent = father.parent;
    }

    public static Character parserProto(CharacterProto.Character proto) {
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

    public static Character parserProto(CharacterIntProto.Character proto) {
        Character character = new Character("unknown");
        character.setId(proto.getId());
        character.position.set(proto.getPosition().getX() / 100f, proto.getPosition().getY() / 100f);
        character.velocity.set(proto.getVelocity().getX() / 100f, proto.getVelocity().getY() / 100f);
        character.state = proto.getState();
        character.dir = proto.getDir();
        character.rect.set(proto.getRect().getX() / 100f
                , proto.getRect().getY() / 100f
                , proto.getRect().getWidth() / 100f
                , proto.getRect().getHeight() / 100f);
        character.stateChanged = proto.getStateChanged();
        character.hp = proto.getHp() / 100f;
        character.collisionTimer = proto.getCollisionTimer() / 100f;
        return character;

    }


    public CharacterProto.Character.Builder toCharacterProto() {
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


    public CharacterIntProto.Character.Builder toCharacterIntProto() {
        CharacterIntProto.Character.Builder builder = CharacterIntProto.Character.newBuilder();
        CharacterIntProto.Vector2.Builder positionProto = CharacterIntProto.Vector2.newBuilder();
        CharacterIntProto.Vector2.Builder velocityProto = CharacterIntProto.Vector2.newBuilder();
//        CharacterProto.Vector2.Builder tartgetPositionProto = CharacterProto.Vector2.newBuilder();
        CharacterIntProto.Rectangle.Builder rectProto = CharacterIntProto.Rectangle.newBuilder();
        builder.setType(CharacterType.unknown)
                .setId(this.getId())
                .setPosition(positionProto
                        .setX((int) this.position.x * 100)
                        .setY((int) this.position.y * 100)
                        .build())
                .setVelocity(velocityProto
                        .setX((int) this.velocity.x * 100)
                        .setY((int) this.velocity.y * 100)
                        .build())
                .setState(this.state)
//                .setStateTime(character.stateTime)
                .setDir((int) this.dir)
//                .setAirTime(character.airTime)
                .setRect(rectProto
                        .setX((int) this.rect.x * 100)
                        .setY((int) this.rect.y * 100)
                        .setWidth((int) this.rect.width * 100)
                        .setHeight((int) this.rect.height * 100))
                .setStateChanged(this.stateChanged)
                .setHp((int) this.hp * 100)
//                .setMaxVelocityX(character.maxVelocityX)
//                .setCollisionOffsetY(character.collisionOffsetY)
//                .setJumpVelocity(character.jumpVelocity)
//                .setDamage(character.damage)
                .setCollisionTimer((int) this.collisionTimer * 100);
//                .setTargetPosition(tartgetPositionProto
//                        .setX(character.targetPosition.x)
//                        .setY(character.targetPosition.y)
//                        .build());
        return builder;
    }


    public <T extends Character> void updateByCharacter(T character) {

        this.id = character.id;
        this.name = character.name;
        this.position = character.position;
        this.targetPosition = character.targetPosition;
        this.velocity = character.velocity;
        this.state = character.state;
//        this.stateTime = character.stateTime;
        this.dir = character.dir;
        this.airTime = character.airTime;
        this.rect = character.rect;
        this.stateChanged = character.stateChanged;
        this.hp = character.hp;
        this.maxVelocityX = character.maxVelocityX;
        this.collisionOffsetY = character.collisionOffsetY;
        this.jumpVelocity = character.jumpVelocity;
//        this.isWin = character.isWin;
        this.damage = character.damage;
        this.collisionTimer = character.collisionTimer;
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

    public Model getModel() {
        return model;
    }

    public void setTargetPosition(Vector2 targetPosition) {
        this.targetPosition = targetPosition;
    }


    public void setModel(Model model) {
        this.model = model;
    }



    public void win() {

    }


    @Override
    public String toString() {
        return name + getId();
    }

    public EventQueue getQueue() {
        return queue;
    }

    public void setQueue(EventQueue queue) {
        this.queue = queue;

    }

    public CharacterType getCharacterType() {
        return characterType;
    }

    public void setCharacterType(CharacterType characterType) {
        this.characterType = characterType;
    }

    @Override
    public void onOneObserverEvent(Event.OneObserverEvent event) {
        if(event.getCharacter() instanceof Group) return;
        switch (event.getEventType()) {
            case frameEnd:
            case collisionMap:
                return;
            default:
                Gdx.app.log("监听者"+this+"| 事件"+event.getEventType().toString(),event.getCharacter().toString());
        }
    }

    @Override
    public void onTwoObserverEvent(Event.TwoObserverEvent event) {
        Gdx.app.log("监听者"+this+"| 事件"+event.getEventType().toString(),event.getA()+"->"+event.getB());
    }

    public boolean isNeedCheckCollision(){
        if(state == CharacterState.death)return false;
        return true;
    }





    public boolean isGrounded() {
        // The character is considered grounded for a short time after leaving the ground, making jumping over gaps easier.
        //角色离开地面后会被视为短暂停飞，从而更容易跳过空隙
        return airTime < groundedTime;
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





}




