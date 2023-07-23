package hhs.mygame.text1.games;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.math.MathUtils;

public class MyGame implements ApplicationListener {
	//渲染
	SpriteBatch batch;
	//Tiled地图
	OrthographicCamera cam;
	TiledMap map;
	OrthogonalTiledMapRenderer ren;
	float ppm = 50;
	//box2d相关
	World world;			//box2d世界
	Box2DDebugRenderer ren2;//debug渲染器(渲染出Body)
	Body body;				//角色的身体
	Texture text;				//角色的纹理
	float x=0,y=0;			//角色坐标
	
	float time= 0;			//记录游戏时间

	//ui与操控
	Stage st;					//定义一个舞台，存放ui
	Stage actors;				//定义一个舞台，存放角色
	ImageButton left,right;		//左右两边的按钮
	int speed = 5;			//定义角色速度
	boolean move = false,isleft = false;		//当任意一个按钮被点击时，move=true否则move=false
	//当左边按钮被点击时，isleft=true,当右边按钮被点击时，isleft=false

	@Override
	public void create() {
		//初始化debug渲染器
		ren2 = new Box2DDebugRenderer();

		batch = new SpriteBatch();
		//Tiled地图初始化
		map = new TmxMapLoader().load("1.tmx");
		//Tiled地图渲染器初始化
		ren = new OrthogonalTiledMapRenderer(map , 1 / ppm);
		//相机
		cam = new OrthographicCamera(Gdx.graphics.getWidth() / (ppm + 400), Gdx.graphics.getHeight() / (ppm + 400));
		//Box2d世界
		world = new World(new Vector2(0, -9.81f), true);

		//初始化Bodydef和FixtureDef
		BodyDef bdef = new BodyDef();
		//BodyDef的类型选择静态(Tiled地图中的碰撞区域不能移动，模拟墙壁和地面的效果)
		bdef.type = BodyDef.BodyType.StaticBody;
		FixtureDef fdef = new FixtureDef();

        //获取地图中所有层
		MapLayers layers = map.getLayers();
		//获取名称为'ground'的层(就是刚刚编辑的对象层)
		MapLayer rectLayer = layers.get("ground");
		//获取层内所有对象
		MapObjects objects = rectLayer.getObjects();
		//获取所有对象中的矩形对象
		Array<RectangleMapObject> rmos = objects.getByType(RectangleMapObject.class);
		//创建一个for循环，遍历层中的矩形对象
		for (RectangleMapObject rmo : rmos) {
			Rectangle rect = rmo.getRectangle();
			//定义一个多边形对象并初始化(矩形也是多边形)
			PolygonShape shape = new PolygonShape();
			//设置一个框，并将框的大小设置为矩形的大小
			//因为在Box2d中，PolygonShape的边框大小，是PolygonShape的中心点位置左右上下分别相加所以要除2
			//除以ppm的原因在第一期有说
			shape.setAsBox(rect.getWidth() / 2 / ppm, rect.getHeight() / 2 / ppm);
			//令FixtureDef的图形等于多边形对象
			fdef.shape = shape;
			//设定物体位置
			//因为Box2d中的位置是在物体中心，所以要在原坐标上各减去物体的长宽的一半(第一期视频有说)
			bdef.position.set((rect.getX() + rect.getWidth() / 2) / ppm, (rect.getY() + rect.getHeight() / 2) / ppm);
			//创建
			world.createBody(bdef).createFixture(fdef);

		}
		//BodyDef的类型选择动态的身体(角色可以移动)
		bdef.type = BodyDef.BodyType.DynamicBody;
		//设置生成位置
		bdef.position.set((16 + 8) / ppm, 4 * 16 / ppm);
		//初始化一个圆形对象
		CircleShape cshape = new CircleShape();
		//设置半径
		cshape.setRadius(8 / ppm);
		fdef.shape = cshape;
		//创建出来并保存到body变量中
		body = world.createBody(bdef);
		//绑定圆形对象(FixtureDef)
		body.createFixture(fdef);

		text = new Texture("gk.png");//初始化纹理

		//初始化舞台
		st = new Stage();
		actors = new Stage();
		//初始化左右按钮
		left = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("left.png"))), new TextureRegionDrawable(new TextureRegion(new Texture("right.png"))));
		right = new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("right.png"))), new TextureRegionDrawable(new TextureRegion(new Texture("left.png"))));
		//设置左右按钮位置
		left.setPosition(0, 0);
		right.setPosition(Gdx.graphics.getWidth() - right.getWidth(), 0);
		//添加按钮触摸监听
		left.addListener(new InputListener(){
				//当按钮被按下时，会执行这个函数
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					move = true;
					isleft = true;
					return true;
				}
				//当抬起手指时，会执行这个函数
				@Override
				public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
					move = false;
				}
			});
		right.addListener(new InputListener(){
				//当按钮被按下时，会执行这个函数
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					move = true;
					isleft = false;
					return true;
				}
				//当抬起手指时，会执行这个函数
				@Override
				public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
					move = false;
				}
			});

		st.addActor(left);
		st.addActor(right);

		Gdx.input.setInputProcessor(st);
	}

	@Override
	public void render() {
		world.step(Gdx.graphics.getDeltaTime(), 6, 2);

		float radius = body.getFixtureList().first().getShape().getRadius();
		x = body.getPosition().x - radius; //每一帧都获取一次角色的坐标
		y = body.getPosition().y - radius;

	    Gdx.gl.glClearColor(1, 1, 1, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//设置相机位置在角色中
		cam.position.x = body.getPosition().x;
		cam.position.y = body.getPosition().y;
		cam.update();
		
		time += Gdx.graphics.getDeltaTime();	//这个函数表示这一帧渲染的时长单位为秒
		if(time > 0.5f){	//每0.5秒创建一个刚体
			CircleShape shape = new CircleShape();
			shape.setRadius(8 / ppm);
			actors.addActor(new MyActor(world,shape,text,new Vector2(MathUtils.random(24,120)/ppm,128 / ppm)));
			time = 0;
		}

		//移动逻辑
		//当move=true时，也就是有一个按钮被按下
		if (move) {
			//如果isleft=true,也就是左边按钮被按下
			if (isleft) {
                //这个函数有两个参数
                //分别是，x方向速度，y方向速度
				//不改变刚体的y坐标速度否则刚体无法下落
                body.setLinearVelocity(-speed, body.getLinearVelocity().y);
			} else {
                body.setLinearVelocity(speed, body.getLinearVelocity().y);
			}
		} else {
			//如果没触碰任何按键接触，设置速度为零
			body.setLinearVelocity(0, body.getLinearVelocity().y);
		}

		//设置视角
		ren.setView(cam);
		//渲染
		ren.render();

		batch.setProjectionMatrix(cam.combined); //设置渲染相机
		batch.begin();
		batch.draw(text, x, y, radius * 2, radius * 2);//绘制
		batch.end();
		
		//设置场景相机相同
		actors.getViewport().setCamera(cam);
		actors.act();
		actors.draw();

		st.act();
		st.draw();
		

		//渲染box2d刚体边框
		ren2.render(world, cam.combined);

	}

	@Override
	public void dispose() {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

}
