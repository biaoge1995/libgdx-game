package awt.game;

import awt.model.Constans;
import awt.model.domain.Element;
import awt.model.msg.TankActionMsg;
import awt.proto.enums.GameRole;
import awt.utils.LoadClassUtils;
import lombok.Data;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * @author chenbiao
 * @date 2020-12-19 12:34
 * 游戏地图 地图为xy坐标轴的第四象限
 */
@Data
public class GameMapContext {
    private int height = 2000;//长
    private int wide = 2000;
    //玩家控制角色
    private final Map<Integer, Set<Element>> playerAndEle = new HashMap<>();

    private Vector<Element> elements = new Vector<Element>();
    //垃圾回收站
    private Vector<Element> trash = new Vector<Element>();

    private Map<Integer, Element> elementMap = new HashMap<>();
    private Map<String, TankActionMsg> idToAction;
    private Map<Integer, Element> playerElement = new HashMap<>();
    //元素当前最大id
    private int elementMaxId = 0;
    private Vector<Vector<Element>> eleMap;

    private GameContext gameContext;

    //是否开启迷雾模式
    private boolean isDenseFog = false;
    //默认玩家为系统
    private final int systemPlayId = 0;

    public GameMapContext() {
    }

    public Set<Element> getPlayerElement(int playerId) {
        return playerAndEle.get(playerId);
    }

    public Set<Element> putPlayerElement(int playerId, Set<Element> elements) {
        return playerAndEle.put(playerId, elements);
    }

    /**
     * 垃圾回收
     *
     * @param e
     */
    public void trash(Element e) {
        trash.add(e);
    }

    public void trash(Collection<Element> e) {
        trash.addAll(e);
    }

    public Element getElementByIncreaseId(int increaseId) {
        Element element = elementMap.get(increaseId);
        if (element == null) {
            System.out.println("无法找到该元素" + increaseId);
        }
        Objects.requireNonNull(element);

        return element;
    }


    /**
     * @param protoElement
     */
    public void syncFromProtoElement(awt.proto.Element protoElement) throws Exception {

        if (!elementMap.containsKey(protoElement.getIncreaseId())) {
            if (protoElement.getIncreaseId() < elementMaxId) {
                throw new Exception("increaseId error elementMaxId is" + elementMaxId + " but element increaseId is" + protoElement.getIncreaseId());
            } else {
                //TODO
                //服务端消息中的元素 在客户端中不存在如何按照消息创建一个元素
                Element newElement = LoadClassUtils.loadElementClass(protoElement.getElementType()
                        , new Class<?>[]{int.class, int.class}
                        , protoElement.getMapXCoordinate(), protoElement.getMapYCoordinate());
                addElement(newElement, protoElement.getIncreaseId());

            }
        } else {
            Element clientElement = elementMap.get(protoElement.getIncreaseId());
            switch (clientElement.getGameRole()) {
                case SCENE:
                    clientElement.setLive(protoElement.getIsLive());
                    break;
                case PLAYER:
                case NPC:
                    clientElement.setMoved(protoElement.getIsMoved());
                    clientElement.setMapXCoordinate(protoElement.getMapXCoordinate());
                    clientElement.setMapYCoordinate(protoElement.getMapYCoordinate());
                    clientElement.setXCoordinate(protoElement.getXCoordinate());
                    clientElement.setYCoordinate(protoElement.getYCoordinate());
                    clientElement.setLive(protoElement.getIsLive());
                    clientElement.setBlood(clientElement.getBlood());
                    clientElement.setDistance(clientElement.getDistance());
                    break;
            }
        }
    }


    /**
     * @param protoElements
     */
    public void syncFromProtoElements(List<awt.proto.Element> protoElements) throws Exception {
        for (int i = 0; i < protoElements.size(); i++) {
            syncFromProtoElement(protoElements.get(i));
        }
    }

