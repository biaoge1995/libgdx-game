package awt.game;

//import awt.enums.*;

import awt.game.command.Command;
import awt.model.Constans;
import awt.model.domain.Element;
import awt.model.domain.Player;
import awt.model.domain.Tank;
import awt.proto.Msg;
import awt.proto.enums.*;
import awt.utils.MapUtil;
import awt.utils.MathUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.socket.DatagramPacket;
import lombok.SneakyThrows;

import java.net.InetSocketAddress;
import java.util.*;

/**
 * @ClassName Game
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/6/26 2:47 下午
 * @Version 1.0
 **/
public class Game extends TimerTask {

    private GameContext gameContext;
    public final Player systemPlayer = new Player("localhost", 0);
    private int count = 0;//计数器
    private int counter = 0;//桢计数器
    public CommandFactory commandQueue;

    public final Queue<Msg> msgQueue = new LinkedList<>();

    private Vector<awt.proto.Element> elementsProto;


    public Game(boolean isServer) {
        gameContext = new GameContext(isServer);
        commandQueue = new CommandFactory(gameContext);
    }


    //加载静态地图资源
    public void loading() throws Exception {
        System.out.println("地图加载中");
        gameContext.gameLoading();
        //加载1号地图
        GameMapContext gameMapContext = MapUtil.loadGameMap("2");
        gameMapContext.setGameContext(gameContext);
        gameContext.setGameMapContext(gameMapContext);
        gameContext.gameReady();
        Set<Element> npc = new HashSet<>();
        gameContext.getGameMapContext()
                .getElements()
                .stream()
                .filter(e -> e.getPlayId() == 0)
                .forEach(e -> {
                    e.setGameRole(GameRole.NPC);
                    npc.add(e);
                });
        systemPlayer.setReady(true);
//        gameContext.addPlayer(systemPlayer);
        gameContext.addPlayerAndEles(systemPlayer, npc, Constans.SYSTEM_GROUP_NAME);
        //初始化时设置所有元素都没有被更新过
        gameContext.getGameMapContext()
                .getElements().stream().map(e -> {
            e.setUpdate(false);
            return e;
        });
        System.out.println("地图加载完毕，等待玩家加入");
    }


