package quentin.jeu.mole.screens;


import java.util.Locale;

import quentin.jeu.mole.MoleGame;
import quentin.jeu.mole.entities.Ground;
import quentin.jeu.mole.entities.Mole;
import quentin.jeu.mole.graphics.Background;
import quentin.jeu.mole.interpolate.SpriteAccessor;
import quentin.jeu.mole.interpolate.Tween;
import quentin.jeu.mole.interpolate.TweenManager;
import quentin.jeu.mole.utils.Assets;
import quentin.jeu.mole.utils.MyContactlistener;
import quentin.jeu.mole.utils.MyInputProc;
import quentin.jeu.mole.utils.Save;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
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
import com.badlogic.gdx.utils.I18NBundle;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.math.Interpolation.*;


public class MainMenu implements Screen {
	
	private World world;
	private final  float TIMESTEP = (float) 1 / 60;
	private final  int VELOCITYITERATIONS = 8, POSITIONITERATIONS = 3;
	private  final int SKIN1PRICE=25, HAT1PRICE=15, HAT2PRICE=20, MAXUJP=3,MAXUPLANE=3, MAXUTIME=3, MAXBSPEED=4,MAXBCONTROL=4;
	private int jpcost, planecost, timecost, speedcost, controlcost;
	private  float pad=Gdx.graphics.getHeight()/36f;
	
	private MyContactlistener cl;

	public Mole mole;
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	
	private TweenManager interManager;

	private SpriteBatch batch;
	
	private Background bg4, bg7, bg8, bg5, bg6, bg3, bg1,
	cloud1bg, cloud2bg, cloud3bg,bg2,  grass1bg, grass2bg, grass3bg, groundjointbg ,ground0bg ,ugroundjoint1bg, ground1bg,
	sky0bg, upskybg;
	
	private Sprite backsky;
	private Sprite rainbow;
	
	private Stage stage;	
	private Skin skin;
	private Group table, lvltable, shopgroup, buymenu;
	private Label coinsbuymenu, silvcoinsshop;
	private Image black, buttonaccel, buttonset;
	private ScrollPane scroll;
	private boolean wasPanDragFling = false;
	private float pagespacing, pagewidth;
	private int pagenumber;
	private Label a1desc ,a2desc, a3desc, a4desc, a5desc, a6desc, a7desc;
	
	private Music music1, musicspace;
	private Sound click, no;
	private float aspectratio;
	
