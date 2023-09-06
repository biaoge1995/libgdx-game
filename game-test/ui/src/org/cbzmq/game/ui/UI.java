/******************************************************************************
 * Spine Runtimes Software License
 * Version 2.1
 *
 * Copyright (c) 2013, Esoteric Software
 * All rights reserved.
 *
 * You are granted a perpetual, non-exclusive, non-sublicensable and
 * non-transferable license to install, execute and perform the Spine Runtimes
 * Software (the "Software") solely for internal use. Without the written
 * permission of Esoteric Software (typically granted by licensing Spine), you
 * may not (a) modify, translate, adapt or otherwise create derivative works,
 * improvements of the Software or develop new applications using the Software
 * or (b) remove, delete, alter or obscure any trademarks or any copyright,
 * trademark, patent or other intellectual property or proprietary rights notices
 * on or in the Software, including any copy thereof. Redistributions in binary
 * or source form must include this license and terms.
 *
 * THIS SOFTWARE IS PROVIDED BY ESOTERIC SOFTWARE "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL ESOTERIC SOFTARE BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *****************************************************************************/

package org.cbzmq.game.ui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.esotericsoftware.spine.SkeletonRendererDebug;
import org.cbzmq.game.logic.Assets;
import org.cbzmq.game.model.MyVector2;
import org.cbzmq.game.model.Player;
import org.cbzmq.game.logic.Constants;
import org.cbzmq.game.logic.AbstractLogicEngine;

/**
 * The user interface displayed on top of the game (menu, health bar, splash screens).
 */
public class UI extends Stage {
    static final Color gray = new Color(0.15f, 0.15f, 0.15f, 1);
    View view;
    AbstractLogicEngine abstractLogicEngine;
    ShapeRenderer shapes;
    SkeletonRendererDebug skeletonRendererDebug;
    Skin skin;
    Label fpsLabel, bindsLabel,delayLabel,packagePer;
    TextButton debugButton, zoomButton, bgButton;
    TextButton speed200Button, speed150Button, speed100Button, speed33Button, speed15Button, speed3Button, pauseButton;
    public Table splashTable;
    Image splashImage, splashTextImage;
    ProgressBar healthBar;
    TextButton fullscreenButton, restartButton, menuButton;
    Table menu;
    Vector2 temp = new Vector2();

    int windowWidth, windowHeight;
    public float inputTimer;
    public boolean hasSplash;

    private float cameraZoom = 1;

    UI(final View view) {
        super(new ScreenViewport());
        this.view = view;
        this.abstractLogicEngine = view.abstractLogicEngine;

        shapes = new ShapeRenderer();

        skeletonRendererDebug = new SkeletonRendererDebug(shapes);
        skeletonRendererDebug.setScale(Constants.scale);
        // skeletonRendererDebug.setPremultipliedAlpha(true);

        loadSkin();
        create();
        layout();
        events();

        showSplash(view.assets.titleRegion, view.assets.startRegion);
    }


    public void create() {
        speed200Button = speedButton(2f);
        speed150Button = speedButton(1.5f);
        speed100Button = speedButton(1);
        speed33Button = speedButton(0.33f);
        speed15Button = speedButton(0.15f);
        speed3Button = speedButton(0.03f);
        pauseButton = speedButton(0);
        pauseButton.setText("Pause");
        new ButtonGroup(speed200Button, speed150Button, speed100Button, speed33Button, speed15Button, speed3Button, pauseButton);
        speed100Button.setChecked(true);

        healthBar = new ProgressBar(0, Player.hpStart, 1, false, skin);
        healthBar.setAnimateDuration(0.3f);
        healthBar.setAnimateInterpolation(Interpolation.fade);
        fpsLabel = new Label("", skin);
        delayLabel = new Label("", skin);
        bindsLabel = new Label("", skin);
        packagePer =  new Label("", skin);
        debugButton = button("Debug", true);
        zoomButton = button("Zoom", true);
        bgButton = button("Background", true);
        bgButton.setChecked(true);

        fullscreenButton = button("Fullscreen", true);

        menuButton = button("Menu", true);
        menuButton.getColor().a = 0.3f;

        splashImage = new Image();
        splashImage.setScaling(Scaling.fit);

        splashTextImage = new Image();
        splashTextImage.addAction(Actions.forever(Actions.sequence(Actions.fadeOut(0.4f, Interpolation.pow2In), Actions.fadeIn(0.4f, Interpolation.pow2Out))));
    }

