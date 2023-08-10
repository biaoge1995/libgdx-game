/******************************************************************************
 * Spine Runtimes Software License
 * Version 2.1
 *
 * Copyright (c) 2013, Esoteric Software
 * All rights reserved.
 *
 * You are granted a perpetual, non-exclusive, non-sublicensable and
 * non-transferable license to install, execute and perform the Spine Runtimes
 * Software (the "Software") solely for internal use. Without the written
 * permission of Esoteric Software (typically granted by licensing Spine), you
 * may not (a) modify, translate, adapt or otherwise create derivative works,
 * improvements of the Software or develop new applications using the Software
 * or (b) remove, delete, alter or obscure any trademarks or any copyright,
 * trademark, patent or other intellectual property or proprietary rights notices
 * on or in the Software, including any copy thereof. Redistributions in binary
 * or source form must include this license and terms.
 *
 * THIS SOFTWARE IS PROVIDED BY ESOTERIC SOFTWARE "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL ESOTERIC SOFTARE BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *****************************************************************************/

package org.cbzmq.game.stage;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import org.cbzmq.game.Assets;
import org.cbzmq.game.Constants;
import org.cbzmq.game.Map;
import org.cbzmq.game.enums.CharacterState;
import org.cbzmq.game.enums.EnemyType;
import org.cbzmq.game.model.Character;
import org.cbzmq.game.model.*;

/**
 * The core of the game logic. The model manages all game information but knows nothing about the view, ie it knows nothing about
 * how this information might be drawn to the screen. This model-view separation is a clean way to organize the code.
 * 游戏逻辑的核心。模型管理着所有的游戏信息，但对视图一无所知（不用管如何将这些信息绘制到屏幕上）。这种模型视图分离是组织代码的一种干净方法。
 */
public class GameEngine implements Model {

    private static final String TAG = GameEngine.class.getName();
    Group<Character> root;
    Player player;
    Map map;
    TiledMapTileLayer collisionLayer;
    final Array<Character> container = new Array<>();
    final Array<Observer> listener = new Array<>();
    private final EventQueue queue = new EventQueue();

    float timeScale = 1;
    Array<Trigger> triggers = new Array<>();

    Group<Enemy> enemyGroup;
    Group<Player> playerGroup;
    float gameOverTimer;
    Assets assets;
    Physic2DEngine physic2DEngine;

    boolean isGameOver;

    boolean isPlayerWin;


    public GameEngine() {
        this.assets = new Assets();
        this.map = new Map(assets.tiledMap);
        this.collisionLayer = map.collisionLayer;
        this.root = new Group<>();
        this.root.setModel(this);
//        this.root.setQueue(queue);
        this.queue.setObservers(listener);
        this.physic2DEngine = Physic2DEngine
                .newBuilder()
                .setAssets(assets)
                .setMap(map)
                .setCollisionLayer(collisionLayer)
                .setQueue(queue)
                .setRoot(container)
                .build();
        addListener(new ObserverAdapter() {
            @Override
            public boolean onOneObserverEvent(Event.OneCharacterEvent event) {
                switch (event.getEventType()) {
                    case aimPoint:
                    case frameEnd:
                        break;
                    default:
                        Gdx.app.log("监听者" + this + "| 事件" + event.getEventType().toString(), event.getCharacter().toString());
                        break;
                }
                return super.onOneObserverEvent(event);
            }

            @Override
            public boolean onTwoObserverEvent(Event.TwoObserverEvent event) {
                Character a = (event.getA());
                Character b = (event.getB());
                switch (event.getEventType()) {
                    case hit:
                        float hp = a.hp - b.damage;
                        onCharacterEvent(Event.bloodUpdate(TAG, a, hp));
                    case beKilled:
                    case collisionCharacter:
                        Gdx.app.log("监听者" + this + "| 事件" + event.getEventType().toString(), a+"->"+b);
                        break;
                }
                return true;
            }
        });

        //初始化
        init();
    }

    public void init() {

        enemyGroup = new Group<>("enemyGroup");
        playerGroup = new Group<>("playerGroup");

        this.root.addCharacter(playerGroup);
        this.root.addCharacter(enemyGroup);
        restart();
    }

