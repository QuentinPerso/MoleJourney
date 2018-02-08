package quentin.jeu.mole;


import quentin.jeu.mole.screens.Splash;
import quentin.jeu.mole.utils.Assets;
import quentin.jeu.mole.utils.Save;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;


public class MoleGame extends Game {

	public static I18NBundle myBundle;
	public static Shop shop;
	public static GameServices gservices;
//	public static Facebook fb;
	public static final String TITLE = "A Mole's Journey";
	public static  String price1,price2,price3,price4,price5,price6,price7, price8;
	public static boolean bought;
	////===================ARCADE LEVELS====================///////
	public static final int ARCADELVL=1;
	///Good old time
	public static final int OLDARCADE  = 2000;
	public static final int CLAWUP01 = 5, CLAWUP02=10, CLAWUP03 =15; ////gold bonuses to dig deeper
	///New Good time
	public static final int ARCADE1  = 2001, ARCADE2=2002, ARCADE3=2003,ARCADEPLANE=2004, ARCADEJP=2005;
	
	//////Adventure level type
	public static final int SCORELVL=101, CONSEQGLLVL=250, GLLVL=145, HEIGHTLVL=122, DIGLVL=1300, BIGAIRLVL=188, COMBOLVL=42, 
			DISTLVL=70, COINLVL=72;
	public static final int LIMITTIME=1, LIMITJUMP=4;
	/////skins
	public static final int CLASSIC = 0, OLDMOLE = 1;
	
	
	public MoleGame(Shop shop, GameServices gservices) {
	      MoleGame.shop = shop;
	      MoleGame.gservices = gservices;
//	      MoleGame.fb = fb;
	   }
	
	@Override
	public void create() {
		Save.load();
		Assets.manager = new AssetManager();
		setScreen(new Splash());
		FileHandle baseFileHandle = Gdx.files.internal("internation/MyBundle");
		myBundle = I18NBundle.createBundle(baseFileHandle);
		
		
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {
		super.render();
		Gdx.graphics.setTitle(TITLE + " -- FPS: " + Gdx.graphics.getFramesPerSecond());
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

}