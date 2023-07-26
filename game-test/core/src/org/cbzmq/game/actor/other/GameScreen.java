package org.cbzmq.game.actor.other;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;

/**
 * @ClassName GameScreen
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/20 2:12 下午
 * @Version 1.0
 **/

public class GameScreen extends ScreenAdapter {

    GameStage gameStage;

    public GameScreen(MainGame game) {
        this.gameStage = new GameStage(game);
        Gdx.input.setInputProcessor(gameStage);
    }

    @Override
    public void render(float delta) {
        gameStage.act(delta);
        gameStage.draw();
    }


}
