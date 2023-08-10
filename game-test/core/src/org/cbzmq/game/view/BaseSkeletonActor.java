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

package org.cbzmq.game.view;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.AnimationState.TrackEntry;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.SkeletonRendererDebug;
import com.esotericsoftware.spine.utils.SkeletonActor;
import org.cbzmq.game.Assets;
import org.cbzmq.game.GameCamera;
import org.cbzmq.game.StateAnimation;
import org.cbzmq.game.model.Character;
import org.cbzmq.game.model.Event;
import org.cbzmq.game.model.ObserverAdapter;

/**
 * The view class for an enemy or player that moves around the map.
 */
public class BaseSkeletonActor<T extends Character> extends SkeletonActor {
    private Assets assets;
    private T model;

    private Viewport viewport;
    private GameCamera camera;


    public BaseSkeletonActor(T model) {
        this(null, model);
    }


    public BaseSkeletonActor(Assets assets, T model) {
        this.assets = assets;
        this.model = model;
        SkeletonRenderer skeletonRenderer = new SkeletonRenderer();
        skeletonRenderer.setPremultipliedAlpha(true);
        setRenderer(skeletonRenderer);
        model.addListen(new ObserverAdapter(){
            @Override
            public boolean onOneObserverEvent(Event.OneCharacterEvent event) {

                return super.onOneObserverEvent(event);
            }

            @Override
            public boolean onTwoObserverEvent(Event.TwoObserverEvent event) {
                Character a = event.getA();
                Character b = event.getB();
                switch (event.getEventType()) {
                    case hit:
                        beHit();
                        break;
                }
                return super.onTwoObserverEvent(event);
            }
        });

    }

    public void loadAssets(Assets assets) {
        this.assets = assets;
    }


    public boolean setAnimation(StateAnimation state, boolean force) {
        // Changes the current animation on track 0 of the AnimationState, if needed.
        Animation animation = state.animation;
        TrackEntry current = getAnimationState().getCurrent(0);
        Animation oldAnimation = current == null ? null : current.getAnimation();
        if (force || oldAnimation != animation) {
            if (state.animation == null) return true;
            TrackEntry entry = getAnimationState().setAnimation(0, state.animation, state.loop);
            if (oldAnimation != null) {
                float v = state.startTimes.get(oldAnimation, state.defaultStartTime);
                entry.setTrackTime(v);
            };
            if (!state.loop) entry.setTrackEnd(Float.MAX_VALUE);
            return true;
        }
        return false;
    }


    @Override
    public void act(float delta) {
//        super.act(delta);
//        setX(model.position.x);
//        setY(model.position.y);
//        getSkeleton().setX(model.position.x + model.rect.width / 2);
//        getSkeleton().setY(model.position.y);
//
//        getSkeleton().setScaleX(model.dir);
//        getSkeleton().updateWorldTransform();

        //如果角色死亡则remove掉自己
//        if(model.state== CharacterState.death){
//            remove();
//        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    public void beHit() {
    }

    public void drawDebug(SkeletonRendererDebug skeletonRendererDebug) {
        ShapeRenderer shapeRenderer = skeletonRendererDebug.getShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GREEN);

        shapeRenderer.rect(model.rect.x, model.rect.y, model.rect.width, model.rect.height);
        shapeRenderer.end();
        if (getSkeleton() != null) {
            skeletonRendererDebug.draw(getSkeleton());
        }
    }

    public Assets getAssets() {
        return assets;
    }

    public void setAssets(Assets assets) {
        this.assets = assets;
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    public GameCamera getCamera() {
        return camera;
    }

    public void setCamera(GameCamera camera) {
        this.camera = camera;
    }
}