    /**
     * 游戏主进程
     * 2、清除死亡元素
     * 3、控制一些需要自动移动的元素
     * 4、元素内部元素转移到地图上
     */
    @SneakyThrows
    @Override
    public void run() {
        if (gameContext.getGameStatus() != GameStatus.RUNNING && !gameContext.getPlayers().isEmpty()) {
            gameContext.gameRunning();
            System.out.println("游戏开始运行");
        }

        if ((gameContext.getGameStatus() != GameStatus.RUNNING && gameContext.getGameStatus() != GameStatus.NOT_START) && gameContext.getPlayers().isEmpty()) {
            gameContext.gameOver();
            System.out.println("游戏结束");
        }

        if (gameContext.getGameStatus() == GameStatus.RUNNING && !gameContext.getPlayers().isEmpty()) {
            long l = System.currentTimeMillis();
            //画布整体元素
            Vector<Element> elements = gameContext.getGameMapContext().getElements();
            //所有元素
            for (int i = 0; i < elements.size(); i++) {

                Element element = elements.get(i);
                //将元素释放的子元素加入地图
                while (element.isJoinMapQueueHasEle()) {
                    Element e = element.releaseElement();

                    gameContext.getGameMapContext().addElement(e);
                }

                //1、移除死亡的元素
                if (!element.isLive()) {
                    if (gameContext.isServer()) {
                        this.commandQueue.addCommand(CommandFactory.createElementUpdateCommand(element, ElementAction.DEAD, 1, 0));
                    }
                    elements.remove(i);
                    i--;
                    gameMapContext().trash(element);
                }
                //2、服务器端控制npc行为
                if (gameContext.isServer() && element.getGameRole() == GameRole.NPC) {
                    Tank t = (Tank) element;
//                    randomRun(t);
                }
            }

            //执行所有指令
            //TODO 服务器在一边接受指令执行指令，会存在如果收到指令太多执行不完的情况，可以设置处理上线，待下一轮在执行
            for (int i = 0; i < commandQueue.commandQueue.size(); i++) {
                Command command = commandQueue.commandQueue.get(i);
                //移动命令的检查
                if (command instanceof CommandFactory.ElementUpdateCommand) {
                    CommandFactory.ElementUpdateCommand c = (CommandFactory.ElementUpdateCommand) command;

                    moveCommandCheck(c);
                }

                Object obj = command.exec();
                if (obj instanceof Command) {
                    commandQueue.addCommand((Command) obj);
                }
            }

            if (counter == 0 && gameContext.isServer()) {
                elementsProto = gameMapContext().toProtoElements();
            }

            if (counter == 5 && gameContext.isServer()) {
                Msg.Builder builder = Msg
                        .newBuilder();
                Vector<awt.proto.command.Command> protoCommands = CommandFactory.toProtoCommands(this.commandQueue.optimizeCommand().commandQueue);

                if (protoCommands.size() > 0) {
                    builder.addAllCommands(protoCommands);
                }

                Msg msg = builder.setHeader(MsgAction.BROADCAST_GAME_MAP)
                        .addAllElements(elementsProto)
                        .setTimeStamp(new Date().getTime())
                        .build();

                msgQueue.add(msg);
                counter = -1;

            }
            if (gameContext.isServer()) {
                this.commandQueue.clearCache();
            }

//            long end = System.currentTimeMillis();
//            long l1 = end - l;
            counter++;
        }
//            System.out.println("调度次数"+counter);
//            System.out.println("循环一次时间"+l1);

    }

    /**
     * @param command
     */
    private void moveCommandCheck(CommandFactory.ElementUpdateCommand command) {
        if (command.getT().isLive()) {
            switch (command.getElementAction()) {
                case MOVE:
                    if (command.getT().isMoved() && checkElementIsCollide(command.getT())) {
                        command.interrupt();
                    }
            }
        } else {
            command.failed();
        }

    }

    public void syncFromProto(List<awt.proto.command.Command> commandList, List<awt.proto.Element> elements) throws Exception {
        GameStatus gameStatus = this.gameContext.getGameStatus();
        this.gameContext.gamePause();
        this.gameMapContext().syncFromProtoElements(elements);

        if (commandList.size() > 0) {
//            this.commandQueue.clearCommandQueue();
            Vector<Command> commands = this.commandQueue.parseProtoCommands(commandList);
            this.commandQueue.addCommands(commands);
        }

        this.gameContext.setGameStatus(gameStatus);
    }


    /**
     * 随机跑
     */
    public void randomRun(Tank tank) {
        //需要系统控制

        if (tank.count == 60) {
            Calendar CalendarCalendar = Calendar.getInstance();
            int seconds = CalendarCalendar.get(Calendar.SECOND);
            if (seconds % 4 == 0) {
                //1-4随机数
                int i = ((int) (Math.random() * 4));
                Direct direct = Direct.valueOf(i);
                if (direct != null) {
                    commandQueue.addCommand(CommandFactory.createRedirectCommand(tank, direct));
                }
            }

            commandQueue.addCommand(CommandFactory.createElementUpdateCommand(tank, ElementAction.MOVE, 60, 0));
            commandQueue.addCommand(CommandFactory.createTankCommand(tank, TankAction.AMMUNITION_LOADING, 1));
//            commandQueue.addTankCommand(tank, TankAction.FIRE, 5);
            tank.count = 0;
        }

        tank.count++;

    }

    public void sendMsg(Channel channel, Msg msg, InetSocketAddress recipient) throws InterruptedException {
        channel.writeAndFlush(new DatagramPacket(
                Unpooled.copiedBuffer(msg.toByteArray()), recipient)).sync();
        System.out.println("服务端消息" + msg);
    }

