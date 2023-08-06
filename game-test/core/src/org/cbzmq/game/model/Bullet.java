package org.cbzmq.game.model;

import org.cbzmq.game.enums.CharacterState;
import org.cbzmq.game.enums.CharacterType;
import org.cbzmq.game.proto.CharacterIntProto;
import org.cbzmq.game.proto.CharacterProto;

public class Bullet extends Character<Bullet> {


    public final static float width = 1;
    public final static float height = 1;
    public final static float distance = 25;

    public Player player;


    public Bullet(Player player,float startX, float startY, float vx, float vy) {
        super( "bullet");
        position.set(startX, startY);
        velocity.set(vx, vy);
        damage = 1;
        hp = 0.2f;
        rect.width = width;
        rect.height = height;
        this.player = player;
        this.characterType = CharacterType.bullet;

    }


    public void collideMapX() {
        beCollide();
    }

    public void collideMapY() {
        beCollide();
    }


    @Override
    public void beCollide() {
        hp = 0;
        beDeath();
        state = CharacterState.death;
    }

    @Override
    public void update(float delta) {

       super.update(delta);

    }


    public static Bullet parserProto(CharacterProto.Character proto) {
        Bullet bullet = new Bullet(
                null
                , proto.getPosition().getX()
                , proto.getPosition().getY()
                , proto.getVelocity().getX()
                , proto.getVelocity().getY());
        Character father = Character.parserProto(proto);
        Character.copyToSon(father, bullet);
        return bullet;
    }

    public static Bullet parserProto(CharacterIntProto.Character proto) {
        Bullet bullet = new Bullet(
                null
                , proto.getPosition().getX()
                , proto.getPosition().getY()
                , proto.getVelocity().getX()
                , proto.getVelocity().getY());
        Character father = Character.parserProto(proto);
        Character.copyToSon(father, bullet);
        return bullet;
    }

    public static Bullet parseFromBytes(byte[] bytes) throws Exception {
        Character father = Character.parseFromBytes(bytes);
        Bullet bullet = new Bullet(
                null
                , father.position.x
                , father.position.y
                , father.velocity.x
                , father.velocity.y);
        Character.copyToSon(father, bullet);
        return bullet;
    }


    public  CharacterProto.Character.Builder toCharacterProto() {
        CharacterProto.Character.Builder builder = super.toCharacterProto();
        return builder.setType(CharacterType.bullet);

    }

    public CharacterIntProto.Character.Builder toCharacterIntProto() {
        CharacterIntProto.Character.Builder builder = super.toCharacterIntProto();
        return builder.setType(CharacterType.bullet);

    }


    public  void updateByCharacter(Bullet father) {
        super.updateByCharacter(father);
        this.player = father.player;
    }


}
