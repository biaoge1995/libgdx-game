package org.cbzmq.game.ui;

import org.cbzmq.game.model.Character;

public interface ActorFactory {
    BaseSkeletonActor getOrCreateActor(Character character);
    void reset();
}
