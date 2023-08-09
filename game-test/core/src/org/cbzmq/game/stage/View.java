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

package org.cbzmq.game.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import org.cbzmq.game.Assets;
import org.cbzmq.game.Constants;
import org.cbzmq.game.GameCamera;
import org.cbzmq.game.enums.CharacterState;
import org.cbzmq.game.model.*;
import org.cbzmq.game.model.Character;
import org.cbzmq.game.view.*;

import java.util.HashMap;
import java.util.Map;


/**
 * The core of the view logic. The view knows about the model and manages everything needed to draw to the screen.
 */
public class View extends Stage {

    public static int[] mapLayersOpaque1 = {1};
    public static int[] mapLayersBackground2 = {2, 3, 4, 5, 6};
    public static int[] mapLayersOpaque3 = {10};
    public static int[] mapForegroundLayers4 = {7, 8, 9};
    public static int[] mapForegroundLayers5 = {11};

    public Map<Integer, BaseSkeletonActor> modelAndViewMap = new HashMap<>();
    ActorFactory actorFactory;

    public Model model;
    public Player player;
    public PlayerActor playerView;
    public GameCamera camera;
    public ExtendViewport viewport;
    public SpriteBatch batch;
    public OrthoCachedTiledMapRenderer mapRenderer;
    public Assets assets;
    public UI ui;

    Vector2 mouse = new Vector2();
//    Group playerGroup;
//    Group enemyGroup;
//    Group bulletGroup;


    public View(Model model) {
        camera = new GameCamera();
        batch = (SpriteBatch) getBatch();
        viewport = new ExtendViewport(GameCamera.cameraMinWidth
                , GameCamera.cameraHeight
                , GameCamera.cameraMaxWidth
                , GameCamera.cameraHeight
                , camera);
        actorFactory = new ActorFactoryImp();

        setViewport(viewport);

        this.model = model;
        mapRenderer = new OrthoCachedTiledMapRenderer(model.getMap().tiledMap, Constants.scale, 3000);
        mapRenderer.setOverCache(0.6f);
        mapRenderer.setMaxTileSize(512, 512);
        assets = model.getAssets();
        ui = new UI(this);
        model.addListener(new ObserverAdapter() {
            @Override
            public void onTwoObserverEvent(Event.TwoObserverEvent event) {
                Character a = event.getA();
                Character b = event.getB();

                switch (event.getEventType()) {
                    case hit:
                        Character character = (Character) a;
                        if (a instanceof Character && modelAndViewMap.containsKey(character.getId())) {
                            BaseSkeletonActor baseSkeletonActor = modelAndViewMap.get(character.getId());

                            baseSkeletonActor.beHit();
                        }
                    default:
                        Gdx.app.log("监听者" + this + "| 事件" + event.getEventType().toString(), event.getA().toString());
                        break;
                }


            }


        });
        restart();
    }

    public void gameRestart() {
        model.restart();
        this.restart();
    }

    private void restart() {

        clear();
        modelAndViewMap.clear();
        player = model.getPlayer();
        playerView = new PlayerActor(player);
        addActor(playerView);
        camera.lookahead = 0;
        playerView.setShootPressed(false);
//        hits.clear();
//        playerGroup.clear();
//        enemyGroup.clear();
//        bulletGroup.clear();


    }

    public void eventGameOver(boolean win) {
        if (!ui.splashTable.hasParent()) {
            ui.showSplash(assets.gameOverRegion, win ? assets.youWinRegion : assets.youLoseRegion);
            ui.inputTimer = win ? 5 : 1;
        }
        playerView.setJumpPressed(false);
        playerView.setLeftPressed(false);
        playerView.setRightPressed(false);

    }


    public void addActor(BaseSkeletonActor actor) {
        if (modelAndViewMap.containsKey(actor.getModel().getId())) {
            return;
        } else {
            modelAndViewMap.put(actor.getModel().getId(), actor);
            super.addActor(actor);
            actor.loadAssets(assets);
            actor.setViewport(viewport);
            actor.setCamera(camera);
        }


    }

