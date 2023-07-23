package cd2;

import cd2.Scenes.GameScene;
import cd2.Scenes.StartUpScene;
import info.u250.c2d.engine.Engine;
import info.u250.c2d.engine.EngineDrive;
import info.u250.c2d.engine.resources.AliasResourceManager;

public class TankEngineDrive implements EngineDrive {
    static final String FLAT = "paint/S.png";

    GameScene gameScene = null;
    StartUpScene startUpScene = null;
    @Override
    public EngineOptions onSetupEngine() {
        EngineOptions opt = new EngineOptions(new String[]{
                "data/all.atlas"
                ,"spineboy/spineboy-pma.atlas"
                , "data/all.pp"
                , "data/fnt/big.fnt"
                , "data/fnt/menu.fnt"
                , "data/fnt/foot.fnt"
                , "data/music/battle.ogg"
                , "data/music/bg.ogg"
                , "data/music/collection-pre.ogg"
                , "data/music/cont.ogg"
                , "data/music/timer.ogg"
                , "data/sounds/boss-break.ogg"
                , "data/sounds/boss-xo.ogg"
                , "data/sounds/choose-guide.ogg"
                , "data/sounds/choose-pack1.ogg"
                , "data/sounds/choose-pack2.ogg"
                , "data/sounds/click.ogg"
                , "data/sounds/coin.ogg"
                , "data/sounds/collection.ogg"
                , "data/sounds/die.ogg"
                , "data/sounds/dig.ogg"
                , "data/sounds/dispose.wav"
                , "data/sounds/fail.ogg"
                , "data/sounds/func.ogg"
                , "data/sounds/hurt.ogg"
                , "data/sounds/ka_dock.ogg"
                , "data/sounds/lvl5-bang.ogg"
                , "data/sounds/meetf.ogg"
                , "data/sounds/new.ogg"
                , "data/sounds/newcon.ogg"
                , "data/sounds/newnpc.ogg"
                , "data/sounds/shot.ogg"
                , "data/sounds/teleport.ogg"
                , "data/sounds/trans.ogg"
                , "data/sounds/win.ogg"
                , "paint/S.png"}, 960.0F, 540.0F);
        opt.configFile = "info.u250.digs.cfg";
        opt.useGL20 = true;
        opt.autoResume = true;
        opt.catchBackKey = true;
        opt.debug = false;
        return opt;
    }

    @Override
    public void onLoadedResourcesCompleted() {
        Engine.getLanguagesManager().setLang("zh_CN");
        Engine.getLanguagesManager().load("vx");
//        this.gameScene = new GameScene();
        this.startUpScene = new StartUpScene(this);
        Engine.setMainScene(this.startUpScene);
//        Digs.delayPlayActorSound();
    }

    @Override
    public void onResourcesRegister(AliasResourceManager<String> reg) {
        reg.textureAtlas("All", "data/all.atlas");
        reg.textureAtlas("spineboy-ess", "spineboy/spineboy-pma.atlas");
    }

    @Override
    public void dispose() {

    }
}
