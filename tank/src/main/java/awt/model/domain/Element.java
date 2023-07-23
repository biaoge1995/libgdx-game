package awt.model.domain;


import awt.proto.enums.Direct;
import awt.proto.enums.ElementAction;
import awt.proto.enums.ElementType;
import awt.proto.enums.GameRole;
import lombok.Data;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.*;

/**
 * @author chenbiao
 * @date 2020-12-19 12:37
 */
@Data
public class Element implements ElementInteractive, Comparable<Element> {
    private int playId;//初始化时会附上操作者的id
    private int increaseId;//自增id;
    private GameRole gameRole = GameRole.SCENE;
    private ElementType elementType;
    private int xCoordinate; // 物体中心位置在窗口的X坐标
    private int yCoordinate; // 物体中心位置在窗口的Y坐标
    private int mapXCoordinate; // 物体中心位置地图中的X坐标
    private int mapYCoordinate; // 物体中心位置地图中的的Y坐标

    private Direct direct = Direct.UP;
    //旋转角度的弧度值
    private double theta;
    private double[][] vectors = new double[2][2];

    private int height;//长
    private int wide; //宽
    private int step = 0; //步长
    private int speed = 0;//速度（像素/1ms）
    private boolean isMoved = false; //是否处于移动状态
    private boolean isLive = true;//是否存活
    private boolean isCanFly = false; //是否飞行
    private int hardness = 1; //硬度
    private double fullBlood = 0;//满血的血条
    private double blood = 0;//当前血条
    private int power = 0; //破坏力 一般是发生碰撞时
    private boolean isShow = true;//是否展示
    private boolean isCollisionDead = false; //是否碰撞后即死亡 用于不计算自己血量消耗的一次性元素
    private boolean isNeedSystemControlMove = false;//是否需要系统控制其移动
    private int drawRank = 0;//渲染时的有限等级 越大 越优先
    private boolean isBuild = false;
    private Color color = Color.CYAN;

    private boolean isCanSee = false;
    private int viewR = 350;// 视野半径
    private Vector<Element> childElement = new Vector<>();
    private Set<Element> canSeeOtherElement = new HashSet<>();
    private String uuid = UUID.randomUUID().toString();

    private int distance = 0;//已走射程计数器
    private boolean isCanBoom = false;

    //是否发生过更新
    private boolean isUpdate = true;

    //等待加入地图的元素队列
    private Queue<Element> joinMapQueue = new LinkedList<>();


    public Element() {
        this.height = 25;
        this.wide = 25;
    }

    public Element(ElementType elementType, int mapXCoordinate, int mapYCoordinate, int height, int wide) {
        this.elementType = elementType;
        this.mapXCoordinate = mapXCoordinate;
        this.mapYCoordinate = mapYCoordinate;
        this.height = height;
        this.wide = wide;
    }



    /**
     * 血量操作
     * @param step
     */
    public void setStep(int step){
        //防止血量扣成负数
        if(step<0){
            step=0;
        }else {
            this.step = step;
        }

    }


    /**
     * 血量操作
     * @param blood
     */
    public void setBlood(double blood){
        //防止血量扣成负数
        if(blood<0){
            this.blood=0;
        }else {
            this.blood = blood;
        }

    }

    /**
     *
     * @param action 动作
     * @param newData 变更数值
     */

    public void action(ElementAction action, int newData){
        switch (action) {
            case MOVE:
                this.move();

                break;
            case LIVE:
                this.setLive(true);
                break;
            case DEAD:
                this.beDead();
                break;
            case BLOOD:
                setBlood(newData);
                break;
            case STEP:
                setStep(newData);
                break;
        }
    }

    /**
     *
     * @param pct
     * @param diffX
     * @param diffY
     * @return
     */

    public Element EleProportionalScaling(double pct, int diffX, int diffY) {
        return EleProportionalScalingToNewOne(pct, diffX, diffY, this);
    }


    /**
     * 将子元素放到队列中等待加入地图
     * @param e
     */
    public void offerElementToQueue(Element e){
        joinMapQueue.offer(e);
    }

    public boolean isJoinMapQueueHasEle(){
        return joinMapQueue.size()>0;
    }

    public Element releaseElement(){
        return joinMapQueue.poll();
    }


