package hhs.mygame.text1.games;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.math.Vector2;

public class MyActor extends Actor {	//继承自Actor
	Body body;		//储存角色刚体
	TextureRegion texture;	//储存角色纹理
	float radius;			//储存角色半径
	//构造函数，参数分别为Box2d世界,角色纹理,角色位置
	public MyActor(World w, Shape shape, Texture text, Vector2 pos) {	//构造函数
		this.texture = new TextureRegion(text);

		radius = shape.getRadius();
		BodyDef bdef = new BodyDef();
		bdef.type = BodyDef.BodyType.DynamicBody;
		bdef.position.set(pos);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.restitution = 1;	//让刚体可以弹起来
		body = w.createBody(bdef);
		body.createFixture(fdef);	//创建刚体

		setWidth(radius * 2);
		setHeight(radius * 2);
	}
	@Override
	public void draw(Batch batch, float parentAlpha) {
		setPosition(body.getPosition().x - radius, body.getPosition().y - radius);
		batch.draw(texture,		//渲染纹理
				   getX(),
				   getY(),
				   getOriginX(),
				   getOriginY(),
				   getWidth(),
				   getHeight(),
				   getScaleX(),
				   getScaleY(),
				   getRotation());
	}
}
