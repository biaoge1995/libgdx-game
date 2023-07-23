package awt.model.domain;

import lombok.Data;

/**
 * 距离
 */
@Data
public class Distance{
    private int distanceX;
    private int distanceY;
    public Distance(int distanceX,int distanceY){
        this.distanceX = distanceX;
        this.distanceY = distanceY;

    }
}

