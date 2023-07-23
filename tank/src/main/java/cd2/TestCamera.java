package cd2;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;


public class TestCamera extends ApplicationAdapter {

    private SpriteBatch spriteBatch;
    private Sprite sprite;
    private OrthographicCamera camera;
    private static final float Weight = 1000;
    private static final float Height = 600;
    private Stage stage;
    private float rotationSpeed = 0.5f;


    public static void main(String[] args) {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.width = (int)Weight;
        configuration.height = (int)Height;
        new LwjglApplication(new TestCamera(),configuration);
    }

    @Override
    public void create() {
//        super.create();

//        stage = new Stage(new StretchViewport(Weight,Height),spriteBatch);
        Texture texture = new Texture(Gdx.files.internal("map/img.png"));
        sprite = new Sprite(texture);
//        Image actor = new Image(texture);
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        System.out.println(w+","+h);
        sprite.setPosition(0,0);
        sprite.setSize(Weight,Height);

        camera = new OrthographicCamera(300f,300f*(h/w));
        System.out.println(camera.viewportWidth+","+camera.viewportHeight);
        camera.position.set(camera.viewportWidth/2f,camera.viewportHeight/2f,0);
//        stage.addActor(actor);
//        spriteBatch.setProjectionMatrix(camera.combined);
        camera.update();
        spriteBatch = new SpriteBatch();
        spriteBatch.setProjectionMatrix(camera.combined);


    }

    @Override
    public void render() {
//        spriteBatch.begin();
        handleInput();
        camera.update();
        System.out.println(camera.viewportWidth + "," + camera.viewportHeight);
        spriteBatch.setProjectionMatrix(camera.combined);
//        System.out.println(camera.combined);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        sprite.draw(spriteBatch);
        spriteBatch.end();
//        spriteBatch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = 300f;                 // Viewport of 30 units!
        camera.viewportHeight = 300f * height/width; // Lets keep things in proportion.
        camera.update();
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.zoom += 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom -= 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.translate(-3, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.translate(3, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.translate(0, -3, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.translate(0, 3, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.rotate(-rotationSpeed, 0, 0, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            camera.rotate(rotationSpeed, 0, 0, 1);
        }

//        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 100/camera.viewportWidth);
//
//        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
//        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;
//
//        camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, 100 - effectiveViewportWidth / 2f);
//        camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, 100 - effectiveViewportHeight / 2f);
    }
}
