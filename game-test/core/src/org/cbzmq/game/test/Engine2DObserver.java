package org.cbzmq.game.test;

import com.badlogic.gdx.math.Rectangle;
import org.cbzmq.game.model.Character;
import org.cbzmq.game.model.Event;


/**
 * @ClassName Engine2DObserver
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/8/7 6:12 下午
 * @Version 1.0
 **/
public interface Engine2DObserver{
    /**现在只处理2d引擎的功能**/

    /** 碰撞到了地图  */
    public abstract void collisionMap (Character Character, Rectangle tile);

    /** 碰撞到了其他角色  */
    public abstract void collisionObserver (Character Character, Character other);

    /** 事件 **/
    public abstract void event (Character Character, Event.OneCharacterEvent event);

    public abstract void eventOneObserver (Event.OneCharacterEvent event);

    public abstract void eventTwoObserver (Event.TwoObserverEvent event);
}
