package awt.model.domain;

import lombok.Data;

/**
 * @ClassName Line2D
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/3/27 2:05 下午
 * @Version 1.0
 **/
@Data
public class Line2D {
    private double x1;
    private double y1;
    private double x2;
    private double y2;

    public Line2D(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
}
