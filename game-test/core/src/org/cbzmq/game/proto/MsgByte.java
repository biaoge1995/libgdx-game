package org.cbzmq.game.proto;

import com.badlogic.gdx.utils.Array;
import org.cbzmq.game.MathUtils;
import org.cbzmq.game.enums.CharacterState;
import org.cbzmq.game.enums.MsgHeader;
import org.cbzmq.game.model.Character;

public class MsgByte {
    MsgHeader msgHeader;
    private long timeStamp;
    private Array<Character> characters;

    public MsgByte(MsgHeader msgHeader, long timeStamp) {
        this.msgHeader = msgHeader;
        this.timeStamp = timeStamp;
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
            bytes.addAll(character.toCharacterBytes());
        }
        byte[] bytes2 = new byte[bytes.size + 5];

        bytes2[0] = (byte) msgHeader.getNumber();
        byte[] time = MathUtils.intToByteArray((int) (timeStamp / 1000));
        bytes2[1] = time[0];
        bytes2[2] = time[1];
        bytes2[3] = time[2];
        bytes2[4] = time[3];
        for (int i = 5; i < bytes.size; i++) {
            bytes2[i] = bytes.get(i).byteValue();
        }
        return bytes2;
    }

    public static MsgByte parseFromBytes(byte[] bytes) {
        MsgHeader msgHeader1 = MsgHeader.valueOf(bytes[0]);
        byte[] timeBytes = {bytes[1], bytes[2], bytes[3], bytes[4]};
        int time = MathUtils.byteArrayToInt(timeBytes);
        return null;
    }

    ;


}
