package quentin.jeu.mole.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.math.Interpolation.*;

import java.util.Locale;

import quentin.jeu.mole.MoleGame;
import quentin.jeu.mole.entities.Coin;
import quentin.jeu.mole.entities.Ground;
import quentin.jeu.mole.entities.Mole;
import quentin.jeu.mole.graphics.Background;
import quentin.jeu.mole.graphics.MyParticleEffect;
import quentin.jeu.mole.interpolate.Sine;
import quentin.jeu.mole.interpolate.SpriteAccessor;
import quentin.jeu.mole.interpolate.Tween;
import quentin.jeu.mole.interpolate.TweenManager;
import quentin.jeu.mole.ui.ProgressDisp;
import quentin.jeu.mole.ui.RadialSprite;
import quentin.jeu.mole.utils.Assets;
import quentin.jeu.mole.utils.MyContactlistener;
import quentin.jeu.mole.utils.MyInputProc;
import quentin.jeu.mole.utils.Save;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;


//resolution iphone 5 : 1136x640
//======================  960/540 ===============
////stage.getHeight()/8.4f=64
////stage.getHeight()/6.35f = 85
////stage.getHeight()/36 = 15 (pad)
////stage.getHeight()*0.28f = 151

/////camera 19.2 / 19.2*9/16 max zoom 2.4


public class Tuto1 implements Screen {
	
	private World world;
	private  final float pad=Gdx.graphics.getHeight()/36f;
	private int lvl, type, objectif, limit, limittype,score1,score2;
	private boolean stared1,stared2, stared3;
	private float staredtimer;

	public Mole mole;
	
	private MyContactlistener cl;
	private MyInputProc in;
	
	private Array<Coin> coins;
	
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	
	private TweenManager tweenManager;
	
	private InputMultiplexer multiplexer;
	public Image buttonmusic,buttonfx, buttonset;
	
	private SpriteBatch batch;
	
	private final  int numDrawX = 20;
	private final  float lvllenght = 300;
	private int leveloffset=0;
	private Background hillsf0bg, hills0bg, hills1bg, tree2bg, tree3bg, tree1bg, rockbg;
	private Background cloud1bg, cloud2bg, cloud3bg,tree0bg,  grass1bg,  grass2bg;
	private Background grass3bg, groundjointbg ,ground0bg ,ugroundjoint1bg, ground1bg;
	private Background ugroundjoint2bg ,ground2bg ,ugroundjoint3bg,ground3bg;
	private Background sky0bg, upskybg, hcloudbg;
	
	private Sprite rmvdgld, rmvdslv,rmvdbrz,rmvdt, rmvdjp,rmvddp, rmvdcoin;
	private MyParticleEffect windeffectback, windeffectffronf, windeffectback1,nebulaeffect1;
	
	private int staroffset1, staroffset2,staroffset3,staroffset4;
	private   Sprite backsky,rainbow, moon, mars,jupiter,saturne,uranus,neptune, nebuleuse;

	private final  int VELOCITYITERATIONS = 8, POSITIONITERATIONS = 3;
	public   enum State{Running, Paused}
	public   State state = State.Running;

	private ProgressDisp progress;
	
	private Group tuto;
	private boolean tutocheck1=false, tutocheck2=false, tutoover;
	private Label tuto4, tuto5, tuto6, c2c; 
	private Image tutobg;
	
	private Stage stage;
	private Skin skin;
	private Table gameovertable, pausetable;
	private Group mainui,powers,flipcom, holecom; // can't rotate a label
	private Label fliptxt, heightcom, landcom, holetxt, bonuscom;
	private Label completion, limitactor, top2line;
	private Image star1, star2, star3;
	private Label coinsbuymenu;

	private   boolean gameover = false;
	
	private Music music1, musicspace;
	private float timeScale=1;
	private float shakeTimer,gameOverTimer;
	private Image timetimer,timebutton,planetimer,planebutton,jptimer,jpbutton,black;
	private Label jpnumber;
	private Ground ground0b,ground1b,ground2b,ground3b,ground4b;
	private int coincolected=0;
	
	private  final int CONTINUEPRICE=18;
	private Sound click = Assets.manager.get(Assets.click, Sound.class);
	
	public Tuto1(int level, int type, int objectif, int limittype, int limit, int score1, int score2){
		this.lvl=level;
		this.type=type;
		this.objectif=objectif;
		this.limittype=limittype;
		this.limit=limit;
		this.score1=score1;
		this.score2=score2;
	}
	
