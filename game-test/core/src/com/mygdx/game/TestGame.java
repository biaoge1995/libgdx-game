package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TestGame extends ApplicationAdapter {

    Stage stage;

    Skin skin;


    @Override
    public void create() {
        skin =  new Skin();
        skin.add("but_up",new Texture(Gdx.files.internal("but.png")));
        skin.add("but_down",new Texture(Gdx.files.internal("but_down.jpg")));
        skin.add("action",new Texture(Gdx.files.internal("badlogic.jpg")));

        stage = new Stage();
        Button button = new Button(
                new Drawable() {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                Texture but = skin.get("but_up", Texture.class);
                batch.draw(but, x, y);
            }

            @Override
            public float getLeftWidth() {
                return 0;
            }

            @Override
            public void setLeftWidth(float leftWidth) {

            }

            @Override
            public float getRightWidth() {
                return 0;
            }

            @Override
            public void setRightWidth(float rightWidth) {

            }

            @Override
            public float getTopHeight() {
                return 0;
            }

            @Override
            public void setTopHeight(float topHeight) {

            }

            @Override
            public float getBottomHeight() {
                return 0;
            }

            @Override
            public void setBottomHeight(float bottomHeight) {

            }

            @Override
            public float getMinWidth() {
                return 0;
            }

            @Override
            public void setMinWidth(float minWidth) {

            }

            @Override
            public float getMinHeight() {
                return 0;
            }

            @Override
            public void setMinHeight(float minHeight) {

            }
        }
        , new Drawable() {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                Texture but = skin.get("but_down", Texture.class);
                batch.draw(but, x, y);
            }

            @Override
            public float getLeftWidth() {
                return 0;
            }

            @Override
            public void setLeftWidth(float leftWidth) {

            }

            @Override
            public float getRightWidth() {
                return 0;
            }

            @Override
            public void setRightWidth(float rightWidth) {

            }

            @Override
            public float getTopHeight() {
                return 0;
            }

            @Override
            public void setTopHeight(float topHeight) {

            }

            @Override
            public float getBottomHeight() {
                return 0;
            }

            @Override
            public void setBottomHeight(float bottomHeight) {

            }

            @Override
            public float getMinWidth() {
                return 0;
            }

            @Override
            public void setMinWidth(float minWidth) {

            }

            @Override
            public float getMinHeight() {
                return 0;
            }

            @Override
            public void setMinHeight(float minHeight) {

            }
        });
        button.setX(55);
        button.setY(55);
        stage.addActor(button);
        Gdx.input.setInputProcessor(stage);

        // 构建纹理
        TextureRegion action = skin.getRegion("action");
//        action.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        // 构建图像精灵
        Image img = new Image( action);
        img.setWidth(100);
        img.setHeight(100) ;
        img.setX(100);
        img.setY(100);

        ScaleToAction scaleToAction = new ScaleToAction();
        scaleToAction.setScale(1,1);
        ScaleToAction scaleToAction2 = new ScaleToAction();
        scaleToAction2.setScale(0.8f,0.8f);
        MoveToAction move = new MoveToAction();
        move.setPosition(200,200);
        img.addAction(scaleToAction);
        img.addAction(scaleToAction2);
        img.addAction(move);
        stage.addActor(img);



    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        stage.act(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
