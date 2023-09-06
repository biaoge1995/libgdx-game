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
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import org.cbzmq.game.enums.OneBodyEventType;
import org.cbzmq.game.logic.*;
import org.cbzmq.game.model.*;
import org.cbzmq.game.model.Character;
import org.cbzmq.game.proto.CharacterState;
import org.cbzmq.game.proto.CharacterType;
import org.cbzmq.game.proto.MoveType;


/**
 * The core of the view logic. The view knows about the model and manages everything needed to draw to the screen.
 */
public class View extends Stage {

    public static int[] mapLayersOpaque1 = {1};
    public static int[] mapLayersBackground2 = {2, 3, 4, 5, 6};
    public static int[] mapLayersOpaque3 = {10};
    public static int[] mapForegroundLayers4 = {7, 8, 9};
    public static int[] mapForegroundLayers5 = {11};
    public static final String TAG = View.class.getName();



    public AbstractLogicEngine abstractLogicEngine;
    public Player player;
    public PlayerActor playerView;
    public GameCamera camera;
    public ExtendViewport viewport;
    public SpriteBatch batch;
    public OrthoCachedTiledMapRenderer mapRenderer;
    public Assets assets;
    public UI ui;
    private boolean isObservationMode;

    private boolean isClient = false;

    Vector2 mouse = new Vector2();
//    Group playerGroup;
//    Group enemyGroup;
//    Group bulletGroup;


    public View(AbstractLogicEngine abstractLogicEngine, boolean isClient) {
        camera = new GameCamera();
        this.isClient = isClient;
        batch = (SpriteBatch) getBatch();
        viewport = new ExtendViewport(GameCamera.cameraMinWidth
                , GameCamera.cameraHeight
                , GameCamera.cameraMaxWidth
                , GameCamera.cameraHeight
                , camera);



        setViewport(viewport);

        this.abstractLogicEngine = abstractLogicEngine;

        assets = new Assets();
        abstractLogicEngine.map.load(assets.tiledMap);
        mapRenderer = new OrthoCachedTiledMapRenderer(assets.tiledMap, Constants.scale, 3000);
        mapRenderer.setOverCache(0.6f);
        mapRenderer.setMaxTileSize(512, 512);
        ui = new UI(this);
        abstractLogicEngine.addListener(new ObserverAdapter() {

            @Override
            public boolean onOneObserverEvent(Event.OneCharacterEvent event) {
                Character character = event.getCharacter();
                switch (event.getEventType()) {
                    case born:
                        switch (character.characterType) {

                            case player:
                                if (player != character) {
                                    player = (Player) character;
                                    playerView = new PlayerActor(player);
                                }
                                addActor(playerView);
                                break;
                            case enemy:
                                addActor(new EnemyActor((Enemy) character));
                                break;
                            case bullet:
                                addActor(new BulletActor((Bullet) character));
                                break;
                        }


                }
                return true;
            }

        });
//        restart();
    }

    public void gameRestart() {
        abstractLogicEngine.restart();
        this.restart();
    }

    private void restart() {

        clear();
        abstractLogicEngine.join(new Player());
        player = abstractLogicEngine.getPlayerA();
        if (player != null) {
            playerView = new PlayerActor(player);
            addActor(playerView);
            camera.lookahead = 0;
            playerView.setShootPressed(false);
        }


    }

