package awt.model.domain;

import awt.game.GameMapContext;
import awt.model.msg.Msg;
import awt.model.msg.UserActionMsg;
import awt.proto.enums.GameStatus;

public interface Game {









    /**
     * 获取地图
     */
    Msg getGameMap();

    /**
     * 注册玩家
     *
     * @param playId
     */
     Msg register(int playId);

    /**
     * 处理玩家需求
     * @param userAction
     */
    Msg handlerUserAction(UserActionMsg userAction);

    /**
     * 初始化游戏地图
     */
     Msg joinGame();

    /**
     * 获取地图
     * @return
     */
     GameMapContext getGamerMap();

    /**
     * 获取游戏状态
     * @return
     */
    GameStatus getGameStatus();
    /**
     * 游戏主进程
     * 2、清除死亡元素
     * 3、控制一些需要自动移动的元素
     * 4、元素内部元素转移到地图上
     */
    void GameRunning();


    /**
     * 移动
     *
     * @param element
     */
    void elementMoved(Element element,int sum) ;








    /**
     * 解析地图元素
     *
     * @param jsonObject
     * @return
     */
//     GamerMap parseGetMap(JSONObject jsonObject);

}

