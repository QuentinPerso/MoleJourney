package quentin.jeu.mole.ui;

import static com.badlogic.gdx.math.Interpolation.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import java.util.Locale;

import quentin.jeu.mole.MoleGame;
import quentin.jeu.mole.screens.MainMenu;
import quentin.jeu.mole.screens.PlayScreen;
import quentin.jeu.mole.screens.Tuto0;
import quentin.jeu.mole.screens.Tuto1;
import quentin.jeu.mole.utils.Assets;
import quentin.jeu.mole.utils.Save;
import quentin.jeu.mole.utils.TimeCalc;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;

public class MapUI extends InputAdapter {
	
	public  float sizeratiow=Gdx.graphics.getWidth()/960f;
	public  float sizeratioh=Gdx.graphics.getHeight()/540f;
	public  float aspectratio=(float)Gdx.graphics.getWidth()/(float)Gdx.graphics.getHeight();
	private float universSize;
	private  final int JPPRICE=11, PLANEPRICE=11, TIMEPRICE=11;
	private  final int SKIN1PRICE=25, HAT1PRICE=15, HAT2PRICE=20, MAXUJP=3,MAXUPLANE=3, MAXUTIME=3, MAXBSPEED=4,MAXBCONTROL=4;
	private int jpcost, planecost, timecost, speedcost, controlcost;
	private  float pad=Gdx.graphics.getHeight()/36f;
	public  Stage stage,starstage;
	private Skin skin;
	public  Music music;
	
	private Table mainMenu;
	private Group playMenu, lifemenu;
	private float clicktimer;
	
	public  Image menuButton, menuBg, titleButton, exmenubutton, musicButton, soundButton, buttonset;
	private Group shopButton, unlockshop, unlockmenu;
	private Label coinsbuymenu, coinsplaymenu, silvcoinsshop,coinslifemenu;
	public Image/* playerIm,*/ black;
	private Array<Image> pathImages;
	
	private ScrollPane map;
	private Group starsTable, buymenu, shopgroup;
	private boolean hasScrolled;
	@SuppressWarnings("unused")
	private float playerX,playerY;
	private Label lifenumber, time2life;
	public TimeCalc timecalc;
	
	public Sound click, no;

	
	public MapUI () {
		//shop & services
		MoleGame.shop.checkprice();
		if(MoleGame.gservices.isSignedIn())
			MoleGame.gservices.checkachiev();
		//sound
		click = Assets.manager.get(Assets.click, Sound.class);
		no    = Assets.manager.get(Assets.no, Sound.class);
		
		//UI
		stage = new Stage();
		starstage = new Stage();
		
		createlvls();
		loadSkin();
		checkunlock();
		create();
		layout();
		events();
	
		Save.save();
		black=new Image(skin.newDrawable("white", Color.BLACK));
		black.setSize(stage.getWidth(), stage.getHeight());
		stage.addActor(black);
		black.addAction(sequence(fadeOut(0.25f), hide()));
		
	}


