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



import com.badlogic.gdx.*;
import org.cbzmq.game.model.MyVector2;
import org.cbzmq.game.net.GameOne;
import org.cbzmq.game.net.UdpServer;
import org.cbzmq.game.logic.GameLogicEngine;
import org.cbzmq.game.logic.AbstractLogicEngine;
import org.cbzmq.game.ui.View;
import org.cbzmq.game.ui.UI;

/** The controller class for the game. It knows about both the model and view and provides a way for the view to know about events
 * that occur in the model. */



public class GameServer extends Game {
	static MyVector2 temp = new MyVector2();

	View view;
	AbstractLogicEngine abstractLogicEngine;
	Screen screen;
	UdpServer udpServer;

	float time;
	 class Screen extends ScreenAdapter{

		View view;
		UI ui;

		public Screen(View view, UI ui) {
			this.view = view;
			this.ui = ui;
		}

		@Override
		public void render(float delta) {


//			if ((int) SpineBoyGame.this.time % 2 == 0) {
//
//			}
			view.act(delta);

			view.draw();
			ui.act(delta);
			ui.draw();
		}
	}

	public void create () {
		abstractLogicEngine =  GameLogicEngine.me();
		GameOne gameOne = new GameOne();
		gameOne.start();
		try {
			udpServer = new UdpServer();
			abstractLogicEngine.addListener(udpServer);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		view = new View(abstractLogicEngine,false);
//		view.gameRestart();
		screen = new Screen(view, view.ui);
		setScreen(screen);
		Gdx.input.setInputProcessor(new InputMultiplexer(view.ui, view));

	}

	public void render () {
		float delta = Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f) * abstractLogicEngine.getTimeScale();
		time+=delta;
//		if ((int)time%2==0) {
//			model.update(delta);0
//		}
		if(delta>0){
			abstractLogicEngine.update(delta);
//			try {
//
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//				System.exit(-1);
//			}
		}
		super.render();
	}

	public void resize (int width, int height) {
		view.resize(width, height);
	}

	public void restart () {
		view.gameRestart();
	}





}
