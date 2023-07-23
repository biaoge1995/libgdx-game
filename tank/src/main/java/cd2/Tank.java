package cd2;

import info.u250.c2d.engine.Engine;
import info.u250.c2d.engine.EngineDrive;

public class Tank extends Engine {
    @Override
    protected EngineDrive onSetupEngineDrive() {
        return new TankEngineDrive();
    }
}
