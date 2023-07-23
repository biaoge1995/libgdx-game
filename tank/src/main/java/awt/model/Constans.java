package awt.model;

/**
 * @author chenbiao
 * @date 2020-12-20 02:24
 */
public class Constans {
    //系统刷新间隔时间（ms）
    public static final long time = 5;
    public static final long windosMoveTime = 5;
    public static final int windosMoveStep = 15;
    //tank大战时间单位10ms

    //tank长宽
    public static final int tankHeight=20;
    public static final int tankWidth=20;

    //其他元素 墙，水，草地
    public static final int otherHeight=25;
    public static final int otherWidth=25;

    private static final int initInterval=1;//加载地图元素时相邻元素间隔像素点

    //阵营名称
    public static final String SYSTEM_GROUP_NAME = "NPC";
    public static final String RED_GROUP_NAME = "RED";
    public static final String BLUE_GROUP_NAME = "BLUE";
}