    /**
     * 获取等比缩放后
     * 挪动 x y
     *
     * @param pct
     * @return
     */
    public Element EleProportionalScalingToNewOne(double pct, int diffX, int diffY, Element newOne) {

        newOne.setXCoordinate((int) (this.getXCoordinate() * pct) + diffX);
        newOne.setYCoordinate((int) (this.getYCoordinate() * pct) + diffY);
        newOne.setMapXCoordinate((int) (this.getMapXCoordinate() * pct));
        newOne.setMapYCoordinate((int) (this.getMapYCoordinate() * pct));
        newOne.setHeight((int) (this.getHeight() * pct));
        newOne.setWide((int) (this.getWide() * pct));
        newOne.setSpeed((int) (this.getSpeed() * pct));
        newOne.setStep((int) (this.getStep() * pct));
        return newOne;
    }

    public void executeCommand() {
    }

    public double getDamagePct() {
        double pct = 1.0 - hardness / 10.0;
        if (pct < 0) {
            return 0;
        } else
            return pct;
    }

    /**
     * 获取多边形
     * @param mapXCoordinate
     * @param mapYCoordinate
     * @param width
     * @param height
     * @param theta
     * @return
     */
    public static Polygon getPolygon(int mapXCoordinate, int mapYCoordinate, int width, int height, int theta) {
        Rectangle r = new Rectangle( (mapXCoordinate - width / 2),  (mapYCoordinate - height / 2), width, height);
        AffineTransform at = AffineTransform.getRotateInstance(
                theta, mapXCoordinate, mapYCoordinate);
        PathIterator i = r.getPathIterator(at);
        Polygon polygon = new Polygon();
        while (!i.isDone()) {
            double[] xy = new double[2];
            i.currentSegment(xy);
            polygon.addPoint((int) xy[0], (int) xy[1]);
            System.out.println(Arrays.toString(xy));

            i.next();
        }
        return polygon;
    }



    /**
     * 判断是否相交
     *
     * @param other
     * @return
     */
    public boolean intersects(Element other) {
        double[] distanceVector = {
                other.mapXCoordinate - mapXCoordinate,
                other.mapYCoordinate - mapYCoordinate
        };

        for (int i = 0; i < 2; ++i) {
            if (getProjectionRadius(vectors[i]) + other.getProjectionRadius(vectors[i])
                    <= dot(distanceVector, vectors[i])) {
                return false;
            }
            if (getProjectionRadius(other.vectors[i]) + other.getProjectionRadius(other.vectors[i])
                    <= dot(distanceVector, other.vectors[i])) {
                return false;
            }
        }

        return true;
    }


    /**
     * 矢量点积
     *
     * @param a
     * @param b
     * @return
     */
    private double dot(double[] a, double[] b) {
        return Math.abs(a[0] * b[0] + a[1] * b[1]);
    }

    /**
     * 转化为矢量
     */
    void resetVector() {
        vectors[0][0] = Math.cos(theta);
        vectors[0][1] = Math.sin(theta);
        vectors[1][0] = vectors[0][1];
        vectors[1][1] = vectors[0][0];
    }

    private double getProjectionRadius(double[] vector) {
        return (wide * dot(vectors[0], vector) / 2
                + height * dot(vectors[1], vector) / 2);
    }


    /**
     * 获取整体坐标
     *
     * @return
     */
    public Coordinate getCoordinate() {
        Coordinate coordinate = new Coordinate();
        coordinate.setX1Coordinate(xCoordinate - wide / 2);
        coordinate.setX2Coordinate(xCoordinate + wide / 2);
        coordinate.setY1Coordinate(yCoordinate - height / 2);
        coordinate.setY2Coordinate(yCoordinate + height / 2);
        return coordinate;
    }

    /**
     * 获取地图坐标
     *
     * @return
     */
    public Coordinate getMapCoordinate() {
        Coordinate coordinate = new Coordinate();
        coordinate.setX1Coordinate(mapXCoordinate - wide / 2);
        coordinate.setX2Coordinate(mapXCoordinate + wide / 2);
        coordinate.setY1Coordinate(mapYCoordinate - height / 2);
        coordinate.setY2Coordinate(mapYCoordinate + height / 2);
        return coordinate;
    }



