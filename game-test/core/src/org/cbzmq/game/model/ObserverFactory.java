package org.cbzmq.game.model;

import org.cbzmq.game.enums.CharacterType;
import org.cbzmq.game.enums.EnemyType;

/**
 * @ClassName ObserverFactory
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/8/7 3:52 下午
 * @Version 1.0
 **/
public class ObserverFactory {
    public static Player createPlayer(){
        Player player = new Player();
        player.setCharacterType(CharacterType.player);
        return player;
    }

    public static Bullet createBullet(Player player,float startX, float startY, float vx, float vy){
        Bullet bullet = new Bullet(player,startX,startY,vx,vy);
        bullet.setCharacterType(CharacterType.bullet);
        return bullet;
    }

    public static Enemy createEnemy(EnemyType enemyType){
        Enemy enemy = new Enemy(enemyType);
        enemy.setCharacterType(CharacterType.enemy);
        return enemy;
    }


}
