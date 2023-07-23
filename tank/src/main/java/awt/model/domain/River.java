package awt.model.domain;

import awt.proto.enums.ElementType;

import java.awt.*;

/**
 * @author chenbiao
 * @date 2020-12-24 16:31
 */
public class River extends Element {

    public River(int mapX1Coordinate,int mapY1Coordinate){
        this(mapX1Coordinate,mapY1Coordinate,25,25);
    }

    public River(int mapX1Coordinate,int mapY1Coordinate,int height,int wide) {
        super(ElementType.RIVER, mapX1Coordinate, mapY1Coordinate, height, wide);
        this.setBlood(10);
        this.setHardness(1);
        this.setBuild(true);
        this.setColor(Color.BLUE);
    }
    public River(Element element) {
        toElementSon(element, this);
        this.setBuild(true);
    }


    @Override
    public boolean bePassedThrough(Element other) {
       if(other.isCanFly()){
           return true;
       }
       return false;
    }


}
