package com.cbzmq.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import org.cbzmq.game.SuperSpineBoyGame;

/**
 * @ClassName SpineBoyDesktop
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/25 12:35 上午
 * @Version 1.0
 **/
public class SpineBoyDesktop {
    public static void main (String[] args) throws Exception {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Super Spineboy");
        config.setWindowedMode(800,450);
        new Lwjgl3Application(new SuperSpineBoyGame(), config);
    }
}
