package org.cbzmq.game.model;

import org.cbzmq.game.Map;
import org.cbzmq.game.enums.CharacterState;
import org.cbzmq.game.enums.CharacterType;
import org.cbzmq.game.proto.CharacterProto;
import org.cbzmq.game.view.BulletActor;

public class Bullet extends Character<Bullet> {


    public final static float width = 1;
    public final static float height = 1;
    public final static float distance = 25;

    public Player player;


    public Bullet(Player player, Map map, float startX, float startY, float vx, float vy) {
        super(map, "bullet");
        position.set(startX, startY);
        velocity.set(vx, vy);
        damage = 1;
        hp = 1;
        rect.width = width;
        rect.height = height;
        this.player = player;
        this.characterType = CharacterType.bullet;

    }

    @Override
    public void update(float delta) {

       super.update(delta);

    }

    @Override
    public boolean collideX() {
        if (super.collideX()) {
            hp = 0;
            beDeath();
            return true;
        }
        return false;
    }

    @Override
    public boolean collideY() {
        if (super.collideY()) {
            hp = 0;
            beDeath();
            return true;
        }
        return false;
    }

    public static Bullet parserProto(CharacterProto.Character proto) {
        Bullet bullet = new Bullet(
                null,
                null
                , proto.getPosition().getX()
                , proto.getPosition().getY()
                , proto.getVelocity().getX()
                , proto.getVelocity().getY());
        Character father = Character.parserProto(proto);
        Character.copyToSon(father, bullet);
        return bullet;
    }


    public static CharacterProto.Character toBulletProto(Bullet bullet) {
        return Character.toCharacterProto(bullet).setType(CharacterType.bullet).build();

    }


    public  void updateByCharacter(Bullet father) {
        super.updateByCharacter(father);
        this.player = father.player;
    }
}
