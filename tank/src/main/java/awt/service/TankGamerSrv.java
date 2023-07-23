package awt.service;



import awt.game.GameMapContext;
import awt.model.Constans;
import awt.model.domain.*;
import awt.model.msg.Msg;
import awt.model.msg.TankActionMsg;
import awt.model.msg.UserActionMsg;
import awt.proto.enums.Direct;
import awt.proto.enums.GameStatus;
import awt.proto.enums.TankAction;
import awt.proto.enums.UserAction;
import awt.utils.MapUtil;
import awt.utils.MathUtils;
import com.alibaba.fastjson.JSON;

import java.util.*;

/**
 * @author chenbiao
 * @date 2020-12-19 11:51
 */
public class TankGamerSrv extends TimerTask implements Game {
    //维护一个 id->玩家 的map
    private final Map<Integer, Player> playerMap = new HashMap<>();
    //目前设计只能一个id 一个tank
    private final Map<Integer, Tank> idToTank = new HashMap<>();

    private GameMapContext gameMapContext = new GameMapContext();
    private GameStatus gameStatus = GameStatus.NOT_START;
    private static final Map<String, Class> classMap = new HashMap<String, Class>();


    public TankGamerSrv() throws Exception {
        if (gameStatus == GameStatus.NOT_START) {
            gameStatus = GameStatus.RUNNING;
            gameMapContext = MapUtil.loadGameMap();
            for (Element e : gameMapContext.getElements()) {
                //id system的元素自动跑
                if (e.getPlayId()==0) {
                    e.setNeedSystemControlMove(true);
                }
            }

        }
    }

    public void run() {
        GameRunning();

    }

