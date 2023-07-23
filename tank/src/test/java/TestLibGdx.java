
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;

public class TestLibGdx implements ApplicationListener{
    @Override
    public void create() {

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void render() {
        /*
         * 设置清屏颜色为红色（RGBA）,
         *
         * LibGDX 中使用 4 个浮点类型变量（值范围 0.0 ~ 1.0）表示一个颜色（分别表示颜色的 RGBA 四个通道）,
         *
         * 十六进制颜色与浮点颜色之间的转换: 将十六进制颜色的每一个分量除以 255 得到的浮点数就是浮点颜色对应的通道值。
         */
        Gdx.gl.glClearColor(1, 0, 0, 1);

        // 清屏
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    public static void main(String[] args) {
        // 应用配置
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.width = 320;			// 指定窗口宽度
        config.height = 480;		// 指定窗口高度

        config.resizable = false;	// 窗口设置为大小不可改变

        // 创建游戏主程序启动入口类 MainGame 对象, 传入配置 config, 启动游戏程序
        new LwjglApplication(new TestLibGdx(), config);

    }
}
