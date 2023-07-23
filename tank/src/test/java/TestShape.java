import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class TestShape implements Shape {
    /**
     * 返回一个整数Rectangle完全包围Shape 。
     * @return
     */
    @Override
    public Rectangle getBounds() {
        return null;
    }

    /**
     * 返回 Shape高精度和更精确的边界框，而不是 getBounds方法。
     * @return
     */
    @Override
    public Rectangle2D getBounds2D() {
        return null;
    }

    /**
     * 测试指定的坐标是否在 Shape的边界内，如
     * @param x
     * @param y
     * @return
     */
    @Override
    public boolean contains(double x, double y) {
        return false;
    }

    /**
     * 测试指定Point2D是的边界内Shape
     * @param p
     * @return
     */

    @Override
    public boolean contains(Point2D p) {
        return false;
    }

    /**
     * 测试 Shape的内部 Shape完全包含指定的矩形区域。
     * @param x
     * @param y
     * @param w
     * @param h
     * @return
     */
    @Override
    public boolean intersects(double x, double y, double w, double h) {
        return false;
    }

    /**
     * 测试 Shape的内部 Shape完全包含指定的 Rectangle2D 。
     * @param r
     * @return
     */

    @Override
    public boolean intersects(Rectangle2D r) {
        return false;
    }

    /**
     *
     * 测试 Shape的内部 Shape完全包含指定的矩形区域。
     * @param x
     * @param y
     * @param w
     * @param h
     * @return
     */

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return false;
    }

    /**
     * 测试指定Point2D是的边界内Shape ，
     * @param r
     * @return
     */
    @Override
    public boolean contains(Rectangle2D r) {
        return false;
    }

    /**
     * 返回迭代器对象，该对象沿 Shape边界进行迭代，并提供对 Shape轮廓几何的访问
     * @param at
     * @return
     */
    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        return null;
    }

    /**
     *
     * 返回迭代器对象，该对象沿 Shape边界进行迭代，并提供对 Shape轮廓几何体的 Shape平视图的访问。
     * @param at
     * @param flatness
     * @return
     */
    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return null;
    }
}
