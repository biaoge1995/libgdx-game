package org.cbzmq.game.actor.other;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import org.cbzmq.game.actor.other.Res;
import org.cbzmq.game.actor.other.GameScreen;


/**
 * @ClassName MainGame
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/20 11:00 上午
 * @Version 1.0
 **/
public class MainGame extends Game {

    private AssetManager assetManager;
    private TextureAtlas atlas;
    public final static String TAG = "myGame";
    private GameScreen gameScreen;

    /** 世界宽度 */
    private float worldWidth;
    /** 世界高度 */
    private float worldHeight;


    @Override
    public void create() {
        // 为了不压扁或拉长图片, 按实际屏幕比例计算世界宽高
        worldWidth = Res.FIX_WORLD_WIDTH;
        worldHeight = Gdx.graphics.getHeight() * worldWidth / Gdx.graphics.getWidth();
        Gdx.app.log(TAG,"资源加载器");
        assetManager = new AssetManager();
        assetManager.load(Res.Atlas.ATLAS_PATH, TextureAtlas.class);
        assetManager.load(Res.Atlas.ATLAS_SPINE_CONTRA, TextureAtlas.class);
        assetManager.load(Res.FPS_BITMAP_FONT_PATH, BitmapFont.class);
        //等待加载完成
        assetManager.finishLoading();
        //获取地图集

        gameScreen = new GameScreen(this);
        this.setScreen(gameScreen);
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }



    @Override
    public void render() {
        // 黑色清屏
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render();
    }

    public float getWorldWidth() {
        return worldWidth;
    }

    public void setWorldWidth(float worldWidth) {
        this.worldWidth = worldWidth;
    }

    public float getWorldHeight() {
        return worldHeight;
    }

    public void setWorldHeight(float worldHeight) {
        this.worldHeight = worldHeight;
    }
}
