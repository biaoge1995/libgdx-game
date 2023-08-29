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



    @ActionMethod(GameCmd.move)
    public Move move(Move move) {
        int id = move.id;
        Move.MoveType moveType = move.moveType;
        float time = move.getTime();
        Character childById = GameLogicEngine.me().root.getChildById(id);
        System.out.println("收到请求");
        if (Objects.nonNull(childById)) {
            switch (moveType) {
                case moveLeft:
                    Event.OneCharacterEvent event = Event.OneCharacterEvent.createEvent("", OneBodyEventType.moveLeft, childById);
                    event.setFloatData(time);
                    GameLogicEngine.me().updateByEvent(event);
                    return move;
                case moveRight:
                    event = Event.OneCharacterEvent.createEvent("", OneBodyEventType.moveRight, childById);
                    event.setFloatData(time);
                    GameLogicEngine.me().updateByEvent(event);
                    return move;
            }

        }
        return null;


    }
}