	@Override
	public void show() {
		Save.load();
		Assets.load();
		if(Save.gd.lang==0){
			FileHandle baseFileHandle = Gdx.files.internal("internation/MyBundle");
			Locale locale = new Locale("en");
			MoleGame.myBundle = I18NBundle.createBundle(baseFileHandle, locale);
		}
		else if(Save.gd.lang==1){
			FileHandle baseFileHandle = Gdx.files.internal("internation/MyBundle");
			Locale locale = new Locale("fr");
			MoleGame.myBundle = I18NBundle.createBundle(baseFileHandle, locale);
		}
		if(MoleGame.gservices.isSignedIn())
			MoleGame.gservices.checkachiev();
			
		
		MoleGame.shop.checkprice();
		click= Assets.manager.get(Assets.click, Sound.class);
		no    = Assets.manager.get(Assets.no, Sound.class);
		aspectratio = (float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth();
		//sizeratio = (float)Gdx.graphics.getHeight()/540f;
		world = new World(new Vector2(0, -9.81f), true);
		debugRenderer = new Box2DDebugRenderer();
		batch = new SpriteBatch();
		
		camera = new OrthographicCamera(19.2f, aspectratio*19.2f);
		loadSkin();
		
		// creating animations
		interManager = new TweenManager();
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		
		int w=38; //System.out.println(Gdx.graphics.getWidth()/25);
		int h=10; //System.out.println(Gdx.graphics.getHeight()/50);
		
		// create backgrounds
		//object
		backsky =   new Sprite(Assets.manager.get(Assets.backsky,   Texture.class));
		backsky.setSize(2*260, 40);
		backsky.setPosition(-260, 0);
		rainbow =   new Sprite(Assets.manager.get(Assets.rainbow, Texture.class));
		
			
		//static bgs
		float[] color = new float[]{0.9f, 0.6f, 0.3f};
		final Sprite sky0        = new Sprite(Assets.manager.get(Assets.sky0,   Texture.class));
		final Sprite upsky       = new Sprite(Assets.manager.get(Assets.upsky,  Texture.class));
		final Sprite ground0     = new Sprite(Assets.manager.get(Assets.ground0,Texture.class));
		final Sprite ground1     = new Sprite(Assets.manager.get(Assets.ground1,Texture.class));
		final Sprite hcloud      = new Sprite(Assets.manager.get(Assets.hcloud, Texture.class));
		final Sprite groundjoint   = new Sprite(Assets.manager.get(Assets.ugroundjoint0,  Texture.class));
		final Sprite ugroundjoint1 = new Sprite(Assets.manager.get(Assets.ugroundjoint1,Texture.class));
		ground0.setColor(color[0],color[1],color[2],1);
		groundjoint.setColor(color[0],color[1],color[2],1);
		
		sky0.setSize(10, 10);
		upsky.setSize(10, 10);
		final Sprite grass1       = new Sprite(Assets.manager.get(Assets.grass1,  Texture.class));
		final Sprite grass2       = new Sprite(Assets.manager.get(Assets.grass2,  Texture.class));
		final Sprite grass3       = new Sprite(Assets.manager.get(Assets.grass3,  Texture.class));
		grass1       .setSize(15*1.15f, 1.5f*1.15f);
		grass2       .setSize(15*1.15f, 1.5f*1.15f);
		grass3       .setSize(15*1.15f, 1.5f*1.15f);
		groundjoint  .setSize(10, 2);
		ground0      .setSize(10, 10);
		ugroundjoint1.setSize(10, 2);
		ground1      .setSize(10, 10);
		hcloud       .setSize(w, 1.5f*h);
		
		grass1bg =        new Background(interManager,grass1,        Background.WIND2, camera,      0f,1);
		grass2bg =        new Background(interManager,grass2,        Background.WIND3, camera,      0f,1);
		grass3bg =        new Background(interManager,grass3,        Background.WIND3, camera,      0f,1);
		groundjointbg =   new Background(interManager,groundjoint,  Background.NONE,  camera,   -0.5f,1);
		ground0bg =       new Background(interManager,ground0,      Background.NONE,  camera,     -1f,1);
		ugroundjoint1bg = new Background(interManager,ugroundjoint1,Background.NONE,  camera, -5-0.5f,1);
		ground1bg =       new Background(interManager,ground1,      Background.NONE,  camera,     -2f,1);
		sky0bg =          new Background(interManager,sky0,         Background.NONE,  camera,      0f,1);
		upskybg =         new Background(interManager,upsky,        Background.NONE,  camera,  1+1/3f,1);
		
		
		
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
		hills0   .setSize(w/1.5f*1.1f, h*1.1f);
		hills1   .setSize(w/1.5f*1.1f, h*1.1f);
		fhills0  .setSize(w/2*1.5f, 1.5f*1.5f);
		tree0.setSize(w/4*1.5f, w/4*1.5f);
		tree1.setSize(w/5*1.5f, w/5*1.5f);
		tree2.setSize(w/10*1.5f,w/10*1.5f);
		tree3.setSize(w/14*1.5f,w/14*1.5f);
		rock  .setSize(w/7*1.2f, h/5*1.2f);
		cloud .setSize(w/3*1.5f, h/2*1.5f);
		//paralax bgs
		
		bg1 =   new Background(interManager,rock,   Background.NONE,  camera, 0.1f, -0.1f ,8);
		bg2 =   new Background(interManager,tree0,  Background.WIND1, camera, 0.2f, -0.1f ,3);
		bg3 =   new Background(interManager,tree1,  Background.WIND1, camera, 0.3f, -0.1f ,2);
		bg4 =   new Background(interManager,fhills0,Background.WIND2, camera, 0.6f, -0.15f,1);
		bg5 =   new Background(interManager,tree2,  Background.WIND1, camera, 0.6f,  0.0f ,3);
		bg6 =   new Background(interManager,tree3,  Background.WIND1, camera, 0.7f,  0.0f ,4);
		bg7 =   new Background(interManager,hills0, Background.NONE,  camera, 0.8f, -0.15f,2);
		bg8 =   new Background(interManager,hills1, Background.NONE,  camera, 0.8f, -0.15f,2,0.995f);
		cloud1bg =  new Background(interManager,cloud,  Background.NONE,  camera, 1.5f, -0.1f , 4);
		cloud2bg =  new Background(interManager,cloud,  Background.NONE,  camera, 1.3f,  0.3f , 2);
		cloud3bg =  new Background(interManager,cloud,  Background.NONE,  camera, 1.1f,  0.8f , 4);
	
					
		//GROUNDS
		final Vector2[] groundchain = new Vector2[]{new Vector2(-1000,0), new Vector2(-5,0), new Vector2(5,0), new Vector2(1000,0)};
		
		//Create mole and set contact listener to it
		cl = new MyContactlistener(0);
		MyInputProc in =new MyInputProc();
		float [] color1 = new float[]{1f, 1f,1f};
		mole = new Mole(world, in, cl, color, color1, 0);
		mole.auto=true;
		world.setContactListener(cl);
		
		//ground
		new Ground(world, "ground", new Vector2(0,0), groundchain);
        //Underground
		new Ground(world, "underground" , new Vector2(0, -10), groundchain);

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
		
		//////UI///////
		stage = new Stage();
		table = new Group();
		
		Image heading = new Image(Assets.manager.get(Assets.title,Texture.class));
		float ratio=heading.getHeight()/heading.getWidth();
		heading.setSize(stage.getWidth()*3/4, stage.getWidth()*3/4*ratio);
		heading.setPosition(stage.getWidth()/2-heading.getWidth()/2, stage.getHeight());
		heading.addAction(sequence(delay(2),
				moveTo(stage.getWidth()/2-heading.getWidth()/2, stage.getHeight()*2/3-heading.getHeight()/2,1,pow2In),
				moveTo(stage.getWidth()/2-heading.getWidth()/2, stage.getHeight()*2/2.3f-heading.getHeight()/2,0.7f,pow2Out),
				moveTo(stage.getWidth()/2-heading.getWidth()/2, stage.getHeight()*2/3f-heading.getHeight()/2,0.7f,pow2In),
				moveTo(stage.getWidth()/2-heading.getWidth()/2, stage.getHeight()*2/2.5f-heading.getHeight()/2,0.5f,pow2Out),
				moveTo(stage.getWidth()/2-heading.getWidth()/2, stage.getHeight()*2/3f-heading.getHeight()/2,0.5f,pow2In),
				moveTo(stage.getWidth()/2-heading.getWidth()/2, stage.getHeight()*2/2.7f-heading.getHeight()/2,0.3f,pow2Out),
				moveTo(stage.getWidth()/2-heading.getWidth()/2, stage.getHeight()*2/3f-heading.getHeight()/2,0.3f,pow2In),
				forever(sequence(moveTo(stage.getWidth()/2-heading.getWidth()/2, stage.getHeight()*2/2.8f-heading.getHeight()/2,0.3f,pow2Out),
								 moveTo(stage.getWidth()/2-heading.getWidth()/2, stage.getHeight()*2/3f-heading.getHeight()/2,0.3f,pow2In)))
				));
		
		// creating buttons
		createlvlsel();

		
		/////////////////Button Arcade////////////////////
		Image buttonArcade = new Image(Assets.manager.get(Assets.challenge, Texture.class));
		buttonArcade.setSize(stage.getHeight()/4.5f, stage.getHeight()/4.5f);
		buttonArcade.setPosition(stage.getWidth()/2-buttonArcade.getWidth()/2, stage.getHeight()/3-buttonArcade.getHeight()/2);
		buttonArcade.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				if(Save.gd.uA1){
					lvltable.addAction(sequence(show2(), alpha(1,0.5f)));
					table.addAction(sequence(alpha(0,0.5f),hide2()));
					buttonset.addAction(sequence(alpha(0,0.5f),hide2()));
				}
				else if(!Save.gd.reward3 && !Save.gd.uA1) {
					Save.gd.reward3=true;
					Save.gd.silvercoins+=10;
					table.addAction(sequence(alpha(0,0.5f),hide2()));
					buttonset.addAction(sequence(alpha(0,0.5f),hide2()));
					final Label bonus= new Label("Well done little Monkey! \n here is a reward for your curiosity ^^", skin);
					bonus.setAlignment(Align.center);
					bonus.setPosition(stage.getWidth()/2-bonus.getWidth()/2, stage.getHeight()/2-bonus.getHeight()/2);
					bonus.getColor().a=0;
					final Image bg=new Image();
					bg.setSize(stage.getWidth(), stage.getHeight());
					bg.setVisible(false);
					bg.addListener(new ClickListener() {
						@Override
						public void clicked(InputEvent event, float x, float y) {
							if(Save.gd.sound)click.play();
							bonus.addAction(sequence(alpha(0,0.2f),removeActor()));
							bg.addAction(sequence(alpha(0,0.2f),removeActor()));
							lvltable.addAction(sequence(show2(), alpha(1,0.5f)));
						}
					});
					bonus.addAction(alpha(1,0.35f));
					bg.addAction(sequence(delay(0.25f), show2()));
					stage.addActor(bonus);
					stage.addActor(bg);
				}
				
			}
		});
		buttonArcade.addAction(sequence(delay(0.1f),forever(sequence(sizeTo(stage.getHeight()/4.8f, stage.getHeight()/4.2f,0.5f,sine),
										sizeTo(stage.getHeight()/4.2f, stage.getHeight()/4.8f,0.5f,sine)))));
		
		////////////////////////Button Adventure////////////////////////////
		Image buttonAventure = new Image(Assets.manager.get(Assets.map, Texture.class));
		buttonAventure.setSize(stage.getHeight()/4.5f, stage.getHeight()/4.5f);
		buttonAventure.setPosition(stage.getWidth()/4-buttonAventure.getWidth()/2, stage.getHeight()/3-buttonAventure.getHeight()/2);
		buttonAventure.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				black.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),
						fadeIn(0.25f),run(new Runnable() {
							@Override
							public void run() {
								((Game) Gdx.app.getApplicationListener()).setScreen(new WorldMap());
					}
				})));
			}
		});
		buttonAventure.addAction(sequence(delay(0),forever(sequence(sizeTo(stage.getHeight()/4.8f, stage.getHeight()/4.2f,0.5f,sine),
				sizeTo(stage.getHeight()/4.2f, stage.getHeight()/4.8f,0.5f,sine)))));
		
		
		/////////////////Button shop//////////////////////////
		Image shopButton = new Image(Assets.manager.get(Assets.shop, Texture.class));
		shopButton.setSize(stage.getHeight()/4.5f, stage.getHeight()/4.5f);
		shopButton.setPosition(stage.getWidth()*3/4-shopButton.getWidth()/2, stage.getHeight()/3-shopButton.getHeight()/2);
		shopButton .addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				shopgroup.addAction(sequence(moveTo(0,stage.getHeight()), alpha(1),show2(),moveTo(0,0, 0.5f, fade)));
			}
		});
		shopButton.addAction(sequence(delay(0.2f),forever(sequence(sizeTo(stage.getHeight()/4.8f, stage.getHeight()/4.2f,0.5f,sine),
				sizeTo(stage.getHeight()/4.2f, stage.getHeight()/4.8f,0.5f,sine)))));
		
		buttonAventure.addAction(sequence(
				moveTo(stage.getWidth()/4-buttonAventure.getWidth()/2, stage.getHeight()),
				delay(0),
				moveTo(stage.getWidth()/4-buttonAventure.getWidth()/2, stage.getHeight()/3-buttonAventure.getHeight()/2,1,pow2In)));
		if(Save.gd.uA1 || Save.gd.uAgom)
			buttonArcade.addAction(sequence(
					moveTo(stage.getWidth()/2-buttonArcade.getWidth()/2, stage.getHeight()),
					delay(0.5f),
					moveTo(stage.getWidth()/2-buttonArcade.getWidth()/2, stage.getHeight()/3-buttonArcade.getHeight()/2,1,pow2In)));
		else
			buttonArcade.addAction(sequence(
					moveTo(stage.getWidth()/2-buttonArcade.getWidth()/2, stage.getHeight()),
					delay(0.5f),
					moveTo(stage.getWidth()/2-buttonArcade.getWidth()/2, stage.getHeight()/3-buttonArcade.getHeight()/2,1,pow2In),
					moveTo(stage.getWidth()/2-buttonArcade.getWidth()/2, stage.getHeight(),1,pow2Out)));
		if(Save.gd.uspeed>0)
			shopButton.addAction(sequence(
					moveTo(stage.getWidth()*3/4-buttonAventure.getWidth()/2, stage.getHeight()),
					delay(1f),
					moveTo(stage.getWidth()*3/4-shopButton.getWidth()/2, stage.getHeight()/3-shopButton.getHeight()/2,1,pow2In)));
		else
			shopButton.addAction(sequence(
					moveTo(stage.getWidth()*3/4-buttonAventure.getWidth()/2, stage.getHeight()),
					delay(1f),
					moveTo(stage.getWidth()*3/4-shopButton.getWidth()/2, stage.getHeight()/3-shopButton.getHeight()/2,1,pow2In),
					moveTo(stage.getWidth()*3/4-buttonAventure.getWidth()/2, stage.getHeight(),1,pow2Out)));
		
		createsetb();
		
		// putting stuff together
		table.addActor(heading);
		table.addActor(buttonAventure);
		table.addActor(buttonArcade);
		table.addActor(shopButton);

		stage.addActor(table);
		black=new Image(skin.newDrawable("white", Color.BLACK));
		black.setSize(stage.getWidth(), stage.getHeight());
		black.addAction(sequence(delay(0.01f),alpha(0,0.25f,fade), com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
		stage.addActor(black);
		//Input processor
		Gdx.input.setInputProcessor(stage);
		
		createbank();
		createshop();
		
		stage.addActor(shopgroup);
		stage.addActor(buymenu);

		interManager.update(Gdx.graphics.getDeltaTime());
		
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
//		buttonstyle.over = skin.newDrawable("white", Color.DARK_GRAY);
		skin.add("default", buttonstyle);
		
		buttonstyle = new ButtonStyle(buttonstyle);
		buttonstyle.checked = skin.newDrawable("white", new Color(0x5287ccff));
		skin.add("toggle", buttonstyle);
		
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up   = skin.newDrawable("white", Color.BLACK);
		textButtonStyle.down = skin.newDrawable("white", new Color(0x416ba1ff));
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

	private void createbank(){
		
		buymenu = new Group();
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
		iaplist.addActor(cn2);
		iaplist.addActor(cn3);
		iaplist.addActor(cn4);
		iaplist.addActor(cn5);
		iaplist.addActor(cn6);
		iaplist.addActor(cn7);
		iaplist.addActor(cn8);
		
		Group coins1=new Group();
		float coinsize= stage.getHeight()/16;
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
		iaplist.addActor(buy2);
		iaplist.addActor(buy3);
		iaplist.addActor(buy4);
		iaplist.addActor(buy5);
		iaplist.addActor(buy6);
		iaplist.addActor(buy7);
		iaplist.addActor(buy8);
		
		iaplist.setSize(stage.getWidth(), buy2.getY()+buy2.getHeight()+4*pad-cn8.getY());
		ScrollPane buy=new ScrollPane(iaplist);
		buy.setBounds(0, backbt.getY()+backbt.getHeight()+pad, //x,y
				stage.getWidth(),                              //w
				buytitle.getY()-pad-(backbt.getY()+backbt.getHeight()+pad));//h
		buymenu.addActor(buy);
		
		backbt.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				buymenu.addAction(sequence(moveTo(0,stage.getHeight(),0.5f, fade), hide2()));
				//if(menuButton.isChecked()) mainMenu.addAction(sequence(show(), alpha(1, 0.5f, fade)));
			}
		});
		buy2.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				if(MoleGame.shop!=null)MoleGame.shop.buyCoin2();
			}
		});
		buy3.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				if(MoleGame.shop!=null)MoleGame.shop.buyCoin3();
			}
		});
		buy4.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				if(MoleGame.shop!=null)MoleGame.shop.buyCoin4();
			}
		});
		buy5.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				if(MoleGame.shop!=null)MoleGame.shop.buyCoin5();
			}
		});
		buy6.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				if(MoleGame.shop!=null)MoleGame.shop.buyCoin6();
			}
		});
		buy7.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				if(MoleGame.shop!=null)MoleGame.shop.buyCoin7();
			}
		});
		buy8.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				if(MoleGame.shop!=null)MoleGame.shop.buyCoin8();
			}
		});
		
		buymenu.getColor().a=0;
		buymenu.setVisible(false);
		
	}
	
	private Action hide2() {
		Action action=com.badlogic.gdx.scenes.scene2d.actions.Actions.hide();
		return action;
	}
	
	private Action show2() {
		Action action=com.badlogic.gdx.scenes.scene2d.actions.Actions.show();
		return action;
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
	
	private void createshop() {
		shopgroup = new Group();
		Image bgbuy1 = new Image(Assets.manager.get(Assets.uiback, Texture.class));
		bgbuy1.setSize(stage.getWidth(), stage.getHeight());
		bgbuy1.setPosition(0, 0);
		shopgroup.addActor(bgbuy1);
		
		Label shoptitle=new Label(MoleGame.myBundle.get("shop"), skin, "big");
		shoptitle.setPosition(stage.getWidth()/2-shoptitle.getWidth()/2, stage.getHeight()-shoptitle.getHeight()-2*pad);
		shopgroup.addActor(shoptitle);
		
		
		//////////==========GLOBAL=============//////////
		
		final Image goldcoinIm=new Image(Assets.manager.get(Assets.orbgold, Texture.class));
		goldcoinIm.setSize(stage.getHeight()/16,stage.getHeight()/16);
		goldcoinIm.setPosition(stage.getWidth()-goldcoinIm.getWidth()-2.5f*pad,stage.getHeight()-goldcoinIm.getHeight()-2.5f*pad);
		goldcoinIm.addAction(forever(sequence(alpha(0.5f,1f, sine),alpha(1,1f, sine))));
		shopgroup.addActor(goldcoinIm);
		final Label goldcoins=new Label(Integer.toString(Save.gd.goldcoins),skin);
		goldcoins.setPosition(
				stage.getWidth()-3.5f*pad-stage.getHeight()/16,
				stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
		goldcoins.addAction(forever(sequence(alpha(0.5f,1f, sine),alpha(1,1f, sine))));
		shopgroup.addActor(goldcoins);
		
		final Image silvcoinIm=new Image(Assets.manager.get(Assets.orbsilver, Texture.class));
		silvcoinIm.setSize(stage.getHeight()/16,stage.getHeight()/16);
		silvcoinIm.setPosition(stage.getWidth()-silvcoinIm.getWidth()-2.5f*pad,stage.getHeight()-silvcoinIm.getHeight()-2.5f*pad);
		silvcoinIm.addAction(forever(sequence(alpha(0.5f,1f, sine),alpha(1,1f, sine))));
		shopgroup.addActor(silvcoinIm);
		silvcoinsshop=new Label(Integer.toString(Save.gd.silvercoins),skin);
		silvcoinsshop.setPosition(
				stage.getWidth()-3.5f*pad-stage.getHeight()/16,
				stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
		silvcoinsshop.addAction(forever(sequence(alpha(0.5f,1f, sine),alpha(1,1f, sine))));
		shopgroup.addActor(silvcoinsshop);
		silvcoinIm.getColor().a=0;
		silvcoinsshop.getColor().a=0;
		silvcoinIm.setVisible(false);
		silvcoinsshop.setVisible(false);
		
		Image backbt = new Image(Assets.manager.get(Assets.back,Texture.class));
		backbt=new Image(Assets.manager.get(Assets.back, Texture.class));
		backbt.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
		backbt.setPosition(2*pad, 2*pad);
		shopgroup.addActor(backbt);
		
		final Image equib = new Image(Assets.manager.get(Assets.set, Texture.class));
		equib.setSize(stage.getHeight()*0.28f, stage.getHeight()*0.28f);
		equib.setPosition(pad, stage.getHeight()*3/4-equib.getHeight()/2);
		equib.setOrigin(Align.center);
		equib.addAction(forever(sequence(rotateTo(-15,0.5f, sine),rotateTo(15,0.5f, sine))));
    	shopgroup.addActor(equib);
		
    	final Image skinb = new Image(Assets.manager.get(Assets.shirt, Texture.class));
		skinb.setSize(stage.getHeight()*0.28f, stage.getHeight()*0.28f);
		skinb.setPosition(pad, stage.getHeight()*1/4);
		skinb.setOrigin(Align.center);
    	//equib.addAction(forever(sequence(rotateTo(-15,0.5f, sine),rotateTo(15,0.5f, sine))));
    	shopgroup.addActor(skinb);
    	
    	
    	//////////============EQUIPMENT==========////////////
    	
    	////Upgrade jet pack
		final Group equipgroup=new Group();
		Image jpbutton = new Image(Assets.manager.get(Assets.jetpack, Texture.class));
		jpbutton.setSize(stage.getHeight()*0.23f, stage.getHeight()*0.23f);
		jpbutton.setPosition(stage.getWidth()/4,  shoptitle.getY()-jpbutton.getHeight()-pad);
		final Image coinjp=new Image(Assets.manager.get(Assets.orbgold, Texture.class));
		coinjp.setSize(stage.getHeight()/16,stage.getHeight()/16);
		jpcost=Save.gd.ujp*200;
		final Label jpprice=new Label(Integer.toString(jpcost), skin, "default");
		final Label jpnumber = new Label(MoleGame.myBundle.get("lvl")+Integer.toString(Save.gd.ujp+1), skin);
		if(Save.gd.uplane==MAXUJP) jpnumber.setText("Max");
		coinjp.setPosition(jpbutton.getX()+jpbutton.getWidth()-coinjp.getWidth(),
				jpbutton.getY()-coinjp.getHeight()-pad/2);
		jpprice.setPosition(jpbutton.getX()+jpbutton.getWidth()-pad-coinjp.getWidth()-jpprice.getWidth(),
				coinjp.getY()+coinjp.getHeight()/2-jpprice.getHeight()/2f);
		jpnumber.setTouchable(Touchable.disabled);
		jpnumber.setAlignment(Align.center);
		jpnumber.setPosition(jpbutton.getX()+jpbutton.getWidth()/2-jpnumber.getWidth()/2,
				jpbutton.getY()+jpbutton.getWidth()/2-jpnumber.getHeight()/2);
		jpnumber.getColor().a=0.7f;
		equipgroup.addActor(jpbutton);
		equipgroup.addActor(coinjp);
		equipgroup.addActor(jpprice);
		equipgroup.addActor(jpnumber);
		jpbutton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				if(Save.gd.ujp<MAXUJP && Save.gd.goldcoins>=jpcost ){
					showbuy();
				}
				else {
					if(Save.gd.sound) no.play(1,0.65f,0);
					goldcoins.addAction(sequence(color(Color.RED,0.1f),color(Color.WHITE,0.1f),
							color(Color.RED,0.1f),color(Color.WHITE,0.1f)));
				}
				
			}
			private void showbuy() {
				final Group comfirm=new Group();
				Image bgbuy1 = new Image(Assets.manager.get(Assets.uiback, Texture.class));
				bgbuy1.setSize(stage.getWidth()*2/3, stage.getHeight()*2/3);
				bgbuy1.setPosition(stage.getWidth()*1/6, stage.getHeight()*1/6);
				Image bgbuybg = new Image(skin.getDrawable("white"));
				bgbuybg.getColor().a=0;
				bgbuybg.setSize(stage.getWidth(), stage.getHeight());
				bgbuybg.setPosition(0, 0);
				final Label spendgoldlabel=new Label(MoleGame.myBundle.get("upgrade1")+Integer.toString(jpcost)+
						MoleGame.myBundle.get("goldquest"), skin, "default");
				spendgoldlabel.setPosition(stage.getWidth()/2,stage.getHeight()/2+4*pad,Align.center);
				
				final Image yesb=new Image(Assets.manager.get(Assets.gover2, Texture.class));
				yesb.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
				yesb.setPosition(stage.getWidth()/2-yesb.getWidth()-4*pad, stage.getHeight()/2-4*pad);
				
				final Image nob=new Image(Assets.manager.get(Assets.gover, Texture.class));
				nob.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
				nob.setPosition(stage.getWidth()/2+4*pad, stage.getHeight()/2-4*pad);
				
				bgbuybg.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(Save.gd.sound)click.play();
						comfirm.clear();
						comfirm.remove();
						
					}
				});
				
				yesb.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(Save.gd.sound)click.play();
						Save.gd.goldcoins-=jpcost;
						Save.gd.ujp++;
						goldcoins.setText(Integer.toString(Save.gd.goldcoins));
						goldcoins.pack();
						goldcoins.setPosition(
								stage.getWidth()-3.5f*pad-stage.getHeight()/16,
								stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
						comfirm.clear();
						comfirm.remove();
						if(Save.gd.ujp<MAXUJP){
							jpnumber.setText(MoleGame.myBundle.get("lvl")+Integer.toString(Save.gd.ujp+1));
							jpcost=Save.gd.ujp*200;
							jpprice.setText(Integer.toString(jpcost));
						}
						else {
							jpprice.remove();
							coinjp.remove();
							jpnumber.setText("Max");
						}
					}
				});
				
				nob.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(Save.gd.sound)click.play();
						comfirm.clear();
						comfirm.remove();
						
					}
				});
				
				comfirm.addActor(bgbuybg);
				comfirm.addActor(bgbuy1);
				comfirm.addActor(spendgoldlabel);
				comfirm.addActor(yesb);
				comfirm.addActor(nob);
				stage.addActor(comfirm);
			}
		});
		if(Save.gd.ujp==0){
			jpbutton.setVisible(false);
			coinjp.setVisible(false);
			jpprice.setVisible(false);
			jpnumber.setVisible(false);
		}
		else if(Save.gd.ujp==MAXUJP){
			coinjp.setVisible(false);
			jpprice.setVisible(false);
		}
		
		/////// Upgrade plane
		Image planebutton = new Image(Assets.manager.get(Assets.plane, Texture.class));
		planebutton.setSize(stage.getHeight()*0.23f, stage.getHeight()*0.23f);
		planebutton.setPosition(stage.getWidth()/4+(planebutton.getWidth()+7*pad),  shoptitle.getY()-planebutton.getHeight()-pad);
		final Image coinplane=new Image(Assets.manager.get(Assets.orbgold, Texture.class));
		coinplane.setSize(stage.getHeight()/16,stage.getHeight()/16);
		
		planecost=Save.gd.uplane*200;
		
		final Label planeprice=new Label(Integer.toString(planecost), skin, "default");
		final Label planenumber = new Label(MoleGame.myBundle.get("lvl")+Integer.toString(Save.gd.uplane+1), skin);
		if(Save.gd.uplane==MAXUPLANE) planenumber.setText("Max");
		coinplane.setPosition(planebutton.getX()+planebutton.getWidth()-coinplane.getWidth(),
				planebutton.getY()-coinplane.getHeight()-pad/2);
		planeprice.setPosition(planebutton.getX()+planebutton.getWidth()-pad-coinplane.getWidth()-planeprice.getWidth(),
				coinplane.getY()+coinplane.getHeight()/2-planeprice.getHeight()/2f);
		planenumber.setTouchable(Touchable.disabled);
		planenumber.setAlignment(Align.center);
		planenumber.setPosition(planebutton.getX()+planebutton.getWidth()/2-planenumber.getWidth()/2,
				planebutton.getY()+planebutton.getWidth()/2-planenumber.getHeight()/2);
		planenumber.getColor().a=0.7f;
		equipgroup.addActor(planebutton);
		equipgroup.addActor(coinplane);
		equipgroup.addActor(planeprice);
		equipgroup.addActor(planenumber);
		planebutton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				if(Save.gd.uplane<MAXUPLANE && Save.gd.goldcoins>=planecost ){
					showbuy();
				}
				else {
					if(Save.gd.sound)no.play(1,0.65f,0);
					goldcoins.addAction(sequence(color(Color.RED,0.1f),color(Color.WHITE,0.1f),
							color(Color.RED,0.1f),color(Color.WHITE,0.1f)));
				}
				
			}
			private void showbuy() {
				final Group comfirm=new Group();
				Image bgbuy1 = new Image(Assets.manager.get(Assets.uiback, Texture.class));
				bgbuy1.setSize(stage.getWidth()*2/3, stage.getHeight()*2/3);
				bgbuy1.setPosition(stage.getWidth()*1/6, stage.getHeight()*1/6);
				Image bgbuybg = new Image(skin.getDrawable("white"));
				bgbuybg.getColor().a=0;
				bgbuybg.setSize(stage.getWidth(), stage.getHeight());
				bgbuybg.setPosition(0, 0);
				final Label spendgoldlabel=new Label(MoleGame.myBundle.get("upgrade1")+Integer.toString(planecost)+
						MoleGame.myBundle.get("goldquest"), skin, "default");
				spendgoldlabel.setPosition(stage.getWidth()/2,stage.getHeight()/2+4*pad,Align.center);
				
				final Image yesb=new Image(Assets.manager.get(Assets.gover2, Texture.class));
				yesb.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
				yesb.setPosition(stage.getWidth()/2-yesb.getWidth()-4*pad, stage.getHeight()/2-4*pad);
				
				final Image nob=new Image(Assets.manager.get(Assets.gover, Texture.class));
				nob.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
				nob.setPosition(stage.getWidth()/2+4*pad, stage.getHeight()/2-4*pad);
				
				bgbuybg.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(Save.gd.sound)click.play();
						comfirm.clear();
						comfirm.remove();
						
					}
				});
				
				yesb.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(Save.gd.sound)click.play();
						Save.gd.goldcoins-=planecost;
						Save.gd.uplane++;
						goldcoins.setText(Integer.toString(Save.gd.goldcoins));
						goldcoins.pack();
						goldcoins.setPosition(
								stage.getWidth()-3.5f*pad-stage.getHeight()/16,
								stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
						comfirm.clear();
						comfirm.remove();
						if(Save.gd.uplane<MAXUPLANE){
							planenumber.setText(MoleGame.myBundle.get("lvl")+Integer.toString(Save.gd.uplane+1));
							planecost=Save.gd.uplane*200;
							planeprice.setText(Integer.toString(planecost));
						}
						else {
							planeprice.remove();
							coinplane.remove();
							planenumber.setText("Max");
						}
					}
				});
				
				nob.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(Save.gd.sound)click.play();
						comfirm.clear();
						comfirm.remove();
						
					}
				});
				
				comfirm.addActor(bgbuybg);
				comfirm.addActor(bgbuy1);
				comfirm.addActor(spendgoldlabel);
				comfirm.addActor(yesb);
				comfirm.addActor(nob);
				stage.addActor(comfirm);
			}
		});
		if(Save.gd.uplane==0){
			planebutton.setVisible(false);
			coinplane.setVisible(false);
			planeprice.setVisible(false);
			planenumber.setVisible(false);
		}
		else if(Save.gd.uplane==MAXUPLANE){
			coinplane.setVisible(false);
			planeprice.setVisible(false);
		}
		
		///Upgrade time slow
		Image timebutton = new Image(Assets.manager.get(Assets.time, Texture.class));
		timebutton.setSize(stage.getHeight()*0.23f, stage.getHeight()*0.23f);
		timebutton.setPosition(stage.getWidth()/4+2*(timebutton.getWidth()+7*pad),  shoptitle.getY()-timebutton.getHeight()-pad);
		final Image cointime=new Image(Assets.manager.get(Assets.orbgold, Texture.class));
		cointime.setSize(stage.getHeight()/16,stage.getHeight()/16);
		
		timecost=Save.gd.utime*200;
		
		final Label timeprice=new Label(Integer.toString(timecost), skin, "default");
		final Label timenumber = new Label(MoleGame.myBundle.get("lvl")+Integer.toString(Save.gd.utime+1), skin);
		if(Save.gd.utime==MAXUTIME) timenumber.setText("Max");
		cointime.setPosition(timebutton.getX()+timebutton.getWidth()-cointime.getWidth(),
				timebutton.getY()-cointime.getHeight()-pad/2);
		timeprice.setPosition(timebutton.getX()+timebutton.getWidth()-pad-cointime.getWidth()-timeprice.getWidth(),
				cointime.getY()+cointime.getHeight()/2-timeprice.getHeight()/2f);
		timenumber.setTouchable(Touchable.disabled);
		timenumber.setAlignment(Align.center);
		timenumber.setPosition(timebutton.getX()+timebutton.getWidth()/2-timenumber.getWidth()/2,
				timebutton.getY()+timebutton.getWidth()/2-timenumber.getHeight()/2);
		timenumber.getColor().a=0.7f;
		equipgroup.addActor(timebutton);
		equipgroup.addActor(cointime);
		equipgroup.addActor(timeprice);
		equipgroup.addActor(timenumber);
		timebutton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				if(Save.gd.utime<MAXUTIME && Save.gd.goldcoins>=timecost ){
					showbuy();
				}
				else {
					if(Save.gd.sound)no.play(1,0.65f,0);
					goldcoins.addAction(sequence(color(Color.RED,0.1f),color(Color.WHITE,0.1f),
							color(Color.RED,0.1f),color(Color.WHITE,0.1f)));
				}
				
			}
			private void showbuy() {
				final Group comfirm=new Group();
				Image bgbuy1 = new Image(Assets.manager.get(Assets.uiback, Texture.class));
				bgbuy1.setSize(stage.getWidth()*2/3, stage.getHeight()*2/3);
				bgbuy1.setPosition(stage.getWidth()*1/6, stage.getHeight()*1/6);
				Image bgbuybg = new Image(skin.getDrawable("white"));
				bgbuybg.getColor().a=0;
				bgbuybg.setSize(stage.getWidth(), stage.getHeight());
				bgbuybg.setPosition(0, 0);
				final Label spendgoldlabel=new Label(MoleGame.myBundle.get("upgrade1")+Integer.toString(timecost)+
						MoleGame.myBundle.get("goldquest"), skin, "default");
				spendgoldlabel.setPosition(stage.getWidth()/2,stage.getHeight()/2+4*pad,Align.center);
				
				final Image yesb=new Image(Assets.manager.get(Assets.gover2, Texture.class));
				yesb.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
				yesb.setPosition(stage.getWidth()/2-yesb.getWidth()-4*pad, stage.getHeight()/2-4*pad);
				
				final Image nob=new Image(Assets.manager.get(Assets.gover, Texture.class));
				nob.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
				nob.setPosition(stage.getWidth()/2+4*pad, stage.getHeight()/2-4*pad);
				
				bgbuybg.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(Save.gd.sound)click.play();
						comfirm.clear();
						comfirm.remove();
						
					}
				});
				
				yesb.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(Save.gd.sound)click.play();
						Save.gd.goldcoins-=timecost;
						Save.gd.utime++;
						goldcoins.setText(Integer.toString(Save.gd.goldcoins));
						goldcoins.pack();
						goldcoins.setPosition(
								stage.getWidth()-3.5f*pad-stage.getHeight()/16,
								stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
						comfirm.clear();
						comfirm.remove();
						if(Save.gd.utime<MAXUTIME){
							timenumber.setText(MoleGame.myBundle.get("lvl")+Integer.toString(Save.gd.utime+1));
							timecost=Save.gd.utime*200;
							timeprice.setText(Integer.toString(timecost));
						}
						else {
							timeprice.remove();
							cointime.remove();
							timenumber.setText("Max");
						}
					}
				});
				
				nob.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(Save.gd.sound)click.play();
						comfirm.clear();
						comfirm.remove();
						
					}
				});
				
				comfirm.addActor(bgbuybg);
				comfirm.addActor(bgbuy1);
				comfirm.addActor(spendgoldlabel);
				comfirm.addActor(yesb);
				comfirm.addActor(nob);
				stage.addActor(comfirm);
			}
		});
		if(Save.gd.utime==0){
			timebutton.setVisible(false);
			cointime.setVisible(false);
			timeprice.setVisible(false);
			timenumber.setVisible(false);
		}
		else if(Save.gd.utime==MAXUTIME){
			cointime.setVisible(false);
			timeprice.setVisible(false);
		}
		
		///Upgrade speed
		Image speedbutton = new Image(Assets.manager.get(Assets.gover2, Texture.class));
		speedbutton.setSize(stage.getHeight()*0.23f, stage.getHeight()*0.23f);
		speedbutton.setPosition(stage.getWidth()/4,  coinjp.getY()-speedbutton.getHeight()-2*pad);
		final Image coinspeed=new Image(Assets.manager.get(Assets.orbgold, Texture.class));
		coinspeed.setSize(stage.getHeight()/16,stage.getHeight()/16);
		
		speedcost=(Save.gd.bspeed+1)*100;
		
		final Label speedprice=new Label(Integer.toString(speedcost), skin, "default");
		final Label speednumber = new Label(MoleGame.myBundle.get("speed")+"\n"+
		MoleGame.myBundle.get("lvl") +Integer.toString(Save.gd.bspeed+Save.gd.uspeed+1), skin);
		coinspeed.setPosition(speedbutton.getX()+speedbutton.getWidth()-coinspeed.getWidth(),
				speedbutton.getY()-coinspeed.getHeight()-pad/2);
		speedprice.setPosition(speedbutton.getX()+speedbutton.getWidth()-pad-coinspeed.getWidth()-speedprice.getWidth(),
				coinspeed.getY()+coinspeed.getHeight()/2-speedprice.getHeight()/2f);
		speednumber.setTouchable(Touchable.disabled);
		speednumber.setAlignment(Align.center);
		speednumber.setPosition(speedbutton.getX()+speedbutton.getWidth()/2-speednumber.getWidth()/2,
				speedbutton.getY()+speedbutton.getWidth()/2-speednumber.getHeight()/2);
		speednumber.getColor().a=0.7f;
		equipgroup.addActor(speedbutton);
		equipgroup.addActor(coinspeed);
		equipgroup.addActor(speedprice);
		equipgroup.addActor(speednumber);
		speedbutton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				if(Save.gd.bspeed<MAXBSPEED && Save.gd.goldcoins>=speedcost ){
					showbuy();
				}
				else {
					if(Save.gd.sound)no.play(1,0.65f,0);
					goldcoins.addAction(sequence(color(Color.RED,0.1f),color(Color.WHITE,0.1f),
							color(Color.RED,0.1f),color(Color.WHITE,0.1f)));
				}
				
			}
			private void showbuy() {
				final Group comfirm=new Group();
				Image bgbuy1 = new Image(Assets.manager.get(Assets.uiback, Texture.class));
				bgbuy1.setSize(stage.getWidth()*2/3, stage.getHeight()*2/3);
				bgbuy1.setPosition(stage.getWidth()*1/6, stage.getHeight()*1/6);
				Image bgbuybg = new Image(skin.getDrawable("white"));
				bgbuybg.getColor().a=0;
				bgbuybg.setSize(stage.getWidth(), stage.getHeight());
				bgbuybg.setPosition(0, 0);
				final Label spendgoldlabel=new Label(MoleGame.myBundle.get("upgrade1")+Integer.toString(speedcost)+
						MoleGame.myBundle.get("goldquest"), skin, "default");
				spendgoldlabel.setPosition(stage.getWidth()/2,stage.getHeight()/2+4*pad,Align.center);
				
				final Image yesb=new Image(Assets.manager.get(Assets.gover2, Texture.class));
				yesb.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
				yesb.setPosition(stage.getWidth()/2-yesb.getWidth()-4*pad, stage.getHeight()/2-4*pad);
				
				final Image nob=new Image(Assets.manager.get(Assets.gover, Texture.class));
				nob.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
				nob.setPosition(stage.getWidth()/2+4*pad, stage.getHeight()/2-4*pad);
				
				bgbuybg.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(Save.gd.sound)click.play();
						comfirm.clear();
						comfirm.remove();
						
					}
				});
				
				yesb.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(Save.gd.sound)click.play();
						Save.gd.goldcoins-=speedcost;
						Save.gd.bspeed++;
						goldcoins.setText(Integer.toString(Save.gd.goldcoins));
						goldcoins.pack();
						goldcoins.setPosition(
								stage.getWidth()-3.5f*pad-stage.getHeight()/16,
								stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
						comfirm.clear();
						comfirm.remove();
						if(Save.gd.bspeed<MAXBSPEED){
							speednumber.setText(MoleGame.myBundle.get("speed") +"\n"+
						MoleGame.myBundle.get("lvl")+Integer.toString(Save.gd.bspeed+1));
							speedcost=(Save.gd.bspeed+1)*100;
							speedprice.setText(Integer.toString(speedcost));
						}
						else {
							speedprice.remove();
							coinspeed.remove();
							speednumber.setText("Max");
						}
					}
				});
				
				nob.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(Save.gd.sound)click.play();
						comfirm.clear();
						comfirm.remove();
						
					}
				});
				
				comfirm.addActor(bgbuybg);
				comfirm.addActor(bgbuy1);
				comfirm.addActor(spendgoldlabel);
				comfirm.addActor(yesb);
				comfirm.addActor(nob);
				stage.addActor(comfirm);
			}
		});
		if(Save.gd.uspeed==0){
			speedbutton.setVisible(false);
			coinspeed.setVisible(false);
			speedprice.setVisible(false);
			speednumber.setVisible(false);
		}
		else if(Save.gd.bspeed==MAXBSPEED){
			coinspeed.setVisible(false);
			speedprice.setVisible(false);
		}
		
		///Upgrade control
		Image controlbutton = new Image(Assets.manager.get(Assets.gover2, Texture.class));
		controlbutton.setSize(stage.getHeight()*0.23f, stage.getHeight()*0.23f);
		controlbutton.setPosition(stage.getWidth()/4 +(planebutton.getWidth()+7*pad),  coinjp.getY()-controlbutton.getHeight()-2*pad);
		final Image coincontrol=new Image(Assets.manager.get(Assets.orbgold, Texture.class));
		coincontrol.setSize(stage.getHeight()/16,stage.getHeight()/16);
		
		controlcost=(Save.gd.bcontrol+1)*100;
		
		final Label controlprice=new Label(Integer.toString(controlcost), skin, "default");
		final Label controlnumber = new Label(MoleGame.myBundle.get("control")+"\n"+ 
				MoleGame.myBundle.get("lvl")+Integer.toString(Save.gd.bcontrol+Save.gd.ucontrol+1), skin);
		coincontrol.setPosition(controlbutton.getX()+controlbutton.getWidth()-coincontrol.getWidth(),
				controlbutton.getY()-coincontrol.getHeight()-pad/2);
		controlprice.setPosition(controlbutton.getX()+controlbutton.getWidth()-pad-coincontrol.getWidth()-controlprice.getWidth(),
				coincontrol.getY()+coincontrol.getHeight()/2-controlprice.getHeight()/2f);
		controlnumber.setTouchable(Touchable.disabled);
		controlnumber.setAlignment(Align.center);
		controlnumber.setPosition(controlbutton.getX()+controlbutton.getWidth()/2-controlnumber.getWidth()/2,
				controlbutton.getY()+controlbutton.getWidth()/2-controlnumber.getHeight()/2);
		controlnumber.getColor().a=0.7f;
		equipgroup.addActor(controlbutton);
		equipgroup.addActor(coincontrol);
		equipgroup.addActor(controlprice);
		equipgroup.addActor(controlnumber);
		controlbutton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				if(Save.gd.bcontrol<MAXBCONTROL && Save.gd.goldcoins>=controlcost ){
					showbuy();
				}
				else {
					if(Save.gd.sound)no.play(1,0.65f,0);
					goldcoins.addAction(sequence(color(Color.RED,0.1f),color(Color.WHITE,0.1f),
							color(Color.RED,0.1f),color(Color.WHITE,0.1f)));
				}
				
			}
			private void showbuy() {
				final Group comfirm=new Group();
				Image bgbuy1 = new Image(Assets.manager.get(Assets.uiback, Texture.class));
				bgbuy1.setSize(stage.getWidth()*2/3, stage.getHeight()*2/3);
				bgbuy1.setPosition(stage.getWidth()*1/6, stage.getHeight()*1/6);
				Image bgbuybg = new Image(skin.getDrawable("white"));
				bgbuybg.getColor().a=0;
				bgbuybg.setSize(stage.getWidth(), stage.getHeight());
				bgbuybg.setPosition(0, 0);
				final Label spendgoldlabel=new Label(MoleGame.myBundle.get("upgrade1")+Integer.toString(controlcost)+
						MoleGame.myBundle.get("goldquest"), skin, "default");
				spendgoldlabel.setPosition(stage.getWidth()/2,stage.getHeight()/2+4*pad,Align.center);
				
				final Image yesb=new Image(Assets.manager.get(Assets.gover2, Texture.class));
				yesb.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
				yesb.setPosition(stage.getWidth()/2-yesb.getWidth()-4*pad, stage.getHeight()/2-4*pad);
				
				final Image nob=new Image(Assets.manager.get(Assets.gover, Texture.class));
				nob.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
				nob.setPosition(stage.getWidth()/2+4*pad, stage.getHeight()/2-4*pad);
				
				bgbuybg.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(Save.gd.sound)click.play();
						comfirm.clear();
						comfirm.remove();
						
					}
				});
				
				yesb.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(Save.gd.sound)click.play();
						Save.gd.goldcoins-=controlcost;
						Save.gd.bcontrol++;
						goldcoins.setText(Integer.toString(Save.gd.goldcoins));
						goldcoins.pack();
						goldcoins.setPosition(
								stage.getWidth()-3.5f*pad-stage.getHeight()/16,
								stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
						comfirm.clear();
						comfirm.remove();
						if(Save.gd.bcontrol<MAXBCONTROL){
							controlnumber.setText(MoleGame.myBundle.get("control")+ "\n"+
									MoleGame.myBundle.get("lvl")+Integer.toString(Save.gd.bcontrol+1));
							controlcost=(Save.gd.bcontrol+1)*100;
							controlprice.setText(Integer.toString(controlcost));
						}
						else {
							controlprice.remove();
							coincontrol.remove();
							controlnumber.setText("Max");
						}
					}
				});
				
				nob.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(Save.gd.sound)click.play();
						comfirm.clear();
						comfirm.remove();
						
					}
				});
				
				comfirm.addActor(bgbuybg);
				comfirm.addActor(bgbuy1);
				comfirm.addActor(spendgoldlabel);
				comfirm.addActor(yesb);
				comfirm.addActor(nob);
				stage.addActor(comfirm);
			}
		});
		if(Save.gd.ucontrol==0){
			controlbutton.setVisible(false);
			coincontrol.setVisible(false);
			controlprice.setVisible(false);
			controlnumber.setVisible(false);
		}
		else if(Save.gd.bcontrol==MAXBCONTROL){
			coincontrol.setVisible(false);
			controlprice.setVisible(false);
		}
		
		shopgroup.addActor(equipgroup);
    	
    	
		/////=======================================SKINS===============================================///////////
		
		final Group itemslist= new Group();
		
		//////======= SKIN DEFAULT ========/////
		//button
		final Image molebutton = new Image(Assets.manager.get(Assets.empty, Texture.class));
		if(Save.gd.skin==MoleGame.CLASSIC)
			molebutton.setColor(Color.GREEN);
		molebutton.setSize(stage.getHeight()*0.23f, stage.getHeight()*0.23f);
		molebutton.setPosition(0,  5*(molebutton.getHeight()+pad)-(pad+molebutton.getHeight())); //y=item number*(button size+pad)-button height
		//Image
		Image moleim = new Image(Assets.manager.get(Assets.skin0ic, Texture.class));
		moleim.setSize(stage.getHeight()*0.23f, stage.getHeight()*0.23f);
		moleim.setPosition(0,  5*(molebutton.getHeight()+pad)-(pad+molebutton.getHeight()));
		moleim.setTouchable(Touchable.disabled);
		//description
		final Label skin0desc=new Label(MoleGame.myBundle.get("skin0"), skin, "default");
		skin0desc.setPosition(molebutton.getX()+molebutton.getWidth()+pad,
				molebutton.getY()+molebutton.getHeight()/2-skin0desc.getHeight()/2f);
		itemslist.addActor(molebutton);
		itemslist.addActor(moleim);
		itemslist.addActor(skin0desc);
		
		//////========== SKIN OLD MOLE ==========//////
		//Button
		final Image oldmolebutton = new Image(Assets.manager.get(Assets.empty, Texture.class));
		if(Save.gd.skin==MoleGame.OLDMOLE)
			oldmolebutton.setColor(Color.GREEN);
		oldmolebutton.setSize(stage.getHeight()*0.23f, stage.getHeight()*0.23f);
		oldmolebutton.setPosition(0,  5*(molebutton.getHeight()+pad)-2*(pad+oldmolebutton.getHeight())); 
		//image
		Image oldmoleim = new Image(Assets.manager.get(Assets.skin1ic, Texture.class));
		oldmoleim.setSize(stage.getHeight()*0.23f, stage.getHeight()*0.23f);
		oldmoleim.setPosition(0,  5*(molebutton.getHeight()+pad)-2*(pad+molebutton.getHeight()));
		oldmoleim.setTouchable(Touchable.disabled);
		//Description
		final Label skin1desc=new Label(MoleGame.myBundle.get("skin1"), skin, "default");
		skin1desc.setPosition(oldmolebutton.getX()+oldmolebutton.getWidth()+pad,
				oldmolebutton.getY()+oldmolebutton.getHeight()/2-skin1desc.getHeight()/2f);
		//Price
		final Group skin1price = price(Integer.toString(SKIN1PRICE), false);
		skin1price.setPosition(stage.getWidth()*3/4-skin1price.getWidth()-stage.getHeight()/32-2.5f*pad,
				5*(molebutton.getHeight()+pad)-2*(pad+molebutton.getHeight())+skin1price.getHeight()+pad/2);
		
		itemslist.addActor(oldmolebutton);
		itemslist.addActor(oldmoleim);
		itemslist.addActor(skin1price);
		itemslist.addActor(skin1desc);
		if(Save.gd.skin1owned){
			skin1price.setVisible(false);
		}
		
		////======== NO HAT ======/////
		///button
		final Image hat0button = new Image(Assets.manager.get(Assets.empty, Texture.class));
		if(Save.gd.hat==0)
			hat0button.setColor(Color.GREEN);
		hat0button.setSize(stage.getHeight()*0.23f, stage.getHeight()*0.23f);
		hat0button.setPosition(0,  5*(molebutton.getHeight()+pad)-3*(pad+hat0button.getHeight()));
		//description
		final Label hat0desc=new Label(MoleGame.myBundle.get("hat0") , skin, "default");
		hat0desc.setPosition(hat0button.getX()+hat0button.getWidth()+pad,
				hat0button.getY()+hat0button.getHeight()/2-hat0desc.getHeight()/2f);
		itemslist.addActor(hat0button);
		itemslist.addActor(hat0desc);
		
		//////======== HAT 1============///////
		//button
		final Image hat1button = new Image(Assets.manager.get(Assets.empty, Texture.class));
		if(Save.gd.hat==1)
			hat1button.setColor(Color.GREEN);
		hat1button.setSize(stage.getHeight()*0.23f, stage.getHeight()*0.23f);
		hat1button.setPosition(0,  5*(molebutton.getHeight()+pad)-4*(pad+hat1button.getHeight()));
		//Image
		Image hat1im = new Image(Assets.manager.get(Assets.hat1, Texture.class));
		hat1im.setSize(stage.getHeight()*0.23f, stage.getHeight()*0.23f);
		hat1im.setPosition(0,  5*(molebutton.getHeight()+pad)-4*(pad+molebutton.getHeight()));
		hat1im.setTouchable(Touchable.disabled);
		//description
		final Label hat1desc=new Label(MoleGame.myBundle.get("hat1"), skin, "default");
		hat1desc.setPosition(hat1button.getX()+hat1button.getWidth()+pad,
				hat1button.getY()+hat1button.getHeight()/2-hat1desc.getHeight()/2f);
		//Price
		final Group hat1price = price(Integer.toString(HAT1PRICE), false);
		hat1price.setPosition(stage.getWidth()*3/4-hat1price.getWidth()-stage.getHeight()/32-2.5f*pad,
				5*(molebutton.getHeight()+pad)-4*(pad+molebutton.getHeight())+hat1price.getHeight()+pad/2);
		if(Save.gd.hat1own){
			hat1price.setVisible(false);
		}
		itemslist.addActor(hat1price);
		itemslist.addActor(hat1button);
		itemslist.addActor(hat1im);
		itemslist.addActor(hat1desc);
		
		//////////=========== HAT 2=============////
		//button
		final Image hat2button = new Image(Assets.manager.get(Assets.empty, Texture.class));
		if(Save.gd.hat==2)
			hat2button.setColor(Color.GREEN);
		hat2button.setSize(stage.getHeight()*0.23f, stage.getHeight()*0.23f);
		hat2button.setPosition(0,  5*(molebutton.getHeight()+pad)-5*(pad+hat2button.getHeight()));
		//image
		Image hat2im = new Image(Assets.manager.get(Assets.hat2, Texture.class));
		hat2im.setSize(stage.getHeight()*0.23f, stage.getHeight()*0.23f);
		hat2im.setPosition(0,  5*(molebutton.getHeight()+pad)-5*(pad+molebutton.getHeight()));
		hat2im.setTouchable(Touchable.disabled);
		//description
		final Label hat2desc=new Label(MoleGame.myBundle.get("hat2"), skin, "default");
		hat2desc.setPosition(hat2button.getX()+hat2button.getWidth()+pad,
				hat2button.getY()+hat2button.getHeight()/2-hat2desc.getHeight()/2f);
		//price
		final Group hat2price = price(Integer.toString(HAT2PRICE), false);
		hat2price.setPosition(stage.getWidth()*3/4-hat2price.getWidth()-stage.getHeight()/32-2.5f*pad,
				5*(molebutton.getHeight()+pad)-5*(pad+molebutton.getHeight())+hat2price.getHeight()+pad/2);
		if(Save.gd.hat2own){
			hat2price.setVisible(false);
		}
		itemslist.addActor(hat2price);
		itemslist.addActor(hat2button);
		itemslist.addActor(hat2im);
		itemslist.addActor(hat2price);
		itemslist.addActor(hat2desc);
		///listener
		molebutton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				Save.gd.skin=MoleGame.CLASSIC;
				molebutton.setColor(Color.GREEN);
				oldmolebutton.setColor(Color.WHITE);
			}
		});
		
		hat0button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				Save.gd.hat=0;
				hat0button.setColor(Color.GREEN);
				hat1button.setColor(Color.WHITE);
				hat2button.setColor(Color.WHITE);
			}
		});
		
		oldmolebutton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				if(Save.gd.skin1owned){
					Save.gd.skin=MoleGame.OLDMOLE;
					oldmolebutton.setColor(Color.GREEN);
					molebutton.setColor(Color.WHITE);
				}
				else 
					showbuy();
			}

			private void showbuy() {
				final Group comfirm=new Group();
				Image bgbuy1 = new Image(Assets.manager.get(Assets.uiback, Texture.class));
				bgbuy1.setSize(stage.getWidth()*2/3, stage.getHeight()*2/3);bgbuy1.setPosition(stage.getWidth()*1/6, stage.getHeight()*1/6);
				Image bgbuybg = new Image(skin.getDrawable("white"));
				bgbuybg.getColor().a=0;
				bgbuybg.setSize(stage.getWidth(), stage.getHeight());
				bgbuybg.setPosition(0, 0);
				final Label spendgoldlabel=new Label(MoleGame.myBundle.get("buy1")+Integer.toString(SKIN1PRICE)+
						MoleGame.myBundle.get("silvquest"), skin, "default");
				spendgoldlabel.setPosition(stage.getWidth()/2,stage.getHeight()/2+4*pad,Align.center);
				
				final Image yesb=new Image(Assets.manager.get(Assets.gover2, Texture.class));
				yesb.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
				yesb.setPosition(stage.getWidth()/2-yesb.getWidth()-4*pad, stage.getHeight()/2-4*pad);
				
				final Image nob=new Image(Assets.manager.get(Assets.gover, Texture.class));
				nob.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
				nob.setPosition(stage.getWidth()/2+4*pad, stage.getHeight()/2-4*pad);
				
				bgbuybg.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(Save.gd.sound)click.play();
						comfirm.clear();
						comfirm.remove();
						
					}
				});
				
				yesb.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(Save.gd.sound)click.play();
						if(Save.gd.silvercoins>=SKIN1PRICE){
							Save.gd.silvercoins-=SKIN1PRICE;
							silvcoinsshop.setText(Integer.toString(Save.gd.silvercoins));
							silvcoinsshop.pack();
							silvcoinsshop.setPosition(
									stage.getWidth()-3.5f*pad-stage.getHeight()/16,
									stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
							itemslist.removeActor(skin1price);
							Save.gd.skin1owned=true;
							comfirm.clear();
							comfirm.remove();
							Save.gd.skin=MoleGame.OLDMOLE;
							oldmolebutton.setColor(Color.GREEN);
							molebutton.setColor(Color.WHITE);
						}
						else{
							buymenu.addAction(sequence(moveTo(0,stage.getHeight()), alpha(1),show2(),moveTo(0,0, 0.5f, fade)));
							buymenu.toFront();
						}
					}
				});
				
				nob.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(Save.gd.sound)click.play();
						comfirm.clear();
						comfirm.remove();
						
					}
				});
				
				comfirm.addActor(bgbuybg);
				comfirm.addActor(bgbuy1);
				comfirm.addActor(spendgoldlabel);
				comfirm.addActor(yesb);
				comfirm.addActor(nob);
				stage.addActor(comfirm);
			}
			
		});
		
		hat1button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				if(Save.gd.hat1own){
					Save.gd.hat=1;
					hat1button.setColor(Color.GREEN);
					hat0button.setColor(Color.WHITE);
					hat2button.setColor(Color.WHITE);
				}
				else 
					showbuy();
			}

			private void showbuy() {
				final Group comfirm=new Group();
				Image bgbuy1 = new Image(Assets.manager.get(Assets.uiback, Texture.class));
				bgbuy1.setSize(stage.getWidth()*2/3, stage.getHeight()*2/3);bgbuy1.setPosition(stage.getWidth()*1/6, stage.getHeight()*1/6);
				Image bgbuybg = new Image(skin.getDrawable("white"));
				bgbuybg.getColor().a=0;
				bgbuybg.setSize(stage.getWidth(), stage.getHeight());
				bgbuybg.setPosition(0, 0);
				final Label spendgoldlabel=new Label(MoleGame.myBundle.get("buy1")+Integer.toString(HAT1PRICE)+
						MoleGame.myBundle.get("silvquest"), skin, "default");
				spendgoldlabel.setPosition(stage.getWidth()/2,stage.getHeight()/2+4*pad,Align.center);
				
				final Image yesb=new Image(Assets.manager.get(Assets.gover2, Texture.class));
				yesb.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
				yesb.setPosition(stage.getWidth()/2-yesb.getWidth()-4*pad, stage.getHeight()/2-4*pad);
				
				final Image nob=new Image(Assets.manager.get(Assets.gover, Texture.class));
				nob.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
				nob.setPosition(stage.getWidth()/2+4*pad, stage.getHeight()/2-4*pad);
				
				bgbuybg.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(Save.gd.sound)click.play();
						comfirm.clear();
						comfirm.remove();
						
					}
				});
				
				yesb.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(Save.gd.sound)click.play();
						if(Save.gd.silvercoins>=HAT1PRICE){
							Save.gd.silvercoins-=HAT1PRICE;
							silvcoinsshop.setText(Integer.toString(Save.gd.silvercoins));
							silvcoinsshop.pack();
							silvcoinsshop.setPosition(
									stage.getWidth()-3.5f*pad-stage.getHeight()/16,
									stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
							itemslist.removeActor(hat1price);
							Save.gd.hat1own=true;
							comfirm.clear();
							comfirm.remove();
							Save.gd.hat=1;
							hat1button.setColor(Color.GREEN);
							hat0button.setColor(Color.WHITE);
							hat2button.setColor(Color.WHITE);
						}
						else{
							buymenu.addAction(sequence(moveTo(0,stage.getHeight()), alpha(1),show2(),moveTo(0,0, 0.5f, fade)));
							buymenu.toFront();
						}
					}
				});
				
				nob.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(Save.gd.sound)click.play();
						comfirm.clear();
						comfirm.remove();
						
					}
				});
				
				comfirm.addActor(bgbuybg);
				comfirm.addActor(bgbuy1);
				comfirm.addActor(spendgoldlabel);
				comfirm.addActor(yesb);
				comfirm.addActor(nob);
				stage.addActor(comfirm);
			}
			
		});
		
		hat2button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				if(Save.gd.hat2own){
					Save.gd.hat=2;
					hat2button.setColor(Color.GREEN);
					hat0button.setColor(Color.WHITE);
					hat1button.setColor(Color.WHITE);
				}
				else 
					showbuy();
			}

			private void showbuy() {
				final Group comfirm=new Group();
				Image bgbuy1 = new Image(Assets.manager.get(Assets.uiback, Texture.class));
				bgbuy1.setSize(stage.getWidth()*2/3, stage.getHeight()*2/3);bgbuy1.setPosition(stage.getWidth()*1/6, stage.getHeight()*1/6);
				Image bgbuybg = new Image(skin.getDrawable("white"));
				bgbuybg.getColor().a=0;
				bgbuybg.setSize(stage.getWidth(), stage.getHeight());
				bgbuybg.setPosition(0, 0);
				final Label spendgoldlabel=new Label(MoleGame.myBundle.get("buy1")+Integer.toString(HAT2PRICE)+
						MoleGame.myBundle.get("silvquest"), skin, "default");
				spendgoldlabel.setPosition(stage.getWidth()/2,stage.getHeight()/2+4*pad,Align.center);
				
				final Image yesb=new Image(Assets.manager.get(Assets.gover2, Texture.class));
				yesb.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
				yesb.setPosition(stage.getWidth()/2-yesb.getWidth()-4*pad, stage.getHeight()/2-4*pad);
				
				final Image nob=new Image(Assets.manager.get(Assets.gover, Texture.class));
				nob.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
				nob.setPosition(stage.getWidth()/2+4*pad, stage.getHeight()/2-4*pad);
				
				bgbuybg.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(Save.gd.sound)click.play();
						comfirm.clear();
						comfirm.remove();
						
					}
				});
				
				yesb.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(Save.gd.sound)click.play();
						if(Save.gd.silvercoins>=HAT2PRICE){
							Save.gd.silvercoins-=HAT2PRICE;
							silvcoinsshop.setText(Integer.toString(Save.gd.silvercoins));
							silvcoinsshop.pack();
							silvcoinsshop.setPosition(
									stage.getWidth()-3.5f*pad-stage.getHeight()/16,
									stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
							itemslist.removeActor(hat2price);
							Save.gd.hat2own=true;
							comfirm.clear();
							comfirm.remove();
							Save.gd.hat=1;
							hat2button.setColor(Color.GREEN);
							hat0button.setColor(Color.WHITE);
							hat1button.setColor(Color.WHITE);
						}
						else{
							buymenu.addAction(sequence(moveTo(0,stage.getHeight()), alpha(1),show2(),moveTo(0,0, 0.5f, fade)));
							buymenu.toFront();
						}
					}
				});
				
				nob.addListener(new ClickListener() {
					@Override
					public void clicked(InputEvent event, float x, float y) {
						if(Save.gd.sound)click.play();
						comfirm.clear();
						comfirm.remove();
						
					}
				});
				
				comfirm.addActor(bgbuybg);
				comfirm.addActor(bgbuy1);
				comfirm.addActor(spendgoldlabel);
				comfirm.addActor(yesb);
				comfirm.addActor(nob);
				stage.addActor(comfirm);
			}
			
		});
		
		
		itemslist.setSize(stage.getWidth()-(stage.getWidth()/4),molebutton.getY()+molebutton.getHeight());
		final ScrollPane skins=new ScrollPane(itemslist);
		skins.setBounds(stage.getWidth()/4, backbt.getY(),         //x,y
				stage.getWidth()-(stage.getWidth()/4),             //w
				shoptitle.getY()-pad-(backbt.getY()));             //h
		
		skins.getColor().a=0;
		skins.setVisible(false);
		shopgroup.addActor(skins);
		
		skinb.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				equib.clearActions();
				skinb.addAction(forever(sequence(rotateTo(-15,0.5f, sine),rotateTo(15,0.5f, sine))));
				skins.addAction(sequence(show2(),alpha(1,0.1f, fade)));
				equipgroup.addAction(sequence(alpha(0,0.1f, fade), hide2()));
				goldcoins.addAction(sequence(alpha(0,0.1f, fade), hide2()));
				goldcoinIm.addAction(sequence(alpha(0,0.1f, fade), hide2()));
				silvcoinsshop.addAction(sequence(show2(),alpha(1,0.1f, fade)));
				silvcoinIm.addAction(sequence(show2(),alpha(1,0.1f, fade)));
			}
		});
		equib.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				skinb.clearActions();
				equib.addAction(forever(sequence(rotateTo(-15,0.5f, sine),rotateTo(15,0.5f, sine))));
				skins.addAction(sequence(alpha(0,0.1f, fade), hide2()));
				equipgroup.addAction(sequence(show2(),alpha(1,0.1f, fade)));
				goldcoins.addAction(sequence(show2(),alpha(1,0.1f, fade)));
				goldcoinIm.addAction(sequence(show2(),alpha(1,0.1f, fade)));
				silvcoinsshop.addAction(sequence(alpha(0,0.1f, fade), hide2()));
				silvcoinIm.addAction(sequence(alpha(0,0.1f, fade), hide2()));
			}
		});
		
		backbt.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				shopgroup.addAction(sequence(moveTo(0,stage.getHeight(),0.5f, fade), hide2()));
			}
		});
		
		
		shopgroup.getColor().a=0;
		shopgroup.setVisible(false);
		
	}

	private Group price(String price, boolean gold) {
		Group grp=new Group();
		Image coinIm=new Image(Assets.manager.get(Assets.orbgold, Texture.class));
		if(gold){
			coinIm=new Image(Assets.manager.get(Assets.orbgold, Texture.class));
			coinIm.setSize(stage.getHeight()/16,stage.getHeight()/16);
		}
		else{
			coinIm=new Image(Assets.manager.get(Assets.orbsilver, Texture.class));
			coinIm.setSize(stage.getHeight()/16,stage.getHeight()/16);
		}
		
		Label pricelabel=new Label( price , skin, "default");
		pricelabel.setPosition(0,pricelabel.getHeight()/2,Align.right);
		coinIm.setPosition(pad,0);
		
		grp.addActor(coinIm);
		grp.addActor(pricelabel);
		grp.setSize(stage.getHeight()/15f, stage.getHeight()/15f);
		return grp;
	}
	
	private void createsetb() {
		
		final Table achievtable =new Table();
		final Image achievbg = new Image(Assets.manager.get(Assets.setting, Texture.class));
		
		Image achievB = new Image(Assets.manager.get(Assets.achiev, Texture.class));
		achievB.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				if(MoleGame.gservices!=null)MoleGame.gservices.showAchieve();
				achievtable.addAction(sequence(alpha(0,0.3f),hide2()));
				table.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(1,0.3f)));
				achievbg.setTouchable(Touchable.disabled);
				}});
		
		Image hsB = new Image(Assets.manager.get(Assets.challenge, Texture.class));
		hsB.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				if(MoleGame.gservices!=null)MoleGame.gservices.showScores();
				achievtable.addAction(sequence(alpha(0,0.3f),hide2()));
				table.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(1,0.3f)));
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
				if(Save.gd.sound)click.play();
				achievtable.addAction(sequence(alpha(0,0.3f),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
				table.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(1,0.3f)));
				achievbg.setTouchable(Touchable.disabled);
			}
		
		});
		
		final Image buttonachiev= new Image(Assets.manager.get(Assets.achiev, Texture.class));
		buttonachiev.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
		buttonachiev.setPosition(stage.getHeight()/36f, stage.getHeight()/36f);
		buttonachiev.getColor().a=0;
		buttonachiev.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Save.load();
				if(Save.gd.sound)click.play();
				table.addAction(sequence(alpha(0,0.3f),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
				achievtable.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(1,0.3f)));
				achievbg.setTouchable(Touchable.enabled);
				}});
		
		
		
		final Image buttonmusic;
		if(!Save.gd.music)
			buttonmusic= new Image(Assets.manager.get(Assets.musicoff, Texture.class));
		else
			buttonmusic= new Image(Assets.manager.get(Assets.musicon, Texture.class));
		buttonmusic.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
		buttonmusic.setPosition(stage.getHeight()/36f, stage.getHeight()/36f);
		buttonmusic.getColor().a=0;
		buttonmusic.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Save.load();
				if(Save.gd.sound)click.play();
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
		
		
		if(Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)){
			if(!Save.gd.accelerometer)
				buttonaccel= new Image(Assets.manager.get(Assets.acceloff, Texture.class));
			else
				buttonaccel= new Image(Assets.manager.get(Assets.accelon, Texture.class));
			buttonaccel.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
			buttonaccel.setPosition(stage.getHeight()/36f, stage.getHeight()/36f);
			buttonaccel.getColor().a=0;
			buttonaccel.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if(Save.gd.sound)click.play();
					if(!Save.gd.accelerometer) {
						Save.gd.accelerometer=true;
						buttonaccel.setDrawable(new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.accelon, Texture.class))));
						Save.save();
					}
					else  {
						buttonaccel.setDrawable(new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.acceloff, Texture.class))));
						Save.gd.accelerometer=false; Save.save();
					}
			}});
		}
		
		
		final Image buttonfx;
		if(!Save.gd.sound)
			buttonfx= new Image(Assets.manager.get(Assets.soundoff, Texture.class));
		else
			buttonfx= new Image(Assets.manager.get(Assets.soundon, Texture.class));
		buttonfx.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
		buttonfx.setPosition(stage.getHeight()/36f, stage.getHeight()/36f);
		buttonfx.getColor().a=0;
		buttonfx.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(!Save.gd.sound) {
					click.play();
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
				if(Save.gd.sound)click.play();
				Save.gd.lang=1;
				FileHandle baseFileHandle = Gdx.files.internal("internation/MyBundle");
				Locale locale = new Locale("fr");
				MoleGame.myBundle = I18NBundle.createBundle(baseFileHandle, locale);
				langtable.addAction(sequence(alpha(0,0.3f),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
				table.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(1,0.3f)));
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
				if(Save.gd.sound)click.play();
				Save.gd.lang=0;
				FileHandle baseFileHandle = Gdx.files.internal("internation/MyBundle");
				Locale locale = new Locale("en");
				MoleGame.myBundle = I18NBundle.createBundle(baseFileHandle, locale);
				langtable.addAction(sequence(alpha(0,0.3f),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
				table.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(1,0.3f)));
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
				if(Save.gd.sound)click.play();
				langtable.addAction(sequence(alpha(0,0.3f),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
				table.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(1,0.3f)));
				langbg.setTouchable(Touchable.disabled);
			}
		
		});
		
		final Image buttonlang= new Image(Assets.manager.get(Assets.lang, Texture.class));
		buttonlang.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
		buttonlang.setPosition(stage.getHeight()/36f, stage.getHeight()/36f);
		buttonlang.getColor().a=0;
		buttonlang.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				Save.load();
				table.addAction(sequence(alpha(0,0.3f),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
				langtable.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),alpha(1,0.3f)));
				langbg.setTouchable(Touchable.enabled);
				}});
		
		buttonfx    .setTouchable(Touchable.disabled);
		if(buttonaccel!=null)buttonaccel .setTouchable(Touchable.disabled);
		buttonmusic .setTouchable(Touchable.disabled);
		buttonlang  .setTouchable(Touchable.disabled);
		buttonachiev.setTouchable(Touchable.disabled);
		
		
		buttonset = new Image(Assets.manager.get(Assets.setting, Texture.class));
		buttonset.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
		buttonset.setPosition(stage.getHeight()/36f, stage.getHeight()/36f);
		buttonset.setOrigin(stage.getHeight()/8.4f/2, stage.getHeight()/8.4f/2);
		
		final Image setbg = new Image(Assets.manager.get(Assets.setting, Texture.class));
		setbg.setPosition(0, 0);
		setbg.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		setbg.setTouchable(Touchable.disabled);
		setbg.getColor().a=0;
		setbg.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				buttonset.addAction(rotateTo(0,0.5f));
				buttonfx.addAction(parallel(alpha(0,0.5f), moveTo(stage.getHeight()/36f,stage.getHeight()/36f,0.5f)));
				buttonmusic.addAction(parallel(alpha(0,0.5f), moveTo(stage.getHeight()/36f,stage.getHeight()/36f,0.5f)));
				if(buttonaccel!=null)buttonaccel.addAction(parallel(alpha(0,0.5f), moveTo(stage.getHeight()/36f,stage.getHeight()/36f,0.5f)));
				buttonlang.addAction(parallel(alpha(0,0.5f), moveTo(stage.getHeight()/36f,stage.getHeight()/36f,0.5f)));
				buttonachiev.addAction(parallel(alpha(0,0.5f), moveTo(stage.getHeight()/36f,stage.getHeight()/36f,0.5f)));
				
				setbg       .setTouchable(Touchable.disabled);
				buttonfx    .setTouchable(Touchable.disabled);
				if(buttonaccel!=null)buttonaccel .setTouchable(Touchable.disabled);
				buttonmusic .setTouchable(Touchable.disabled);
				buttonlang  .setTouchable(Touchable.disabled);
				buttonachiev.setTouchable(Touchable.disabled);
			}
		
		});
		
		buttonset.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				if(buttonaccel!=null){
					buttonset.addAction(rotateTo(-180,0.5f));
					buttonaccel .addAction(parallel(alpha(1,0.5f), moveTo((1*stage.getWidth()/13f+stage.getHeight()/36f),stage.getHeight()/36f,0.5f)));
					buttonfx    .addAction(parallel(alpha(1,0.5f), moveTo((2*stage.getWidth()/13f+stage.getHeight()/36f),stage.getHeight()/36f,0.5f)));
					buttonmusic .addAction(parallel(alpha(1,0.5f), moveTo((3*stage.getWidth()/13f+stage.getHeight()/36f),stage.getHeight()/36f,0.5f)));
					buttonlang  .addAction(parallel(alpha(1,0.5f), moveTo((4*stage.getWidth()/13f+stage.getHeight()/36f),stage.getHeight()/36f,0.5f)));
					buttonachiev.addAction(parallel(alpha(1,0.5f), moveTo((5*stage.getWidth()/13f+stage.getHeight()/36f),stage.getHeight()/36f,0.5f)));
					setbg       .setTouchable(Touchable.enabled);
					buttonaccel .setTouchable(Touchable.enabled);
					buttonfx    .setTouchable(Touchable.enabled);
					buttonmusic .setTouchable(Touchable.enabled);
					buttonlang  .setTouchable(Touchable.enabled);
					buttonachiev.setTouchable(Touchable.enabled);
				}
				else{
					buttonset.addAction(rotateTo(-180,0.5f));
					buttonfx    .addAction(parallel(alpha(1,0.5f), moveTo((1*stage.getWidth()/13f+stage.getHeight()/36f),stage.getHeight()/36f,0.5f)));
					buttonmusic .addAction(parallel(alpha(1,0.5f), moveTo((2*stage.getWidth()/13f+stage.getHeight()/36f),stage.getHeight()/36f,0.5f)));
					buttonlang  .addAction(parallel(alpha(1,0.5f), moveTo((3*stage.getWidth()/13f+stage.getHeight()/36f),stage.getHeight()/36f,0.5f)));
					buttonachiev.addAction(parallel(alpha(1,0.5f), moveTo((4*stage.getWidth()/13f+stage.getHeight()/36f),stage.getHeight()/36f,0.5f)));
					setbg       .setTouchable(Touchable.enabled);
					buttonfx    .setTouchable(Touchable.enabled);
					buttonmusic .setTouchable(Touchable.enabled);
					buttonlang  .setTouchable(Touchable.enabled);
					buttonachiev.setTouchable(Touchable.enabled);
				}
			}
		
		});
		
		stage.addActor(buttonset);
		stage.addActor(setbg);
		if(buttonaccel!=null)stage.addActor(buttonaccel);
		stage.addActor(buttonfx);
		stage.addActor(buttonmusic);
		stage.addActor(buttonlang);
		stage.addActor(buttonachiev);
		stage.addActor(langbg);
		stage.addActor(langtable);
		stage.addActor(achievbg);
		stage.addActor(achievtable);
	}

	private void createlvlsel() {
		lvltable = new Table();
		
		Image buttonBack = new Image(Assets.manager.get(Assets.back, Texture.class));
		buttonBack.setSize(64, 64);
		buttonBack.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				table    .addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(), alpha(1,0.5f)));
				lvltable .addAction(sequence(alpha(0,0.5f),com.badlogic.gdx.scenes.scene2d.actions.Actions.hide()));
				buttonset.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(), alpha(1,0.5f)));
			}
			;});
		Image descbg = new Image(Assets.manager.get(Assets.uiback, Texture.class));
		descbg.setPosition(-stage.getWidth()*0.025f, -stage.getWidth()*0.025f);
		descbg.setSize(stage.getWidth()*1.05f, stage.getHeight()/4);
		
		//////========================================== decoy image start ====================================
		Image decoy1=new Image();
		decoy1.setVisible(false);
		decoy1.setPosition(-stage.getHeight()*0.14f, 0);
		
		//////======================================Good old time level button ================================
		
		// Create the label to show the level name
		Label title1 = new Label(MoleGame.myBundle.get("a1"), skin);
		title1.setFontScale(0.9f);
		title1.setAlignment(Align.center);
		
		// Create the label to show the level HS
		Label hscore1 = new Label(MoleGame.myBundle.get("best")+ "\n" + Integer.toString(Save.gd.scores[MoleGame.OLDARCADE]), skin);
		hscore1.setFontScale(0.75f);
		hscore1.setAlignment(Align.center);
		
		// Create the image to show the level image
		Image lvlimage1 = new Image(Assets.manager.get(Assets.play, Texture.class));
		lvlimage1.setAlign(Align.center);
		
		if(Save.gd.uAgom){
			title1 = new Label(MoleGame.myBundle.get("a1"), skin);
			lvlimage1 = new Image(Assets.manager.get(Assets.play, Texture.class));
			a1desc = new Label(MoleGame.myBundle.get("a1d"), skin);
			a1desc.setAlignment(Align.center);
			a1desc.setPosition(stage.getWidth()/2-a1desc.getWidth()/2,
					descbg.getY()+descbg.getHeight()-a1desc.getHeight()-stage.getHeight()/36);
			
		}
		else  {
			title1 = new Label(MoleGame.myBundle.get("locked"), skin);
			lvlimage1 = new Image(Assets.manager.get(Assets.myster, Texture.class));
			hscore1.setVisible(false);
			a1desc = new Label(MoleGame.myBundle.get("ld"), skin);
			a1desc.setAlignment(Align.center);
			a1desc.setPosition(stage.getWidth()/2-a1desc.getWidth()/2,
					descbg.getY()+descbg.getHeight()-a1desc.getHeight()-stage.getHeight()/36);
		}
		
		Group a1b= new Group();
		lvlimage1.setSize(stage.getHeight()*0.28f, stage.getHeight()*0.28f);
		hscore1.setPosition(lvlimage1.getWidth()/2-hscore1.getWidth()/2, 0);
		lvlimage1.setPosition(0, hscore1.getHeight()+stage.getHeight()/36);
		title1 .setPosition(lvlimage1.getWidth()/2-title1.getWidth()/2 , lvlimage1.getY()+lvlimage1.getHeight()+stage.getHeight()/36);
		a1b.addActor(lvlimage1);
		a1b.addActor(hscore1);
		a1b.addActor(title1);
		a1b.setSize(stage.getHeight()*0.26f, stage.getHeight()*0.26f);
		a1b.setPosition(stage.getWidth()/2-a1b.getWidth()/2, 0);
		
		a1b.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				if(Save.gd.uAgom){
					black.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),
							fadeIn(0.25f),run(new Runnable() {
								@Override
								public void run() {
									((Game) Gdx.app.getApplicationListener())
					.setScreen(new PlayScreen(MoleGame.OLDARCADE, MoleGame.ARCADELVL, 0, MoleGame.LIMITTIME, 105, 0, 0));
						}
					})));
				}
			}
		});
		
		//////====================================== Arcade1 level button =====================================
		
		// Create the label to show the level name
		Label title2 = new Label("New Challenge", skin);
		title2.setFontScale(0.9f);
		title2.setAlignment(Align.center);
		
		// Create the label to show the level HS
		Label hscore2 = new Label(MoleGame.myBundle.get("best")+ "\n" + Integer.toString(Save.gd.scores[MoleGame.ARCADE1]), skin);
		hscore2.setFontScale(0.75f);
		hscore2.setAlignment(Align.center);
		
		// Create the image to show the level image
		Image lvlimage2 = new Image(Assets.manager.get(Assets.play, Texture.class));
		lvlimage2.setAlign(Align.center);
		
		if(Save.gd.uA1){
			title2 = new Label(MoleGame.myBundle.get("a2"), skin);
			lvlimage2 = new Image(Assets.manager.get(Assets.play, Texture.class));
			a2desc = new Label(MoleGame.myBundle.get("a2d"), skin);
			a2desc.setAlignment(Align.center);
			a2desc.setPosition(stage.getWidth()/2-a2desc.getWidth()/2,
					descbg.getY()+descbg.getHeight()-a2desc.getHeight()-stage.getHeight()/36);
		}
		else  {
			title2 = new Label(MoleGame.myBundle.get("locked"), skin);
			lvlimage2 = new Image(Assets.manager.get(Assets.myster, Texture.class));
			hscore2.setVisible(false);
			a2desc = new Label(MoleGame.myBundle.get("ld"), skin);
			a2desc.setAlignment(Align.center);
			a2desc.setPosition(stage.getWidth()/2-a2desc.getWidth()/2,
					descbg.getY()+descbg.getHeight()-a2desc.getHeight()-stage.getHeight()/36);
		}
		
		Group a2b= new Group();
		lvlimage2.setSize(stage.getHeight()*0.28f, stage.getHeight()*0.28f);
		hscore2.setPosition(lvlimage2.getWidth()/2-hscore2.getWidth()/2, 0);
		lvlimage2.setPosition(0, hscore2.getHeight()+stage.getHeight()/36);
		title2 .setPosition(lvlimage2.getWidth()/2-title2.getWidth()/2 , lvlimage2.getY()+lvlimage2.getHeight()+stage.getHeight()/36);
		a2b.addActor(lvlimage2);
		a2b.addActor(hscore2);
		a2b.addActor(title2);
		a2b.setSize(stage.getHeight()*0.26f, stage.getHeight()*0.26f);
		a2b.setPosition(a1b.getX()+a1b.getWidth()+stage.getWidth()/5, 0);
		
		a2b.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				if(Save.gd.uA1){
					black.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),
							fadeIn(0.25f),run(new Runnable() {
								@Override
								public void run() {
									((Game) Gdx.app.getApplicationListener())
					.setScreen(new PlayScreen(MoleGame.ARCADE1, MoleGame.ARCADELVL, 0, MoleGame.LIMITTIME, 105, 0, 0));
						}
					})));
				}
			}
		});
		
		//////=====================================Arcade Plane level button
		// Create the label to show the level name
		Label title3 = new Label(MoleGame.myBundle.get("a3"), skin);
		title3.setFontScale(0.9f);
		title3.setAlignment(Align.center);
		
		// Create the label to show the level HS
		Label hscore3 = new Label(MoleGame.myBundle.get("best")+ "\n" + Integer.toString(Save.gd.scores[MoleGame.ARCADEPLANE]), skin);
		hscore3.setFontScale(0.75f);
		hscore3.setAlignment(Align.center);
		
		// Create the image to show the level image
		Image lvlimage3 = new Image(Assets.manager.get(Assets.play, Texture.class));
		lvlimage3.setAlign(Align.center);
		
		if(Save.gd.uAplane){
			title3 = new Label(MoleGame.myBundle.get("a3"), skin);
			lvlimage3 = new Image(Assets.manager.get(Assets.play, Texture.class));
			a3desc = new Label(MoleGame.myBundle.get("a3d"), skin);
			a3desc.setAlignment(Align.center);
			a3desc.setPosition(stage.getWidth()/2-a3desc.getWidth()/2,
					descbg.getY()+descbg.getHeight()-a3desc.getHeight()-stage.getHeight()/36);
		}
		else  {
			title3 = new Label(MoleGame.myBundle.get("locked"), skin);
			lvlimage3 = new Image(Assets.manager.get(Assets.myster, Texture.class));
			hscore3.setVisible(false);
			a3desc = new Label(MoleGame.myBundle.get("ld"), skin);
			a3desc.setAlignment(Align.center);
			a3desc.setPosition(stage.getWidth()/2-a3desc.getWidth()/2,
					descbg.getY()+descbg.getHeight()-a3desc.getHeight()-stage.getHeight()/36);
		}
		
		Group a3b= new Group();
		lvlimage3.setSize(stage.getHeight()*0.28f, stage.getHeight()*0.28f);
		hscore3.setPosition(lvlimage3.getWidth()/2-hscore3.getWidth()/2, 0);
		lvlimage3.setPosition(0, hscore3.getHeight()+stage.getHeight()/36);
		title3 .setPosition(lvlimage3.getWidth()/2-title3.getWidth()/2 , lvlimage3.getY()+lvlimage3.getHeight()+stage.getHeight()/36);
		a3b.addActor(lvlimage3);
		a3b.addActor(hscore3);
		a3b.addActor(title3);
		a3b.setSize(stage.getHeight()*0.26f, stage.getHeight()*0.26f);
		a3b.setPosition(a2b.getX()+a2b.getWidth()+stage.getWidth()/5, 0);
		
		a3b.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				if(Save.gd.uAplane){
					black.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),
							fadeIn(0.25f),run(new Runnable() {
								@Override
								public void run() {
									((Game) Gdx.app.getApplicationListener())
					.setScreen(new PlayScreen(MoleGame.ARCADEPLANE, MoleGame.ARCADELVL, 0, MoleGame.LIMITJUMP, 1, 0, 0));
						}
					})));
				}
			}
		});
		
		//////============================================ Arcade2 level button =========================================
		// Create the label to show the level name
		Label title4 = new Label("Monument Challenge", skin);
		title4.setFontScale(0.9f);
		title4.setAlignment(Align.center);
		
		// Create the label to show the level HS
		Label hscore4 = new Label(MoleGame.myBundle.get("best")+ "\n" + Integer.toString(Save.gd.scores[MoleGame.ARCADE2]), skin);
		hscore4.setFontScale(0.75f);
		hscore4.setAlignment(Align.center);
		
		// Create the image to show the level image
		Image lvlimage4 = new Image(Assets.manager.get(Assets.play, Texture.class));
		lvlimage4.setAlign(Align.center);
		
		if(Save.gd.uA2){
			title4 = new Label(MoleGame.myBundle.get("a4"), skin);
			lvlimage4 = new Image(Assets.manager.get(Assets.play, Texture.class));
			a4desc = new Label(MoleGame.myBundle.get("a4d"), skin);
			a4desc.setAlignment(Align.center);
			a4desc.setPosition(stage.getWidth()/2-a4desc.getWidth()/2,
					descbg.getY()+descbg.getHeight()-a4desc.getHeight()-stage.getHeight()/36);
		}
		else  {
			title4 = new Label(MoleGame.myBundle.get("locked"), skin);
			lvlimage4 = new Image(Assets.manager.get(Assets.myster, Texture.class));
			hscore4.setVisible(false);
			a4desc = new Label(MoleGame.myBundle.get("ld"), skin);
			a4desc.setAlignment(Align.center);
			a4desc.setPosition(stage.getWidth()/2-a4desc.getWidth()/2,
					descbg.getY()+descbg.getHeight()-a4desc.getHeight()-stage.getHeight()/36);
		}
		
		Group a4b= new Group();
		lvlimage4.setSize(stage.getHeight()*0.28f, stage.getHeight()*0.28f);
		hscore4.setPosition(lvlimage4.getWidth()/2-hscore4.getWidth()/2, 0);
		lvlimage4.setPosition(0, hscore4.getHeight()+stage.getHeight()/36);
		title4 .setPosition(lvlimage4.getWidth()/2-title4.getWidth()/2 , lvlimage4.getY()+lvlimage4.getHeight()+stage.getHeight()/36);
		a4b.addActor(lvlimage4);
		a4b.addActor(hscore4);
		a4b.addActor(title4);
		a4b.setSize(stage.getHeight()*0.26f, stage.getHeight()*0.26f);
		a4b.setPosition(a3b.getX()+a3b.getWidth()+stage.getWidth()/5, 0);
		
		a4b.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				if(Save.gd.uA2){
					black.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),
							fadeIn(0.25f),run(new Runnable() {
								@Override
								public void run() {
									((Game) Gdx.app.getApplicationListener())
					.setScreen(new PlayScreen(MoleGame.ARCADE2, MoleGame.ARCADELVL, 0, MoleGame.LIMITTIME, 105, 0, 0));
						}
					})));
				}
			}
		});
		
		////===================================== Arcade3 level button ===================================================
		// Create the label to show the level name
		Label title5 = new Label("Urban Challenge", skin);
		title5.setFontScale(0.9f);
		title5.setAlignment(Align.center);
		
		// Create the label to show the level HS
		Label hscore5 = new Label(MoleGame.myBundle.get("best")+ "\n" + Integer.toString(Save.gd.scores[MoleGame.ARCADE3]), skin);
		hscore5.setFontScale(0.75f);
		hscore5.setAlignment(Align.center);
		
		// Create the image to show the level image
		Image lvlimage5 = new Image(Assets.manager.get(Assets.play, Texture.class));
		lvlimage5.setAlign(Align.center);
		
		if(Save.gd.uA3){
			title5 = new Label(MoleGame.myBundle.get("a5"), skin);
			lvlimage5 = new Image(Assets.manager.get(Assets.play, Texture.class));
			a5desc = new Label(MoleGame.myBundle.get("a5d"), skin);
			a5desc.setAlignment(Align.center);
			a5desc.setPosition(stage.getWidth()/2-a5desc.getWidth()/2,
					descbg.getY()+descbg.getHeight()-a5desc.getHeight()-stage.getHeight()/36);
		}
		else  {
			title5 = new Label(MoleGame.myBundle.get("locked"), skin);
			lvlimage5 = new Image(Assets.manager.get(Assets.myster, Texture.class));
			hscore5.setVisible(false);
			a5desc = new Label(MoleGame.myBundle.get("ld"), skin);
			a5desc.setAlignment(Align.center);
			a5desc.setPosition(stage.getWidth()/2-a5desc.getWidth()/2,
					descbg.getY()+descbg.getHeight()-a5desc.getHeight()-stage.getHeight()/36);
		}
		
		Group a5b= new Group();
		lvlimage5.setSize(stage.getHeight()*0.28f, stage.getHeight()*0.28f);
		hscore5.setPosition(lvlimage5.getWidth()/2-hscore5.getWidth()/2, 0);
		lvlimage5.setPosition(0, hscore5.getHeight()+stage.getHeight()/36);
		title5 .setPosition(lvlimage5.getWidth()/2-title5.getWidth()/2 , lvlimage5.getY()+lvlimage5.getHeight()+stage.getHeight()/36);
		a5b.addActor(lvlimage5);
		a5b.addActor(hscore5);
		a5b.addActor(title5);
		a5b.setSize(stage.getHeight()*0.26f, stage.getHeight()*0.26f);
		a5b.setPosition(a4b.getX()+a4b.getWidth()+stage.getWidth()/5, 0);
		
		a5b.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				if(Save.gd.uA3){
					black.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),
							fadeIn(0.25f),run(new Runnable() {
								@Override
								public void run() {
									((Game) Gdx.app.getApplicationListener())
					.setScreen(new PlayScreen(MoleGame.ARCADE3, MoleGame.ARCADELVL, 0, MoleGame.LIMITTIME, 90, 0, 0));
						}
					})));
				}
			}
		});
		
		///============================= No one ever know 11111 level button ================================
		
		// Create the label to show the level name
		Label title6 = new Label("Good Ol' Time", skin);
		title6.setFontScale(0.9f);
		title6.setAlignment(Align.center);
		
		// Create the label to show the level HS
		Label hscore6 = new Label(MoleGame.myBundle.get("best")+ "\n" + Integer.toString(Save.gd.scores[MoleGame.OLDARCADE]), skin); //change here
		hscore6.setFontScale(0.75f);
		hscore6.setAlignment(Align.center);
		
		// Create the image to show the level image
		Image lvlimage6 = new Image(Assets.manager.get(Assets.play, Texture.class));
		lvlimage6.setAlign(Align.center);
		
		if(Save.gd.uAnotexistyet){                                                                 //change here
			title6 = new Label(MoleGame.myBundle.get("a6"), skin);                                   //change here
			lvlimage6 = new Image(Assets.manager.get(Assets.play, Texture.class));
			a6desc = new Label(MoleGame.myBundle.get("a6d"), skin);
			a6desc.setAlignment(Align.center);
			a6desc.setPosition(stage.getWidth()/2-a6desc.getWidth()/2,
					descbg.getY()+descbg.getHeight()-a6desc.getHeight()-stage.getHeight()/36);
		}
		else  {
			title6 = new Label(MoleGame.myBundle.get("locked"), skin);
			lvlimage6 = new Image(Assets.manager.get(Assets.myster, Texture.class));
			hscore6.setVisible(false);
			a6desc = new Label(MoleGame.myBundle.get("ld"), skin);
			a6desc.setAlignment(Align.center);
			a6desc.setPosition(stage.getWidth()/2-a6desc.getWidth()/2,
					descbg.getY()+descbg.getHeight()-a6desc.getHeight()-stage.getHeight()/36);
		}
		
		Group a6b= new Group();
		lvlimage6.setSize(stage.getHeight()*0.28f, stage.getHeight()*0.28f);
		hscore6.setPosition(lvlimage6.getWidth()/2-hscore6.getWidth()/2, 0);
		lvlimage6.setPosition(0, hscore6.getHeight()+stage.getHeight()/36);
		title6 .setPosition(lvlimage6.getWidth()/2-title6.getWidth()/2 , lvlimage6.getY()+lvlimage6.getHeight()+stage.getHeight()/36);
		a6b.addActor(lvlimage6);
		a6b.addActor(hscore6);
		a6b.addActor(title6);
		a6b.setSize(stage.getHeight()*0.26f, stage.getHeight()*0.26f);
		a6b.setPosition(a5b.getX()+a5b.getWidth()+stage.getWidth()/5, 0);
		
		a6b.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				if(Save.gd.uAnotexistyet){                                                             //change here
					black.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),
							fadeIn(0.25f),run(new Runnable() {
								@Override
								public void run() {
									((Game) Gdx.app.getApplicationListener())
					.setScreen(new PlayScreen(MoleGame.OLDARCADE, MoleGame.ARCADELVL, 0, MoleGame.LIMITTIME, 90, 0, 0)); //change here
						}
					})));
				}
			}
		});
					
		///============================= No one ever know 222222 level button ================================
		
		// Create the label to show the level name
		Label title7 = new Label("Good Ol' Time", skin);                                //change here
		title7.setFontScale(0.9f);
		title7.setAlignment(Align.center);
		
		// Create the label to show the level HS
		Label hscore7 = new Label(MoleGame.myBundle.get("best")+ "\n" + Integer.toString(Save.gd.scores[MoleGame.OLDARCADE]), skin); //change here
		hscore7.setFontScale(0.75f);
		hscore7.setAlignment(Align.center);
		
		// Create the image to show the level image
		Image lvlimage7 = new Image(Assets.manager.get(Assets.play, Texture.class));
		lvlimage7.setAlign(Align.center);
		
		if(Save.gd.uAnotexistyet){                                                               //change here
			title7 = new Label(MoleGame.myBundle.get("a7"), skin);                                            //change here
			lvlimage7 = new Image(Assets.manager.get(Assets.play, Texture.class)); 
			a7desc = new Label(MoleGame.myBundle.get("a8d"), skin);
			a7desc.setAlignment(Align.center);
			a7desc.setPosition(stage.getWidth()/2-a7desc.getWidth()/2,
					descbg.getY()+descbg.getHeight()-a7desc.getHeight()-stage.getHeight()/36);
		}
		else  {
			title7 = new Label(MoleGame.myBundle.get("locked"), skin);
			lvlimage7 = new Image(Assets.manager.get(Assets.myster, Texture.class));
			hscore7.setVisible(false);
			a7desc = new Label(MoleGame.myBundle.get("ld"), skin);
			a7desc.setAlignment(Align.center);
			a7desc.setPosition(stage.getWidth()/2-a7desc.getWidth()/2,
					descbg.getY()+descbg.getHeight()-a7desc.getHeight()-stage.getHeight()/36);
		}
		
		Group a7b= new Group();
		lvlimage7.setSize(stage.getHeight()*0.28f, stage.getHeight()*0.28f);
		hscore7.setPosition(lvlimage7.getWidth()/2-hscore7.getWidth()/2, 0);
		lvlimage7.setPosition(0, hscore7.getHeight()+stage.getHeight()/36);
		title7 .setPosition(lvlimage7.getWidth()/2-title7.getWidth()/2 , lvlimage7.getY()+lvlimage7.getHeight()+stage.getHeight()/36);
		a7b.addActor(lvlimage7);
		a7b.addActor(hscore7);
		a7b.addActor(title7);
		a7b.setSize(stage.getHeight()*0.26f, stage.getHeight()*0.26f);
		a7b.setPosition(a6b.getX()+a6b.getWidth()+stage.getWidth()/5, 0);
		
		a7b.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				if(Save.gd.uAnotexistyet){                                                              //change here
					black.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),
							fadeIn(0.25f),run(new Runnable() {
								@Override
								public void run() {
									((Game) Gdx.app.getApplicationListener())
					.setScreen(new PlayScreen(MoleGame.OLDARCADE, MoleGame.ARCADELVL, 0, MoleGame.LIMITTIME, 90, 0, 0));   //change here
						}
					})));
				}
			}
		});
		
		////decoy end
		Image decoy2=new Image();
		decoy2.setVisible(false);
		decoy2.setPosition(a7b.getX()+a7b.getWidth()+stage.getWidth()/2+stage.getHeight()*0.14f, 0);
		
		Group allbuttonsg= new Group();
		allbuttonsg.addActor(decoy1);
		allbuttonsg.addActor(a1b);
		allbuttonsg.addActor(a2b);
		allbuttonsg.addActor(a3b);
		allbuttonsg.addActor(a4b);
		allbuttonsg.addActor(a5b);
		allbuttonsg.addActor(a6b);
		allbuttonsg.addActor(a7b);
		allbuttonsg.addActor(decoy2);
		allbuttonsg.setSize(decoy2.getX()-decoy1.getX(), buttonBack.getY()-stage.getHeight()/4-2*stage.getHeight()/36);
		
		scroll = new ScrollPane(allbuttonsg);
		scroll.setScrollingDisabled(false, true);
		pagespacing=stage.getWidth()/5; pagewidth=stage.getHeight()*0.26f;
		pagenumber=7;
		
		
		//lvltable.debug();
		buttonBack.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
		buttonBack.setPosition(stage.getWidth()-buttonBack.getWidth()-stage.getHeight()/36,
				stage.getHeight()-buttonBack.getHeight()-stage.getHeight()/36);
		scroll.setSize(stage.getWidth(), buttonBack.getY()-stage.getHeight()/4-2*stage.getHeight()/36);
		scroll.setPosition(0, stage.getHeight()/4);
		lvltable.addActor(buttonBack);
		lvltable.addActor(scroll);
		lvltable.addActor(descbg);
		lvltable.addActor(a1desc);
		lvltable.addActor(a2desc);
		lvltable.addActor(a3desc);
		lvltable.addActor(a4desc);
		lvltable.addActor(a5desc);
		lvltable.addActor(a6desc);
		lvltable.addActor(a7desc);
		lvltable.getColor().a=0;
		lvltable.setVisible(false);
		stage.addActor(lvltable);
		
	}

	@Override
	public void render(float delta) {
		
		if(MoleGame.bought==true){
			coinsbuymenu.setText(Integer.toString(Save.gd.silvercoins));
			coinsbuymenu.pack();
			coinsbuymenu.setPosition(
					stage.getWidth()-3.5f*pad-stage.getHeight()/16,
					stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
			silvcoinsshop.setText(Integer.toString(Save.gd.silvercoins));
			silvcoinsshop.pack();
			silvcoinsshop.setPosition(
					stage.getWidth()-3.5f*pad-stage.getHeight()/16,
					stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
			MoleGame.bought=false;
		}
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		
		if(black.getColor().a<0.9f){
			//draw blue background
			batch.begin();
			batch.draw(backsky, mole.molebody.getPosition().x-30, 0,60,14);
			
			//draw background
			sky0bg   .render(batch); 
			upskybg  .render(batch);
			cloud1bg .render(batch);
			cloud2bg .render(batch);
			cloud3bg .render(batch);
			batch.draw(rainbow, mole.molebody.getPosition().x/1.5f-12, -1,38,18);
			bg7 .render(batch); 
			bg8 .render(batch); 
			bg6  .render(batch);
			bg5  .render(batch);
			bg4.render(batch); 
			bg2  .render(batch);
			bg3  .render(batch);
			bg1   .render(batch);
			ground0bg      .render(batch); 
			groundjointbg  .render(batch);
			ugroundjoint1bg.render(batch); 
			ground1bg      .render(batch); 	
			if(grass1bg!=null)grass1bg        .render(batch);
			if(grass2bg!=null)grass2bg        .render(batch);
			if(grass3bg!=null)grass3bg        .render(batch);
			
			mole.rendertrail(batch);
			mole.updateMole(world, mole, delta);
			mole.render(batch, delta);
					
			//explosion effect 
			mole.renderexplosion(batch);
			batch.end();
		}
		
		//UPDATE 
		world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);
		//Adjust Camera
		if(camera.zoom<1+((mole.vitesse-5)/100)) camera.zoom+=0.001;
		else if(camera.zoom>=1+((mole.vitesse-5)/100)) camera.zoom-=0.001;	
		camera.position.set(mole.molebody.getPosition().x, mole.molebody.getPosition().y, 0);
		camera.update();
		
		//////////////  UI   //////////////////////////
		stage.act(delta);stage.draw();
		
		if (wasPanDragFling && !scroll.isPanning() && !scroll.isDragging() && !scroll.isFlinging()) {
            wasPanDragFling = false;
            final float scrollX = scroll.getScrollX();
            final float maxX = scroll.getMaxX();
            if (scrollX >= maxX || scrollX <= 0) return;
            
            float pageX = 0;
            float pageWidth = 0;
            //page1x=stage.getWidth()/2-a1b.getWidth()/2
            //page2x=stage.getWidth()/2-a1b.getWidth()/2+1*(a1b.getWidth()+stage.getWidth()/5)
            //page3x=stage.getWidth()/2-a1b.getWidth()/2+2*(a1b.getWidth()+stage.getWidth()/5)
            //page4x=stage.getWidth()/2-a1b.getWidth()/2+3*(a1b.getWidth()+stage.getWidth()/5)
            if (pagenumber > 0) {
                for (int i=0;i<pagenumber;i++) {
                    pageX = stage.getWidth()/2f-pagewidth/2f+i*(pagespacing+pagewidth);
                    pageWidth = pagewidth;
                    if (scrollX < (pageX + pageWidth * 0.5)) {
                        break;
                    }
                }
                scroll.setScrollX(MathUtils.clamp(pageX-pagewidth/2f , 0, 1000000000));
            }
        } else {
            if (scroll.isPanning() || scroll.isDragging() || scroll.isFlinging()) {
                wasPanDragFling = true;
            }
        }
		if(scroll.getScrollX()<0)
			a1desc.getColor().a=MathUtils.clamp(scroll.getScrollX()*2/(pagespacing+pagewidth)-2*(0)/(pagespacing+pagewidth)+1, 0, 1);
		else 
			a1desc.getColor().a=MathUtils.clamp(-scroll.getScrollX()*2/(pagespacing+pagewidth)+2*(0)/(pagespacing+pagewidth)+1, 0, 1);
		
		if(scroll.getScrollX()<(pagespacing+pagewidth))
			a2desc.getColor().a=MathUtils.clamp(scroll.getScrollX()*2/(pagespacing+pagewidth)-2*((pagespacing+pagewidth))/(pagespacing+pagewidth)+1, 0, 1);
		else 
			a2desc.getColor().a=MathUtils.clamp(-scroll.getScrollX()*2/(pagespacing+pagewidth)+2*((pagespacing+pagewidth))/(pagespacing+pagewidth)+1, 0, 1);
		
		if(scroll.getScrollX()<2*(pagespacing+pagewidth))
			a3desc.getColor().a=MathUtils.clamp(scroll.getScrollX()*2/(pagespacing+pagewidth)-2*(2*(pagespacing+pagewidth))/(pagespacing+pagewidth)+1, 0, 1);
		else 
			a3desc.getColor().a=MathUtils.clamp(-scroll.getScrollX()*2/(pagespacing+pagewidth)+2*(2*(pagespacing+pagewidth))/(pagespacing+pagewidth)+1, 0, 1);
		
		if(scroll.getScrollX()<3*(pagespacing+pagewidth))
			a4desc.getColor().a=MathUtils.clamp(scroll.getScrollX()*2/(pagespacing+pagewidth)-2*(3*(pagespacing+pagewidth))/(pagespacing+pagewidth)+1, 0, 1);
		else 
			a4desc.getColor().a=MathUtils.clamp(-scroll.getScrollX()*2/(pagespacing+pagewidth)+2*(3*(pagespacing+pagewidth))/(pagespacing+pagewidth)+1, 0, 1);
		
		if(scroll.getScrollX()<4*(pagespacing+pagewidth))
			a5desc.getColor().a=MathUtils.clamp(scroll.getScrollX()*2/(pagespacing+pagewidth)-2*(4*(pagespacing+pagewidth))/(pagespacing+pagewidth)+1, 0, 1);
		else 
			a5desc.getColor().a=MathUtils.clamp(-scroll.getScrollX()*2/(pagespacing+pagewidth)+2*(4*(pagespacing+pagewidth))/(pagespacing+pagewidth)+1, 0, 1);
		
		if(scroll.getScrollX()<5*(pagespacing+pagewidth))
			a6desc.getColor().a=MathUtils.clamp(scroll.getScrollX()*2/(pagespacing+pagewidth)-2*(5*(pagespacing+pagewidth))/(pagespacing+pagewidth)+1, 0, 1);
		else 
			a6desc.getColor().a=MathUtils.clamp(-scroll.getScrollX()*2/(pagespacing+pagewidth)+2*(5*(pagespacing+pagewidth))/(pagespacing+pagewidth)+1, 0, 1);
		
		if(scroll.getScrollX()<6*(pagespacing+pagewidth))
			a7desc.getColor().a=MathUtils.clamp(scroll.getScrollX()*2/(pagespacing+pagewidth)-2*(6*(pagespacing+pagewidth))/(pagespacing+pagewidth)+1, 0, 1);
		else 
			a7desc.getColor().a=MathUtils.clamp(-scroll.getScrollX()*2/(pagespacing+pagewidth)+2*(6*(pagespacing+pagewidth))/(pagespacing+pagewidth)+1, 0, 1);
		
		interManager.update(delta);
	
		//adjust music volume
		if(mole.molebody.getPosition().y>25) {
			music1.setVolume(MathUtils.clamp(mole.vitesse/25, 0.2f, 0.8f)/(mole.molebody.getPosition().y-25));
		}
		else
			music1.setVolume(MathUtils.clamp(mole.vitesse/25, 0.2f, 0.8f));
		
		
		if(mole.molebody.getPosition().y>60) {
			musicspace.setVolume(0.005f*(mole.molebody.getPosition().y-60));
		}
	}
	
	@Override
	public void resize(int width, int height) {
		
	}
	
	@Override
	public void hide() {
		dispose();
		mole.dispose();
		mole.sdig.stop(); mole.sfire.stop(); mole.swind.stop(); mole.sstar.stop();
		music1.stop();musicspace.stop();
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		while(!Assets.manager.update()){
			;
		}
	}

	@Override
	public void dispose() {
		stage.dispose();
		world.dispose();
		batch.dispose();
		debugRenderer.dispose();
	}


}
	