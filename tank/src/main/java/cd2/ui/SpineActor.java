package cd2.ui;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.Event;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonBinary;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonRenderer;

public class SpineActor extends Actor {
    float time;
    SkeletonRenderer renderer = new SkeletonRenderer();
    SkeletonData skeletonData;
    Skeleton skeleton;
    Animation animation;
    Array<Event> events = new Array();

    public SpineActor(String name, TextureAtlas atlas, String defaultAnimation, float scale) {
        SkeletonBinary binary = new SkeletonBinary(atlas);
        binary.setScale(scale);
        FileHandle internal = Gdx.files.internal(name + ".skel");
        this.skeletonData = binary.readSkeletonData(internal);
        this.animation = this.skeletonData.findAnimation(defaultAnimation);
        this.skeleton = new Skeleton(this.skeletonData);
        if (name.equals("goblins")) {
            this.skeleton.setSkin("goblin");
        }

        this.skeleton.setToSetupPose();
        this.skeleton = new Skeleton(this.skeleton);
        this.skeleton.updateWorldTransform();
    }

    public void act(float delta) {
        this.skeleton.setX(this.getX());
        this.skeleton.setY(this.getY());
        float lastTime = this.time;
        this.time += delta;
        this.events.clear();
        this.animation.apply(this.skeleton, lastTime, this.time, true, this.events,1, Animation.MixBlend.first, Animation.MixDirection.in);
        if (this.events.size > 0) {
            System.out.println(this.events);
        }

        this.skeleton.updateWorldTransform();
        this.skeleton.update(Gdx.graphics.getDeltaTime());
        super.act(delta);
    }

    public void setColor(Color color) {
        super.setColor(color);
        this.skeleton.getColor().set(color);
    }

    public void draw(Batch batch, float parentAlpha) {
        this.renderer.draw((SpriteBatch)batch, this.skeleton);
    }
}

