package org.cbzmq.game.ui;



import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.*;
import com.esotericsoftware.spine.AnimationState.AnimationStateAdapter;
import com.esotericsoftware.spine.attachments.Attachment;
import org.cbzmq.game.logic.Assets;
import org.cbzmq.game.enums.EnemyType;
import org.cbzmq.game.model.Enemy;


/**
 * The view class for an enemy.
 */
public class EnemyActor extends BaseSkeletonActor<Enemy> {
    public Animation hitAnimation;
    public Slot headSlot;
    public Attachment burstHeadAttachment;
    public Color headColor;

    private Enemy enemy;

    public EnemyActor(Enemy enemy) {
        super(enemy);
        this.enemy = enemy;

    }

    @Override
    public void loadAssets(Assets assets) {
        super.loadAssets(assets);
        setSkeleton(new Skeleton(assets.enemySkeletonData));
        burstHeadAttachment = getSkeleton().getAttachment("head", "burst01");
        headSlot = getSkeleton().findSlot("head");
        hitAnimation = getSkeleton().getData().findAnimation("hit");

        setAnimationState(new AnimationState(assets.enemyAnimationData));

        // Play squish sound when enemies die.

        getAnimationState().addListener(new AnimationStateAdapter() {
            public void event(AnimationState.TrackEntry entry, Event event) {
                if (event.getData().getName().equals("squish") ) Assets.SoundEffect.squish.play();
                else if(event.getData().getName().equals("hit")) Assets.SoundEffect.hurtAlien.play();
            }
        });

        // Enemies have slight color variations.
        if (enemy.enemyType == EnemyType.strong)
            headColor = new Color(1, 0.6f, 1, 1);
        else
            headColor = new Color(MathUtils.random(0.8f, 1), MathUtils.random(0.8f, 1), MathUtils.random(0.8f, 1), 1);
        headSlot.getColor().set(headColor);
    }

    @Override
    public void act(float delta) {

        // Change head attachment for enemies that are about to die.
        if (getModel().hp == 1 && getModel().enemyType != EnemyType.weak) headSlot.setAttachment(burstHeadAttachment);

        // Change color for big enemies.
        if (getModel().enemyType == EnemyType.big)
            headSlot.getColor().set(headColor).lerp(0, 1, 1, 1, 1 - getModel().bigTimer / Enemy.bigDuration);

        getSkeleton().setX(getModel().position.x + Enemy.width / 2);
        getSkeleton().setY(getModel().position.y);

        if (!setAnimation(getAssets().enemyStates.get(getModel().state), getModel().stateChanged)) {
            AnimationState animationState = getAnimationState();
            animationState.update(delta);

        }
        getAnimationState().apply(getSkeleton());

        Bone root = getSkeleton().getRootBone();
        root.setScaleX(root.getScaleX() * getModel().size);
        root.setScaleY(root.getScaleY() * getModel().size);
//
        getSkeleton().setScaleX(getModel().dir);
        getSkeleton().updateWorldTransform();
//        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
//		super.draw(batch, parentAlpha);
        Skeleton skeleton = getSkeleton();
        skeleton.getColor().a = Math.min(1, getModel().deathTimer / Enemy.fadeTime);
        getRenderer().draw(batch, skeleton);


    }

    public void beHit () {
//        Assets.SoundEffect.hurtAlien.play();
        if (hitAnimation != null) {
            AnimationState.TrackEntry entry = getAnimationState().setAnimation(1, hitAnimation, false);
            entry.setTrackEnd(hitAnimation.getDuration());
        }
    }

    public void setModel(Enemy model) {
        setModel(model);
        this.enemy = model;
    }
}
