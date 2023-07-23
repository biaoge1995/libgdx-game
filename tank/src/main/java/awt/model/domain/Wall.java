package awt.model.domain;

import awt.proto.enums.ElementType;

import java.awt.*;

/**
 * @author chenbiao
 * @date 2020-12-23 19:18
 */
public class Wall extends Element{
    private boolean isBuild = true;

    public Wall(int mapX1Coordinate,int mapY1Coordinate){
        this(mapX1Coordinate,mapY1Coordinate,25,25);
    }

    public Wall(int mapX1Coordinate,int mapY1Coordinate, int height, int wide) {
        super(ElementType.WALL, mapX1Coordinate, mapY1Coordinate, height, wide);
        this.setBlood(10);
        this.setHardness(1);
        this.setColor(new Color(96,56,17));
        this.setBuild(true);
    }

    public Wall(Element element) {
        this.setBuild(true);
        toElementSon(element, this);
    }




    public static void main(String[] args) {
        Element wall = new Wall(1, 1, 1, 1);
        System.out.println(wall);
    }
}
