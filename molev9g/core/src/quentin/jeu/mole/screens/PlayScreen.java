package quentin.jeu.mole.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.math.Interpolation.*;

import java.util.Locale;

import quentin.jeu.mole.MoleGame;
import quentin.jeu.mole.entities.Bonus;
import quentin.jeu.mole.entities.Coin;
import quentin.jeu.mole.entities.Ground;
import quentin.jeu.mole.entities.Mole;
import quentin.jeu.mole.entities.Rock;
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
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
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


public class PlayScreen implements Screen {
	
	private World world;
	private  final float pad=Gdx.graphics.getHeight()/36f;
	private int lvl, type, objectif, limit, limittype,score1,score2;
	private boolean stared1,stared2, stared3;
	private float staredtimer;

	public Mole mole;
	
	private MyContactlistener cl;
	private MyInputProc in;
	
	private Array<Bonus> bonuses;
	private Array<Rock> rocks;
	private float bonusalt, bonusup, bonusxrange;
	private Array<Coin> coins;
	private float coinalt;
	//private Array<Ufo> ufos;
	
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	
	private TweenManager tweenManager;
	
	private InputMultiplexer multiplexer;
	public Image buttonmusic,buttonfx, buttonset;
	
	private SpriteBatch batch;
	
	private final  int numDrawX = 20;
	private final  float lvllenght = 300;
	private int leveloffset=0;
	private Background bg4, bg7, bg8, bg5, bg6, bg2, bg1;
	private Background cloud1bg, cloud2bg, cloud3bg,bg3,  grass1bg,  grass2bg;
	private Background grass3bg, groundjointbg ,ground0bg ,ugroundjoint1bg, ground1bg;
	private Background ugroundjoint2bg ,ground2bg ,ugroundjoint3bg,ground3bg;
	private Background sky0bg, upskybg, hcloudbg;
	private Background asteroid1bg, asteroid2bg, asteroidbbg, asteroidbbg2;
	
	private Sprite rmvdgld, rmvdslv,rmvdbrz,rmvdt, rmvdjp,rmvddp, rmvdcoin;
	private MyParticleEffect windeffect, windeffect1,nebulaeffect1;
	
	//private RandomBg stars1, stars2, stars3; 
	private int staroffset1, staroffset2,staroffset3,staroffset4;
	private Sprite backsky,rainbow, moon, mars,jupiter,saturne,uranus,neptune, nebuleuse;

	private final  int VELOCITYITERATIONS = 8, POSITIONITERATIONS = 3;
	private  final int JPPRICE=11, PLANEPRICE=11, TIMEPRICE=11;
	public   enum State{Running, Paused}
	private State state = State.Running;

	private ProgressDisp progress;
	
	private Stage stage;
	private Skin skin;
	private Table gameovertable, pausetable;
	private Group mainui,powers,flipcom, holecom; // can't rotate a label
	private Group bonusMenu, lifemenu, quittable;
	private Label fliptxt, heightcom, landcom, holetxt, bonuscom;
	private Label completion, limitactor, top2line;
	private Image star1, star2, star3;
	
	private Label coinsbuymenu, coinsbonusmenu, coinslifemenu, coinsGOtable;
	private   boolean gameover = false;
	
	private Music music1, musicspace;
	private float timeScale=1;
	private float shakeTimer, animtimer,gameOverTimer;
	private boolean animup;
	private Image timetimer,timebutton,planetimer,planebutton,jptimer,jpbutton,black;
	private Label jpnumber;
	private Ground ground0b,ground1b,ground2b,ground3b,ground4b;
	public int coincolected=0;
	private  final int CONTINUEPRICE=18;
	
