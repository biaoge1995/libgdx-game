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
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ObjectFloatMap;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.SkeletonRenderer;
import org.cbzmq.game.character.GameCamera.*;

import org.cbzmq.game.character.*;
import org.cbzmq.game.constant.Constants;


/** The core of the view logic. The view knows about the model and manages everything needed to draw to the screen. */
public class SpineBoyStage extends InputAdapter {
	public static float bulletHitTime = 0.2f, bulletHitOffset = 50 * Constants.scale;
//	public static float cameraMinWidth = 16, cameraMaxWidth = 28, cameraHeight = 16, cameraZoom = 0.4f, cameraZoomSpeed = 0.5f;
//	public static float cameraBottom = 2, cameraTop = 7, cameraMinX = 1;
//	public static float cameraLookahead = 0.75f, cameraLookaheadSpeed = 8f, cameraLookaheadSpeedSlow = 3f;
//	public static float cameraSpeed = 5f, cameraShake = 6 * Constants.scale;
	public static int[] mapLayersOpaque1 = {1};
	public static int[] mapLayersBackground2 = {2, 3, 4, 5, 6};
	public static int[] mapLayersOpaque3 = {10};
	public static int[] mapForegroundLayers4 = {7, 8,};
	public static int[] mapForegroundLayers5 = {11};

	public Model model;
	public Player player;
	public GameCamera camera;
	public ExtendViewport viewport;
	public SpriteBatch batch;
	public SkeletonRenderer skeletonRenderer;
	public OrthoCachedTiledMapRenderer mapRenderer;
	public Assets assets;
	public UI2 ui;

//	public float shakeX, shakeY, lookahead, zoom = 1;
	public FloatArray hits = new FloatArray();
//	public boolean touched, jumpPressed, leftPressed, rightPressed;

	public SpineBoyStage(Model model) {
		this.model = model;

		mapRenderer = new OrthoCachedTiledMapRenderer(model.map.tiledMap, Constants.scale, 3000);
		mapRenderer.setOverCache(0.6f);
		mapRenderer.setMaxTileSize(512, 512);

		batch = new SpriteBatch();
		camera = new GameCamera();
		viewport = new ExtendViewport(GameCamera.cameraMinWidth, GameCamera.cameraHeight, GameCamera.cameraMaxWidth, GameCamera.cameraHeight, camera);

		skeletonRenderer = new SkeletonRenderer();
		skeletonRenderer.setPremultipliedAlpha(true);

		assets = new Assets();

		ui = new UI2(this);

		Gdx.input.setInputProcessor(new InputMultiplexer( ui, this));

		restart();
	}

	public void restart () {
		player = model.player;
		player.view = new PlayerView(assets,player,viewport,camera,model);
		camera.lookahead = 0;
		player.view.setTouched(false);
		hits.clear();
	}

	public void update (float delta) {
		// Update the hit marker images.
		for (int i = hits.size - 4; i >= 0; i -= 4) {
			float time = hits.get(i) - delta;
			if (time < 0)
				hits.removeRange(i, i + 3);
			else
				hits.set(i, time);
		}

		updateInput(delta);
		updateCamera(delta);

		player.view.update(delta);

		for (Enemy enemy : model.enemies) {
			if (enemy.view == null) enemy.view = new EnemyView(assets, enemy);
			enemy.view.update(delta);
		}
	}

	public void updateInput (float delta) {
		if (player.hp == 0) return;

		if (player.view.isLeftPressed())
			player.moveLeft(delta);
		else if (player.view.isRightPressed())
			player.moveRight(delta);
		else if (player.state == CharacterState.run) //
			player.setState(CharacterState.idle);

		if (player.view.isTouched()) player.view.shoot();
	}