	@Override
	public void show() {

		Save.load();
		Assets.load();
		state = State.Running; gameover=false;
		
		world = new World(new Vector2(0, -9.81f), true);
		debugRenderer = new Box2DDebugRenderer();
		batch = new SpriteBatch();
		
		float ratio= (float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth();
		camera = new OrthographicCamera(19f, ratio*19f);
		
		loadSkin();
		
		// creating animations
		tweenManager = new TweenManager();
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());

		
		int w=38; //System.out.println(Gdx.graphics.getWidth()/25);
		int h=10; //System.out.println(Gdx.graphics.getHeight()/50);
		
		// CREATE BACKGROUNDS
		rmvdgld= new Sprite(Assets.manager.get(Assets.orbgold,Texture.class));
		rmvdgld.setSize(1f, 1f);
		rmvdgld.setPosition(10000, 100000);
		rmvdslv= new Sprite(Assets.manager.get(Assets.orbsilver,Texture.class));
		rmvdslv.setSize(1f, 1f);
		rmvdslv.setPosition(10000, 100000);
		rmvdbrz= new Sprite(Assets.manager.get(Assets.orbbronze,Texture.class));
		rmvdbrz.setSize(1f, 1f);
		rmvdbrz.setPosition(10000, 100000);
		rmvdjp= new Sprite(Assets.manager.get(Assets.jetpack,Texture.class));
		rmvdjp.setSize(1f, 1f);
		rmvdjp.setPosition(10000, 100000);
		rmvddp= new Sprite(Assets.manager.get(Assets.plane,Texture.class));
		rmvddp.setSize(1f, 1f);
		rmvddp.setPosition(10000, 100000);
		rmvdt= new Sprite(Assets.manager.get(Assets.time,Texture.class));
		rmvdt.setSize(1f, 1f);
		rmvdt.setPosition(10000, 100000);
		
		rmvdcoin= new Sprite(Assets.manager.get(Assets.orbgold,Texture.class));
		rmvdcoin.setSize(0.5f, 0.5f);
		rmvdcoin.setPosition(10000, 100000);
		
		backsky =   new Sprite(Assets.manager.get(Assets.backsky,   Texture.class));
		backsky.setSize(2*(lvllenght), 40);
		//space objects
		rainbow =   new Sprite(Assets.manager.get(Assets.rainbow,   Texture.class));
		rainbow  .setSize(38, 18);
		moon    =   new Sprite(Assets.manager.get(Assets.moon,      Texture.class));
		moon     .setSize(10, 10);
		mars    =   new Sprite(Assets.manager.get(Assets.mars,      Texture.class));
		mars     .setSize(10, 10);
		jupiter =   new Sprite(Assets.manager.get(Assets.jupiter,   Texture.class));
		jupiter  .setSize(20, 20);
		saturne =   new Sprite(Assets.manager.get(Assets.saturne,   Texture.class));
		saturne.rotate(25);
		saturne  .setSize(25, 10);
		uranus  =   new Sprite(Assets.manager.get(Assets.uranus,    Texture.class));
		uranus   .setSize(10, 10);
		neptune =   new Sprite(Assets.manager.get(Assets.neptune,   Texture.class));
		neptune  .setSize(10, 10);
		nebuleuse = new Sprite(Assets.manager.get(Assets.nebula, Texture.class));
		nebuleuse.setSize(55, 35);
		
		
//		final Sprite star1    =   new Sprite(Assets.manager.get(Assets.stars1,   Texture.class));
//		final Sprite star2    =   new Sprite(Assets.manager.get(Assets.stars2,   Texture.class));
//		final Sprite star3    =   new Sprite(Assets.manager.get(Assets.stars1,   Texture.class));
//		
//		stars1 = new RandomBg(star1, 10, 4,  60,  211,    1f,     0, camera, 10);
//		stars2 = new RandomBg(star2, 10, 5, 210, 1000,    1f,     0, camera, 10);
//		stars3 = new RandomBg(star3,  7, 4, 150, 1000, 1.05f,-0.05f, camera, 40);
			
//		final Sprite asteroidb = new Sprite(Assets.manager.get(Assets.asterback,  Texture.class));
//		final Sprite asteroid1 = new Sprite(Assets.manager.get(Assets.asteroid2, Texture.class));
//		final Sprite asteroid2 = new Sprite(Assets.manager.get(Assets.asteroid1, Texture.class));
//		
//		asteroidbbg = new RandomBg(asteroidb, 10, 2, 150, 180, 0.5f , 0.01f, camera, 40);
//		asteroid1bg = new RandomBg(asteroid1, 5, 3, 150, 180, 0.25f, 0.02f, camera, 80);
//		asteroid2bg = new RandomBg(asteroid2, 3, 3, 149, 181, 0.25f, 0.03f, camera, 140);
		
		
		//static bgs
		float[] color = new float[]{0.9f, 0.6f, 0.3f};
		final Sprite grass1       = new Sprite(Assets.manager.get(Assets.grass1,  Texture.class));
		final Sprite grass2       = new Sprite(Assets.manager.get(Assets.grass2,  Texture.class));
		final Sprite grass3       = new Sprite(Assets.manager.get(Assets.grass3,  Texture.class));
		final Sprite sky0        = new Sprite(Assets.manager.get(Assets.sky0,   Texture.class));
		final Sprite upsky       = new Sprite(Assets.manager.get(Assets.upsky,  Texture.class));
		final Sprite ground0     = new Sprite(Assets.manager.get(Assets.ground0,Texture.class));
		ground0.setColor(color[0],color[1],color[2],1);
		final Sprite ground1     = new Sprite(Assets.manager.get(Assets.ground1,Texture.class));
		final Sprite ground2     = new Sprite(Assets.manager.get(Assets.ground2,Texture.class));
		final Sprite ground3     = new Sprite(Assets.manager.get(Assets.ground3,Texture.class));
		final Sprite hcloud      = new Sprite(Assets.manager.get(Assets.hcloud, Texture.class));
		final Sprite groundjoint   = new Sprite(Assets.manager.get(Assets.ugroundjoint0,  Texture.class));
		groundjoint.setColor(color[0],color[1],color[2],1);
		final Sprite ugroundjoint1 = new Sprite(Assets.manager.get(Assets.ugroundjoint1,Texture.class));
		final Sprite ugroundjoint2 = new Sprite(Assets.manager.get(Assets.ugroundjoint2,Texture.class));
		final Sprite ugroundjoint3 = new Sprite(Assets.manager.get(Assets.ugroundjoint3,Texture.class));
		
		grass1       .setSize(15*1.15f, 1.5f*1.15f);
		grass2       .setSize(15*1.15f, 1.5f*1.15f);
		grass3       .setSize(15*1.15f, 1.5f*1.15f);
		groundjoint  .setSize(10, 2);
		ground0      .setSize(10, 10);
		ugroundjoint1.setSize(10, 2);
		ground1      .setSize(10, 10);
		ugroundjoint2.setSize(10, 2);
		ground2      .setSize(10, 10);
		ugroundjoint3.setSize(10, 2);
		ground3      .setSize(10, 10);
		ground0      .setSize(10, 10);
		sky0         .setSize(10, 10);
		upsky        .setSize(30, 30);
		hcloud       .setSize(w, 1.8f*h);
		
		grass1bg =        new Background(tweenManager,grass1,        Background.WIND3, camera,      0f,1);
		grass2bg =        new Background(tweenManager,grass2,        Background.WIND3, camera,      0f,1);
		grass3bg =        new Background(tweenManager,grass3,        Background.WIND3, camera,      0f,1);
		groundjointbg =   new Background(tweenManager,groundjoint,  Background.NONE,  camera,   -0.5f,1);
		ground0bg =       new Background(tweenManager,ground0,      Background.NONE,  camera,     -1f,1);
		ugroundjoint1bg = new Background(tweenManager,ugroundjoint1,Background.NONE,  camera, -5-0.6f,1);
		ground1bg =       new Background(tweenManager,ground1,      Background.NONE,  camera,     -2f,1);
		ugroundjoint2bg = new Background(tweenManager,ugroundjoint2,Background.NONE,  camera,-10-0.5f,1);
		ground2bg =       new Background(tweenManager,ground2,      Background.NONE,  camera,     -3f,1);
		ugroundjoint3bg = new Background(tweenManager,ugroundjoint3,Background.NONE,  camera,-15-0.5f,1);
		ground3bg =       new Background(tweenManager,ground3,      Background.NONE,  camera,     -4f,1);
		sky0bg =          new Background(tweenManager,sky0,         Background.NONE,  camera,      0f,1);
		upskybg =         new Background(tweenManager,upsky,        Background.NONE,  camera,  1+1/3f,1);
		hcloudbg =        new Background(tweenManager,hcloud,       Background.NONE,  camera, 2-0.5f,1);
		
		
		
		//paralax bgs
		
		final Sprite hills0 =  new Sprite(Assets.manager.get(Assets.hills0, Texture.class));
		//hills0.getTexture().setFilter( TextureFilter.Linear, TextureFilter.Linear);
		final Sprite hills1 =  new Sprite(Assets.manager.get(Assets.hills1, Texture.class));
		final Sprite fhills0 = new Sprite(Assets.manager.get(Assets.fhills0,Texture.class));
		final Sprite tree0 =   new Sprite(Assets.manager.get(Assets.tree0,  Texture.class));
		final Sprite tree1 =   new Sprite(Assets.manager.get(Assets.tree1,  Texture.class));
		final Sprite tree2 =   new Sprite(Assets.manager.get(Assets.tree1,  Texture.class));
		final Sprite tree3 =   new Sprite(Assets.manager.get(Assets.tree0 , Texture.class));
		final Sprite rock =    new Sprite(Assets.manager.get(Assets.rock,   Texture.class));
		final Sprite cloud =   new Sprite(Assets.manager.get(Assets.cloud,  Texture.class));
		hills0   .setSize(w/1.5f*1.6f, h*1.6f);
		hills1   .setSize(w/1.5f*1.6f, h*1.6f);
		fhills0  .setSize(w/2*1.3f, 1.5f*1.3f);
		tree0.setSize(w/4*1.5f, w/4*1.5f);
		tree1.setSize(w/5*1.5f, w/5*1.5f);
		tree2.setSize(w/10*1.5f,w/10*1.5f);
		tree3.setSize(w/14*1.5f,w/14*1.5f);
		rock  .setSize(w/7*1.2f, h/5*1.2f);
		cloud .setSize(w/3*1.5f, h/2*1.5f);
		
		
		rockbg =    new Background(tweenManager,rock,   Background.NONE,  camera, 0.1f, -0.1f ,8);
		tree1bg =   new Background(tweenManager,tree0,  Background.WIND1, camera, 0.2f, -0.1f ,3);
		tree0bg =   new Background(tweenManager,tree1,  Background.WIND1, camera, 0.3f, -0.1f ,2);
		hillsf0bg = new Background(tweenManager,fhills0,Background.WIND2, camera, 0.6f, -0.25f,1);
		tree2bg =   new Background(tweenManager,tree2,  Background.WIND1, camera, 0.6f,  0.0f ,3);
		tree3bg =   new Background(tweenManager,tree3,  Background.WIND1, camera, 0.7f,  0.0f ,4);
		hills0bg =  new Background(tweenManager,hills0, Background.NONE,  camera, 0.8f, -0.15f,2);
		hills1bg =  new Background(tweenManager,hills1, Background.NONE,  camera, 0.8f, -0.15f,2,0.995f);
		cloud1bg =  new Background(tweenManager,cloud,  Background.NONE,  camera, 1.5f,  0.5f ,4);
		cloud2bg =  new Background(tweenManager,cloud,  Background.NONE,  camera, 1.3f,  1.0f ,2);
		cloud3bg =  new Background(tweenManager,cloud,  Background.NONE,  camera, 1.1f,  1.8f ,4);
		
		//wind effect
		windeffectback1 = new MyParticleEffect();
		windeffectback1.loadEmitters(Gdx.files.internal("effect/wind.p"), false); 
		Texture saku = Assets.manager.get(Assets.saku,Texture.class);
		windeffectback1.getEmitters().get(0).setcamera(camera);
		windeffectback1.loadEmitterImages(saku);////////////!!!!!!
		windeffectback1.scaleEffect(windeffectback1, 0, 0.45f);
		windeffectback1.ScaleParticles(windeffectback1, 0, 0.65f);
		windeffectback1.start();
		
		windeffectback = new MyParticleEffect();
		windeffectback.loadEmitters(Gdx.files.internal("effect/wind.p"), false); 
		Texture leaf = Assets.manager.get(Assets.leaf,Texture.class);
		windeffectback.getEmitters().get(0).setcamera(camera);
		windeffectback.loadEmitterImages(leaf);////////////!!!!!!
		windeffectback.scaleEffect(windeffectback, 0, 0.45f);
		windeffectback.ScaleParticles(windeffectback, 0, 0.65f);
		windeffectback.start();
		
		
		windeffectffronf = new MyParticleEffect();
		windeffectffronf.loadEmitters(Gdx.files.internal("effect/windf.p"), false); 
		windeffectffronf.getEmitters().get(0).setcamera(camera);
		windeffectffronf.loadEmitterImages(leaf);////////////!!!!!!
		windeffectffronf.scaleEffect(windeffectffronf, 0, 1.5f);
		windeffectffronf.ScaleParticles(windeffectffronf, 0, 0.8f);
		windeffectffronf.start();
		
		//nebula effect
		nebulaeffect1 = new MyParticleEffect();
		nebulaeffect1.loadEmitters(Gdx.files.internal("effect/nebula.p"), true); 
		nebulaeffect1.loadEmitterImages(Assets.manager.get(Assets.nebul, Texture.class));
		nebulaeffect1.getEmitters().get(0).autotrans=true;
		nebulaeffect1.getEmitters().get(1).autotrans=true;
		nebulaeffect1.getEmitters().get(2).autotrans=true;
		nebulaeffect1.scaleEffect(nebulaeffect1, 0, 0.2f);
		nebulaeffect1.scaleEffect(nebulaeffect1, 1, 0.2f);
		nebulaeffect1.scaleEffect(nebulaeffect1, 2, 0.2f);
		
		
		//other objects
		//ufos      = Ufo.createufo(world);		
					
		//GROUNDS
		final Vector2[] groundchain = new Vector2[]{new Vector2(-300,0), new Vector2(300,0)};
		
		//Create mole and set contact listener to it
		cl = new MyContactlistener(lvl);
		in = new MyInputProc();
		float [] color1 = new float[]{1f, 1f,1f};
		mole = new Mole(world,in, cl, color, color1, lvl);
		world.setContactListener(cl);
		
		//ground
		ground0b = new Ground(world, "ground", new Vector2(0,0), groundchain);
        //Underground
		ground1b = new Ground(world, "underground" , new Vector2(0, -10), groundchain);
		ground2b = new Ground(world, "underground1", new Vector2(0, -20), groundchain);
		ground3b = new Ground(world, "underground2", new Vector2(0, -30), groundchain);
		ground4b = new Ground(world, "underground3", new Vector2(0, -40), groundchain);
		
		
		
		/////////////////// MUSIC ////////////////////////////////
		
		music1 = Assets.manager.get(Assets.music,Music.class);
		music1.setLooping(true);
		music1.setVolume(0.00f);

		musicspace = Assets.manager.get(Assets.musicspace,Music.class);
		musicspace.setLooping(true);
		musicspace.setVolume(0.0f);
		
		if(Save.gd.music){
			music1.play();
			musicspace.play();
		}
       ///////////////////////////////////USER TweenFACE///////////////////////////////////////
		createUI();
		
		/////////////////////////////Input processor////////////////////////////// 
		
		multiplexer = new InputMultiplexer();
		
		
		InputAdapter zoom = new InputAdapter() {
			@Override
			public boolean scrolled(int amount) {
				camera.zoom += amount / 25f;
				System.out.println("zoom : " +Float.toString(camera.zoom));
				return true;
			}
		};
		multiplexer.addProcessor(zoom);
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(in);
		Gdx.input.setInputProcessor(multiplexer);	
		
		
  }
	
	private void createUI() {
		stage=new Stage();
		Save.gd.accelerometer=false;
		//////////////////////////////////MAIN TABLE///////////////////////
    	mainui = new Table(skin);
    	
    	// creating score displayer
    	top2line = new Label(MoleGame.myBundle.get("best") +Integer.toString(Save.gd.scores[lvl]), skin,"default");
    	top2line.setFontScale(0.75f);
    	completion = new Label("Score : 1000000", skin,"default");
    	progress   = new ProgressDisp(mole, lvl, type, objectif,limittype,limit);
    	
    	// creating limit displayer
    	limitactor = new Label("", skin,"default");
    	
    	//comments displayer
    	flipcom = new Group();
    	fliptxt = new Label("", skin, "default");
    	fliptxt.setAlignment(Align.center);
    	flipcom.setPosition(Gdx.graphics.getWidth()*1/4, Gdx.graphics.getHeight()*0.66f);
    	flipcom.addActor(fliptxt);
    	
    	heightcom = new Label("", skin, "default");
    	heightcom.setAlignment(Align.center);
    	heightcom.setPosition(Gdx.graphics.getWidth()*3/4, Gdx.graphics.getHeight()*0.66f);
    	
    	landcom = new Label("", skin, "default");
    	landcom.setAlignment(Align.center);
    	landcom.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()*0.16f);
    	
