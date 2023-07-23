package awt.model.domain;

import awt.proto.enums.Direct;
import awt.proto.enums.ElementType;
import awt.proto.enums.TankAction;
import lombok.Data;

import java.awt.*;
import java.util.*;

/**
 * @author chenbiao
 * @date 2020-12-19 11:28
 * 坦克类
 */
@Data
public class Tank extends Element {

//    private Vector<Bullet> bullets = new Vector<>(); //弹夹
    private int bulletsSize;//弹夹容量
    private double circularRPct = 0.5;
    private Set<Integer> secondSet = new HashSet<>();
    public int count = 0;//计数器


    public Tank(int mapX1Coordinate,int mapY1Coordinate){
        this(mapX1Coordinate,mapY1Coordinate,20,20);
    }


    public Tank(int mapXCoordinate, int mapYCoordinate, int height, int wide) {
        super(ElementType.TANK, mapXCoordinate, mapYCoordinate, height, wide);
        this.setBulletsSize(30);
        this.setBlood(10);
        this.setDirect(Direct.UP);
        this.setStep(2);
        this.setSpeed(40);
        this.setFullBlood(10);
        this.setHardness(1);
        this.setMoved(true);

    }

    public Tank(Element element) {
        toElementSon(element, this);
    }


    /**
     * 处理动作
     *
     * @param tankAction
     * @return
     */
    public void tankAction( TankAction tankAction) {
        switch (tankAction) {
            case FIRE:
                this.fire();
                break;
            case STOP:
                this.setMoved(false);
                break;
            case TANK_MOVE:
                this.move();
                this.setMoved(true);
                break;
            case STAND_BY:
                break;
            case AMMUNITION_LOADING:
                this.ammunitionLoading();
                break;
            case TURN_UP:
                switch (this.getDirect()) {
                    case RIGHT:
                    case LEFT:
                        exchangeDirectXy();
                }
                this.setMoved(true);
                this.setDirect(Direct.UP);
                break;
            case TURN_LEFT:
                switch (this.getDirect()) {
                    case UP:
                    case DOWN:
                        exchangeDirectXy();
                }
                this.setMoved(true);
                this.setDirect(Direct.LEFT);
                break;
            case TURN_RIGHT:
                switch (this.getDirect()) {
                    case UP:
                    case DOWN:
                        exchangeDirectXy();
                }
                this.setMoved(true);
                this.setDirect(Direct.RIGHT);
                break;
            case TURN_DOWN:
                switch (this.getDirect()) {
                    case RIGHT:
                    case LEFT:
                        exchangeDirectXy();
                }
                this.setMoved(true);
                this.setDirect(Direct.DOWN);
                break;
        }
    }

    /**
     * 开火
     */
    public Bullet fire() {
        Vector<Element> bullets = this.getChildElement();
        Iterator<Element> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet b = (Bullet) it.next();
            if (!b.isFire()) {
                b.setMapXCoordinate(this.getMapXCoordinate());
                b.setMapYCoordinate(this.getMapYCoordinate());
                b.setDirect(this.getDirect());
                b.setMoved(true);
                b.setFire(true);
                b.setPlayId(this.getPlayId());
//                if(this.getPlayId()>0){
//                    b.setGameRole(GameRole.PLAYER);
//                }
                offerElementToQueue(b);
                it.remove();
                setUpdate(true);
                return b;
            }

        }

        return null;
    }

    /**
     * 装弹
     */
    private Boolean ammunitionLoading() {
        int bulletsSize = this.getBulletsSize();
        Vector<Element> bullets = this.getChildElement();
        int i = bulletsSize - bullets.size();
        for (int j = 0; j < i; j++) {
            Bullet bullet = new Bullet(this.getPlayId());
            bullets.add(bullet);
        }
        setUpdate(true);
        return true;
    }




    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Coordinate coordinate = this.getCoordinate();

        //获取圆半径
        int r = (int) (Math.min(this.getHeight(), this.getWide()) * this.getCircularRPct() / 2);
        int trackWidth = this.getWide();
        int trackHeight = ((this.getHeight() - 2 * r) / 2);
        //炮管子
        int barrelWidth = this.getWide() / 2 - 2;
        int barrelHeight = r / 3;
        int barrelX1 = this.getXCoordinate();
        int barrelY1 = this.getYCoordinate() - barrelHeight / 2;

        //第一条履带
        int trackX1 = coordinate.getX1Coordinate();
        int trackY1 = coordinate.getY1Coordinate();
        //第二条履带
        int trackX2 = trackX1;
        int trackY2 = trackY1 + this.getHeight() - r;




        Rectangle mainTank = new Rectangle(coordinate.getX1Coordinate()
                , coordinate.getY1Coordinate()
                , this.getWide(), this.getHeight());
        Rectangle track1 = new Rectangle(trackX1, trackY1, trackWidth, trackHeight);
        Rectangle track2 = new Rectangle(trackX2, trackY2, trackWidth, trackHeight);
        Rectangle barrel = new Rectangle(barrelX1, barrelY1, barrelWidth, barrelHeight);




        Shape rotateMainTank = rotate(mainTank, getRotationDegree(getDirect()), this.getXCoordinate(), this.getYCoordinate());
        Shape rotateTrack1 = rotate(track1, getRotationDegree(getDirect()), this.getXCoordinate(), this.getYCoordinate());
        Shape rotateTrack2 = rotate(track2, getRotationDegree(getDirect()), this.getXCoordinate(), this.getYCoordinate());
        Shape rotateBarrel = rotate(barrel, getRotationDegree(getDirect()), this.getXCoordinate(), this.getYCoordinate());

        g.setColor(Color.DARK_GRAY);
        g2.fill(rotateMainTank);
        g2.setColor(Color.GREEN);
        g2.fill(rotateTrack1);
        g2.fillOval(this.getXCoordinate() - r, this.getYCoordinate() - r, 2 * r, 2 * r);
        g2.fill(rotateTrack2);
        g2.fill(rotateBarrel);
        drawBlood(g2,this.getDirect());




    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid());
    }

    public static void main(String[] args) {
        Tank tank = new Tank(0, 0, 20, 10);
        Tank tank2 = new Tank(0, 0, 10, 10);
        Set<Tank> hashSet = new HashSet();
        System.out.println(tank.hashCode());
        System.out.println(tank2.hashCode());
        hashSet.add(tank2);
        hashSet.add(tank);
        if (tank == tank2) {
            System.out.println(true);
        }
        System.out.println(hashSet);
    }
}