	private void createlvls() {
		Save.gd.lvlscar.clear();
		addlevel(MoleGame.GLLVL      , 4    , MoleGame.LIMITTIME,45,  2000, 15000);
		addlevel(MoleGame.SCORELVL   , 3500 , MoleGame.LIMITTIME,45,  5000, 10000);
		addlevel(MoleGame.GLLVL      , 3    , MoleGame.LIMITJUMP,10,  5000, 10000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME,45,  5000, 10000);
		addlevel(MoleGame.COINLVL    , 10   , MoleGame.LIMITTIME,45,  5000, 15000);
		addlevel(MoleGame.SCORELVL   , 3500 , MoleGame.LIMITJUMP,10,  5000, 15000); ///5 speed 1
		addlevel(MoleGame.DISTLVL    , 350  , MoleGame.LIMITTIME,45,  7500, 15000);
		addlevel(MoleGame.GLLVL      , 5    , MoleGame.LIMITTIME,45, 10000, 20000);
		addlevel(MoleGame.SCORELVL   , 10000, MoleGame.LIMITTIME,60, 15000, 30000);
		addlevel(MoleGame.HEIGHTLVL  , 10   , MoleGame.LIMITJUMP,10, 10000, 20000); //9 control 1 
		addlevel(MoleGame.DISTLVL    , 500  , MoleGame.LIMITTIME,55, 20000, 30000); //10 unlock bonus time 
		addlevel(MoleGame.CONSEQGLLVL, 7    , MoleGame.LIMITTIME,45, 20000, 30000); //11 ground not soft 
		addlevel(MoleGame.SCORELVL   , 18000, MoleGame.LIMITTIME,45, 25000, 35000);
		addlevel(MoleGame.COMBOLVL   , 1    , MoleGame.LIMITTIME,60, 25000, 50000);
		addlevel(MoleGame.COINLVL    , 30   , MoleGame.LIMITJUMP,15, 20000, 40000);
		addlevel(MoleGame.GLLVL      , 6    , MoleGame.LIMITTIME,30, 15000, 35000); //15  30 silver coins offered (2 stars)
		addlevel(MoleGame.DISTLVL    , 600  , MoleGame.LIMITJUMP,20, 30000, 60000);
		addlevel(MoleGame.SCORELVL   , 50000, MoleGame.LIMITTIME,100, 70000, 90000);
		addlevel(MoleGame.GLLVL      , 10   , MoleGame.LIMITJUMP,11, 25000, 35000);
		addlevel(MoleGame.HEIGHTLVL  , 30   , MoleGame.LIMITTIME,60, 50000, 85000);
		addlevel(MoleGame.CONSEQGLLVL, 12   , MoleGame.LIMITTIME,45, 20000, 40000);//20 unlock arcade1 
		addlevel(MoleGame.BIGAIRLVL  , 6500 , MoleGame.LIMITTIME,60, 60000, 70000);//21 GOM(3 stars)
		///next red rock
		addlevel(MoleGame.DISTLVL    , 600  , MoleGame.LIMITTIME,55, 20000, 30000); //22 ground harder
		addlevel(MoleGame.HEIGHTLVL  , 30   , MoleGame.LIMITTIME,50, 50000, 90000); 
		addlevel(MoleGame.GLLVL      , 15   , MoleGame.LIMITTIME,30, 2000, 3500);
		addlevel(MoleGame.SCORELVL   , 70000, MoleGame.LIMITTIME,105,90000, 100000); //25 bonus plane
		addlevel(MoleGame.DIGLVL     , 300  , MoleGame.LIMITTIME,55, 15000, 30000); 
		addlevel(MoleGame.COINLVL    , 50   , MoleGame.LIMITTIME,75, 50000, 90000);
		addlevel(MoleGame.SCORELVL   ,100000, MoleGame.LIMITJUMP,25 ,110000, 120000);
		addlevel(MoleGame.COMBOLVL   , 3    , MoleGame.LIMITTIME,100, 100000, 120000);
		addlevel(MoleGame.DIGLVL     , 600  , MoleGame.LIMITTIME,120, 100000, 120000); //30  100 gold coins offered  (3 stars)
		addlevel(MoleGame.DISTLVL    , 500  , MoleGame.LIMITTIME,45, 20000, 30000); 
		addlevel(MoleGame.HEIGHTLVL  , 70   , MoleGame.LIMITTIME,90, 100000, 120000); //32 unlock jet pack
		addlevel(MoleGame.BIGAIRLVL  , 15000, MoleGame.LIMITTIME,120, 100000, 120000);
		addlevel(MoleGame.COINLVL    , 50   , MoleGame.LIMITJUMP,22 , 50000, 90000); 
		addlevel(MoleGame.SCORELVL   , 60000, MoleGame.LIMITJUMP,12, 70000, 100000); //35 control 2 (2 stars)
		addlevel(MoleGame.BIGAIRLVL  , 20000, MoleGame.LIMITTIME,100, 100000, 120000);
		addlevel(MoleGame.DISTLVL    , 1000 , MoleGame.LIMITTIME,60 , 15000, 30000); 
		addlevel(MoleGame.COMBOLVL   , 1    , MoleGame.LIMITTIME,45 , 15000, 30000); //38 unlock speed 2 ( 2 stars)
		addlevel(MoleGame.COINLVL    , 72  , MoleGame.LIMITTIME,120, 50000, 90000);  
		addlevel(MoleGame.HEIGHTLVL  , 130  , MoleGame.LIMITTIME,120, 100000, 140000);//40 unlock arcade 2
		addlevel(MoleGame.SCORELVL   ,200000, MoleGame.LIMITJUMP,32 ,80000, 100000); //41 plane level (3 stars)
		///city
		addlevel(MoleGame.DIGLVL     , 600  , MoleGame.LIMITTIME,100, 100000, 120000); 
		addlevel(MoleGame.HEIGHTLVL  , 130  , MoleGame.LIMITJUMP,22 , 15000, 30000); 
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);//45 speed 3
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);//50 unlock jet pack
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		addlevel(MoleGame.CONSEQGLLVL, 2    , MoleGame.LIMITTIME  ,5 , 15000, 30000);
		
	}

	private void addlevel(int type, int objectif, int limittype, int limit, int score1, int score2) {
		Save.gd.lvlscar.add(type);
		Save.gd.lvlscar.add(objectif); 
		Save.gd.lvlscar.add(limittype);
		Save.gd.lvlscar.add(limit);
		Save.gd.lvlscar.add(score1);
		Save.gd.lvlscar.add(score2);
	}
		
	


	private void checkunlock() {
		//5 speed 1
		if(Save.gd.nextlvl==6 && Save.gd.uspeed==0){
			unlock(Assets.manager.get(Assets.gover2,Texture.class), MoleGame.myBundle.get("uspeed"));
			Save.gd.uspeed=1;
		}
		
		//7 unlock bonus time .... 
		if(Save.gd.nextlvl==8 && Save.gd.utime==0){
			Save.gd.newstuff=true;
			unlock(Assets.manager.get(Assets.time,Texture.class), MoleGame.myBundle.get("ubonus"));
			Save.gd.utime=1;
		}
		
		//10 control 1 
		if(Save.gd.nextlvl==11 && Save.gd.ucontrol==0){
			Save.gd.newstuff=true;
			unlock(Assets.manager.get(Assets.gover2,Texture.class), MoleGame.myBundle.get("ucontrol"));
			Save.gd.ucontrol=1;
		}
		//11 ground not soft 
		
		//12 rate please
		if(Save.gd.nextlvl==13 && Save.gd.urate==0){
			unlock(Assets.manager.get(Assets.gover2,Texture.class), MoleGame.myBundle.get("urate"));
			Save.gd.urate=1;
		}
		
		//15  15 silver coins offered (2 stars)
		int ok=0;
		for(int i=0;i<16;i++){
			if(Save.gd.scores[i]>Save.gd.lvlscar.get(i*6+4)) ok++;
			if(ok==16 && !Save.gd.reward1){
				unlock(Assets.manager.get(Assets.orbsilver,Texture.class), MoleGame.myBundle.get("ucoin1"));
				Save.gd.silvercoins+=20;
				Save.gd.reward1=true;
			}
		}
		
		//20 unlock arcade1 
		if(Save.gd.nextlvl==21 && !Save.gd.uA1){
			unlock(Assets.manager.get(Assets.play,Texture.class), MoleGame.myBundle.get("ulevel"));
			Save.gd.uA1=true;
		}
		
		//21 GOM(3 stars)
		ok=0;
		for(int i=0;i<21;i++){
			if(Save.gd.scores[i]>Save.gd.lvlscar.get(i*6+5))ok++;
			if(ok==21 && !Save.gd.uAgom){
				unlock(Assets.manager.get(Assets.play,Texture.class), MoleGame.myBundle.get("ulevel"));
				Save.gd.uAgom=true;
			}
		}
		//////ROCK 
		//22 ground harder
		//25 bonus plane
		if(Save.gd.nextlvl==26 && Save.gd.uplane==0){
			Save.gd.newstuff=true;
			unlock(Assets.manager.get(Assets.plane,Texture.class), MoleGame.myBundle.get("ubonus"));
			Save.gd.uplane=1;
		}
		
		//30  100 gold coins offered  (3 stars)
		ok=0;
		for(int i=22;i<31;i++){
			if(Save.gd.scores[i]>Save.gd.lvlscar.get(i*6+5))ok++;
			if(ok==8 && !Save.gd.reward2){
				unlock(Assets.manager.get(Assets.orbgold,Texture.class), MoleGame.myBundle.get("ucoin2"));
				Save.gd.goldcoins+=100;
				Save.gd.reward2=true;
			}
		}
		
		//32 unlock jet pack
		if(Save.gd.nextlvl==33 && Save.gd.ujp==0){
			Save.gd.newstuff=true;
			unlock(Assets.manager.get(Assets.jetpack,Texture.class), MoleGame.myBundle.get("ubonus"));
			Save.gd.ujp=1;
		}
		
		//33 rate please
		if(Save.gd.nextlvl==34 && Save.gd.urate==1){
			unlock(Assets.manager.get(Assets.gover2,Texture.class), MoleGame.myBundle.get("urate"));
			Save.gd.urate=2;
		}
		
		//35 control 2 (2 stars)
		ok=0;
		for(int i=22;i<36;i++){
			if(Save.gd.scores[i]>Save.gd.lvlscar.get(i*6+5))ok++;
			if(ok==12 && Save.gd.ucontrol==1){
				Save.gd.newstuff=true;
				unlock(Assets.manager.get(Assets.gover2,Texture.class), MoleGame.myBundle.get("ucontrol"));
				Save.gd.ucontrol=2;
			}
		}
		
		//38 unlock speed 2 (2 stars)
		for(int i=22;i<39;i++){
			if(Save.gd.scores[i]>Save.gd.lvlscar.get(i*6+4))ok++;
			if(ok==17 && Save.gd.uspeed==1){
				unlock(Assets.manager.get(Assets.gover2,Texture.class), MoleGame.myBundle.get("uspeed"));
				Save.gd.uspeed=2;
				Save.gd.newstuff=true;
			}
		}
		
		//40 unlock arcade 2
		if(Save.gd.nextlvl==41 && !Save.gd.uA2){
			unlock(Assets.manager.get(Assets.play,Texture.class), MoleGame.myBundle.get("ulevel"));
			Save.gd.uA2=true;
		}
		//41 plane level (3 stars)
		ok=0;
		for(int i=0;i<42;i++){
			if(Save.gd.scores[i]>Save.gd.lvlscar.get(i*6+5))ok++;
			if(ok==43 && !Save.gd.uAplane){
				unlock(Assets.manager.get(Assets.play,Texture.class), MoleGame.myBundle.get("ulevel"));
				Save.gd.uAplane=true;
			}
		}
		
		
		//////////city/////////
		//50 rate please
		if(Save.gd.nextlvl==51 && Save.gd.urate==1){
			unlock(Assets.manager.get(Assets.gover2,Texture.class), MoleGame.myBundle.get("urate"));
			Save.gd.urate=2;
		}
	}

	private void create () {
		
		//================================================STAR MAP STAGE========================================================//
		//playerIm    = new Image(Assets.manager.get(Assets.moleatlas, TextureAtlas.class).findRegion("mole",0));
	//	playerIm.setSize(32,32);
	//	playerIm.setOrigin(16, 16);
		//playerIm.setPosition(prevplayerX-16+prevplayerstarsize/2, prevplayerY-16+prevplayerstarsize/2);
	//	playerIm.setTouchable(Touchable.disabled);
		
		map=new ScrollPane(createMap(Save.gd.starX.length-1));
		
		map.setFillParent(true);
		map.getColor().a=0;
		map.addAction(fadeIn(1,pow5In));
		coinsplaymenu=new Label("", skin);
		coinsbuymenu=new Label("", skin); 
		silvcoinsshop=new Label("", skin);
		coinslifemenu=new Label("", skin);
		//=========================================MAIN STAGE============================================//
		timecalc=new TimeCalc();
		
		//Ressources
		time2life = new Label("88:88" , skin);
		lifenumber  = new Label("0" , skin);
		lifenumber.setText(Integer.toString(Save.gd.lives));
		lifenumber.addAction(forever(sequence(alpha(1f,1,sine),alpha(0.5f,1, sine))));
		
		//Garage
		//gar =new Garage1(skin, stage.getWidth(), stage.getHeight(), scale);
		//lab=(gar.lab);
		
		
		//Menu
		menuButton = new Image(Assets.manager.get(Assets.menu,Texture.class));
		menuButton = new Image(Assets.manager.get(Assets.menu,Texture.class));
		menuBg = new Image(skin.newDrawable("white", Color.BLACK));
		menuBg.getColor().a=0;
		menuBg.setTouchable(Touchable.disabled);
		titleButton = new Image(Assets.manager.get(Assets.menu,Texture.class));
		exmenubutton= new Image(Assets.manager.get(Assets.back,Texture.class));
		shopButton  = new Group();
		Image shopButtonIm  = new Image(Assets.manager.get(Assets.shop,Texture.class));
		shopButtonIm.setSize(stage.getHeight()/6.35f, stage.getHeight()/6.35f);
		unlockshop = new Group();
		Label unlockstxt= new Label("New", skin, "default");
		unlockstxt.setFontScale(1f);
		unlockstxt.setColor(Color.RED);
		unlockshop.setSize(unlockstxt.getWidth(), unlockstxt.getHeight());
		unlockshop.addActor(unlockstxt);
		unlockshop.rotateBy(45);
		unlockshop.setPosition(0, stage.getHeight()/6.35f-unlockshop.getHeight()*1.35f);
		unlockshop.setTouchable(Touchable.disabled);
		shopButton.addActor(shopButtonIm);
		shopButton.addActor(unlockshop);
		unlockshop.addAction(forever(sequence(alpha(0,0.5f),alpha(1,0.5f))));
		if(!Save.gd.newstuff)
			unlockshop.setVisible(false);
		
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
	
	private void layout () {
		
		//Create menu
		Image menu =new Image(Assets.manager.get(Assets.menu, Texture.class));
		mainMenu = new Table(skin);
		mainMenu.setFillParent(true);
		mainMenu.add(menu).size(stage.getHeight()*0.28f).colspan(3).row();
		mainMenu.defaults().uniform().fill().center().padTop(stage.getHeight()/36f);
		mainMenu.add(exmenubutton).size(stage.getHeight()/6.35f).pad(stage.getHeight()/36f);
		mainMenu.add(shopButton)  .size(stage.getHeight()/6.35f).pad(stage.getHeight()/36f);
		mainMenu.add(titleButton) .size(stage.getHeight()/6.35f).pad(stage.getHeight()/36f).row();
		
		menuBg.setSize(stage.getWidth(), stage.getHeight());
		mainMenu.setVisible(false);
		
		Group lifeim=new Group();
		Image lifeim1=  new Image(Assets.manager.get(Assets.life1,Texture.class));
		lifeim1.setSize(stage.getHeight()/8.4f,stage.getHeight()/8.4f);
		Image lifeim2=  new Image(Assets.manager.get(Assets.life2,Texture.class));
		lifeim2.setSize(stage.getHeight()/8.4f,stage.getHeight()/8.4f);
		lifeim.addActor(lifeim1);
		lifeim.addActor(lifeim2);
		lifeim.setSize(stage.getHeight()/8.4f,stage.getHeight()/8.4f);
		
		time2life.setPosition ((2*pad+0)+lifeim1.getWidth(),
				stage.getHeight()-pad-lifeim1.getHeight()/2-lifenumber.getHeight()/2);
		lifenumber.setPosition((pad+0)+lifeim1.getWidth()/2-lifenumber.getWidth()/2   ,
				stage.getHeight()-pad-lifeim1.getHeight()/2-lifenumber.getHeight()/2);
		lifeim.setPosition   ((pad+0),
				stage.getHeight()-pad-lifeim.getHeight());
		
		starstage.addActor(map);
		
		menuButton.setSize(stage.getHeight()/8.4f,stage.getHeight()/8.4f);
		menuButton.setPosition(stage.getWidth()-menuButton.getWidth()-pad, stage.getHeight()-menuButton.getHeight()-pad);
		unlockmenu = new Group();
		Label unlockstxt= new Label("New", skin, "default");
		unlockstxt.setFontScale(1f);
		unlockstxt.setColor(Color.RED);
		unlockmenu.setSize(unlockstxt.getWidth(), unlockstxt.getHeight());
		unlockmenu.addActor(unlockstxt);
		unlockmenu.rotateBy(45);
		unlockmenu.setPosition(menuButton.getX(), menuButton.getY()+ menuButton.getHeight()-unlockmenu.getHeight()*1.35f);
		unlockmenu.setTouchable(Touchable.disabled);
		unlockmenu.addAction(forever(sequence(alpha(0,0.5f),alpha(1,0.5f))));
		if(!Save.gd.newstuff)
			unlockmenu.setVisible(false);
		stage.addActor(menuButton);
		stage.addActor(unlockmenu);
		
		//stage.addActor(lab);
		
		stage.addActor(menuBg);
		stage.addActor(mainMenu);
		createsetb();
		stage.addActor(time2life);
		stage.addActor(lifeim);
		stage.addActor(lifenumber);
		
		createbank();
		createshop();
		
		stage.addActor(shopgroup);
		stage.addActor(buymenu);
	}
	
	private void createsetb() {
		
		final Table achievtable =new Table();
		final Image achievbg = new Image(Assets.manager.get(Assets.setting, Texture.class));
		
		Image achievB = new Image(Assets.manager.get(Assets.achiev, Texture.class));
		achievB.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//if(Save.gd.sound)click.play();
				MoleGame.gservices.showAchieve();
				achievtable.addAction(sequence(alpha(0,0.3f),hide()));
				mainMenu.addAction(sequence(show(),alpha(1,0.3f)));
				achievbg.setTouchable(Touchable.disabled);
				}});
		
		Image hsB = new Image(Assets.manager.get(Assets.challenge, Texture.class));
		hsB.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//if(Save.gd.sound)click.play();
				MoleGame.gservices.showScores();
				achievtable.addAction(sequence(alpha(0,0.3f),hide()));
				mainMenu.addAction(sequence(show(),alpha(1,0.3f)));
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
				achievtable.addAction(sequence(alpha(0,0.3f),hide()));
				mainMenu.addAction(sequence(show(),alpha(1,0.3f)));
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
				mainMenu.addAction(sequence(alpha(0,0.3f),hide()));
				achievtable.addAction(sequence(show(),alpha(1,0.3f)));
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
					}
				else  {
					Save.gd.music=false; Save.save();
					buttonmusic.setDrawable(new TextureRegionDrawable(new TextureRegion(Assets.manager.get(Assets.musicoff, Texture.class))));
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
				langtable.addAction(sequence(alpha(0,0.3f),hide()));
				mainMenu.addAction(sequence(show(),alpha(1,0.3f)));
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
				langtable.addAction(sequence(alpha(0,0.3f),hide()));
				mainMenu.addAction(sequence(show(),alpha(1,0.3f)));
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
				langtable.addAction(sequence(alpha(0,0.3f),hide()));
				mainMenu.addAction(sequence(show(),alpha(1,0.3f)));
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
				mainMenu.addAction(sequence(alpha(0,0.3f),hide()));
				langtable.addAction(sequence(show(),alpha(1,0.3f)));
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
		jpnumber.setPosition(jpbutton.getX()+jpbutton.getWidth()/2-jpnumber.getWidth()/2, jpbutton.getY()+jpbutton.getWidth()/2-jpnumber.getHeight()/2);
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
							speednumber.setText(MoleGame.myBundle.get("speed") +"\n"+MoleGame.myBundle.get("lvl")
									+Integer.toString(Save.gd.bspeed+Save.gd.uspeed+1));
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
						MoleGame.myBundle.get("lvl")+Integer.toString(Save.gd.bcontrol+Save.gd.ucontrol+1));
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
				bgbuy1.setSize(stage.getWidth()*2/3, stage.getHeight()*2/3);
				bgbuy1.setPosition(stage.getWidth()*1/6, stage.getHeight()*1/6);
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
							buymenu.addAction(sequence(moveTo(0,stage.getHeight()), alpha(1),show(),moveTo(0,0, 0.5f, fade)));
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
				bgbuy1.setSize(stage.getWidth()*2/3, stage.getHeight()*2/3);
				bgbuy1.setPosition(stage.getWidth()*1/6, stage.getHeight()*1/6);
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
							buymenu.addAction(sequence(moveTo(0,stage.getHeight()), alpha(1),show(),moveTo(0,0, 0.5f, fade)));
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
							buymenu.addAction(sequence(moveTo(0,stage.getHeight()), alpha(1),show(),moveTo(0,0, 0.5f, fade)));
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
				skins.addAction(sequence(show(),alpha(1,0.1f, fade)));
				equipgroup.addAction(sequence(alpha(0,0.1f, fade), hide()));
				goldcoins.addAction(sequence(alpha(0,0.1f, fade), hide()));
				goldcoinIm.addAction(sequence(alpha(0,0.1f, fade), hide()));
				silvcoinsshop.addAction(sequence(show(),alpha(1,0.1f, fade)));
				silvcoinIm.addAction(sequence(show(),alpha(1,0.1f, fade)));
			}
		});
		equib.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				skinb.clearActions();
				equib.addAction(forever(sequence(rotateTo(-15,0.5f, sine),rotateTo(15,0.5f, sine))));
				skins.addAction(sequence(alpha(0,0.1f, fade), hide()));
				equipgroup.addAction(sequence(show(),alpha(1,0.1f, fade)));
				goldcoins.addAction(sequence(show(),alpha(1,0.1f, fade)));
				goldcoinIm.addAction(sequence(show(),alpha(1,0.1f, fade)));
				silvcoinsshop.addAction(sequence(alpha(0,0.1f, fade), hide()));
				silvcoinIm.addAction(sequence(alpha(0,0.1f, fade), hide()));
			}
		});
		
		backbt.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				menuButton.setTouchable(Touchable.enabled);
				exmenubutton.setTouchable(Touchable.enabled);
				shopgroup.addAction(sequence(moveTo(0,stage.getHeight(),0.5f, fade), hide()));
			}
		});
		
		
		shopgroup.getColor().a=0;
		shopgroup.setVisible(false);
		
	}
	
	private void createplaymenu(final int lvl, final int type, final int objectif,
 final int limittype, final int limit, final int score1, final int score2){
		
		playMenu = new Group();
		Image playbg = new Image(Assets.manager.get(Assets.uiback, Texture.class));
		playbg.setSize(stage.getWidth(), stage.getHeight());
		playbg.setPosition(0, 0);
		playbg.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//anti transparency ????
			}
		});
		playMenu.addActor(playbg);
		
		Label lvltitle=new Label(MoleGame.myBundle.get("level")+Integer.toString(lvl)+" ==", skin, "big");
		lvltitle.setPosition(stage.getWidth()/2-lvltitle.getWidth()/2, stage.getHeight()-lvltitle.getHeight()-2*pad);
		playMenu.addActor(lvltitle);
		
		Image coinIm=new Image(Assets.manager.get(Assets.orbsilver, Texture.class));
		coinIm.setSize(stage.getHeight()/16,stage.getHeight()/16);
		coinIm.setPosition(stage.getWidth()-coinIm.getWidth()-2.5f*pad,stage.getHeight()-coinIm.getHeight()-2.5f*pad);
		coinIm.addAction(forever(sequence(alpha(0.5f,1f, sine),alpha(1,1f, sine))));
		playMenu.addActor(coinIm);
		coinsplaymenu=new Label(Integer.toString(Save.gd.silvercoins),skin);
		coinsplaymenu.setPosition(
				stage.getWidth()-3.5f*pad-stage.getHeight()/16,
				stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
		coinsplaymenu.addAction(forever(sequence(alpha(0.5f,1f, sine),alpha(1,1f, sine))));
		playMenu.addActor(coinsplaymenu);
		
		Image mole = new Image(Assets.manager.get(Assets.gover2, Texture.class));
		mole.setSize(stage.getHeight()*0.28f, stage.getHeight()*0.28f);
		mole.setPosition(2*pad, stage.getHeight()-mole.getHeight()-2*pad);
		mole.setOrigin(Align.center);
    	mole.addAction(forever(sequence(rotateTo(-15,0.5f, sine),rotateTo(15,0.5f, sine))));

		playMenu.addActor(mole);
		
		/////////// OBJECTIF TEXT /////////////
		String objectiftxt = null;
		if     (type==MoleGame.SCORELVL)
			objectiftxt = MoleGame.myBundle.get("objscore1") +Integer.toString((int) objectif)+MoleGame.myBundle.get("objscore2");
		else if(type==MoleGame.COMBOLVL)
			objectiftxt = MoleGame.myBundle.get("objcombo1") +Integer.toString((int) objectif)+MoleGame.myBundle.get("objcombo2");
		else if(type==MoleGame.BIGAIRLVL)
			objectiftxt = MoleGame.myBundle.get("objbair1")  +Integer.toString((int) objectif)+MoleGame.myBundle.get("objbair2");
		else if(type==MoleGame.CONSEQGLLVL)
			objectiftxt = MoleGame.myBundle.get("objconsgl1")+Integer.toString((int) objectif)+MoleGame.myBundle.get("objconsgl2");
		else if(type==MoleGame.DIGLVL)
			objectiftxt = MoleGame.myBundle.get("objdig1")   +Integer.toString((int) objectif)+MoleGame.myBundle.get("objdig2");
		else if(type==MoleGame.GLLVL)
			objectiftxt = MoleGame.myBundle.get("objgl1")    +Integer.toString((int) objectif)+MoleGame.myBundle.get("objgl2");
		else if(type==MoleGame.HEIGHTLVL)
			objectiftxt = MoleGame.myBundle.get("objheight1")+Integer.toString((int) objectif)+MoleGame.myBundle.get("objheight2");
		else if(type==MoleGame.DISTLVL)
			objectiftxt = MoleGame.myBundle.get("objdist1")  +Integer.toString((int) objectif)+ MoleGame.myBundle.get("objdig2");
		else if(type==MoleGame.COINLVL)
			objectiftxt = MoleGame.myBundle.get("objcoin1")  +Integer.toString((int) objectif)+ MoleGame.myBundle.get("objcoin2");
		
		//////////// LIMIT TXT /////////////
		String limittxt = null;
		if     (limittype==MoleGame.LIMITTIME)
			limittxt = MoleGame.myBundle.get("lmttime1")+Integer.toString((int) limit)+MoleGame.myBundle.get("lmttime2");
		else if(limittype==MoleGame.LIMITJUMP)
			limittxt = MoleGame.myBundle.get("lmtjump1")+Integer.toString((int) limit)+MoleGame.myBundle.get("lmtjump2");
		
		Label objectiflabel = new Label(objectiftxt + limittxt,skin, "default");
		objectiflabel.setAlignment(Align.center);
		objectiflabel.setPosition(stage.getWidth()/2-objectiflabel.getWidth()/2, lvltitle.getY()-objectiflabel.getHeight()-2*pad);
		playMenu.addActor(objectiflabel);
		
		//////////// WARNING TEXT /////////////
		String warningtxt = null;
		if     (lvl<11)
			warningtxt = MoleGame.myBundle.get("warning0");
		else if(lvl>=11 && lvl<22)
			warningtxt = MoleGame.myBundle.get("warning1");
		else if(lvl>=22 && lvl<42)
			warningtxt = MoleGame.myBundle.get("warning2");
		Label warning = new Label(warningtxt, skin);
		warning.setPosition(stage.getWidth()/2-warning.getWidth()/2, objectiflabel.getY()-warning.getHeight()-pad);
		playMenu.addActor(warning);
		
		Label hs= new Label(MoleGame.myBundle.get("best")+Integer.toString(Save.gd.scores[lvl]), skin);
		hs.setPosition(stage.getWidth()/2-hs.getWidth()/2, warning.getY()-hs.getHeight()-2*pad);
		playMenu.addActor(hs);
	
		
		Image playbutton = new Image(Assets.manager.get(Assets.play, Texture.class));
		playbutton.setSize(stage.getHeight()*0.28f, stage.getHeight()*0.28f);
		playbutton.setPosition(stage.getWidth()-playbutton.getWidth()-pad/2f, 2*pad);
		playbutton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Save.gd.lives-=1;
				Save.save();
				black.toFront();
				black.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),
						fadeIn(0.25f),run(new Runnable() {
							@Override
							public void run() {
		((Game) Gdx.app.getApplicationListener()).setScreen(new PlayScreen(lvl,type,objectif,limittype,limit,score1,score2));
					}
				})));
			}
		});
		playbutton.addAction(sequence(delay(0),forever(sequence(
				sizeTo(stage.getHeight()/4.8f, stage.getHeight()/4.2f,0.5f,sine),
				sizeTo(stage.getHeight()/4.2f, stage.getHeight()/4.8f,0.5f,sine)))));
		playMenu.addActor(playbutton);
		
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
					canceltime.addAction(sequence(show(),alpha(1,0.25f,fade)));
					cointime.addAction(alpha(1,0.25f,fade));
					timeprice.addAction(alpha(1,0.25f,fade));
				}
				if(Save.gd.timebought<10 && Save.gd.silvercoins>=TIMEPRICE){
					Save.gd.timebought+=2.5f;
					Save.gd.silvercoins-=TIMEPRICE;
					timeprice.setText(Integer.toString(Save.gd.timebought/2*TIMEPRICE));
					coinsplaymenu.setText(Integer.toString(Save.gd.silvercoins));
					coinsplaymenu.pack();
					coinsplaymenu.setPosition(
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
					buymenu.addAction(sequence(moveTo(0,stage.getHeight()), alpha(1),show(),moveTo(0,0, 0.5f, fade)));
					buymenu.setZIndex(50);
				}
				
			}
		});
		
		canceltime.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				canceltime.addAction(sequence(alpha(0,0.25f,fade),hide()));
				cointime.addAction(alpha(0,0.25f,fade));
				timeprice.addAction(alpha(0,0.25f,fade));
				int refund = Save.gd.timebought/2*TIMEPRICE;
				Save.gd.silvercoins+= refund;
				coinsplaymenu.setText(Integer.toString(Save.gd.silvercoins));
				coinsplaymenu.pack();
				coinsplaymenu.setPosition(
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
		timetimer .setPosition(stage.getWidth()/2+timetimer .getWidth()/2+7f*pad, hs.getY()-hs.getHeight()/2-pad);
		timebutton.setPosition(stage.getWidth()/2+timebutton.getWidth()/2+7f*pad, hs.getY()-hs.getHeight()/2-timebutton.getHeight()-pad);
		canceltime.setSize(stage.getHeight()/16.8f, stage.getHeight()/16.8f);
		canceltime.setPosition(timebutton.getX()+timebutton.getWidth()-canceltime.getWidth()*3/4f,
				timebutton.getY()+timebutton.getHeight()-canceltime.getHeight()*3/4f);
		playMenu.addActor(timebutton);
		playMenu.addActor(timetimer);
		playMenu.addActor(canceltime);
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
					cancelplane.addAction(sequence(show(),alpha(1,0.25f,fade)));
					coinplane.addAction(alpha(1,0.25f,fade));
					planeprice.addAction(alpha(1,0.25f,fade));
				}
				if(Save.gd.planebought<10 && Save.gd.silvercoins>=PLANEPRICE){
					Save.gd.planebought+=2.5f;
					Save.gd.silvercoins-=PLANEPRICE;
					planeprice.setText(Integer.toString(Save.gd.planebought/2*PLANEPRICE));
					coinsplaymenu.setText(Integer.toString(Save.gd.silvercoins));
					coinsplaymenu.pack();
					coinsplaymenu.setPosition(
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
					buymenu.addAction(sequence(moveTo(0,stage.getHeight()), alpha(1),show(),moveTo(0,0, 0.5f, fade)));
					buymenu.setZIndex(50);
				}
				
			}
		});
		
		cancelplane.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				cancelplane.addAction(sequence(alpha(0,0.25f,fade),hide()));
				int refund = Save.gd.planebought/2*PLANEPRICE;
				Save.gd.silvercoins+= refund;
				coinsplaymenu.setText(Integer.toString(Save.gd.silvercoins));
				coinsplaymenu.pack();
				coinsplaymenu.setPosition(
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
		planebutton.setPosition(stage.getWidth()/2-planebutton.getWidth()/2, hs.getY()-hs.getHeight()/2-planebutton.getHeight()-pad);
		planetimer .setPosition(stage.getWidth()/2-planetimer .getWidth()/2, hs.getY()-hs.getHeight()/2-pad);
		cancelplane.setSize(stage.getHeight()/16.8f, stage.getHeight()/16.8f);
		cancelplane.setPosition(planebutton.getX()+planebutton.getWidth()-cancelplane.getWidth()*3/4f,
				planebutton.getY()+planebutton.getHeight()-cancelplane.getHeight()*3/4f);
		playMenu.addActor(planebutton);
		playMenu.addActor(planetimer);
		playMenu.addActor(cancelplane);
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
					canceljp.addAction(sequence(show(),alpha(1,0.25f,fade)));
					coinjp  .addAction(alpha(1,0.25f,fade));
					jpprice .addAction(alpha(1,0.25f,fade));
					jpnumber.addAction(alpha(1,0.25f,fade));
				}
				if(Save.gd.jpbought<5 && Save.gd.silvercoins>=JPPRICE){
					Save.gd.jpbought+=1;
					Save.gd.silvercoins-=JPPRICE;
					jpprice.setText(Integer.toString(Save.gd.jpbought*JPPRICE));
					coinsplaymenu.setText(Integer.toString(Save.gd.silvercoins));
					coinsplaymenu.pack();
					coinsplaymenu.setPosition(
							stage.getWidth()-3.5f*pad-stage.getHeight()/16,
							stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
					jpnumber.setText(Integer.toString(Save.gd.jpbought));
					Color color1;
					color1=new Color(0,1,0,1);
					((RadialSprite) jptimer.getDrawable()).setAngle(360 -Save.gd.jpbought*72);
					((RadialSprite) jptimer.getDrawable()).setColor(color1);
				}
				else if(Save.gd.silvercoins<JPPRICE){
					buymenu.addAction(sequence(moveTo(0,stage.getHeight()), alpha(1),show(),moveTo(0,0, 0.5f, fade)));
					buymenu.setZIndex(50);
				}
				
			}
		});
		
		canceljp.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				canceljp.addAction(sequence(alpha(0,0.25f,fade),hide()));
				coinjp.addAction(alpha(0,0.25f,fade));
				jpprice.addAction(alpha(0,0.25f,fade));
				jpnumber.addAction(alpha(0,0.25f,fade));
				int refund = Save.gd.jpbought*JPPRICE;
				Save.gd.silvercoins+= refund;
				coinsplaymenu.setText(Integer.toString(Save.gd.silvercoins));
				coinsplaymenu.pack();
				coinsplaymenu.setPosition(
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
		jpbutton.setPosition(stage.getWidth()/2-jpbutton.getWidth()*3/2-7f*pad, hs.getY()-hs.getHeight()/2-jpbutton.getHeight()-pad);
		jptimer .setPosition(stage.getWidth()/2-jptimer .getWidth()*3/2-7f*pad, hs.getY()-hs.getHeight()/2-pad);
		jpnumber.setTouchable(Touchable.disabled);
		jpnumber.setAlignment(Align.center);
		jpnumber.setPosition(jpbutton.getX()+jpbutton.getWidth()/2-jpnumber.getWidth()/2, jpbutton.getY()+jpbutton.getWidth()/2-jpnumber.getHeight()/2);
		jpnumber.getColor().a=0.7f;
		canceljp.setSize(stage.getHeight()/16.8f, stage.getHeight()/16.8f);
		canceljp.setPosition(jpbutton.getX()+jpbutton.getWidth()-canceljp.getWidth()*3/4f,
				jpbutton.getY()+jpbutton.getHeight()-canceljp.getHeight()*3/4f);
		playMenu.addActor(jpbutton);
		playMenu.addActor(jptimer);
		playMenu.addActor(jpnumber);
		playMenu.addActor(canceljp);
		if(Save.gd.ujp==0){
			jptimer.setVisible(false);
			jpbutton.setVisible(false);
			jpnumber.setVisible(false);
		}
		
		coinjp.setPosition(jpbutton.getX()+jpbutton.getWidth()-coinjp.getWidth(),
				jpbutton.getY()-coinjp.getHeight()-pad/2);
		playMenu.addActor(coinjp);
		jpprice.setPosition(jpbutton.getX()+jpbutton.getWidth()-pad-coinjp.getWidth()-jpprice.getWidth(),
				coinjp.getY()+coinjp.getHeight()/2-jpprice.getHeight()/2f);
		playMenu.addActor(jpprice);
		
		coinplane.setPosition(planebutton.getX()+planebutton.getWidth()-coinplane.getWidth(),
				planebutton.getY()-coinplane.getHeight()-pad/2);
		playMenu.addActor(coinplane);
		planeprice.setPosition(planebutton.getX()+planebutton.getWidth()-pad-coinplane.getWidth()-planeprice.getWidth(),
				coinplane.getY()+coinplane.getHeight()/2-planeprice.getHeight()/2f);
		playMenu.addActor(planeprice);
		
		cointime.setPosition(timebutton.getX()+timebutton.getWidth()-cointime.getWidth(),
				timebutton.getY()-cointime.getHeight()-pad/2);
		playMenu.addActor(cointime);
		timeprice.setPosition(timebutton.getX()+timebutton.getWidth()-pad-cointime.getWidth()-timeprice.getWidth(),
				cointime.getY()+cointime.getHeight()/2-timeprice.getHeight()/2f);
		playMenu.addActor(timeprice);
		
		Image rbutton = new Image(Assets.manager.get(Assets.back, Texture.class));
		rbutton.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
		rbutton.setPosition(2*pad, 2*pad);
		rbutton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				int refund = Save.gd.jpbought*JPPRICE;
				Save.gd.silvercoins+= refund;
				coinsplaymenu.setText(Integer.toString(Save.gd.silvercoins));
				Save.gd.jpbought=0;
				refund = Save.gd.planebought/2*PLANEPRICE;
				Save.gd.silvercoins+= refund;
				coinsplaymenu.setText(Integer.toString(Save.gd.silvercoins));
				Save.gd.planebought=0;
				refund = Save.gd.timebought/2*TIMEPRICE;
				Save.gd.silvercoins+= refund;
				coinsplaymenu.setText(Integer.toString(Save.gd.silvercoins));
				Save.gd.timebought=0;
				menuButton.setTouchable(Touchable.enabled);
				exmenubutton.setTouchable(Touchable.enabled);
				
				playMenu.addAction(sequence(moveTo(0,stage.getHeight(),0.5f, fade), hide()));
				//if(menuButton.isChecked()) mainMenu.addAction(sequence(show(), alpha(1, 0.5f, fade)));
			}
		});
		playMenu.addActor(rbutton);
		
		playMenu.clearActions();
		playMenu.getColor().a =  0;
		playMenu.addAction(sequence(moveTo(0,stage.getHeight()), alpha(1),show(),moveTo(0,0,0.5f, fade)));
		stage.addActor(playMenu);
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
				if(Save.gd.sound)click.play();
				if(Save.gd.silvercoins>=livesprice && Save.gd.lives==0){
					Save.gd.lives=5;
					lifenumber.setText(Integer.toString(Save.gd.lives));
					Save.gd.silvercoins-=livesprice;
					coinslifemenu.setText(Integer.toString(Save.gd.silvercoins));
					coinslifemenu.pack();
					coinslifemenu.setPosition(
							stage.getWidth()-3.5f*pad-stage.getHeight()/16,
							stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
					lifemenu.addAction(sequence(moveTo(0,stage.getHeight(),0.5f, fade), hide()));
				}
				else if(Save.gd.silvercoins<livesprice){
					buymenu.addAction(sequence(moveTo(0,stage.getHeight()), alpha(1),show(),moveTo(0,0, 0.5f, fade)));
					buymenu.setZIndex(50);
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
				if(Save.gd.sound)click.play();
				menuButton.setTouchable(Touchable.enabled);
				exmenubutton.setTouchable(Touchable.enabled);
				lifemenu.addAction(sequence(moveTo(0,stage.getHeight(),0.5f, fade), hide()));
				//if(menuButton.isChecked()) mainMenu.addAction(sequence(show(), alpha(1, 0.5f, fade)));
			}
		});
		lifemenu.addActor(rbutton);
		lifemenu.clearActions();
		lifemenu.getColor().a =  0;
		lifemenu.addAction(sequence(moveTo(0,stage.getHeight()), alpha(1),show(),moveTo(0,0,0.5f, fade)));
		stage.addActor(lifemenu);
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
				if(Save.gd.sound)click.play();
				menuButton.setTouchable(Touchable.enabled);
				exmenubutton.setTouchable(Touchable.enabled);
				buymenu.addAction(sequence(moveTo(0,stage.getHeight(),0.5f, fade), hide()));
				//if(menuButton.isChecked()) mainMenu.addAction(sequence(show(), alpha(1, 0.5f, fade)));
			}
		});
		buy1.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				MoleGame.shop.buyCoin1();
			}
		});
		buy2.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				MoleGame.shop.buyCoin2();
			}
		});
		buy3.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				MoleGame.shop.buyCoin3();
			}
		});
		buy4.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				MoleGame.shop.buyCoin4();
			}
		});
		buy5.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				MoleGame.shop.buyCoin5();
			}
		});
		buy6.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				MoleGame.shop.buyCoin6();
			}
		});
		buy7.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				MoleGame.shop.buyCoin7();
			}
		});
		buy8.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				MoleGame.shop.buyCoin8();
			}
		});
		
		buymenu.getColor().a=0;
		buymenu.setVisible(false);
		
	}
	
	private void events () {
		//main menu
		menuButton .addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				if(playMenu!=null) 
					playMenu.addAction(sequence(alpha(0, 0.5f, fade), hide()));
				mainMenu.clearActions();
				mainMenu.getColor().a = mainMenu.isVisible() ? 1 : 0;
				if (mainMenu.isVisible()){
					menuBg.setTouchable(Touchable.disabled);
					mainMenu.addAction(sequence(alpha(0, 0.5f, fade), hide()));
					buttonset.addAction(sequence(alpha(0, 0.5f, fade), hide()));
					map.addAction(alpha(1, 0.5f, fade));
				}
				else{
					menuBg.setTouchable(Touchable.enabled);
					mainMenu.addAction(sequence(show(), alpha(1, 0.5f, fade)));
					buttonset.addAction(sequence(show(), alpha(1, 0.5f, fade)));
					map.addAction(alpha(0.5f, 0.5f, fade));
				}
			}
		});
		
		menuBg.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				mainMenu.clearActions();
				buttonset.clearActions();
				menuBg.setTouchable(Touchable.disabled);
				mainMenu.addAction(sequence(alpha(0, 0.5f, fade), hide()));
				buttonset.addAction(sequence(alpha(0, 0.5f, fade), hide()));
				map.addAction(sequence(alpha(1, 0.5f, fade)));
			}
		});
		
		exmenubutton .addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				mainMenu.clearActions();
				buttonset.clearActions();
				menuBg.setTouchable(Touchable.disabled);
				mainMenu.addAction(sequence(alpha(0, 0.5f, fade), hide()));
				buttonset.addAction(sequence(alpha(0, 0.5f, fade), hide()));
				map.addAction(sequence(alpha(1, 0.5f, fade)));
			}
		});
		
		titleButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				black.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),
						fadeIn(0.25f),run(new Runnable() {
							@Override
							public void run() {
								((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu());
					}
				})));
				
			}
		});
		
		shopButton .addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound)click.play();
				if(Save.gd.newstuff){
					unlockshop.setVisible(false);
					unlockmenu.setVisible(false);
					Save.gd.newstuff=false;
				}
				menuButton.setTouchable(Touchable.disabled);
				exmenubutton.setTouchable(Touchable.disabled);
				shopgroup.addAction(sequence(moveTo(0,stage.getHeight()), alpha(1),show(),moveTo(0,0, 0.5f, fade)));
			}
		});
		
	}
	
	private void unlock(Texture texture, String txt){
		final Group unlock= new Group();
		Image unlockedIm = new Image(texture);
		unlockedIm.setSize(stage.getHeight()*0.28f, stage.getHeight()*0.28f);
		unlockedIm.setPosition(stage.getWidth()/2-unlockedIm.getWidth()/2, stage.getHeight()/2-unlockedIm.getHeight()/2);
		unlockedIm.getColor().a=0;
		unlockedIm.setOrigin(Align.center);
		unlockedIm.addAction(sequence(delay(2),forever(sequence(rotateTo(5,0.5f,sine),rotateTo(-5,0.5f,sine)))));
		unlockedIm.addAction(parallel(
				sequence(delay(1),
						sizeTo(stage.getHeight(),stage.getHeight()),
						sizeTo(stage.getHeight()*0.28f,stage.getHeight()*0.28f, 1, pow2In)),
				sequence(delay(1),
						moveTo(stage.getWidth()/2-stage.getHeight()/2,0),
						moveTo(stage.getWidth()/2-stage.getHeight()*0.28f/2, stage.getHeight()/2-stage.getHeight()*0.28f/2, 1, pow2In)),	
				sequence(delay(1),
						alpha(0),
						alpha(1, 1, pow2In))	
				));
		
		Label unlockedtxt=new Label(txt, skin);
		unlockedtxt.setPosition(stage.getWidth()/2-unlockedtxt.getWidth()/2, unlockedIm.getY()-unlockedtxt.getHeight()-pad);
		unlockedtxt.setTouchable(Touchable.disabled);
		
		Image bg=new Image(skin.newDrawable("white"));
		bg.getColor().a=0;
		bg.setSize(stage.getWidth(), stage.getHeight());
		bg.setPosition(0, 0);
		bg.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(Save.gd.sound) click.play();
				unlock.addAction(sequence(alpha(0,0.2f),hide()));
			}
		});
		if(Save.gd.nextlvl==(13 | 34 | 51)){
			unlockedIm.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if(Save.gd.sound)click.play();
					MoleGame.gservices.rateGame();
				}
			});
			unlockedtxt.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if(Save.gd.sound)click.play();
					MoleGame.gservices.rateGame();
				}
			});
		}
		unlock.addActor(bg);
		unlock.addActor(unlockedIm);
		unlock.addActor(unlockedtxt);
		unlock.setVisible(false);
		unlock.getColor().a=0;
		stage.addActor(unlock);
		unlock.addAction(sequence(delay(0),show(),alpha(1,1f)));
	}
	
	private Group createMap(int starNbr ) {
		universSize=2*stage.getHeight()+stage.getHeight()/2;
		starsTable=new Group();
		starsTable.setHeight(universSize);
		starsTable.setWidth(0);
		Image map1= new Image(Assets.manager.get(Assets.map1, Texture.class));
		Image map2= new Image(Assets.manager.get(Assets.map2, Texture.class));
		Image map3= new Image(Assets.manager.get(Assets.map3, Texture.class));
		map1.setSize(stage.getWidth(), stage.getHeight());
		map2.setSize(stage.getWidth(), stage.getHeight());
		map3.setSize(stage.getWidth(), stage.getHeight());
		map1.setPosition(0, 0);
		map2.setPosition(0,   stage.getHeight());
		map3.setPosition(0, 2*stage.getHeight());
		Label lvl1 = new Label(MoleGame.myBundle.get("map1"), skin);
		Image tree1 = new Image(Assets.manager.get(Assets.tree1, Texture.class));
		Image tree2 = new Image(Assets.manager.get(Assets.tree0, Texture.class));
		Image grass = new Image(Assets.manager.get(Assets.grass3, Texture.class));
		tree1.setSize(stage.getHeight()/5, stage.getHeight()/5);
		tree2.setSize(stage.getHeight()/5, stage.getHeight()/5);
		grass.setSize(stage.getWidth()/3, stage.getWidth()/3/7.1f);
		tree1.setPosition(225*sizeratiow, 160*sizeratioh);
		tree2.setPosition(275*sizeratiow, 160*sizeratioh);
		grass.setPosition(275*sizeratiow-grass.getWidth()/2, 160*sizeratioh-grass.getHeight()/2);
		grass.setColor(0.7f, 0.7f, 0.7f, 1f);
		tree1.setOrigin(tree1.getWidth()/2, 0);
		tree2.setOrigin(tree2.getWidth()/2, 0);
		tree1.addAction(forever(sequence(rotateTo(30,0.75f,sine),rotateTo(15,0.75f,sine))));
		tree2.addAction(forever(sequence(rotateTo(-30,0.75f,sine),rotateTo(-15,0.75f,sine))));
		lvl1.setPosition(98*sizeratiow,331*sizeratioh);
		Label lvl2 = new Label(MoleGame.myBundle.get("map2"), skin);
		lvl2.setPosition(483*sizeratiow,640*sizeratioh);
		Image cloud1 = new Image(Assets.manager.get(Assets.cloud, Texture.class));
		Image cloud2 = new Image(Assets.manager.get(Assets.cloud, Texture.class));
		cloud1.setSize(stage.getHeight()/4, stage.getHeight()/8);
		cloud2.setSize(stage.getHeight()/4, stage.getHeight()/8);
		cloud1.setPosition(740*sizeratiow,850*sizeratioh);
		cloud2.setPosition(710*sizeratiow,800*sizeratioh);
		cloud1.addAction(forever(parallel(
				sequence(alpha(0.7f,0.8f,fade),delay(1.4f),alpha(0.0f,0.8f,fade)),
				sequence(moveTo(740f*sizeratiow,850f*sizeratioh),moveTo(510*sizeratiow,850*sizeratioh,3f))
				)));
		cloud2.addAction(forever(parallel(
				sequence(alpha(0.7f,0.8f,fade),delay(1.4f),alpha(0.0f,0.8f,fade)),
				sequence(moveTo(685*sizeratiow,800*sizeratioh),moveTo(500*sizeratiow,800*sizeratioh,3f))
				)));
		if(Save.gd.nextlvl<=22){
			lvl2.getColor().a=0;
			cloud1.setVisible(false);
			cloud2.setVisible(false);
		}
		Label lvl3 = new Label(MoleGame.myBundle.get("map3"), skin);
		if(Save.gd.nextlvl<42){
			lvl3.getColor().a=0;
		}
		lvl3.setPosition(124*sizeratiow,1122*sizeratioh);
		starsTable.addActor(map1);
		starsTable.addActor(map2);
		starsTable.addActor(map3);
		starsTable.addActor(lvl1);
		starsTable.addActor(tree2);
		starsTable.addActor(tree1);
		starsTable.addActor(grass);
		
		starsTable.addActor(lvl2);
		starsTable.addActor(cloud1);
		starsTable.addActor(cloud2);
		starsTable.addActor(lvl3);
		pathImages = new Array<Image>();
		if(Save.gd.nextlvl!=0)
			for(int i=1;i<Save.gd.nextlvl;i++){
				if(!Save.gd.newlvlunloked)
					drawPath(Save.gd.starX[i-1]*sizeratiow+stage.getHeight()/8.4f/2,Save.gd.starX[i]*sizeratiow+stage.getHeight()/8.4f/2,
							 Save.gd.starY[i-1]*sizeratioh+stage.getHeight()/8.4f/2,Save.gd.starY[i]*sizeratioh+stage.getHeight()/8.4f/2,
							 0.9f, 0.6f, 0.3f, 1,0,0);
				else if(i==Save.gd.nextlvl-1 && Save.gd.newlvlunloked){
					drawPath(Save.gd.starX[i-1]*sizeratiow+stage.getHeight()/8.4f/2,Save.gd.starX[i]*sizeratiow+stage.getHeight()/8.4f/2,
							 Save.gd.starY[i-1]*sizeratioh+stage.getHeight()/8.4f/2,Save.gd.starY[i]*sizeratioh+stage.getHeight()/8.4f/2,
							 0.9f, 0.6f, 0.3f, 1,0.1f,1.1f);
					Save.gd.newlvlunloked=false;
				}
				else if(i<Save.gd.nextlvl-1 && Save.gd.newlvlunloked)
					drawPath(Save.gd.starX[i-1]*sizeratiow+stage.getHeight()/8.4f/2,Save.gd.starX[i]*sizeratiow+stage.getHeight()/8.4f/2,
							 Save.gd.starY[i-1]*sizeratioh+stage.getHeight()/8.4f/2,Save.gd.starY[i]*sizeratioh+stage.getHeight()/8.4f/2,
							 0.9f, 0.6f, 0.3f, 1,0,0);
			}
		for(int i=0; i< starNbr; i++){
			starsTable.addActor(levelbuttons(i));
		}
		playerX=Save.gd.starX[Save.gd.nextlvl]*sizeratiow;
		playerY=Save.gd.starY[Save.gd.nextlvl]*sizeratioh;
		return starsTable;
		
	}
	
	private Group levelbuttons(int lvlnbr) {
		//create map
		Group lvl = new Group();
		Image lvlimage = new Image(Assets.manager.get(Assets.empty, Texture.class));
		lvl.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
		lvl.setUserObject(lvlnbr);
		lvl.setPosition(Save.gd.starX[lvlnbr]*sizeratiow,Save.gd.starY[lvlnbr]*sizeratioh);
		lvlimage.setSize(stage.getHeight()/8.4f, stage.getHeight()/8.4f);
		Label lvltxtnbr= new Label(Integer.toString(lvlnbr), skin);
		lvltxtnbr.setAlignment(Align.center);
		lvltxtnbr.setPosition(stage.getHeight()/16.8f-lvltxtnbr.getWidth()/2, stage.getHeight()/16.8f-lvltxtnbr.getHeight()/2);
		lvl.addActor(lvlimage);
		lvl.addActor(lvltxtnbr);
		
		
		if(lvlnbr<Save.gd.nextlvl) {
			Image star1 = new Image(Assets.manager.get(Assets.stuns, Texture.class));
			star1.setSize(stage.getHeight()/8.4f/3f, stage.getHeight()/8.4f/3f);
			star1.setPosition(0, stage.getHeight()/8.4f*1/4f-star1.getHeight()/1.8f);
			lvl.addActor(star1);
			
			Image star2 = new Image(Assets.manager.get(Assets.stuns, Texture.class));
			star2.setSize(stage.getHeight()/8.4f/3f, stage.getHeight()/8.4f/3f);
			star2.setPosition(stage.getHeight()/8.4f/2-star2.getWidth()/2, -star2.getHeight()/3);
			lvl.addActor(star2);
			
			Image star3 = new Image(Assets.manager.get(Assets.stuns, Texture.class));
			star3.setSize(stage.getHeight()/8.4f/3f, stage.getHeight()/8.4f/3f);
			star3.setPosition(stage.getHeight()/8.4f-star3.getWidth(), stage.getHeight()/8.4f*1/4f-star2.getHeight()/1.8f);
			lvl.addActor(star3);
			
			star1.setTouchable(Touchable.disabled);
			star2.setTouchable(Touchable.disabled);
			star3.setTouchable(Touchable.disabled);
			
			star2.getColor().a=0;
			star3.getColor().a=0;

			if(Save.gd.scores[lvlnbr]>Save.gd.lvlscar.get(lvlnbr*6+4))
				star2.setColor(1, 1, 1, 1);
			if(Save.gd.scores[lvlnbr]>Save.gd.lvlscar.get(lvlnbr*6+5))
				star3.setColor(1, 1, 1, 1);
			lvl.addListener(playlvllistener);
		}
		else if (lvlnbr==Save.gd.nextlvl && lvlnbr<42){ /////// 42 = last published level
			lvlimage.addAction(forever(sequence(
					sizeTo(stage.getHeight()/8.0f, stage.getHeight()/8.8f,0.5f,sine),
					sizeTo(stage.getHeight()/8.8f, stage.getHeight()/8.0f,0.5f,sine))));
			lvltxtnbr.addAction(forever(sequence(
					moveTo(stage.getHeight()/16.8f-lvltxtnbr.getWidth()/2, stage.getHeight()/14f-lvltxtnbr.getHeight()/2,0.5f,pow2Out),
					moveTo(stage.getHeight()/16.8f-lvltxtnbr.getWidth()/2, stage.getHeight()/20f-lvltxtnbr.getHeight()/2,0.5f,pow2In))));
			lvl.addListener(playlvllistener);
		}
		else if(lvlnbr>Save.gd.nextlvl || lvlnbr>=42){
			lvl.setVisible(false);
		}
				
		
		return lvl;
	}
	
	public ClickListener playlvllistener = new ClickListener() {
		public void clicked ( InputEvent event, float xbut, float ybut) {
			if(clicktimer<=0){
				clicktimer=1;
				if(Save.gd.sound) click.play();
				int lvl=(Integer) event.getListenerActor().getUserObject();
				if(lvl==0) {
					black.toFront();
					black.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),
						fadeIn(0.35f),run(new Runnable() {
							@Override
							public void run() {
								((Game) Gdx.app.getApplicationListener()).setScreen(new Tuto0(
										0,(int)Save.gd.lvlscar.get(0),
										(int)Save.gd.lvlscar.get(1),
										(int)Save.gd.lvlscar.get(2), 
										(int)Save.gd.lvlscar.get(3),
										(int)Save.gd.lvlscar.get(4),
										(int)Save.gd.lvlscar.get(5)));
						}
					})));
				}
				else if (lvl==8){
					Save.gd.timebought=10;
					black.toFront();
					black.addAction(sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.show(),
						fadeIn(0.35f),run(new Runnable() {
							@Override
							public void run() {
			((Game) Gdx.app.getApplicationListener()).setScreen(new Tuto1(8, (int)Save.gd.lvlscar.get(8*6+0), 
					(int)Save.gd.lvlscar.get(8*6+1), 
					(int)Save.gd.lvlscar.get(8*6+2), 
					(int)Save.gd.lvlscar.get(8*6+3),
					(int)Save.gd.lvlscar.get(8*6+4),
					(int)Save.gd.lvlscar.get(8*6+5)));
						}
					})));
				}
				else{
					if(Save.gd.lives>0){
					createplaymenu(lvl, (int)Save.gd.lvlscar.get(lvl*6+0), 
										(int)Save.gd.lvlscar.get(lvl*6+1), 
										(int)Save.gd.lvlscar.get(lvl*6+2), 
										(int)Save.gd.lvlscar.get(lvl*6+3),
										(int)Save.gd.lvlscar.get(lvl*6+4),
										(int)Save.gd.lvlscar.get(lvl*6+5));
					}
					else{
						if(Save.gd.sound) no.play(1,0.65f,0);
						buylives();
					}
				}
			}
		}
	};
	
	private void drawPath(float xstart,float xend,float ystart, float yend, float r, float g, float b, float a, float time, float delay) {
		float dir=(yend-ystart)/(xend-xstart);
		float dist=(float) Math.sqrt((xstart-xend)*(xstart-xend)+(ystart-yend)*(ystart-yend));
		if((yend-ystart)>0 && (xend-xstart)>0){
			if(dir>1 ){
				for(int i=0; i<dist;i+=10){
					if(ystart+i+10<yend){
						Image pathImage = new Image(Assets.manager.get(Assets.hole1 , Texture.class));
						pathImage.setSize(stage.getHeight()/16.8f,stage.getHeight()/16.8f);
						pathImage.setPosition(xstart+i/dir-pathImage.getWidth()/2,ystart+i-pathImage.getHeight()/2);
						pathImage.setColor(r,g,b,a);pathImage.getColor().a=0;
						pathImage.addAction(sequence(delay(delay+i/10f*time), show(), alpha(1,0.1f,fade)));
						pathImages.add(pathImage);
					}
				}
			}
			else if(dir<1) {
				for(int i=0; i<dist;i+=10){
					if(xstart+i+10<xend){
						Image pathImage = new Image(Assets.manager.get(Assets.hole1 , Texture.class));
						pathImage.setSize(stage.getHeight()/16.8f,stage.getHeight()/16.8f);
						pathImage.setPosition(xstart+i-pathImage.getWidth()/2,ystart+i*dir-pathImage.getHeight()/2);
						pathImage.setColor(r,g,b,a);pathImage.getColor().a=0;
						pathImage.addAction(sequence(delay(delay+i/10f*time), show(), alpha(1,0.1f,fade)));
						pathImages.add(pathImage);
					}
				}
			}
		}
		if((yend-ystart)<0 && (xend-xstart)>0){
			if(dir<-1 ){
				for(int i=0; i<dist;i+=10){
					if(ystart-i-10>yend){
						Image pathImage = new Image(Assets.manager.get(Assets.hole1 , Texture.class));
						pathImage.setSize(stage.getHeight()/16.8f,stage.getHeight()/16.8f);
						pathImage.setPosition(xstart+i/-dir-pathImage.getWidth()/2,ystart-i-pathImage.getHeight()/2);
						pathImage.setColor(r,g,b,a);pathImage.getColor().a=0;
						pathImage.addAction(sequence(delay(delay+i/10f*time), show(), alpha(1,0.1f,fade)));
						pathImages.add(pathImage);
					}
				}
			}
			else {
				for(int i=0; i<dist;i+=10){
					if(xstart+i+10<xend){
						Image pathImage = new Image(Assets.manager.get(Assets.hole1 , Texture.class));
						pathImage.setSize(stage.getHeight()/16.8f,stage.getHeight()/16.8f);
						pathImage.setPosition(xstart+i-pathImage.getWidth()/2,ystart+i*dir-pathImage.getHeight()/2);
						pathImage.setColor(r,g,b,a);pathImage.getColor().a=0;
						pathImage.addAction(sequence(delay(delay+i/10f*time), show(), alpha(1,0.1f,fade)));
						pathImages.add(pathImage);
					}
				}
			}
		}
		if((yend-ystart)>0 && (xend-xstart)<0){
			if(dir<-1 ){
				for(int i=0; i<dist;i+=10){
					if(ystart+i+10<yend){
						Image pathImage = new Image(Assets.manager.get(Assets.hole1 , Texture.class));
						pathImage.setSize(stage.getHeight()/16.8f,stage.getHeight()/16.8f);
						pathImage.setPosition(xstart+i/dir-pathImage.getWidth()/2,ystart+i-pathImage.getHeight()/2);
						pathImage.setColor(r,g,b,a);pathImage.getColor().a=0;
						pathImage.addAction(sequence(delay(delay+i/10f*time), show(), alpha(1,0.1f,fade)));
						pathImages.add(pathImage);
					}
				}
			}
			else {
				for(int i=0; i<dist;i+=10){
					if(xstart-i-10>xend){
						Image pathImage = new Image(Assets.manager.get(Assets.hole1 , Texture.class));
						pathImage.setSize(stage.getHeight()/16.8f,stage.getHeight()/16.8f);
						pathImage.setPosition(xstart-i-pathImage.getWidth()/2,ystart+i*-dir-pathImage.getHeight()/2);
						pathImage.setColor(r,g,b,a);pathImage.getColor().a=0;
						pathImage.addAction(sequence(delay(delay+i/10f*time), show(), alpha(1,0.1f,fade)));
						pathImages.add(pathImage);
					}
				}
			}
		}
		if((yend-ystart)<0 && (xend-xstart)<0){
			if(dir>1 ){
				for(int i=0; i<dist;i+=10){
					if(ystart-i-10>yend){
						Image pathImage = new Image(Assets.manager.get(Assets.hole1 , Texture.class));
						pathImage.setSize(stage.getHeight()/16.8f,stage.getHeight()/16.8f);
						pathImage.setPosition(xstart+i/-dir-pathImage.getWidth()/2,ystart-i-pathImage.getHeight()/2);
						pathImage.setColor(r,g,b,a);pathImage.getColor().a=0;
						pathImage.addAction(sequence(delay(delay+i/10f*time), show(), alpha(1,0.1f,fade)));
						pathImages.add(pathImage);
					}
				}
			}
			else if(dir<1) {
				for(int i=0; i<dist;i+=10){
					if(xstart-i-10>xend){
						Image pathImage = new Image(Assets.manager.get(Assets.hole1 , Texture.class));
						pathImage.setSize(stage.getHeight()/16.8f,stage.getHeight()/16.8f);
						pathImage.setPosition(xstart-i-pathImage.getWidth()/2,ystart-i*dir-pathImage.getHeight()/2);
						pathImage.setColor(r,g,b,a);pathImage.getColor().a=0;
						pathImage.addAction(sequence(delay(delay+i/10f*time), show(), alpha(1,0.1f,fade)));
						pathImages.add(pathImage);
					}
				}
			}
		}
		for(int i=0;i<pathImages.size;i++){
			starsTable.addActor(pathImages.get(i));
		}
		
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
	
	public void render (float delta) {
		if(clicktimer>0){
			clicktimer-=delta;
		}
		//update coins bought
		if(MoleGame.bought==true){
			coinsbuymenu.setText(Integer.toString(Save.gd.silvercoins));
			coinsbuymenu.pack();
			coinsbuymenu.setPosition(
					stage.getWidth()-3.5f*pad-stage.getHeight()/16,
					stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
			coinsplaymenu.setText(Integer.toString(Save.gd.silvercoins));
			coinsplaymenu.pack();
			coinsplaymenu.setPosition(
					stage.getWidth()-3.5f*pad-stage.getHeight()/16,
					stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
			silvcoinsshop.setText(Integer.toString(Save.gd.silvercoins));
			silvcoinsshop.pack();
			silvcoinsshop.setPosition(
					stage.getWidth()-3.5f*pad-stage.getHeight()/16,
					stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
			coinslifemenu.setText(Integer.toString(Save.gd.silvercoins));
			coinslifemenu.pack();
			coinslifemenu.setPosition(
					stage.getWidth()-3.5f*pad-stage.getHeight()/16,
					stage.getHeight()-stage.getHeight()/16/2-2.5f*pad, Align.right);
			MoleGame.bought=false;
		}
		//initial scroll
		if(!hasScrolled){
			if(playerY>starstage.getHeight() ){
				map.setScrollY(universSize -playerY-starstage.getHeight()/2);
				if(map.getScrollY()==universSize-playerY-starstage.getHeight()/2 ||
						map.getScrollPercentY()==0) hasScrolled=true;
			}
			else{
				map.setScrollPercentY(1);
				if(map.getScrollPercentY()==1) hasScrolled=true;
			}
		}
		
		starstage.act();
		if(black.getColor().a<0.98f)starstage.draw();
		stage.act();
		stage.draw();
		
		//time calculator
		timecalc.update();
		time2life.setText(timecalc.time2);
		
		//up life when time has come
		if (timecalc.uiLifeUp){
			lifenumber.setText(Integer.toString(Save.gd.lives));
		}
		
	}
	
	public void resize (int width, int height) {
	map.setBounds(0, 0, width, height);
	}
	
	public void dispose(){
		starstage.dispose();
		timecalc.dispose();
		skin.dispose();
		stage.dispose();  //problem glitch black disposed....
	}

}
