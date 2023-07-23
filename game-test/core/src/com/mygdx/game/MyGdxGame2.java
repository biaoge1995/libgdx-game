package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.model.Ball;
import com.mygdx.game.model.Paddle;

import java.util.ArrayList;
import java.util.Random;

public class MyGdxGame2 extends ApplicationAdapter {
    ShapeRenderer shape;
    Paddle paddle ;


    ArrayList<Ball> balls = new ArrayList<>();
    Random r = new Random();

    // SpriteBatch是libgdx提供的opengl封装，可以在其中执行一些常规的图像渲染，
    // 并且libgdx所提供的大多数图形功能也是围绕它建立的。
    SpriteBatch spriteBatch;

    // Pixmap是Libgdx提供的针对opengl像素操作的上级封装，它可以凭空构建一个像素贴图，
    // 但是它的现实必须通过Texture。
    Pixmap pixmap;


    ShaderProgram shader;
    // 准备Texture
    Texture texture;
    Texture texture2;
    Mesh mesh;

    // 文字贴图(默认不支持中文)。
    BitmapFont font;


    @Override
    public void create() {
        font = new BitmapFont(Gdx.files.internal("font/yahei.fnt"));
        spriteBatch = new SpriteBatch();
        shape = new ShapeRenderer();
        paddle = new Paddle(5,20,50,100);
        for (int i = 0; i < 1; i++) {
            balls.add(new Ball(r.nextInt(Gdx.graphics.getWidth()),
                    r.nextInt(Gdx.graphics.getHeight()),
                    r.nextInt(100), r.nextInt(15), r.nextInt(15)));
        }


        // 以下命令供GPU使用(不支持GLES2.0就不用跑了)
//        String vertexShader = "attribute vec4 a_position; \n"
//                + "attribute vec2 a_texCoord;   \n"
//                + "varying vec2 v_texCoord;     \n"
//                + "void main()                  \n"
//                + "{                            \n"
//                + "   gl_Position = a_position; \n"
//                + "   v_texCoord = a_texCoord;  \n"
//                + "}                            \n";
//        String fragmentShader = "#ifdef GL_ES\n"
//                + "precision mediump float;\n"
//                + "#endif\n"
//                + "varying vec2 v_texCoord;                            \n"
//                + "uniform sampler2D s_texture;                        \n"
//                + "uniform sampler2D s_texture2;                       \n"
//                + "void main()                                         \n"
//                + "{                                                   \n"
//                + "  gl_FragColor = texture2D( s_texture, v_texCoord ) * texture2D( s_texture2, v_texCoord);\n"
//                + "}                                                   \n";
//        // 构建ShaderProgram
//        shader = new ShaderProgram(vertexShader, fragmentShader);
//        // 构建网格对象
//        mesh = new Mesh(true, 4, 6, new VertexAttribute(VertexAttributes.Usage.Position, 2,
//                "a_position"), new VertexAttribute(
//                VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoord"));
//        float[] vertices = { -0.5f, 0.5f, 0.0f, 0.0f, -0.5f, -0.5f, 0.0f,
//                1.0f, 0.5f, -0.5f, 1.0f, 1.0f, 0.5f, 0.5f, 1.0f, 0.0f };
//        short[] indices = { 0, 1, 2, 0, 2, 3 };
//        // 注入定点坐标
//        mesh.setVertices(vertices);
//        mesh.setIndices(indices);
//        // 以Pixmap生成两个指定内容的Texture
//        Pixmap pixmap = new Pixmap(256, 256, Pixmap.Format.RGBA8888);
//        pixmap.setColor(1, 1, 1, 1);
//        pixmap.fill();
//        pixmap.setColor(0, 0, 0, 1);
//        pixmap.drawLine(0, 0, 256, 256);
//        pixmap.drawLine(256, 0, 0, 256);
//        texture = new Texture(pixmap);
//        pixmap.dispose();
//        pixmap = new Pixmap(256, 256, Pixmap.Format.RGBA8888);
//        pixmap.setColor(1, 1, 1, 1);
//        pixmap.fill();
//        pixmap.setColor(0, 0, 0, 1);
//        pixmap.drawLine(128, 0, 128, 256);
//        texture2 = new Texture(pixmap);
//        pixmap.dispose();


    }

    @Override
    public void render() {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        font.draw(spriteBatch, "FPS " + Gdx.graphics.getFramesPerSecond(), 5, Gdx.graphics.getHeight());
        font.draw(spriteBatch, "welcome my game", 150, Gdx.graphics.getHeight());
        spriteBatch.end();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        for (Ball ball : balls) {
//            ball.update();
//            ball.checkCollision(paddle);
            ball.draw(shape);
        }
        paddle.update();
        paddle.draw(shape);
        shape.end();

//        // PS:由于使用了ShaderProgram，因此必须配合gl20模式（否则缺少关键opengles接口）
//        Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics
//                .getHeight());
//        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE0);
//        texture.bind();
//        Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE1);
//        texture2.bind();
//        // 开始使用ShaderProgram渲染
//        shader.begin();
//        shader.setUniformi("s_texture", 0);
//        shader.setUniformi("s_texture2", 1);
//        mesh.render(shader, GL20.GL_TRIANGLES);
//        // 结束ShaderProgram渲染
//        shader.end();

    }

    @Override
    public void dispose() {
        shape.dispose();
        spriteBatch.dispose();
        font.dispose();

    }
}