    public void setObservationMode(boolean observationMode) {
        isObservationMode = observationMode;
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
        super.addActor(actor);
        actor.loadAssets(assets);
        actor.setViewport(viewport);
        actor.setCamera(camera);


    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (abstractLogicEngine.isGameOver()) eventGameOver(abstractLogicEngine.isPlayerWin());
        if (player == null) return;
        if (!isObservationMode) updateAimPoint();
        updateInput(delta);
        updateCamera(delta);

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

    public void updateAimPoint() {
        mouse.set(Gdx.input.getX(), Gdx.input.getY());
        //将指定的屏幕坐标系转换为世界坐标系
        getViewport().unproject(mouse);

        abstractLogicEngine.updateByEvent(Event.aimPoint(TAG, player, mouse.x,mouse.y));
    }

    public void updateInput(float delta) {
        if (playerView == null) return;
        if (playerView.isLeftPressed()) {
            //TODO

            if (isClient) CharacterOnMsg.MoveOnMessage.me().request(player, MoveType.moveLeft, delta);
            else abstractLogicEngine.updateByEvent(Event.moveLeft(TAG, player, delta));

        } else if (playerView.isRightPressed()) {
            //TODO

            if (isClient) CharacterOnMsg.MoveOnMessage.me().request(player, MoveType.moveRight, delta);
            else abstractLogicEngine.updateByEvent(Event.moveRight(TAG, player, delta));
        } else if (player.state == CharacterState.running) {
            //TODO
//            model.onCharacterEvent(Event.stateUpdate(TAG,player, CharacterState.idle));
        }


        if (playerView.isShootPressed()) {
            if (abstractLogicEngine.getQueue() != null && playerView.shoot()) {
                abstractLogicEngine.updateByEvent(Event.attack(TAG, player));

            }
        }
        ;
    }

    public void updateCamera(float delta) {
        if (player == null) return;
        if (player.hp > 0) {
            // Reduce camera lookahead based on distance of enemies behind the player.
            float enemyBehindDistance = 0;
            for (int i = 0; i < abstractLogicEngine.getEnemies().size; i++) {

                Array<Enemy> enemies = abstractLogicEngine.getEnemies();
                Enemy enemy = enemies.get(i);
                float dist = enemy.getPosition().x - player.getPosition().x;
                if (enemy.hp > 0 && Math.signum(dist) == -player.dir) {
                    dist = Math.abs(dist);
                    enemyBehindDistance = enemyBehindDistance == 0 ? dist : Math.min(enemyBehindDistance, dist);
                }
            }
            float lookaheadDist = GameCamera.cameraLookahead * viewport.getWorldWidth() / 2 * (1 - Math.min(1, enemyBehindDistance / 22));
            float lookaheadDiff = player.getPosition().x + lookaheadDist * player.dir - camera.position.x;
            float lookaheadAdjust = (enemyBehindDistance > 0 ? GameCamera.cameraLookaheadSpeedSlow : GameCamera.cameraLookaheadSpeed) * delta;
            if (Math.abs(camera.lookahead - lookaheadDiff) > 1) {
                if (camera.lookahead < lookaheadDiff)
                    camera.lookahead = Math.min(lookaheadDist, camera.lookahead + lookaheadAdjust);
                else if (camera.lookahead > lookaheadDiff) //
                    camera.lookahead = Math.max(-lookaheadDist, camera.lookahead - lookaheadAdjust);
            }
            if (player.getPosition().x + camera.lookahead < GameCamera.cameraMinX) camera.lookahead = camera.cameraLookahead;
        }

        // Move camera to the player position over time, adjusting for lookahead.
        float minX = player.getPosition().x + camera.lookahead, maxX = player.getPosition().x + camera.lookahead;
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
        float maxY = player.getPosition().y + viewport.getWorldHeight() / 2 - bottom;
        float minY = player.getPosition().y - viewport.getWorldHeight() / 2 + top;
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
        if (player != null) {
            camera.position.x = player.getPosition().x;
            camera.position.y = player.getPosition().y + viewport.getWorldHeight() / 2 - GameCamera.cameraBottom;
        }
        mapRenderer.setView(camera);
        ui.resize(width, height);
    }


    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (playerView != null)
            playerView.setShootPressed(true);
        return true;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (playerView != null)
            playerView.setShootPressed(false);
        return true;
    }

    public boolean keyDown(int keycode) {
        if (playerView != null)
            switch (keycode) {
                case Keys.W:
                case Keys.UP:
                case Keys.SPACE:
                    abstractLogicEngine.updateByEvent(Event.jump(TAG, player));
                    if (isClient) CharacterOnMsg.MoveOnMessage.me().request(player, MoveType.jump, 0f);
                    return true;
                case Keys.A:
                case Keys.LEFT:
                    playerView.setLeftPressed(true);
                    return true;
                case Keys.D:
                case Keys.RIGHT:
                    playerView.setRightPressed(true);
                    return true;
            }
        return false;
    }

    public boolean keyUp(int keycode) {
        if (playerView != null)
            switch (keycode) {
                case Keys.W:
                case Keys.UP:
                case Keys.SPACE:
                    if (player.hp == 0) return false;
                    // Releasing jump on the way up reduces jump height.
                    //如果快速松开空格时候可以实现小的跳跃，长时间按空格可以大跳
                    abstractLogicEngine.updateByEvent(Event.jumpDamping(TAG, player));
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
