package cd2;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.esotericsoftware.spine.*;
import com.esotericsoftware.spine.attachments.AtlasAttachmentLoader;
import com.esotericsoftware.spine.attachments.Attachment;
import com.esotericsoftware.spine.attachments.AttachmentLoader;
import com.esotericsoftware.spine.attachments.RegionAttachment;

import java.net.URL;

public class test extends ApplicationAdapter {

    private SpriteBatch batch;
    private TextureAtlas textureAtlas;
    private OrthographicCamera camera;
    private Skeleton skeleton;
    private Animation animation;
    private Box2DDebugRenderer box2dRenderer;
    private SkeletonRenderer skeletonRenderer;
    private World world;
    private float time;
    private Array<Event> events = new Array();
    Vector2 vector = new Vector2();

    @Override
    public void create() {

        batch = new SpriteBatch();
        box2dRenderer = new Box2DDebugRenderer();
        skeletonRenderer = new SkeletonRenderer();
        textureAtlas = new TextureAtlas(Gdx.files.internal("spineboy/spineboy-pma.atlas"));
        AtlasAttachmentLoader atlasAttachmentLoader = new AtlasAttachmentLoader(textureAtlas){
            public RegionAttachment newRegionAttachment (Skin skin, String name, String path) {
                Box2dAttachment attachment = new Box2dAttachment(name);
                TextureAtlas.AtlasRegion region = textureAtlas.findRegion(attachment.getName());
                if (region == null) throw new RuntimeException("Region not found in atlas: " + attachment);
                attachment.setRegion(region);
                return attachment;
            }
        };


        SkeletonBinary skeletonBinary = new SkeletonBinary(atlasAttachmentLoader);
        skeletonBinary.setScale(0.6f * 0.05f);
        SkeletonData skeletonData = skeletonBinary.readSkeletonData(Gdx.files.internal("spineboy/spineboy-ess.skel"));

        animation = skeletonData.findAnimation("walk");
        skeleton = new Skeleton(skeletonData);
        skeleton.setPosition(0, 16);

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        System.out.println(width+","+height);
        camera = new OrthographicCamera(48, 32);
        camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2,0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        createWord();

        for (Slot slot : skeleton.getSlots()) {
            if (!(slot.getAttachment() instanceof Box2dAttachment)) continue;
            Box2dAttachment attachment = (Box2dAttachment)slot.getAttachment();

            PolygonShape boxPoly = new PolygonShape();
            boxPoly.setAsBox(attachment.getWidth() / 2 * attachment.getScaleX(), attachment.getHeight() / 2 * attachment.getScaleY(),
                    vector.set(attachment.getX(), attachment.getY()), attachment.getRotation() * MathUtils.degRad);

            BodyDef boxBodyDef = new BodyDef();
            boxBodyDef.type = BodyDef.BodyType.StaticBody;
            attachment.body = world.createBody(boxBodyDef);
            attachment.body.createFixture(boxPoly, 1);

            float x = slot.getBone().getWorldX();
            float y = slot.getBone().getWorldY();
            float rotation = slot.getBone().getWorldRotationX();
            System.out.println("初始化"+attachment.getName()+"x:"+x+",y:"+y+",rotation:"+rotation);

            boxPoly.dispose();
        }
//        skeleton.updateWorldTransform();

    }

    public void createWord(){
        world = new World(new Vector2(0, -1), true);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(24,16);
        Body body = world.createBody(bodyDef);
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(1f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 1.0f;
        fixtureDef.restitution = 0.6f;
        body.createFixture(fixtureDef);
        circleShape.dispose();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void render() {

        float deltaTime = Gdx.graphics.getDeltaTime();
        float remaining = deltaTime;
        while (remaining > 0) {
            float d = Math.min(0.016f, remaining);
            world.step(d, 8, 3);
            time += d;
            remaining -= d;
        }


        ScreenUtils.clear(0, 0, 0, 0);
        batch.setProjectionMatrix(camera.combined);
        batch.setTransformMatrix(camera.view);
        camera.update();
        time += deltaTime;


//        System.out.println(time);
        batch.begin();
        animation.apply(skeleton, time, time, true, events, 1, Animation.MixBlend.first, Animation.MixDirection.in);
        float v = skeleton.getX() + 8 * deltaTime;
        skeleton.setX(v);
        skeleton.updateWorldTransform();
        System.out.println(skeleton.getX()+","+skeleton.getY());
        skeletonRenderer.draw(batch, skeleton);
        batch.end();


        // Position each attachment body.
        for (Slot slot : skeleton.getSlots()) {
            if (!(slot.getAttachment() instanceof Box2dAttachment)) continue;
            Box2dAttachment attachment = (Box2dAttachment)slot.getAttachment();
            if (attachment.body == null) continue;
            float x = slot.getBone().getWorldX();
            float y = slot.getBone().getWorldY();
            float rotation = slot.getBone().getWorldRotationX();

            attachment.body.setTransform(x, y, rotation * MathUtils.degRad);

        }
        box2dRenderer.render(world, camera.combined);

    }

    static class Box2dAttachment extends RegionAttachment {
        Body body;

        public Box2dAttachment (String name) {
            super(name);
        }
    }

    public static void main(String[] args) {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.width = 500;
        configuration.height = 500;
        new LwjglApplication(new test(), configuration);
    }
}
