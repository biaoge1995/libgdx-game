package awt.model.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName Group
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/6/26 4:40 下午
 * @Version 1.0
 **/
@Data
public class Group {
    private String Name;
    private Map<Player,Set<Element>> playerEle = new HashMap<>();

    public Group(String name) {
        Name = name;
    }

    /**
     * 给组添加成员
     * @param player
     * @param elements
     */
    public void addPlayerAndElement(Player player,Set<Element> elements){
        playerEle.put(player,elements);
    }
}
