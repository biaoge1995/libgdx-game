package org.cbzmq.game.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.internal.SocketUtils;
import org.cbzmq.game.enums.CharacterState;
import org.cbzmq.game.enums.CharacterType;
import org.cbzmq.game.enums.MsgHeader;
import org.cbzmq.game.model.*;
import org.cbzmq.game.model.Character;
import org.cbzmq.game.proto.CharacterProto;
import org.cbzmq.game.proto.MsgProto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName Model
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/27 4:12 下午
 * @Version 1.0
 **/
public abstract class AbstractLogicEngine {
    public static final String TAG = AbstractLogicEngine.class.getName();
    public final Group<Character> root;
    public Player playerA;
    public Player playerB;
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
    private final Array<MsgProto.Event> protoEvents = new Array<>();

    private static long counter = 0;
    private static final Array<CharacterProto.Character> characterProtos = new Array<>();
    //用来广播的通信渠道
    private boolean isNetworkingMode;
    private Channel broadcastChl;
    //    private List<Address> clientAddressList;
    private static final int maxPlayerNum = 2;

//    public static class Address {
//        String host;
//        int port;
//
//        public Address(String host, int port) {
//            this.host = host;
//            this.port = port;
//        }
//    }

    public AbstractLogicEngine(boolean isClient) {
        this(null, isClient);
        this.isNetworkingMode = false;
    }

    public AbstractLogicEngine(Channel broadcastChl,boolean isClient) {
        this.isNetworkingMode = true;
//        this.clientAddressList = new ArrayList<>(maxPlayerNum);
        this.assets = new Assets();
        this.broadcastChl = broadcastChl;
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
                .setClient(isClient)
                .setGameEngine(this)
                .build();
        enemyGroup = new Group<>("enemyGroup");
        playerGroup = new Group<>("playerGroup");
        this.root.addCharacter(playerGroup);
        this.root.addCharacter(enemyGroup);
        addListener(new ObserverAdapter() {
            @Override
            public boolean onOneObserverEvent(Event.OneCharacterEvent event) {
                switch (event.getEventType()) {
                    case frameEnd:
                    case aimPoint:
                        break;
                    default:
//                        Gdx.app.log("监听者" + this + "| 事件" + event.getEventType().toString(), event.getCharacter().toString());
                        break;
                }
                return true;
            }

            @Override
            public boolean onTwoObserverEvent(Event.TwoObserverEvent event) {
                switch (event.getEventType()) {
                    default:
//                        Gdx.app.log("监听者" + this + "| 事件" + event.getEventType().toString(), event.getA() + "->" + event.getB());
                        break;
                }
                return true;
            }
        });
    }

    public void restart() {
        isGameOver = false;
//        playerGroup.clear();
        enemyGroup.clear();
        gameOverTimer = 0;
        //加入玩家A

        //加入玩家B
//        join(new Player(),new Address("127.0.0.1",8888));
    }

    ;


    public Player getPlayerA() {
        return playerA;
    }

    ;

    public Map getMap() {
        return map;
    }

    ;

    public void addListener(Observer listener) {
        listeners.add(listener);
    }

    ;

    public void removeListener(Observer listener) {
        this.listeners.removeValue(listener, false);
    }

    ;

    public Array<Enemy> getEnemies() {
        return enemyGroup.getChildren();
    }

    ;

    public Assets getAssets() {
        return assets;
    }

    ;

    public void update(float delta) {
        root.update(delta);
        getAll();
        physic2DEngine.update(delta);

    }

    public void frameEnd(float delta) {
        updateByEvent(Event.frameEnd(TAG, playerGroup, delta));
        //将事件队列处理掉
        queue.drain();
    }

    public float getTimeScale() {
        if (playerA != null && playerA.hp == 0)
            return timeScale * Interpolation.pow2In.apply(0, 1, MathUtils.clamp(gameOverTimer / Constants.gameOverSlowdown, 0.01f, 1));
        return timeScale;
    }

    ;

    public void setTimeScale(float timeScale) {
        this.timeScale = timeScale;
    }

    ;


    public boolean isPlayerWin() {
        return isPlayerWin;
    }

    ;

    public boolean isGameOver() {
        return isGameOver;
    }

