package awt.game.command;

import awt.proto.enums.Status;

import java.util.*;

/**
 * @ClassName Test
 * @Description TODO
 * @Author chenbiao
 * @Date 2023/6/29 6:49 下午
 * @Version 1.0
 **/
abstract public class Command<T,F> {
    private F function;
    private T t;
    //事件戳
    private long startTimestamp;
    //事件戳
    private long endTimestamp;

    //指令计数器
    private int count;
    //需要执行的次数
    private int needCount;

    private Status status = Status.ACCEPT;

    private boolean isMainCommand=true;

    public Command(F function, T t, int needCount){
        this.function = function;
        this.t = t;
        this.needCount = needCount;

    }


    //命令中要执行的方法
    abstract Object function(Object ... obj);



    public boolean isMainCommand() {
        return isMainCommand;
    }

    public void setMainCommand(boolean mainCommand) {
        isMainCommand = mainCommand;
    }

    /**
     *执行动作
     */
    public Object exec() throws Exception {
        Object obj=null;
        if(status == Status.ACCEPT){
            if(count==0){
                startTimestamp = new Date().getTime();
            }
            if(needCount==0){
                return null;
            }
            if(count<needCount){
                //执行动作
                if(t == null){
                    throw  new Exception("构建命令时,主体不能未null");
                }
                if(function==null){
                    throw  new Exception("构建命令时,方法不能未null");
                }
                obj = function();
                count++;
                if(count==needCount){
                    status=Status.SUCCESS;
                    endTimestamp = new Date().getTime();
                }
            }
        }
        return obj;
    };


    abstract public Command<T,F> copy() throws Exception;

    public void copy(Command<T,F> copy){
        copy.setFunction(this.function);
        copy.setT(copy.t);
        copy.setCount(this.count);
        copy.setNeedCount(this.needCount);
        copy.setStartTimestamp(this.startTimestamp);
        copy.setEndTimestamp(this.endTimestamp);
        copy.setStatus(this.status);
        copy.setMainCommand(this.isMainCommand);

    };


    public Status status(){
        return status;
    }

    public void setAccept(){
        status = Status.ACCEPT;
    }

    public void await(){
        status = Status.WAIT;
    }

    public void interrupt(){
//        System.out.println(commandName+"执行命令被打断"+needCount+"/"+count);
        status = Status.INTERRUPT;
    }

    public void failed(){
//        System.out.println(commandName+"执行命令被失败"+needCount+"/"+count);
        status = Status.FAILED;
    }


    public void setCount(int count) {
        this.count = count;
    }

    public void setNeedCount(int count) {
        this.needCount = count;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public int getCount() {
        return count;
    }

    public int getNeedCount() {
        return needCount;
    }



    @Override
    public String toString() {
        return "Command{" +
                "startTimestamp=" + startTimestamp +
                ", endTimestamp=" + endTimestamp +
                ", count=" + count +
                ", needCount=" + needCount +
                '}';
    }


    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public F getFunction() {
        return function;
    }

    public void setFunction(F function) {
        this.function = function;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}






