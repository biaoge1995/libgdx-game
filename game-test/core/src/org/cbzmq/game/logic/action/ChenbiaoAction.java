package org.cbzmq.game.logic.action;

import com.iohao.game.action.skeleton.annotation.ActionController;
import com.iohao.game.action.skeleton.annotation.ActionMethod;
import org.cbzmq.game.net.GameStartUp;
import org.cbzmq.game.proto.Msg;

@ActionController(0)
public class ChenbiaoAction {

    @ActionMethod(1)
    public Msg login(Msg msg) {

        msg.name = "逻辑服务已经收到你的请求，感谢";
        return msg;
    }
}
