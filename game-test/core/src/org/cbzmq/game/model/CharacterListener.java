package org.cbzmq.game.model;

import com.badlogic.gdx.math.Rectangle;
import com.esotericsoftware.spine.Event;

/**
 * @ClassName ModelListener
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/27 12:44 下午
 * @Version 1.0
 **/
public interface CharacterListener {
    /** Invoked when this entry has been set as the current entry. */

    /** 出生 */
    public void born (Character character);

    /** 被伤害者，加害者 */
    public void hit (Character character,Character hitCharacter);

    /** 死亡 */
    public void death (Character character);

    /** 出生 */
    public void beRemove (Character character);

    /** 被销毁 */
    public void dispose (Character character);

    /** 碰撞到了地图  */
    public void collisionMap (Character character, Rectangle tile);

    /** 碰撞到了其他角色  */
    public void collisionCharacter (Character character,Character other);

    /** 攻击  */
    public void attack (Character character);

    /** 事件 **/
    public void event (Character character, Event event);


}
