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

package org.cbzmq.game.logic;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import org.cbzmq.game.model.*;
import org.cbzmq.game.model.Character;
import org.cbzmq.game.proto.Move2;

/**
 * The core of the game logic. The model manages all game information but knows nothing about the view, ie it knows nothing about
 * how this information might be drawn to the screen. This model-view separation is a clean way to organize the code.
 * 游戏逻辑的核心。模型管理着所有的游戏信息，但对视图一无所知（不用管如何将这些信息绘制到屏幕上）。这种模型视图分离是组织代码的一种干净方法。
 */
public class Physic2DEngine {
    //物理参数
    //重力参数
    private static final String TAG = Physic2DEngine.class.getName();
    public static final float minVelocityX = 0.001f, maxVelocityY = 20f;
    //x方向 地板阻尼 空气阻尼 碰撞阻尼
    public static final float dampingGroundX = 36, dampingAirX = 15, collideDampingX = 0.7f;
    //重力参数
    private static final float gravity = 32;

//    private final Group<Character> root;

    private final Map map;

    private Array<? extends Character> container;
    //是否开启同组内不检测碰撞
    private boolean isGroupNoCollision = true;
    private final AbstractLogicEngine abstractLogicEngine;
    private MyVector2 tmp = new MyVector2();

    private boolean isClient = false;

    long start;

    static class Builder {

//        private Group<Character> root;

        private Array<? extends Character> container = new Array<>();

        private Map map;

        private boolean isClient = false;
        //是否开启同组内不检测碰撞
        private boolean isGroupNoCollision = true;
        private AbstractLogicEngine abstractLogicEngine;

        Physic2DEngine build() {
            return new Physic2DEngine(this);
        }


        public Builder setClient(boolean client) {
            isClient = client;
            return this;
        }

        public Builder setRoot(Array<? extends Character> root) {
            this.container = root;
            return this;
        }


        public Builder setMap(Map map) {
            this.map = map;
            return this;
        }


        public Builder setGameEngine(AbstractLogicEngine abstractLogicEngine) {
            this.abstractLogicEngine = abstractLogicEngine;

            return this;
        }

        public Builder setGroupNoCollision(boolean groupNoCollision) {
            isGroupNoCollision = groupNoCollision;
            return this;
        }
    }


    public static Builder newBuilder() {
        return new Builder();
    }

    private Physic2DEngine(Builder builder) {
        this.container = builder.container;

        this.map = builder.map;
        this.isClient = builder.isClient;

        this.isGroupNoCollision = builder.isGroupNoCollision;
        this.abstractLogicEngine = builder.abstractLogicEngine;
//        this.queue.setObservers(this.container);
    }


    public void update(float delta) {

        //在2d世界运动
        move(delta);
        //检测碰撞
        collision();

//        queue.drain();
    }


    /**
     * 角色运动
     *
     * @param delta
     */
    private void move(float delta) {
        for (int i = 0; i < container.size; i++) {
            move(container.get(i), delta);
        }

    }

    private void move(Character character, float delta) {

        // Apply gravity.
        //设置重力加速度
        character.velocity.y -= gravity * delta;
        //如果速度超过了最大值则给他赋予默认的最大速度
        if (character.velocity.y < 0 && -character.velocity.y > maxVelocityY)
            character.velocity.y = Math.signum(character.velocity.y) * maxVelocityY;

        boolean grounded = character.isGrounded();

        // Damping reduces velocity so the body2D eventually comes to a complete stop.
        //空中的阻尼和地板的阻尼
        float damping = (grounded ? dampingGroundX : dampingAirX) * delta;

        if (character.velocity.x > 0)
            character.velocity.x = Math.max(0, character.velocity.x - damping);
        else
            character.velocity.x = Math.min(0, character.velocity.x + damping);
        if (Math.abs(character.velocity.x) < minVelocityX && grounded) {
            character.velocity.x = 0;

        }
        //s=v * delta 该段时间内位移的距离
//        character.velocity.scl(delta); // Change velocity from units/sec to units since last frame.
        tmp.set(character.velocity).scl(delta);
        boolean isCollideMapX = collideMapX(character, tmp);
        boolean isCollideMapY = collideMapY(character, tmp);
        if (isCollideMapX) {
            character.collideMapX();
        }
        if (isCollideMapY) {
            character.collideMapY();
        }


//发生移动
        if (tmp.x != 0 || tmp.y != 0) {

            Move2 move2 = new Move2();
            move2.setId(character.getId());
            move2.setTime(delta);
            move2.setSourcePosition(character.getPosition());
            move2.setVelocity(character.velocity);

            if (!isClient) character.addPosition("单机运行", tmp);
//            else {
//                MoveTask moveTask = character.getMoveTask();
//                if (moveTask.isStart()) {
//
//                    MyVector2 compensateV = moveTask.getCompensateV();
//                    MyVector2 tmp2 = new MyVector2();
//                    tmp2.set(compensateV).scl(delta);
//                    tmp.add(tmp2);
//                    character.addPosition("客户端补偿", tmp);
//                    if (false) {
//                        System.out.println(moveTask.getTimestamp() + "开始补偿作业 " + "速度补偿" + compensateV + " 目标位置" + moveTask.getTargetPosition() + ",当前位置:" + character.position + " 剩余" + moveTask.getResidueTime() + "-" + delta);
//                    } else {
//                        moveTask.setStart(false);
//                    }
//                    start = System.currentTimeMillis();
//                }
//            }
            move2.setTargetPosition(character.getPosition());
            move2.setRequestTime(System.currentTimeMillis());
//            abstractLogicEngine.broadCastMove(move2);
        }

    }

