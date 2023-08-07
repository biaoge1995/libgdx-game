package org.cbzmq.game.model;

/**
 * @ClassName GameLogicObserver
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/8/4 2:51 下午
 * @Version 1.0
 **/
public interface GameLogicObserver{
    
    /** 出生 */
    public abstract void born (Character character);

    /** 死亡 */
    public abstract void death (Character character, Character killer);

    /** 出生 */
    public abstract void beRemove (Character character);

    /** 被销毁 */
    public abstract void dispose (Character character);

    /** 被伤害者，加害者 */
    public abstract void hit (Character character, Character hitCharacter);

    /** 攻击  */
    public abstract void attack (Character character);

    /** 当前帧结束*/
    public abstract void frameEnd(Iterable<Character> root, float time);
}

