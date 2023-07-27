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


import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Event;
import com.esotericsoftware.spine.EventData;
import org.cbzmq.game.*;
import org.cbzmq.game.model.Character;
import org.cbzmq.game.model.*;
import org.cbzmq.game.model.Enemy.Type;

/**
 * The core of the game logic. The model manages all game information but knows nothing about the view, ie it knows nothing about
 * how this information might be drawn to the screen. This model-view separation is a clean way to organize the code.
 * 游戏逻辑的核心。模型管理着所有的游戏信息，但对视图一无所知（不用管如何将这些信息绘制到屏幕上）。这种模型视图分离是组织代码的一种干净方法。
 */
public class LocalModel implements Model {


    SpineBoyGame controller;
    Player player;
    Map map;
    TiledMapTileLayer collisionLayer;
    Array<CharacterListener> listeners = new Array();
    private final EventQueue queue = new EventQueue();

    float timeScale = 1;
    Array<Trigger> triggers = new Array();
    Array<Bullet> bullets;
    Array<Enemy> enemies = new Array();
    float gameOverTimer;
    Assets assets;

    public LocalModel(SpineBoyGame controller) {
        this.assets = new Assets();
        this.controller = controller;
        this.map = new Map(assets.tiledMap);
        this.collisionLayer = map.collisionLayer;
        restart();
    }

    public void restart() {
        player = new Player(this.map);
        //TODO 诞生了一个玩家
        queue.born(player);
        player.position.set(4, 8);
        bullets = player.getBullets();
        bullets.clear();
        enemies.clear();
        gameOverTimer = 0;

        // Setup triggers to spawn enemies based on the x coordinate of the player.
        triggers.clear();
        addTrigger(17, 17 + 22, 8, Type.normal, 2);
        addTrigger(17, 17 + 22, 8, Type.strong, 1);
        addTrigger(31, 31 + 22, 8, Type.normal, 3);
        addTrigger(43, 43 + 22, 8, Type.strong, 3);
        addTrigger(64, 64 + 22, 8, Type.normal, 10);
        addTrigger(64, 64 + 29, 8, Type.strong, 1);
        addTrigger(76, 76 - 19, 8, Type.strong, 2);
        addTrigger(87, 87 - 19, 8, Type.normal, 2);
        addTrigger(97, 97 - 19, 8, Type.normal, 2);
        addTrigger(100, 100 + 34, 8, Type.strong, 2);
        addTrigger(103, 103 + 34, 8, Type.normal, 4);
        addTrigger(125, 60 - 19, 8, Type.normal, 10);
        addTrigger(125, 125 - 19, 8, Type.weak, 10);
        addTrigger(125, 125 - 45, 8, Type.becomesBig, 1);
        addTrigger(125, 125 + 22, 22, Type.normal, 5);
        addTrigger(125, 125 + 32, 22, Type.normal, 2);
        addTrigger(125, 220, 23, Type.strong, 3);
        addTrigger(158, 158 - 19, 8, Type.weak, 10);
        addTrigger(158, 158 - 23, 8, Type.strong, 1);
        addTrigger(158, 158 + 22, 23, Type.normal, 3);
        addTrigger(165, 165 + 22, 23, Type.strong, 4);
        addTrigger(176, 176 + 22, 23, Type.normal, 12);
        addTrigger(176, 176 + 22, 23, Type.weak, 10);
        addTrigger(176, 151, 8, Type.strong, 1);
        addTrigger(191, 191 - 19, 23, Type.normal, 5);
        addTrigger(191, 191 - 19, 23, Type.weak, 15);
        addTrigger(191, 191 - 27, 23, Type.strong, 2);
        addTrigger(191, 191 + 34, 23, Type.weak, 10);
        addTrigger(191, 191 + 34, 23, Type.weak, 8);
        addTrigger(191, 191 + 34, 23, Type.normal, 2);
        addTrigger(191, 191 + 42, 23, Type.strong, 2);
        addTrigger(213, 213 + 22, 23, Type.normal, 3);
        addTrigger(213, 213 + 22, 23, Type.strong, 3);
        addTrigger(213, 213 - 19, 23, Type.normal, 7);
        addTrigger(246, 247 - 30, 23, Type.strong, 7);
        addTrigger(246, 225, 23, Type.normal, 2);
        addTrigger(246, 220, 23, Type.becomesBig, 3);
        addTrigger(246, 220, 23, Type.becomesBig, 3);
        triggers.clear();
        addTrigger(17, 17 + 22, 8, Type.becomesBig, 2);
    }

