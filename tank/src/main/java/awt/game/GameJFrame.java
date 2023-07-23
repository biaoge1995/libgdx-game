package awt.game;


import awt.proto.enums.MsgAction;
import awt.proto.enums.TankAction;
import awt.service.BgmSrv;
import awt.service.ClientSrv;
import lombok.Data;
import awt.model.Constans;
import awt.model.Music;
import awt.model.domain.*;
import awt.model.msg.TankActionMsg;
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;


import static java.lang.Thread.sleep;

/**
 * @author chenbiao
 * @date 2020-12-19 21:20
 */
public class GameJFrame extends JFrame {
    private MyPanel myPanel;
    private MyPanel smallMap;
    private JLabel jLabel;
    private JLabel jLabelPlayerPosition;
    private Player player;
    private boolean isStarted = false;
    private long time = 0;
    private int fpsTimes = 0;
    private String delayInfo = "";
    private Set<String> actionUuid = new HashSet<String>();
    private GameMapContext gameMapContext;


    /**
     * @param
     * @param player
     */

    public GameJFrame(GameMapContext gameMapContext,String title, GameClient gameClient, Player player) {
        super("坦克大战-"+title +" player:"+ player.getId());
        this.setDefaultLookAndFeelDecorated(true);
        this.setSize(800, 800);
        this.setBackground(Color.BLACK);

        this.player = player;
        this.gameMapContext = gameMapContext;

        myPanel = new MyPanel(gameMapContext);
        myPanel.setBounds(0, 0, 500, 800);
        myPanel.setBackground(Color.DARK_GRAY);
        jLabel = new JLabel();
        jLabel.setBounds(0, 0, 20, 20);
        jLabel.setForeground(Color.white);
//        jLabel.setHorizontalAlignment(JLabel.RIGHT);
        jLabelPlayerPosition = new JLabel();
        jLabelPlayerPosition.setForeground(Color.white);
        jLabelPlayerPosition.setHorizontalAlignment(JLabel.LEFT);
        myPanel.addKeyListener(new MyKeyListener(myPanel, gameClient, player));
        myPanel.add(jLabel);
        myPanel.add(jLabelPlayerPosition);
        myPanel.setFocusable(true);

        this.add(myPanel);


        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLayout(null);     //采用绝对布局，通过坐标指定组件的位置,注意程序顺序
        isStarted = true;
    }


    public boolean isStarted() {
        return isStarted;
    }


