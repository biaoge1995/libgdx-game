package org.cbzmq.game.model;


import org.cbzmq.game.proto.Move2;

public class MoveTask {


    private final Character character;

    private long timestamp;

    private final Move2 move2;

    private final MyVector2 tmp = new MyVector2();
    private final MyVector2 avgV = new MyVector2();
    private boolean isFinish;

    private boolean isInitPosition;

    public MoveTask(Character character, Move2 move2) {
        this.character = character;
        this.move2 = move2;
    }

    /**
     * @param delta
     * @return
     */
    public void update(float delta) {
        if(!isInitPosition){
            character.setPosition(move2.sourcePosition);
            MyVector2 sub = tmp.set(move2.targetPosition).sub(move2.sourcePosition);
            //计算平均速度
            avgV.set(sub).scl(1/move2.time);
            isInitPosition=true;
        }

        if (!isFinish) {


            //根据当前客户端刷新频率计算需要执行的次数
            float multiple = move2.time  / delta;




//            tmp.set(targetPosition).sub(character.position);
            if(move2.time-delta>0){
                tmp.set(avgV).scl(delta);
                character.addPosition("task delta", tmp);
                move2.time-=delta;
            }else {
                tmp.set(avgV).scl(move2.time);
                character.addPosition("task residueTime", tmp);
                move2.time=0;
            }


//            if(multiple>=1.5f){
//                int round = Math.round(multiple);
//                tmp.scl(1f /round);
//                character.addPosition("task 补偿"+multiple,tmp);
//                residueTime-=delta;
//            }else {
//                character.addPosition("task 最后一次"+multiple,tmp);
//                residueTime=0;
//            }
            if (character.getPosition().epsilonEquals(move2.targetPosition,0.0001f)) {
                isFinish = true;
                System.out.println("到达指定位置");
            }

        }


    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    public boolean isFinish() {
        return isFinish;
    }



    public long getTimestamp() {
        return timestamp;
    }



    public static void main(String[] args) {
        System.out.println(Math.round(1.4));
    }
}
