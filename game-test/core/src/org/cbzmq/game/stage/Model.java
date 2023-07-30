package org.cbzmq.game.stage;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import org.cbzmq.game.Assets;
import org.cbzmq.game.Map;
import org.cbzmq.game.model.Bullet;
import org.cbzmq.game.model.CharacterListener;
import org.cbzmq.game.model.Enemy;
import org.cbzmq.game.model.Player;

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
    Array<CharacterListener> getListeners();
    void addListener(CharacterListener listener);
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
    int generalId();

}
