package org.cbzmq.game.logic;
import com.badlogic.gdx.utils.ObjectFloatMap;
import com.esotericsoftware.spine.Animation;
/**
 * @ClassName StateView
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/24 11:07 下午
 * @Version 1.0
 **/




/** Stores information needed by the view for a character state. */
public  class StateAnimation {
    public Animation animation;
    public boolean loop;
    // Controls the start frame when changing from another animation to this animation.
    public ObjectFloatMap<Animation> startTimes = new ObjectFloatMap();
    public float defaultStartTime;
}
