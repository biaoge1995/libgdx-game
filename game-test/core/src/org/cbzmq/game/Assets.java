package org.cbzmq.game;
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



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.AtlasTmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import org.cbzmq.game.domain.Enemy;
import org.cbzmq.game.domain.Player;


/** Centralized place to load and store assets. */
public class Assets {
	public TextureAtlas playerAtlas, enemyAtlas;
	public TextureRegion bulletRegion, hitRegion, crosshair;
	public TextureRegion titleRegion, gameOverRegion, youLoseRegion, youWinRegion, startRegion;
	public SkeletonData playerSkeletonData, enemySkeletonData;
	public AnimationStateData playerAnimationData, enemyAnimationData;
	public ObjectMap<CharacterState, StateAnimation> playerStates = new ObjectMap(), enemyStates = new ObjectMap();
	public final AssetManager assetManager;
	public TiledMap tiledMap;

	public Assets() {
		assetManager = new AssetManager();
		assetManager.load("spineother/bullet.png",Texture.class);
		assetManager.load("spineother/bullet-hit.png",Texture.class);
		assetManager.load("spineother/title.png",Texture.class);
		assetManager.load("spineother/gameOver.png",Texture.class);
		assetManager.load("spineother/youLose.png",Texture.class);
		assetManager.load("spineother/youWin.png",Texture.class);
		assetManager.load("spineother/start.png",Texture.class);
		assetManager.load("spineother/crosshair.png",Texture.class);

		assetManager.load("sounds/shoot.ogg",Sound.class);
		assetManager.load("sounds/hit.ogg",Sound.class);
		assetManager.load("sounds/footstep1.ogg",Sound.class);
		assetManager.load("sounds/footstep2.ogg",Sound.class);
		assetManager.load("sounds/squish.ogg",Sound.class);
		assetManager.load("sounds/hurt-player.ogg",Sound.class);
		assetManager.load("sounds/hurt-alien.ogg",Sound.class);
		assetManager.load("spineboy/spineboy.atlas",TextureAtlas.class);
		assetManager.load("alien/alien.atlas",TextureAtlas.class);

		assetManager.setLoader(TiledMap.class,new AtlasTmxMapLoader(assetManager.getFileHandleResolver()));
		assetManager.load("map/map.tmx",TiledMap.class);
		//等待异步加载结束
		assetManager.finishLoading();

		loadMap();
		loadPng();
		loadSounds();
		loadPlayerAssets();
		loadEnemyAssets();
	}
	public void loadMap(){
		tiledMap = assetManager.get("map/map.tmx",TiledMap.class);
	}

	public void loadPng(){
		bulletRegion = setTextureFilter(assetManager.get("spineother/bullet.png",Texture.class));
		hitRegion = setTextureFilter(assetManager.get("spineother/bullet-hit.png",Texture.class));
		titleRegion = setTextureFilter(assetManager.get("spineother/title.png",Texture.class));
		gameOverRegion = setTextureFilter(assetManager.get("spineother/gameOver.png",Texture.class));
		youLoseRegion = setTextureFilter(assetManager.get("spineother/youLose.png",Texture.class));
		youWinRegion = setTextureFilter(assetManager.get("spineother/youWin.png",Texture.class));
		startRegion = setTextureFilter(assetManager.get("spineother/start.png",Texture.class));
		crosshair = setTextureFilter(assetManager.get("spineother/crosshair.png",Texture.class));
	}

	 void loadSounds(){
		SoundEffect.shoot.sound =assetManager.get("sounds/shoot.ogg",Sound.class);
		SoundEffect.hit.sound = assetManager.get("sounds/hit.ogg",Sound.class);
		SoundEffect.footstep1.sound = assetManager.get("sounds/footstep1.ogg",Sound.class);
		SoundEffect.footstep2.sound = assetManager.get("sounds/footstep2.ogg",Sound.class);
		SoundEffect.squish.sound = assetManager.get("sounds/squish.ogg",Sound.class);
		SoundEffect.squish.volume = 0.6f;
		SoundEffect.hurtPlayer.sound = assetManager.get("sounds/hurt-player.ogg",Sound.class);
		SoundEffect.hurtAlien.sound = assetManager.get("sounds/hurt-alien.ogg",Sound.class);
		SoundEffect.hurtAlien.volume = 0.5f;
	}

