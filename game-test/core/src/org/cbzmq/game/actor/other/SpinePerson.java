package org.cbzmq.game.actor.other;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.*;
import com.esotericsoftware.spine.utils.SkeletonActor;

import java.util.Stack;

/**
 * @ClassName SpinePerson
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/7/20 10:13 下午
 * @Version 1.0
 **/

public class SpinePerson extends SkeletonActor {

    private boolean isIdle = false;

    private boolean isJumping = false;

    private final Stack<AnimationState.TrackEntry> jumpStack = new Stack<>();
    private final Stack<AnimationState.TrackEntry> shootStack = new Stack<>();
    private final Stack<AnimationState.TrackEntry> runStack = new Stack<>();
    private final Stack<AnimationState.TrackEntry> walkStack = new Stack<>();
    private final Stack<AnimationState.TrackEntry> idleStack = new Stack<>();

    private Array<String> debugInfos = new Array<>();


    private JumpEntry jumpEntry;

    public static class JumpEntry {
        AnimationState.TrackEntry entry;
        Status status;

        public JumpEntry(AnimationState.TrackEntry entry, Status status) {
            this.entry = entry;
            this.status = status;
        }

        public AnimationState.TrackEntry getEntry() {
            return entry;
        }

        public void setEntry(AnimationState.TrackEntry entry) {
            this.entry = entry;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }
    }

    public enum Action {
        AIM("aim"), DEATH("death"), HIT("hit"), IDLE("idle"), JUMP("jump"), RUN("run"), SHOOT("shoot"), WALK("walk");
        private String name;

        Action(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public enum Status {
        START, END, COMPLETE, INTERRUPT
    }

    public Stack<AnimationState.TrackEntry> getStack(Action action) {
        Stack<AnimationState.TrackEntry> stack = null;
        switch (action) {
            case JUMP:
                stack = jumpStack;
                break;
            case RUN:
                stack = runStack;
                break;
            case WALK:
                stack = walkStack;
                break;
            case SHOOT:
                stack = shootStack;
                break;
            case IDLE:
                stack = idleStack;
                break;

        }
        return stack;
    }

    public void addJump(AnimationState.TrackEntry entry) {
        Action action = Action.valueOf(entry.getAnimation().getName().toUpperCase());
        Stack<AnimationState.TrackEntry> stack = getStack(action);
        if (stack != null) {
            int search = stack.search(entry);
            if (search == -1) {
                stack.add(entry);
                debugInfos.add("增加entry");
//                if (!entry.getLoop()) {
//
//                }
            }
        }


    }

    public void popJump(AnimationState.TrackEntry entry) {
//        Action action = Action.valueOf(entry.getAnimation().getName().toUpperCase());
//        Stack<AnimationState.TrackEntry> stack = getStack(action);
//        if (stack != null) {
//            int search = stack.search(entry);
//
//            if (search != -1) {
//                stack.remove(entry);
//                debugInfos.add("pop entry");
//            }
//        }

    }

    public SpinePerson(AssetManager assetManager) {
        TextureAtlas atlas = assetManager.get(Res.Atlas.ATLAS_SPINE_CONTRA, TextureAtlas.class);
        SkeletonJson skeletonJson = new SkeletonJson(atlas);
        skeletonJson.setScale(1f);
        SkeletonData skeletonData = skeletonJson.readSkeletonData(Gdx.files.internal(Res.Atlas.ATLAS_SPINE_CONTRA_JSON));
        AnimationStateData animationStateData = new AnimationStateData(skeletonData);
//        float jump = skeletonData.findAnimation("run").getDuration();
//        animationStateData.setDefaultMix(0.2f);
//        animationStateData.setMix("jump", "run",0.2f );
//        animationStateData.setMix("jump", "jump",0.2f );
//        animationStateData.setMix("jump", "walk",0.2f );
//        animationStateData.setMix("jump", "idea",0.2f );


        AnimationState state = new AnimationState(animationStateData);
        Skeleton skeleton = new Skeleton(skeletonData);

        this.setSkeleton(skeleton);
        this.setAnimationState(state);
        SkeletonRenderer skeletonRenderer = new SkeletonRenderer();
        this.setRenderer(skeletonRenderer);
        getAnimationState().setAnimation(0, "attack", true);
//        getAnimationState().addAnimation(1, "shoot", true,0);
//        getAnimationState().addAnimation(3, "run", true,0);

        getAnimationState().addListener(new AnimationState.AnimationStateAdapter() {
            @Override
            public void start(AnimationState.TrackEntry entry) {
                addJump(entry);
                String debugInfo = " Thread" + Thread.currentThread().getId() + " 开始" + entry + " " + System.identityHashCode(entry);
                debugInfos.add(debugInfo);
            }

            @Override
            public void interrupt(AnimationState.TrackEntry entry) {
                String debugInfo = System.identityHashCode(entry) + "打断" + entry;
                popJump(entry);
                debugInfos.add(debugInfo);

            }

            @Override
            public void end(AnimationState.TrackEntry entry) {

                String debugInfo = System.identityHashCode(entry) + "结束" + entry;
                popJump(entry);
                debugInfos.add(debugInfo);

            }

            @Override
            public void dispose(AnimationState.TrackEntry entry) {
                super.dispose(entry);
            }

            @Override
            public void complete(AnimationState.TrackEntry entry) {

                String debugInfo = System.identityHashCode(entry) + "动画" + entry.getTrackIndex() + " 完成:" + entry;

                popJump(entry);
                debugInfos.add(debugInfo);
                super.complete(entry);

            }

            @Override
            public void event(AnimationState.TrackEntry entry, Event event) {
                System.out.println("动画:" + entry.getAnimation().getName() + " event :" + event.toString());
                super.event(entry, event);
            }
        });


    }

    @Override
    public void act(float delta) {
        super.act(delta);
        isJumping = jumpStack.size() > 0;
    }

    public void action(Action action, boolean loop) {
        AnimationState state = getAnimationState();

        String name = action.toString();
        Stack<AnimationState.TrackEntry> stack = getStack(action);
        switch (action) {
            case AIM:
            case DEATH:
            case HIT:
            case RUN:
            case WALK:
            case JUMP:
            case IDLE:
//                state.setAnimation(0, name, loop);
                if (!(stack != null && stack.size() == 0)) {
                    return;
                }
                state.setAnimation(0, name, loop);
            case SHOOT:
                if (!(stack != null && stack.size() == 0)) {
                    return;
                }
                state.addAnimation(1, name, loop,0.2f);
                debugInfos.add("Thread" + Thread.currentThread().getId() + " set action");
                break;


        }
    }

    public boolean isIdle() {
        return isIdle;
    }

    public void setIdle(boolean idle) {
        isIdle = idle;
    }

    public boolean isJumping() {
        return isJumping;
    }

    public void setJumping(boolean jumping) {
        isJumping = jumping;
    }

    public Array<String> getDebugInfos() {
        return debugInfos;
    }
}
