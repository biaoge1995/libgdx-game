package com.mygdx.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Paddle {
    int x;
    int y;
    float width;
    float height;

    Color color = Color.WHITE;


    private final BodyDef bodyDef;
    private final FixtureDef fixtureDef ;
    private final  PolygonShape groundBox;
    Body body;


    public Paddle(int x, int y,float width,float height) {
        this.x = x;
        this.y = y;
        bodyDef = new BodyDef();
        bodyDef.position.set(x,y);
//        bodyDef.type = BodyDef.BodyType.DynamicBody;
        groundBox = new PolygonShape();
        groundBox.setAsBox(width,height);

        Gdx.app.log("BodyShape", "PolygonShape = " + groundBox.getVertexCount());

        fixtureDef = new FixtureDef();
        //形状
        fixtureDef.shape = groundBox;
    }
    public void update() {
        int x = Gdx.input.getX();
        int y = Gdx.graphics.getHeight() - Gdx.input.getY();
        body.setTransform(x,y,0);
        Gdx.app.log("body", "x,y = " + "("+body.getPosition()+")");
//        Gdx.app.log("gl", "gl = ["+Gdx.graphics.getWidth()+","+Gdx.graphics.getHeight()+"]");




    }
    public void draw(ShapeRenderer shape) {
        shape.rect(body.getPosition().x,body.getPosition().y, width,height);
    }

    public Body createBody(World world){
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        groundBox.dispose();
        return body;
    }




}