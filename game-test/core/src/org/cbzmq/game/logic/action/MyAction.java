package org.cbzmq.game.logic.action;

import com.iohao.game.action.skeleton.annotation.ActionController;
import com.iohao.game.action.skeleton.annotation.ActionMethod;
import org.cbzmq.game.enums.OneBodyEventType;
import org.cbzmq.game.logic.GameCmd;
import org.cbzmq.game.logic.GameLogicEngine;
import org.cbzmq.game.model.Character;
import org.cbzmq.game.model.Event;
import org.cbzmq.game.proto.Move;
import org.cbzmq.game.proto.Msg;

import java.util.Objects;

@ActionController(GameCmd.cmd)
public class MyAction {

    @ActionMethod(9)
    public Msg login2(Msg msg) {

        msg.name = "逻辑服务已经收到你的请求，感谢";
        return msg;
    }

    @ActionMethod(GameCmd.move)
    public Move move(Move moveData) {
        int id = moveData.id;
        Move.MoveType moveType = moveData.moveType;
        GameLogicEngine me = GameLogicEngine.me();
        Character childById = me.playerGroup.getChildById(id);
        System.out.println("收到请求");
        if (Objects.nonNull(childById)) {
            switch (moveType) {
                case moveLeft:
                case moveRight:
                case jump:
                   return childById.move(moveData);

            }

        }
        return null;


    }
}
