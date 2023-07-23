package awt.model.msg;

import awt.proto.enums.UserAction;
import lombok.Data;

@Data
public class UserActionMsg {
    private int playId;
    private UserAction userAction;
    private String uuid;
    private long time;

    public UserActionMsg(int playId, UserAction userAction) {
        this.playId = playId;
        this.userAction = userAction;
    }

    public UserActionMsg(int playId, UserAction userAction, String uuid, long time) {
        this.playId = playId;
        this.userAction = userAction;
        this.uuid = uuid;
        this.time = time;
    }
}
