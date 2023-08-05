package org.cbzmq.game.model;

import com.badlogic.gdx.math.Rectangle;
import com.esotericsoftware.spine.Event;
import org.cbzmq.game.stage.Model;

/**
 * @ClassName Observe
 * @Description 多线程观察者
 * @Author chenbiao
 * @Date 2023/8/4 2:39 下午
 * @Version 1.0
 **/
public abstract class Observer<T extends Observer> {
    //任务中心
    protected World world;
    public abstract void update(float delta);

    public Observer() {
    }
    /** Invoked when this entry has been set as the current entry. */

    /** 出生 */
    public abstract void born (T observer);

    /** 被伤害者，加害者 */
    public abstract void hit (T Observer,T hitObserver);

    /** 死亡 */
    public abstract void death (T Observer,T killer);

    /** 出生 */
    public abstract void beRemove (T Observer);

    /** 被销毁 */
    public abstract void dispose (T Observer);

    /** 碰撞到了地图  */
    public abstract void collisionMap (T Observer, Rectangle tile);

    /** 碰撞到了其他角色  */
    public abstract void collisionObserver (T Observer,T other);


    /** 攻击  */
    public abstract void attack (T Observer);

    /** 事件 **/
    public abstract void event (T Observer, Event event);

    public abstract void frameEnd(T root,float time);
    
}

