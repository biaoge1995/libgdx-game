package org.cbzmq.game.model;

import com.badlogic.gdx.utils.Disposable;

/**
 * @ClassName Pause
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/8/2 10:21 上午
 * @Version 1.0
 **/
public interface Pause extends Disposable {
    void init();
    void comeOnStage();
    void update();
    void sleep();
}