	public void updateCamera (float delta) {
		if (player.hp > 0) {
			// Reduce camera lookahead based on distance of enemies behind the player.
			float enemyBehindDistance = 0;
			for (Enemy enemy : model.enemies) {
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

		camera.update();
		batch.setProjectionMatrix(camera.combined);
		mapRenderer.setView(camera);

		camera.position.add(-camera.shakeX, -camera.shakeY, 0);
		camera.shakeX = 0;
		camera.shakeY = 0;
	}

	public void render () {
		viewport.apply();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (ui.bgButton.isChecked()) {
			mapRenderer.setBlending(false);
			mapRenderer.render(mapLayersOpaque1);

			mapRenderer.setBlending(true);
			mapRenderer.render(mapLayersBackground2);
		}

		batch.begin();
		// Draw enemies.
		for (Enemy enemy : model.enemies) {
			enemy.view.skeleton.getColor().a = Math.min(1, enemy.deathTimer / Enemy.fadeTime);
			skeletonRenderer.draw(batch, enemy.view.skeleton);
		}
		// Draw player.
		if (player.collisionTimer < 0 || (int)(player.collisionTimer / Player.flashTime * 1.5f) % 2 != 0)
			skeletonRenderer.draw(batch, player.view.skeleton);
		batch.end();

		if (ui.bgButton.isChecked()) {
			mapRenderer.setBlending(false);
			mapRenderer.render(mapLayersOpaque3);

			mapRenderer.setBlending(true);
			mapRenderer.render(mapForegroundLayers4);
		}

		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		batch.begin();

		// Draw bullets.
		TextureRegion bulletRegion = assets.bulletRegion;
		float bulletWidth = bulletRegion.getRegionWidth() * Constants.scale;
		float bulletHeight = bulletRegion.getRegionHeight() * Constants.scale / 2;
		for (int i = 2, n = model.bullets.size; i < n; i += 5) {
			float x = model.bullets.get(i), y = model.bullets.get(i + 1);
			float angle = model.bullets.get(i + 2);
			float vx = MathUtils.cosDeg(angle);
			float vy = MathUtils.sinDeg(angle);
			// Adjust position so bullet region is drawn with the bullet position in the center of the fireball.
			x -= vx * bulletWidth * 0.65f;
			y -= vy * bulletWidth * 0.65f;
			x += vy * bulletHeight / 2;
			y += -vx * bulletHeight / 2;
			batch.draw(bulletRegion, x, y, 0, 0, bulletWidth, bulletHeight, 1, 1, angle);
		}

		// Draw hit markers.
		TextureRegion hitRegion = assets.hitRegion;
		Color color = batch.getColor().set(1, 1, 1, 1);
		float hitWidth = hitRegion.getRegionWidth() * Constants.scale;
		float hitHeight = hitRegion.getRegionWidth() * Constants.scale;
		for (int i = hits.size - 4; i >= 0; i -= 4) {
			float time = hits.get(i);
			float x = hits.get(i + 1);
			float y = hits.get(i + 2);
			float angle = hits.get(i + 3);
			color.a = time / bulletHitTime;
			batch.setColor(color);
			float vx = MathUtils.cosDeg(angle);
			float vy = MathUtils.sinDeg(angle);
			// Adjust position so bullet region is drawn with the bullet position in the center of the fireball.
			x += vy * bulletHeight * 0.2f;
			y += -vx * bulletHeight * 0.2f;
			batch.draw(hitRegion, x - hitWidth / 2, y, hitWidth / 2, 0, hitWidth, hitHeight, 1, 1, angle);
		}
		batch.setColor(Color.WHITE);

		batch.end();
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		if (ui.bgButton.isChecked()) mapRenderer.render(mapForegroundLayers5);

//		ui.render();
	}

	public void resize (int width, int height) {
		viewport.update(width, height);
		camera.position.x = player.position.x;
		camera.position.y = player.position.y + viewport.getWorldHeight() / 2 - GameCamera.cameraBottom;
		mapRenderer.setView(camera);
		ui.resize(width, height);
	}

	public boolean touchDown (int screenX, int screenY, int pointer, int button) {

		player.view.setTouched(true);
		player.view.shoot();
		return true;
	}

	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		player.view.setTouched(false);
		return true;
	}

	public boolean keyDown (int keycode) {
		switch (keycode) {
		case Keys.W:
		case Keys.UP:
		case Keys.SPACE:
			if (player.hp == 0) return false;
			player.view.setJumpPressed(true);
			if (player.isGrounded()) player.view.jump();
			return true;
		case Keys.A:
		case Keys.LEFT:
			player.view.setLeftPressed(true);
			return true;
		case Keys.D:
		case Keys.RIGHT:
			player.view.setRightPressed(true);
			return true;
		}
		return false;
	}

	public boolean keyUp (int keycode) {
		switch (keycode) {
		case Keys.W:
		case Keys.UP:
		case Keys.SPACE:
			if (player.hp == 0) return false;
			// Releasing jump on the way up reduces jump height.
			if (player.velocity.y > 0) player.velocity.y *= Player.jumpDamping;
			player.view.setJumpPressed(false);
			return true;
		case Keys.A:
		case Keys.LEFT:
			player.view.setLeftPressed(false);
			return true;
		case Keys.D:
		case Keys.RIGHT:
			player.view.setRightPressed(false);
			return true;
		}
		return false;
	}

}
