package org.cbzmq.game.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Files;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3GL31;
import com.badlogic.gdx.maps.tiled.AtlasTmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
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

    public static void main(String[] args) {

        System.load("/Users/chenbiao/libgdx-game/game-test/core/src/main/resources/libgdxarm64.dylib");
        System.load("/Users/chenbiao/libgdx-game/game-test/core/src/main/resources/liblwjgl_opengl.dylib");
        System.load("/Users/chenbiao/libgdx-game/game-test/core/src/main/resources/libglfw.dylib");
        System.load("/Users/chenbiao/libgdx-game/game-test/core/src/main/resources/libglfw_async.dylib");
        System.load("/Users/chenbiao/libgdx-game/game-test/core/src/main/resources/libjemalloc.dylib");

        System.load("/Users/chenbiao/libgdx-game/game-test/core/src/main/resources/liblwjgl_stb.dylib");
        System.load("/Users/chenbiao/libgdx-game/game-test/core/src/main/resources/libopenal.dylib");

        Gdx.files = new Lwjgl3Files();
        Gdx.gl = new Lwjgl3GL31();
        new Map();
    }

    public Map() {
        tiledMap = new AtlasTmxMapLoader().load("/Users/chenbiao/libgdx-game/game-test/assets/map/map.tmx");
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