    ;

    public Array<Character> getAll() {
        container.clear();
        root.flat(container);
        return container;
    }

    ;

    public Array<Observer> getListeners() {
        return listeners;
    }

    ;

    public EventQueue getQueue() {
        return queue;
    }

    ;

    /**
     * 用户发起的请求
     **/
    public void updateByEvent(Event.OneCharacterEvent event) {
        //确定event更新成功后向外部 把消息推入队列
        if (event.getCharacter().onOneObserverEvent(event)) {
            queue.pushCharacterEvent(event);
        }
    }

    ;

    public void updateBy2CharacterEvent(Event.TwoObserverEvent event) {
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


    public void onOneObserverEvent(Event.OneCharacterEvent event) {

        switch (event.getEventType()) {
            case aimPoint:
            case attack:
            case born:
            case jump:
            case beDeath:
            case beRemove:
            case win:
            case moveLeft:
            case moveRight:
            case bloodUpdate:
            case stateUpdate:
            case jumpDamping:
            case lose:
                if (event.getCharacter().getCharacterType() == CharacterType.unknown) return;
                protoEvents.add(event.toMsgProtoEvent());
                break;
            case frameEnd:
                Group root = (Group) (event.getCharacter());
                byte[] bytes = null;
                //没60个轮训给客户端同步一次数据
//                counter % 60 == 0
                if (false) {
                    syncAllCharacter(root);
                    MsgProto.Msg.Builder builder = MsgProto.Msg.newBuilder();
                    MsgProto.Msg msg = builder
                            .setId(counter)
                            .setHeader(MsgHeader.SYNC_CHARACTERS_INFO)
                            .addAllCharacterData(characterProtos)
                            .setTimeStamp(new Date().getTime())
                            .build();

                    bytes = msg.toByteArray();
                } else if (protoEvents.size > 0) {
                    MsgProto.Msg.Builder builder = MsgProto.Msg.newBuilder();
                    MsgProto.Msg msg = builder
                            .setId(counter)
                            .setHeader(MsgHeader.SYNC_CHARACTERS_EVENT)
                            .addAllEvents(protoEvents)
                            .setTimeStamp(new Date().getTime())
                            .build();

                    bytes = msg.toByteArray();
//                    Gdx.app.log("events", msg.toString());
                }

                if (bytes == null) return;
                //二次压缩
                Utils.CompressData compress = Utils.compress(bytes);
                ByteBuf byteBuf = Unpooled.copiedBuffer(compress.getOutput());

//              System.out.println("元素数量" + msg.getCharacterDataList().size());
//              System.out.println("protobuf消息长度" + bytes.length + "byte");

                try {

                    broadcastChl.writeAndFlush(new DatagramPacket(
                            byteBuf,
                            SocketUtils.socketAddress("127.0.0.1", 8088)
//                                    SocketUtils.socketAddress("192.168.2.145", 8088)
//                                    SocketUtils.socketAddress(address.host, address.port)
                    )).sync();
                    if (counter == Long.MAX_VALUE) {

                        counter = 0;
                    } else {
                        counter++;
                        protoEvents.clear();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
//
//                if (isNetworkingMode && clientAddressList.size() > 0) {
//                    for (Address address : clientAddressList) {
//
//                    }
//                }
        }


    }


    public void syncAllCharacter(Group root) {
        container.clear();
        characterProtos.clear();
        root.flat(container);

        for (Character character : container) {
            if (character.state == CharacterState.death) continue;
            CharacterProto.Character proto = character.toCharacterProto().build();
            characterProtos.add(proto);
        }
    }

    public void join(Player player) {
        if (playerA == null) {
            playerA = player;
            //给用户添加一个监听
            //TODO 诞生了一个玩家
            playerA.position.set(4, 8);
            playerA.setId(2);
            playerGroup.addCharacter(playerA);
//            clientAddressList.add(address);
        } else if (playerB == null) {
            playerB = player;
            //TODO 诞生了一个玩家
            playerB.position.set(6, 8);
            playerB.setId(3);
            playerGroup.addCharacter(playerB);
//            clientAddressList.add(address);
        }


    }

    ;

    public abstract void save();

    public abstract void quit();
}