    private void layout() {
        Table buttons = new Table();
        buttons.defaults().uniformX().fillX();
        buttons.add(speed200Button).row();
        buttons.add(speed150Button).row();
        buttons.add(speed100Button).row();
        buttons.add(speed33Button).row();
        buttons.add(speed15Button).row();
        buttons.add(speed3Button).row();
        buttons.add(pauseButton).row();
        buttons.defaults().padTop(5);
        buttons.add(debugButton).row();
        buttons.add(zoomButton).row();
        buttons.add(bgButton).row();
        buttons.add(fullscreenButton).row();
        buttons.add(restartButton).row();

        menu = new Table(skin);
        menu.defaults().space(5);
        menu.add("FPS:");
        menu.add(fpsLabel).expandX().left().row();
        menu.add("Binds:");
        menu.add(bindsLabel).left().row();
        menu.add("Delay:");
        menu.add(delayLabel).left().row();
        menu.add("Package/s:");
        menu.add(packagePer).left().row();
        menu.add(buttons).colspan(2).left();
        menu.setVisible(false);

        Table root = new Table(skin);
        this.addActor(root);
        root.top().left().pad(5).defaults().space(5);
        root.setFillParent(true);
        root.add(menuButton).fillX();
        root.add(healthBar).height(10).fillY().expandX().right().top().row();
        root.add(menu);

        splashTable = new Table(skin);
        splashTable.setFillParent(true);
        splashTable.add(splashImage).fillX().row();
        splashTable.add(splashTextImage);
        splashTable.setTouchable(Touchable.enabled);
    }

