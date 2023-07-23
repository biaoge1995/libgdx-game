package awt.model.msg;

import lombok.Data;

/**
 * @author chenbiao
 * @date 2020-12-19 16:48
 */
@Data
public class Msg {
    private String header;
    private String content;

    public Msg(String header, String content) {
        this.header = header;
        this.content = content;
    }
}
