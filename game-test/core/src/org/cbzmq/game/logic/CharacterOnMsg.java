package org.cbzmq.game.logic;

import com.iohao.game.action.skeleton.core.CmdKit;
import com.iohao.game.action.skeleton.core.DataCodecKit;
import com.iohao.game.external.core.message.ExternalMessage;
import org.cbzmq.game.model.Character;
import org.cbzmq.game.net.Client;
import org.cbzmq.game.proto.Move;
import org.cbzmq.game.proto.Move2;
import org.cbzmq.game.proto.MoveType;
import org.cbzmq.game.proto.UserLogin;

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
    public static class broadCastOnMessage implements OnMessage{

        @Override
        public int getCmdMerge()  {
            return CmdKit.merge(GameCmd.cmd, GameCmd.broadcasts);
        }




        @Override
        public Character response(ExternalMessage externalMessage, byte[] data) {
            Character character = DataCodecKit.decode(data, Character.class);
            Client.me().updateByBroadCast(character);
            return character;
        }

        public static broadCastOnMessage me() {
            return broadCastOnMessage.Holder.ME;
        }

        /** 通过 JVM 的类加载机制, 保证只加载一次 (singleton) */
        private static class Holder {
            static final broadCastOnMessage ME = new broadCastOnMessage();
        }

    }

    public static class MoveOnMessage implements OnMessage{

        @Override
        public int getCmdMerge()  {
            return CmdKit.merge(GameCmd.cmd, GameCmd.move);
        }


        public void request(Character character, MoveType moveType, float time) {
            Move move = new Move(character.id
                    , moveType
                    , time
                    , character.getPosition()
                    , character.velocity
            );
            move.setRequestTime(System.currentTimeMillis());
            OnMessage.super.request(move);
//            System.out.println("移动msg"+move);
        }

        @Override
        public Move2 response(ExternalMessage externalMessage, byte[] data) {
            Move2 move = DataCodecKit.decode(data, Move2.class);

            Client.me().updatePosition(move);
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