    /**
     * 游戏主进程
     * 2、清除死亡元素
     * 3、控制一些需要自动移动的元素
     * 4、元素内部元素转移到地图上
     */
    public void GameRunning() {
        long l = System.currentTimeMillis();
        //画布整体元素
        Vector<Element> elements = gameMapContext.getElements();
        int sum = 0;
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            //移除死亡的元素
            if (!element.isLive()) {
                element.beDead();
                elements.remove(i);
                i = i - 1;
            }
            //tank元素
            if (element instanceof Tank) {
                Tank tank = (Tank) element;
                if (!tank.isLive()) {
                    idToTank.remove(tank.getPlayId());
                }
                Vector<Element> bullets = tank.getChildElement();
                for (int j = 0; j < bullets.size(); j++) {
                    Bullet bullet = (Bullet) bullets.get(j);

                    if (bullet.isLive() && bullet.isFire()) {
                        //移除弹夹
                        bullets.remove(j);
                        j = j - 1;
                        //加入地图
                        addEleToGameMap(bullet);
                    }
                }
            }

            if (element instanceof Tank) {
                Tank t = (Tank) element;
                if (t.isNeedSystemControlMove()){
//                    t.randomRun();
//                    t.executeCommand();
                }
//
            }

            //给需要自动运动的元素赋能element.isNeedSystemControlMove() &&
            if (element.isLive() && (element.isMoved())) {
                sum++;
                elementMoved(element,sum);

            }
        }
        long end = System.currentTimeMillis();
        long l1 = end - l;
//        System.out.println("循环一次时间"+l1);

    }


    /**
     * 获取地图
     */
    public Msg getGameMap() {
        Msg msg = new Msg(UserAction.MAP.toString(), JSON.toJSONString(gameMapContext));
        return msg;
    }


    /**
     * 处理用户tank 信息
     *
     * @param tankActionMsg
     * @return
     */
    public Msg handlerUserTankAction(TankActionMsg tankActionMsg) {
        return TankAction(tankActionMsg);
    }

    /**
     * 处理玩家需求
     *
     * @param userAction
     */
    public Msg handlerUserAction(UserActionMsg userAction) {
        Msg msg = null;
        switch (userAction.getUserAction()) {
            case REGISTER:
                msg = register(userAction.getPlayId());
                break;
            case USER_LOGIN:
                msg = login(userAction.getPlayId());
                break;
            case START_PLAY:
                msg = joinGame();
                break;
            case MAP:
//                msg = getGameMap();
                break;
            case GET_GAME_STAT:
                GameStatus gameStatus = getGameStatus();
                msg = new Msg(UserAction.GET_GAME_STAT.toString(), JSON.toJSONString(gameStatus));
                break;
        }
        return msg;
    }


    /**
     * 注册
     *
     * @param playId
     */
    public Msg register(int playId) {
        if (playerMap.containsKey(playId)) {
            System.out.println("已存在该id");
        } else {
            playerMap.put(playId, new Player("localhost", 0));
            System.out.println(playId + "注册成功");
        }
        return null;
    }

    /**
     * 注册
     *
     * @param playId
     */
    public Msg login(int playId) {
        if (playerMap.containsKey(playId)) {
            System.out.println("已登陆");
        } else {
            System.out.println(playId + "链接成功");
        }
        return null;
    }

    /**
     * 加入玩家的坦克
     *
     * @param playId
     * @param tank
     */
    private void addPlayerTank(int playId, Tank tank) {
        idToTank.put(playId, tank);
    }

    /**
     * 根据id 返回玩家的坦克
     *
     * @param playId
     * @return
     */
    public Tank getPlayerTank(int playId) {
        return idToTank.get(playId);
    }

    /**
     * 加入游戏
     */
    public Msg joinGame() {
        int x = 20;
        for (int id : playerMap.keySet()) {
            if (!idToTank.containsKey(id)) {
                Tank tank = new Tank(100, 100, Constans.tankHeight, Constans.tankHeight);
                tank.setMoved(false);
                tank.setCanSee(true);
                gameMapContext.getPlayerElement().put(id, tank);

                tank.setPlayId(id);
                int wide = tank.getWide();
                x += wide + 1;
//                tank.ammunitionLoading();
                addEleToGameMap(tank);
                //添加到map中
                addPlayerTank(id, tank);
                System.out.println("初始化对局成功" );
            }
        }
        return null;
    }

    /**
     * 获取地图
     *
     * @return
     */
    public GameMapContext getGamerMap() {
        return gameMapContext;
    }

    /**
     * 获取游戏状态
     *
     * @return
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }


    /**
     * PK 系统
     *
     * @param element
     */
    private void vs(Element element, Element element2) {
        if (element.isCollisionDead()) {
            element.setLive(false);
        }
        if (element2.isCollisionDead()) {
            element2.setLive(false);
        }
        //硬度比较
        int diffHardness = element.getHardness() - element2.getHardness();
        if (diffHardness > 0) {
            double blood = element2.getBlood() - element2.getDamagePct() * element.getPower();
            element2.setBlood(blood);

        } else if (diffHardness < 0) {
            double blood = element.getBlood() - element.getDamagePct() * element2.getPower();
            element.setBlood(blood);
        } else {
            double blood2 = element2.getBlood() - element2.getDamagePct() * element.getPower();
            element2.setBlood(blood2);
            double blood = element.getBlood() - element.getDamagePct() * element2.getPower();
            element.setBlood(blood);
        }
    }


    /**
     * 看见对方
     * @param element
     * @param element2
     */

    private void seeEachOther(Element element,Element element2){
        int mapXCoordinate = element.getMapXCoordinate();
        int mapYCoordinate = element.getMapYCoordinate();
        int mapXCoordinate1 = element2.getMapXCoordinate();
        int mapYCoordinate1 = element2.getMapYCoordinate();
        double distance = MathUtils.distance(mapXCoordinate, mapYCoordinate, mapXCoordinate1, mapYCoordinate1);
        //当前对象看到其他对象
        if(element.isCanSee() && !element2.isBuild()){
            if(distance<=element.getViewR()){
                element.addOtherEleCanSee(element2);
            }else {
                element.removeOtherEleCanSee(element2);
            }
        }
        //其他对象 看到当前对象
        if(element2.isCanSee() && !element.isBuild()){
            if(distance<=element2.getViewR()){
                element2.addOtherEleCanSee(element);
            }else {
                element2.removeOtherEleCanSee(element);
            }
        }
    }

    /**
     * 元素与其他元素的pk
     *
     * @param element
     * @return
     */
    private Set<Direct> ElementVsOthers(Element element,int sum) {
        Set<Direct> directs = new HashSet<Direct>();
        Vector<Element> elements = gameMapContext.getElements();
        long l = System.currentTimeMillis();
        for (int i = 0; i < gameMapContext.getElements().size(); i++) {
            sum++;
            Element e = elements.get(i);
            seeEachOther(element,e);
            Set<Direct> canMove = element.collision(e);
            //同一个玩家id的元素 互相不卡位置
            if (( element.getPlayId()==(e.getPlayId()))) {
                canMove.clear();
            } else if (element.bePassedThrough(e) || e.bePassedThrough(element)) {
                canMove.clear();
            } else if (canMove != null && canMove.size() > 0) {
                //vs系统
                vs(element, e);
                directs.addAll(canMove);

            }
        }
        long end = System.currentTimeMillis();
        long l1 = end - l;
//        System.out.println("vs循环一次时间"+l1);
        return directs;
    }


    /**
     * 移动一个元素
     *
     * @param element
     */
    public void elementMoved(Element element,int sum) {
        //元素与其他元素的pk,计算后不可前进的方向
        Set<Direct> canMoveDirects = ElementVsOthers(element,sum);
        Direct direct = element.getDirect();
        if (canMoveDirects.contains(direct)) {
            return;
        }
        //可以移动
        if (element.isLive() && element.isMoved()) {
            //判断到达了哪个边界
            Direct reachMapDirect = element.reachMapBorder(0, 0, gameMapContext.getWide(), gameMapContext.getHeight());
            if (reachMapDirect == null || element.getDirect() != reachMapDirect) {
                //元素移动
                element.move();
            } else {
                //到达边界行为
                element.reachMapBorderAction();
            }
        }
    }


    /**
     * @param element
     */
    private void addEleToGameMap(Element element) {
        gameMapContext.getElements().add(element);
    }

    /**
     * 坦克动作
     *
     * @param
     * @param tankActionMsg
     */
    public Msg TankAction(TankActionMsg tankActionMsg) {
        TankAction(tankActionMsg.getId(), tankActionMsg.getTankAction());
//        idToAction.put(id, tankActionMsg);
        return null;
    }

    /**
     * 坦克移动,开火,停止
     *
     * @param id
     * @param tankAction
     */
    private void TankAction(int id, TankAction tankAction) {
        Tank tank = getPlayerTank(id);
        if (tank != null) {
            switch (tankAction) {
                case FIRE:
                case STOP:
                case AMMUNITION_LOADING:
                case TURN_UP:
                case TURN_LEFT:
                case TURN_RIGHT:
                case TURN_DOWN:
//                    tank.addCommand(new Command(tankAction,1));
//                    tank.tankAction(tankAction);
                    elementMoved(tank,0);
                    break;
            }
        }
    }





