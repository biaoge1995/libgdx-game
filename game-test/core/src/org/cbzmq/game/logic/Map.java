package org.cbzmq.game.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import org.cbzmq.game.utils.MapContext;


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

    private MapContext mapContext;

    public static void main(String[] args) {

        System.load("D:\\work\\code\\libgdx-game\\game-test\\core\\src\\main\\resources\\gdx64.dll");
//        System.load("/Users/chenbiao/libgdx-game/game-test/core/src/main/resources/liblwjgl_opengl.dylib");
//        System.load("/Users/chenbiao/libgdx-game/game-test/core/src/main/resources/libglfw.dylib");
//        System.load("/Users/chenbiao/libgdx-game/game-test/core/src/main/resources/libglfw_async.dylib");
//        System.load("/Users/chenbiao/libgdx-game/game-test/core/src/main/resources/libjemalloc.dylib");
//
//        System.load("/Users/chenbiao/libgdx-game/game-test/core/src/main/resources/liblwjgl_stb.dylib");
//        System.load("/Users/chenbiao/libgdx-game/game-test/core/src/main/resources/libopenal.dylib");

        Gdx.files = new Lwjgl3Files();
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        new Lwjgl3Graphics(null);
        Gdx.gl = Gdx.gl20 = Gdx.gl30 = Gdx.gl31 = Gdx.gl32 = new Lwjgl3GL32();

        System.out.println("load finish");
    }

    public Map(){

        MapContext mapContext;
        try {
            mapContext = new MapContext("assets/map/map.xlsx");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        mapContext.loadGameMap();
        this.mapContext = mapContext;
    }

    public void load(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
//        this.mapLoader = new TmxMapLoader();
//        this.tiledMap = mapLoader.load("assets/map/desert/desert.tmx");
//        tiledMap = new AtlasTmxMapLoader().load("assets/map/map.tmx");
        collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get(Constants.mapCollisionLayer);


        int width = collisionLayer.getWidth();
        int height = collisionLayer.getHeight();

//        byte[][] cells =  new byte[width][height];
        for (int y = 0; y <= height; y++) {
            mapContext.createRow(y);
            for (int x = 0; x <= width; x++) {

                TiledMapTileLayer.Cell cell = collisionLayer.getCell(x, y);

                if (cell != null) {
                    mapContext.writeCell(x,"1");
                }else {
                    mapContext.writeCell(x,"");
                }

            }
        }
        mapContext.writeXy(width,height);
        mapContext.finishFlush();
        System.out.println("地图加载完毕");
    }

    /**
     * Returns rectangles for the tiles within the specified area.
     */
    public Array<Rectangle> getCollisionTiles(int startX, int startY, int endX, int endY) {
        Pools.freeAll(tiles, true);
        tiles.clear();
        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
//                TiledMapTileLayer.Cell cell = collisionLayer.getCell(x, y);
                byte cell1 = mapContext.getCell(x, y);
                if (cell1==1) {
                    Rectangle rect = Pools.obtain(Rectangle.class);
                    rect.set(x, y, 1, 1);
                    tiles.add(rect);
                }
            }
        }
        return tiles;
    }

}