    @Override
    public void act(float delta) {
        super.act(delta);
        updateInput(delta);
        updateCamera(delta);
        Array<Character> all = model.getAll();

        for (int i = 0; i < all.size; i++) {
            Character character = all.get(i);
            BaseSkeletonActor actor = null;
            if (modelAndViewMap.containsKey(character.getId())) {
                actor = modelAndViewMap.get(character.getId());
            }
            switch (character.characterType) {

                case player:
                    if (player != character) {
                        this.player = (Player) character;
                        this.playerView = new PlayerActor(this.player);
                    }
                    if (actor != null && this.playerView != actor) {
                        PlayerActor actor1 = (PlayerActor) actor;
                        actor1.setModel(this.player);
                        this.playerView = actor1;
                    }
                    break;
                case enemy:
                    if (actor == null) {
                        actor = new EnemyActor((Enemy) character);
                    }
                    break;
                case bullet:
                    if (actor == null) {
                        actor = new BulletActor((Bullet) character);
                    }
                    break;
            }
            if (actor != null) {
                addActor(actor);
            }
        }
        if (model.isGameOver()) eventGameOver(model.isPlayerWin());

        updateAimPoint();
    }

    @Override
    public void draw() {

//        setDebugUnderMouse(true);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        if (ui.bgButton.isChecked()) {
            mapRenderer.setBlending(false);
            mapRenderer.render(mapLayersOpaque1);

            mapRenderer.setBlending(true);
            mapRenderer.render(mapLayersBackground2);


            //上下边框
            mapRenderer.setBlending(false);
            mapRenderer.render(mapLayersOpaque3);

            mapRenderer.setBlending(true);
            mapRenderer.render(mapForegroundLayers4);


        }


