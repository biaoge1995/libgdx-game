package awt.model.domain;

import awt.proto.enums.ElementType;

import java.awt.*;

/**
 * @author chenbiao
 * @date 2020-12-24 17:15
 */
public class GrassLand extends Element{
    public GrassLand(int mapX1Coordinate,int mapY1Coordinate){
        this(mapX1Coordinate,mapY1Coordinate,25,25);
    }

    public GrassLand(int mapX1Coordinate,int mapY1Coordinate, int height, int wide) {
        super(ElementType.GRASS_LAND, mapX1Coordinate, mapY1Coordinate, height, wide);
        this.setDrawRank(1);
        this.setBlood(1);
        this.setFullBlood(1);
        this.setBuild(true);
        this.setColor(Color.green);
    }

    public GrassLand(Element element) {
        this.setBuild(true);
        toElementSon(element, this);
    }
    public GrassLand(){
        this.setBuild(true);
    }


    @Override
    public boolean bePassedThrough(Element other) {
        if(other.isLive()){
            return true;
        }
        return false;
    }
}
