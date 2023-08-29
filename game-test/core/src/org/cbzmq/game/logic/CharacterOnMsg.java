package org.cbzmq.game.logic;

import com.iohao.game.action.skeleton.core.CmdKit;
import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.external.core.message.ExternalMessage;
import org.cbzmq.game.logic.GameCmd;
import org.cbzmq.game.logic.OnMessage;
import org.cbzmq.game.logic.UserCmd;
import org.cbzmq.game.model.Character;
import org.cbzmq.game.net.Client;
import org.cbzmq.game.proto.Move;
import org.cbzmq.game.proto.UserLogin;
import org.cbzmq.game.proto.VectorProto;

public class CharacterOnMsg {
    public static class LoginVerifyOnMessage implements OnMessage {

        @Override
        public int getCmdMerge()  {
            return CmdKit.merge(UserCmd.cmd, UserCmd.login);
        }

        @Override
        public UserLogin response(ExternalMessage externalMessage, byte[] data) {
            UserLogin UserLogin = DataCodecKit.decode(data, UserLogin.class);
            return UserLogin;
        }

        public static LoginVerifyOnMessage me() {
            return Holder.ME;
        }

        /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
        private static class Holder {
            static final LoginVerifyOnMessage ME = new LoginVerifyOnMessage();
        }

    }


    public static class MoveOnMessage implements OnMessage{

        @Override
        public int getCmdMerge()  {
            return CmdKit.merge(GameCmd.cmd, GameCmd.move);
        }


        public void request(Character character, Move.MoveType moveType,float time) {
            Move move = new Move(character.id
                    , moveType
                    , time
                    , new VectorProto(character.position.x,character.position.y)
                    , new VectorProto(character.velocity.x,character.velocity.y));
            OnMessage.super.request(move);
//            System.out.println("移动msg"+move);
        }

        @Override
        public Move response(ExternalMessage externalMessage, byte[] data) {
            Move move = DataCodecKit.decode(data, Move.class);
            Character childById = Client.me().playerGroup.getChildById(move.id);
            childById.updatePosition(move);
            return move;
        }

        public static MoveOnMessage me() {
            return MoveOnMessage.Holder.ME;
        }

        /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
        private static class Holder {
            static final MoveOnMessage ME = new MoveOnMessage();
        }

    }

}
