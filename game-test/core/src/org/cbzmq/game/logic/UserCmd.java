package org.cbzmq.game.logic;

public interface UserCmd {

    /** 模块 - 主 cmd : 2 */
    int cmd = ActionModuleCmd.hallModuleCmd;

    /** 创建房间 */
    int createRoom = 1;
    /** 进入房间 */
    int enterRoom = 2;
    /** 游戏开始 */
    int gameStart = 3;
    /** 登录 **/
    int login = 4;

}
