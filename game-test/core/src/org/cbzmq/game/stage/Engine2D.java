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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import org.cbzmq.game.Assets;
import org.cbzmq.game.Map;
import org.cbzmq.game.enums.CharacterState;
import org.cbzmq.game.model.Character;
import org.cbzmq.game.model.CharacterListener;
import org.cbzmq.game.model.Group;

/**
 * The core of the game logic. The model manages all game information but knows nothing about the view, ie it knows nothing about
 * how this information might be drawn to the screen. This model-view separation is a clean way to organize the code.
 * 游戏逻辑的核心。模型管理着所有的游戏信息，但对视图一无所知（不用管如何将这些信息绘制到屏幕上）。这种模型视图分离是组织代码的一种干净方法。
 */
public class Engine2D {

    private Group<Character> root;
    private int id;
    private Assets assets;
    private Map map;
    private TiledMapTileLayer collisionLayer;
    private Array<CharacterListener> listeners = new Array();
    private final EventQueue queue = new EventQueue(listeners);
    private float timeScale = 1;
    //是否开启同组内不检测碰撞
    private boolean isGroupNoCollision;
    Array<Character> container = new Array<>();

    //物理参数
    //重力参数
    private float gravity = 32;

    public Engine2D() {
        this(new Group<>(),new Assets());
    }

    public Engine2D(Assets assets) {
        this(new Group<>(),assets);
        this.map = new Map(this.assets.tiledMap);
    }

    public Engine2D(Group<Character> root,Assets assets) {
        this.root = root;
        this.assets = assets;
        this.map = new Map(assets.tiledMap);
        this.collisionLayer = map.collisionLayer;
        this.root.setQueue(queue);
    }

    public Engine2D(Group<Character> root,Map map) {
        this.root = root;
        this.map = map;
        this.collisionLayer = map.collisionLayer;
        this.root.setQueue(queue);
    }

    public void update(float delta) {
        container.clear();
        //角色本身更新动作
        root.update(delta);
        root.flat(container);
        //在2d世界运动
        move(delta);
        //检测碰撞
        collision();

        queue.drain();
    }

    public void addCharacter(Character character){
        root.addCharacter(character);
    }

    /**
     * 角色运动
     * @param delta
     */
    private void move( float delta){
        for (Character character : container) {
            move(character,delta);
        }
    }

    private void move(Character character, float delta) {

        character.stateTime += delta;

//            if(character.hp<=0){
//                beDeath();
//            }


        // If moving downward, change state to fall.
        //如果角色在往下移动则设置该角色的状态为fll
        if (character.velocity.y < 0 && character.state != CharacterState.jump && character.state != CharacterState.fall) {
            character.setState(CharacterState.fall);
            character.setGrounded(false);
        }

        // Apply gravity.
        //设置重力加速度
        character.velocity.y -= gravity * delta;
        //如果速度超过了最大值则给他赋予默认的最大速度
        if (character.velocity.y < 0 && -character.velocity.y > Character.maxVelocityY) character.velocity.y = Math.signum(character.velocity.y) * Character.maxVelocityY;

        boolean grounded = character.isGrounded();

        // Damping reduces velocity so the character eventually comes to a complete stop.
        //空中的阻尼和地板的阻尼
        float damping = (grounded ? Character.dampingGroundX : Character.dampingAirX) * delta;

        if (character.velocity.x > 0)
            character.velocity.x = Math.max(0, character.velocity.x - damping);
        else
            character.velocity.x = Math.min(0, character.velocity.x + damping);
        if (Math.abs(character.velocity.x) < Character.minVelocityX && grounded) {
            character.velocity.x = 0;
            character.setState(CharacterState.idle);
        }
        //s=v * delta 该段时间内位移的距离
        character.velocity.scl(delta); // Change velocity from units/sec to units since last frame.
        collideMapX(character);
        collideMapY(character);
        character.position.add(character.velocity);
        character.velocity.scl(1 / delta); // Change velocity back.

    }

