package cd2;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import info.u250.c2d.engine.Engine;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class TankDesktop {
    public static void main(String[] args) {
        Tank game = new Tank();
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = (int) Engine.getWidth();
        config.height = (int)Engine.getHeight();
        config.useGL30 = false;
        new LwjglApplication(game, config);
    }
}
