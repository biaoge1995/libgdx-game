package cd2.Scenes;

import cd2.TankEngineDrive;
import cd2.ui.SpineActor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import info.u250.c2d.engine.Engine;
import info.u250.c2d.engine.EngineDrive;
import info.u250.c2d.engine.SceneStage;
import info.u250.c2d.graphic.WebColors;


public class StartUpScene extends SceneStage {
    private TankEngineDrive drive;
    TextureAtlas atlas;
    float deltaAppend;
    final SpineActor wmr;
    public StartUpScene(final TankEngineDrive tankEngineDrive) {
        this.drive = tankEngineDrive;
        this.atlas = (TextureAtlas)Engine.resource("spineboy-ess");
        this.wmr = new SpineActor("spineboy/spineboy-ess", this.atlas, "run", 0.6F);
        this.wmr.setColor(WebColors.DARK_GREEN.get());
        this.wmr.setX(200);
        this.wmr.setY(0);
        this.addActor(wmr);
    }

    @Override
    public void show() {
        super.show();
//        Engine.getMusicManager().playMusic("MusicBackground", true);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw() {
        super.draw();
    }
}
