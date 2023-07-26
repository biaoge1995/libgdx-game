package org.cbzmq.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

/**
 * @ClassName GameCamera
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/25 11:40 下午
 * @Version 1.0
 **/

public class GameCamera extends OrthographicCamera {
    public static float cameraMinWidth = 16, cameraMaxWidth = 28, cameraHeight = 16, cameraZoom = 0.4f, cameraZoomSpeed = 0.5f;
    public static float cameraBottom = 2, cameraTop = 7, cameraMinX = 1;
    public static float cameraLookahead = 0.75f, cameraLookaheadSpeed = 8f, cameraLookaheadSpeedSlow = 3f;
    public static float cameraSpeed = 5f, cameraShake = 6 * Constants.scale;
    public float shakeX, shakeY, lookahead, zoom = 1;

    public void shackCamera(){
        this.position.sub(shakeX, shakeY, 0);
        shakeX += cameraShake * (MathUtils.randomBoolean() ? 1 : -1);
        shakeY += cameraShake * (MathUtils.randomBoolean() ? 1 : -1);
        this.position.add(shakeX, shakeY, 0);
    }


}
