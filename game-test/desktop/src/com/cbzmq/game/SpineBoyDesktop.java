package com.cbzmq.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import org.cbzmq.game.GameServer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

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
        float scale = 1.4f;
        config.setWindowedMode((int)(800*scale),(int)(450*scale));
        new Lwjgl3Application(new GameServer(), config);
    }
}
