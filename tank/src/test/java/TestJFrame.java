import lombok.Data;
import awt.model.domain.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

import static java.lang.Thread.sleep;

/**
 * @author chenbiao
 * @date 2020-12-19 21:20
 */
public class TestJFrame extends JFrame {


    public static void main(String[] args) {
        new TestJFrame();
    }

    /**
     * @param
     * @param
     */

    public TestJFrame() {
        super("坦克大战-");
        this.setDefaultLookAndFeelDecorated(true);
        this.setSize(800, 800);
        this.setBackground(Color.black);

        MyPanel myPanel = new MyPanel();
        myPanel.setBackground(Color.white);
        this.add(myPanel);

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLayout(null);     //采用绝对布局，通过坐标指定组件的位置,注意程序顺序

    }


}

@Data
class MyPanel extends JPanel {


    public MyPanel() {

    }


    /**
     * 画图方法
     *
     * @param g
     */
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;

//        g2.clearRect(0, 0, this.getSize().width, this.getSize().height);
//
//        g2.setColor(Color.BLACK);
//        g2.fillOval(0,0,10,10);
//        g2.fillRect(0,0,10,10);
//
//        Rectangle rect = new Rectangle(100, 100, 80, 20);
//        Rectangle rect2 = new Rectangle(60, 100, 30, 20);
//
//        AffineTransform transform = new AffineTransform();
//        AffineTransform transform2 = new AffineTransform();
//
//
//        transform.rotate(Math.toRadians(-30), rect.getX()+rect.getWidth()/2 , rect.getY()+rect.getHeight()/2 );
//        transform2.rotate(Math.toRadians(-30),rect.getX()+rect.getWidth()/2 , rect.getY()+rect.getHeight()/2 );
//
//        Shape transformed = transform.createTransformedShape(rect);
//        Shape transformed2 = transform2.createTransformedShape(rect2);
//
//        g2.fill(transformed);
//        g2.fill(transformed2);


        ObbRect track2 = new ObbRect(100, 100, 20, 80,0);
        ObbRect barrel = new ObbRect(120, 100, 20, 80,0);

        System.out.println(track2.intersects(barrel));

//        g.setColor(Color.DARK_GRAY);
//        g2.fill(track2);
//        g.setColor(Color.GREEN);
//        g2.fill(barrel);



//        Shape rotateMainTank = rotate(mainTank, 30, tank.getXCoordinate(), tank.getYCoordinate());
//        Shape rotateTrack1 = rotate(track1, 30, tank.getXCoordinate(), tank.getYCoordinate());
//        Shape rotateTrack2 = rotate(track2, 30, tank.getXCoordinate(), tank.getYCoordinate());
//        Shape rotateBarrel = rotate(barrel, 30, tank.getXCoordinate(), tank.getYCoordinate());
//
//        g.setColor(Color.DARK_GRAY);
//        g2.fill(rotateMainTank);
//        g2.setColor(Color.GREEN);
//        g2.fill(rotateTrack1);
//        g2.fillOval(tank.getXCoordinate() - r, tank.getYCoordinate() - r, 2 * r, 2 * r);
//        g2.fill(rotateTrack2);
//        g2.fill(rotateBarrel);


    }

    /**
     * @param rectangle
     * @param theta
     * @param anchorx
     * @param anchory
     * @return
     */
    public Shape rotate(Rectangle rectangle, double theta, double anchorx, double anchory) {
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(theta), anchorx, anchory);
        Shape transformed = transform.createTransformedShape(rectangle);
        return transformed;

    }


}
