package org.cbzmq.game.view.other;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

/**
 * @ClassName Info
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/21 5:09 下午
 * @Version 1.0
 **/

public class Info extends Actor {

    private SpriteBatch batch;

    private BitmapFont fpsBitmapFont;

    private float scaledFontSize;
    private float offset;

    private Array<String> infos;

    /**
     * 文本高度占屏幕高度的比例
     */
    private static final float OCCUPY_HEIGHT_RATIO = 14.0F / 480.0F;

    /**
     * 显示的文本偏移右下角的X轴和Y轴比例(相对于字体高度的比例)
     */
    private static final float DISPLAY_ORIGIN_OFFSET_RATIO = 0.5F;

    // 帧率字体绘制的位置
    private float x;
    private float y;

    private String text;
    private int counter =0;

    public void setInfos(Array<String> infos) {
        this.infos = infos;
    }

    public Info(BitmapFont fpsBitmapFont, int fontRawPixSize) {
        this.infos = new Array<>();
        this.batch = new SpriteBatch();
        this.fpsBitmapFont = fpsBitmapFont;
        // 计算帧率文本显示位置（为了兼容所有不同尺寸的屏幕）
        float height = Gdx.graphics.getHeight();
        float scale = (height * OCCUPY_HEIGHT_RATIO) / (float) fontRawPixSize;
        this.fpsBitmapFont.getData().setScale(scale);
        scaledFontSize = fontRawPixSize * scale;
        offset = scaledFontSize * DISPLAY_ORIGIN_OFFSET_RATIO;
        x = scaledFontSize - offset;
        y = scaledFontSize * 1.85F - offset;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        int framesPerSecond = Gdx.graphics.getFramesPerSecond();
        fpsBitmapFont.draw(batch, "FPS: " + framesPerSecond, x, y);

        Array.ArrayIterator<String> iterator = infos.iterator();
        int i=1;
        if(text!=null){
            fpsBitmapFont.draw(batch, text, x, y + 1*scaledFontSize);
            i++;
        }
        while (iterator.hasNext()){
            String next = iterator.next();
            fpsBitmapFont.draw(batch, next, x, y + i*scaledFontSize);
            i++;
        }

        int mod = 0;

        if(Gdx.graphics.getFramesPerSecond()!=0 ){
            framesPerSecond = Gdx.graphics.getFramesPerSecond();
            mod = counter/framesPerSecond;
        }
        if(mod ==1 ){
            if(infos.size>0)
            infos.removeIndex(0);
            counter=0;
        }
        counter++;

    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
