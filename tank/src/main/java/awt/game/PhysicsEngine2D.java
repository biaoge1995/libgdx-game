package awt.game;

import awt.model.domain.Element;
import awt.proto.enums.Direct;
import awt.proto.enums.ElementAction;
import awt.utils.MathUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * @ClassName PhysicsEngine2D
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/7 3:53 下午
 * @Version 1.0
 **/
public class PhysicsEngine2D {


    private final int wide;
    private final int height;

    public PhysicsEngine2D(int wide, int height) {
        this.wide = wide;
        this.height = height;
    }



    /**
     * 检测一个物体是否与其他物体发生碰撞
     *
     * @param element
     */
    private boolean checkElementIsCollide(Element element,Element other) {

        Set<Direct> canNotMove = element.collision(other);
        //同一个玩家id的元素 互相不卡位置
        if (element.getPlayId() == (other.getPlayId())) {
            canNotMove.clear();
        } else if (element.bePassedThrough(other) || other.bePassedThrough(element)) {
            canNotMove.clear();
        } else if (canNotMove != null && canNotMove.size() > 0) {
            return true;

        }
        return false;
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


}