    /**
     * 重绘画板
     */
    public void rePaintPanel() {
        myPanel.repaint();

        Element element = gameMapContext.getPlayerElement().get(player.getId());
        if (element != null) {
            jLabelPlayerPosition.setText("<html>pos: (X:"
                    + element.getXCoordinate() + " Y:"
                    + element.getYCoordinate() + ") map_pos：(X:"
                    + element.getMapXCoordinate() + " Y:"
                    + element.getMapYCoordinate() + " </html>"
            );

        }
        //计算延迟
        Map<String, TankActionMsg> idToAction = gameMapContext.getIdToAction();
        if (idToAction != null && idToAction.containsKey(player.getId())) {
            TankActionMsg tankActionMsg = idToAction.get(player.getId());
            String tankAction = tankActionMsg.getTankAction().toString();
            String uuid = tankActionMsg.getUuid();
            if (!actionUuid.contains(uuid)) {
                long time = tankActionMsg.getTime();
                long now = new Date().getTime();
                long delay = now - time;
                delayInfo = "tankAction:" + tankAction + "<br>delay:" + delay + "ms";
                actionUuid.add(uuid);
            }
            if (actionUuid.size() == 10) {
                actionUuid.clear();
            }
        }

        //计算fps
        if (fpsTimes == 0) {
            this.time = new Date().getTime();
            jLabel.setText(String.valueOf(60));
        } else if (fpsTimes == 60) {
            Date now = new Date();
            double fps = 60.0 / ((now.getTime() - this.time) / 1000.0);
            jLabel.setText("<html>fps:" + String.format("%.1f", fps)
                    + "<br>" + delayInfo + "</html>");
            fpsTimes = 0;
            this.time = new Date().getTime();
        }
        fpsTimes++;
        try {
            sleep(Constans.time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public Element getLockElement(){
        return myPanel.getLockElement();
    }
    public void setLockElement(Element element){
         myPanel.setLockElement(element);
    }
}

@Data
class MyPanel extends JPanel {
    private GameMapContext gameMapContext;
    //展示用的坐标
    private int drawX = 0;
    private int drawY = 0;
    private int drawWidth = 800;
    private int drawHeight = 800;
    //小地图的比例
    private double smallMapPct = 0.2;
    private boolean isDrawSmallMap = false;
    //保持此元素在视角正中心
    private Element lockElement;
    private Distance distance = new Distance(0, 0);

    public MyPanel(GameMapContext gameMapContext) {
        super();
        this.gameMapContext = gameMapContext;
    }

    /**
     * @param x
     */
    public void jFrameXMove(int x) {

        this.setDrawX(this.getDrawX() + x);
        System.out.println("窗口 x轴移动 " + x + " (" + this.getDrawX() + "," + this.getDrawY() + ")");
    }

    /**
     * 窗口 纵向hua动
     *
     * @param y
     */
    public void jFrameYMove(int y) {
        this.setDrawY(this.getDrawY() + y);
        System.out.println("窗口 y轴移动 " + y + " (" + this.getDrawX() + "," + this.getDrawY() + ")");
    }


    /**
     * 画图方法
     *
     * @param g
     */
    public void paint(Graphics g) {
        super.paint(g);
        if (lockElement != null) {
            //计算元素与屏幕中间的距离
            distance = lockElement.FromMapPositionDistance(drawX + drawWidth / 2, drawY + drawHeight / 2);

        } else {
            //记录偏移量
            setDrawX(distance.getDistanceX() + getDrawX());
            setDrawY(distance.getDistanceY() + getDrawY());
            //归零
            distance.setDistanceX(0);
            distance.setDistanceY(0);
        }
        gameMapContext.draw(g, drawX+distance.getDistanceX(), drawY+distance.getDistanceY(), drawWidth, drawHeight);
    }
}


class MyKeyListener implements KeyListener {
    private GameClient gameClient;
    private Player player;

    private Music music;
    private MyPanel myPanel;

    public MyKeyListener(MyPanel myPanel, GameClient gameClient, Player player) {
        this.gameClient = gameClient;
        this.player = player;
        this.myPanel = myPanel;
        this.music = BgmSrv.initMusic(BgmSrv.fireMusicFilePath);
    }

    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
//    @SneakyThrows
//    public void keyReleased(KeyEvent e) {
//        //TODO
//        int key = e.getKeyCode();
//        if (key == KeyEvent.VK_W) {
//            gameClient.TankAction(TankAction.STOP,1);
//        }
//        if (key == KeyEvent.VK_S) {
//            clientSrv.tankAction(player.getId(), TankAction.STOP);
//
//        }
//        if (key == KeyEvent.VK_D) {
//            clientSrv.tankAction(player.getId(), TankAction.STOP);
//
//        }
//        if (key == KeyEvent.VK_A) {
//            clientSrv.tankAction(player.getId(), TankAction.STOP);
//        }
//
//    }


    @SneakyThrows
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
//        sleep(Constans.time);
        // w键盘往上跑
        if (key == KeyEvent.VK_W) {

            gameClient.TankAction(TankAction.TURN_UP,1);
            gameClient.TankAction(TankAction.TANK_MOVE,40);
        }

        // S键往下跑

        if (key == KeyEvent.VK_S) {
            gameClient.TankAction(TankAction.TURN_DOWN,1);
            gameClient.TankAction(TankAction.TANK_MOVE,40);

        }
        // D
        if (key == KeyEvent.VK_D) {
            gameClient.TankAction(TankAction.TURN_RIGHT,1);
            gameClient.TankAction(TankAction.TANK_MOVE,40);

        }
        // A
        if (key == KeyEvent.VK_A) {
            gameClient.TankAction(TankAction.TURN_LEFT,1);
            gameClient.TankAction(TankAction.TANK_MOVE,40);


        }
        //开火
        if (key == KeyEvent.VK_ENTER) {
            sleep(Constans.time);
            gameClient.TankAction(TankAction.FIRE,1);

//            BgmSrv bgmSrv = new BgmSrv(music);
//            new Thread(bgmSrv).start();

        }
        //装弹
        if (key == KeyEvent.VK_R) {
//            sleep(Constans.time);
            gameClient.TankAction(TankAction.AMMUNITION_LOADING,1);

        }
        //开始游戏
        if (key == KeyEvent.VK_P) {
            sleep(Constans.time);
            gameClient.userAction(MsgAction.LOGIN);
//            clientSrv.getMap( player);

        }
        //加入游戏
        if (key == KeyEvent.VK_J) {
            sleep(Constans.time);
            gameClient.userAction(MsgAction.GAME_JOIN);

        }
        //锁定视角
//        if (key == KeyEvent.VK_Y || key == KeyEvent.VK_SPACE) {
//            sleep(Constans.time);
//            clientSrv.lockPerspectiveOnOff(player);
//
//        }
        //窗口滑动
        if (key == KeyEvent.VK_RIGHT) {
            sleep(Constans.windosMoveTime);
            myPanel.jFrameXMove(Constans.windosMoveStep);

        }
        //窗口滑动
        if (key == KeyEvent.VK_LEFT) {
            sleep(Constans.windosMoveTime);
            myPanel.jFrameXMove(-Constans.windosMoveStep);

        }
        //窗口滑动
        if (key == KeyEvent.VK_UP) {
            sleep(Constans.windosMoveTime);
            myPanel.jFrameYMove(-Constans.windosMoveStep);

        }
        //窗口滑动
        if (key == KeyEvent.VK_DOWN) {
            sleep(Constans.windosMoveTime);
            myPanel.jFrameYMove(Constans.windosMoveStep);

        }

    }



    /**
     * 睡眠
     *
     * @param millis
     */
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);//不仅可以消除延迟，还可以控制延迟时间
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }



}
