package org.cbzmq.game.stage;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import org.cbzmq.game.Assets;
import org.cbzmq.game.Map;
import org.cbzmq.game.model.*;
import org.cbzmq.game.model.Character;

/**
 * @ClassName Model
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/27 4:12 下午
 * @Version 1.0
 **/
public interface Model {
    Player getPlayer();
    Map getMap();
    void addListener(Observer listener);
    Array<Enemy> getEnemies();
    Assets getAssets();
    void update(float delta);
    float getTimeScale();
    void setTimeScale(float timeScale);
    void restart();

    boolean isPlayerWin();

    boolean isGameOver();

    Array<Character> getAll();

    Array<Observer> getListeners();
    EventQueue getQueue();

}
