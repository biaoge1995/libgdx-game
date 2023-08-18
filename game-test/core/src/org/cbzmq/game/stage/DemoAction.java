package org.cbzmq.game.stage;

import com.iohao.game.action.skeleton.annotation.ActionController;
import com.iohao.game.action.skeleton.annotation.ActionMethod;
import com.iohao.game.action.skeleton.core.flow.FlowContext;
import org.cbzmq.game.enums.OneBodyEventType;
import org.cbzmq.game.model.Character;
import org.cbzmq.game.model.Event;
import org.cbzmq.game.model.Group;
import org.cbzmq.game.proto.Move;

import java.util.Objects;

@ActionController(ActionModuleCmd.gameModuleCmd)
public class DemoAction {



    @ActionMethod(GameCmd.move)
    Move move(Move move, FlowContext flowContext) {
        int id = move.id;
        Move.MoveType moveType = move.moveType;
        float time = move.getTime();
        Character childById = GameEngine.me().root.getChildById(id);
        if(Objects.nonNull(childById)){
            switch (moveType) {
                case moveLeft:
                    Event.OneCharacterEvent event = Event.OneCharacterEvent.createEvent("", OneBodyEventType.moveLeft, childById);
                    event.setFloatData(time);
                    GameEngine.me().onOneObserverEvent(event);
                case moveRight:
                    event = Event.OneCharacterEvent.createEvent("", OneBodyEventType.moveRight, childById);
                    event.setFloatData(time);
                    GameEngine.me().onOneObserverEvent(event);
                    break;
            }

        }

        return null;

    }
}
