package awt.game;

import awt.model.domain.*;
import awt.proto.enums.GameRole;
import awt.proto.enums.GameStatus;
import lombok.Data;
import io.netty.channel.Channel;
import java.util.*;

/**
 * @ClassName GameContext
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/6/27 6:22 下午
 * @Version 1.0
 **/
@Data
public class GameContext {
    //玩家列表
    private final ArrayList<Player> players = new ArrayList<>();
    //ip to player
    private final Map<String, Player> ip2player = new HashMap<>();

    //玩家的通信渠道
    private final Map<Integer, Channel> playerChannels = new HashMap<>();

    //阵营
    private final HashMap<String,Group> groupMap = new HashMap<>();
    //游戏状态
    private GameStatus gameStatus = GameStatus.NOT_START;
    //游戏地图
    private GameMapContext gameMapContext;
    //是否为服务器端
    private boolean isServer;


    public GameContext(boolean isServer) {
        this.isServer = isServer;
    }

    public void addPlayerChannel(int playerId,Channel channel){
        playerChannels.put(playerId,channel);
    }

    public Collection<Channel> getAllPlayerChannels(){
        return playerChannels.values();
    }

    public Channel getPlayerChannel(int playerId){
        return playerChannels.get(playerId);
    }

    /**
     * 加入玩家
     *
     * @param player
     */
    public void addPlayer(Player player) {
        players.add(player);
        player.setId(players.size()+1);
        ip2player.put(player.getIp(), player);
        Set<Element> tanks = new HashSet<>();
        Tank tank = new Tank(gameMapContext.getWide() / 2, gameMapContext.getHeight()/2);
        tank.setGameRole(player.getId()==0?GameRole.NPC:GameRole.PLAYER);
        tanks.add(tank);
        addPlayerAndEles(player,tanks,"玩家阵营");
    }



    /**
     * 通过主机ip地址设置玩家是否准备状态
     * @param ip
     */
    public void setPlayerReadyByIp(String ip) {
        Player player = ip2player.get(ip);

        if (player != null)
            player.setReady(true);
    }




    /**
     * 添加玩家以及
     * @param player
     * @param elements
     */
    public void addPlayerAndEles(Player player, Set<Element> elements,String groupName) {
        gameMapContext.putPlayerElement(player.getId(), elements);
        players.add(player);
        if(groupMap.containsKey(groupName)){
            Group group = groupMap.get(groupName);
            group.addPlayerAndElement(player,elements);
        }else {
            Group group = new Group(groupName);
            groupMap.put(groupName,group);
        }
        //给元素负上操作者id
        for (Element element : elements) {
            element.setPlayId(player.getId());
        }
        gameMapContext.addElements(elements);
    }

    public void gameLoading() {
        this.gameStatus = GameStatus.LOADING;
    }

    public void gameRunning() {
        this.gameStatus = GameStatus.RUNNING;
    }

    public void gameReady() {
        this.gameStatus = GameStatus.READY;
    }

    public void gamePause() {
        this.gameStatus = GameStatus.PAUSE;
    }

    public void gameOver() {
        this.gameStatus = GameStatus.GAME_OVER;
    }
}
