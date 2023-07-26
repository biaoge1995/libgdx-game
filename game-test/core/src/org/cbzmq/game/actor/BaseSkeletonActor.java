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

package org.cbzmq.game.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.AnimationState.TrackEntry;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.utils.SkeletonActor;
import org.cbzmq.game.Assets;
import org.cbzmq.game.StateAnimation;
import org.cbzmq.game.domain.Character;
import org.cbzmq.game.domain.Player;

/**
 * The view class for an enemy or player that moves around the map.
 */
public class BaseSkeletonActor extends SkeletonActor{
    public Assets assets;
    public Character character;
    public BaseSkeletonActor(Assets assets,Character character) {
        this.assets = assets;
        this.character = character;
        SkeletonRenderer skeletonRenderer = new SkeletonRenderer();
        skeletonRenderer.setPremultipliedAlpha(true);
        setRenderer(skeletonRenderer);

    }


    public boolean setAnimation(StateAnimation state, boolean force) {
        // Changes the current animation on track 0 of the AnimationState, if needed.
        Animation animation = state.animation;
        TrackEntry current = getAnimationState().getCurrent(0);
        Animation oldAnimation = current == null ? null : current.getAnimation();
        if (force || oldAnimation != animation) {
            if (state.animation == null) return true;
            TrackEntry entry = getAnimationState().setAnimation(0, state.animation, state.loop);
            if (oldAnimation != null) entry.setTrackTime(state.startTimes.get(oldAnimation, state.defaultStartTime));
            if (!state.loop) entry.setTrackEnd(Float.MAX_VALUE);
            return true;
        }
        return false;
    }


    @Override
    public void act(float delta) {
        super.act(delta);
        getSkeleton().setX(character.position.x + character.rect.width / 2);
        getSkeleton().setY(character.position.y);

        getSkeleton().setScaleX(character.dir);
        getSkeleton().updateWorldTransform();

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
