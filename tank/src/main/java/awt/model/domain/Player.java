package awt.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author chenbiao
 * @date 2020-12-19 11:53
 */
@Data
public class Player implements Serializable {
    private String Ip;
    //昵称
    private String  name;
    //玩家id
    private int id;
    private boolean isReady=false;



    public Player(String ip, int id) {
        Ip = ip;
        this.id = id;
    }

    public Player() {
    }

    public Player(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getIp()+"-"+name+"-"+getId();
    }
}
