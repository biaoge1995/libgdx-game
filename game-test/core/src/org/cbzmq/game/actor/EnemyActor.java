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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.*;
import com.esotericsoftware.spine.AnimationState.AnimationStateAdapter;
import com.esotericsoftware.spine.attachments.Attachment;
import org.cbzmq.game.Assets;
import org.cbzmq.game.domain.Enemy;
import org.cbzmq.game.domain.Enemy.Type;


/**
 * The view class for an enemy.
 */
public class EnemyActor extends BaseSkeletonActor {
    public Enemy enemy;
    public Animation hitAnimation;
    public Slot headSlot;
    public Attachment burstHeadAttachment;
    public Color headColor;

    public EnemyActor(Assets assets, Enemy enemy) {
        super(assets,enemy);
        this.enemy = enemy;
        setSkeleton(new Skeleton(assets.enemySkeletonData));
        burstHeadAttachment = getSkeleton().getAttachment("head", "burst01");
        headSlot = getSkeleton().findSlot("head");
        hitAnimation = getSkeleton().getData().findAnimation("hit");

        setAnimationState(new AnimationState(assets.enemyAnimationData));

        // Play squish sound when enemies die.
        final EventData squishEvent = assets.enemySkeletonData.findEvent("squish");
        getAnimationState().addListener(new AnimationStateAdapter() {
            public void event(AnimationState.TrackEntry entry, Event event) {
                if (event.getData() == squishEvent) Assets.SoundEffect.squish.play();
            }
        });

        // Enemies have slight color variations.
        if (enemy.type == Enemy.Type.strong)
            headColor = new Color(1, 0.6f, 1, 1);
        else
            headColor = new Color(MathUtils.random(0.8f, 1), MathUtils.random(0.8f, 1), MathUtils.random(0.8f, 1), 1);
        headSlot.getColor().set(headColor);
    }




    @Override
    public void act(float delta) {

        // Change head attachment for enemies that are about to die.
        if (enemy.hp == 1 && enemy.type != Type.weak) headSlot.setAttachment(burstHeadAttachment);

        // Change color for big enemies.
        if (enemy.type == Type.big)
            headSlot.getColor().set(headColor).lerp(0, 1, 1, 1, 1 - enemy.bigTimer / Enemy.bigDuration);

        getSkeleton().setX(enemy.position.x + Enemy.width / 2);
        getSkeleton().setY(enemy.position.y);

        if (!setAnimation(assets.enemyStates.get(enemy.state), enemy.stateChanged)) getAnimationState().update(delta);
        getAnimationState().apply(getSkeleton());

        Bone root = getSkeleton().getRootBone();
        root.setScaleX(root.getScaleX() * enemy.size);
        root.setScaleY(root.getScaleY() * enemy.size);
//
        getSkeleton().setScaleX(enemy.dir);
        getSkeleton().updateWorldTransform();
//        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
//		super.draw(batch, parentAlpha);
        getSkeleton().getColor().a = Math.min(1, enemy.deathTimer / Enemy.fadeTime);
        getRenderer().draw(batch, getSkeleton());

    }
}
