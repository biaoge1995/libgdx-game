package org.cbzmq.game.actor.other;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * @ClassName GameState
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/20 12:20 下午
 * @Version 1.0
 **/

public class GameStage extends Stage {

    private Person person;
    private final MainGame game;
    private SpinePerson spinePerson;
    private Info info;

    public GameStage(MainGame game) {
        super(new StretchViewport(game.getWorldWidth(), game.getWorldHeight()));

        this.game = game;

        init();
    }

    public void init() {
        AssetManager assetManager = game.getAssetManager();
        person = new Person(assetManager);
        spinePerson = new SpinePerson(assetManager);
        info = new Info(assetManager.get(Res.FPS_BITMAP_FONT_PATH, BitmapFont.class), 24);
        spinePerson.setPosition(this.getWidth() / 3, this.getHeight() / 3);
        float x = this.getWidth() / 2;
        float y = this.getHeight() / 2;
        person.setX(x);
        person.setY(y);
        person.setOrigin(Align.center);
        person.setScale(1.2F);
        this.addActor(person);
        this.addActor(spinePerson);
        this.addActor(info);
    }

    private boolean isJumpKeyPressed = false;
    private boolean isRunPressed = false;
    private boolean isShootKeyPressed = false;
    private boolean isWalkKeyPressed = false;
    private boolean isIdle=false;

    @Override
    public void act(float delta) {
        super.act(delta);
        info.setText("jump:"+spinePerson.isJumping());
        info.setInfos(spinePerson.getDebugInfos());

        if (isJumpKeyPressed) {
            System.out.println("跳");
            spinePerson.action(SpinePerson.Action.JUMP,true);
        }
//        else if (isShootKeyPressed) {
//            System.out.println("开火");
//            spinePerson.action(SpinePerson.Action.SHOOT,true);
//        }
        else if (isWalkKeyPressed) {
            System.out.println("走");
            spinePerson.action(SpinePerson.Action.WALK,true);
        } else if (isRunPressed) {
            System.out.println("跑");
            spinePerson.action(SpinePerson.Action.RUN,true);
        }


    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Gdx.app.log(MainGame.TAG, "touchDown screenX:" + screenX + " screenY:" + screenY);
        Gdx.app.log(MainGame.TAG, "stage width:" + getWidth() + " height:" + getHeight());
        Gdx.app.log(MainGame.TAG, "camera position:" + getCamera().position + " viewportWidth:" + getCamera().viewportWidth + "viewportHeight:" + getCamera().viewportHeight);
        person.setPosition(Gdx.input.getX(), getCamera().viewportHeight - Gdx.input.getY());
//        MoveToAction moveToAction = new MoveToAction();
//        moveToAction.setX(screenX);
//        moveToAction.setY(getCamera().viewportHeight-screenY);
//        moveToAction.setDuration(1);
//        person.addAction(moveToAction);
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return super.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.SPACE) {
            isJumpKeyPressed = true;
        } else if (keyCode == Input.Keys.ENTER) {
            isShootKeyPressed = true;
            spinePerson.action(SpinePerson.Action.SHOOT,false);
        }
        if (keyCode == Input.Keys.D) {
            isWalkKeyPressed = true;
        } else if (keyCode == Input.Keys.SHIFT_LEFT) {
            isRunPressed = true;
        }

        return true;
    }



    @Override
    public boolean keyUp(int keyCode) {
        if (keyCode == Input.Keys.SPACE) {
            isJumpKeyPressed = false;
            spinePerson.action(SpinePerson.Action.IDLE,false);
        } else if (keyCode == Input.Keys.ENTER) {
            isShootKeyPressed = false;
//            spinePerson.action(SpinePerson.Action.IDLE,false);
        }
        if (keyCode == Input.Keys.D) {
            isWalkKeyPressed = false;
            spinePerson.action(SpinePerson.Action.IDLE,false);
        } else if (keyCode == Input.Keys.SHIFT_LEFT) {
            isRunPressed = false;
            spinePerson.action(SpinePerson.Action.IDLE,false);
        }
        return true;
    }

}
