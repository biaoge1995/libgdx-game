import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;


public class testTimer {
    public static void main(String[] args) {
        Timer time = new Timer();
        time.schedule(new MyTask(),1000,2000);
//        time.cancel();
    }
}


class MyTask extends TimerTask{
    public long lastTime=System.currentTimeMillis();

    @Override
    public void run() {
        long l = System.currentTimeMillis() - lastTime;
        System.out.println("______"+l);
        lastTime = System.currentTimeMillis();
        try {
            Thread.sleep(4500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
