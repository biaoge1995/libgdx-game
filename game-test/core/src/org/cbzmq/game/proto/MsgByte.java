package org.cbzmq.game.proto;

import com.badlogic.gdx.utils.Array;
import org.cbzmq.game.MathUtils;
import org.cbzmq.game.enums.CharacterState;
import org.cbzmq.game.enums.CharacterType;
import org.cbzmq.game.enums.EnemyType;
import org.cbzmq.game.enums.MsgHeader;
import org.cbzmq.game.model.Bullet;
import org.cbzmq.game.model.Character;
import org.cbzmq.game.model.Enemy;
import org.cbzmq.game.model.Player;

import java.util.Date;

public class MsgByte {
    MsgHeader msgHeader;
    private long timeStamp;
    private Array<Character> characters;

    public MsgByte(MsgHeader msgHeader, long timeStamp) {
        this.msgHeader = msgHeader;
        this.timeStamp = timeStamp;
    }

    public static void main(String[] args) throws Exception {
        long time = new Date().getTime();
        MsgByte msgByte = new MsgByte(MsgHeader.SYNC_CHARACTERS_INFO, time);
        Array<Character> characters = new Array<>();
        Player player = new Player();
        player.position.set(1, 2);
        player.velocity.set(3, 4);
        player.rect.set(5, 6, 7, 8);
//        ByteArray byteArray = player.toCharacterBytes();
//        byte[] tmp = new byte[byteArray.getBytes().size];
//        for (int i = 0; i < byteArray.getBytes().size; i++) {
//            tmp[i] = byteArray.getBytes().get(i);
//        }
//        Player player1 = Player.parseFromBytes(tmp);
//        System.out.println(player1);
        characters.add(player);
        characters.add(new Bullet(null, 2, 4, 4, 5));
        Enemy enemy = new Enemy(EnemyType.becomesBig);
        enemy.position.set(1, 2);
        enemy.velocity.set(3, 4);
        enemy.rect.set(5, 6, 7, 8);
        characters.add(enemy);
        msgByte.setCharacters(characters);
        byte[] bytes = msgByte.toByteArray();
        System.out.println(bytes.length);
        MsgByte msgByte1 = parseFromBytes(bytes);
        System.out.println(msgByte1.characters.size);
    }

    public MsgHeader getMsgHeader() {
        return msgHeader;
    }

    public void setMsgHeader(MsgHeader msgHeader) {
        this.msgHeader = msgHeader;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Array<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(Array<Character> characters) {
        this.characters = characters;
    }

    public byte[] toByteArray() {

        Array<Byte> bytes = new Array<>();
        for (Character character : characters) {
            if (character.state == CharacterState.death) continue;
            bytes.addAll(character.toCharacterBytes().getBytes());
        }
        byte[] bytes2 = new byte[bytes.size + 5];

        bytes2[0] = (byte) msgHeader.getNumber();
        byte[] time = MathUtils.intToByteArray((int) (timeStamp / 1000));
        bytes2[1] = time[0];
        bytes2[2] = time[1];
        bytes2[3] = time[2];
        bytes2[4] = time[3];
        int j = 0;
        for (int i = 5; i < bytes2.length; i++) {
            bytes2[i] = bytes.get(j).byteValue();
            j++;
        }
        return bytes2;
    }

    public static MsgByte parseFromBytes(byte[] bytes) throws Exception {
        MsgHeader msgHeader1 = MsgHeader.valueOf(bytes[0]);
        byte[] timeBytes = {bytes[1], bytes[2], bytes[3], bytes[4]};
        int time = MathUtils.byteArrayToInt(timeBytes);
        Array<Character> characters = new Array<>();
        int index = 5;
        byte[] tmp = new byte[29];
        int j = 0;
        for (int i = 5; i < bytes.length; i++) {
            CharacterType characterType;

            characterType = CharacterType.valueOf(bytes[index]);
            switch (characterType) {
                case player:
                    if (i <= index + 24) {
                        tmp[j] = bytes[i];
                        j++;
                        if (i == index + 24) {
                            characters.add(Player.parseFromBytes(tmp));
                            index = i + 1;
                            tmp = new byte[29];
                            j = 0;
                        }
                    }
                    break;
                case bullet:
                    if (i <= index + 22) {
                        tmp[j] = bytes[i];
                        j++;
                        if (i == index + 22) {
                            characters.add(Bullet.parseFromBytes(tmp));
                            index = i + 1;
                            tmp = new byte[29];
                            j = 0;
                        }
                    }
                    break;
                case enemy:
                    int i1 = index + 28;
                    if (i <= i1) {
                        tmp[j] = bytes[i];
                        j++;
                        if (i == i1) {
                            characters.add(Enemy.parseFromBytes(tmp));
                            index = i + 1;
                            tmp = new byte[29];
                            j = 0;
                        }
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + characterType);
            }


        }
        MsgByte msgByte = new MsgByte(msgHeader1, time);
        msgByte.setCharacters(characters);
        return msgByte;
    }

    ;


}