    public Element copy() {
        Element newOne = null;
        try {
            newOne = this.getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        newOne.setElementType(this.getElementType());
        newOne.setXCoordinate(this.getXCoordinate());
        newOne.setYCoordinate(this.getYCoordinate());
        newOne.setMapXCoordinate(this.getMapXCoordinate());
        newOne.setMapYCoordinate(this.getMapYCoordinate());
        newOne.setHeight(this.getHeight());
        newOne.setWide(this.getWide());
        newOne.setStep(this.getStep());
        newOne.setSpeed(this.getSpeed());
        newOne.setMoved(this.isMoved());
        newOne.setDirect(this.getDirect());
        newOne.setLive(this.isLive());
        newOne.setCanFly(this.isCanFly());
        newOne.setHardness(this.getHardness());
        newOne.setFullBlood(this.getFullBlood());
        newOne.setBlood(this.getBlood());
        newOne.setPower(this.getPower());
        newOne.setShow(this.isShow());
        newOne.setCanFly(this.isCanFly());
        newOne.setDrawRank(this.getDrawRank());
        newOne.setCollisionDead(this.isCollisionDead());
        newOne.setNeedSystemControlMove(this.isNeedSystemControlMove());
        newOne.setDrawRank(this.drawRank);
        newOne.setBuild(this.isBuild);
        newOne.setColor(this.color);
        newOne.setCanSee(this.isCanSee);
        newOne.setViewR(this.viewR);
        //注意此处只是给了对象的引用，并未真正copy
        newOne.setCanSeeOtherElement(this.canSeeOtherElement);
        return newOne;
    }


    /**
     * 转换为子类
     *
     * @param element
     * @param <T>
     * @return
     */
    public <T extends Element> void toElementSon(Element element, T elementSon) {
        elementSon.setElementType(element.getElementType());
        elementSon.setXCoordinate(element.getXCoordinate());
        elementSon.setYCoordinate(element.getYCoordinate());
        elementSon.setMapXCoordinate(this.getMapXCoordinate());
        elementSon.setMapYCoordinate(this.getMapYCoordinate());
        elementSon.setHeight(element.getHeight());
        elementSon.setWide(element.getWide());
        elementSon.setStep(element.getStep());
        elementSon.setSpeed(element.getSpeed());
        elementSon.setMoved(element.isMoved());
        elementSon.setDirect(element.getDirect());
        elementSon.setLive(element.isLive());
        elementSon.setCanFly(element.isCanFly());
        elementSon.setHardness(element.getHardness());
        elementSon.setFullBlood(element.getFullBlood());
        elementSon.setBlood(element.getBlood());
        elementSon.setPower(element.getPower());
        elementSon.setShow(element.isShow());
        elementSon.setCanFly(element.isCanFly());
        elementSon.setDrawRank(element.getDrawRank());
        elementSon.setCollisionDead(element.isCollisionDead());
        elementSon.setNeedSystemControlMove(element.isNeedSystemControlMove());
    }

    /**
     * 是否需要将元素放入到地图中
     *
     * @return
     */
    public boolean isJoinToMap() {
        if (this.isLive) {
            return true;
        }
        return false;
    }

    /**
     * 是否需要移除地图
     *
     * @return
     */
    public boolean isRemoveOutMap() {
        if (!this.isLive) {
            return true;
        }
        return false;
    }

    public boolean isLive() {
        if (blood <= 0) {
            return false;
        }
        return isLive;
    }

    public Element(int xCoordinate, int yCoordinate, int height, int wide, int speed, boolean isMoved, boolean isCanFly, Direct direct, boolean isLive) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.height = height;
        this.wide = wide;
        this.speed = speed;
        this.isMoved = isMoved;
        this.isCanFly = isCanFly;
        this.direct = direct;
        this.isLive = isLive;
    }

    public Element(ElementType elementType, int xCoordinate, int yCoordinate, int height, int wide, int step, int speed, boolean isMoved, Direct direct, boolean isLive, boolean isCanFly, int hardness, double blood, int power, boolean isShow) {
        this.elementType = elementType;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.height = height;
        this.wide = wide;
        this.step = step;
        this.speed = speed;
        this.isMoved = isMoved;
        this.direct = direct;
        this.isLive = isLive;
        this.isCanFly = isCanFly;
        this.hardness = hardness;
        this.blood = blood;
        this.power = power;
        this.isShow = isShow;
    }


    public void addOtherEleCanSee(Element e) {
        canSeeOtherElement.add(e);
    }