    /**
     * 处理用户命令
     */
    public void handlerProtoCommands(Channel channel, Msg msgProto, InetSocketAddress recipient) throws InterruptedException {
        switch (msgProto.getHeader()) {
            case LOGIN:
            case GAME_JOIN:
                awt.proto.Player player = msgProto.getPlayer();
                Player p = new Player(player.getName());
                this.gameContext.addPlayer(p);
                this.gameContext.addPlayerChannel(p.getId(), channel);
                awt.proto.Player newPlayer = player.toBuilder().setId(player.getId()).build();
                Msg msg = Msg.newBuilder()
                        .setHeader(MsgAction.GAME_JOIN)
                        .setPlayer(newPlayer)
                        .setTimeStamp(new Date().getTime())
                        .build();
                sendMsg(channel, msg, recipient);
                break;
            case BROADCAST_COMMAND:
                List<awt.proto.command.Command> commandsList = msgProto.getCommandsList();
                Vector<Command> commands = this.commandQueue.parseProtoCommands(commandsList);
                this.commandQueue.addCommands(commands);
        }

    }

    /**
     * @param ip
     */
    public void setPlayerReadyByIp(String ip) {
        gameContext.setPlayerReadyByIp(ip);
    }

    //暂停游戏
    public void pause() {
        gameContext.gamePause();
        System.out.println("游戏暂停中。。。");
    }

    //结束游戏
    public void gameOver() {
        gameContext.gameOver();
    }

    //玩家登陆
    public void login(Player player) {
        gameContext.addPlayer(player);
    }

    //玩家加入
    public void PlayerJoin(Player player, Set<Element> elements, String groupName) {
        gameContext.addPlayerAndEles(player, elements, groupName);
    }

    //获取当前游戏状态
    public GameStatus getGameStatus() {
        return gameContext.getGameStatus();
    }

    public GameContext gameContext() {
        return gameContext;
    }

    public GameMapContext gameMapContext() {
        return gameContext.getGameMapContext();
    }

    //获取玩家状态
    public Object getPlayerInfo(Player player) {
        return null;
    }

    //向所有在线的玩家主机发送当前游戏消息
    public void broadCast() {
    }

    /**
     * 检测一个物体是否与其他物体发生碰撞
     *
     * @param element
     */
    public boolean checkElementIsCollide(Element element) {
        GameMapContext gameMapContext = gameContext.getGameMapContext();
        //元素与其他元素的pk,计算后不可前进的方向
        Set<Direct> canNotMoveDirects = ElementVsOthers(element);
        Direct direct = element.getDirect();

        if (canNotMoveDirects.contains(direct)) {
            return false;
        }
        //可以移动
        if (element.isLive() && element.isMoved()) {
            //判断到达了哪个边界
            Direct reachMapDirect = element.reachMapBorder(0, 0, gameMapContext.getWide(), gameMapContext.getHeight());
            if (reachMapDirect == null || element.getDirect() != reachMapDirect) {
                return true;
            } else {
                element.reachMapBorderAction();
                return false;
            }
        }
        return false;
    }

    /**
     * 元素与其他元素的pk
     *
     * @param element
     * @return
     */
    private Set<Direct> ElementVsOthers(Element element) {
        GameMapContext gameMapContext = gameContext.getGameMapContext();
        Set<Direct> directs = new HashSet<>();
        Vector<Element> elements = gameMapContext.getElements();
        long l = System.currentTimeMillis();
        for (int i = 0; i < gameMapContext.getElements().size(); i++) {
            Element e = elements.get(i);
            seeEachOther(element, e);
            //检测后不可移动的方向集合
            Set<Direct> canNotMove = element.collision(e);
            //同一个玩家id的元素 互相不卡位置
            if (element.getPlayId() == (e.getPlayId())) {
                canNotMove.clear();
            } else if (element.bePassedThrough(e) || e.bePassedThrough(element)) {
                canNotMove.clear();
            } else if (canNotMove != null && canNotMove.size() > 0) {
                //vs系统
//                command.failed();
                vs(element, e);
                directs.addAll(canNotMove);

            }
        }
        long end = System.currentTimeMillis();
//        long l1 = end - l;
//        System.out.println("vs循环一次时间"+l1);
        return directs;
    }

