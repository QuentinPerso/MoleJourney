package quentin.jeu.mole.utils;

import java.util.Calendar;

import com.badlogic.gdx.utils.FloatArray;

public class GameData {
	
	//////////TIME/////////////////////
	public int year, month, day, hour, minute, second, timelaps;
	public int lives;
	public int silvercoins, goldcoins;
	public boolean skin1owned;
	public boolean hat1own,hat2own;
	public int hat;
	
	public boolean music, sound, accelerometer;
	public boolean uA1, uA2, uA3, uAplane, uAgom, uAnotexistyet;
	
	//////////MAP//////////////////////
	public float[]   starX,starY;
	public FloatArray lvlscar;
	public int nextlvl, explornbr, visitnbr, playerPosEvent;
	public int lang;
	public int skin;
	public boolean isSignedIn;
	public boolean newlvlunloked, newstuff;
	
	//////////MOLE CARACT///////////
	public float rotspeed, acceler, airCtrl, vmin,claw; //max : 8,2,5,10,10
	public int uspeed, ucontrol, bspeed, bcontrol, utime,uplane,ujp,urate;
	public boolean canrock, canHrock, canlava, reward1, reward2, reward3;
	
	//////////ADVENTURE////////////////
	public int jpbought=0, planebought=0, timebought=0;
	
	//////////SCORE//////////////
	public int[] scores;
	
	
	public GameData() {
		
	}
	
	// sets up an empty high scores table
	public void init() {
		//time
		year   = Calendar.getInstance().get(Calendar.YEAR);
		month  = Calendar.getInstance().get(Calendar.MONTH);
		day    = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		hour   = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		minute = Calendar.getInstance().get(Calendar.MINUTE);
		second = Calendar.getInstance().get(Calendar.SECOND);
		
		//belongings
		lives=5;
		goldcoins=0;
		silvercoins=0;
		
		//mole specifications
		rotspeed =3;
		acceler  =1;
		airCtrl  =1;
		vmin     =5;
		
		//score
		scores= new int[5000];
		
		//skin
		skin=0;
		
		//configuration
		newlvlunloked  =  false;
		newstuff = false;
		music = true;
		sound = true;
		lang  = -1;
		
		//map
		nextlvl = 0;
		starX   = new float []{138,246,354,470,574,686,784,824,752,660,562,560,660,772,826,742,640,538,434,326,222,136,74,109,206,288,380,432,374,268,168,94,158,254,352,450,542,636,748,818,774,664,608,690,798,842,776,680,586,500,408,304,225,130,82,162 };
		starY   = new float []{44,48,56,62,66,54,68,134,192,206,232,330,354,326,414,482,502,490,456,460,480,536,624,710,728,680,720,802,878,880,860,918,996,1038,1046,1000,950,918,917,984,1076,1102,1188,1252,1242,1324,1387,1380,1404,1450,1498,1494,1432,1414,1494,1543 };
		lvlscar= new FloatArray();
	}
	
}
