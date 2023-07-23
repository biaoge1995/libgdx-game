package awt.utils;


import awt.model.domain.Distance;

public class MathUtils {

    /**
     * 计算两个点的直线距离
     *
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @return
     */
    public static double distance(double x, double y, double x1, double y1) {
        return Math.sqrt(Math.pow(y - y1, 2) + Math.pow(x - x1, 2));
    }

    /**
     * 返回两个坐标的atan2值
     *
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @return
     */
    public static double getAtan2(int x, int y, int x1, int y1) {
        //斜率对应的弧度
        double v = Math.atan2(y1 - y, x1 - x);
        double d = v * (180 / Math.PI);
        System.out.println(v);
        System.out.println(d);

        return v;
    }

    public static Distance getDistance(double atan2, int step) {

        double y = Math.sin(atan2) * step;

        double x = Math.cos(atan2) * step;

        Distance distance = new Distance((int) x, (int) y);
        return distance;
    }


    public static void main(String[] args) {


        double atan2 = getAtan2(0, 0, 1, 0);
        System.out.println(getDistance(atan2, 10));

    }
}
