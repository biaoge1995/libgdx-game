package org.cbzmq.game.view;

import org.cbzmq.game.model.Character;

public interface ActorFactory {
    BaseSkeletonActor getOrCreateActor(Character character);
    void reset();
}
