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

package org.cbzmq.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.AnimationState.TrackEntry;
import org.cbzmq.game.character.Assets.SoundEffect;
import org.cbzmq.game.character.Assets;
import org.cbzmq.game.domain.Enemy;
import org.cbzmq.game.constant.Constants;
import org.cbzmq.game.stage.Model;
import org.cbzmq.game.stage.View;
import org.cbzmq.game.stage.UI;

/** The controller class for the game. It knows about both the model and view and provides a way for the view to know about events
 * that occur in the model. */



public class SuperSpineBoyGame extends Game {
	static Vector2 temp = new Vector2();

	View view;
	Model model;
	Screen screen;

	static class Screen extends ScreenAdapter{

		View view;
		UI ui;

		public Screen(View view, UI ui) {
			this.view = view;
			this.ui = ui;
		}

		@Override
		public void render(float delta) {
			view.act(delta);
			view.draw();
			ui.act(delta);
			ui.draw();
		}
	}

	public void create () {
		model = new Model(this);
		view = new View(model);
		screen = new Screen(view, view.ui);
		setScreen(screen);

	}

	public void render () {
		float delta = Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f) * model.getTimeScale();
		if (delta > 0) {
			model.update(delta);
//			view.update(delta);
		}
		super.render();
	}

	public void resize (int width, int height) {
		view.resize(width, height);
	}

	public void restart () {
		model.restart();
		view.restart();
	}

	public void eventHitPlayer (Enemy enemy) {
		Assets.SoundEffect.hurtPlayer.play();
		if (view.player.hp > 0 && view.player.view.hitAnimation != null) {
			TrackEntry entry = view.player.view.getAnimationState().setAnimation(1, view.player.view.hitAnimation, false);
			entry.setTrackEnd(view.player.view.hitAnimation.getDuration());
		}
	}

	public void eventHitEnemy (Enemy enemy) {
		SoundEffect.hurtAlien.play();
		if (enemy.view.hitAnimation != null) {
			TrackEntry entry = enemy.view.getAnimationState().setAnimation(1, enemy.view.hitAnimation, false);
			entry.setTrackEnd(enemy.view.hitAnimation.getDuration());
		}
	}

	public void eventHitBullet (float x, float y, float vx, float vy) {
		Vector2 offset = temp.set(vx, vy).nor().scl(15 * Constants.scale);
		view.hits.add(View.bulletHitTime);
		view.hits.add(x + offset.x);
		view.hits.add(y + offset.y);
		view.hits.add(temp.angle() + 90);
		SoundEffect.hit.play();
	}

	public void eventGameOver (boolean win) {
		if (!view.ui.splashTable.hasParent()) {
			view.ui.showSplash(view.assets.gameOverRegion, win ? view.assets.youWinRegion : view.assets.youLoseRegion);
			view.ui.inputTimer = win ? 5 : 1;
		}
		view.player.view.setJumpPressed(false);
		view.player.view.setLeftPressed(false);
		view.player.view.setRightPressed(false);

	}


}