    public void addTrigger(float triggerX, float spawnX, float spawnY, Type type, int count) {
        Trigger trigger = new Trigger();
        trigger.x = triggerX;
        triggers.add(trigger);
        int offset = spawnX > triggerX ? 2 : -2;
        for (int i = 0; i < count; i++) {
            Enemy enemy = new Enemy(this.map, type);

            enemy.position.set(spawnX, spawnY);
            trigger.enemies.add(enemy);

            spawnX += offset;
        }
    }

    public void update(float delta) {
        if (player.hp == 0) {
            gameOverTimer += delta / getTimeScale() * timeScale; // Isn't affected by player death time scaling.
            controller.eventGameOver(false);
        }
        updateEnemies(delta);
        updateBullets(delta);
        player.update(delta);
        updateTriggers();
        //将事件队列处理掉
        queue.drain();
    }

    public void updateTriggers() {
        for (int i = 0, n = triggers.size; i < n; i++) {
            Trigger trigger = triggers.get(i);
            if (player.position.x > trigger.x) {
                for (Enemy enemy : trigger.enemies) {
                    enemies.add(enemy);
                    //TODO 出生了一头怪物
                    queue.born(enemy);
                }
                triggers.removeIndex(i);
                break;
            }
        }
    }

    public void updateEnemies(float delta) {
        int alive = 0;
        for (int i = enemies.size - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);

            enemy.update(delta);

            if (enemy.deathTimer < 0) {
                enemies.removeIndex(i);
                //TODO 移除了一头
                queue.beRemove(enemy);
                continue;
            }
            //怪物胜利
            if (player.hp == 0 && enemy.hp > 0) {
                enemy.win();
                queue.event(enemy, new Event(0, new EventData("win")));
            }
            //怪物孩子出生
            if (enemy.childs != null) {
                queue.event(enemy, new Event(0, new EventData("children")));
                for (Character child : enemy.childs) {
                    Enemy c = (Enemy) child;
                    enemies.add(c);
                    queue.born(c);
                }
                enemy.childs.clear();
            }
            if (enemy.hp > 0) {
                alive++;
            }
            if (enemy.hp > 0 && player.hp > 0) {
                enemy.setTargetPosition(player.position);
                if (enemy.collisionTimer < 0 && enemy.rect.overlaps(player.rect)) {
                    if (enemy.rect.y + enemy.rect.height * 0.6f < player.rect.y) {
                        // Enemy head bounce.
                        //碰撞到怪物头部时弹起
                        float bounceX = Player.headBounceX
                                * (enemy.position.x + enemy.rect.width / 2 < player.position.x + player.rect.width / 2 ? 1 : -1);

                        enemy.collisionTimer = Enemy.collisionDelay;
                        enemy.velocity.x -= bounceX;
                        enemy.velocity.y -= 10f;
                        enemy.setGrounded(false);
                        enemy.hp -= player.damage;
                        if (enemy.hp <= 0){
                            enemy.state = CharacterState.death;
                            //怪物死亡
                            queue.death(enemy);
                        }
						else enemy.state = CharacterState.fall;

                        player.velocity.x = bounceX;
                        player.velocity.y = Player.headBounceY;
                        player.setGrounded(false);
                        player.setState(CharacterState.fall);



                        //TODO 把这快给分离开
                        queue.collisionCharacter(enemy,player);
                        queue.hit(enemy,player);
//                        enemy.view.beHit();

                    } else if (player.collisionTimer < 0) {
                        // Player gets hit.
                        //玩家收到撞击
                        player.dir = enemy.position.x + enemy.rect.width / 2 < player.position.x + player.rect.width / 2 ? -1 : 1;
                        //判断击退的量
                        float amount = Player.knockbackX * player.dir;
                        player.velocity.x = -amount;
                        player.velocity.y += Player.knockbackY;
                        player.setGrounded(false);
                        player.hp--;
                        if (player.hp > 0) {
                            player.setState(CharacterState.fall);
                            player.collisionTimer = Player.collisionDelay;

                            enemy.velocity.x = amount * 1.6f;
                            enemy.velocity.y += 5f;
                            enemy.setState(CharacterState.fall);
                            enemy.jumpDelayTimer = MathUtils.random(0, enemy.jumpDelay);
                        } else {
                            player.setState(CharacterState.death);
                            player.velocity.y *= 0.5f;
                        }
                        enemy.setGrounded(false);
                        enemy.collisionTimer = Enemy.collisionDelay;

                        //这块也改下
//                        player.view.beHit(enemy);
//                        player.beHit(enemy);
                        queue.hit(player,enemy);
                        queue.collisionCharacter(player,enemy);
                    }
                }
            }

        }
        // End the game when all enemies are dead and all triggers have occurred.
        if (alive == 0 && triggers.size == 0) {
            queue.event(player,new Event(0,new EventData("win")));
            controller.eventGameOver(true);
        }
    }

    public void updateBullets(float delta) {
        Array.ArrayIterator<Bullet> iterator = bullets.iterator();
        outer:
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();

            if (bullet.hp == 0) {
                continue;
            }

            for (Enemy enemy : enemies) {
                if (enemy.state == CharacterState.death) continue;
                if (enemy.bigTimer <= 0 && enemy.rect.contains(bullet.position)) {
                    // Bullet hit enemy.
                    //子弹击中怪物
                    bullet.hp = 0;
                    //TODO 这块要改掉
//                    enemy.view.beHit();
                    //怪物被击中事件
                    queue.hit(enemy,player);
                    enemy.collisionTimer = Enemy.collisionDelay;
                    enemy.hp -= bullet.damage;
                    if (enemy.hp <= 0) {
                        enemy.state = CharacterState.death;
                        enemy.velocity.y *= 0.5f;
                    } else
                        enemy.state = CharacterState.fall;
                    //击退
                    enemy.velocity.x = MathUtils.random(enemy.knockbackX / 2, enemy.knockbackX)
                            * (player.position.x < enemy.position.x + enemy.rect.width / 2 ? 1 : -1);
                    enemy.velocity.y += MathUtils.random(enemy.knockbackY / 2, enemy.knockbackY);
                    continue outer;
                }
            }
            bullet.update(delta);

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


    static class Trigger {
        float x;
        Array<Enemy> enemies = new Array();
    }

     static class ObjectsAndEventType {
        Array<Object> objects = new Array<>();
        EventType eventType;


    }

    class EventQueue {
        private final Array<ObjectsAndEventType> objects = new Array<>();
        boolean drainDisabled=false;

        public void born(Character character) {

            ObjectsAndEventType objectsAndEventType = new ObjectsAndEventType();
            objectsAndEventType.objects.add(character);
            objectsAndEventType.eventType = EventType.born;
            objects.add(objectsAndEventType);

        }

        public void hit(Character character,Character hitCharacter) {

            ObjectsAndEventType objectsAndEventType = new ObjectsAndEventType();
            objectsAndEventType.objects.add(character);
            objectsAndEventType.objects.add(hitCharacter);
            objectsAndEventType.eventType = EventType.hit;
            objects.add(objectsAndEventType);
        }

        public void death(Character character) {
            ObjectsAndEventType objectsAndEventType = new ObjectsAndEventType();
            objectsAndEventType.objects.add(character);
            objectsAndEventType.eventType = EventType.death;
            objects.add(objectsAndEventType);
        }

        public void beRemove(Character character) {
            ObjectsAndEventType objectsAndEventType = new ObjectsAndEventType();
            objectsAndEventType.objects.add(character);
            objectsAndEventType.eventType = EventType.beRemove;
            objects.add(objectsAndEventType);
        }

        public void collisionMap(Character character, Rectangle tile) {
            //TODO
            ObjectsAndEventType objectsAndEventType = new ObjectsAndEventType();
            objectsAndEventType.objects.add(character);
            objectsAndEventType.objects.add(tile);
            objectsAndEventType.eventType = EventType.collisionMap;
            objects.add(objectsAndEventType);
        }

        public void collisionCharacter(Character character, Character other) {
            //TODO
            ObjectsAndEventType objectsAndEventType = new ObjectsAndEventType();
            objectsAndEventType.objects.add(character);
            objectsAndEventType.objects.add(other);
            objectsAndEventType.eventType = EventType.collisionCharacter;
            objects.add(objectsAndEventType);
        }

        public void attack(Character character) {
            //TODO
            ObjectsAndEventType objectsAndEventType = new ObjectsAndEventType();
            objectsAndEventType.objects.add(character);
            objectsAndEventType.eventType = EventType.attack;
            objects.add(objectsAndEventType);
        }

        public void dispose(Character character) {
            //TODO
            ObjectsAndEventType objectsAndEventType = new ObjectsAndEventType();
            objectsAndEventType.objects.add(character);
            objectsAndEventType.eventType = EventType.dispose;
            objects.add(objectsAndEventType);
        }

        public void event(Character character, Event event) {
            ObjectsAndEventType objectsAndEventType = new ObjectsAndEventType();
            objectsAndEventType.objects.add(character);
            objectsAndEventType.objects.add(event);
            objectsAndEventType.eventType = EventType.event;
            objects.add(objectsAndEventType);
        }

        public void drain() {
            if (drainDisabled) return; // Not reentrant.
            drainDisabled = true;

            Array<ObjectsAndEventType> objects = this.objects;
            Array<CharacterListener> listeners = LocalModel.this.listeners;
            for (ObjectsAndEventType objectAndEvent : objects) {
                EventType type = objectAndEvent.eventType;
                Character character = (Character) objectAndEvent.objects.get(0);
                switch (type) {
                    case born:
                        if (character.listener != null) character.listener.born(character);
                        for (int ii = 0; ii < listeners.size; ii++)
                            listeners.get(ii).born(character);
                        break;
                    case death:
                        if (character.listener != null) character.listener.death(character);
                        for (int ii = 0; ii < listeners.size; ii++)
                            listeners.get(ii).death(character);
                        break;
                    case hit:
                        Character hitCharacter = (Character) objectAndEvent.objects.get(1);
                        if (character.listener != null) character.listener.hit(character,hitCharacter);
                        for (int ii = 0; ii < listeners.size; ii++)
                            listeners.get(ii).hit(character,hitCharacter);
                        // Fall through.
                    case dispose:
                        if (character.listener != null) character.listener.dispose(character);
                        for (int ii = 0; ii < listeners.size; ii++)
                            listeners.get(ii).dispose(character);
//						trackEntryPool.free(entry);
                        break;
                    case beRemove:
                        if (character.listener != null) character.listener.beRemove(character);
                        for (int ii = 0; ii < listeners.size; ii++)
                            listeners.get(ii).beRemove(character);
                        break;
                    case attack:
                        if (character.listener != null) character.listener.attack(character);
                        for (int ii = 0; ii < listeners.size; ii++)
                            listeners.get(ii).attack(character);
                        break;
                    case collisionCharacter:
                        Character other = (Character) objectAndEvent.objects.get(1);
                        if (character.listener != null) character.listener.collisionCharacter(character, other);
                        for (int ii = 0; ii < listeners.size; ii++)
                            listeners.get(ii).collisionCharacter(character, other);
                        break;
                    case collisionMap:
                        Rectangle tile = (Rectangle) objectAndEvent.objects.get(1);
                        if (character.listener != null) character.listener.collisionMap(character, tile);
                        for (int ii = 0; ii < listeners.size; ii++)
                            listeners.get(ii).collisionMap(character, tile);
                        break;
                    case event:
                        Event event = (Event) objectAndEvent.objects.get(1);
                        if (character.listener != null) character.listener.event(character, event);
                        for (int ii = 0; ii < listeners.size; ii++)
                            listeners.get(ii).event(character, event);
                        break;
                }
            }
            clear();

            drainDisabled = false;
        }

        public void clear() {
            objects.clear();
        }
    }

    enum EventType {
        born, hit, death, dispose, beRemove, attack, collisionCharacter, collisionMap, event
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Map getMap() {
        return map;
    }

    public Array<CharacterListener> getListeners() {
        return listeners;
    }

    @Override
    public Array<Bullet> getBullets() {
        return bullets;
    }

    @Override
    public Array<Enemy> getEnemies() {
        return enemies;
    }

    @Override
    public Assets getAssets() {
        return assets;
    }

    public void addListener(CharacterListener listener) {
        this.listeners.add(listener);
    }
}
