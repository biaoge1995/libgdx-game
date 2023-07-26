package org.cbzmq.game;

import com.badlogic.gdx.maps.tiled.AtlasTmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;


/**
 * @ClassName Map
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/24 11:28 下午
 * @Version 1.0
 **/
public class Map {
    public TiledMap tiledMap;
    public TiledMapTileLayer collisionLayer;
    Array<Rectangle> tiles = new Array();

    public Map(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
        collisionLayer = (TiledMapTileLayer)tiledMap.getLayers().get(Constants.mapCollisionLayer);
    }

    /** Returns rectangles for the tiles within the specified area. */
    public Array<Rectangle> getCollisionTiles (int startX, int startY, int endX, int endY) {
        Pools.freeAll(tiles, true);
        tiles.clear();
        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                TiledMapTileLayer.Cell cell = collisionLayer.getCell(x, y);
                if (cell != null) {
                    Rectangle rect = Pools.obtain(Rectangle.class);
                    rect.set(x, y, 1, 1);
                    tiles.add(rect);
                }
            }
        }
        return tiles;
    }

}
