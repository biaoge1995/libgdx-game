package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.model.Ball;
import com.mygdx.game.model.Paddle;


public class BodyShape extends ApplicationAdapter {
    World world;
    Box2DDebugRenderer box2DDebugRenderer;

    ShapeRenderer shapeRenderer;
    Body rectBody, circleBody, chainBody, edgeBody;

    OrthographicCamera camera;

    float scene_width = 100;
    float scene_height = 100;

    Ball ball;

    Paddle paddle;

    @Override
    public void create() {
        world = new World(new Vector2(0.0f, -9.8f), true);
        box2DDebugRenderer = new Box2DDebugRenderer();
        shapeRenderer = new ShapeRenderer();

        ball = new Ball(50, 50, 20, 0, 0);
        paddle = new Paddle(20, 5, 20, 5f);
        ball.createBody(world);
        paddle.createBody(world);
//        BodyDef bodyDef = new BodyDef();
//        bodyDef.type = BodyDef.BodyType.DynamicBody;
//        bodyDef.position.set(scene_width/2,6f);
//        bodyDef.linearVelocity.set(0,10);
//
//        Body body1 = world.createBody(bodyDef);
//
//        CircleShape circleShape = new CircleShape();
//        circleShape.setPosition(new Vector2(0f, 0f));
//        circleShape.setRadius(1);
//        Gdx.app.log("BodyShape", "Radius = " + circleShape.getRadius());
//
//        FixtureDef fixtureDef = new FixtureDef();
//        //形状
//        fixtureDef.shape = circleShape;
//        //密度
//        fixtureDef.density = 2;
//        //弹性
//        fixtureDef.restitution = 0.8f;
//        body1.createFixture(fixtureDef);
//        circleShape.dispose();


        createGround();
        camera = new OrthographicCamera(scene_width, scene_height);
        camera.position.set(scene_width / 2, scene_height / 2, 0);
        camera.update();


    }

    @Override
    public void render() {
        world.step(1 / 60f, 6, 2);


        Gdx.gl.glClearColor(0.39f, 0.58f, 0.92f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        paddle.update();
//
//
        box2DDebugRenderer.render(world, camera.combined);


//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        ball.draw(shapeRenderer);
//        paddle.draw(shapeRenderer);
//        shapeRenderer.end();

    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        box2DDebugRenderer.dispose();
    }

    private void createGround() {
        BodyDef goundBodyDef = new BodyDef();
        goundBodyDef.position.set(scene_width * 0.5f, 0.5f);

        Body groundBody = world.createBody(goundBodyDef);

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(scene_width * 0.5f, 0.5f);

        groundBody.createFixture(groundBox, 0);
        groundBox.dispose();
    }
}
