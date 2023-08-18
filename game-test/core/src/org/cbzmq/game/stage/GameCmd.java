package org.cbzmq.game.stage;

public interface GameCmd {

    /** 模块 - 主 cmd : 2 */
    int cmd = ActionModuleCmd.gameModuleCmd;


    /** 坦克移动 */
    int move = 1;

    /** 坦克射击(发射子弹) */
    int jump = 2;

    /** 坦克射击(发射子弹) */
    int attack = 3;

    /** 瞄准 */
    int aim = 4;

    /*** 广播 */
    int broadcasts = 5;
}