	public PlayScreen(int level, int type, int objectif, int limittype, int limit, int score1, int score2){
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
		
		if(type!=MoleGame.ARCADELVL)
			Gdx.input.setCatchBackKey(true);
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
		float ratiopla;
		moon    =   new Sprite(Assets.manager.get(Assets.moon,      Texture.class));
		moon     .setSize(10, 10);
		mars    =   new Sprite(Assets.manager.get(Assets.mars,      Texture.class));
		mars     .setSize(10, 10);
		jupiter =   new Sprite(Assets.manager.get(Assets.jupiter,   Texture.class));
		jupiter  .setSize(30, 30);
		saturne =   new Sprite(Assets.manager.get(Assets.saturne,   Texture.class));
		ratiopla=saturne.getHeight()/saturne.getWidth();
		saturne  .setSize(25, 25*ratiopla);
		saturne.setOriginCenter();
		uranus  =   new Sprite(Assets.manager.get(Assets.uranus,    Texture.class));
		ratiopla=uranus.getHeight()/uranus.getWidth();
		uranus   .setSize(10, 10*ratiopla);
		neptune =   new Sprite(Assets.manager.get(Assets.neptune,   Texture.class));
		neptune  .setSize(10, 10);
		nebuleuse = new Sprite(Assets.manager.get(Assets.nebula, Texture.class));
		nebuleuse.setSize(55, 35);
		
		
//		stars1 = new RandomBg(star1, 10, 4,  60,  211,    1f,     0, camera, 10);
//		stars2 = new RandomBg(star2, 10, 5, 210, 1000,    1f,     0, camera, 10);
//		stars3 = new RandomBg(star3,  7, 4, 150, 1000, 1.05f,0, camera, 40);
			
		final Sprite asteroidb  = new Sprite(Assets.manager.get(Assets.asterback,  Texture.class));
		final Sprite asteroidb2 = new Sprite(Assets.manager.get(Assets.asterback,  Texture.class));
		final Sprite asteroid1 = new Sprite(Assets.manager.get(Assets.asteroid2, Texture.class));
		final Sprite asteroid2 = new Sprite(Assets.manager.get(Assets.asteroid1, Texture.class));
		asteroidb.setSize(15, 15);
		asteroidb2.setSize(12, 12);
		asteroid1.setSize(10, 10);
		asteroid2.setSize(9, 9);
		
		asteroidbbg = new Background(tweenManager,asteroidb, Background.NONE,  camera , 0.8f,  9.7f ,1);
		asteroidbbg2 = new Background(tweenManager,asteroidb2, Background.NONE,  camera, 0.8f,11 ,1
				,MathUtils.random(0.4f, 0.9f));
		asteroid1bg = new Background(tweenManager,asteroid1,  Background.NONE, camera, 0.3f, 15.2f ,3);
		asteroid2bg = new Background(tweenManager,asteroid2,  Background.NONE, camera,
				0.3f, 15.6f ,4 ,MathUtils.random(0.4f, 0.9f));
		
		//static bgs
		float[] color0 = null, color1=null;
		if     (lvl<=21 || lvl==MoleGame.ARCADE1 || lvl==MoleGame.OLDARCADE){
			color0 = new float[]{0.9f, 0.6f, 0.3f};
			color1 = new float[]{1f, 1f,1f};
			final Sprite grass1       = new Sprite(Assets.manager.get(Assets.grass1,  Texture.class));
			final Sprite grass2       = new Sprite(Assets.manager.get(Assets.grass2,  Texture.class));
			final Sprite grass3       = new Sprite(Assets.manager.get(Assets.grass3,  Texture.class));
			grass1       .setSize(15*1.15f, 1.5f*1.15f);
			grass2       .setSize(15*1.15f, 1.5f*1.15f);
			grass3       .setSize(15*1.15f, 1.5f*1.15f);
			grass1bg =        new Background(tweenManager,grass1,        Background.WIND3, camera,      0f,1);
			grass2bg =        new Background(tweenManager,grass2,        Background.WIND3, camera,      0f,1);
			grass3bg =        new Background(tweenManager,grass3,        Background.WIND3, camera,      0f,1);
			rainbow  =   new Sprite(Assets.manager.get(Assets.rainbow,   Texture.class));
			rainbow  .setSize(38, 18);
		}
		else if(lvl<=41 || lvl==MoleGame.ARCADE2 || lvl==MoleGame.ARCADEPLANE){
			color0= color1 = new float[]{0.91f, 0.35f, 0.f};
			final Sprite grass1       = new Sprite(Assets.manager.get(Assets.aloe,Texture.class));
			grass1       .setSize(15*1.15f, 1.5f*1.15f);
			grass1bg =        new Background(tweenManager,grass1,        Background.WIND3, camera,      0f,1);
		}
		else {
			color0 = color1 = new float[]{1f, 1f,1f};
		}
		
		
		final Sprite sky0        = new Sprite(Assets.manager.get(Assets.sky0,   Texture.class));
		final Sprite upsky       = new Sprite(Assets.manager.get(Assets.upsky,  Texture.class));
		final Sprite ground0     = new Sprite(Assets.manager.get(Assets.ground0,Texture.class));
		ground0.setColor(color0[0],color0[1],color0[2],1);
		final Sprite ground1     = new Sprite(Assets.manager.get(Assets.ground1,Texture.class));
		ground1.setColor(color1[0],color1[1],color1[2],1);
		final Sprite ground2     = new Sprite(Assets.manager.get(Assets.ground2,Texture.class));
		final Sprite ground3     = new Sprite(Assets.manager.get(Assets.ground3,Texture.class));
		final Sprite hcloud      = new Sprite(Assets.manager.get(Assets.hcloud, Texture.class));
		final Sprite groundjoint   = new Sprite(Assets.manager.get(Assets.ugroundjoint0,  Texture.class));
		groundjoint.setColor(color0[0],color0[1],color0[2],1);
		final Sprite ugroundjoint1 = new Sprite(Assets.manager.get(Assets.ugroundjoint1,Texture.class));
		ugroundjoint1.setColor(color1[0],color1[1],color1[2],1);
		final Sprite ugroundjoint2 = new Sprite(Assets.manager.get(Assets.ugroundjoint2,Texture.class));
		final Sprite ugroundjoint3 = new Sprite(Assets.manager.get(Assets.ugroundjoint3,Texture.class));
		
		
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
		if(lvl<=21 || lvl==MoleGame.ARCADE1 || lvl==MoleGame.OLDARCADE){
			final Sprite hills0 =  new Sprite(Assets.manager.get(Assets.hills0, Texture.class));
			final Sprite hills1 =  new Sprite(Assets.manager.get(Assets.hills1, Texture.class));
			final Sprite fhills0 = new Sprite(Assets.manager.get(Assets.fhills0,Texture.class));
			final Sprite tree0 =   new Sprite(Assets.manager.get(Assets.tree0,  Texture.class));
			final Sprite tree1 =   new Sprite(Assets.manager.get(Assets.tree1,  Texture.class));
			final Sprite tree2 =   new Sprite(Assets.manager.get(Assets.tree1,  Texture.class));
			final Sprite tree3 =   new Sprite(Assets.manager.get(Assets.tree0 , Texture.class));
			final Sprite rock =    new Sprite(Assets.manager.get(Assets.rock,   Texture.class));
			final Sprite cloud =   new Sprite(Assets.manager.get(Assets.cloud,  Texture.class));
			hills0   .setSize(w/1.5f*1.1f, h*1.1f);
			hills1   .setSize(w/1.5f*1.1f, h*1.1f);
			fhills0  .setSize(w/2*1.5f, 1.5f*1.5f);
			tree0.setSize(w/4*1.5f, w/4*1.5f);
			tree1.setSize(w/5*1.5f, w/5*1.5f);
			tree2.setSize(w/10*1.5f,w/10*1.5f);
			tree3.setSize(w/14*1.5f,w/14*1.5f);
			rock  .setSize(w/7*1.2f, h/5*1.2f);
			cloud .setSize(w/3*1.5f, h/2*1.5f);
			
			bg1 =   new Background(tweenManager,rock,   Background.NONE,  camera, 0.1f, -0.1f ,8);
			bg2 =   new Background(tweenManager,tree0,  Background.WIND1, camera, 0.2f, -0.1f ,3);
			bg3 =   new Background(tweenManager,tree1,  Background.WIND1, camera, 0.3f, -0.1f ,2);
			bg4 =   new Background(tweenManager,fhills0,Background.WIND2, camera, 0.6f, -0.15f,1);
			bg5 =   new Background(tweenManager,tree2,  Background.WIND1, camera, 0.6f,  0.0f ,3);
			bg6 =   new Background(tweenManager,tree3,  Background.WIND1, camera, 0.7f,  0.0f ,4);
			bg7 =   new Background(tweenManager,hills0, Background.NONE,  camera, 0.8f, -0.15f,2);
			bg8 =   new Background(tweenManager,hills1, Background.NONE,  camera, 0.8f, -0.15f,2,0.995f);
			cloud1bg =  new Background(tweenManager,cloud,  Background.NONE,  camera, 1.5f,  0.5f ,4);
			cloud2bg =  new Background(tweenManager,cloud,  Background.NONE,  camera, 1.3f,  1.0f ,2);
			cloud3bg =  new Background(tweenManager,cloud,  Background.NONE,  camera, 1.1f,  1.8f ,4);
			
			//wind effect
			windeffect1 = new MyParticleEffect();
			windeffect1.loadEmitters(Gdx.files.internal("effect/wind.p"), false); 
			Texture saku = Assets.manager.get(Assets.saku,Texture.class);
			windeffect1.getEmitters().get(0).setcamera(camera);
			windeffect1.loadEmitterImages(saku);////////////!!!!!!
			windeffect1.scaleEffect(windeffect1, 0, 0.45f);
			windeffect1.ScaleParticles(windeffect1, 0, 0.65f);
			windeffect1.start();
			
			windeffect = new MyParticleEffect();
			windeffect.loadEmitters(Gdx.files.internal("effect/wind.p"), false); 
			Texture leaf = Assets.manager.get(Assets.leaf,Texture.class);
			windeffect.getEmitters().get(0).setcamera(camera);
			windeffect.loadEmitterImages(leaf);////////////!!!!!!
			windeffect.scaleEffect(windeffect, 0, 0.45f);
			windeffect.ScaleParticles(windeffect, 0, 0.65f);
			windeffect.start();
			
		}
		else if(lvl<=41 || lvl==MoleGame.ARCADE2 || lvl==MoleGame.ARCADEPLANE){
			//paralax bgs
			final Sprite monument0  = new Sprite(Assets.manager.get(Assets.monument0,Texture.class));
			final Sprite monument1  = new Sprite(Assets.manager.get(Assets.monument1,Texture.class));
			final Sprite monumentbck= new Sprite(Assets.manager.get(Assets.monmtbck,Texture.class));
			final Sprite monumentf  = new Sprite(Assets.manager.get(Assets.monumentf,Texture.class));
			final Sprite dedtree    = new Sprite(Assets.manager.get(Assets.dedtree,Texture.class));
			final Sprite tree       = new Sprite(Assets.manager.get(Assets.treev,Texture.class));
			
			tree.setSize(w/6*1.5f, w/8*1.5f);
			monument0.setSize(w/1.5f*1.5f, h*1.5f);
			monument1.setSize(w/1.5f*1.5f, h*1.5f);
			monumentf.setSize(w/2f*1.5f, 1.5f*1.5f);
			monumentbck.setSize(w/2*1.5f, h*1.5f);
			dedtree.setSize(w/4*1.5f, w/8*1.5f);
			
			bg6 = new Background(tweenManager,monumentbck, Background.NONE , camera, 0.85f,-0.08f, 1);
			bg5 = new Background(tweenManager,monument0  , Background.NONE , camera, 0.65f,-0.08f, 2);
			bg4 = new Background(tweenManager,monument1  , Background.NONE , camera, 0.65f,-0.08f, 2);
			bg3 = new Background(tweenManager,monumentf  , Background.WIND2, camera, 0.5f ,-0.20f,1);
			bg2 = new Background(tweenManager,dedtree    , Background.NONE , camera, 0.3f ,-0.03f,5);
			bg1 = new Background(tweenManager,tree       , Background.WIND1, camera, 0.2f ,-0.08f ,7);
		}
		else{
			final Sprite city = new Sprite(Assets.manager.get(Assets.city,Texture.class));
			final Sprite b1b = new Sprite(Assets.manager.get(Assets.build1bot,Texture.class));
			final Sprite b1t = new Sprite(Assets.manager.get(Assets.build1top,Texture.class));
			final Sprite b2 = new Sprite(Assets.manager.get(Assets.build2,Texture.class));
			final Sprite r1 = new Sprite(Assets.manager.get(Assets.road1,Texture.class));
			final Sprite r2 = new Sprite(Assets.manager.get(Assets.road2,Texture.class));
			final Sprite cloud =   new Sprite(Assets.manager.get(Assets.cloud,  Texture.class));
			
			city.setSize(w/1.5f*1.5f, h/1.f*1.5f);
			b1b.setSize(w/7*1.5f, w/3*1.5f);
			b1t.setSize(w/7*1.5f, w/3*1.5f);
			r1.setSize(w/3*1.5f, h/1.5f*1.5f);
			r2.setSize(w/3*1.5f, h/1.5f*1.5f);
			b2.setSize(w/8*1.5f, w/4*1.5f);
			
			bg6  = new Background(tweenManager,city, Background.NONE, camera, 0.85f, -0.1f, 1);
			bg5  = new Background(tweenManager,b1b , Background.NONE, camera, 0.45f,   -0f, 5);
			bg4  = new Background(tweenManager,b1t , Background.NONE, camera, 0.45f,    1f, 5);
			bg3  = new Background(tweenManager,b2  , Background.NONE, camera, 0.65f,  -0.f, 3);
			bg2  = new Background(tweenManager,r1  , Background.NONE, camera, 0    ,-0.05f, 1);
			bg1  = new Background(tweenManager,r2  , Background.NONE, camera, 0    ,-0.05f, 2);
			cloud1bg =  new Background(tweenManager,cloud,  Background.NONE,  camera, 1.5f,  0.5f ,4);
			cloud2bg =  new Background(tweenManager,cloud,  Background.NONE,  camera, 1.3f,  1.0f ,2);
			cloud3bg =  new Background(tweenManager,cloud,  Background.NONE,  camera, 1.1f,  1.8f ,4);
		}
		
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
		
		
		///////============================================Create bonuses
		bonusalt=0;
		bonusup=5;
		bonusxrange=lvllenght-84;
		if(lvl==MoleGame.OLDARCADE)
			createbonuses1(70, -9,100);
		else if(type==MoleGame.ARCADELVL && lvl!=MoleGame.ARCADEPLANE){
			createbonuses1(35, -9,100);
			if(Save.gd.ujp>=1)
				createbonuses2(50,3, 7,50);
			else if(Save.gd.uplane>=1)
				createbonuses2(47,2, 7,50);
			else if(Save.gd.utime>=1)
				createbonuses2(42,1, 7,50);
		}
		else if(lvl==MoleGame.ARCADEPLANE){
			createbonuses2(80,4, 5,50);
		}
		else if(lvl==MoleGame.ARCADEJP){
			createbonuses2(80,6, 10,100);
		}
		else{
			if(Save.gd.ujp>=1)
				createbonuses2(32,3, 10,70);
			else if(Save.gd.uplane>=1)
				createbonuses2(25,2, 7,50);
			else if(Save.gd.utime>=1)
				createbonuses2(18,1, 5,30);
			createCoins(10, 1,8f);
		}
		//other objects
		//ufos      = Ufo.createufo(world);		
		if(lvl>=60 && lvl<70)	{
			createrock(6,color0, 1, -12,10);
		}
		
		//GROUNDS
		final Vector2[] groundchain = new Vector2[]{new Vector2(-300,0), new Vector2(300,0)};
		
		//Create mole and set contact listener to it
		cl = new MyContactlistener(lvl);
		in = new MyInputProc();
		
		mole = new Mole(world,in, cl, color0, color1, lvl);
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
				if(camera.zoom<2.4f && camera.zoom>1)camera.zoom += amount / 25f;
				System.out.println("zoom : " +Float.toString(camera.zoom));
				return true;
			}
		};
		multiplexer.addProcessor(zoom);
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(in);
		Gdx.input.setInputProcessor(multiplexer);	
		
  }
	
	
	private void showquit() {
		state = State.Paused;
		quittable.clear();
		mainui.addAction(sequence(alpha(0,0.1f,fade), hide2()));
		Label exit = new Label(MoleGame.myBundle.get("quit"), skin, "big");
		Group buttonyes = new Group();
		Image buttonrazbg = new Image(Assets.manager.get(Assets.gover, Texture.class));
		buttonrazbg.setSize(stage.getHeight()/6.35f,stage.getHeight()/6.35f);
		buttonrazbg.setOrigin(Align.center);
		Image life1 = new Image(Assets.manager.get(Assets.life1, Texture.class));
		Image life2 = new Image(Assets.manager.get(Assets.life2, Texture.class));
		life1.setSize(stage.getHeight()/10.35f, stage.getHeight()/10.35f);
		life2.setSize(stage.getHeight()/10.35f, stage.getHeight()/10.35f);
		life1.setPosition(buttonrazbg.getWidth()/2-life1.getWidth()/2, buttonrazbg.getWidth()/2-life1.getWidth()/2);
		life2.setPosition(buttonrazbg.getWidth()/2-life1.getWidth()/2, buttonrazbg.getWidth()/2-life1.getWidth()/2);
		life1.setOrigin(life1.getWidth()/2, life1.getHeight()*0.14f);
		life2.setOrigin(life2.getWidth()/2, life2.getHeight()*0.14f);
		life1.addAction(forever(sequence(
				delay(0.7f),
				parallel(rotateTo(15,1,pow2In),alpha(0,1.2f,pow5In)),
				delay(0.5f),
				parallel(rotateTo(0),alpha(1,0.35f,pow2Out))
				)));
		life2.addAction(forever(sequence(
				delay(0.7f),
				parallel(rotateTo(-15,1,pow2In),alpha(0,1.2f,pow5In)),
				delay(0.5f),
				parallel(rotateTo(0),alpha(1,0.35f,pow2Out))
				)));
		buttonyes.addActor(buttonrazbg);
		buttonyes.addActor(life1);
		buttonyes.addActor(life2);
		
		buttonyes.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		
		Image buttonNo = new Image(Assets.manager.get(Assets.gover2, Texture.class));
		buttonNo.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				state = State.Running;
				quittable.addAction(sequence(alpha(0,0.05f,fade),removeActor()));
				mainui.addAction(sequence(show2(),alpha(1,0.05f,fade)));
			}
		});
		
		exit.setAlignment(Align.center);
		exit.setPosition(stage.getWidth()/2-exit.getWidth()/2, stage.getHeight()/2+pad);
		buttonyes.setSize(stage.getHeight()/6.35f,stage.getHeight()/6.35f);
		buttonyes.setPosition(stage.getWidth()/2-buttonyes.getWidth()-2*pad, stage.getHeight()/2-buttonyes.getHeight()-pad);
		buttonNo.setSize(stage.getHeight()/6.35f,stage.getHeight()/6.35f);
		buttonNo.setPosition(stage.getWidth()/2+2*pad, stage.getHeight()/2-buttonNo.getHeight()-pad);
		buttonyes.setOrigin(buttonyes.getHeight()/2f,buttonyes.getHeight()/2f);
		buttonyes.addAction(forever(sequence(rotateTo(-30,0.75f, sine),rotateTo(30,0.75f, sine))));
		buttonNo.addAction(forever(sequence(
    			moveTo(stage.getWidth()/2+2*pad, stage.getHeight()/2-buttonNo.getHeight()    ,0.3f,pow2Out),
				moveTo(stage.getWidth()/2+2*pad, stage.getHeight()/2-buttonNo.getHeight()-pad,0.3f,pow2In)
    			)));
		
		quittable.addActor(exit);
		quittable.addActor(buttonyes);
		quittable.addActor(buttonNo);
		quittable.getColor().a=0;
		quittable.addAction(alpha(1,0.2f));
		stage.addActor(quittable);
	}
	
	private void createCoins(int coinsgoupnbr, float altitudeMin, float altitudeMax) {
		coins = new Array<Coin>();
		float coinposx, coinposy, grpposx,grpposy;
		FixtureDef coinfdef = new FixtureDef();
		CircleShape cshape = new CircleShape();
		cshape.setRadius(0.25f);
		coinfdef.shape = cshape;
		coinfdef.isSensor = true;
		BodyDef coindef = new BodyDef();
		coindef.type = BodyType.StaticBody;
		float xgp=0;
		float ygp=0;	
		for(int i=0;i<coinsgoupnbr;i++) {
			int grptype=MathUtils.random(3);
			grpposx = -lvllenght/2+MathUtils.random(0, 10)+42+xgp*(lvllenght-89)/coinsgoupnbr;
			grpposy=altitudeMax*MathUtils.random()+altitudeMin + ygp;
			xgp+=2;
			if(i==5){
				xgp=0.5f;
				ygp=12;
			}
			if(grptype==0){ //// square 4x4=16
				float geox=0,geoy=0;
				for(int j=0;j<16;j++){
					if(geox==4) {
						geox=0;
						geoy++;
					}
					geox++;
					coinposx=grpposx+geox*0.75f;
					coinposy=grpposy+geoy*0.75f;
					coindef.position.set(coinposx, coinposy);
					Body body = world.createBody(coindef);
					body.createFixture(coinfdef).setUserData("coin");
					Coin coin = new Coin(body);
					body.setUserData(coin);
					coins.add(coin);
				}
			}
			if(grptype==1){ //// triangle=15
				float geox=0,geoy=0;
				for(int j=0;j<15;j++){
					if(j==5) {
						geox=0.75f/2  +0.75f*1/5;
						geoy++;
					}
					if(j==9) {
						geox=0.75f    +0.75f*2/5;
						geoy++;
					}
					if(j==12) {
						geox=0.75f*3/2+0.75f*3/5;
						geoy++;
					}
					if(j==14) {
						geox=0.75f*2  +0.75f*4/5;
						geoy++;
					}
					geox++;
					coinposx=grpposx+geox*0.75f;
					coinposy=grpposy+geoy*0.75f;
					coindef.position.set(coinposx, coinposy);
					Body body = world.createBody(coindef);
					body.createFixture(coinfdef).setUserData("coin");
					Coin coin = new Coin(body);
					body.setUserData(coin);
					coins.add(coin);
				}
			}
			if(grptype==2){ //// triangle reverse=15
				float geox=0,geoy=0;
				for(int j=0;j<15;j++){
					if(j==1) {
						geox=-(0.75f/2  +0.75f*1/5);
						geoy++;
					}
					if(j==3) {
						geox=-(0.75f    +0.75f*2/5);
						geoy++;
					}
					if(j==6) {
						geox=-(0.75f*3/2+0.75f*3/5);
						geoy++;
					}
					if(j==10) {
						geox=-(0.75f*2  +0.75f*4/5);
						geoy++;
					}
					geox++;
					coinposx=grpposx+geox*0.75f;
					coinposy=grpposy+geoy*0.75f;
					coindef.position.set(coinposx, coinposy);
					Body body = world.createBody(coindef);
					body.createFixture(coinfdef).setUserData("coin");
					Coin coin = new Coin(body);
					body.setUserData(coin);
					coins.add(coin);
				}
			}
			if(grptype==3){ //// M=17
				float geox=0,geoy=0;
				for(int j=0;j<19;j++){
					if(j<=6) {
						geox=0;
						geoy++;
					}
					else if(j<=9) {
						geox+=0.75f;
						geoy--;
					}
					else if(j<=12) {
						geox+=0.75f;
						geoy++;
					}
					else {
						geoy--;
					}
					coinposx=grpposx+geox*0.75f;
					coinposy=grpposy+geoy*0.75f;
					coindef.position.set(coinposx, coinposy);
					Body body = world.createBody(coindef);
					body.createFixture(coinfdef).setUserData("coin");
					Coin coin = new Coin(body);
					body.setUserData(coin);
					coins.add(coin);
				}
			}
			/*if(grptype==4){ //// star=25
				float geox=0,geoy=0;
				for(int j=0;j<13;j++){
					if(j==1) {
						geox=-(0.75f/2  +0.75f*1/7);
						geoy++;
					}
					if(j==3) {
						geox=-2;
						geoy++;
					}
					if(j==8) {
						geox=-3;
						geoy++;
					}
					if(j==12) {
						geox=0;
						geoy++;
					}
					coinposx=grpposx+geox*0.75f;
					coinposy=grpposy+geoy*0.75f;
					coindef.position.set(coinposx, coinposy);
					Body body = world.createBody(coindef);
					body.createFixture(coinfdef).setUserData("coin");
					Coin coin = new Coin(body);
					body.setUserData(coin);
					coins.add(coin);
				}
			}*/
		}
		cshape.dispose();
	}
	
	private void createbonuses2(int bstnbr, int type, float altitudeMin, int altitudeMax) {
		if(bonuses==null)bonuses = new Array<Bonus>();
		float bonusposx, bonusposy;
		float espace = (lvllenght-84)/bstnbr;
		for(int i=0;i<bstnbr;i++) {
			bonusposx = -lvllenght/2+MathUtils.random(-espace/2, espace/2)+42+espace*i;
			bonusposy=altitudeMax*MathUtils.random()+altitudeMin;
			BodyDef bonusdef = new BodyDef();
			bonusdef.type = BodyType.StaticBody;
			bonusdef.position.set(bonusposx, bonusposy);
			Body body = world.createBody(bonusdef);
			FixtureDef bonusfdef = new FixtureDef();
			CircleShape cshape = new CircleShape();
			cshape.setRadius(0.5f);
			bonusfdef.shape = cshape;
			bonusfdef.isSensor = true;
			body.createFixture(bonusfdef).setUserData("bonus");
			 //Old arcade : equi-repartition des 3 bonus de base
			Bonus bonus = null;
			if(type==1)
				bonus = new Bonus(body, 4);
			else if(type==2)
				bonus = new Bonus(body, MathUtils.random(4,5));
			else if(type==3)
				bonus = new Bonus(body, MathUtils.random(4,6));
			else if(type==4)
				bonus = new Bonus(body, MathUtils.random(5,6));
			else if(type==5)
				bonus = new Bonus(body, 5);
			else if(type==6)
				bonus = new Bonus(body, 6);
			body.setUserData(bonus);
			bonuses.add(bonus);
			cshape.dispose();
		}
		
	}
	
	private void createrock(int rocknbr,float[]color, int type, float altitudeMin, int altitudeMax) {
		if(rocks==null)rocks = new Array<Rock>();
		float bonusposx, bonusposy;
		float espace = (lvllenght-84)/rocknbr;
		for(int i=0;i<rocknbr;i++) {
			bonusposx = -lvllenght/2+MathUtils.random(-espace/2, espace/2)+42+espace*i;
			bonusposy=altitudeMax*MathUtils.random()+altitudeMin;
			BodyDef bonusdef = new BodyDef();
			bonusdef.type = BodyType.StaticBody;
			bonusdef.position.set(bonusposx, bonusposy);
			Body body = world.createBody(bonusdef);
			FixtureDef bonusfdef = new FixtureDef();
			CircleShape cshape = new CircleShape();
			cshape.setRadius(MathUtils.random(1, 1.5f));
			bonusfdef.shape = cshape;
			//bonusfdef.isSensor = true;
			body.createFixture(bonusfdef).setUserData("underground3");
			Rock rock = new Rock(body,color, 1);
			body.setUserData(rock);
			rocks.add(rock);
			cshape.dispose();
		}
		
	}

	
	private void createbonuses1(int bstnbr, float altitudeMin, int altitudeMax) {
		bonuses = new Array<Bonus>();
		float bonusposx, bonusposy;
		float espace = (lvllenght-84)/bstnbr;
		for(int i=0;i<bstnbr;i++) {
			bonusposx = -lvllenght/2+42+espace*i;
			bonusposy=altitudeMax*MathUtils.random()+altitudeMin;
			BodyDef bonusdef = new BodyDef();
			bonusdef.type = BodyType.StaticBody;
			bonusdef.position.set(bonusposx, bonusposy);
			Body body = world.createBody(bonusdef);
			FixtureDef bonusfdef = new FixtureDef();
			CircleShape cshape = new CircleShape();
			cshape.setRadius(0.5f);
			bonusfdef.shape = cshape;
			bonusfdef.isSensor = true;
			body.createFixture(bonusfdef).setUserData("bonus");
			 //Old arcade : equi-repartition des 3 bonus de base
			Bonus bonus;
			if(lvl==MoleGame.OLDARCADE)
				bonus= new Bonus(body, MathUtils.random(1,3));
			else
				bonus= new Bonus(body, MathUtils.random(2,3));
			body.setUserData(bonus);
			bonuses.add(bonus);
			cshape.dispose();
		}
		
	}
	
	
	private void createUI() {
		stage=new Stage();
		/////////////// QUIT TABLE //////////////////
		quittable=new Group();
		
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
		if((Save.gd.planebought==0 || type==MoleGame.ARCADELVL) && lvl!=MoleGame.ARCADEPLANE){
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
		if((Save.gd.jpbought==0 || type==MoleGame.ARCADELVL) && lvl!=MoleGame.ARCADEPLANE){
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
		
		//mainui.addActor(completion);
		mainui.addActor(buttonPause);
		mainui.addActor(top2line);
		mainui.addActor(limitactor);
		
	  	
		//////////////////////////////////////////////PAUSE TABLE    ///////////////////////////////////////
		
    	Image pause =new Image(Assets.manager.get(Assets.pause, Texture.class));
    	pause.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				state = State.Running;
				buttonset.addAction(sequence(alpha(0,0.5f),hide2()));
				pausetable.addAction(sequence(alpha(0,0.5f),hide2()));
			}
		});
    	Image buttonresume = new Image(Assets.manager.get(Assets.play, Texture.class));
		buttonresume.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				state = State.Running;
				buttonset.addAction(sequence(alpha(0,0.5f),hide2()));
				pausetable.addAction(sequence(alpha(0,0.5f),hide2()));
			}
		});
		
		Group buttonrestart = new Group();
		Image buttonrazbg = new Image(Assets.manager.get(Assets.raz, Texture.class));
		buttonrazbg.setSize(stage.getHeight()/6.35f,stage.getHeight()/6.35f);
		buttonrazbg.setOrigin(Align.center);
		buttonrazbg.addAction(forever(sequence(
				delay(0.1f),
				rotateTo(-180,1.1f,pow4In),
				rotateTo(-360,0.9f,pow4Out),
				delay(0.1f),
				rotateTo(0)
				)));
		buttonrestart.addActor(buttonrazbg);
		if(type!=MoleGame.ARCADELVL){
			Image life1 = new Image(Assets.manager.get(Assets.life1, Texture.class));
			Image life2 = new Image(Assets.manager.get(Assets.life2, Texture.class));
			life1.setSize(stage.getHeight()/10.35f, stage.getHeight()/10.35f);
			life2.setSize(stage.getHeight()/10.35f, stage.getHeight()/10.35f);
			life1.setPosition(buttonrazbg.getWidth()/2-life1.getWidth()/2, buttonrazbg.getWidth()/2-life1.getWidth()/2);
			life2.setPosition(buttonrazbg.getWidth()/2-life1.getWidth()/2, buttonrazbg.getWidth()/2-life1.getWidth()/2);
			life1.setOrigin(life1.getWidth()/2, life1.getHeight()*0.14f);
			life2.setOrigin(life2.getWidth()/2, life2.getHeight()*0.14f);
			life1.addAction(forever(sequence(
					delay(0.7f),
					parallel(rotateTo(15,1,pow2In),alpha(0,1.2f,pow5In)),
					delay(0.5f),
					parallel(rotateTo(0),alpha(1,0.35f,pow2Out))
					)));
			life2.addAction(forever(sequence(
					delay(0.7f),
					parallel(rotateTo(-15,1,pow2In),alpha(0,1.2f,pow5In)),
					delay(0.5f),
					parallel(rotateTo(0),alpha(1,0.35f,pow2Out))
					)));
			buttonrestart.addActor(life1);
			buttonrestart.addActor(life2);
		}
		buttonrestart.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(type!=MoleGame.ARCADELVL){
					if(Save.gd.lives>0){
						if(Save.gd.utime==0){
							Save.gd.lives-=1;
							Save.save();
							gameovertable.addAction(sequence(delay(0.25f),alpha(0,0.25f)));
							black.addAction(sequence(show2(), fadeIn(0.5f),run(new Runnable() {
								@Override
								public void run() {
					((Game) Gdx.app.getApplicationListener()).setScreen(new PlayScreen(lvl,type,objectif,limittype,limit, score1, score2));
								}
							})));
						}
						else{
							bonusmenu();
						}
					}
					else {
						buylives();
					}
					
				}
				else {
					black.addAction(sequence(show2(),
							fadeIn(0.5f),run(new Runnable() {
								@Override
								public void run() {
			((Game) Gdx.app.getApplicationListener()).setScreen(new PlayScreen(lvl,type,objectif,limittype,limit, score1, score2));
						}
					})));
				}
			}
		});
	
		Image buttonmenu = new Image(Assets.manager.get(Assets.menu, Texture.class));
		buttonmenu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.input.setCatchBackKey(false);
				black.addAction(sequence(show2(),
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
		pausetable.add(buttonresume) .size(stage.getHeight()/6.35f).pad(pad);
		pausetable.add(buttonrestart).size(stage.getHeight()/6.35f).pad(pad);
		pausetable.add(buttonmenu)   .size(stage.getHeight()/6.35f).pad(pad).row();
    	pausetable.getColor().a=0;
    	pausetable.setVisible(false);
    	
    	
		
		//////////////////////////////////////////////   GAME OVER TABLE    ///////////////////////////////////////
		gameovertable = new Table(skin);
    	gameovertable.setFillParent(true);
    	coinsbuymenu=new Label("", skin);
    	coinsbonusmenu=new Label("", skin);
    	coinslifemenu=new Label("", skin);
    	coinsGOtable=new Label("", skin);
    	
		stage.addActor(flipcom);
		stage.addActor(heightcom);
		stage.addActor(bonuscom);
		stage.addActor(landcom);
		stage.addActor(holecom);
		stage.addActor(star1);
		stage.addActor(star2);
		stage.addActor(star3);
		stage.addActor(mainui);
		stage.addActor(completion);
		stage.addActor(pausetable);
		createsetb();
		
		black=new Image(skin.newDrawable("white", Color.BLACK));
		black.setSize(stage.getWidth(), stage.getHeight());
		black.addAction(sequence(delay(0.02f),alpha(0,0.25f,fade), hide2()));
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
	
	
	private void createsetb() {
		
		final Table achievtable =new Table();
		final Image achievbg = new Image(Assets.manager.get(Assets.setting, Texture.class));
		
		Image achievB = new Image(Assets.manager.get(Assets.achiev, Texture.class));
		achievB.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//if(Save.gd.sound)click.play();
				if(MoleGame.gservices!=null)MoleGame.gservices.showAchieve();
				achievtable.addAction(sequence(alpha(0,0.3f),hide2()));
				pausetable.addAction(sequence(show2(),alpha(1,0.3f)));
				achievbg.setTouchable(Touchable.disabled);
				}});
		
		Image hsB = new Image(Assets.manager.get(Assets.challenge, Texture.class));
		hsB.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//if(Save.gd.sound)click.play();
				if(MoleGame.gservices!=null)MoleGame.gservices.showScores();
				achievtable.addAction(sequence(alpha(0,0.3f),hide2()));
				pausetable.addAction(sequence(show2(),alpha(1,0.3f)));
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
				achievtable.addAction(sequence(alpha(0,0.3f),hide2()));
				pausetable.addAction(sequence(show2(),alpha(1,0.3f)));
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
				pausetable.addAction(sequence(alpha(0,0.3f),hide2()));
				achievtable.addAction(sequence(show2(),alpha(1,0.3f)));
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
				langtable.addAction(sequence(alpha(0,0.3f),hide2()));
				pausetable.addAction(sequence(show2(),alpha(1,0.3f)));
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
				langtable.addAction(sequence(alpha(0,0.3f),hide2()));
				pausetable.addAction(sequence(show2(),alpha(1,0.3f)));
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
				langtable.addAction(sequence(alpha(0,0.3f),hide2()));
				pausetable.addAction(sequence(show2(),alpha(1,0.3f)));
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
				pausetable.addAction(sequence(alpha(0,0.3f),hide2()));
				langtable.addAction(sequence(show2(),alpha(1,0.3f)));
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
		if(in.backK && !gameover && !quittable.hasParent() && !pausetable.isVisible()){
			showquit();
		}
		else if(in.backK && !gameover && !quittable.hasParent()&& pausetable.isVisible()){
			pausetable.addAction(sequence(alpha(0,0.25f,fade), hide2()));
			showquit();
		}
		if(MoleGame.bought==true){
			coinsbuymenu.setText(Integer.toString(Save.gd.silvercoins));
			coinsbuymenu.pack();
			coinsbuymenu.setPosition(
					stage.getWidth()-3.5f*pad-stage.getHeight()/16,
					stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
			coinsbonusmenu.setText(Integer.toString(Save.gd.silvercoins));
			coinsbonusmenu.pack();
			coinsbonusmenu.setPosition(
					stage.getWidth()-3.5f*pad-stage.getHeight()/16,
					stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
			coinslifemenu.setText(Integer.toString(Save.gd.silvercoins));
			coinslifemenu.pack();
			coinslifemenu.setPosition(
					stage.getWidth()-3.5f*pad-stage.getHeight()/16,
					stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
			coinsGOtable.setText(Integer.toString(Save.gd.silvercoins));
			coinsGOtable.pack();
			coinsGOtable.setPosition(
					stage.getWidth()-2*pad-stage.getHeight()/16,
					stage.getHeight()-stage.getHeight()/16/2-pad, Align.right);
			MoleGame.bought=false;
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
		
		//draw background
		sky0bg   .render(batch); 
		upskybg  .render(batch);
		if(cloud1bg!=null)cloud1bg .render(batch);
		if(cloud2bg!=null)cloud2bg .render(batch);
		if(cloud3bg!=null)cloud3bg .render(batch);
		if(rainbow!=null)batch.draw(rainbow, mole.molebody.getPosition().x/1.5f-12, -1,38,18);
		if(bg7!=null)bg7 .render(batch); 
		if(bg8!=null)bg8 .render(batch); 
		bg6  .render(batch);
		bg5  .render(batch);
		bg4.render(batch); 
		if(windeffect !=null){
			for(int i = -numDrawX; i < numDrawX; i=i+2) {
				windeffect.setPosition(i*15+leveloffset*lvllenght, 3);
				windeffect.drawext(batch);
			}
			windeffect .draw(batch, delta);
		}
		bg3  .render(batch);
		bg2  .render(batch);
		bg1   .render(batch);
		if(windeffect1!=null){
			for(int i = -numDrawX-1; i < numDrawX; i=i+2) {
				windeffect1.setPosition(i*15+leveloffset*lvllenght, 3);
				windeffect1.drawext(batch);
				}
			windeffect1.draw(batch, delta);
		}
		ground0bg       .render(batch); 
		groundjointbg   .render(batch);
		ugroundjoint1bg .render(batch); 
		ground1bg       .render(batch); 	
		ugroundjoint2bg .render(batch); 	
		ground2bg       .render(batch); 
		ugroundjoint3bg .render(batch); 
		ground3bg       .render(batch); 
		ground3bg       .render(batch,-5); 
		if(grass1bg!=null)grass1bg        .render(batch);
		if(grass2bg!=null)grass2bg        .render(batch);
		if(grass3bg!=null)grass3bg        .render(batch);
	
		//Infinite Depth Background
		
		//stars
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
		asteroidbbg.renderWrot(batch,0.007f);
		asteroidbbg2.renderWrot(batch,0.01f,11.8f);
		asteroidbbg2.renderWrot(batch,0.01f);
		asteroid2bg.renderWrot(batch,0.05f);
		asteroid1bg.renderWrot(batch,0.03f);
	
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
		/*//bottom side<first image
		while((camera.position.y-camera.viewportHeight*camera.zoom/2>star1.getY()+10)){
			staroffset1++;
			
		}
		//right side> last image
		while(camera.position.y+camera.viewportHeight*camera.zoom/2<(star1.getY())){
			staroffset1--;
		}*/
		//planet
		//mole.molebody.setTransform(0, 65, 0);
		moon     .setPosition(mole.molebody.getPosition().x+3,  60);
			if(onScreen(moon))      moon     .draw(batch);
		mars     .setPosition(mole.molebody.getPosition().x-12,  110);
			if(onScreen(mars))      mars     .draw(batch);
		jupiter  .setPosition(mole.molebody.getPosition().x-2.5f, 200);
			if(onScreen(jupiter))   jupiter  .draw(batch);
		saturne  .setPosition( mole.molebody.getPosition().x+3, 280);
			if(onScreen(saturne))   saturne  .draw(batch);
		uranus   .setPosition(mole.molebody.getPosition().x-16,  380);
			if(onScreen(uranus))    uranus   .draw(batch);
		neptune  .setPosition(mole.molebody.getPosition().x-10, 500);
			if(onScreen(neptune))   neptune  .draw(batch);
		nebuleuse.setPosition(mole.molebody.getPosition().x-30, 700);
			if(onScreen(nebuleuse)) nebuleuse.draw(batch);
		nebulaeffect1.setPosition(mole.molebody.getPosition().x, 700+17);
		float nv=mole.molebody.getLinearVelocity().x;
		float nv1=Math.abs(nv);
		nebulaeffect1.getEmitters().get(0).getAngle().setHighMax((nv>0? 0:180)+15);
		nebulaeffect1.getEmitters().get(0).getAngle().setHighMin((nv>0? 0:180)-15);
		nebulaeffect1.getEmitters().get(0).getAngle().setLowMax((nv>0? 0:180)+5);
		nebulaeffect1.getEmitters().get(0).getAngle().setLowMin((nv>0? 0:180)-5);
		nebulaeffect1.getEmitters().get(0).getVelocity().setHighMax(nv1+3);
		nebulaeffect1.getEmitters().get(0).getVelocity().setHighMin(nv1-3);
		nebulaeffect1.getEmitters().get(0).getVelocity().setLowMax(nv1-5);
		nebulaeffect1.getEmitters().get(0).getVelocity().setLowMin(nv1-8);
		
		nebulaeffect1.getEmitters().get(1).getAngle().setHighMax((nv>0? 0:180)+15);
		nebulaeffect1.getEmitters().get(1).getAngle().setHighMin((nv>0? 0:180)-15);
		nebulaeffect1.getEmitters().get(1).getAngle().setLowMax((nv>0? 0:180)+5);
		nebulaeffect1.getEmitters().get(1).getAngle().setLowMin((nv>0? 0:180)-5);
		nebulaeffect1.getEmitters().get(1).getVelocity().setHighMax(nv1+3);
		nebulaeffect1.getEmitters().get(1).getVelocity().setHighMin(nv1-3);
		nebulaeffect1.getEmitters().get(1).getVelocity().setLowMax(nv1-5);
		nebulaeffect1.getEmitters().get(1).getVelocity().setLowMin(nv1-8);
		
		nebulaeffect1.getEmitters().get(2).getAngle().setHighMax((nv>0? 0:180)+15);
		nebulaeffect1.getEmitters().get(2).getAngle().setHighMin((nv>0? 0:180)-15);
		nebulaeffect1.getEmitters().get(2).getAngle().setLowMax((nv>0? 0:180)+5);
		nebulaeffect1.getEmitters().get(2).getAngle().setLowMin((nv>0? 0:180)-5);
		nebulaeffect1.getEmitters().get(2).getVelocity().setHighMax(nv1+3);
		nebulaeffect1.getEmitters().get(2).getVelocity().setHighMin(nv1-3);
		nebulaeffect1.getEmitters().get(2).getVelocity().setLowMax(nv1-5);
		nebulaeffect1.getEmitters().get(2).getVelocity().setLowMin(nv1-8);
		
	
		nebulaeffect1.draw(batch,delta,2/(nv>1? nv:1));
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
		if(rocks!=null)
			drawRocks();
		if(bonuses!=null)
			drawBonuses(delta);
		if(coins!=null){
			drawCoins(delta);
			if(coins.size<120) {
				coinalt+=5;
				spawncoins(0+coinalt,8+coinalt);
			}
		}
		//front ground 
		hcloudbg.render(batch); 
		
		batch.end();
		}
		
		//======================  MOVE LEVEL OFFSET  ====================//
		//left side<first image
		while(mole.molebody.getPosition().x<-lvllenght/2+(leveloffset * lvllenght)){
			leveloffset--;
			if(bonuses!=null)
			for(int i = 0; i < bonuses.size; i++) {
				bonuses.get(i).body.setTransform(
						bonuses.get(i).body.getPosition().x -lvllenght, bonuses.get(i).body.getPosition().y, 0);
			}
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
			if(bonuses!=null)
			for(int i = 0; i < bonuses.size; i++) {
				bonuses.get(i).body.setTransform(
						bonuses.get(i).body.getPosition().x +lvllenght, bonuses.get(i).body.getPosition().y, 0);
			}
			if(coins!=null)
				for(int i = 0; i < coins.size; i++) {
					coins.get(i).body.setTransform(
							coins.get(i).body.getPosition().x +lvllenght, coins.get(i).body.getPosition().y, 0);
				}
			ground0b.body.setTransform(ground0b.body.getPosition().x+lvllenght, ground0b.body.getPosition().y, 0);
			ground1b.body.setTransform(ground1b.body.getPosition().x+lvllenght, ground1b.body.getPosition().y, 0);
			ground2b.body.setTransform(ground2b.body.getPosition().x+lvllenght, ground2b.body.getPosition().y, 0);
			ground3b.body.setTransform(ground3b.body.getPosition().x+lvllenght, ground3b.body.getPosition().y, 0);
			ground4b.body.setTransform(ground4b.body.getPosition().x+lvllenght, ground4b.body.getPosition().y, 0);
		}
			
		
		//////////////===================  MUSIC  =====================///////////////////
		if(mole.molebody.getPosition().y>50) {
			music1.setVolume(MathUtils.clamp(mole.vitesse/40, 0.5f, 1f)/(mole.molebody.getPosition().y-50));
		}
		else
			music1.setVolume(MathUtils.clamp(mole.vitesse/40, 0.5f, 1f));
		
		
		if(mole.molebody.getPosition().y>60) {
			musicspace.setVolume(0.005f*(mole.molebody.getPosition().y-60));
		}
		else 
			musicspace.setVolume(0);
		
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
		if((progress.gameover ==true || mole.vitesse<2) && mole.molebody.getPosition().y<0 && !gameovertable.hasParent()) {
			gameover =true;
			if(!progress.win){
				showGOlose();
				mole.autotuto=true;
			}
			else{
				showGOwin(progress.score);
				mole.auto=true;
			}
			mainui   .addAction(sequence(alpha(0,0.5f),hide2()));
			//completion.addAction(sequence(alpha(1,0.6f),show2()));
			flipcom  .addAction(sequence(alpha(0,0.5f),hide2()));
			heightcom.addAction(sequence(alpha(0,0.5f),hide2()));
			landcom  .addAction(sequence(alpha(0,0.5f),hide2()));
			bonuscom .addAction(sequence(alpha(0,0.5f),hide2()));
			holecom  .addAction(sequence(alpha(0,0.5f),hide2()));
			powers   .addAction(sequence(alpha(0,0.5f),hide2()));
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
			jptimer .addAction(sequence(alpha(0,0.5f,pow2Out),hide2() ));
			jpbutton.addAction(sequence(alpha(0,0.5f,pow2Out),hide2() ));
			jpnumber.addAction(sequence(alpha(0,0.5f,pow2Out),hide2() ));
		}
		if(mole.planetimer<=0  && planetimer.getColor().a==1){
			planetimer .addAction(sequence(alpha(0,0.5f,pow2Out),hide2() ));
			planebutton.addAction(sequence(alpha(0,0.5f,pow2Out),hide2() ));
		}
		if(mole.timetimer<=0  && timetimer.getColor().a==1){
			timetimer .addAction(sequence(alpha(0,0.5f,pow2Out),hide2() ));
			timebutton.addAction(sequence(alpha(0,0.5f,pow2Out),hide2() ));
		}
		stage.act(delta);
		stage.draw();
		
		tweenManager.update(delta);
		//debugRenderer.render(world, camera.combined);
		
	}

	
	private void spawncoins(float altitudeMin, float altitudeMax) {    ////////modif maybe
		float coinposx, coinposy, grpposx,grpposy;
		FixtureDef coinfdef = new FixtureDef();
		CircleShape cshape = new CircleShape();
		cshape.setRadius(0.25f);
		coinfdef.shape = cshape;
		coinfdef.isSensor = true;
		BodyDef coindef = new BodyDef();
		coindef.type = BodyType.StaticBody;
		int grptype=MathUtils.random(3);
		grpposx = mole.molebody.getPosition().x+MathUtils.randomSign()*44;
		//, (-lvllenght/2+42)+leveloffset*lvllenght/2f, (lvllenght/2-42)+leveloffset*lvllenght/2f)
		grpposy=altitudeMax*MathUtils.random()+altitudeMin;
		if(grptype==0){ //// square 4x4=16
			float geox=0,geoy=0;
			for(int j=0;j<16;j++){
				if(geox==4) {
					geox=0;
					geoy++;
				}
				geox++;
				coinposx=grpposx+geox*0.75f;
				coinposy=grpposy+geoy*0.75f;
				coindef.position.set(coinposx, coinposy);
				Body body = world.createBody(coindef);
				body.createFixture(coinfdef).setUserData("coin");
				Coin coin = new Coin(body);
				body.setUserData(coin);
				coins.add(coin);
			}
		}
		if(grptype==1){ //// triangle=15
			float geox=0,geoy=0;
			for(int j=0;j<15;j++){
				if(j==5) {
					geox=0.75f/2  +0.75f*1/5;
					geoy++;
				}
				if(j==9) {
					geox=0.75f    +0.75f*2/5;
					geoy++;
				}
				if(j==12) {
					geox=0.75f*3/2+0.75f*3/5;
					geoy++;
				}
				if(j==14) {
					geox=0.75f*2  +0.75f*4/5;
					geoy++;
				}
				geox++;
				coinposx=grpposx+geox*0.75f;
				coinposy=grpposy+geoy*0.75f;
				coindef.position.set(coinposx, coinposy);
				Body body = world.createBody(coindef);
				body.createFixture(coinfdef).setUserData("coin");
				Coin coin = new Coin(body);
				body.setUserData(coin);
				coins.add(coin);
			}
		}
		if(grptype==2){ //// triangle reverse=15
			float geox=0,geoy=0;
			for(int j=0;j<15;j++){
				if(j==1) {
					geox=-(0.75f/2  +0.75f*1/5);
					geoy++;
				}
				if(j==3) {
					geox=-(0.75f    +0.75f*2/5);
					geoy++;
				}
				if(j==6) {
					geox=-(0.75f*3/2+0.75f*3/5);
					geoy++;
				}
				if(j==10) {
					geox=-(0.75f*2  +0.75f*4/5);
					geoy++;
				}
				geox++;
				coinposx=grpposx+geox*0.75f;
				coinposy=grpposy+geoy*0.75f;
				coindef.position.set(coinposx, coinposy);
				Body body = world.createBody(coindef);
				body.createFixture(coinfdef).setUserData("coin");
				Coin coin = new Coin(body);
				body.setUserData(coin);
				coins.add(coin);
			}
		}
		if(grptype==3){ //// M=17
			float geox=0,geoy=0;
			for(int j=0;j<19;j++){
				if(j<=6) {
					geox=0;
					geoy++;
				}
				else if(j<=9) {
					geox+=0.75f;
					geoy--;
				}
				else if(j<=12) {
					geox+=0.75f;
					geoy++;
				}
				else {
					geoy--;
				}
				coinposx=grpposx+geox*0.75f;
				coinposy=grpposy+geoy*0.75f;
				coindef.position.set(coinposx, coinposy);
				Body body = world.createBody(coindef);
				body.createFixture(coinfdef).setUserData("coin");
				Coin coin = new Coin(body);
				body.setUserData(coin);
				coins.add(coin);
			}
		}
	
		cshape.dispose();

		
	}

	
	private void drawBonuses(float delta) {
		if(animtimer>2) animup=false;
		if(animtimer<=0) animup=true;
		animtimer+=(animup? 3:-3) *delta;
		float bonusw=(1-animtimer)*0.5f/4f+1;
		float bonush=(animtimer-1)*0.5f/4f+1;
		for(int i = 0; i < bonuses.size; i++) {
			Bonus bonus= bonuses.get(i);
			Sprite sprite=bonus.sprite;
			sprite.setSize(bonusw, bonush);
			sprite.setPosition((bonus.body.getPosition().x  - bonusw/2f),  (bonus.body.getPosition().y - bonush/2f));
			if(onScreen(sprite)) sprite.draw(batch);
		}
		rmvdgld.draw(batch);
		rmvdslv.draw(batch);
		rmvdbrz.draw(batch);
		rmvdjp.draw(batch);
		rmvddp.draw(batch);
		rmvdt.draw(batch);
	}
	
	private void drawRocks() {
		
		for(int i = 0; i < rocks.size; i++) {
			Rock rock= rocks.get(i);
			float rockw=rock.body.getFixtureList().get(0).getShape().getRadius()*2*1.17f;
			float rockh=rock.body.getFixtureList().get(0).getShape().getRadius()*2*1.17f;
			Sprite sprite=rock.sprite;
			sprite.setSize(rockw, rockh);
			sprite.setPosition((rock.body.getPosition().x  - rockw/2f),  (rock.body.getPosition().y - rockh/2f));
			if(onScreen(sprite)) sprite.draw(batch);
		}
	}
	
	
	private void drawCoins(float delta) {
		if(animtimer>2) animup=false;
		if(animtimer<=0) animup=true;
		animtimer+=(animup? 3:-3) *delta;
		float coinw=0.5f;//(1-animtimer)*0.5f/4f+1;
		float coinh=0.5f;//(animtimer-1)*0.5f/4f+1;
		for(int i = 0; i < coins.size; i++) {
			Coin coin= coins.get(i);
			Sprite sprite=coin.sprite;
			sprite.setSize(coinw, coinh);
			sprite.setPosition((coin.body.getPosition().x  - coinw/2f),  (coin.body.getPosition().y - coinh/2f));
			if(onScreen(sprite)) sprite.draw(batch);
		}
		rmvdcoin.draw(batch);
	}

	
	
	private void showGOwin(final int score) {
		///win level
		if(Save.gd.nextlvl<Save.gd.starX.length-1 && lvl==Save.gd.nextlvl){
			Save.gd.nextlvl+=1;
			Save.gd.newlvlunloked=true;
		}
		Gdx.input.setCatchBackKey(false);
		///GO table
		gameovertable = new Table(skin);
    	gameovertable.setFillParent(true);
		Image gover = new Image(Assets.manager.get(Assets.gover2, Texture.class));
    	Label scoretactor1    = new Label("Score : " + Integer.toString(score),skin,"default");
    	Label bestscore;
    	if(progress.score>Save.gd.scores[lvl]){
    		bestscore  = new Label(MoleGame.myBundle.get("newbest"), skin,"default");
    		if(MoleGame.gservices.isSignedIn()){
    			if(lvl==MoleGame.OLDARCADE){
					MoleGame.gservices.submitScore1(score);
				}
				if(lvl==MoleGame.ARCADE1){
					MoleGame.gservices.submitScore2(score);
				}
				if(lvl==MoleGame.ARCADEPLANE){
					MoleGame.gservices.submitScore3(score);
				}
				if(lvl==MoleGame.ARCADE2){
					MoleGame.gservices.submitScore4(score);
				}
    		}
    	}
    	else 
    		bestscore = new Label(MoleGame.myBundle.get("best")+Integer.toString(Save.gd.scores[lvl]), skin,"default");
    	
    	if(score>Save.gd.scores[lvl]){
			Save.gd.scores[lvl]=progress.score;
		}
		Save.gd.jpbought=0;
		Save.gd.planebought=0;
		Save.gd.timebought=0;
		Save.gd.goldcoins+=coincolected;
		Save.save();
    	
    	Image buttonMenu = new Image(Assets.manager.get(Assets.menu, Texture.class));
		buttonMenu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				gameovertable.addAction(sequence(delay(0.25f),alpha(0,0.25f)));
				black.addAction(sequence(show2(),
						fadeIn(0.5f),run(new Runnable() {
							@Override
							public void run() {
		((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
					}
				})));
			}
		});

		Image buttonRestart;
		if(type==MoleGame.ARCADELVL){
			buttonRestart = new Image(Assets.manager.get(Assets.raz, Texture.class));
			buttonRestart.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					black.addAction(sequence(show2(),
							fadeIn(0.5f),run(new Runnable() {
								@Override
								public void run() {
			((Game) Gdx.app.getApplicationListener()).setScreen(new PlayScreen(lvl,type,objectif,limittype,limit, score1, score2));
						}
					})));
				}
			});
		}
		else {
			buttonRestart = new Image(Assets.manager.get(Assets.shop, Texture.class));
			buttonRestart.setVisible(false);
		}
		
		//button submit score for arcade or continue for adventure
		Image buttonContinue;
		if(type==MoleGame.ARCADELVL){
			buttonContinue = new Image(Assets.manager.get(Assets.challenge, Texture.class));
			buttonContinue.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(MoleGame.gservices!=null){
							if(lvl==MoleGame.OLDARCADE){
								MoleGame.gservices.submitScore1(score);
								MoleGame.gservices.showScore1();
							}
							if(lvl==MoleGame.ARCADE1){
								MoleGame.gservices.submitScore2(score);
								MoleGame.gservices.showScore2();
							}
							if(lvl==MoleGame.ARCADEPLANE){
								MoleGame.gservices.submitScore3(score);
								MoleGame.gservices.showScore3();
							}
							if(lvl==MoleGame.ARCADE2){
								MoleGame.gservices.submitScore4(score);
								MoleGame.gservices.showScore4();
							}
						}
					}
				});
		}
		else{
			buttonContinue = new Image(Assets.manager.get(Assets.map, Texture.class));
			buttonContinue.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						gameovertable.addAction(sequence(delay(0.25f),alpha(0,0.25f)));
						black.addAction(sequence(show2(),
								fadeIn(0.5f),run(new Runnable() {
									@Override
									public void run() {
				((Game) Gdx.app.getApplicationListener()).setScreen(new WorldMap());
							}
						})));
					}
				});
		}
		
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
		gameovertable.addAction(sequence(show2(),alpha(1,0.5f)));
		
	}
	
	
	private void showGOlose() {
		gameovertable.clear();
		
		Save.gd.jpbought=0;
		Save.gd.planebought=0;
		Save.gd.timebought=0;
		Save.save();
		Gdx.input.setCatchBackKey(false);
		Image gover = new Image(Assets.manager.get(Assets.gover, Texture.class));
    	
    	Image buttonMenu = new Image(Assets.manager.get(Assets.map, Texture.class));
		buttonMenu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				gameovertable.addAction(sequence(delay(0.25f),alpha(0,0.25f)));
				black.addAction(sequence(show2(),
						fadeIn(0.5f),run(new Runnable() {
							@Override
							public void run() {
		((Game) Gdx.app.getApplicationListener()).setScreen(new WorldMap());
					}
				})));
			}
		});

		Group buttonrestart = new Group();
		Image buttonrazbg = new Image(Assets.manager.get(Assets.raz, Texture.class));
		buttonrazbg.setSize(stage.getHeight()/6.35f,stage.getHeight()/6.35f);
		buttonrazbg.setOrigin(Align.center);
		buttonrazbg.addAction(forever(sequence(
				delay(0.1f),
				rotateTo(-180,1.1f,pow4In),
				rotateTo(-360,0.9f,pow4Out),
				delay(0.1f),
				rotateTo(0)
				)));
		Image life1 = new Image(Assets.manager.get(Assets.life1, Texture.class));
		Image life2 = new Image(Assets.manager.get(Assets.life2, Texture.class));
		life1.setSize(stage.getHeight()/10.35f, stage.getHeight()/10.35f);
		life2.setSize(stage.getHeight()/10.35f, stage.getHeight()/10.35f);
		life1.setPosition(buttonrazbg.getWidth()/2-life1.getWidth()/2, buttonrazbg.getWidth()/2-life1.getWidth()/2);
		life2.setPosition(buttonrazbg.getWidth()/2-life1.getWidth()/2, buttonrazbg.getWidth()/2-life1.getWidth()/2);
		life1.setOrigin(life1.getWidth()/2, life1.getHeight()*0.14f);
		life2.setOrigin(life2.getWidth()/2, life2.getHeight()*0.14f);
		life1.addAction(forever(sequence(
				delay(0.7f),
				parallel(rotateTo(15,1,pow2In),alpha(0,1.2f,pow5In)),
				delay(0.5f),
				parallel(rotateTo(0),alpha(1,0.35f,pow2Out))
				)));
		life2.addAction(forever(sequence(
				delay(0.7f),
				parallel(rotateTo(-15,1,pow2In),alpha(0,1.2f,pow5In)),
				delay(0.5f),
				parallel(rotateTo(0),alpha(1,0.35f,pow2Out))
				)));
		buttonrestart.addActor(buttonrazbg);
		buttonrestart.addActor(life1);
		buttonrestart.addActor(life2);
		
		buttonrestart.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				if(Save.gd.lives>0){
					if(Save.gd.utime==0){
						Save.gd.lives-=1;
						Save.save();
						gameovertable.addAction(sequence(delay(0.25f),alpha(0,0.25f)));
						black.addAction(sequence(show2(), fadeIn(0.5f),run(new Runnable() {
							@Override
							public void run() {
				((Game) Gdx.app.getApplicationListener()).setScreen(new PlayScreen(lvl,type,objectif,limittype,limit, score1, score2));
							}
						})));
					}
					else{
						bonusmenu();
					}
				}
				else {
					buylives();
				}
			}
		});
		
		Image coinIm=new Image(Assets.manager.get(Assets.orbsilver, Texture.class));
		coinIm.setSize(stage.getHeight()/16,stage.getHeight()/16);
		coinIm.setPosition(stage.getWidth()-coinIm.getWidth()-pad,stage.getHeight()-coinIm.getHeight()-pad);
		gameovertable.addActor(coinIm);
		coinsGOtable=new Label(Integer.toString(Save.gd.silvercoins),skin);
		coinsGOtable.setPosition(
				stage.getWidth()-2*pad-stage.getHeight()/16,
				stage.getHeight()-stage.getHeight()/16/2-pad, Align.right);
		gameovertable.addActor(coinsGOtable);
		
		Group buttonContinue = new Group();
		Image buttonbg = new Image(Assets.manager.get(Assets.empty, Texture.class));
		buttonbg.setSize(stage.getHeight()/6.35f,stage.getHeight()/6.35f);
		Label continutxt;
		if(limittype==MoleGame.LIMITTIME){
			continutxt=new Label("+15s", skin);
		}
		else if(limittype==MoleGame.LIMITJUMP){
			continutxt=new Label("+5 "+MoleGame.myBundle.get("lmtjump2"), skin);
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
		continutxt.addAction(forever(sequence(delay(0.1f),alpha(0,1.1f,pow4In),alpha(1,0.9f,pow4Out),delay(0.1f))));
		continuprice.addAction(forever(sequence(delay(0.1f),alpha(0,1.1f,pow4In),alpha(1,0.9f,pow4Out),delay(0.1f))));
		coinContinue.addAction(forever(sequence(delay(0.1f),alpha(0,1.1f,pow4In),alpha(1,0.9f,pow4Out),delay(0.1f))));
		
		buttonContinue.addListener(new ClickListener() {

				@Override
				public void clicked(InputEvent event, float x, float y) {
					if(Save.gd.silvercoins>=CONTINUEPRICE){
						Save.gd.silvercoins-=CONTINUEPRICE;
						Save.save();
						gameovertable.addAction(sequence(alpha(0,0.5f),
								hide2(), removeActor()));
						mainui   .addAction(sequence(show2(),alpha(1,0.5f)));
						flipcom  .addAction(sequence(show2(),alpha(0,0.5f)));
						heightcom.addAction(sequence(show2(),alpha(0,0.5f)));
						landcom  .addAction(sequence(show2(),alpha(0,0.5f)));
						bonuscom .addAction(sequence(show2(),alpha(0,0.5f)));
						holecom  .addAction(sequence(show2(),alpha(0,0.5f)));
						powers   .addAction(sequence(show2(),alpha(1,0.5f)));
						if(limittype==MoleGame.LIMITTIME)
							progress.settime(0, 15);
						if(limittype==MoleGame.LIMITJUMP)
							progress.limit+=5;
						if(state==State.Paused) state=State.Running;
						progress.gameover=false;
						gameOverTimer=0;
						mole.auto=mole.autotuto=false;
						gameover=false;
					}
					else createbank();
				}
			});
		
		gameovertable.add(gover).size(stage.getHeight()*0.28f).pad(pad).colspan(3).row();
		gameovertable.defaults().uniform().fill();
		gameovertable.defaults().center().padTop(pad);
		gameovertable.add(buttonrestart) .size(stage.getHeight()/6.35f).pad(pad);
		gameovertable.add(buttonContinue).size(stage.getHeight()/6.35f).pad(pad);
		gameovertable.add(buttonMenu)    .size(stage.getHeight()/6.35f).pad(pad);
		gameovertable.getColor().a=0;
		gameovertable.setVisible(false);
		gover.setOrigin(stage.getHeight()*0.28f/2f,stage.getHeight()*0.28f/2f);
    	gover.addAction(forever(sequence(rotateTo(-30,1.5f, sine),rotateTo(30,1.5f, sine))));
		stage.addActor(gameovertable);
		gameovertable.addAction(sequence(show2(),alpha(1,0.5f)));
	}
	
	private void buylives(){
		
		lifemenu = new Group();
		Image lifebg = new Image(Assets.manager.get(Assets.uiback, Texture.class));
		lifebg.setSize(stage.getWidth(), stage.getHeight());
		lifebg.setPosition(0, 0);
		lifebg.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//anti transparency ????
			}
		});
		lifemenu.addActor(lifebg);
		
		Image coinIm=new Image(Assets.manager.get(Assets.orbsilver, Texture.class));
		coinIm.setSize(stage.getHeight()/16,stage.getHeight()/16);
		coinIm.setPosition(stage.getWidth()-coinIm.getWidth()-2.5f*pad,stage.getHeight()-coinIm.getHeight()-2.5f*pad);
		coinIm.addAction(forever(sequence(alpha(0.5f,1f, sine),alpha(1,1f, sine))));
		lifemenu.addActor(coinIm);
		coinslifemenu=new Label(Integer.toString(Save.gd.silvercoins),skin);
		coinslifemenu.setPosition(
				stage.getWidth()-3.5f*pad-stage.getHeight()/16,
				stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
		coinslifemenu.addAction(forever(sequence(alpha(0.5f,1f, sine),alpha(1,1f, sine))));
		lifemenu.addActor(coinslifemenu);
		
		/////// heading
		Group lifeIm = new Group();
		Image lifeImbg = new Image(Assets.manager.get(Assets.empty, Texture.class));
		lifeImbg.setSize(stage.getHeight()*0.28f, stage.getHeight()*0.28f);
		lifeImbg.setOrigin(Align.center);
		lifeImbg.addAction(forever(sequence(rotateTo(-15,0.5f, sine),rotateTo(15,0.5f, sine))));
		Image life1 = new Image(Assets.manager.get(Assets.life1, Texture.class));
		Image life2 = new Image(Assets.manager.get(Assets.life2, Texture.class));
		life1.setSize(stage.getHeight()*0.28f, stage.getHeight()*0.28f);
		life2.setSize(stage.getHeight()*0.28f, stage.getHeight()*0.28f);
		life1.setPosition(lifeImbg.getWidth()/2-life1.getWidth()/2, lifeImbg.getWidth()/2-life1.getWidth()/2);
		life2.setPosition(lifeImbg.getWidth()/2-life1.getWidth()/2, lifeImbg.getWidth()/2-life1.getWidth()/2);
		life1.setOrigin(life1.getWidth()/2, life1.getHeight()*0.14f);
		life2.setOrigin(life2.getWidth()/2, life2.getHeight()*0.14f);
		life1.addAction(forever(sequence(
				delay(0.7f),
				parallel(rotateTo(0,1,pow2In),alpha(1,1.1f,pow3Out)),
				delay(0.5f),
				alpha(0,0.35f,pow2Out),
				rotateTo(15)
				)));
		life2.addAction(forever(sequence(
				delay(0.7f),
				parallel(rotateTo(0,1,pow2In),alpha(1,1.1f,pow3Out)),
				delay(0.5f),
				alpha(0,0.35f,pow2Out),
				rotateTo(-15)
				)));
		lifeIm.addActor(lifeImbg);
		lifeIm.addActor(life1);
		lifeIm.addActor(life2);
		lifeIm.setSize(stage.getHeight()*0.28f, stage.getHeight()*0.28f);
		lifeIm.setPosition(stage.getWidth()/2-lifeIm.getWidth()/2, stage.getHeight()-lifeIm.getHeight()-2*pad);
		lifemenu.addActor(lifeIm);
		
		
		/////////   ============== Buttons =======================/////////////
		Image buttonFB0 = new Image(Assets.manager.get(Assets.empty, Texture.class));
		buttonFB0.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
			}
		});
		
		buttonFB0.setSize(stage.getHeight()*0.2f, stage.getHeight()*0.2f);
		buttonFB0.setPosition(stage.getWidth()/2+buttonFB0.getWidth()/2+7f*pad, stage.getHeight()/2-buttonFB0.getHeight()-pad);
		lifemenu.addActor(buttonFB0);
		
		Image buttonFB1 = new Image(Assets.manager.get(Assets.empty, Texture.class));
		buttonFB1.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
			}
		});
		
		buttonFB1.setSize(stage.getHeight()*0.2f, stage.getHeight()*0.2f);
		buttonFB1.setPosition(stage.getWidth()/2-buttonFB1.getWidth()/2, stage.getHeight()/2-buttonFB1.getHeight()-pad);
		lifemenu.addActor(buttonFB1);
		
		Group buylifeB = new Group();
		Image buttonbg = new Image(Assets.manager.get(Assets.empty, Texture.class));
		buttonbg.setSize(stage.getHeight()*0.2f, stage.getHeight()*0.2f);
		Label buylifeBtxt=new Label(MoleGame.myBundle.get("lives"), skin);
		buylifeBtxt.setAlignment(Align.center);
		final int livesprice=12;
		buylifeBtxt.setPosition(buttonbg.getHeight()/2-buylifeBtxt.getWidth()/2, buttonbg.getHeight()*3/4-buylifeBtxt.getHeight()/2);
		Label buylifeprice=new Label(Integer.toString(livesprice), skin);
		buylifeprice.setPosition(buttonbg.getHeight()/2-buylifeprice.getWidth()-pad/2, buttonbg.getHeight()*1/4-buylifeprice.getHeight()/2);
		Image buylifeBcoin   = new Image(Assets.manager.get(Assets.orbsilver, Texture.class));
		buylifeBcoin.setSize(stage.getHeight()/16, stage.getHeight()/16);
		buylifeBcoin.setPosition(buttonbg.getHeight()/2, buylifeprice.getY());
		buylifeB.addActor(buttonbg);
		buylifeB.addActor(buylifeBtxt);
		buylifeB.addActor(buylifeprice);
		buylifeB.addActor(buylifeBcoin);
		buylifeBtxt.addAction(forever(sequence(delay(0.1f),alpha(0,1.1f,pow4In),alpha(1,0.9f,pow4Out),delay(0.1f))));
		buylifeprice.addAction(forever(sequence(delay(0.1f),alpha(0,1.1f,pow4In),alpha(1,0.9f,pow4Out),delay(0.1f))));
		buylifeBcoin.addAction(forever(sequence(delay(0.1f),alpha(0,1.1f,pow4In),alpha(1,0.9f,pow4Out),delay(0.1f))));
		buylifeB.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.silvercoins>=livesprice && Save.gd.lives==0){
					Save.gd.lives=5;
					Save.gd.silvercoins-=livesprice;
					Save.save();
					coinslifemenu.setText(Integer.toString(Save.gd.silvercoins));
					coinslifemenu.pack();
					coinslifemenu.setPosition(
							stage.getWidth()-3.5f*pad-stage.getHeight()/16,
							stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
					coinsGOtable=new Label(Integer.toString(Save.gd.silvercoins),skin);
					coinsGOtable.setPosition(
							stage.getWidth()-2*pad-stage.getHeight()/16,
							stage.getHeight()-stage.getHeight()/16/2-pad, Align.right);
					lifemenu.addAction(sequence(moveTo(0,stage.getHeight(),0.5f, fade), hide2()));
				}
				else if(Save.gd.silvercoins<livesprice){
					createbank();
				}
				
			}
		});
		
		buylifeB.setSize(stage.getHeight()*0.2f, stage.getHeight()*0.2f);
		buylifeB.setPosition(stage.getWidth()/2-buylifeB.getWidth()*3/2-7f*pad, stage.getHeight()/2-buylifeB.getHeight()-pad);
		lifemenu.addActor(buylifeB);
		
		
		Image rbutton = new Image(Assets.manager.get(Assets.back, Texture.class));
		rbutton.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
		rbutton.setPosition(stage.getWidth()/2-rbutton.getWidth()/2, 2*pad);
		rbutton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				int refund = Save.gd.jpbought*JPPRICE;
				Save.gd.silvercoins+= refund;
				coinslifemenu.setText(Integer.toString(Save.gd.silvercoins));
				Save.gd.jpbought=0;
				refund = Save.gd.planebought/2*PLANEPRICE;
				Save.gd.silvercoins+= refund;
				coinslifemenu.setText(Integer.toString(Save.gd.silvercoins));
				Save.gd.planebought=0;
				refund = Save.gd.timebought/2*TIMEPRICE;
				Save.gd.silvercoins+= refund;
				coinslifemenu.setText(Integer.toString(Save.gd.silvercoins));
				Save.gd.timebought=0;
				lifemenu.addAction(sequence(moveTo(0,stage.getHeight(),0.5f, fade), hide2()));
				//if(menuButton.isChecked()) mainMenu.addAction(sequence(show(), alpha(1, 0.5f, fade)));
				coinsGOtable=new Label(Integer.toString(Save.gd.silvercoins),skin);
				coinsGOtable.setPosition(
						stage.getWidth()-2*pad-stage.getHeight()/16,
						stage.getHeight()-stage.getHeight()/16/2-pad, Align.right);
			}
		});
		lifemenu.addActor(rbutton);
		lifemenu.clearActions();
		lifemenu.getColor().a =  0;
		lifemenu.addAction(sequence(moveTo(0,stage.getHeight()), alpha(1),show2(),moveTo(0,0,0.5f, fade)));
		stage.addActor(lifemenu);
	}
	
	
	private void bonusmenu(){
		bonusMenu = new Group();
		Image playbg = new Image(Assets.manager.get(Assets.uiback, Texture.class));
		playbg.setSize(stage.getWidth(), stage.getHeight());
		playbg.setPosition(0, 0);
		playbg.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//anti transparency ????
			}
		});
		bonusMenu.addActor(playbg);
		
		Image coinIm=new Image(Assets.manager.get(Assets.orbsilver, Texture.class));
		coinIm.setSize(stage.getHeight()/16,stage.getHeight()/16);
		coinIm.setPosition(stage.getWidth()-coinIm.getWidth()-2.5f*pad,stage.getHeight()-coinIm.getHeight()-2.5f*pad);
		coinIm.addAction(forever(sequence(alpha(0.5f,1f, sine),alpha(1,1f, sine))));
		bonusMenu.addActor(coinIm);
		coinsbonusmenu=new Label(Integer.toString(Save.gd.silvercoins),skin);
		coinsbonusmenu.setPosition(
				stage.getWidth()-3.5f*pad-stage.getHeight()/16,
				stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
		coinsbonusmenu.addAction(forever(sequence(alpha(0.5f,1f, sine),alpha(1,1f, sine))));
		bonusMenu.addActor(coinsbonusmenu);
		
		Label lvltitle=new Label(MoleGame.myBundle.get("bonusttl"), skin, "big");
		lvltitle.setAlignment(Align.center);
		lvltitle.setPosition(stage.getWidth()/2-lvltitle.getWidth()/2, stage.getHeight()-lvltitle.getHeight()-2*pad);
		bonusMenu.addActor(lvltitle);
		
		Image playbutton = new Image(Assets.manager.get(Assets.play, Texture.class));
		playbutton.setSize(stage.getHeight()*0.28f, stage.getHeight()*0.28f);
		playbutton.setPosition(stage.getWidth()-playbutton.getWidth()-pad/2f, 2*pad);
		playbutton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.lives>0){
					Save.gd.lives-=1;
					black.toFront();
					Save.save();
					black.addAction(sequence(show2(),
							fadeIn(0.25f),run(new Runnable() {
								@Override
								public void run() {
			((Game) Gdx.app.getApplicationListener()).setScreen(new PlayScreen(lvl,type,objectif,limittype,limit,score1,score2));
						}
					})));
				}
				else{
					buylives();
				}
			}
		});
		playbutton.addAction(sequence(delay(0),forever(sequence(
				sizeTo(stage.getHeight()/4.8f, stage.getHeight()/4.2f,0.5f,sine),
				sizeTo(stage.getHeight()/4.2f, stage.getHeight()/4.8f,0.5f,sine)))));
		bonusMenu.addActor(playbutton);
		
		final Image coinjp=new Image(Assets.manager.get(Assets.orbsilver, Texture.class));
		coinjp.setSize(stage.getHeight()/16,stage.getHeight()/16);
		final Label jpprice=new Label("55", skin, "default");
		jpprice.getColor().a=0;
		coinjp.getColor().a=0;
		
		final Image coinplane=new Image(Assets.manager.get(Assets.orbsilver, Texture.class));
		coinplane.setSize(stage.getHeight()/16,stage.getHeight()/16);
		final Label planeprice=new Label("55", skin, "default");
		planeprice.getColor().a=0;
		coinplane.getColor().a=0;
		
		final Image cointime=new Image(Assets.manager.get(Assets.orbsilver, Texture.class));
		cointime.setSize(stage.getHeight()/16,stage.getHeight()/16);
		final Label timeprice=new Label("55", skin, "default");
		timeprice.getColor().a=0;
		cointime.getColor().a=0;
		
		/////////boosters/////////////
		RadialSprite rprog1 = new RadialSprite(new TextureRegion(Assets.manager.get(Assets.timer, Texture.class)));
		final Image timetimer= new Image(rprog1);
		timetimer.setTouchable(Touchable.disabled);
		((RadialSprite) timetimer.getDrawable()).setAngle(359.9f);
		((RadialSprite) timetimer.getDrawable()).setColor(Color.RED);
		Image timebutton = new Image(Assets.manager.get(Assets.time, Texture.class));
		final Image canceltime = new Image(Assets.manager.get(Assets.musicoff, Texture.class));
		canceltime.setVisible(false);
		timebutton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.timebought==0 && Save.gd.silvercoins>=TIMEPRICE){
					canceltime.clearActions();
					canceltime.addAction(sequence(show2(),alpha(1,0.25f,fade)));
					cointime.addAction(alpha(1,0.25f,fade));
					timeprice.addAction(alpha(1,0.25f,fade));
				}
				if(Save.gd.timebought<10 && Save.gd.silvercoins>=TIMEPRICE){
					Save.gd.timebought+=2;
					Save.gd.silvercoins-=TIMEPRICE;
					timeprice.setText(Integer.toString(Save.gd.timebought/2*TIMEPRICE));
					coinsbonusmenu.setText(Integer.toString(Save.gd.silvercoins));
					coinsbonusmenu.pack();
					coinsbonusmenu.setPosition(
							stage.getWidth()-3.5f*pad-stage.getHeight()/16,
							stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
					Color color1;
					if(Save.gd.timebought>20/3)color1=new Color((3-Save.gd.timebought*3/10),1,0,1);
					else if(Save.gd.timebought>10/3)color1=new Color(1,Save.gd.timebought*3/10-1,0,1);
					else color1=new Color(1,0,0,1);
					((RadialSprite) timetimer.getDrawable()).setAngle(360 -Save.gd.timebought*36);
					((RadialSprite) timetimer.getDrawable()).setColor(color1);
				}
				else if(Save.gd.silvercoins<TIMEPRICE){
					createbank();
				}
				
			}
		});
		
		canceltime.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				canceltime.addAction(sequence(alpha(0,0.25f,fade),hide2()));
				cointime.addAction(alpha(0,0.25f,fade));
				timeprice.addAction(alpha(0,0.25f,fade));
				int refund = Save.gd.timebought/2*TIMEPRICE;
				Save.gd.silvercoins+= refund;
				coinsbonusmenu.setText(Integer.toString(Save.gd.silvercoins));
				coinsbonusmenu.pack();
				coinsbonusmenu.setPosition(
						stage.getWidth()-3.5f*pad-stage.getHeight()/16,
						stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
				Save.gd.timebought=0;
				Color color1;
				if(Save.gd.timebought>20/3)color1=new Color((3-Save.gd.timebought*3/10),1,0,1);
				else if(Save.gd.timebought>10/3)color1=new Color(1,Save.gd.timebought*3/10-1,0,1);
				else color1=new Color(1,0,0,1);
				((RadialSprite) timetimer.getDrawable()).setAngle(360 -0.01f*36);
				((RadialSprite) timetimer.getDrawable()).setColor(color1);
				
			}
		});
		timetimer .setSize(stage.getHeight()/6.35f, -stage.getHeight()/6.35f);
		timebutton.setSize(stage.getHeight()/6.35f,  stage.getHeight()/6.35f);
		timetimer .setPosition(stage.getWidth()/2+timetimer .getWidth()/2+7f*pad, stage.getHeight()/2-pad);
		timebutton.setPosition(stage.getWidth()/2+timebutton.getWidth()/2+7f*pad, stage.getHeight()/2-timebutton.getHeight()-pad);
		canceltime.setSize(stage.getHeight()/16.8f, stage.getHeight()/16.8f);
		canceltime.setPosition(timebutton.getX()+timebutton.getWidth()-canceltime.getWidth()*3/4f,
				timebutton.getY()+timebutton.getHeight()-canceltime.getHeight()*3/4f);
		bonusMenu.addActor(timebutton);
		bonusMenu.addActor(timetimer);
		bonusMenu.addActor(canceltime);
		if(Save.gd.utime==0){
			timetimer.setVisible(false);
			timebutton.setVisible(false);
		}
		
		RadialSprite rprog2 = new RadialSprite(new TextureRegion(Assets.manager.get(Assets.timer, Texture.class)));
		final Image planetimer= new Image(rprog2);
		((RadialSprite) planetimer.getDrawable()).setAngle(359.9f);
		((RadialSprite) planetimer.getDrawable()).setColor(Color.RED);
		planetimer.setTouchable(Touchable.disabled);
		final Image cancelplane = new Image(Assets.manager.get(Assets.musicoff, Texture.class));
		cancelplane.setVisible(false);
		Image planebutton = new Image(Assets.manager.get(Assets.plane, Texture.class));
		planebutton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.planebought==0 && Save.gd.silvercoins>=PLANEPRICE){
					cancelplane.clearActions();
					cancelplane.addAction(sequence(show2(),alpha(1,0.25f,fade)));
					coinplane.addAction(alpha(1,0.25f,fade));
					planeprice.addAction(alpha(1,0.25f,fade));
				}
				if(Save.gd.planebought<10 && Save.gd.silvercoins>=PLANEPRICE){
					Save.gd.planebought+=2;
					Save.gd.silvercoins-=PLANEPRICE;
					planeprice.setText(Integer.toString(Save.gd.planebought/2*PLANEPRICE));
					coinsbonusmenu.setText(Integer.toString(Save.gd.silvercoins));
					coinsbonusmenu.pack();
					coinsbonusmenu.setPosition(
							stage.getWidth()-3.5f*pad-stage.getHeight()/16,
							stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
					Color color1;
					if(Save.gd.planebought>20/3)color1=new Color((3-Save.gd.planebought*3/10),1,0,1);
					else if(Save.gd.planebought>10/3)color1=new Color(1,Save.gd.planebought*3/10-1,0,1);
					else color1=new Color(1,0,0,1);
					((RadialSprite) planetimer.getDrawable()).setAngle(360 -Save.gd.planebought*36);
					((RadialSprite) planetimer.getDrawable()).setColor(color1);
				}
				else if(Save.gd.silvercoins<PLANEPRICE){
					createbank();
				}
				
			}
		});
		
		cancelplane.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				cancelplane.addAction(sequence(alpha(0,0.25f,fade),hide2()));
				int refund = Save.gd.planebought/2*PLANEPRICE;
				Save.gd.silvercoins+= refund;
				coinsbonusmenu.setText(Integer.toString(Save.gd.silvercoins));
				coinsbonusmenu.pack();
				coinsbonusmenu.setPosition(
						stage.getWidth()-3.5f*pad-stage.getHeight()/16,
						stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
				Save.gd.planebought=0;
				coinplane.addAction(alpha(0,0.25f,fade));
				planeprice.addAction(alpha(0,0.25f,fade));
				Color color1;
				if(Save.gd.planebought>20/3)color1=new Color((3-Save.gd.planebought*3/10),1,0,1);
				else if(Save.gd.planebought>10/3)color1=new Color(1,Save.gd.planebought*3/10-1,0,1);
				else color1=new Color(1,0,0,1);
				((RadialSprite) planetimer.getDrawable()).setAngle(359.9f);
				((RadialSprite) planetimer.getDrawable()).setColor(color1);
				
			}
		});
		planebutton.setSize(stage.getHeight()/6.35f, stage.getHeight()/6.35f);
		planetimer .setSize(stage.getHeight()/6.35f, -stage.getHeight()/6.35f);
		planebutton.setPosition(stage.getWidth()/2-planebutton.getWidth()/2, stage.getHeight()/2-planebutton.getHeight()-pad);
		planetimer .setPosition(stage.getWidth()/2-planetimer .getWidth()/2, stage.getHeight()/2-pad);
		cancelplane.setSize(stage.getHeight()/16.8f, stage.getHeight()/16.8f);
		cancelplane.setPosition(planebutton.getX()+planebutton.getWidth()-cancelplane.getWidth()*3/4f,
				planebutton.getY()+planebutton.getHeight()-cancelplane.getHeight()*3/4f);
		bonusMenu.addActor(planebutton);
		bonusMenu.addActor(planetimer);
		bonusMenu.addActor(cancelplane);
		if(Save.gd.uplane==0){
			planetimer.setVisible(false);
			planebutton.setVisible(false);
		}
		
		RadialSprite rprog3 = new RadialSprite(new TextureRegion(Assets.manager.get(Assets.timer, Texture.class)));
		final Image jptimer= new Image(rprog3);
		((RadialSprite) jptimer.getDrawable()).setAngle(359.9f);
		((RadialSprite) jptimer.getDrawable()).setColor(Color.RED);
		jptimer.setTouchable(Touchable.disabled);
		final Image canceljp = new Image(Assets.manager.get(Assets.musicoff, Texture.class));
		canceljp.setVisible(false);
		Image jpbutton = new Image(Assets.manager.get(Assets.jetpack, Texture.class));
		final Label jpnumber = new Label("", skin);
		jpbutton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.jpbought==0 && Save.gd.silvercoins>=JPPRICE){
					canceljp.clearActions();
					canceljp.addAction(sequence(show2(),alpha(1,0.25f,fade)));
					coinjp  .addAction(alpha(1,0.25f,fade));
					jpprice .addAction(alpha(1,0.25f,fade));
					jpnumber.addAction(alpha(1,0.25f,fade));
				}
				if(Save.gd.jpbought<5 && Save.gd.silvercoins>=JPPRICE){
					Save.gd.jpbought+=1;
					Save.gd.silvercoins-=JPPRICE;
					jpprice.setText(Integer.toString(Save.gd.jpbought*JPPRICE));
					coinsbonusmenu.setText(Integer.toString(Save.gd.silvercoins));
					coinsbonusmenu.pack();
					coinsbonusmenu.setPosition(
							stage.getWidth()-3.5f*pad-stage.getHeight()/16,
							stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
					jpnumber.setText(Integer.toString(Save.gd.jpbought));
					Color color1;
					color1=new Color(0,1,0,1);
					((RadialSprite) jptimer.getDrawable()).setAngle(360 -Save.gd.jpbought*72);
					((RadialSprite) jptimer.getDrawable()).setColor(color1);
				}
				else if(Save.gd.silvercoins<JPPRICE){
					createbank();
				}
				
			}
		});
		
		canceljp.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				canceljp.addAction(sequence(alpha(0,0.25f,fade),hide2()));
				coinjp.addAction(alpha(0,0.25f,fade));
				jpprice.addAction(alpha(0,0.25f,fade));
				jpnumber.addAction(alpha(0,0.25f,fade));
				int refund = Save.gd.jpbought*JPPRICE;
				Save.gd.silvercoins+= refund;
				coinsbonusmenu.setText(Integer.toString(Save.gd.silvercoins));
				coinsbonusmenu.pack();
				coinsbonusmenu.setPosition(
						stage.getWidth()-3.5f*pad-stage.getHeight()/16,
						stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
				Save.gd.jpbought=0;
				jpnumber.setText(Integer.toString(Save.gd.jpbought));
				Color color1;
				if(Save.gd.jpbought>20/3)color1=new Color((3-Save.gd.jpbought*3/10),1,0,1);
				else if(Save.gd.jpbought>10/3)color1=new Color(1,Save.gd.jpbought*3/10-1,0,1);
				else color1=new Color(1,0,0,1);
				((RadialSprite) jptimer.getDrawable()).setAngle(359.9f);
				((RadialSprite) jptimer.getDrawable()).setColor(color1);
				
			}
		});
		jpbutton.setSize(stage.getHeight()/6.35f, stage.getHeight()/6.35f);
		jptimer .setSize(stage.getHeight()/6.35f, -stage.getHeight()/6.35f);
		jpbutton.setPosition(stage.getWidth()/2-jpbutton.getWidth()*3/2-7f*pad, stage.getHeight()/2-jpbutton.getHeight()-pad);
		jptimer .setPosition(stage.getWidth()/2-jptimer .getWidth()*3/2-7f*pad, stage.getHeight()/2-pad);
		jpnumber.setTouchable(Touchable.disabled);
		jpnumber.setAlignment(Align.center);
		jpnumber.setPosition(jpbutton.getX()+jpbutton.getWidth()/2-jpnumber.getWidth()/2, jpbutton.getY()+jpbutton.getWidth()/2-jpnumber.getHeight()/2);
		jpnumber.getColor().a=0.7f;
		canceljp.setSize(stage.getHeight()/16.8f, stage.getHeight()/16.8f);
		canceljp.setPosition(jpbutton.getX()+jpbutton.getWidth()-canceljp.getWidth()*3/4f,
				jpbutton.getY()+jpbutton.getHeight()-canceljp.getHeight()*3/4f);
		bonusMenu.addActor(jpbutton);
		bonusMenu.addActor(jptimer);
		bonusMenu.addActor(jpnumber);
		bonusMenu.addActor(canceljp);
		if(Save.gd.ujp==0){
			jptimer.setVisible(false);
			jpbutton.setVisible(false);
			jpnumber.setVisible(false);
		}
		
		coinjp.setPosition(jpbutton.getX()+jpbutton.getWidth()-coinjp.getWidth(),
				jpbutton.getY()-coinjp.getHeight()-pad/2);
		bonusMenu.addActor(coinjp);
		jpprice.setPosition(jpbutton.getX()+jpbutton.getWidth()-pad-coinjp.getWidth()-jpprice.getWidth(),
				coinjp.getY()+coinjp.getHeight()/2-jpprice.getHeight()/2f);
		bonusMenu.addActor(jpprice);
		
		coinplane.setPosition(planebutton.getX()+planebutton.getWidth()-coinplane.getWidth(),
				planebutton.getY()-coinplane.getHeight()-pad/2);
		bonusMenu.addActor(coinplane);
		planeprice.setPosition(planebutton.getX()+planebutton.getWidth()-pad-coinplane.getWidth()-planeprice.getWidth(),
				coinplane.getY()+coinplane.getHeight()/2-planeprice.getHeight()/2f);
		bonusMenu.addActor(planeprice);
		
		cointime.setPosition(timebutton.getX()+timebutton.getWidth()-cointime.getWidth(),
				timebutton.getY()-cointime.getHeight()-pad/2);
		bonusMenu.addActor(cointime);
		timeprice.setPosition(timebutton.getX()+timebutton.getWidth()-pad-cointime.getWidth()-timeprice.getWidth(),
				cointime.getY()+cointime.getHeight()/2-timeprice.getHeight()/2f);
		bonusMenu.addActor(timeprice);
		
		Image rbutton = new Image(Assets.manager.get(Assets.back, Texture.class));
		rbutton.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
		rbutton.setPosition(2*pad, 2*pad);
		rbutton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				int refund = Save.gd.jpbought*JPPRICE;
				Save.gd.silvercoins+= refund;
				coinsbonusmenu.setText(Integer.toString(Save.gd.silvercoins));
				Save.gd.jpbought=0;
				refund = Save.gd.planebought/2*PLANEPRICE;
				Save.gd.silvercoins+= refund;
				coinsbonusmenu.setText(Integer.toString(Save.gd.silvercoins));
				Save.gd.planebought=0;
				refund = Save.gd.timebought/2*TIMEPRICE;
				Save.gd.silvercoins+= refund;
				coinsbonusmenu.setText(Integer.toString(Save.gd.silvercoins));
				Save.gd.timebought=0;
				
				bonusMenu.addAction(sequence(moveTo(0,stage.getHeight(),0.5f, fade), hide2()));
				//if(menuButton.isChecked()) mainMenu.addAction(sequence(show(), alpha(1, 0.5f, fade)));
			}
		});
		bonusMenu.addActor(rbutton);
		
		bonusMenu.clearActions();
		bonusMenu.getColor().a =  0;
		bonusMenu.addAction(sequence(moveTo(0,stage.getHeight()), alpha(1),show2(),moveTo(0,0,0.5f, fade)));
		stage.addActor(bonusMenu);
	}

	
	private Action hide2() {
		Action action=com.badlogic.gdx.scenes.scene2d.actions.Actions.hide();
		return action;
	}
	
	
	private Action show2() {
		Action action=com.badlogic.gdx.scenes.scene2d.actions.Actions.show();
		return action;
	}
	
	
	private void createbank() {
		if(MoleGame.shop!=null)MoleGame.shop.checkprice();
		final Group buymenu = new Group();
		Image bgbuy1 = new Image(Assets.manager.get(Assets.uiback, Texture.class));
		bgbuy1.setSize(stage.getWidth(), stage.getHeight());
		bgbuy1.setPosition(0, 0);
		buymenu.addActor(bgbuy1);
		
		Image mole = new Image(Assets.manager.get(Assets.gover2, Texture.class));
		mole.setSize(stage.getHeight()*0.28f, stage.getHeight()*0.28f);
		mole.setPosition(2*pad, stage.getHeight()-mole.getHeight()-2*pad);
		buymenu.addActor(mole);
		
		Label wanted= new Label("Wanted! \n Dead or Alive", skin);
		wanted.setAlignment(Align.center);
		wanted.setPosition(2*pad, stage.getHeight()-mole.getHeight()/1.5f-2*pad);
		wanted.addAction(forever(sequence(alpha(0.f,1f, sine),delay(1),alpha(0.8f,1f, sine))));
		buymenu.addActor(wanted);
		
		Group buy1 = button (MoleGame.price1, false);
		Group buy2 = button (MoleGame.price2, false);
		Group buy3 = button (MoleGame.price3, false);
		Group buy4 = button (MoleGame.price4, false);
		Group buy5 = button (MoleGame.price5, false);
		Group buy6 = button (MoleGame.price6, false);
		Group buy7 = button (MoleGame.price7, false);
		Group buy8 = button (MoleGame.price8, false);
		
		Image backbt = new Image(Assets.manager.get(Assets.back,Texture.class));
		
		Label buytitle=new Label(MoleGame.myBundle.get("bank"), skin, "big");
		buytitle.setPosition(stage.getWidth()/2-buytitle.getWidth()/2, stage.getHeight()-buytitle.getHeight()-2*pad);
		buymenu.addActor(buytitle);
		
		Image coinIm=new Image(Assets.manager.get(Assets.orbsilver, Texture.class));
		coinIm.setSize(stage.getHeight()/16,stage.getHeight()/16);
		coinIm.setPosition(stage.getWidth()-coinIm.getWidth()-2.5f*pad,stage.getHeight()-coinIm.getHeight()-2.5f*pad);
		coinIm.addAction(forever(sequence(alpha(0.5f,1f, sine),alpha(1,1f, sine))));
		buymenu.addActor(coinIm);
		coinsbuymenu=new Label(Integer.toString(Save.gd.silvercoins),skin);
		coinsbuymenu.setPosition(
				stage.getWidth()-3.5f*pad-stage.getHeight()/16,
				stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
		coinsbuymenu.addAction(forever(sequence(alpha(0.5f,1f, sine),alpha(1,1f, sine))));
		buymenu.addActor(coinsbuymenu);
		
		backbt=new Image(Assets.manager.get(Assets.back, Texture.class));
		backbt.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
		backbt.setPosition(stage.getWidth()/2-backbt.getWidth()/2, 2*pad);
		buymenu.addActor(backbt);
		
		Group iaplist= new Group();
		Label cn1 = new Label("10", skin, "big");
		Label cn2 = new Label("50", skin, "big");
		Label cn3 = new Label("100", skin, "big");
		Label cn4 = new Label("150", skin, "big");
		Label cn5 = new Label("200", skin, "big");
		Label cn6 = new Label("250", skin, "big");
		Label cn7 = new Label("500", skin, "big");
		Label cn8 = new Label("1000", skin, "big");
		cn8.setPosition(stage.getWidth()/4-4*pad, +0*cn8.getHeight()+pad);
		cn7.setPosition(stage.getWidth()/4-4*pad, +1*cn8.getHeight()+pad);
		cn6.setPosition(stage.getWidth()/4-4*pad, +2*cn8.getHeight()+pad);
		cn5.setPosition(stage.getWidth()/4-4*pad, +3*cn8.getHeight()+pad);
		cn4.setPosition(stage.getWidth()/4-4*pad, +4*cn8.getHeight()+pad);
		cn3.setPosition(stage.getWidth()/4-4*pad, +5*cn8.getHeight()+pad);
		cn2.setPosition(stage.getWidth()/4-4*pad, +6*cn8.getHeight()+pad);
		cn1.setPosition(stage.getWidth()/4-4*pad, +7*cn8.getHeight()+pad);
		iaplist.addActor(cn1);
		iaplist.addActor(cn2);
		iaplist.addActor(cn3);
		iaplist.addActor(cn4);
		iaplist.addActor(cn5);
		iaplist.addActor(cn6);
		iaplist.addActor(cn7);
		iaplist.addActor(cn8);
		
		float coinsize= stage.getHeight()/16;
		Image coin0=new Image(Assets.manager.get(Assets.orbsilver, Texture.class));
		coin0.setSize(coinsize, coinsize);
		coin0.setPosition(stage.getWidth()/2-coin0.getWidth()/2, cn1.getY()+cn1.getHeight()/2-coin0.getHeight()/2);
		iaplist.addActor(coin0);
		
		Group coins1=new Group();
		float lign=0, col=0;
		for(int i =0; i<3;i++){
			if(i==2){
				col=0.5f;
				lign++;
			};
			Image coin=new Image(Assets.manager.get(Assets.orbsilver, Texture.class));
			coin.setSize(coinsize, coinsize);
			coin.setPosition(coinsize*3/4f*col, coinsize*2/4f*lign);
			coins1.addActor(coin);
			col++;
			
		}
		coins1.setSize(coinsize*(1+3/4f), coinsize*(1+2/4f));
		coins1.setPosition(stage.getWidth()/2-coins1.getWidth()/2, cn2.getY()+cn5.getHeight()/2-coins1.getHeight()/2);
		iaplist.addActor(coins1);
		
		Group coins2=new Group();
		lign=0; col=0;
		for(int i =0; i<5;i++){
			if(i==3){
				col=0.5f;
				lign++;
			};
			Image coin=new Image(Assets.manager.get(Assets.orbsilver, Texture.class));
			coin.setSize(coinsize, coinsize);
			coin.setPosition(coinsize*3/4f*col, coinsize*2/4f*lign);
			coins2.addActor(coin);
			col++;
		}
		coins2.setSize(coinsize*(1+2*3/4f), coinsize*(1+2/4f));
		coins2.setPosition(stage.getWidth()/2-coins2.getWidth()/2, cn3.getY()+cn5.getHeight()/2-coins2.getHeight()/2);
		iaplist.addActor(coins2);
		
		Group coins3=new Group();
		lign=0; col=0;
		for(int i =0; i<9;i++){
			if(i==5){
				col=0.5f;
				lign++;
			};
			Image coin=new Image(Assets.manager.get(Assets.orbsilver, Texture.class));
			coin.setSize(coinsize, coinsize);
			coin.setPosition(coinsize*3/4f*col, coinsize*2/4f*lign);
			coins3.addActor(coin);
			col++;
		}
		coins3.setSize(coinsize*(1+4*3/4f), coinsize*(1+2/4f));
		coins3.setPosition(stage.getWidth()/2-coins3.getWidth()/2, cn4.getY()+cn5.getHeight()/2-coins3.getHeight()/2);
		iaplist.addActor(coins3);
		
		Group coins5=new Group();
		lign=0; col=0;
		for(int i =0; i<13;i++){
			if(i==7){
				col=0.5f;
				lign++;
			};
			Image coin=new Image(Assets.manager.get(Assets.orbsilver, Texture.class));
			coin.setSize(coinsize, coinsize);
			coin.setPosition(coinsize*3/4f*col, coinsize*2/4f*lign);
			coins5.addActor(coin);
			col++;
		}
		coins5.setSize(coinsize*(1+6*3/4f), coinsize*(1+2/4f));
		coins5.setPosition(stage.getWidth()/2-coins5.getWidth()/2, cn5.getY()+cn5.getHeight()/2-coins5.getHeight()/2);
		iaplist.addActor(coins5);
		
		Group coins6=new Group();
		lign=0; col=0;
		for(int i =0; i<17;i++){
			if(i==9){
				col=0.5f;
				lign++;
			};
			Image coin=new Image(Assets.manager.get(Assets.orbsilver, Texture.class));
			coin.setSize(coinsize, coinsize);
			coin.setPosition(coinsize*3/4f*col, coinsize*2/4f*lign);
			coins6.addActor(coin);
			col++;
		}
		coins6.setSize(coinsize*(1+8*3/4f), coinsize*(1+2/4f));
		coins6.setPosition(stage.getWidth()/2-coins6.getWidth()/2, cn6.getY()+cn5.getHeight()/2-coins6.getHeight()/2);
		iaplist.addActor(coins6);
		
		Group coins7=new Group();
		lign=0; col=0;
		for(int i =0; i<24;i++){
			if(i==9){
				col=0.5f;
				lign++;
			};
			if(i==17){
				col=1f;
				lign++;
			};
			Image coin=new Image(Assets.manager.get(Assets.orbsilver, Texture.class));
			coin.setSize(coinsize, coinsize);
			coin.setPosition(coinsize*3/4f*col, coinsize*1/4f*lign);
			coins7.addActor(coin);
			col++;
		}
		coins7.setSize(coinsize*(1+8*3/4f), coinsize*(1+2/4f));
		coins7.setPosition(stage.getWidth()/2-coins7.getWidth()/2, cn7.getY()+cn5.getHeight()/2-coins7.getHeight()/2);
		iaplist.addActor(coins7);
		
		Group coins8=new Group();
		lign=0; col=0;
		for(int i =0; i<30;i++){
			if(i==9){
				col=0.5f;
				lign++;
			};
			if(i==17){
				col=1f;
				lign++;
			};
			if(i==24){
				col=1.5f;
				lign++;
			};
			Image coin=new Image(Assets.manager.get(Assets.orbsilver, Texture.class));
			coin.setSize(coinsize, coinsize);
			coin.setPosition(coinsize*3/4f*col, coinsize*1/4f*lign);
			coins8.addActor(coin);
			col++;
		}
		coins8.setSize(coinsize*(1+8*3/4f), coinsize*(1+3/4f));
		coins8.setPosition(stage.getWidth()/2-coins8.getWidth()/2, cn8.getY()+cn5.getHeight()/2-coins8.getHeight()/2);
		iaplist.addActor(coins8);
		
		buy8.setPosition(stage.getWidth()*3/4-buy8.getWidth()+4*pad, +1*cn8.getHeight()+pad-cn8.getHeight()/2-buy8.getHeight()/2);
		buy7.setPosition(stage.getWidth()*3/4-buy8.getWidth()+4*pad, +2*cn8.getHeight()+pad-cn8.getHeight()/2-buy8.getHeight()/2);
		buy6.setPosition(stage.getWidth()*3/4-buy8.getWidth()+4*pad, +3*cn8.getHeight()+pad-cn8.getHeight()/2-buy8.getHeight()/2);
		buy5.setPosition(stage.getWidth()*3/4-buy8.getWidth()+4*pad, +4*cn8.getHeight()+pad-cn8.getHeight()/2-buy8.getHeight()/2);
		buy4.setPosition(stage.getWidth()*3/4-buy8.getWidth()+4*pad, +5*cn8.getHeight()+pad-cn8.getHeight()/2-buy8.getHeight()/2);
		buy3.setPosition(stage.getWidth()*3/4-buy8.getWidth()+4*pad, +6*cn8.getHeight()+pad-cn8.getHeight()/2-buy8.getHeight()/2);
		buy2.setPosition(stage.getWidth()*3/4-buy8.getWidth()+4*pad, +7*cn8.getHeight()+pad-cn8.getHeight()/2-buy8.getHeight()/2);
		buy1.setPosition(stage.getWidth()*3/4-buy8.getWidth()+4*pad, +8*cn8.getHeight()+pad-cn8.getHeight()/2-buy8.getHeight()/2);
		iaplist.addActor(buy1);
		iaplist.addActor(buy2);
		iaplist.addActor(buy3);
		iaplist.addActor(buy4);
		iaplist.addActor(buy5);
		iaplist.addActor(buy6);
		iaplist.addActor(buy7);
		iaplist.addActor(buy8);
		
		iaplist.setSize(stage.getWidth(), buy1.getY()+buy1.getHeight()+4*pad-cn8.getY());
		ScrollPane buy=new ScrollPane(iaplist);
		buy.setBounds(0, backbt.getY()+backbt.getHeight()+pad, //x,y
				stage.getWidth(),                              //w
				buytitle.getY()-pad-(backbt.getY()+backbt.getHeight()+pad));//h
		buymenu.addActor(buy);
		
		backbt.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//if(Save.gd.sound)click.play();
				buymenu.addAction(sequence(moveTo(0,stage.getHeight(),0.5f, fade),removeActor()));
				//if(menuButton.isChecked()) mainMenu.addAction(sequence(show(), alpha(1, 0.5f, fade)));
			}
		});
		buy1.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//if(Save.gd.sound)click.play();
				if(MoleGame.shop!=null)MoleGame.shop.buyCoin1();
			}
		});
		buy2.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//if(Save.gd.sound)click.play();
				if(MoleGame.shop!=null)MoleGame.shop.buyCoin2();
			}
		});
		buy3.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//if(Save.gd.sound)click.play();
				if(MoleGame.shop!=null)MoleGame.shop.buyCoin3();
			}
		});
		buy4.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//if(Save.gd.sound)click.play();
				if(MoleGame.shop!=null)MoleGame.shop.buyCoin4();
			}
		});
		buy5.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//if(Save.gd.sound)click.play();
				if(MoleGame.shop!=null)MoleGame.shop.buyCoin5();
			}
		});
		buy6.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//if(Save.gd.sound)click.play();
				if(MoleGame.shop!=null)MoleGame.shop.buyCoin6();
			}
		});
		buy7.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//if(Save.gd.sound)click.play();
				if(MoleGame.shop!=null)MoleGame.shop.buyCoin7();
			}
		});
		buy8.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//if(Save.gd.sound)click.play();
				if(MoleGame.shop!=null)MoleGame.shop.buyCoin8();
			}
		});
		stage.addActor(buymenu);
		buymenu.addAction(sequence(moveTo(0,stage.getHeight()), alpha(1),moveTo(0,0, 0.5f, fade)));

		
	}
	
	
	private Group button(String txt, boolean b) {
		Group grp=new Group();
		Image image = new Image(Assets.manager.get(Assets.empty, Texture.class));
		image.setSize(stage.getHeight()/3.85f, stage.getHeight()/3.85f/2);
		Label label = new Label(txt, skin, "default");
		label.setPosition(stage.getHeight()/3.85f/2, stage.getHeight()/3.85f/4, Align.center);
		label.setTouchable(Touchable.disabled);
		grp.addActor(image);
		grp.addActor(label);
		grp.setSize(stage.getHeight()/3.85f, stage.getHeight()/3.85f/2);
		return grp;
	}

	
	public void update(float delta){
		if(gameover && !progress.win)
			world.step(delta* Interpolation.pow2In.apply(0.5f, 0, MathUtils.clamp(gameOverTimer / 3f, 0.01f, 1)), VELOCITYITERATIONS, POSITIONITERATIONS);
		else
			world.step(delta*timeScale, VELOCITYITERATIONS, POSITIONITERATIONS);
    	
    	progress.addtime(0, (int) (5*mole.speedup));
    	//Score
		progress.update();
    	if (!gameover ){
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
		updateBonuses();
		updateCoins();
		//Ufo.updateUFOs();
	}

	
	private void updateCoins() {
		for(int i = 0; i < cl.coins2remove.size; i++) {
			if(!gameover) coincolected++;
			progress.coins=coincolected;
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
	
	
	private void updateBonuses() {
		for(int i = 0; i < cl.bonus2remove.size; i++) {
			Body b = cl.bonus2remove.get(i);
			Bonus bonus=(Bonus) (b.getUserData());
			if(bonus.type==1){
				mole.clawup++;
				bonuscom.setText(MoleGame.myBundle.get("bnscom1"));
				bonuscom.addAction(sequence(
						moveTo(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/4), alpha(1,0.1f),
						parallel(alpha(0,2f,fade),moveTo(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2,2f,fade))
						));
				Tween.set(rmvdgld, SpriteAccessor.ALPHA).target(1).start(tweenManager);
				Tween.set(rmvdgld, SpriteAccessor.XY).target(b.getPosition().x-0.5f,b.getPosition().y-0.5f).ease(Sine.INOUT).start(tweenManager);
				Tween.to(rmvdgld, SpriteAccessor.XY, 2).target(b.getPosition().x-0.5f,b.getPosition().y+5).ease(Sine.INOUT).start(tweenManager);
				Tween.to(rmvdgld, SpriteAccessor.ALPHA, 2).target(0).start(tweenManager);
				bonusalt+=bonusup;
				spawnbonus(bonus.type, bonusalt, bonusalt+10);
			}
			else if(bonus.type==2){
				mole.controlup++;
				bonuscom.setText(MoleGame.myBundle.get("bnscom2"));
				bonuscom.addAction(sequence(
						moveTo(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/4), alpha(1,0.1f),
						parallel(alpha(0,2f,fade),moveTo(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2,2f,fade))
						));
				Tween.set(rmvdslv, SpriteAccessor.ALPHA).target(1).start(tweenManager);
				Tween.set(rmvdslv, SpriteAccessor.XY).target(b.getPosition().x-0.5f,b.getPosition().y-0.5f).ease(Sine.INOUT).start(tweenManager);
				Tween.to(rmvdslv, SpriteAccessor.XY, 2).target(b.getPosition().x-0.5f,b.getPosition().y+5).ease(Sine.INOUT).start(tweenManager);
				Tween.to(rmvdslv, SpriteAccessor.ALPHA, 2).target(0).start(tweenManager);
				bonusalt+=bonusup;
				spawnbonus(bonus.type, bonusalt, bonusalt+10);
			}
			else if(bonus.type==3){
				mole.speedup++;
				bonuscom.setText(MoleGame.myBundle.get("bnscom3"));
				bonuscom.addAction(sequence(
						moveTo(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/4), alpha(1,0.1f),
						parallel(alpha(0,2f,fade),moveTo(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2,2f,fade))
						));
				Tween.set(rmvdbrz, SpriteAccessor.ALPHA).target(1).start(tweenManager);
				Tween.set(rmvdbrz, SpriteAccessor.XY).target(b.getPosition().x-0.5f,b.getPosition().y-0.5f).ease(Sine.INOUT).start(tweenManager);
				Tween.to(rmvdbrz, SpriteAccessor.XY, 2).target(b.getPosition().x-0.5f,b.getPosition().y+5).ease(Sine.INOUT).start(tweenManager);
				Tween.to(rmvdbrz, SpriteAccessor.ALPHA, 2).target(0).start(tweenManager);
				bonusalt+=bonusup;
				spawnbonus(bonus.type, bonusalt, bonusalt+10);
			}
			else if(bonus.type==6){
				if(mole.jpnumber==0){
					jptimer.clearActions();
					jpbutton.clearActions();
					jpnumber.clearActions();
					jptimer.addAction(sequence(show2(), alpha(1,0.5f,pow2Out)));
					jpbutton.addAction(sequence(show2(), alpha(1,0.5f,pow2Out)));
					jpnumber.addAction(sequence(show2(), alpha(1,0.5f,pow2Out)));
				}
				mole.jpnumber++;
				jpnumber.setText(Integer.toString(mole.jpnumber));
				Tween.set(rmvdjp, SpriteAccessor.ALPHA).target(1).start(tweenManager);
				Tween.set(rmvdjp, SpriteAccessor.XY).target(b.getPosition().x-0.5f,b.getPosition().y-0.5f).ease(Sine.INOUT).start(tweenManager);
				Tween.to(rmvdjp, SpriteAccessor.XY, 2).target(b.getPosition().x-0.5f,b.getPosition().y+5).ease(Sine.INOUT).start(tweenManager);
				Tween.to(rmvdjp, SpriteAccessor.ALPHA, 2).target(0).start(tweenManager);
				bonusalt+=bonusup;
				spawnbonus(bonus.type, bonusalt, bonusalt+10);
			}
			else if(bonus.type==5){
				if(mole.planetimer==0){
					planetimer.clearActions();
					planebutton.clearActions();
					planetimer.addAction(sequence(show2(), alpha(1,0.5f,pow2Out)));
					planebutton.addAction(sequence(show2(), alpha(1,0.5f,pow2Out)));
				}
				if(mole.planetimer+2.5f<=10) mole.planetimer+=2.5f;
				Tween.set(rmvddp, SpriteAccessor.ALPHA).target(1).start(tweenManager);
				Tween.set(rmvddp, SpriteAccessor.XY).target(b.getPosition().x-0.5f,b.getPosition().y-0.5f).ease(Sine.INOUT).start(tweenManager);
				Tween.to(rmvddp, SpriteAccessor.XY, 2).target(b.getPosition().x-0.5f,b.getPosition().y+5).ease(Sine.INOUT).start(tweenManager);
				Tween.to(rmvddp, SpriteAccessor.ALPHA, 2).target(0).start(tweenManager);
				bonusalt+=bonusup;
				spawnbonus(bonus.type, bonusalt, bonusalt+10);
			}
			else if(bonus.type==4){
				if(mole.timetimer==0){
					timetimer.clearActions();
					timebutton.clearActions();
					timetimer.addAction(sequence(show2(), alpha(1,0.5f,pow2Out)));
					timebutton.addAction(sequence(show2(), alpha(1,0.5f,pow2Out)));
				}
				if(mole.timetimer+2.5f<=10) mole.timetimer+=2.5f;
				Tween.set(rmvdt, SpriteAccessor.ALPHA).target(1).start(tweenManager);
				Tween.set(rmvdt, SpriteAccessor.XY).target(b.getPosition().x-0.5f,b.getPosition().y-0.5f).ease(Sine.INOUT).start(tweenManager);
				Tween.to(rmvdt, SpriteAccessor.XY, 2).target(b.getPosition().x-0.5f,b.getPosition().y+5).ease(Sine.INOUT).start(tweenManager);
				Tween.to(rmvdt, SpriteAccessor.ALPHA, 2).target(0).start(tweenManager);
				bonusalt+=bonusup;
				spawnbonus(bonus.type, bonusalt, bonusalt+10);
			}
			bonuses.removeValue( (Bonus) (b.getUserData()), true);
			world.destroyBody(cl.bonus2remove.get(i));
		}
		cl.bonus2remove.clear();
		
	}
	
	
	private void spawnbonus(int type, float altitudeMin, float altitudeMax) {    ////////modif maybe
		float bonusposx, bonusposy;
		//bonusposx = mole.molebody.getPosition().x+MathUtils.randomSign()*44; ///////who knows
		bonusposx = mole.molebody.getPosition().x+MathUtils.random(-1,1)*bonusxrange;
		bonusposy=altitudeMax*MathUtils.random()+altitudeMin;
		BodyDef bonusdef = new BodyDef();
		bonusdef.type = BodyType.StaticBody;
		bonusdef.position.set(bonusposx, bonusposy);
		Body body = world.createBody(bonusdef);
		FixtureDef bonusfdef = new FixtureDef();
		CircleShape cshape = new CircleShape();
		cshape.setRadius(0.5f);
		bonusfdef.shape = cshape;
		bonusfdef.isSensor = true;
		body.createFixture(bonusfdef).setUserData("bonus");
		 //Old arcade : equi-repartition des 3 bonus de base
		Bonus bonus = new Bonus(body, type);
		body.setUserData(bonus);
		bonuses.add(bonus);
		cshape.dispose();
		
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
		if(!pausetable.isVisible() && !gameover && !quittable.hasParent()){
			buttonset.addAction(sequence(show2(),alpha(1,0.5f)));
			pausetable.addAction(sequence(show2(),alpha(1,0.5f)));
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