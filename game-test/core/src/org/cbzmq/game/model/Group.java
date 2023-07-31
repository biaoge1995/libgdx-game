package org.cbzmq.game.model;


import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import org.cbzmq.game.stage.Model;

/**
 * @ClassName Group
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/30 6:13 下午
 * @Version 1.0
 **/
public class Group<T extends Character> extends Character {
    final Array<T> children = new Array<>();

    public Group(String name) {
        this.name = name;
    }

    public Group() {
        this.name = "root";
    }

    @Override
    public void update(float delta) {
        Array.ArrayIterator<T> iterator = children.iterator();
        while (iterator.hasNext()) {
            Character c = iterator.next();
            c.update(delta);
            if(c.isCanBeRemove()){
                c.remove();
            }
        }
    }

    public void addCharacter(T character) {
        if (character.parent != null) {
            if (character.parent == this) return;
            character.parent.removeCharacter(character, false);
        }
        children.add(character);
        character.setParent(this);
        character.setModel(getModel());
        character.setId(getModel().generalId());
        getQueue().born(character);
        character.setQueue(getQueue());

    }

    public boolean removeCharacter(T actor, boolean unfocus) {
        int index = children.indexOf(actor, true);
        if (index == -1) return false;
        removeActorAt(index, unfocus);
        //TODO 移除了一头
        getQueue().beRemove(actor);
        return true;
    }


    public Character removeActorAt(int index, boolean unfocus) {
        Character character = children.removeIndex(index);
        Model model = getModel();
        if (model != null) {
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
    private void split(Group group, Array<T> container) {
        if (group.children != null) {
            container.addAll(children);
            for (T child : children) {
                if (child instanceof Group) {
                    split((Group) child, container);
                }
            }
        }
    }

    public void clear(){
        this.children.clear();
    }

    public Array<T> getChildren() {
        return children;
    }
}
