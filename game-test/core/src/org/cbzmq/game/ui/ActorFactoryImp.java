package org.cbzmq.game.ui;

import org.cbzmq.game.model.Bullet;
import org.cbzmq.game.model.Character;
import org.cbzmq.game.model.Enemy;
import org.cbzmq.game.model.Player;

import java.util.HashMap;
import java.util.Map;

public class ActorFactoryImp implements ActorFactory {

    final Map<Character, BaseSkeletonActor> modelAndViewMap;

    public ActorFactoryImp() {
        this.modelAndViewMap = new HashMap<>();
    }

    public BaseSkeletonActor getOrCreateActor(Character character) {
        if (modelAndViewMap.containsKey(character)) {
            return modelAndViewMap.get(character);
        } else {
            BaseSkeletonActor actor=null;
            switch (character.characterType) {
                case player:
                    actor = new PlayerActor((Player) character);
                    break;
                case bullet:
                    actor = new BulletActor((Bullet) character);
                    break;
                case enemy:
                    actor = new EnemyActor((Enemy) character);
                    break;
            }
            if(actor!=null){
                modelAndViewMap.put(character,actor);
                return actor;
            }
        }
        return null;
    }

    public Map<Character, BaseSkeletonActor> getModelAndViewMap() {
        return modelAndViewMap;
    }
    @Override
    public void reset(){
        modelAndViewMap.clear();
    }
}