//    /**
//     * 解析地图元素
//     *
//     * @param jsonObject
//     * @return
//     */
//    public static GameMapContext parseGetMap(JSONObject jsonObject) {
//
//        GameMapContext gameMapContext = new GameMapContext();
//        JSONArray elements = jsonObject.getJSONArray("elements");
//        Map<String, TankActionMsg> idToActionMap = new HashMap<String, TankActionMsg>();
//        JSONObject idToAction = jsonObject.getJSONObject("idToAction");
//        for (String id : idToAction.keySet()) {
//            JSONObject msg = idToAction.getJSONObject(id);
//            if (msg != null) {
//                TankAction tankAction = TankAction.parse(msg.getString("tankAction"));
//                String uuid = msg.getString("uuid");
//                long time = msg.getLong("time");
//                TankActionMsg tankActionMsg = new TankActionMsg(new PlayId(id), tankAction, uuid, time);
//                idToActionMap.put(id, tankActionMsg);
//            }
//        }
//        for (Object element : elements) {
//            JSONObject jsonObject1 = (JSONObject) element;
//            ElementType elementType = ElementType.parse(jsonObject1.getString("elementType"));
//            Class aClass = null;
//            Element ele;
//            if (elementType.getClassName() != null) {
//                try {
//                    if (classMap.containsKey(elementType.getClassName())) {
//                        aClass = classMap.get(elementType.getClassName());
//                    } else {
//                        aClass = Class.forName(elementType.getClassName());
//                        classMap.put(elementType.getClassName(), aClass);
//                    }
//                } catch (ClassNotFoundException e) {
//                    aClass = Element.class;
//                }
//            }
//            ele = (Element) JSON.parseObject(jsonObject1.toString(), aClass);
//            gameMapContext.getElements().add(ele);
//        }
//        gameMapContext.setIdToAction(idToActionMap);
//        return gameMapContext;
//    }


    public static void main(String[] args) throws Exception {
        TankGamerSrv tankGamerSrv = new TankGamerSrv();
        tankGamerSrv.register(0);
        tankGamerSrv.joinGame();
        tankGamerSrv.TankAction(0, TankAction.TURN_UP);
        tankGamerSrv.TankAction(0, TankAction.FIRE);
        tankGamerSrv.GameRunning();
    }


}
