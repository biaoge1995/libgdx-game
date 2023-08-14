package org.cbzmq.game.model;


import com.badlogic.gdx.utils.Array;
import org.cbzmq.game.enums.CharacterState;
import org.cbzmq.game.stage.AbstractEngine;

/**
 * @ClassName Group
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/30 6:13 下午
 * @Version 1.0
 **/
public class Group<T extends Character> extends Character {
    private final Array<T> children = new Array<>();
    public Group(String name) {
        this.name = name;
    }
    private final static String TAG = Group.class.getName();
    //将container打平
//    private final Array<T> flatContainer = new Array<>();

    public Group() {
        this.name = "root";
    }

    @Override
    public boolean onOneObserverEvent(Event.OneCharacterEvent event) {
        if (!(event.getCharacter() == this)) return false;
        switch (event.getEventType()) {
            case win:
                for (T child : children) {
                    child.setWin(true);
                }
        }
        return true;
    }

    @Override
    public void update(float delta) {
        Array.ArrayIterator<T> iterator = children.iterator();
        while (iterator.hasNext()) {
            Character c = iterator.next();
            if (!(c instanceof Group) && c.hp <= 0) {
                if (this.state != CharacterState.death) {
                    this.state = CharacterState.death;
                    if (getModel().getQueue() != null)
                        getModel().getQueue().pushCharacterEvent(Event.beDeath(TAG,c));
                }
            }
            c.update(delta);
            if (!(c instanceof Group) && c.isCanBeRemove()) {
                c.remove();
                abstractEngine.removeListener(c);
            }
        }
    }

    public void addCharacter(T character) {
        if (character.parent != null) {
            if (character.parent == this) return;
            character.parent.removeCharacter(character, false);
        }
        children.add(character);
//        abstractEngine.addListener(character);
        character.setParent(this);
        character.setModel(getModel());
        character.setId(No.getNo());

//        character.setQueue(getQueue());
        //将消息推送出去
        getModel().getQueue().pushCharacterEvent(Event.born(TAG,character));


    }

    public boolean removeCharacter(T actor, boolean unfocus) {
        int index = children.indexOf(actor, true);
        if (index == -1) return false;
        removeActorAt(index, unfocus);
        //TODO 移除了一头
        getModel().getQueue().pushCharacterEvent(Event.beRemove(TAG,actor));
        return true;
    }


    public Character removeActorAt(int index, boolean unfocus) {
        Character character = children.removeIndex(index);
        AbstractEngine abstractEngine = getModel();
        if (abstractEngine != null) {
            //TODO model的逻辑
        }
        character.setParent(null);
        character.setModel(null);

        return character;
    }

    /**
     * 将group 的child打平
     *
     * @param group
     * @param container
     */
    private void flat(Group<T> group, Array<T> container) {
        if (group.children != null) {

            for (T child : group.children) {
                if (child instanceof Group) {
                    flat((Group) child, container);
                } else {
                    container.addAll(child);
                }
            }
        }
    }

    public void flat(Array<T> container) {
        flat(this, container);
    }


    public void clear() {
        for (T child : this.children) {
            child.setParent(null);
            child.setModel(null);
//            child.setQueue(null);
        }

        this.children.clear();
    }

    public Array<T> getChildren() {
        return children;
    }

    static class No {
        private static int no;

        synchronized public static int getNo() {
            return no++;
        }
    }
}
