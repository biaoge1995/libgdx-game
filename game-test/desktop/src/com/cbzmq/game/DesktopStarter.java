package com.cbzmq.game;


import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.matsemann.libgdxloadingscreen.SomeCoolGame;
import org.cbzmq.game.MainGame;

/**
 * @author Mats Svensson
 */
public class DesktopStarter {
    /** 窗口宽度参考值 */
    public static final float PIX_WIDTH = 320;

    /** 窗口宽高比, 适当调节宽高比可以查看在不同屏幕上的效果, 例如设置为 9:16, 3:4, 2:3 */
    public static final float RATIO = 2.0F / 3.0F;

    /** 适当改变缩放比以适应自己的电脑屏幕 */
    public static final float SCALE = 1.0F;


    public static void main (String[] args) {

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        config.setWindowedMode((int) (PIX_WIDTH * SCALE), (int) ((PIX_WIDTH / RATIO) * SCALE)); // 窗口宽度
        config.setResizable(false);// 窗口设置为大小不可改变
        config.setTitle("Flappy Bird");        // 手动设置窗口标题, 如果没有设置则会使用游戏程序入口类的类名作为标题

        // 手动设置设置窗口图标
        // config.addIcon(path, fileType);

        new Lwjgl3Application(new MainGame(), config);
    }
}