    /**
     * 碰撞地图x轴检测
     *
     * @param character
     * @return
     */
    private boolean collideMapX(Character character, MyVector2 delta) {
        character.rect.x = character.getPosition().x + delta.x;
        character.rect.y = character.getPosition().y + character.collisionOffsetY;

        int x;
        if (character.velocity.x >= 0)
            x = (int) (character.rect.x + character.rect.width);
        else
            x = (int) character.rect.x;
        int startY = (int) character.rect.y;
        int endY = (int) (character.rect.y + character.rect.height);
        for (Rectangle tile : map.getCollisionTiles(x, startY, x, endY)) {
            if (!character.rect.overlaps(tile)) continue;
            if (abstractLogicEngine != null) {
                abstractLogicEngine.updateByEvent(Event.collisionMap(TAG, character, tile));
            }
            if (character.velocity.x >= 0)
                character.getPosition().x = tile.x - character.rect.width;
            else
                character.getPosition().x = tile.x + tile.width;
            character.velocity.x *= collideDampingX;
            tmp.x *= collideDampingX;

            return true;
        }
        return false;
    }

    /**
     * 碰撞地图Y轴检测
     *
     * @param character
     * @return
     */
    private boolean collideMapY(Character character, MyVector2 delta) {
        character.rect.x = character.getPosition().x;
        character.rect.y = character.getPosition().y + delta.y + character.collisionOffsetY;

//        if(character.rect.y<0){
//            character.rect.y=0;
//            character.position.y=0;
//        }

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

            if (character.velocity.y > 0)
                character.getPosition().y = tile.y - character.rect.height;
            else {
                character.getPosition().y = tile.y + tile.height;
                character.setGrounded(true);
            }
            character.velocity.y = 0;
            tmp.y = 0;
            return true;
        }
        return false;
    }


    /**
     * 与其他运动物体的碰撞检测
     */
    public void collision() {
//        root.flat(container);
        //进行角色之间的碰撞检测
        for (int i = 0; i < container.size; i++) {
            Character a = container.get(i);
            if (a instanceof Group) continue;
            for (int j = 0; j < container.size; j++) {
                Character b = container.get(j);
                if ((b instanceof Group) ||
                        (a == b) ||
                        //1、开启了同组不碰撞设置 2、任意一个角色为不可碰撞状态则不检测
                        (isGroupNoCollision && (a.getParent() != null && b.getParent() != null && a.getParent() == b.getParent())) ||
                        !(a.isNeedCheckCollision() && b.isNeedCheckCollision())) continue;
                else {
                    // 撞击
                    if (a.rect.overlaps(b.rect)) {
                        collisionCharacter(a, b);
                        collisionCharacter(b, a);
                    }
                }
            }
        }
    }

    public void collisionCharacter(Character a, Character b) {
        if (a instanceof Bullet) {
            Gdx.app.log("tag", "bullte");
        }
        abstractLogicEngine.updateBy2CharacterEvent(Event.collisionCharacter(TAG, a, b));

        //TODO 扣血 这块可以放到游戏主逻辑中做 不参与物理引擎运行
        float dirX = a.getPosition().x + a.rect.width / 2 < b.getPosition().x + b.rect.width / 2 ? -1 : 1;


        //判断x轴击退的量
        float amount = Player.knockbackX * dirX;
        a.velocity.x = amount;
        a.setGrounded(false);
        //判断y轴击退量
        float dirY = a.rect.y > b.rect.y + b.rect.height * 0.8f ? 1 : -1;
        if (dirY == 1) {
            a.velocity.y = Player.headBounceY * dirY;
        }
    }

    public Map getMap() {
        return map;
    }


    public boolean isGroupNoCollision() {
        return isGroupNoCollision;
    }


    public float getGravity() {
        return gravity;
    }
}