    public void removeOtherEleCanSee(Element e) {
        if (canSeeOtherElement.contains(e)) {
            canSeeOtherElement.remove(e);
        }
    }

    /**
     * 画出自己
     *
     * @param g
     */
    public void draw(Graphics g, Color color) {
        Coordinate coordinate = getCoordinate();
        g.setColor(color);
        g.fill3DRect(coordinate.getX1Coordinate()
                , coordinate.getY1Coordinate()
                , this.getWide(), this.getHeight(), true); // 画出所有元素
    }


    /**
     * 默认颜色
     *
     * @param g
     */
    public void draw(Graphics g) {
        draw(g, color);
    }

    /**
     * @param g
     * @param direct
     */
    public void drawBlood(Graphics g, Direct direct) {
        int r = (int) (getHeight() * 0.2);
        drawBlood(g, direct, r > 0 ? r : 1);
    }

   public int getRotationDegree(Direct direct){
       switch (direct) {
           case UP:
               return -90;
           case DOWN:
               return 90;
           case RIGHT:
               return 0;
           case LEFT:
               return 180;

       }
       return 0;
   }


    /**
     * @param g
     * @param direct 血条相对物体的方位
     * @param bloodR 血条宽度
     */
    public void drawBlood(Graphics g, Direct direct, int bloodR) {
        if (getFullBlood() != 0) {
            Graphics2D g2 = (Graphics2D) g;
            Coordinate coordinate = this.getCoordinate();
            //血条长度
            int bloodRect = Math.min(getHeight(), getWide());

            double bloodUnit = bloodRect / getFullBlood();//血条密度
            int bloodWidth = bloodR;
            int bloodHeight  =(int) (bloodUnit * getBlood());
            int bloodRectWidth =bloodWidth;
            int bloodRectHeight = bloodRect;
            int bloodX1= coordinate.getX1Coordinate() - bloodRectWidth;
            int bloodY1= coordinate.getY1Coordinate();

            Rectangle whiteBlodd = new Rectangle(bloodX1, bloodY1, bloodRectWidth, bloodRectHeight);
            Rectangle redBlodd = new Rectangle(bloodX1, bloodY1, bloodWidth, bloodHeight);
            Shape rotateWhiteBlodd = rotate(whiteBlodd, getRotationDegree(direct), this.getXCoordinate(), this.getYCoordinate());
            Shape rotateRedBlodd = rotate(redBlodd, getRotationDegree(direct), this.getXCoordinate(), this.getYCoordinate());
            g2.setColor(Color.WHITE);
            g2.fill(rotateWhiteBlodd);
            g2.setColor(Color.RED);
            g2.fill(rotateRedBlodd);

        }
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

    /**
     * 判断到达jframe哪个边界
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public Direct reachJFrameBorder(int x1, int y1, int x2, int y2) {
        Coordinate coordinate = getCoordinate();
        if (coordinate.getY1Coordinate() <= y1) {
            return Direct.UP;
        } else if (coordinate.getY2Coordinate() >= y2) {
            return Direct.DOWN;
        } else if (coordinate.getX1Coordinate() <= x1) {
            return Direct.LEFT;
        } else if (coordinate.getX2Coordinate() >= x2) {
            return Direct.RIGHT;
        } else {
            return null;
        }
    }

    /**
     * 判断到达地图哪个边界
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public Direct reachMapBorder(int x1, int y1, int x2, int y2) {
        Coordinate coordinate = getMapCoordinate();
        if (coordinate.getY1Coordinate() <= y1) {
            return Direct.UP;
        } else if (coordinate.getY2Coordinate() >= y2) {
            return Direct.DOWN;
        } else if (coordinate.getX1Coordinate() <= x1) {
            return Direct.LEFT;
        } else if (coordinate.getX2Coordinate() >= x2) {
            return Direct.RIGHT;
        } else {
            return null;
        }
    }

    public Direct getNegativeDirection(Direct direct) {
        Direct dir = null;
        switch (direct) {
            case UP:
                dir = Direct.DOWN;
                break;
            case DOWN:
                dir = Direct.UP;
                break;
            case LEFT:
                dir = Direct.RIGHT;
                break;
            case RIGHT:
                dir = Direct.LEFT;
                break;

        }
        return dir;
    }

    /**
     * 将方向设置为反向
     */
    private void setNegativeDirection() {
        setDirect(getNegativeDirection(getDirect()));
    }


    /**
     * 移动
     */
    public boolean move() {
        if (step != 0 && this.isMoved()) {
            switch (this.direct) {
                case UP:
                    this.setMapYCoordinate(this.getMapYCoordinate() - this.step);
                    distance += this.step;
                    return true;
                case DOWN:
                    this.setMapYCoordinate(this.getMapYCoordinate() + this.step);
                    distance += this.step;
                    return true;
                case LEFT:
                    this.setMapXCoordinate(this.getMapXCoordinate() - this.step);
                    distance += this.step;
                    return true;
                case RIGHT:
                    this.setMapXCoordinate(this.getMapXCoordinate() + this.step);
                    distance += this.step;
                    return true;
            }
        }
        return false;
    }

    /**
     * 转向时调换长宽
     */
    public void exchangeDirectXy() {
        int wide = this.getWide();
        int height = this.getHeight();
        this.setWide(height);
        this.setHeight(wide);
    }

    @Override
    public boolean bePassedThrough(Element other) {
        return false;
    }

    @Override
    public Set<Direct> collision(Element other) {
        Coordinate c = this.getMapCoordinate();
        Coordinate c2 = other.getMapCoordinate();
        Set<Direct> directSet = new HashSet<Direct>();


        if (this == other) {
            return directSet;
        }
        //比较元素是否有重合部分
        if (c.getX2Coordinate() >= c2.getX1Coordinate() && c2.getX2Coordinate() >= c.getX1Coordinate()) {

            if (c.getY2Coordinate() >= c2.getY1Coordinate() && c2.getY2Coordinate() >= c.getY1Coordinate()) {
                if (c.getY1Coordinate() >= c2.getY1Coordinate()) {
                    directSet.add(Direct.UP);
                }
                if (c.getY2Coordinate() <= c2.getY2Coordinate()) {
                    directSet.add(Direct.DOWN);
                }
                if (c.getX2Coordinate() <= c2.getX2Coordinate()) {
                    directSet.add(Direct.RIGHT);
                }
                if (c.getX1Coordinate() >= c2.getX1Coordinate()) {
                    directSet.add(Direct.LEFT);
                }

            }

        }
        return directSet;
    }

    @Override
    public void collisionResult() {


    }

    @Override
    public Vector<Element> boom() {

        return null;
    }

    /**
     * 死亡动作
     */
    public void beDead() {
        setLive(false);
        canSeeOtherElement.clear();
    }

    /**
     * 元素与地图某一点的距离
     *
     * @param
     * @param mapX
     * @param mapY
     * @return
     */
    public Distance FromMapPositionDistance(int mapX, int mapY) {
        return new Distance(this.getMapXCoordinate() - mapX, this.getMapYCoordinate() - mapY);
    }

    /**
     * 元素与地图某一元素的相对的距离
     *
     * @param
     * @param other
     * @return
     */
    public Distance FromOtherEleDistance(Element other) {
        return new Distance(other.getMapXCoordinate() - this.getMapXCoordinate(), other.getMapYCoordinate() - this.getMapYCoordinate());
    }

    @Override
    public void reachIn(Element other) {

    }

    @Override
    public void reachOut(Element other) {

    }

    /**
     * 到达边界后的行为
     */
    public void reachMapBorderAction() {
        //暂未定义
    }


    @Override
    public int hashCode() {
        return Objects.hash(uuid, xCoordinate, yCoordinate, mapXCoordinate, mapYCoordinate, height, wide, step, speed, isMoved, direct, isLive, isCanFly, hardness, fullBlood, blood, power, isShow, isCollisionDead, isNeedSystemControlMove, drawRank, isBuild, isCanSee, viewR);
    }

    @Override
    public int compareTo(Element o) {
        return this.getDrawRank() - o.getDrawRank();
    }


    public void setPlayId(int playId) {
        this.playId = playId;
        compareAndSetIsUpdate(this.playId,playId);
    }

    public void setIncreaseId(int increaseId) {
        this.increaseId = increaseId;
        compareAndSetIsUpdate(this.increaseId,increaseId);
    }

    public void setGameRole(GameRole gameRole) {
        this.gameRole = gameRole;
        compareAndSetIsUpdate(this.gameRole,gameRole);
    }

    public void setElementType(ElementType elementType) {
        this.elementType = elementType;
        compareAndSetIsUpdate(this.elementType,elementType);
    }

    public void setXCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
        compareAndSetIsUpdate(this.xCoordinate,xCoordinate);
    }

