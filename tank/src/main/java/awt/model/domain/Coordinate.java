package awt.model.domain;

import lombok.Data;

/**
 * @author chenbiao
 * @date 2020-12-19 14:56
 * 物体坐标
 */
@Data
public class Coordinate {
    private int x1Coordinate; // 物体X坐标
    private int x2Coordinate; // 物体X2坐标
    private int y1Coordinate; // 物体Y坐标
    private int y2Coordinate; // 物体Y2坐标
}
