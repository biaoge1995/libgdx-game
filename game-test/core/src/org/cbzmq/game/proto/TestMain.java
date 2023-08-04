package org.cbzmq.game.proto;

import com.badlogic.gdx.utils.Array;
import com.google.protobuf.ByteString;
import org.cbzmq.game.MathUtils;
import org.cbzmq.game.enums.MsgHeader;
import org.cbzmq.game.model.Character;
import org.cbzmq.game.model.Player;

import java.util.Date;

/**
 * @ClassName TestMain
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/8/3 9:57 上午
 * @Version 1.0
 **/
public class TestMain {
    public static void main(String[] args) {
        Character character = new Character("chenbiao");
        Player player = new Player();
        Array<Character> characters = new Array<>();
        characters.add(player);
        MsgByte msgByte = new MsgByte(MsgHeader.SYNC_CHARACTERS_INFO, new Date().getTime());
        msgByte.setCharacters(characters);
        byte[] bytes = msgByte.toByteArray();
        System.out.println(bytes.length);

    }
}