    private void events() {
        menuButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                menu.clearActions();
                menu.getColor().a = menu.isVisible() ? 1 : 0;
                if (menu.isVisible())
                    menu.addAction(Actions.sequence(Actions.alpha(0, 0.5f, Interpolation.fade), Actions.hide()));
                else
                    menu.addAction(Actions.sequence(Actions.show(), Actions.alpha(1, 0.5f, Interpolation.fade)));
                menuButton.getColor().a = menu.isVisible() ? 0.3f : 1;
            }
        });

        fullscreenButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                toggleFullscreen();
            }
        });

        restartButton = button("Restart", false);
        restartButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                view.gameRestart();
            }
        });

        splashTable.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (hasSplash && inputTimer < 0) {
                    view.gameRestart();
                    splashTable.clearActions();
                    splashTable.getColor().a = 1;
                    splashTable.addAction(Actions.sequence(Actions.fadeOut(1, Interpolation.fade), Actions.removeActor()));
                    hasSplash = false;
                    return true;
                }
                return false;
            }
        });
    }

    public void loadSkin() {
        skin = new Skin();
        skin.add("default", new BitmapFont());

        Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", new Color(0x416ba1ff));
        textButtonStyle.over = skin.newDrawable("white", Color.GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

        textButtonStyle = new TextButtonStyle(textButtonStyle);
        textButtonStyle.checked = skin.newDrawable("white", new Color(0x5287ccff));
        skin.add("toggle", textButtonStyle);

        LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = skin.getFont("default");
        skin.add("default", labelStyle);

        ProgressBarStyle progressBarStyle = new ProgressBarStyle();
        progressBarStyle.background = skin.newDrawable("white", new Color(0.25f, 0.25f, 0.25f, 0.66f));
        progressBarStyle.background.setMinHeight(15);
        progressBarStyle.knobBefore = skin.newDrawable("white", Color.CLEAR);
        progressBarStyle.knobBefore.setMinHeight(15);
        progressBarStyle.knobAfter = skin.newDrawable("white", new Color(1, 0, 0, 0.66f));
        progressBarStyle.knobAfter.setMinHeight(15);
        skin.add("default-horizontal", progressBarStyle);
    }

    public TextButton speedButton(final float speed) {
        final TextButton button = button((int) (speed * 100) + "%", true);
        button.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                if (button.isChecked()) abstractLogicEngine.setTimeScale(speed);
            }
        });
        return button;
    }

    public TextButton button(String text, boolean toggle) {
        TextButton button = new TextButton(text, skin, toggle ? "toggle" : "default");
        button.pad(2, 12, 2, 12);
        return button;
    }

    @Override
    public void draw() {

//        shapes.setTransformMatrix(view.batch.getTransformMatrix());
//        shapes.setProjectionMatrix(view.batch.getProjectionMatrix());
//        for (Actor child : view.getRoot().getChildren()) {
//            ((BaseSkeletonActor) child).drawDebug(skeletonRendererDebug);
//        }

//        如果开启debug模式
        if (!hasSplash && debugButton.isChecked()) {
            shapes.setTransformMatrix(view.batch.getTransformMatrix());
            shapes.setProjectionMatrix(view.batch.getProjectionMatrix());
            for (Actor child : view.getRoot().getChildren()) {
                ((BaseSkeletonActor)child).drawDebug(skeletonRendererDebug);
            }
        }
        //正常模式
        this.getViewport().apply(true);
        super.draw();
        Batch batch = getBatch();
        batch.setColor(Color.WHITE);

        if(view.player!=null){
            batch.begin();
            int y = Gdx.input.getY();
            int x = Gdx.input.getX();
            Vector2 cursor = this.screenToStageCoordinates(temp.set(x,y));
            MyVector2 aimPoint = view.player.getAimPoint();
            Vector2 screen = new Vector2(aimPoint.x,aimPoint.y);
            screen = view.getViewport().project(screen);
            TextureRegion crosshair = view.assets.crosshair;
            batch.draw(crosshair, screen.x - crosshair.getRegionWidth() / 2, screen.y - crosshair.getRegionHeight() / 2 + 2);
            batch.end();

        }

    }

    @Override
    public void act(float delta) {
        render();
        super.act(delta);
    }

    void render() {
        float delta = Gdx.graphics.getDeltaTime();
        inputTimer -= delta;

//		 cameraZoom = zoomButton.isChecked() ? GameCamera.cameraZoom : 1;
        if (view.camera.zoom != cameraZoom) {
            if (view.camera.zoom < cameraZoom)
                view.camera.zoom = Math.min(cameraZoom, view.camera.zoom + GameCamera.cameraZoomSpeed * delta);
            else
                view.camera.zoom = Math.max(cameraZoom, view.camera.zoom - GameCamera.cameraZoomSpeed * delta);
            view.viewport.setMinWorldWidth(GameCamera.cameraMinWidth * view.camera.zoom);
            view.viewport.setMinWorldHeight(GameCamera.cameraHeight * view.camera.zoom);
            view.viewport.setMaxWorldWidth(GameCamera.cameraMaxWidth * view.camera.zoom);
            view.viewport.setMaxWorldHeight(GameCamera.cameraHeight * view.camera.zoom);
            view.viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        if (!bgButton.isChecked()) {
            shapes.setTransformMatrix(view.batch.getTransformMatrix());
            shapes.setProjectionMatrix(view.batch.getProjectionMatrix());
            shapes.setColor(gray);
            shapes.begin(ShapeType.Filled);
            float w = view.viewport.getWorldWidth(), h = view.viewport.getWorldHeight();
            int x = (int) (view.camera.position.x - w / 2), y = (int) (view.camera.position.y - h / 2);
            for (Rectangle rect : abstractLogicEngine.getMap().getCollisionTiles(x, y, x + (int) (w + 0.5f), y + (int) (h + 0.5f))) {
                shapes.rect(rect.x, rect.y, rect.width, rect.height);
            }
            shapes.end();
        }

        healthBar.setValue(Player.hpStart - ((abstractLogicEngine.getPlayerA()==null)?0: abstractLogicEngine.getPlayerA().hp));

        SpriteCache spriteCache = view.mapRenderer.getSpriteCache();
        int renderCalls = view.batch.totalRenderCalls + spriteCache.totalRenderCalls;
        view.batch.totalRenderCalls = 0;
        spriteCache.totalRenderCalls = 0;
        fpsLabel.setText(Integer.toString(Gdx.graphics.getFramesPerSecond()));
        bindsLabel.setText(Integer.toString(renderCalls));
        delayLabel.setText(abstractLogicEngine.delay+"ms");
        packagePer.setText(abstractLogicEngine.packagePer+"/s");
    }

    public void resize(int width, int height) {
        this.getViewport().update(width, height, true);
    }

    public void toggleFullscreen() {
        if (Gdx.graphics.isFullscreen())
            Gdx.graphics.setWindowedMode(windowWidth, windowHeight);
        else {
            windowWidth = Gdx.graphics.getWidth();
            windowHeight = Gdx.graphics.getHeight();
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
    }

    public void showSplash(TextureRegion splash, TextureRegion text) {
        splashImage.setDrawable(new TextureRegionDrawable(splash));
        splashTextImage.setDrawable(new TextureRegionDrawable(text));
        this.addActor(splashTable);
        splashTable.clearActions();
        splashTable.getColor().a = 0;
        splashTable.addAction(Actions.fadeIn(1));
        hasSplash = true;
    }

    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.NUM_6:
                speed200Button.toggle();
                return true;
            case Keys.NUM_5:
                speed150Button.toggle();
                return true;
            case Keys.NUM_4:
                speed100Button.toggle();
                return true;
            case Keys.NUM_3:
                speed33Button.toggle();
                return true;
            case Keys.NUM_2:
                speed15Button.toggle();
                return true;
            case Keys.NUM_1:
                speed3Button.toggle();
                return true;
            case Keys.P:
            case Keys.GRAVE:
                pauseButton.toggle();
                return true;
            case Keys.B:
                bgButton.toggle();
                return true;
            case Keys.Z:
                zoomButton.toggle();
                return true;
            case Keys.I:
                debugButton.toggle();
                return true;
            case Keys.T:
                view.assets.dispose();
                Texture.invalidateAllTextures(Gdx.app);
                view.assets = new Assets();
                return true;
            case Keys.ESCAPE:
                if (Gdx.graphics.isFullscreen())
                    Gdx.graphics.setWindowedMode(windowWidth, windowHeight);
                else
                    System.exit(0);
                return true;
            case Keys.ENTER:
                if (UIUtils.alt()) toggleFullscreen();
                return true;
            case Keys.O:
                showSplash(view.assets.titleRegion, view.assets.startRegion);
                return true;
        }
        return hasSplash;
    }

    public boolean keyUp(int keycode) {
        return hasSplash;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {

        if (amountY == 1.0f && cameraZoom <= 2) {
            cameraZoom *= 1.1f;
            Gdx.app.log("scrolled", "拉远视角" + cameraZoom);

        } else if (amountY == -1.0f && cameraZoom >= 0.4f) {
            cameraZoom *= 0.9f;
            Gdx.app.log("scrolled", "拉近视角" + cameraZoom);

        }

        return super.scrolled(amountX, amountY);
    }
}
