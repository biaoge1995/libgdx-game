package org.cbzmq.game.proto;

import com.google.protobuf.ByteString;
import org.cbzmq.game.MathUtils;
import org.cbzmq.game.model.Character;
import org.cbzmq.game.model.Player;

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
        CharacterIntProto.Character intBuild = character.toCharacterIntProto().build();
        CharacterProto.Character floatbuild1 = character.toCharacterProto().build();
//        byte[] bytes = Character.toCharacterBytes(player);

        System.out.println(intBuild.toByteArray().length);
        System.out.println(floatbuild1.toByteArray().length);
//        System.out.println(bytes.length);

    }
}