    /**
     * 将所有元素转化为用于通信的proto
     *
     * @return
     */
    public Vector<awt.proto.Element> toProtoElements() {
        Vector<awt.proto.Element> protoElements = new Vector<>();
        Vector<Element> all = new Vector<>();
        all.addAll(elements);
        all.addAll(trash);

        for (int i = 0; i < all.size(); i++) {
            Element e = all.get(i);
            //找到哪些自loading后属性发生过变化的元素
            if (e.isUpdate()) {
                awt.proto.Element.Builder builder = awt.proto.Element.newBuilder();
                builder.setIsLive(e.isLive());
                builder.setIncreaseId(e.getIncreaseId());
                switch (e.getGameRole()) {
                    case SCENE:
                        protoElements.add(builder.build());
                        break;
                    case NPC:
                    case PLAYER:
                        builder.setPlayerId(e.getPlayId())
                                .setElementType(e.getElementType())
                                .setIsMoved(e.isMoved())
                                .setDistance(e.getDistance())
                                .setMapXCoordinate(e.getMapXCoordinate())
                                .setMapYCoordinate(e.getMapYCoordinate())
                                .setBlood((int) e.getBlood())
                                .setIsLive(e.isLive())
                                .setDirect(e.getDirect())
                                .setIncreaseId(e.getIncreaseId())
                                .setXCoordinate(e.getXCoordinate())
                                .setYCoordinate(e.getYCoordinate());
                        protoElements.add(builder.build());
                        break;
                }
            }
        }
        return protoElements;
    }

    /**
     * 开启迷雾模式设置
     *
     * @param groupName 玩家所属阵营
     */

    public void setDenseFog(String groupName) {

    }

    /**
     * 画出地图
     *
     * @param g
     */
    public void draw(Graphics g, int drawX, int drawY, int drawWidth, int drawHeight) {
        Set<Element> canSeeOtherElement = null;
        Element playElement = null;
        //开启玩家视角
        if (isDenseFog) {

            playElement = playerElement.get(systemPlayId);

            if (playElement != null) {
                canSeeOtherElement = playElement.getCanSeeOtherElement();
                int xCoordinate = playElement.getMapXCoordinate() - playElement.getViewR() - drawX;
                int yCoordinate = playElement.getMapYCoordinate() - playElement.getViewR() - drawY;
                if (playElement.isLive()) {
                    g.setColor(Color.GRAY);
                    g.fillOval(xCoordinate, yCoordinate, 2 * playElement.getViewR(), 2 * playElement.getViewR());

                } else {

                }
            }
        }

        if (this.getElements() != null) {
            Collections.sort(elements);
            for (int i = 0; i < elements.size(); i++) {
                Element e = elements.get(i);
                int mapXCoordinate = e.getMapXCoordinate();
                int mapYCoordinate = e.getMapYCoordinate();


                e.setXCoordinate(mapXCoordinate - drawX);
                e.setYCoordinate(mapYCoordinate - drawY);

//                e.draw(g);
                //玩家自己
                if (playElement != null && e == playElement) {
                    e.draw(g);
                }
                //建筑物
                else if (e.isBuild()) {
                    e.draw(g);
                }
                //视野范围内的活物体
                else if (!isDenseFog || (canSeeOtherElement != null && canSeeOtherElement.contains(e))) {
                    e.draw(g);
                }


            }

        }
    }

    /**
     * 计算元素到某一地点的最短路径
     *
     * @param element
     * @param x
     * @param y
     */
    public void minDistance(Element element, int x, int y) {
        //每秒钟的步进
        long step = element.getStep() * 60 / Constans.time;


    }

    public Vector<Element> getElements() {
        return elements;
    }

    public void addElements(Collection<Element> elements) {
        Iterator<Element> iterator = elements.iterator();
        while (iterator.hasNext()) {
            addElement(iterator.next());
        }
    }

    public void addElement(Element e, int increaseId) {

        e.setIncreaseId(increaseId);
        if (increaseId > 0) {
            e.setGameRole(GameRole.PLAYER);
        }
        elementMap.put(increaseId, e);
        elements.add(e);
        setElementMaxId(increaseId);
    }

    public void addElement(Element e) {
        if (!elementMap.containsKey(e.getIncreaseId())) {
            elements.add(e);
            int increaseId = increaseId();
            e.setIncreaseId(increaseId);
            elementMap.put(increaseId, e);
        } else {
            System.out.println(e.getIncreaseId() + "号元素已经存在");
        }

    }

    /**
     * 获取自增id
     *
     * @return
     */
    synchronized public int increaseId() {
        setElementMaxId(elementMaxId + 1);
        return elementMaxId;
    }

    synchronized public void setElementMaxId(int increaseId) {
        this.elementMaxId = increaseId;
    }


}