    public void restart() {
        isGameOver = false;
        playerGroup.clear();
        enemyGroup.clear();
        initPlayer();

        gameOverTimer = 0;

        // Setup triggers to spawn enemies based on the x coordinate of the player.
        triggers.clear();
        addTrigger(17, 17 + 22, 8, EnemyType.normal, 2);
        addTrigger(17, 17 + 22, 8, EnemyType.strong, 1);
        addTrigger(31, 31 + 22, 8, EnemyType.normal, 3);
        addTrigger(43, 43 + 22, 8, EnemyType.strong, 3);
        addTrigger(64, 64 + 22, 8, EnemyType.normal, 10);
        addTrigger(64, 64 + 29, 8, EnemyType.strong, 1);
        addTrigger(76, 76 - 19, 8, EnemyType.strong, 2);
        addTrigger(87, 87 - 19, 8, EnemyType.normal, 2);
        addTrigger(97, 97 - 19, 8, EnemyType.normal, 2);
        addTrigger(100, 100 + 34, 8, EnemyType.strong, 2);
        addTrigger(103, 103 + 34, 8, EnemyType.normal, 4);
        addTrigger(125, 60 - 19, 8, EnemyType.normal, 10);
        addTrigger(125, 125 - 19, 8, EnemyType.weak, 10);
        addTrigger(125, 125 - 45, 8, EnemyType.becomesBig, 1);
        addTrigger(125, 125 + 22, 22, EnemyType.normal, 5);
        addTrigger(125, 125 + 32, 22, EnemyType.normal, 2);
        addTrigger(125, 220, 23, EnemyType.strong, 3);
        addTrigger(158, 158 - 19, 8, EnemyType.weak, 10);
        addTrigger(158, 158 - 23, 8, EnemyType.strong, 1);
        addTrigger(158, 158 + 22, 23, EnemyType.normal, 3);
        addTrigger(165, 165 + 22, 23, EnemyType.strong, 4);
        addTrigger(176, 176 + 22, 23, EnemyType.normal, 12);
        addTrigger(176, 176 + 22, 23, EnemyType.weak, 10);
        addTrigger(176, 151, 8, EnemyType.strong, 1);
        addTrigger(191, 191 - 19, 23, EnemyType.normal, 5);
        addTrigger(191, 191 - 19, 23, EnemyType.weak, 15);
        addTrigger(191, 191 - 27, 23, EnemyType.strong, 2);
        addTrigger(191, 191 + 34, 23, EnemyType.weak, 10);
        addTrigger(191, 191 + 34, 23, EnemyType.weak, 8);
        addTrigger(191, 191 + 34, 23, EnemyType.normal, 2);
        addTrigger(191, 191 + 42, 23, EnemyType.strong, 2);
        addTrigger(213, 213 + 22, 23, EnemyType.normal, 3);
        addTrigger(213, 213 + 22, 23, EnemyType.strong, 3);
        addTrigger(213, 213 - 19, 23, EnemyType.normal, 7);
        addTrigger(246, 247 - 30, 23, EnemyType.strong, 7);
        addTrigger(246, 225, 23, EnemyType.normal, 2);
        addTrigger(246, 220, 23, EnemyType.becomesBig, 3);
        addTrigger(246, 220, 23, EnemyType.becomesBig, 3);
        // Setup triggers to spawn enemies based on the x coordinate of the player.
        triggers.clear();
        addTrigger(10, 10, 8, EnemyType.becomesBig, 1);

    }

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

    }


    public void update(float delta) {
        root.update(delta);
        getAll();
        physic2DEngine.update(delta);

        for (Character p : playerGroup.getChildren()) {
            if (p instanceof Player) {
                if (p.hp > 0) break;
                else {
                    gameOverTimer += delta / getTimeScale() * timeScale; // Isn't affected by player death time scaling.
                    queue.pushCharacterEvent(Event.lose(TAG, playerGroup));
                    isGameOver = true;
                }
            }


        }
        updateEnemies(delta);
        updateTriggers();
        queue.pushCharacterEvent(Event.frameEnd(TAG, playerGroup, delta));
        //将事件队列处理掉
        queue.drain();
    }

    @Override
    public void onCharacterEvent(Event.OneCharacterEvent event) {
      if(  event.getCharacter().onOneObserverEvent(event)){
          queue.pushCharacterEvent(event);
      }


    }

    @Override
    public void save() {

    }

    @Override
    public void quit() {

    }

    public void updateTriggers() {
        for (int i = 0, n = triggers.size; i < n; i++) {
            Trigger trigger = triggers.get(i);
            if (player.position.x > trigger.x) {
                for (Enemy enemy : trigger.enemies) {
                    enemyGroup.addCharacter(enemy);
                }
                triggers.removeIndex(i);
                break;
            }
        }
    }

    public void gameResult(boolean isPlayerWin) {
        if (isGameOver) return;
        this.isPlayerWin = isPlayerWin;
        if (isPlayerWin) {
            queue.pushCharacterEvent(Event.win(TAG, playerGroup));
            queue.pushCharacterEvent(Event.lose(TAG, enemyGroup));
            isGameOver = true;
        } else {
            queue.pushCharacterEvent(Event.lose(TAG, playerGroup));
            queue.pushCharacterEvent(Event.win(TAG, enemyGroup));
            isGameOver = true;
        }

    }

    public void updateEnemies(float delta) {
        int alive = 0;
        for (int i = enemyGroup.getChildren().size - 1; i >= 0; i--) {
            Enemy enemy = enemyGroup.getChildren().get(i);
            //怪物胜利
            if (player.hp == 0 && enemy.hp > 0) {
                enemy.win();
                gameResult(false);
                break;
            }

            if (enemy.hp > 0 || (enemy.hp <= 0 || enemy.deathTimer > 0)) {
                alive++;
            }
            //设置攻击目标
            if (enemy.hp > 0 && player.hp > 0) {
                enemy.setTargetPosition(player.position);
            }

            //产生小怪物
            if (enemy.spawnSmallsTimer > 0) {
                enemy.spawnSmallsTimer -= delta;

                if (enemy.spawnSmallsTimer < 0) {
                    for (int j = 0; j < Enemy.smallCount; j++) {
                        Enemy small = new Enemy(EnemyType.small);
                        small.position.set(enemy.position.x, enemy.position.y + 2);
                        small.velocity.x = MathUtils.random(5, 15) * (MathUtils.randomBoolean() ? 1 : -1);
                        small.velocity.y = MathUtils.random(10, 25);
                        small.setGrounded(false);
                        enemyGroup.addCharacter(small);
                    }
                }
            }

            if (enemy.state != CharacterState.death && (enemy.hp <= 0 || enemy.position.y < -100 || enemy.collisions > 100)) {
                enemy.state = CharacterState.death;
                enemy.hp = 0;
            }


            // Simple enemy AI.
            boolean grounded = enemy.isGrounded();
            if (grounded) enemy.move = true;

            enemy.maxVelocityX = grounded ? enemy.maxVelocityGroundX : Enemy.maxVelocityAirX;

            if (enemy.state != CharacterState.death && enemy.collisionTimer < 0) {

                //跳向目标方向
                if (grounded && (enemy.forceJump || Math.abs(enemy.targetPosition.x - enemy.position.x) < enemy.jumpDistance)) {
                    enemy.jumpDelayTimer -= delta;
                    //跳跃的定时器
                    if (enemy.state != CharacterState.jumping && enemy.jumpDelayTimer < 0 && enemy.position.y <= enemy.targetPosition.y) {
//                        enemy.jump();
                        //TODO 关键帧 跳跃
                        onCharacterEvent(Event.jump(TAG, enemy));
                        enemy.jumpDelayTimer = MathUtils.random(0, enemy.jumpDelay);
                        enemy.forceJump = false;
                    }
                }
                //朝着目标的方向移动
                if (enemy.move) {
                    if (enemy.targetPosition.x > enemy.position.x) {
                        if (enemy.velocity.x >= 0)
                            //TODO 关键帧
                            onCharacterEvent(Event.moveRight(TAG, enemy, delta));
                    } else if (enemy.velocity.x <= 0)
                        //TODO 关键帧
                        onCharacterEvent(Event.moveLeft(TAG, enemy, delta));
                }

            }

            int previousCollision = enemy.collisions;
            if (!grounded || enemy.collisions == previousCollision) enemy.collisions = 0;

        }

        if (alive == 0 && triggers.size == 0) {
            if (!isGameOver) {
                gameResult(true);
            }
        }
    }


    public void addTrigger(float triggerX, float spawnX, float spawnY, EnemyType enemyType, int count) {
        Trigger trigger = new Trigger();
        trigger.x = triggerX;
        triggers.add(trigger);
        int offset = spawnX > triggerX ? 2 : -2;
        for (int i = 0; i < count; i++) {
            Enemy enemy = new Enemy(enemyType);

            enemy.position.set(spawnX, spawnY);
            trigger.enemies.add(enemy);

            spawnX += offset;
        }
    }


    public float getTimeScale() {
        if (player.hp == 0)
            return timeScale * Interpolation.pow2In.apply(0, 1, MathUtils.clamp(gameOverTimer / Constants.gameOverSlowdown, 0.01f, 1));
        return timeScale;
    }

    @Override
    public void setTimeScale(float timeScale) {
        this.timeScale = timeScale;
    }

    public Array<Character> getAll() {
        container.clear();
        root.flat(container);
        return container;
    }

    @Override
    public Array<Observer> getListeners() {
        return listener;
    }

    @Override
    public EventQueue getQueue() {
        return queue;
    }


    static class Trigger {
        float x;
        Array<Enemy> enemies = new Array();
    }


    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Map getMap() {
        return map;
    }


    public void addListener(Observer listener) {
        this.listener.add(listener);
    }

    public void removeListener(Observer listener) {
        this.listener.removeValue(listener,false);
    }

    @Override
    public Array<Enemy> getEnemies() {
        return enemyGroup.getChildren();
    }

    @Override
    public Assets getAssets() {
        return assets;
    }


    @Override
    public boolean isPlayerWin() {
        return isPlayerWin;
    }

    @Override
    public boolean isGameOver() {
        return isGameOver;
    }


}
