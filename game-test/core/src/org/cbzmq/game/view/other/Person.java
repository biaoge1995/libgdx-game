package org.cbzmq.game.view.other;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

/**
 * @ClassName Person
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/20 11:08 上午
 * @Version 1.0
 **/

public class Person extends Actor {

    private Animation<TextureRegion> animation;
    private TextureRegion current;
    private float stateTime;


    public Person(Animation<TextureRegion> animation) {
        this.animation = animation;
        setDefaultFrame();
    }
    public Person(Array<? extends TextureRegion> keyFrames) {
        this.animation = new Animation<>(0.2F,keyFrames);
        this.animation.setPlayMode(Animation.PlayMode.LOOP);
        setDefaultFrame();

    }
    public Person(AssetManager assetManager){
        this(assetManager.get(Res.Atlas.ATLAS_PATH, TextureAtlas.class).findRegions(Res.Atlas.IMAGE_BIRD_YELLOW_01_TO_03));
    }


    public void setDefaultFrame(){
        if(animation!=null)
        current = animation.getKeyFrame(0);
    }


    /**
     * 单线程里面先 act 再执行了draw方法
     * @param delta
     */
    @Override
    public void act(float delta) {
        super.act(delta);
        if(animation!=null){
            stateTime+=delta;
            current = animation.getKeyFrame(stateTime);
        }
    }

    /**
     * 单线程里面先 act -> draw
     * @param batch parentAlpha
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(current, getX(), getY());
//        Gdx.app.log(MainGame.TAG,
//                "person x:"+this.getX()+" y:"+this.getY()
//                        +" originX:"+this.getOriginX()+" originY:"+ this.getOriginY()+""
//                        +" width:"+getWidth()+" height: "+ getHeight()
//                        +" scaleX:"+this.getScaleX()+" scaleY:"+this.getScaleY()
//                        +" rotation:"+this.getRotation());

//        batch.draw(current
//                ,getX(), getY(),
//                getOriginX(), getOriginY(),
//                getWidth(), getHeight(),
//                getScaleX(), getScaleY(),
//                getRotation()
//                );

    }

}