    	bonuscom = new Label("", skin, "default");
    	bonuscom.setAlignment(Align.center);
    	bonuscom.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()*0.16f);
    	
    	holecom = new Group();
    	holetxt = new Label("", skin, "default");
    	holetxt.setAlignment(Align.center);
    	holecom.setPosition(Gdx.graphics.getWidth()*3/4, Gdx.graphics.getHeight()*0.16f);
    	holecom.addActor(holetxt);
  
    	//=========== creating pause button===============//
    	Image buttonPause = new Image(Assets.manager.get(Assets.pause, Texture.class));
		buttonPause.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				pause();
			}
		});
		
		
		//=========================create power button=================
		powers=new Group();
		RadialSprite rprog1 = new RadialSprite(new TextureRegion(Assets.manager.get(Assets.timer, Texture.class)));
		timetimer= new Image(rprog1);
		timetimer.setSize(stage.getHeight()/8.4f, -stage.getHeight()/8.4f);
		timetimer.setPosition(pad, stage.getHeight()/2+timetimer.getWidth()/2);
		timetimer.setTouchable(Touchable.disabled);
		timebutton = new Image(Assets.manager.get(Assets.time, Texture.class));
		timebutton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(!mole.timeslow && mole.timetimer>0){
					mole.timeslow=true;
				}
				else mole.timeslow=false;
			}
		});
		timebutton.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
		timebutton.setPosition(pad, stage.getHeight()/2-timebutton.getWidth()/2);
		powers.addActor(timebutton);
		powers.addActor(timetimer);
		if(lvl==8)Save.gd.timebought=10;
		if(Save.gd.timebought==0 || type==MoleGame.ARCADELVL){
			timetimer.getColor().a=0;
			timetimer.setVisible(false);
			timebutton.getColor().a=0;
			timebutton.setVisible(false);
		}
		
		RadialSprite rprog2 = new RadialSprite(new TextureRegion(Assets.manager.get(Assets.timer, Texture.class)));
		planetimer= new Image(rprog2);
		planetimer.setSize(stage.getHeight()/8.4f, -stage.getHeight()/8.4f);
		planetimer.setPosition(pad, stage.getHeight()/2+planetimer.getWidth()/2-(stage.getWidth()/13f+pad));
		planetimer.setTouchable(Touchable.disabled);
		planebutton = new Image(Assets.manager.get(Assets.plane, Texture.class));
		planebutton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(!mole.plane && mole.planetimer>0){
					mole.plane=true;
				}
				else mole.plane=false;
			}
		});
		planebutton.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
		planebutton.setPosition(pad, stage.getHeight()/2-planebutton.getWidth()/2-(stage.getWidth()/13f+pad));
		powers.addActor(planebutton);
		powers.addActor(planetimer);
		if(Save.gd.planebought==0 || type==MoleGame.ARCADELVL){
			planetimer.getColor().a=0;
			planetimer.setVisible(false);
			planebutton.getColor().a=0;
			planebutton.setVisible(false);
		}
		
		RadialSprite rprog3 = new RadialSprite(new TextureRegion(Assets.manager.get(Assets.timer, Texture.class)));
		jptimer= new Image(rprog3);
		jptimer.setSize(stage.getHeight()/8.4f, -stage.getHeight()/8.4f);
		jptimer.setPosition(pad, stage.getHeight()/2+jptimer.getWidth()/2+stage.getWidth()/13f+pad);
		jptimer.setTouchable(Touchable.disabled);
		jpnumber =new Label(Integer.toString(mole.jpnumber), skin);
		jpbutton = new Image(Assets.manager.get(Assets.jetpack, Texture.class));
		jpbutton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(!mole.jetpack && mole.jpnumber>0 && !mole.jetpacked){
					mole.jetpack=true;
					mole.jpnumber--;
					jpnumber.setText(Integer.toString(mole.jpnumber));
				}
			}
		});
		jpbutton.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
		jpbutton.setPosition(pad, stage.getHeight()/2-jpbutton.getWidth()/2+stage.getWidth()/13f+pad);
		jpnumber.setTouchable(Touchable.disabled);
		jpnumber.setAlignment(Align.center);
		jpnumber.setPosition(jpbutton.getX()+jpbutton.getWidth()/2-jpnumber.getWidth()/2, jpbutton.getY()+jpbutton.getWidth()/2-jpnumber.getHeight()/2);
		jpnumber.getColor().a=0.7f;
		if(Save.gd.jpbought==0 || type==MoleGame.ARCADELVL){
			jptimer .getColor().a=0;
			jptimer .setVisible(false);
			jpbutton.getColor().a=0;
			jpbutton.setVisible(false);
			jpnumber.getColor().a=0;
			jpnumber.setVisible(false);
		}
		powers.addActor(jpbutton);
		powers.addActor(jptimer);
		powers.addActor(jpnumber);
		
		stage.addActor(powers);
		
		///// Layout 
		completion.setPosition(pad, stage.getHeight()-completion.getHeight()-pad/2);
		top2line  .setPosition(pad, completion.getY()-top2line.getHeight()-pad/5);
		buttonPause.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
		buttonPause.setPosition(stage.getWidth()-pad-buttonPause.getWidth(), stage.getHeight()-buttonPause.getHeight()-pad);
		limitactor.setPosition(stage.getWidth()/2-limitactor.getWidth()/2, 2*pad+limitactor.getHeight()/2, Align.center);
		
		star1=new Image(Assets.manager.get(Assets.stuns, Texture.class));
		star2=new Image(Assets.manager.get(Assets.stuns, Texture.class));
		star3=new Image(Assets.manager.get(Assets.stuns, Texture.class));
		star1.getColor().a=0;
		star2.getColor().a=0;
		star3.getColor().a=0;
		star1.setSize(stage.getHeight()/16f, stage.getHeight()/16f);
		star2.setSize(stage.getHeight()/16f, stage.getHeight()/16f);
		star3.setSize(stage.getHeight()/16f, stage.getHeight()/16f);
		star1.setPosition(pad+completion.getWidth()+0.0f*pad+0*star2.getWidth(), stage.getHeight()+star1.getHeight()-completion.getHeight()-pad/2);
		star2.setPosition(pad+completion.getWidth()+0.5f*pad+1*star2.getWidth(), stage.getHeight()+star1.getHeight()-completion.getHeight()-pad/2);
		star3.setPosition(pad+completion.getWidth()+1.0f*pad+2*star2.getWidth(), stage.getHeight()+star1.getHeight()-completion.getHeight()-pad/2);
		
		mainui.addActor(completion);
		mainui.addActor(star1);
		mainui.addActor(star2);
		mainui.addActor(star3);
		mainui.addActor(buttonPause);
		mainui.addActor(top2line);
		mainui.addActor(limitactor);
		
	  	
		//////////////////////////////////////////////PAUSE TABLE    ///////////////////////////////////////
		
    	Image pause =new Image(Assets.manager.get(Assets.pause, Texture.class));
    	pause.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				state = State.Running;
				buttonset.addAction(sequence(alpha(0,0.5f),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
				pausetable.addAction(sequence(alpha(0,0.5f),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
			}
		});
    	Image buttonresume = new Image(Assets.manager.get(Assets.play, Texture.class));
		buttonresume.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				state = State.Running;
				buttonset.addAction(sequence(alpha(0,0.5f),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
				pausetable.addAction(sequence(alpha(0,0.5f),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
			}
		});
		Image buttonraz = new Image(Assets.manager.get(Assets.raz, Texture.class));
		buttonraz.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				black.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),
						fadeIn(0.5f),run(new Runnable() {
							@Override
							public void run() {
		((Game) Gdx.app.getApplicationListener()).setScreen(new Tuto1(lvl,type,objectif,limittype,limit, score1, score2));
					}
				})));
				
			}
		});
		Image buttonmenu = new Image(Assets.manager.get(Assets.menu, Texture.class));
		buttonmenu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				black.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),
						fadeIn(0.5f),run(new Runnable() {
							@Override
							public void run() {
		((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
					}
				})));
			}
		});
    	pausetable = new Table(skin);
		pausetable.setFillParent(true);
		pausetable.add(pause).size(stage.getHeight()*0.28f).colspan(3).row();
		pausetable.defaults().uniform().fill();
		pausetable.defaults().center().padTop(pad);
		pausetable.add(buttonresume).size(stage.getHeight()/6.35f).pad(pad);
		pausetable.add(buttonraz)   .size(stage.getHeight()/6.35f).pad(pad);
		pausetable.add(buttonmenu)  .size(stage.getHeight()/6.35f).pad(pad).row();
    	pausetable.getColor().a=0;
    	pausetable.setVisible(false);
    	
    	
		
		//////////////////////////////////////////////   GAME OVER TABLE    ///////////////////////////////////////
		gameovertable = new Table(skin);
    	gameovertable.setFillParent(true);
    	
		stage.addActor(flipcom);
		stage.addActor(heightcom);
		stage.addActor(bonuscom);
		stage.addActor(landcom);
		stage.addActor(holecom);
		stage.addActor(mainui);
		mainui.setVisible(false);
		stage.addActor(pausetable);
		createsetb();
		createtuto();
		black=new Image(skin.newDrawable("white", Color.BLACK));
		black.setSize(stage.getWidth(), stage.getHeight());
		black.addAction(sequence(delay(0.5f),alpha(0,1,fade), com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
		stage.addActor(black);
		
	}

	private void loadSkin () {
		skin = new Skin();
		if(Gdx.graphics.getHeight()<1000){
			skin.add("big", new BitmapFont(Gdx.files.internal("font/font64.fnt")));
			skin.add("default", new BitmapFont(Gdx.files.internal("font/font32.fnt")));
			skin.add("small"  , new BitmapFont(Gdx.files.internal("font/font32.fnt")));
		}
		else{
			skin.add("big", new BitmapFont(Gdx.files.internal("font/font128.fnt")));
			skin.add("default", new BitmapFont(Gdx.files.internal("font/font64.fnt")));
			skin.add("small"  , new BitmapFont(Gdx.files.internal("font/font32.fnt")));
		}
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));

		ButtonStyle buttonstyle = new ButtonStyle();
		buttonstyle.up   = skin.newDrawable("white", Color.BLACK);
		buttonstyle.down = skin.newDrawable("white", new Color(0x416ba1ff));
		buttonstyle.over = skin.newDrawable("white", Color.DARK_GRAY);
		skin.add("default", buttonstyle);
		
		buttonstyle = new ButtonStyle(buttonstyle);
		buttonstyle.checked = skin.newDrawable("white", new Color(0x5287ccff));
		skin.add("toggle", buttonstyle);
		
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up   = skin.newDrawable("white", Color.BLACK);
		textButtonStyle.down = skin.newDrawable("white", new Color(0x416ba1ff));
		textButtonStyle.over = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.font = skin.getFont("default");
		skin.add("default", textButtonStyle);

		textButtonStyle = new TextButtonStyle(textButtonStyle);
		textButtonStyle.checked = skin.newDrawable("white", new Color(0x5287ccff));
		skin.add("toggle", textButtonStyle);

		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = skin.getFont("small");
		skin.add("small", labelStyle);
		labelStyle = new LabelStyle();
		labelStyle.font = skin.getFont("default");
		skin.add("default", labelStyle);
		labelStyle = new LabelStyle();
		labelStyle.font = skin.getFont("big");
		skin.add("big", labelStyle);
	}
	
	private void createtuto(){
		tuto=new Group();
		
		c2c = new Label(MoleGame.myBundle.get("c2c") , skin);
        c2c.setPosition(stage.getWidth()/2-c2c.getWidth()/2, stage.getHeight()*1/4f-c2c.getHeight()/2);
		c2c.setTouchable(Touchable.disabled);
		c2c.addAction(forever(sequence(alpha(0.8f,1,sine),alpha(0.2f,1,sine))));
		c2c.setVisible(false);
		final Label tuto1 =new Label(MoleGame.myBundle.get("tuto14") , skin);
		final Label tuto2 =new Label(MoleGame.myBundle.get("tuto15") , skin);
		final Label tuto3 =new Label(MoleGame.myBundle.get("tuto16") , skin);
					tuto4 =new Label(MoleGame.myBundle.get("tuto17") , skin);
					tuto5 =new Label(MoleGame.myBundle.get("tuto18") , skin);
					tuto6 =new Label(MoleGame.myBundle.get("tuto19") , skin);
		final Label tuto7 =new Label(MoleGame.myBundle.get("tuto20") , skin);
		final Label tuto8 =new Label(MoleGame.myBundle.get("tuto21") , skin);
		final Label tuto9 =new Label(MoleGame.myBundle.get("tuto22") , skin);
		final Label tuto10=new Label(MoleGame.myBundle.get("tuto23"), skin);
		final Label tuto11=new Label(MoleGame.myBundle.get("tuto24"), skin);
		final Label tuto12=new Label(MoleGame.myBundle.get("tuto25"), skin);
		tuto4.setAlignment(Align.center);
		tuto5.setAlignment(Align.center);
		tuto1.setPosition(stage.getWidth()/2-tuto1.getWidth()/2, stage.getHeight()*3/4f-tuto1.getHeight()/2);
		tuto2.setPosition(stage.getWidth()/2-tuto2.getWidth()/2, stage.getHeight()*3/4f-tuto1.getHeight()/2);
		tuto3.setPosition(stage.getWidth()/2-tuto3.getWidth()/2, stage.getHeight()*3/4f-tuto1.getHeight()/2);
		tuto4.setPosition(stage.getWidth()/2-tuto4.getWidth()/2, stage.getHeight()*3/4f-tuto1.getHeight()/2);
		tuto5.setPosition(stage.getWidth()/2-tuto5.getWidth()/2, stage.getHeight()*3/4f-tuto1.getHeight()/2);
		tuto6.setPosition(stage.getWidth()/2-tuto6.getWidth()/2, stage.getHeight()*3/4f-tuto1.getHeight()/2);
		tuto7.setPosition(stage.getWidth()/2-tuto7.getWidth()/2, stage.getHeight()*3/4f-tuto1.getHeight()/2);
		tuto8.setPosition(stage.getWidth()/2-tuto8.getWidth()/2, stage.getHeight()*3/4f-tuto1.getHeight()/2);
		tuto9.setPosition(stage.getWidth()/2-tuto9.getWidth()/2, stage.getHeight()*3/4f-tuto1.getHeight()/2);
		tuto10.setPosition(stage.getWidth()/2-tuto10.getWidth()/2, stage.getHeight()*3/4f-tuto1.getHeight()/2);
		tuto11.setPosition(stage.getWidth()/2-tuto11.getWidth()/2, stage.getHeight()*3/4f-tuto1.getHeight()/2);
		tuto12.setPosition(stage.getWidth()/2-tuto12.getWidth()/2, stage.getHeight()*3/4f-tuto1.getHeight()/2);
		tuto1.setTouchable(Touchable.disabled);
		tuto2.setTouchable(Touchable.disabled);
		tuto3.setTouchable(Touchable.disabled);
		tuto4.setTouchable(Touchable.disabled);
		tuto5.setTouchable(Touchable.disabled);
		tuto6.setTouchable(Touchable.disabled);
		tuto7.setTouchable(Touchable.disabled);
		tuto8.setTouchable(Touchable.disabled);
		tuto9.setTouchable(Touchable.disabled);
		tuto10.setTouchable(Touchable.disabled);
		tuto11.setTouchable(Touchable.disabled);
		tuto12.setTouchable(Touchable.disabled);
		tuto1.getColor().a=0;
		tuto2.getColor().a=0;
		tuto3.getColor().a=0;
		tuto4.getColor().a=0;
		tuto5.getColor().a=0;
		tuto6.getColor().a=0;
		tuto7.getColor().a=0;
		tuto8.getColor().a=0;
		tuto9.getColor().a=0;
		tuto10.getColor().a=0;
		tuto11.getColor().a=0;
		tuto12.getColor().a=0;
		
		tutobg  = new Image(skin.newDrawable("white"));
		tutobg.setSize(stage.getWidth(), stage.getHeight());
		tutobg.getColor().a=0;
		
		Group skipB = new Group();
		Label skiplbl= new Label(MoleGame.myBundle.get("skip"), skin);
		Image skipbg = new Image(Assets.manager.get(Assets.empty, Texture.class));
		skipbg.setSize(stage.getHeight()/5f, stage.getHeight()/6.35f);
		skiplbl.setAlignment(Align.center);
		skiplbl.setPosition(skipbg.getWidth()/2-skiplbl.getWidth()/2, skipbg.getHeight()/2-skiplbl.getHeight()/2);
		skipB.addActor(skipbg);
		skipB.addActor(skiplbl);
		skipB.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				tuto.addAction(sequence(delay(0),alpha(0,1),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
				mainui.addAction(sequence(delay(1),com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(1,2)));
				mole.autotuto=false;
				tutoover=true;
				mole.goodland=0;
			}
		});
		skipB.setSize(stage.getHeight()/5f, stage.getHeight()/6.35f);
		skipB.setPosition(stage.getWidth()-pad-skipB.getWidth(), stage.getHeight()-skipB.getHeight()-pad);
		skipB.addAction(forever(sequence(alpha(0.8f,1,sine),alpha(0.2f,1,sine))));
		
		tuto.addActor(c2c);
		tuto.addActor(tuto1);
		tuto.addActor(tuto2);
		tuto.addActor(tuto3);
		tuto.addActor(tuto4);
		tuto.addActor(tuto5);
		tuto.addActor(tuto6);
		tuto.addActor(tuto7);
		tuto.addActor(tuto8);
		tuto.addActor(tuto9);
		tuto.addActor(tuto10);
		tuto.addActor(tuto11);
		tuto.addActor(tuto12);
		tuto.addActor(tutobg);
		tuto.addActor(skipB);
		tuto.getColor().a=0;
		
		mole.autotuto=true;
		tuto. addAction(sequence(delay(0), alpha(1,1)));
		tuto1.addAction(sequence(delay(0.5f), alpha(1,1)));
		c2c.addAction(sequence(delay(0.5f), visible(true)));
		tutobg.addAction(sequence(delay(0),touchable(Touchable.enabled)));
		tutobg.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(tuto1.getColor().a==1){
					if(Save.gd.sound)click.play();
					tuto1.addAction(sequence(delay(1), alpha(0,1)));
					tuto2.addAction(sequence(delay(2), alpha(1,1)));
				}
				if(tuto2.getColor().a==1){
					if(Save.gd.sound)click.play();
					tuto2.addAction(sequence(delay(1), alpha(0,1)));
					tuto3.addAction(sequence(delay(2), alpha(1,1)));
				}
				if(tuto3.getColor().a==1){
					if(Save.gd.sound)click.play();
					tuto3.addAction(sequence(delay(1), alpha(0,1)));
					c2c.addAction(sequence(delay(1), com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
					tuto4.addAction(sequence(delay(2), alpha(1,1)));
					tutobg.setTouchable(Touchable.disabled);
					mole.autotuto=false;
				}
				if(tuto6.getColor().a==1){
					if(Save.gd.sound)click.play();
					tuto6.addAction(sequence(delay(1), alpha(0,1)));
					tuto7.addAction(sequence(delay(2), alpha(1,1)));
				}
				if(tuto7.getColor().a==1){
					if(Save.gd.sound)click.play();
					tuto7.addAction(sequence(delay(1), alpha(0,1)));
					tuto8.addAction(sequence(delay(2), alpha(1,1)));
				}
				if(tuto8.getColor().a==1){
					if(Save.gd.sound)click.play();
					tuto8.addAction(sequence(delay(1), alpha(0,1)));
					tuto9.addAction(sequence(delay(2), alpha(1,1)));
				}
				if(tuto9.getColor().a==1){
					if(Save.gd.sound)click.play();
					tuto9.addAction(sequence(delay(1), alpha(0,1)));
					tuto10.addAction(sequence(delay(2), alpha(1,1)));
				}
				if(tuto10.getColor().a==1){
					if(Save.gd.sound)click.play();
					tuto10.addAction(sequence(delay(1), alpha(0,1)));
					tuto11.addAction(sequence(delay(2), alpha(1,1)));
				}
				if(tuto11.getColor().a==1){
					if(Save.gd.sound)click.play();
					tuto11.addAction(sequence(delay(1), alpha(0,1)));
					tuto12.addAction(sequence(delay(2), alpha(1,1)));
				}
				if(tuto12.getColor().a==1){
					if(Save.gd.sound)click.play();
					tuto12.addAction(sequence(delay(1), alpha(0,1)));
					tutobg.addAction(sequence(delay(1),touchable(Touchable.disabled)));
					tuto.addAction(sequence(delay(1),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
					mainui.addAction(sequence(delay(1),com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(1,2)));
					mole.autotuto=false;
					tutoover=true;
				}
			}
		});
		tuto.setPosition(0, 0);
		stage.addActor(tuto);
	}
	
	
	private void createsetb() {
		
		final Table achievtable =new Table();
		final Image achievbg = new Image(Assets.manager.get(Assets.setting, Texture.class));
		
		Image achievB = new Image(Assets.manager.get(Assets.achiev, Texture.class));
		achievB.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//if(Save.gd.sound)click.play();
				if(MoleGame.gservices!=null)MoleGame.gservices.showAchieve();
				achievtable.addAction(sequence(alpha(0,0.3f),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
				pausetable.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(1,0.3f)));
				achievbg.setTouchable(Touchable.disabled);
				}});
		
		Image hsB = new Image(Assets.manager.get(Assets.challenge, Texture.class));
		hsB.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//if(Save.gd.sound)click.play();
				if(MoleGame.gservices!=null)MoleGame.gservices.showScores();
				achievtable.addAction(sequence(alpha(0,0.3f),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
				pausetable.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(1,0.3f)));
				achievbg.setTouchable(Touchable.disabled);
				}});
			
		
		
		
		achievtable.setFillParent(true);
		achievtable.defaults().uniform().fill().center().padTop(15);
		achievtable.add(achievB).size(stage.getHeight()/6.35f).pad(15);
		achievtable.add(hsB).size(stage.getHeight()/6.35f).pad(15);
		achievtable.getColor().a=0;
		achievtable.setVisible(false);
		
		achievbg.setPosition(0, 0);
		achievbg.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		achievbg.setTouchable(Touchable.disabled);
		achievbg.getColor().a=0;
		achievbg.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//if(Save.gd.sound)click.play();
				achievtable.addAction(sequence(alpha(0,0.3f),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
				pausetable.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(1,0.3f)));
				achievbg.setTouchable(Touchable.disabled);
			}
		
		});
		
		final Image buttonachiev= new Image(Assets.manager.get(Assets.achiev, Texture.class));
		buttonachiev.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
		buttonachiev.setPosition(pad, pad);
		buttonachiev.getColor().a=0;
		buttonachiev.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Save.load();
				//if(Save.gd.sound)click.play();
				pausetable.addAction(sequence(alpha(0,0.3f),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
				achievtable.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(1,0.3f)));
				achievbg.setTouchable(Touchable.enabled);
				}});
		
		
		
		final Image buttonmusic;
		if(!Save.gd.music)
			buttonmusic= new Image(Assets.manager.get(Assets.musicoff, Texture.class));
		else
			buttonmusic= new Image(Assets.manager.get(Assets.musicon, Texture.class));
		buttonmusic.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
		buttonmusic.setPosition(pad, pad);
		buttonmusic.getColor().a=0;
		buttonmusic.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Save.load();
				//if(Save.gd.sound)click.play();
				if(!Save.gd.music) {
					Save.gd.music=true;
					buttonmusic.setDrawable(new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.musicon, Texture.class))));
					Save.save();
					music1.play();musicspace.play();
					}
				else  {
					Save.gd.music=false; Save.save();
					buttonmusic.setDrawable(new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.musicoff, Texture.class))));
					music1.stop();musicspace.stop();
					}
				}});
		
		
		final Image buttonfx;
		if(!Save.gd.sound)
			buttonfx= new Image(Assets.manager.get(Assets.soundoff, Texture.class));
		else
			buttonfx= new Image(Assets.manager.get(Assets.soundon, Texture.class));
		buttonfx.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
		buttonfx.setPosition(pad, pad);
		buttonfx.getColor().a=0;
		buttonfx.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(!Save.gd.sound) {
					//click.play();
					Save.gd.sound=true;
					buttonfx
					.setDrawable(new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.soundon, Texture.class))));
					Save.save();
				}
				else  {
					Save.gd.sound=false; Save.save();
					buttonfx.setDrawable(new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.soundoff, Texture.class))));
					mole.sstar.stop();mole.swind.stop();mole.sbland1.stop(); mole.scoin.stop();
					mole.sdig.stop();mole.sfire.stop();mole.sbland2.stop();mole.sjump.stop();mole.sland.stop();mole.sstun.stop();
					}
				}});
		
		final Table langtable =new Table();
		final Image langbg = new Image(Assets.manager.get(Assets.setting, Texture.class));
		
		Group frB = new Group();
		Image frBim = new Image(Assets.manager.get(Assets.empty, Texture.class));
		frBim.setSize(stage.getHeight()/6.35f, stage.getHeight()/6.35f);
		Label fr= new Label("Fr", skin);
		fr.setAlignment(Align.center);
		fr.setPosition(stage.getHeight()/12.7f-fr.getWidth()/2, stage.getHeight()/12.7f-fr.getHeight()/2);
		frB.addActor(frBim);
		frB.addActor(fr);
		frB.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//if(Save.gd.sound)click.play();
				Save.gd.lang=1;
				FileHandle baseFileHandle = Gdx.files.internal("internation/MyBundle");
				Locale locale = new Locale("fr");
				MoleGame.myBundle = I18NBundle.createBundle(baseFileHandle, locale);
				langtable.addAction(sequence(alpha(0,0.3f),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
				pausetable.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(1,0.3f)));
				langbg.setTouchable(Touchable.disabled);
			}});
		
		Group enB = new Group();
		Image enBim = new Image(Assets.manager.get(Assets.empty, Texture.class));
		enBim.setSize(stage.getHeight()/6.35f, stage.getHeight()/6.35f);
		Label en= new Label("En", skin);
		en.setAlignment(Align.center);
		en.setPosition(stage.getHeight()/12.7f-en.getWidth()/2, stage.getHeight()/12.7f-en.getHeight()/2);
		enB.addActor(enBim);
		enB.addActor(en);
		enB.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//if(Save.gd.sound)click.play();
				Save.gd.lang=0;
				FileHandle baseFileHandle = Gdx.files.internal("internation/MyBundle");
				Locale locale = new Locale("en");
				MoleGame.myBundle = I18NBundle.createBundle(baseFileHandle, locale);
				langtable.addAction(sequence(alpha(0,0.3f),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
				pausetable.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(1,0.3f)));
				langbg.setTouchable(Touchable.disabled);
				}});
		
		
		
		langtable.setFillParent(true);
		langtable.defaults().uniform().fill().center().padTop(15);
		langtable.add(frB).size(stage.getHeight()/6.35f).pad(15);
		langtable.add(enB).size(stage.getHeight()/6.35f).pad(15);
		langtable.getColor().a=0;
		langtable.setVisible(false);
		
		langbg.setPosition(0, 0);
		langbg.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		langbg.setTouchable(Touchable.disabled);
		langbg.getColor().a=0;
		langbg.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//if(Save.gd.sound)click.play();
				langtable.addAction(sequence(alpha(0,0.3f),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
				pausetable.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(1,0.3f)));
				langbg.setTouchable(Touchable.disabled);
			}
		
		});
		
		final Image buttonlang= new Image(Assets.manager.get(Assets.lang, Texture.class));
		buttonlang.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
		buttonlang.setPosition(pad, pad);
		buttonlang.getColor().a=0;
		buttonlang.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//if(Save.gd.sound)click.play();
				Save.load();
				pausetable.addAction(sequence(alpha(0,0.3f),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
				langtable.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(1,0.3f)));
				langbg.setTouchable(Touchable.enabled);
				}});
		
		buttonfx.setTouchable(Touchable.disabled);
		buttonmusic.setTouchable(Touchable.disabled);
		buttonlang.setTouchable(Touchable.disabled);
		buttonachiev.setTouchable(Touchable.disabled);
		
		
		buttonset = new Image(Assets.manager.get(Assets.setting, Texture.class));
		buttonset.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
		buttonset.setPosition(pad, pad);
		buttonset.setOrigin(stage.getHeight()/8.4f/2, stage.getHeight()/8.4f/2);
		
		final Image setbg = new Image(Assets.manager.get(Assets.setting, Texture.class));
		setbg.setPosition(0, 0);
		setbg.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		setbg.setTouchable(Touchable.disabled);
		setbg.getColor().a=0;
		setbg.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//if(Save.gd.sound)click.play();
				buttonset.addAction(rotateTo(0,0.5f));
				buttonfx.addAction(parallel(alpha(0,0.5f), moveTo(pad,pad,0.5f)));
				buttonmusic.addAction(parallel(alpha(0,0.5f), moveTo(pad,pad,0.5f)));
				buttonlang.addAction(parallel(alpha(0,0.5f), moveTo(pad,pad,0.5f)));
				buttonachiev.addAction(parallel(alpha(0,0.5f), moveTo(pad,pad,0.5f)));
				setbg.setTouchable(Touchable.disabled);
				buttonfx.setTouchable(Touchable.disabled);
				buttonmusic.setTouchable(Touchable.disabled);
				buttonlang.setTouchable(Touchable.disabled);
				buttonachiev.setTouchable(Touchable.disabled);
			}
		
		});
		
		buttonset.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//if(Save.gd.sound)click.play();
				buttonset.addAction(rotateTo(-180,0.5f));
				buttonfx.addAction(parallel(alpha(1,0.5f), moveTo((stage.getWidth()/13f+pad),pad,0.5f)));
				buttonmusic.addAction(parallel(alpha(1,0.5f), moveTo((2*stage.getWidth()/13f+pad),pad,0.5f)));
				buttonlang.addAction(parallel(alpha(1,0.5f), moveTo((3*stage.getWidth()/13f+pad),pad,0.5f)));
				buttonachiev.addAction(parallel(alpha(1,0.5f), moveTo((4*stage.getWidth()/13f+pad),pad,0.5f)));
				setbg.setTouchable(Touchable.enabled);
				buttonfx.setTouchable(Touchable.enabled);
				buttonmusic.setTouchable(Touchable.enabled);
				buttonlang.setTouchable(Touchable.enabled);
				buttonachiev.setTouchable(Touchable.enabled);
			}
		
		});
		
		buttonset.setVisible(false);
		stage.addActor(buttonset);
		stage.addActor(setbg);
		stage.addActor(buttonfx);
		stage.addActor(buttonmusic);
		stage.addActor(buttonlang);
		stage.addActor(buttonachiev);
		stage.addActor(langbg);
		stage.addActor(langtable);
		stage.addActor(achievbg);
		stage.addActor(achievtable);
		
	}
	
	
	@Override
	public void render(float delta) {
		if(mole.timeslow && mole.timetimer>0){
			if(timeScale>0.4f)
				timeScale *=0.97f;
			else{
				timeScale=0.4f;
			}
		}
		else if(!gameover){
			if(timeScale<1)
				timeScale *=1.05f;
			else{
				timeScale=1f;
			}
		}
		delta = Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f) * timeScale;
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//update if not paused
		 switch(state){
		 	case Running:
			//update
			update(delta);
			break;
			case Paused:
			//don't update
			break;}
		 
		batch.setProjectionMatrix(camera.combined);
		if(black.getColor().a<0.9f){
		//draw blue background
		batch.begin();
		backsky.setPosition(mole.molebody.getPosition().x-lvllenght, 0);
		backsky.draw(batch);
//		batch.end();
		
		//draw background
		sky0bg   .render(batch); 
		upskybg  .render(batch);
		cloud1bg .render(batch);
		cloud2bg .render(batch);
		cloud3bg .render(batch);
		batch.draw(rainbow, mole.molebody.getPosition().x/1.5f-12, -1,38,18);
		hills0bg .render(batch); 
		hills1bg .render(batch); 
		tree3bg  .render(batch);
		tree2bg  .render(batch);
		hillsf0bg.render(batch); 
		for(int i = -numDrawX; i < numDrawX; i=i+2) {
			windeffectback.setPosition(i*15+leveloffset*lvllenght, 3);
			windeffectback.drawext(batch);
		}
		tree0bg  .render(batch);
		tree1bg  .render(batch);
		rockbg   .render(batch);
		for(int i = -numDrawX-1; i < numDrawX; i=i+2) {
			windeffectback1.setPosition(i*15+leveloffset*lvllenght, 3);
			windeffectback1.drawext(batch);
			}
		windeffectback1.draw(batch, delta);
		windeffectback.draw(batch, delta);
		ground0bg       .render(batch); 
		groundjointbg   .render(batch);
		ugroundjoint1bg .render(batch); 
		ground1bg       .render(batch); 	
		ugroundjoint2bg .render(batch); 	
		ground2bg       .render(batch); 
		ugroundjoint3bg .render(batch); 
		ground3bg       .render(batch); 
		ground3bg       .render(batch,-5); 
		grass1bg         .render(batch);
		grass2bg         .render(batch);
		grass3bg         .render(batch);
	
		//Infinite Depth Background
		
		//stars
//		if(mole.molebody.getPosition().y>50){
//		stars1.render(batch);
//		stars2.render(batch);
//		stars3.render(batch);
//		
//		asteroidbbg.render(batch,1, 0.1f,true);
//		asteroid2bg.render(batch,8, 0.8f,true);
//		asteroid1bg.render(batch,5,    1,true);
//		}
		
		Sprite star1    =   new Sprite(Assets.manager.get(Assets.stars2,   Texture.class));
		star1.setSize(10, 10);
		star1.setOriginCenter();
		/*if(camera.position.y>800){
			star1.set=MathUtils.clamp(1-camera.position.y/950,0,1);
		}*/
		for(int i=-3;i<=6;i++){
			star1.setRotation(90*i);
			star1.setPosition(camera.position.x+9*i, 50+40*staroffset1);
			star1.draw(batch);
			star1.setRotation(90*(i+1));
			star1.setPosition(camera.position.x+10*i, 60+40*staroffset2);
			star1.draw(batch);
			star1.setRotation(90*(i-1));
			star1.setPosition(camera.position.x+8*i, 70+40*staroffset3);
			star1.draw(batch);
			star1.setRotation(90*(i-2));
			star1.setPosition(camera.position.x+10*i, 80+40*staroffset4);
			star1.draw(batch);
			
		}
		if(camera.position.y-camera.viewportHeight*camera.zoom/2>50+10+40*staroffset1)
			staroffset1++;
		if(camera.position.y-camera.viewportHeight*camera.zoom/2>60+10+40*staroffset2)
			staroffset2++;
		if(camera.position.y-camera.viewportHeight*camera.zoom/2>70+10+40*staroffset3)
			staroffset3++;
		if(camera.position.y-camera.viewportHeight*camera.zoom/2>80+10+40*staroffset4)
			staroffset4++;
		if(staroffset1>0){
			if(camera.position.y+camera.viewportHeight*camera.zoom/2<50+40*staroffset1)
				staroffset1--;
			if(camera.position.y+camera.viewportHeight*camera.zoom/2<60+40*staroffset2)
				staroffset2--;
			if(camera.position.y+camera.viewportHeight*camera.zoom/2<70+40*staroffset3)
				staroffset3--;	
			if(camera.position.y+camera.viewportHeight*camera.zoom/2<80+40*staroffset4)
				staroffset4--;	
		}
		
		//planet
		moon     .setPosition(mole.molebody.getPosition().x-12,  60);
			if(onScreen(moon))      moon     .draw(batch);
		mars     .setPosition(mole.molebody.getPosition().x+3,  110);
			if(onScreen(mars))      mars     .draw(batch);
		jupiter  .setPosition(mole.molebody.getPosition().x-18, 200);
			if(onScreen(jupiter))   jupiter  .draw(batch);
		saturne  .setPosition( mole.molebody.getPosition().x+3, 280);
			if(onScreen(saturne))   saturne  .draw(batch);
		uranus   .setPosition(mole.molebody.getPosition().x+3,  380);
			if(onScreen(uranus))    uranus   .draw(batch);
		neptune  .setPosition(mole.molebody.getPosition().x-10, 500);
			if(onScreen(neptune))   neptune  .draw(batch);
		nebuleuse.setPosition(mole.molebody.getPosition().x-30, 700);
			if(onScreen(nebuleuse)) nebuleuse.draw(batch);
		nebulaeffect1.setPosition(mole.molebody.getPosition().x, 700+17);
		float nv=mole.molebody.getLinearVelocity().x;
		nebulaeffect1.getEmitters().get(0).getAngle().setHighMax((nv>0? 0:180)+15);
		nebulaeffect1.getEmitters().get(0).getAngle().setHighMin((nv>0? 0:180)-15);
		nebulaeffect1.getEmitters().get(0).getAngle().setLowMax((nv>0? 0:180)+5);
		nebulaeffect1.getEmitters().get(0).getAngle().setLowMin((nv>0? 0:180)-5);
		nebulaeffect1.getEmitters().get(0).getVelocity().setHighMax(nv+3);
		nebulaeffect1.getEmitters().get(0).getVelocity().setHighMin(nv-3);
		nebulaeffect1.getEmitters().get(0).getVelocity().setLowMax(nv-5);
		nebulaeffect1.getEmitters().get(0).getVelocity().setLowMin(nv-8);
		nebulaeffect1.getEmitters().get(1).getAngle().setHighMax((nv>0? 0:180)+15);
		nebulaeffect1.getEmitters().get(1).getAngle().setHighMin((nv>0? 0:180)-15);
		nebulaeffect1.getEmitters().get(1).getAngle().setLowMax((nv>0? 0:180)+5);
		nebulaeffect1.getEmitters().get(1).getAngle().setLowMin((nv>0? 0:180)-5);
		nebulaeffect1.getEmitters().get(1).getVelocity().setHighMax(nv+3);
		nebulaeffect1.getEmitters().get(1).getVelocity().setHighMin(nv-3);
		nebulaeffect1.getEmitters().get(1).getVelocity().setLowMax(nv-5);
		nebulaeffect1.getEmitters().get(1).getVelocity().setLowMin(nv-8);
		nebulaeffect1.getEmitters().get(2).getAngle().setHighMax((nv>0? 0:180)+15);
		nebulaeffect1.getEmitters().get(2).getAngle().setHighMin((nv>0? 0:180)-15);
		nebulaeffect1.getEmitters().get(2).getAngle().setLowMax((nv>0? 0:180)+5);
		nebulaeffect1.getEmitters().get(2).getAngle().setLowMin((nv>0? 0:180)-5);
		nebulaeffect1.getEmitters().get(2).getVelocity().setHighMax(nv+3);
		nebulaeffect1.getEmitters().get(2).getVelocity().setHighMin(nv-3);
		nebulaeffect1.getEmitters().get(2).getVelocity().setLowMax(nv-5);
		nebulaeffect1.getEmitters().get(2).getVelocity().setLowMin(nv-8);
		nebulaeffect1.draw(batch,delta,2/nv);
		//draw trail
		mole.rendertrail(batch);
		//draw mole
		if(gameover && !progress.win)
			mole.render(batch, delta* Interpolation.pow2In.apply(0.5f, 0, MathUtils.clamp(gameOverTimer / 4f, 0.01f, 1)));
		else 
			mole.render(batch, delta);
		//explosion effect 
		mole.renderexplosion(batch);
		
		//draw bonuses
		/*if(bonuses!=null)
			drawBonuses(delta);*/
		
		//front ground 
		hcloudbg.render(batch); 
		
		batch.end();
		}
		
		//======================  MOVE LEVEL OFFSET  ====================//
		//left side<first image
		while(mole.molebody.getPosition().x<-lvllenght/2+(leveloffset * lvllenght)){
			leveloffset--;
			/*if(bonuses!=null)
			for(int i = 0; i < bonuses.size; i++) {
				bonuses.get(i).body.setTransform(
						bonuses.get(i).body.getPosition().x -lvllenght, bonuses.get(i).body.getPosition().y, 0);
			}*/
			if(coins!=null)
			for(int i = 0; i < coins.size; i++) {
				coins.get(i).body.setTransform(
						coins.get(i).body.getPosition().x -lvllenght, coins.get(i).body.getPosition().y, 0);
			}
			ground0b.body.setTransform(ground0b.body.getPosition().x-lvllenght, ground0b.body.getPosition().y, 0);
			ground1b.body.setTransform(ground1b.body.getPosition().x-lvllenght, ground1b.body.getPosition().y, 0);
			ground2b.body.setTransform(ground2b.body.getPosition().x-lvllenght, ground2b.body.getPosition().y, 0);
			ground3b.body.setTransform(ground3b.body.getPosition().x-lvllenght, ground3b.body.getPosition().y, 0);
			ground4b.body.setTransform(ground4b.body.getPosition().x-lvllenght, ground4b.body.getPosition().y, 0);
			
		}
		//right side> last image
		while(mole.molebody.getPosition().x>lvllenght/2 + (leveloffset * lvllenght)){
			leveloffset++;
			/*for(int i = 0; i < bonuses.size; i++) {
				bonuses.get(i).body.setTransform(
						bonuses.get(i).body.getPosition().x +lvllenght, bonuses.get(i).body.getPosition().y, 0);
			}*/
			ground0b.body.setTransform(ground0b.body.getPosition().x+lvllenght, ground0b.body.getPosition().y, 0);
			ground1b.body.setTransform(ground1b.body.getPosition().x+lvllenght, ground1b.body.getPosition().y, 0);
			ground2b.body.setTransform(ground2b.body.getPosition().x+lvllenght, ground2b.body.getPosition().y, 0);
			ground3b.body.setTransform(ground3b.body.getPosition().x+lvllenght, ground3b.body.getPosition().y, 0);
			ground4b.body.setTransform(ground4b.body.getPosition().x+lvllenght, ground4b.body.getPosition().y, 0);
		}
			
		
		//////////////===================  MUSIC  =====================///////////////////
		if(mole.molebody.getPosition().y>25) {
			music1.setVolume(MathUtils.clamp(mole.vitesse/100, 0.2f, 0.8f)/(mole.molebody.getPosition().y-25));
		}
		else
			music1.setVolume(MathUtils.clamp(mole.vitesse/100, 0.2f, 0.8f));
		
		
		if(mole.molebody.getPosition().y>60) {
			musicspace.setVolume(0.005f*(mole.molebody.getPosition().y-60));
		}
		
       //////////////============================  UI  =================================//////////
		uiplayanimation();
		if(type!=MoleGame.ARCADELVL){
			if(staredtimer>=0)staredtimer-=delta;
			if(progress.win && !stared1 && staredtimer<=0){
				star1.addAction(sequence(sizeTo(stage.getHeight()/2,-stage.getHeight()/2),alpha(0),
						parallel(sizeTo(stage.getHeight()/16f,-stage.getHeight()/16f,1), alpha(1,1))));
				stared1=true;
				staredtimer=1;
			}
			if(progress.score>score1 && !stared2 && stared1 && staredtimer<=0){
				star2.addAction(sequence(sizeTo(stage.getHeight()/2,-stage.getHeight()/2),alpha(0),
						parallel(sizeTo(stage.getHeight()/16f,-stage.getHeight()/16f,1), alpha(1,1))));
				stared2=true;
				staredtimer=1;
			}
			if(progress.score>score2 && !stared3 && stared2 && staredtimer<=0){
				star3.addAction(sequence(sizeTo(stage.getHeight()/2,-stage.getHeight()/2),alpha(0),
						parallel(sizeTo(stage.getHeight()/16f,-stage.getHeight()/16f,1), alpha(1,1))));
				stared3=true;
			}
		}
		//Game Over!
		if(progress.gameover ==true && mole.molebody.getPosition().y<0 && !gameovertable.hasParent()) {
			gameover =true;
			if(!progress.win){
				showGOlose();
				mole.autotuto=true;
			}
			else{
				showGOwin(progress.score);
				mole.auto=true;
			}
			mainui   .addAction(sequence(alpha(0,0.5f),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
			flipcom  .addAction(sequence(alpha(0,0.5f),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
			heightcom.addAction(sequence(alpha(0,0.5f),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
			landcom  .addAction(sequence(alpha(0,0.5f),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
			bonuscom .addAction(sequence(alpha(0,0.5f),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
			holecom  .addAction(sequence(alpha(0,0.5f),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
			powers   .addAction(sequence(alpha(0,0.5f),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
		}
		if(gameover && !progress.win){
			gameOverTimer += delta;
		}
		
		/////powers
		Color color1;
		if(mole.timetimer>20/3)color1=new Color((3-mole.timetimer*3/10),1,0,1);
		else if(mole.timetimer>10/3)color1=new Color(1,mole.timetimer*3/10-1,0,1);
		else color1=new Color(1,0,0,1);
		
		Color color2;
		if(mole.planetimer>20/3)color2=new Color((3-mole.planetimer*3/10),1,0,1);
		else if(mole.planetimer>10/3)color2=new Color(1,mole.planetimer*3/10-1,0,1);
		else color2=new Color(1,0,0,1);
		
		Color color3;
		if(mole.jptimer>2)color3=new Color((3-mole.jptimer),1,0,1);
		else if(mole.jptimer>1)color3=new Color(1,mole.jptimer-1,0,1);
		else color3=new Color(1,0,0,1);
		
		if(mole.timetimer>0)
			((RadialSprite) timetimer.getDrawable()).setAngle(360 -mole.timetimer*36);
		((RadialSprite) timetimer.getDrawable()).setColor(color1);
		float a1=timebutton.getColor().a;
		if(mole.timeslow) 
			timebutton.setColor(color1);
		else timebutton.setColor(1,1,1,1);
		timebutton.getColor().a=a1;
		
		if(mole.planetimer>0)
			((RadialSprite) planetimer.getDrawable()).setAngle(360 -mole.planetimer*36);
		((RadialSprite) planetimer.getDrawable()).setColor(color2);
		float a2=planebutton.getColor().a;
		if(mole.plane) 
			planebutton.setColor(color2);
		else planebutton.setColor(1,1,1,1);
		planebutton.getColor().a=a2;
		
		if(mole.jptimer>0)
			((RadialSprite) jptimer.getDrawable()).setAngle(360 -mole.jptimer*360/3f);
		((RadialSprite) jptimer.getDrawable()).setColor(color3);
		float a3=jpbutton.getColor().a;
		if(mole.jetpack) 
			jpbutton.setColor(color3);
		else jpbutton.setColor(1,1,1,1);
		jpbutton.getColor().a=a3;
		
		if(mole.jptimer<=0 &&mole.jpnumber<=0 &&jptimer.getColor().a==1){
			jptimer .addAction(sequence(alpha(0,0.5f,pow2Out),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide() ));
			jpbutton.addAction(sequence(alpha(0,0.5f,pow2Out),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide() ));
			jpnumber.addAction(sequence(alpha(0,0.5f,pow2Out),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide() ));
		}
		if(mole.planetimer<=0  && planetimer.getColor().a==1){
			planetimer .addAction(sequence(alpha(0,0.5f,pow2Out),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide() ));
			planebutton.addAction(sequence(alpha(0,0.5f,pow2Out),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide() ));
		}
		if(mole.timetimer<=0  && timetimer.getColor().a==1){
			timetimer .addAction(sequence(alpha(0,0.5f,pow2Out),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide() ));
			timebutton.addAction(sequence(alpha(0,0.5f,pow2Out),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide() ));
		}
		if(!tutoover){
			if(timeScale==0.4f && !tutocheck1){
				tutocheck1=true;
				tuto4.addAction(sequence(delay(0.2f), alpha(0,0.2f)));
				tuto5.addAction(sequence(delay(0.4f), alpha(1,0.2f)));
			}
			if(tuto5.getColor().a==1 && timeScale==1f && !tutocheck2){
				tutocheck2=true;
				tuto5.addAction(sequence(delay(1), alpha(0,1)));
				tuto6.addAction(sequence(delay(2), alpha(1,1)));
				mole.autotuto=true;
				tutobg.setTouchable(Touchable.enabled);
				c2c.addAction(sequence(delay(1), com.badlogic.gdx.scenes.scene2d.actions.Actions.show()));
			}
		}
		stage.act(delta);
		stage.draw();
		
		tweenManager.update(delta);
		//debugRenderer.render(world, camera.combined);
		
	}

	private void showGOwin(final int score) {
		///win level
		if(Save.gd.nextlvl<Save.gd.starX.length-1 && lvl==Save.gd.nextlvl){
			Save.gd.nextlvl+=1;
			Save.gd.newlvlunloked=true;
		}
		
		///GO table
		gameovertable = new Table(skin);
    	gameovertable.setFillParent(true);
		Image gover = new Image(Assets.manager.get(Assets.gover2, Texture.class));
    	Label scoretactor1    = new Label("Score : " + Integer.toString(score),skin,"default");
    	Label bestscore;
    	if(progress.score>Save.gd.scores[lvl])
    		bestscore  = new Label(MoleGame.myBundle.get("newbest"), skin,"default");
    	else bestscore = new Label(MoleGame.myBundle.get("best")+Integer.toString(Save.gd.scores[lvl]), skin,"default");
    	
    	Image buttonMenu = new Image(Assets.manager.get(Assets.menu, Texture.class));
		buttonMenu.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(score>Save.gd.scores[lvl]){
					Save.gd.scores[lvl]=progress.score;
					Save.save();
				}
				black.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),
						fadeIn(0.5f),run(new Runnable() {
							@Override
							public void run() {
		((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
					}
				})));
			}
		});

		Image buttonRestart;
		buttonRestart = new Image(Assets.manager.get(Assets.shop, Texture.class));
		buttonRestart.setVisible(false);
		
		//button submit score for arcade or continue for adventure
		Image buttonContinue;
		buttonContinue = new Image(Assets.manager.get(Assets.map, Texture.class));
		buttonContinue.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if(score>Save.gd.scores[lvl]){
						Save.gd.scores[lvl]=score;
					}
					Save.gd.jpbought=0;
					Save.gd.planebought=0;
					Save.gd.timebought=0;
					Save.gd.goldcoins+=coincolected;
					Save.save();
					black.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),
							fadeIn(0.5f),run(new Runnable() {
								@Override
								public void run() {
			((Game) Gdx.app.getApplicationListener()).setScreen(new WorldMap());
						}
					})));
				}
			});
		
		gameovertable.add(gover).size(stage.getHeight()*0.28f).pad(pad).colspan(3).row();
		gameovertable.add(scoretactor1).colspan(3).row();
		gameovertable.add(bestscore).padBottom(30).colspan(3).row();
		gameovertable.defaults().uniform().fill();
		gameovertable.defaults().center().padTop(pad);
		gameovertable.add(buttonContinue).size(stage.getHeight()/6.35f).pad(pad);
		gameovertable.add(buttonRestart) .size(stage.getHeight()/6.35f).pad(pad);
		gameovertable.add(buttonMenu)   .size(stage.getHeight()/6.35f).pad(pad);
		gameovertable.getColor().a=0;
		gameovertable.setVisible(false);
		gover.setOrigin(stage.getHeight()*0.28f/2f,stage.getHeight()*0.28f/2f);
		gover.addAction(moveTo(stage.getWidth()/2f-stage.getHeight()*0.28f/2, stage.getHeight()*1.5f/3f));
    	gover.addAction(forever(sequence(
    			moveTo(stage.getWidth()/2f-stage.getHeight()*0.28f/2, stage.getHeight()*2/3f,0.7f,pow2Out),
				moveTo(stage.getWidth()/2f-stage.getHeight()*0.28f/2, stage.getHeight()*1.5f/3f,0.7f,pow2In)
    			)));
    	stage.addActor(gameovertable);
		gameovertable.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(1,0.5f)));
		
	}

	private void showGOlose() {
		gameovertable.clear();
		Image gover = new Image(Assets.manager.get(Assets.gover, Texture.class));
    	
    	Image buttonMenu = new Image(Assets.manager.get(Assets.map, Texture.class));
		buttonMenu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Save.gd.jpbought=0;
				Save.gd.planebought=0;
				Save.gd.timebought=0;
				Save.save();
				black.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),
						fadeIn(0.5f),run(new Runnable() {
							@Override
							public void run() {
		((Game) Gdx.app.getApplicationListener()).setScreen(new WorldMap());
					}
				})));
			}
		});

		Image buttonRestart = new Image(Assets.manager.get(Assets.raz, Texture.class));
		buttonRestart.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Save.gd.jpbought=0;
				Save.gd.planebought=0;
				Save.gd.timebought=0;
				Save.save();
				black.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),
						fadeIn(0.5f),run(new Runnable() {
							@Override
							public void run() {
		((Game) Gdx.app.getApplicationListener()).setScreen(new Tuto1(lvl,type,objectif,limittype,limit, score1, score2));
					}
				})));
			}
		});
		
		Image coinIm=new Image(Assets.manager.get(Assets.orbsilver, Texture.class));
		coinIm.setSize(stage.getHeight()/16,stage.getHeight()/16);
		coinIm.setPosition(stage.getWidth()-coinIm.getWidth()-pad,stage.getHeight()-coinIm.getHeight()-pad);
		gameovertable.addActor(coinIm);
		coinsbuymenu=new Label(Integer.toString(Save.gd.silvercoins),skin);
		coinsbuymenu.setPosition(
				stage.getWidth()-2*pad-stage.getHeight()/16,
				stage.getHeight()-stage.getHeight()/16/2-pad, Align.right);
		gameovertable.addActor(coinsbuymenu);
		
		Group buttonContinue = new Group();
		Image buttonbg = new Image(Assets.manager.get(Assets.empty, Texture.class));
		buttonbg.setSize(stage.getHeight()/6.35f,stage.getHeight()/6.35f);
		Label continutxt;
		if(limittype==MoleGame.LIMITTIME){
			continutxt=new Label("+15s", skin);
		}
		else if(limittype==MoleGame.LIMITJUMP){
			continutxt=new Label("+5 jump", skin);
		}
		else{
			continutxt=new Label("+5 try", skin);
		}
		continutxt.setAlignment(Align.center);
		continutxt.setPosition(buttonbg.getHeight()/2-continutxt.getWidth()/2, buttonbg.getHeight()*3/4-continutxt.getHeight()/2);
		Label continuprice=new Label(Integer.toString(CONTINUEPRICE), skin);
		continuprice.setPosition(buttonbg.getHeight()/2-continuprice.getWidth()-pad/2, buttonbg.getHeight()*1/4-continuprice.getHeight()/2);
		Image coinContinue   = new Image(Assets.manager.get(Assets.orbsilver, Texture.class));
		coinContinue.setSize(stage.getHeight()/16, stage.getHeight()/16);
		coinContinue.setPosition(buttonbg.getHeight()/2, continuprice.getY());
		buttonContinue.addActor(buttonbg);
		buttonContinue.addActor(continutxt);
		buttonContinue.addActor(continuprice);
		buttonContinue.addActor(coinContinue);
		buttonContinue.setVisible(false);
		buttonContinue.addListener(new ClickListener() {

				@Override
				public void clicked(InputEvent event, float x, float y) {
					if(Save.gd.silvercoins>=CONTINUEPRICE){
						Save.gd.silvercoins-=CONTINUEPRICE;
						Save.save();
						gameovertable.addAction(sequence(alpha(0,0.5f),
								com.badlogic.gdx.scenes.scene2d.actions.Actions.hide(), removeActor()));
						mainui   .addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(1,0.5f)));
						flipcom  .addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(0,0.5f)));
						heightcom.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(0,0.5f)));
						landcom  .addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(0,0.5f)));
						bonuscom .addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(0,0.5f)));
						holecom  .addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(0,0.5f)));
						powers   .addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(1,0.5f)));
						if(limittype==MoleGame.LIMITTIME)
							progress.settime(0, 15);
						if(limittype==MoleGame.LIMITJUMP)
							progress.limit+=5;
						progress.gameover=false;
						gameOverTimer=0;
						mole.auto=mole.autotuto=false;
						gameover=false;
					}
					//else createbank();
				}
			});
		
		buttonContinue.setVisible(false);
		gameovertable.add(gover).size(stage.getHeight()*0.28f).pad(pad).colspan(3).row();
		gameovertable.defaults().uniform().fill();
		gameovertable.defaults().center().padTop(pad);
		gameovertable.add(buttonRestart) .size(stage.getHeight()/6.35f).pad(pad);
		gameovertable.add(buttonContinue).size(stage.getHeight()/6.35f).pad(pad);
		gameovertable.add(buttonMenu)    .size(stage.getHeight()/6.35f).pad(pad);
		gameovertable.getColor().a=0;
		gameovertable.setVisible(false);
		gover.setOrigin(stage.getHeight()*0.28f/2f,stage.getHeight()*0.28f/2f);
    	gover.addAction(forever(sequence(rotateTo(-30,1.5f, sine),rotateTo(30,1.5f, sine))));
		stage.addActor(gameovertable);
		gameovertable.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(1,0.5f)));
	}
	
	public void update(float delta){
		if(gameover && !progress.win)
			world.step(delta* Interpolation.pow2In.apply(0.5f, 0, MathUtils.clamp(gameOverTimer / 3f, 0.01f, 1)), VELOCITYITERATIONS, POSITIONITERATIONS);
		else
			world.step(delta*timeScale, VELOCITYITERATIONS, POSITIONITERATIONS);
    	
    	progress.addtime(0, (int) (5*mole.speedup));
    	//Score
		if(mainui.isVisible())progress.update();
    	if (!gameover){
    		mole.auto=false;
    		mole.updateMole(world, mole, delta); 
    	}
    	else if(!progress.win) {
    		mole.updateMole(world, mole, delta* Interpolation.pow2In.apply(0.5f, 0, MathUtils.clamp(gameOverTimer / 1f, 0.01f, 1))); 
    	}
    	else{
    		mole.updateMole(world, mole, delta); 
    	}
    	
    	limitactor.setText(progress.limitchar);
    	limitactor.pack();
    	limitactor.setPosition(stage.getWidth()/2, 2*pad+limitactor.getHeight()/2, Align.center);
		if(!progress.win)
			completion.setText(progress.progresschar);
		else completion.setText("Score : " +Integer.toString(progress.score));
    	
    	
    	//Adjust Camera 
		float shakeX = 0,shakeY = 0;
    	if(mole.badlanded && shakeTimer<=0){
    		shakeTimer=mole.blpower;
			mole.badlanded=false;
		}
    	if(shakeTimer>0){
    		shakeTimer-=delta;
    		shakeX += 0.25f*mole.blpower * (MathUtils.randomBoolean() ? 1 : -1);
			shakeY += 0.25f*mole.blpower * (MathUtils.randomBoolean() ? 1 : -1);
			camera.position.set(mole.molebody.getPosition().x+shakeX, mole.molebody.getPosition().y+shakeY, 0);
			shakeX += 0.25f*mole.blpower * (MathUtils.randomBoolean() ? 1 : -1);
			shakeY += 0.25f*mole.blpower * (MathUtils.randomBoolean() ? 1 : -1);
			camera.position.set(mole.molebody.getPosition().x+shakeX, mole.molebody.getPosition().y+shakeY, 0);
    	}
		else{
			camera.position.set(mole.molebody.getPosition().x, mole.molebody.getPosition().y, 0);
			if(camera.zoom<1+((mole.vitesse-5)/200)&&camera.zoom<2.3f) camera.zoom+=0.001;
			else if(camera.zoom>=1+((mole.vitesse-5)/200)) camera.zoom-=0.001; //hardly>2.5
		}
		
		camera.update();
		
		camera.position.add(-shakeX, -shakeY, 0);
		shakeX = 0;
		shakeY = 0;
		
		//remove Orb taken
		//updateBonuses();
		updateCoins();
		//Ufo.updateUFOs();
	}

	private void updateCoins() {
		for(int i = 0; i < cl.coins2remove.size; i++) {
			coincolected++;
			Body b = cl.coins2remove.get(i);
			Tween.set(rmvdcoin, SpriteAccessor.ALPHA).target(1).start(tweenManager);
			Tween.set(rmvdcoin, SpriteAccessor.XY).target(b.getPosition().x-0.25f,b.getPosition().y-0.25f).ease(Sine.INOUT).start(tweenManager);
			(Tween.to(rmvdcoin, SpriteAccessor.XY, 0.25f).target(b.getPosition().x-0.25f,b.getPosition().y+2).repeatYoyo(1, 0))
		     .start(tweenManager);
			Tween.to(rmvdcoin, SpriteAccessor.ALPHA, 0.5f).target(0).start(tweenManager);
			coins.removeValue( (Coin) (b.getUserData()), true);
			world.destroyBody(cl.coins2remove.get(i));
		}
		cl.coins2remove.clear();
		
	}
	
	@Override
	public void resize(int width, int height) {
		pausetable.invalidateHierarchy();
		gameovertable.invalidateHierarchy();
	}
	
	@Override
	public void hide() {
		dispose();
		mole.sdig.stop(); mole.sfire.stop(); mole.swind.stop(); mole.sstar.stop();
		music1.stop();musicspace.stop();
	}
	
	@Override
	public void pause() {
		state = State.Paused;
		mole.sdig .stop(); mole.sdigplay  =false;
		mole.sfire.stop(); mole.sfireplay =false;
		mole.swind.stop(); mole.swindplay =false;
		if(!pausetable.isVisible() && !gameover){
			buttonset.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(1,0.5f)));
			pausetable.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(1,0.5f)));
		}
	}
	
	@Override
	public void resume() {
		while(!Assets.manager.update());
	}
	
	@Override
	public void dispose() {
		stage        .dispose();
		skin         .dispose();
		world        .dispose();
		batch        .dispose();
		debugRenderer.dispose();
		mole         .dispose();
		
		
		
	}
	
	public void uiplayanimation(){
		
		fliptxt   .setText(progress.sflipcom);
		heightcom .setText(progress.sheightcom);
		landcom   .setText(progress.landcom);
		holetxt   .setText(progress.holecom);
		
		//animation
		if(progress.upflip && mole.molebody.getPosition().y>0){
			progress.upflip=false;
			flipcom.clearActions();
			flipcom.addAction(sequence(
					moveTo(Gdx.graphics.getWidth()*1/4, Gdx.graphics.getHeight()*0.66f),
					parallel(rotateTo(0),alpha(0)),
					parallel(rotateTo(22,0.75f),alpha(1,0.75f))
					));
		}
		
		if(progress.upheight && mole.molebody.getPosition().y>0){
			progress.upheight=false;
			heightcom.clearActions();
			heightcom.addAction(sequence(
					parallel(moveTo(Gdx.graphics.getWidth()*3/4, Gdx.graphics.getHeight()*0.622f),alpha(0.2f)),
					parallel(moveTo(Gdx.graphics.getWidth()*3/4, Gdx.graphics.getHeight()*0.66f,0.75f),alpha(1,0.75f))
					));
		}
		
		if(progress.uphole){
			progress.uphole=false;
			holecom.clearActions();
			holecom.addAction(sequence(
					moveTo(Gdx.graphics.getWidth()*3/4, Gdx.graphics.getHeight()*0.16f),
					parallel(rotateTo(20),alpha(0.5f)),
					parallel(moveTo(Gdx.graphics.getWidth()*3/4, Gdx.graphics.getHeight()*0.23f,0.3f,pow2Out),alpha(1,0.32f)),
					parallel(moveTo(Gdx.graphics.getWidth()*3/4, Gdx.graphics.getHeight()*0.16f,0.3f,pow2In),alpha(0.5f,0.32f))
					));
		}
		if(progress.updatejumpui){
			progress.updatejumpui=false;
			holecom.clearActions();
			holecom.addAction(sequence(
					parallel(moveTo(45,Gdx.graphics.getHeight()-50,0.75f),alpha(0,0.75f))
					));
		}
		
		if( progress.upscore==1 && mole.molebody.getPosition().y<0){
			progress.upscore=0;
			flipcom.addAction(sequence(
					parallel(moveTo(45,Gdx.graphics.getHeight()-50,0.75f),alpha(0,0.75f))
					));
			heightcom.addAction(sequence(
					parallel(moveTo(45,Gdx.graphics.getHeight()-50,0.75f),alpha(0,0.75f))
					));
			landcom.addAction(sequence(
					parallel(alpha(0),moveTo(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()*0.16f)),
					alpha(1,0.1f),delay(1),alpha(0,0.75f)
					));
		}
		else if( progress.upscore==2 && mole.molebody.getPosition().y<0){
			progress.upscore=0;
			flipcom.addAction(sequence(
					parallel(moveTo(Gdx.graphics.getWidth()*1/4,0,0.75f),alpha(0,0.75f))
					));
			heightcom.addAction(sequence(
					parallel(moveTo(45,Gdx.graphics.getHeight()-50,0.75f),alpha(0,0.75f))
					));
		}
	}
	
	private boolean onScreen(Sprite image){
		if(true && camera.position.x-camera.viewportWidth*camera.zoom/2<(image.getX()+image.getWidth())
				&& camera.position.x+camera.viewportWidth*camera.zoom/2>(image.getX())
				&& camera.position.y-camera.viewportHeight*camera.zoom/2<(image.getY()+image.getHeight()) 
				&& camera.position.y+camera.viewportHeight*camera.zoom/2>(image.getY()))
			 return true;
		else return false;
	}

}