    public void setYCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
        compareAndSetIsUpdate(this.yCoordinate,yCoordinate);
    }

    public void setMapXCoordinate(int mapXCoordinate) {
        this.mapXCoordinate = mapXCoordinate;
        compareAndSetIsUpdate(this.mapXCoordinate,mapXCoordinate);
    }

    public void setMapYCoordinate(int mapYCoordinate) {
        this.mapYCoordinate = mapYCoordinate;
        compareAndSetIsUpdate(this.mapYCoordinate,mapYCoordinate);
    }

    public void setDirect(Direct direct) {
        this.direct = direct;
        compareAndSetIsUpdate(this.direct,direct);
    }

    public void setTheta(double theta) {
        this.theta = theta;
        compareAndSetIsUpdate(this.theta,theta);
    }

    public void setVectors(double[][] vectors) {
        this.vectors = vectors;
        compareAndSetIsUpdate(this.vectors,vectors);
    }

    public void setHeight(int height) {
        this.height = height;
        compareAndSetIsUpdate(this.height,height);
    }

    public void setWide(int wide) {
        this.wide = wide;
        compareAndSetIsUpdate(this.wide,wide);
    }

    public void setSpeed(int speed) {
        this.speed = speed;
        compareAndSetIsUpdate(this.speed,speed);
    }

    public void setMoved(boolean moved) {
        isMoved = moved;
        compareAndSetIsUpdate(this.isMoved,moved);
    }

    public void setLive(boolean live) {
        isLive = live;
        compareAndSetIsUpdate(this.isLive,live);
    }

    public void setCanFly(boolean canFly) {
        isCanFly = canFly;
        compareAndSetIsUpdate(this.isCanFly,canFly);
    }

    public void setHardness(int hardness) {
        this.hardness = hardness;
        compareAndSetIsUpdate(this.hardness,hardness);
    }

    public void setFullBlood(double fullBlood) {
        this.fullBlood = fullBlood;
        compareAndSetIsUpdate(this.fullBlood,fullBlood);
    }

    public void setPower(int power) {
        this.power = power;
        compareAndSetIsUpdate(this.power,power);
    }

    public void setShow(boolean show) {
        isShow = show;
        compareAndSetIsUpdate(this.isCollisionDead,isCollisionDead);
    }

    public void setCollisionDead(boolean collisionDead) {
        isCollisionDead = collisionDead;
        compareAndSetIsUpdate(this.isCollisionDead,isCollisionDead);
    }

    public void setNeedSystemControlMove(boolean needSystemControlMove) {
        isNeedSystemControlMove = needSystemControlMove;
    }

    public void setDrawRank(int drawRank) {
        this.drawRank = drawRank;
    }

    public void setBuild(boolean build) {
        isBuild = build;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setCanSee(boolean canSee) {
        isCanSee = canSee;
    }

    public void setViewR(int viewR) {
        this.viewR = viewR;
    }

    public void setChildElement(Vector<Element> childElement) {
        this.childElement = childElement;
        compareAndSetIsUpdate(this.childElement,childElement);
    }

    public void setCanSeeOtherElement(Set<Element> canSeeOtherElement) {
        this.canSeeOtherElement = canSeeOtherElement;
        compareAndSetIsUpdate(this.canSeeOtherElement,canSeeOtherElement);
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
        compareAndSetIsUpdate(this.uuid,uuid);
    }

    public void setDistance(int distance) {
        this.distance = distance;
        compareAndSetIsUpdate(this.distance,distance);
        setUpdate(true);
    }

    public void setCanBoom(boolean canBoom) {
        isCanBoom = canBoom;
        compareAndSetIsUpdate(this.isCanBoom,canBoom);
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public void setJoinMapQueue(Queue<Element> joinMapQueue) {
        this.joinMapQueue = joinMapQueue;
        compareAndSetIsUpdate(joinMapQueue,joinMapQueue);
    }

    public void compareAndSetIsUpdate(Object a,Object b){
        if(a!=b){
            setUpdate(true);
        }
    }
}
