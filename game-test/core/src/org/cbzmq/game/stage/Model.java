package org.cbzmq.game.stage;

import com.badlogic.gdx.utils.Array;
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
    Array<Observer> getListeners();
    void addListener(Observer listener);
    Array<Bullet> getBullets();
    Array<Enemy> getEnemies();
    Assets getAssets();
    void update(float delta);
    float getTimeScale();
    void setTimeScale(float timeScale);
    void restart();

    EventQueue queue();

    boolean isPlayerWin();

    boolean isGameOver();

    Array<Character> getAll();

}