        super.draw();
        //画出最外层边框
        if (ui.bgButton.isChecked()) mapRenderer.render(mapForegroundLayers5);


    }

    public void updateAimPoint(){
        mouse.set(Gdx.input.getX(),Gdx.input.getY());
        //将指定的屏幕坐标系转换为世界坐标系
        getViewport().unproject(mouse);
        model.getQueue().aimPoint(player,mouse);
    }

    public void updateInput(float delta) {
        if (player.hp == 0) return;

        if (playerView.isLeftPressed()) {
            //TODO
//            player.moveLeft(delta);
            model.getQueue().moveLeft(player, delta);
        } else if (playerView.isRightPressed()) {
            //TODO
//            player.moveRight(delta);
            model.getQueue().moveRight(player, delta);
        } else if (player.state == CharacterState.running) {
            //TODO
            player.setState(CharacterState.idle);
        }


        if (playerView.isShootPressed()) {
            if (model.getQueue() != null &&  playerView.shoot()) {
                model.getQueue().attack(player);
            }
        }
        ;
    }

    public void updateCamera(float delta) {
        if (player.hp > 0) {
            // Reduce camera lookahead based on distance of enemies behind the player.
            float enemyBehindDistance = 0;
            for (int i = 0; i < model.getEnemies().size; i++) {

                Array<Enemy> enemies = model.getEnemies();
                Enemy enemy = enemies.get(i);
                float dist = enemy.position.x - player.position.x;
                if (enemy.hp > 0 && Math.signum(dist) == -player.dir) {
                    dist = Math.abs(dist);
                    enemyBehindDistance = enemyBehindDistance == 0 ? dist : Math.min(enemyBehindDistance, dist);
                }
            }
            float lookaheadDist = GameCamera.cameraLookahead * viewport.getWorldWidth() / 2 * (1 - Math.min(1, enemyBehindDistance / 22));
            float lookaheadDiff = player.position.x + lookaheadDist * player.dir - camera.position.x;
            float lookaheadAdjust = (enemyBehindDistance > 0 ? GameCamera.cameraLookaheadSpeedSlow : GameCamera.cameraLookaheadSpeed) * delta;
            if (Math.abs(camera.lookahead - lookaheadDiff) > 1) {
                if (camera.lookahead < lookaheadDiff)
                    camera.lookahead = Math.min(lookaheadDist, camera.lookahead + lookaheadAdjust);
                else if (camera.lookahead > lookaheadDiff) //
                    camera.lookahead = Math.max(-lookaheadDist, camera.lookahead - lookaheadAdjust);
            }
            if (player.position.x + camera.lookahead < GameCamera.cameraMinX) camera.lookahead = camera.cameraLookahead;
        }

        // Move camera to the player position over time, adjusting for lookahead.
        float minX = player.position.x + camera.lookahead, maxX = player.position.x + camera.lookahead;
        if (camera.position.x < minX) {
            camera.position.x += (minX - camera.position.x) * GameCamera.cameraSpeed * delta;
            if (camera.position.x > minX) camera.position.x = minX;
            if (Math.abs(camera.position.x - minX) < 0.1f) camera.position.x = minX;
        } else if (camera.position.x > maxX) {
            camera.position.x += (maxX - camera.position.x) * GameCamera.cameraSpeed * delta;
            if (camera.position.x < maxX) camera.position.x = maxX;
            if (Math.abs(camera.position.x - maxX) < 0.1f) camera.position.x = maxX;
        }
        camera.position.x = Math.max(viewport.getWorldWidth() / 2 + GameCamera.cameraMinX, camera.position.x);

        float top = camera.zoom != 1 ? 5 : GameCamera.cameraTop;
        float bottom = camera.zoom != 1 ? 0 : GameCamera.cameraBottom;
        float maxY = player.position.y + viewport.getWorldHeight() / 2 - bottom;
        float minY = player.position.y - viewport.getWorldHeight() / 2 + top;
        if (camera.position.y < minY) {
            camera.position.y += (minY - camera.position.y) * GameCamera.cameraSpeed / camera.zoom * delta;
            if (Math.abs(camera.position.y - minY) < 0.1f) camera.position.y = minY;
        } else if (camera.position.y > maxY) {
            camera.position.y += (maxY - camera.position.y) * GameCamera.cameraSpeed / camera.zoom * delta;
            if (Math.abs(camera.position.y - maxY) < 0.1f) camera.position.y = maxY;
        }
        camera.position.z = 0.5f;
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        mapRenderer.setView(camera);

        camera.position.add(-camera.shakeX, -camera.shakeY, 0);
        camera.shakeX = 0;
        camera.shakeY = 0;
    }


    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.x = player.position.x;
        camera.position.y = player.position.y + viewport.getWorldHeight() / 2 - GameCamera.cameraBottom;
        mapRenderer.setView(camera);
        ui.resize(width, height);
    }





    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//        playerView.setAimPoint(screenX, screenY);
        playerView.setShootPressed(true);
//        playerView.shoot();
        return true;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        playerView.setShootPressed(false);
        return true;
    }

    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.W:
            case Keys.UP:
            case Keys.SPACE:
                if (player.hp == 0) return false;
                else if (player.isGrounded()) {
//                    playerView.setJumpPressed(true);
//                playerView.jump();
                    model.getQueue().jump(player, 0);
                }

                return true;
            case Keys.A:
            case Keys.LEFT:
//                player.moveLeft(0.03f);
                playerView.setLeftPressed(true);

                return true;
            case Keys.D:
            case Keys.RIGHT:
                playerView.setRightPressed(true);
//                model.getQueue().moveLeft(player, 0.5f);
                return true;
        }
        return false;
    }

    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.W:
            case Keys.UP:
            case Keys.SPACE:
                if (player.hp == 0) return false;
                // Releasing jump on the way up reduces jump height.
                //如果快速松开空格时候可以实现小的跳跃，长时间按空格可以大跳
                player.jumpDamping();
                playerView.setJumpPressed(false);
                return true;
            case Keys.A:
            case Keys.LEFT:
                playerView.setLeftPressed(false);
                return true;
            case Keys.D:
            case Keys.RIGHT:
                playerView.setRightPressed(false);
                return true;
        }
        return false;
    }


}
