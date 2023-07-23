package awt.service;

import awt.game.GameJFrame;
import awt.game.GameMapContext;
import awt.model.Constans;
import awt.model.domain.*;
import awt.model.msg.Msg;
import awt.model.msg.TankActionMsg;
import awt.model.msg.UserActionMsg;
import awt.proto.enums.GameStatus;
import awt.proto.enums.TankAction;
import awt.proto.enums.UserAction;

import java.util.*;

/**
 * @author chenbiao
 * @date 2020-12-19 20:57
 */
public class ClientSrv extends TimerTask {

    //客户端通信渠道
    private GameJFrame gameJFrame;
    private TankGamerSrv tankGamerSrv;
    private BgmSrv bgmSrv;
    private GameMapContext gameMapContext;

    public static void main(String[] args) throws Exception {
        ClientSrv clientSrv = new ClientSrv();
        clientSrv.play("localhost", 0);
        Timer time = new Timer();
        time.scheduleAtFixedRate(clientSrv, 1000, Constans.time);

    }

    public void play(String ip, int playId) throws Exception {
        Player player = new Player(ip, 0);


        tankGamerSrv = new TankGamerSrv();
        gameMapContext = tankGamerSrv.getGamerMap();

        gameJFrame = new GameJFrame(gameMapContext,"客户端", null, player);
//        Music music = BgmSrv.initMusic(BgmSrv.startMusicFilePath);
//        BgmSrv bgmSrv = new BgmSrv(music);
//        new Thread(bgmSrv).start();

        Timer time = new Timer();
        time.schedule(tankGamerSrv, 1000, Constans.time);

    }

    @Override
    public void run() {
        while (tankGamerSrv.getGameStatus() == GameStatus.RUNNING) {
            gameJFrame.rePaintPanel();
        }

    }



    /**
     * 通信
     *
     * @param userActionMsg
     */
    public void sendUserMsg(UserActionMsg userActionMsg) {
        Msg msg = tankGamerSrv.handlerUserAction(userActionMsg);
    }

    public void sendUserTankMsg(TankActionMsg tankActionMsg) {
        Msg msg = tankGamerSrv.handlerUserTankAction(tankActionMsg);
    }

    /**
     * 登录
     *
     * @param
     */
    public void ConnectAndLogin(Player player) {
        sendUserMsg(new UserActionMsg(player.getId(), UserAction.USER_LOGIN));

    }

    /**
     * 获取地图
     *
     * @param
     * @param player
     */
    public void getMap(Player player) {
        sendUserMsg(new UserActionMsg(player.getId(), UserAction.MAP));
    }

    /**
     * 获取游戏状态
     *
     * @param
     * @param player
     */
    public void getGameStat(Player player) {
        sendUserMsg(new UserActionMsg(player.getId(), UserAction.GET_GAME_STAT));
    }

    /**
     * 注册
     *
     * @param
     * @param player
     */
    public void register(Player player) {
        sendUserMsg(new UserActionMsg(player.getId(), UserAction.REGISTER));
    }

    /**
     * 开始游戏
     *
     * @param
     * @param player
     */
    public void startGamer(Player player) {
        sendUserMsg(new UserActionMsg(player.getId(), UserAction.START_PLAY));
    }


    /**
     * 锁定视角
     *
     * @param
     */
    public void lockPerspectiveOnOff(Player player) {
        if (gameJFrame.getLockElement() != null) {
            gameJFrame.setLockElement(null);
        } else {
            gameJFrame.setLockElement(tankGamerSrv.getPlayerTank(player.getId()));
        }

    }


    /**
     * 坦克行动
     *
     * @param
     * @param id
     * @param tankAction
     */
    public void tankAction(int id, TankAction tankAction) {
        TankActionMsg tankActionMsg = new TankActionMsg(id, tankAction);
        String uuid = UUID.randomUUID().toString();
        tankActionMsg.setUuid(uuid);
        tankActionMsg.setTime(new Date().getTime());
        sendUserTankMsg(tankActionMsg);
    }


}