    /**
     * 碰撞地图x轴检测
     * @param character
     * @return
     */
    private boolean collideMapX(Character character) {
        character.rect.x = character.position.x + character.velocity.x;
        character.rect.y = character.position.y + character.collisionOffsetY;

        int x;
        if (character.velocity.x >= 0)
            x = (int) (character.rect.x + character.rect.width);
        else
            x = (int) character.rect.x;
        int startY = (int) character.rect.y;
        int endY = (int) (character.rect.y + character.rect.height);
        for (Rectangle tile : map.getCollisionTiles(x, startY, x, endY)) {
            if (!character.rect.overlaps(tile)) continue;
            if (queue != null) {
                queue.collisionMap(character, tile);
            }
            if (character.velocity.x >= 0)
                character.position.x = tile.x - character.rect.width;
            else
                character.position.x = tile.x + tile.width;
            character.velocity.x *= Character.collideDampingX;
            return true;
        }
        return false;
    }

    /**
     * 碰撞地图Y轴检测
     * @param character
     * @return
     */
    private boolean collideMapY(Character character) {
        character.rect.x = character.position.x;
        character.rect.y = character.position.y + character.velocity.y + character.collisionOffsetY;

        int y;
        if (character.velocity.y > 0)
            y = (int) (character.rect.y + character.rect.height);
        else
            y = (int) character.rect.y;
        int startX = (int) character.rect.x;
        int endX = (int) (character.rect.x + character.rect.width);
        Array<Rectangle> collisionTiles = map.getCollisionTiles(startX, y, endX, y);
        for (Rectangle tile : collisionTiles) {
            if (!character.rect.overlaps(tile)) continue;
            if (queue != null) {
                queue.collisionMap(character, tile);
            }

            if (character.velocity.y > 0)
                character.position.y = tile.y - character.rect.height;
            else {
                character.position.y = tile.y + tile.height;
                if (character.state == CharacterState.jump) character.setState(CharacterState.idle);
                character.setGrounded(true);
            }
            character.velocity.y = 0;
            return true;
        }
        return false;
    }


    /**
     * 与其他运动物体的碰撞检测
     */
    public void collision() {
        root.flat(container);
        //进行角色之间的碰撞检测
        for (Character a : container) {
            for (Character b : container) {

                if ((a instanceof Group || b instanceof Group) ||
                        //1、开启了同组不碰撞设置 2、任意一个角色为不可碰撞状态则不检测
                        (a.state == CharacterState.death || b.state == CharacterState.death) ||
                        (isGroupNoCollision && a.getParent() == b.getParent()) || !(a.isCanBeCollision && a.isCanBeCollision))
                    continue;
                else {
                    if (a.rect.overlaps(b.rect)) {
                        queue.collisionCharacter(a, b);
                        queue.hit(a, b);
                        //b相当于玩家
                        if (a.rect.y + a.rect.height * 0.6f < b.rect.y) {
                            //a 碰到b弹起
                            //TODO
                            float bounceX = b.resilience * b.velocity.x
                                    * (a.position.x + a.rect.width / 2 < b.position.x + b.rect.width / 2 ? 1 : -1);


                            a.velocity.x -= bounceX;
                            a.velocity.y -= 10f;
                            a.setGrounded(false);
                            a.hp -= b.damage;
                            a.beCollide();
//                            a.collisionTimer = Enemy.collisionDelay;
//                            if (a.hp > 0){
//                                a.state = CharacterState.fall;
//                            }

                            b.velocity.x = bounceX;
                            b.velocity.y = b.resilience * b.velocity.y;
                            b.setGrounded(false);
                            b.setState(CharacterState.fall);

                        }
//                        else if (b.collisionTimer < 0) {
//                            // Player gets hit.
//                            //玩家收到撞击
//                            b.dir = b.position.x + b.rect.width / 2 < b.position.x + b.rect.width / 2 ? -1 : 1;
//                            //判断击退的量
//                            float amount = Player.knockbackX * b.dir;
//                            b.velocity.x = -amount;
//                            b.velocity.y += Player.knockbackY;
//                            b.setGrounded(false);
//                            b.hp--;
//                            if (b.hp > 0) {
//                                player.setState(CharacterState.fall);
//                                player.collisionTimer = Player.collisionDelay;
//
//                                a.velocity.x = amount * 1.6f;
//                                a.velocity.y += 5f;
//                                a.setState(CharacterState.fall);
//                                enemy.jumpDelayTimer = MathUtils.random(0, enemy.jumpDelay);
//                            } else {
//                                player.setState(CharacterState.death);
//                                player.velocity.y *= 0.5f;
//                            }
//                            enemy.setGrounded(false);
//                            enemy.collisionTimer = Enemy.collisionDelay;
//
//                            //这块也改下
////                        player.view.beHit(enemy);
////                        player.beHit(enemy);
//                            queue.hit(player,enemy);
//                            queue.collisionCharacter(player,enemy);
//                        }

                    }
                }
            }
        }
    }

