package org.cbzmq.game.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import org.cbzmq.game.Assets;
import org.cbzmq.game.Constants;
import org.cbzmq.game.Map;
import org.cbzmq.game.model.*;
import org.cbzmq.game.model.Character;

/**
 * @ClassName Model
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/27 4:12 下午
 * @Version 1.0
 **/
public abstract class AbstractEngine {
    public static final String TAG = AbstractEngine.class.getName();
    public final Group<Character> root;
    public Player player;
    public final Map map;
    public final TiledMapTileLayer collisionLayer;
    public final Array<Character> container = new Array<>();
    public final Array<Observer> listeners = new Array<>();
    public final EventQueue queue = new EventQueue();

    public float timeScale = 1;

    public Group<Enemy> enemyGroup;
    public Group<Character> playerGroup;
    public float gameOverTimer;
    public final Assets assets;
    public final Physic2DEngine physic2DEngine;
    public boolean isGameOver;
    public boolean isPlayerWin;


    public AbstractEngine() {
        this.assets = new Assets();
        this.map = new Map(assets.tiledMap);
        this.collisionLayer = map.collisionLayer;
        this.root = new Group<>();
        this.root.setModel(this);
        this.queue.setObservers(listeners);
        this.physic2DEngine = Physic2DEngine
                .newBuilder()
                .setAssets(assets)
                .setMap(map)
                .setCollisionLayer(collisionLayer)
                .setRoot(container)
                .setGameEngine(this)
                .build();
        enemyGroup = new Group<>("enemyGroup");
        playerGroup = new Group<>("playerGroup");
        this.root.addCharacter(playerGroup);
        this.root.addCharacter(enemyGroup);
        addListener(new ObserverAdapter(){
            @Override
            public boolean onOneObserverEvent(Event.OneCharacterEvent event) {
                switch (event.getEventType()) {
                    case frameEnd:
                    case aimPoint:
                        break;
                    default:
                        Gdx.app.log("监听者" + this + "| 事件" + event.getEventType().toString(), event.getCharacter().toString());
                        break;
                }
                return true;
            }

            @Override
            public boolean onTwoObserverEvent(Event.TwoObserverEvent event) {
                switch (event.getEventType()) {
                    default:
                        Gdx.app.log("监听者" + this + "| 事件" + event.getEventType().toString(), event.getA() + "->" + event.getB());
                        break;
                }
                return true;
            }
        });
    }

    public  void restart(){
        isGameOver = false;
        playerGroup.clear();
        enemyGroup.clear();
        gameOverTimer = 0;
        initPlayer();
    };

    public void initPlayer() {
        if (player != null) {
            player.updateByCharacter(new Player());
        } else {
            player = new Player();
        }
        playerGroup.addCharacter(player);
        //给用户添加一个监听
        //TODO 诞生了一个玩家
        player.position.set(4, 8);
        player.setId(2);
    }

    public Player getPlayer() {
        return player;
    };

    public Map getMap(){
        return map;
    };

    public void addListener(Observer listener){
        listeners.add(listener);
    };

    public void removeListener(Observer listener){
        this.listeners.removeValue(listener,false);
    };

    public Array<Enemy> getEnemies(){
        return enemyGroup.getChildren();
    };

    public Assets getAssets(){
        return assets;
    };

    public void update(float delta){
        root.update(delta);
        getAll();
        physic2DEngine.update(delta);

    }

    public void frameEnd(float delta){
        updateByEvent(Event.frameEnd(TAG, playerGroup, delta));
        //将事件队列处理掉
        queue.drain();
    }

    public float getTimeScale(){
        if (player.hp == 0)
            return timeScale * Interpolation.pow2In.apply(0, 1, MathUtils.clamp(gameOverTimer / Constants.gameOverSlowdown, 0.01f, 1));
        return timeScale;
    };

    public void setTimeScale(float timeScale){
        this.timeScale = timeScale;
    };



    public boolean isPlayerWin(){
        return isPlayerWin;
    };

    public boolean isGameOver(){
        return isGameOver;
    };

    public Array<Character> getAll(){
        container.clear();
        root.flat(container);
        return container;
    };

    public Array<Observer> getListeners(){
        return listeners;
    };

    public EventQueue getQueue(){
        return queue;
    };

    /**
     * 用户发起的请求
     **/
    public void updateByEvent(Event.OneCharacterEvent event){
        //确定event更新成功后向外部 把消息推入队列
        if (event.getCharacter().onOneObserverEvent(event)) {
            queue.pushCharacterEvent(event);
        }
    };

    public  void updateBy2CharacterEvent(Event.TwoObserverEvent event){
        Character a = (event.getA());
        Character b = (event.getB());



        switch (event.getEventType()) {
            //如果碰撞到了则发布一条击退信息
            case collisionCharacter:
                if (a.collisionTimer < 0) {
                    queue.pushTwoCharacterEvent(event);
                    updateBy2CharacterEvent(Event.hit(TAG, a, b));
                }
            default:
                if (event.getA().onTwoObserverEvent(event)) {
                    queue.pushTwoCharacterEvent(event);
                }
        }
    }
    public void gameResult(boolean isPlayerWin) {
        if (isGameOver) return;
        this.isPlayerWin = isPlayerWin;
        if (isPlayerWin) {
            updateByEvent(Event.win(TAG, playerGroup));
            updateByEvent(Event.lose(TAG, enemyGroup));
            isGameOver = true;
        } else {
            updateByEvent(Event.lose(TAG, playerGroup));
            updateByEvent(Event.win(TAG, enemyGroup));
            isGameOver = true;
        }

    }

    public abstract void save();

    public abstract void quit();
}
