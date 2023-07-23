package com.libgdxloadingscreen;


import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.matsemann.libgdxloadingscreen.SomeCoolGame;

/**
 * @author Mats Svensson
 */
public class DesktopStarter {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("libgdx-loading-screen");


        cfg.setWindowedMode(800, 480); // 窗口宽度
        cfg.setResizable(false);// 窗口设置为大小不可改变

        new Lwjgl3Application(new SomeCoolGame(), cfg);
    }
}
