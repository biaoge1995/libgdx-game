package org.cbzmq.game.model;

import java.util.List;

/**
 * @ClassName Observe
 * @Description 多线程观察者
 * @Author chenbiao
 * @Date 2023/8/4 2:39 下午
 * @Version 1.0
 **/
public abstract class ObserverRunnable implements Runnable {
    final protected LifeCycleListener listener;

    public ObserverRunnable(LifeCycleListener listener) {
        this.listener = listener;
    }

    //去通知被的线程
    protected void notifyChange(RunnableEvent runnableEvent) {
        listener.onEvent(runnableEvent);
    }

    //枚举了三个类型
    public enum RunnableStatue {
        RUNNING, ERROR, DONE
    }

    public static class RunnableEvent {
        private final RunnableStatue runnableStatue;

        //那个线程在执行它的过程中出现了问题
        private final Thread thread;
        //失败了的话什么原因引起的失败呢
        private final Throwable throwable;

        public RunnableEvent(RunnableStatue runnableStatue, Thread thread, Throwable throwable) {
            this.runnableStatue = runnableStatue;
            this.thread = thread;
            this.throwable = throwable;
        }

        public RunnableStatue getRunnableStatue() {
            return runnableStatue;
        }

        public Thread getThread() {
            return thread;
        }

        public Throwable getThrowable() {
            return throwable;
        }

    }

    public static class ThreadLiftListenerImpl implements LifeCycleListener {
        //因为在多线程环境出现共享数据的问题所以需要枷锁 这里简单定义一下锁
        private final Object LOCK = new Object();


        public void concurrentQuery(List<String> ids){
            if(ids==null||ids.isEmpty()) return;
            for (String id : ids) {
                new Thread(new ObserverRunnable(this) {
                    @Override
                    public void run() {
                       RunnableEvent event=null;
                        try {
                            notifyChange(new RunnableEvent(RunnableStatue.RUNNING,Thread.currentThread(),null));
                            Thread.sleep(1000);
                            notifyChange(new RunnableEvent(RunnableStatue.DONE,Thread.currentThread(),null));
                        } catch (InterruptedException e) {
                            notifyChange(new RunnableEvent(RunnableStatue.ERROR,Thread.currentThread(),e));
                        }

                    }
                });
            }
        }


        @Override
        public void onEvent(RunnableEvent event) {
            synchronized (LOCK){
                System.out.println("The runnable [" + event.getThread().getName() + "] data changed and state is [" + event.getThrowable() + "]");
                if(event.getThrowable()!=null){
                    System.out.println("The runnable [" + event.getThread().getName() + "] process failed.");
                    event.getThrowable().printStackTrace();
                }
            }
        }
    }

    interface LifeCycleListener {

        //通知的方法
        void onEvent(ObserverRunnable.RunnableEvent event);
    }
}