    public Group<Character> getRoot() {
        return root;
    }

    public void setRoot(Group<Character> root) {
        this.root = root;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public TiledMapTileLayer getCollisionLayer() {
        return collisionLayer;
    }

    public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
        this.collisionLayer = collisionLayer;
    }

    public Array<CharacterListener> getListeners() {
        return listeners;
    }

    public void setListeners(Array<CharacterListener> listeners) {
        this.listeners = listeners;
    }

    public EventQueue getQueue() {
        return queue;
    }

    public boolean isGroupNoCollision() {
        return isGroupNoCollision;
    }

    public void setGroupNoCollision(boolean groupNoCollision) {
        isGroupNoCollision = groupNoCollision;
    }

    public Assets getAssets() {
        return assets;
    }

    public void setAssets(Assets assets) {
        this.assets = assets;
    }

    public Array<Character> getContainer() {
        return container;
    }

    public void setContainer(Array<Character> container) {
        this.container = container;
    }


    //    public void updateBullets() {
//
//        Array.ArrayIterator<Bullet> iterator = bulletGroup.getChildren().iterator();
//        outer:
//        while (iterator.hasNext()) {
//            Bullet bullet = iterator.next();
//
////            if (bullet.hp == 0) {
////                continue;
////            }
//
//            for (Enemy enemy : enemyGroup.getChildren()) {
//                if (enemy.state == CharacterState.death) continue;
//                if (enemy.bigTimer <= 0 && enemy.rect.contains(bullet.position)) {
//                    // Bullet hit enemy.
//                    //子弹击中怪物
//                    bullet.hp = 0;
//                    bullet.beDeath();
//                    //TODO 这块要改掉
////                    enemy.view.beHit();
//                    //怪物被击中事件
//                    queue.hit(enemy, player);
//                    enemy.collisionTimer = Enemy.collisionDelay;
//                    enemy.hp -= bullet.damage;
//                    if (enemy.hp <= 0) {
//                        enemy.state = CharacterState.death;
//                        enemy.velocity.y *= 0.5f;
//                    } else
//                        enemy.state = CharacterState.fall;
//                    //击退
//                    enemy.velocity.x = MathUtils.random(enemy.knockbackX / 2, enemy.knockbackX)
//                            * (player.position.x < enemy.position.x + enemy.rect.width / 2 ? 1 : -1);
//                    enemy.velocity.y += MathUtils.random(enemy.knockbackY / 2, enemy.knockbackY);
//                    continue outer;
//                }
//            }
////            bullet.update(delta);
//
//        }
//    }


    public int generalId() {
        return id++;
    }


//    public float getTimeScale() {
//        if (player.hp == 0)
//            return timeScale * Interpolation.pow2In.apply(0, 1, MathUtils.clamp(gameOverTimer / Constants.gameOverSlowdown, 0.01f, 1));
//        return timeScale;
//    }

    public void setTimeScale(float timeScale) {
        this.timeScale = timeScale;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }
}