    /**
     * 看见对方
     *
     * @param element
     * @param element2
     */

    private void seeEachOther(Element element, Element element2) {
        int mapXCoordinate = element.getMapXCoordinate();
        int mapYCoordinate = element.getMapYCoordinate();
        int mapXCoordinate1 = element2.getMapXCoordinate();
        int mapYCoordinate1 = element2.getMapYCoordinate();
        double distance = MathUtils.distance(mapXCoordinate, mapYCoordinate, mapXCoordinate1, mapYCoordinate1);
        //当前对象看到其他对象
        if (element.isCanSee() && !element2.isBuild()) {
            if (distance <= element.getViewR()) {
                element.addOtherEleCanSee(element2);
            } else {
                element.removeOtherEleCanSee(element2);
            }
        }
        //其他对象 看到当前对象
        if (element2.isCanSee() && !element.isBuild()) {
            if (distance <= element2.getViewR()) {
                element2.addOtherEleCanSee(element);
            } else {
                element2.removeOtherEleCanSee(element);
            }
        }
    }

    /**
     * PK 系统
     *
     * @param element
     */
    private void vs(Element element, Element element2) {
        if (element.isLive() && element.isCollisionDead() && gameContext.isServer()) {
//            System.out.println(element.getIncreaseId() + "添加死亡命令");
            commandQueue.addCommand(CommandFactory.createElementUpdateCommand(element, ElementAction.DEAD, 1, 0));
            //TODO
//            element.setLive(false);
        }
        if (element2.isLive() && element2.isCollisionDead() && gameContext.isServer()) {
//            System.out.println(element2.getIncreaseId() + "添加死亡命令");
            commandQueue.addCommand(CommandFactory.createElementUpdateCommand(element2, ElementAction.DEAD, 1, 0));
            //TODO
//            element2.setLive(false);
        }
        //硬度比较
        int diffHardness = element.getHardness() - element2.getHardness();
        if (diffHardness > 0 && element2.getDamagePct() * element.getPower() != 0 && gameContext.isServer()) {
            double blood = element2.getBlood() - element2.getDamagePct() * element.getPower();
//            System.out.println(element2.getIncreaseId() + "添加血量+-命令");
            commandQueue.addCommand(CommandFactory.createElementUpdateCommand(element2, ElementAction.BLOOD, 1, (int) blood));

        } else if (diffHardness < 0 && element.getDamagePct() * element2.getPower() != 0 && gameContext.isServer()) {
            double blood = element.getBlood() - element.getDamagePct() * element2.getPower();
//            System.out.println(element.getIncreaseId() + "添加血量+-命令");
            commandQueue.addCommand(CommandFactory.createElementUpdateCommand(element, ElementAction.BLOOD, 1, (int) blood));
        } else if (element2.getDamagePct() * element.getPower() != 0 && gameContext.isServer()) {
            double blood2 = element2.getBlood() - element2.getDamagePct() * element.getPower();
//            System.out.println(element2.getIncreaseId() + "添加血量+-命令");
            commandQueue.addCommand(CommandFactory.createElementUpdateCommand(element2, ElementAction.BLOOD, 1, (int) blood2));
            double blood = element.getBlood() - element.getDamagePct() * element2.getPower();
//            System.out.println(element.getIncreaseId() + "添加血量+-命令");
            commandQueue.addCommand(CommandFactory.createElementUpdateCommand(element, ElementAction.BLOOD, 1, (int) blood));
        }
    }

}
