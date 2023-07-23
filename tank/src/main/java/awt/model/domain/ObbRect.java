package awt.model.domain;

import lombok.Data;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.Arrays;

@Data
public class ObbRect {
    private int mapXCoordinate;
    private int mapYCoordinate;
    private int width;
    private int height;
    private double theta;
    private double[][] vectors = new double[2][2];

    public ObbRect(int mapXCoordinate, int mapYCoordinate, int width, int height, int theta) {
        super();
        this.mapXCoordinate = mapXCoordinate;
        this.mapYCoordinate = mapYCoordinate;
        this.width = width;
        this.height = height;
        this.theta = theta;


        resetVector();
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
    public boolean intersects(ObbRect other) {
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
        return (width * dot(vectors[0], vector) / 2
                + height * dot(vectors[1], vector) / 2);
    }
}
