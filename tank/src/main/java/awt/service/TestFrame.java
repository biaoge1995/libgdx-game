package awt.service;


import awt.utils.MapUtil;
import awt.model.Constans;
import awt.model.domain.Element;
import awt.game.GameMapContext;
//import awt.algo.ShortestPath;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.Timer;

import static java.lang.Thread.sleep;

/**
 * @author chenbiao
 * @date 2020-12-19 21:20
 */
public class TestFrame extends JFrame{
    private TestPanel myPanel;
    private JLabel jLabel;
    private boolean isStarted = false;
    private long time = 0;
    private int fpsTimes = 0;
    private String delayInfo = "";


    public static void main(String[] args) throws Exception {
        GameMapContext test = MapUtil.loadGameMap("/Users/chenbiao/tank/map.xlsx", "test");
//        ShortestPath shortestPath = new ShortestPath(test.getEleMap());
        TestFrame testFrame = new TestFrame(test.getEleMap());

        java.util.Timer time = new Timer();

//        time.schedule(shortestPath,1000,1000);

        time.schedule(new TimerTask() {
            @Override
            public void run() {
                testFrame.rePaintPanel();
            }
        }, 1000, Constans.time);
    }

    public TestFrame(Vector<Vector<Element>> vectors) {
        super("测试路径算法");
        this.setDefaultLookAndFeelDecorated(true);
        this.setSize(800, 800);
        this.setBackground(Color.BLACK);

        jLabel = new JLabel();
        jLabel.setBounds(0, 0, 20, 20);
        jLabel.setForeground(Color.white);
        myPanel = new TestPanel(vectors);
        myPanel.setBounds(0, 0, 500, 800);
        myPanel.setBackground(Color.DARK_GRAY);
        myPanel.add(jLabel);
        myPanel.setFocusable(true);
        this.add(myPanel);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLayout(null);     //采用绝对布局，通过坐标指定组件的位置,注意程序顺序
    }


    public boolean isStarted() {
        return isStarted;
    }


    /**
     * 重绘画板
     */
    public void rePaintPanel() {
        myPanel.repaint();
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
}

@Data
class TestPanel extends JPanel {
    private Vector<Vector<Element>> vectors;
    //展示用的坐标
    private int drawX = 0;
    private int drawY = 0;
    private int drawWidth = 800;
    private int drawHeight = 800;

    public TestPanel(Vector<Vector<Element>> vectors) {
        super();
        this.vectors = vectors;
    }


    /**
     * 画图方法
     *
     * @param g
     */
    public void paint(Graphics g) {
        super.paint(g);
        for (int i = 0; i < vectors.size(); i++) {
            Vector<Element> elementVector = vectors.get(i);
            for (int i1 = 0; i1 < elementVector.size(); i1++) {
                Element element = elementVector.get(i1);
                element.draw(g);
            }
        }
//        gamerMap.draw(g, drawX, drawY, drawWidth, drawHeight);
    }
}



