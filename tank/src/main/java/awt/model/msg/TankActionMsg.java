package awt.model.msg;

import awt.proto.enums.TankAction;
import lombok.Data;

/**
 * @author chenbiao
 * @date 2020-12-19 21:04
 */
@Data
public class TankActionMsg {
    private int id;
    private TankAction tankAction;
    private String uuid;
    private long time;

    public TankActionMsg(int id, TankAction tankAction) {
        this.id = id;
        this.tankAction = tankAction;
    }

    public TankActionMsg(int id, TankAction tankAction, String uuid, long time) {
        this.id = id;
        this.tankAction = tankAction;
        this.uuid = uuid;
        this.time = time;
    }
}
