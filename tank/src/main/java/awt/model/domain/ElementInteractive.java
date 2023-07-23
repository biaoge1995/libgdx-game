package awt.model.domain;

import awt.proto.enums.Direct;

import java.util.Set;
import java.util.Vector;

/**
 * @author chenbiao
 * @date 2020-12-23 23:04
 * 定义元素交互的行为
 */
public interface ElementInteractive {


    boolean isJoinToMap();

    boolean isRemoveOutMap();



    boolean move();

    /**
     * 元素发生交互,是否碰撞逻辑
     * @param other
     * @return 返回当前元素碰撞的其他方向
     */
    Set<Direct> collision(Element other);

    /**
     * 元素与其他元素碰撞时后行为
     */
    void collisionResult();

    /**
     * 爆炸时产生碎片
     * @return
     */
    Vector<Element> boom();

    /**
     * 是否可以被其他元素穿过
     * @return
     */
    boolean bePassedThrough(Element other);

    /**
     * 到达地图边缘
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */

    Direct reachMapBorder(int x1, int y1, int x2, int y2);


    void reachMapBorderAction();



    /**
     * 元素进入其他元素时的行为
     * @param other
     * @return
     */
    void reachIn(Element other);

    /**
     * 元素走出其他元素时的行为
     * @param other
     * @return
     */
    void reachOut(Element other);
}
