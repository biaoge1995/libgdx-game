package com.mygdx.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Ball {
    int x;
    int y;
    int size;
    int xSpeed;
    int ySpeed;
    Color color = Color.WHITE;

    private final BodyDef bodyDef;
    private final FixtureDef fixtureDef ;
    private final CircleShape circleShape;

    Body body;

    public Ball(int x, int y, int size, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        bodyDef = new BodyDef();
        bodyDef.linearVelocity.set(xSpeed,ySpeed);
        bodyDef.position.set(x,y);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        circleShape = new CircleShape();
        circleShape.setPosition(new Vector2(0f, 0f));
        circleShape.setRadius(size);
        Gdx.app.log("BodyShape", "Radius = " + circleShape.getRadius());

        fixtureDef = new FixtureDef();
        //形状
        fixtureDef.shape = circleShape;
        //密度
        fixtureDef.density = 2;
        //弹性
        fixtureDef.restitution = 0.8f;

    }

    public void createBody(World world){
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        circleShape.dispose();
    }


    public void draw(ShapeRenderer shape) {
        shape.setColor(color);
        shape.circle(body.getPosition().x,body.getPosition().y, size);
    }


}