	TextureRegion setTextureFilter (Texture texture) {
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Linear);
		return new TextureRegion(texture);
	}

	 void loadPlayerAssets () {
		playerAtlas =assetManager.get("spineboy/spineboy.atlas",TextureAtlas.class);

		SkeletonJson json = new SkeletonJson(playerAtlas);
		json.setScale(Player.height / Player.heightSource);
		playerSkeletonData = json.readSkeletonData(Gdx.files.internal("spineboy/spineboy.json"));

		playerAnimationData = new AnimationStateData(playerSkeletonData);
		playerAnimationData.setDefaultMix(0.2f);
		setMix(playerAnimationData, "idle", "run", 0.3f);
		setMix(playerAnimationData, "run", "idle", 0.1f);
		setMix(playerAnimationData, "shoot", "shoot", 0);

		setupState(playerStates, CharacterState.death, playerSkeletonData, "death", false);
		StateAnimation idle = setupState(playerStates, CharacterState.idle, playerSkeletonData, "idle", true);
		StateAnimation jump = setupState(playerStates, CharacterState.jump, playerSkeletonData, "jump", false);
		StateAnimation run = setupState(playerStates, CharacterState.run, playerSkeletonData, "run", true);
		if (idle.animation != null) run.startTimes.put(idle.animation, 8 * Constants.fps);
		if (jump.animation != null) run.startTimes.put(jump.animation, 22 * Constants.fps);
		StateAnimation fall = setupState(playerStates, CharacterState.fall, playerSkeletonData, "jump", false);
		fall.defaultStartTime = 22 * Constants.fps;
	}

	 void loadEnemyAssets () {
		enemyAtlas = assetManager.get("alien/alien.atlas",TextureAtlas.class);

		SkeletonJson json = new SkeletonJson(enemyAtlas);
		json.setScale(Enemy.height / Enemy.heightSource);
		enemySkeletonData = json.readSkeletonData(Gdx.files.internal("alien/alien.json"));

		enemyAnimationData = new AnimationStateData(enemySkeletonData);
		enemyAnimationData.setDefaultMix(0.1f);

		setupState(enemyStates, CharacterState.idle, enemySkeletonData, "run", true);
		setupState(enemyStates, CharacterState.jump, enemySkeletonData, "jump", true);
		setupState(enemyStates, CharacterState.run, enemySkeletonData, "run", true);
		setupState(enemyStates, CharacterState.death, enemySkeletonData, "death", false);
		setupState(enemyStates, CharacterState.fall, enemySkeletonData, "run", false);
	}

	public void setMix (AnimationStateData data, String from, String to, float mix) {
		Animation fromAnimation = data.getSkeletonData().findAnimation(from);
		Animation toAnimation = data.getSkeletonData().findAnimation(to);
		if (fromAnimation == null || toAnimation == null) return;
		data.setMix(fromAnimation, toAnimation, mix);
	}

	public StateAnimation setupState (ObjectMap map, CharacterState state, SkeletonData skeletonData, String name, boolean loop) {
		StateAnimation stateAnimation = new StateAnimation();
		stateAnimation.animation = skeletonData.findAnimation(name);
		stateAnimation.loop = loop;
		map.put(state, stateAnimation);
		return stateAnimation;
	}

	public void dispose () {
		playerAtlas.dispose();
		enemyAtlas.dispose();
	}

	public enum SoundEffect {
		shoot, hit, footstep1, footstep2, squish, hurtPlayer, hurtAlien;

		Sound sound;
		float volume = 1;

		public void play () {
			sound.play(volume);
		}
	}
}
