package quentin.jeu.mole.entities;


import quentin.jeu.mole.MoleGame;
import quentin.jeu.mole.graphics.MyParticleEffect;
import quentin.jeu.mole.utils.Assets;
import quentin.jeu.mole.utils.MyContactlistener;
import quentin.jeu.mole.utils.MyInputProc;
import quentin.jeu.mole.utils.Save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Mole {
	
	private  MyInputProc in;
	private  MyContactlistener cl;
	public Body molebody;
	private boolean fliped;

	///Mole movement
	private float y, vx, vy, timeudg/*,prevvy*/;
	public float angle,anglediff;
	
	///Ground levels
	public boolean isudg0=true, isudg1=false, isudg2=false, isudg3=false;
	private float yg0, yug1, yug2, yug3;
	
	///Mole SPECS
	public float vmax,rotspeed,airCtrl,vitesse,vmin, digdamp, landangle, acceler;
	
	///Bonus upgrade
	public float jptrust, jpduration,
				 planecontrol,planeduration,planeaerod,
				 timecontrol,timeduration ;
	
	///Ground collision
	public boolean stun = false,colided=false;
	public float blpower;
	
	////Effects control
	private float newmvalue = 1, newMvalue = 1, newvvalue = 1, newVvalue = 1;

	///Mole's view
	private Sprite bodyR, pawf1R,pawf2R,pawrR,eye1R,eye2R,tailR,stunsR, sensR, armR,legR, eyeLR;
	private Sprite sangleR, jetR,planeR,hat;
	private float animtimer,animspeed,senstimer,animsens,stuntimer=0/*, hattimer, moleposhatx, moleposhaty*/;
	private boolean sens=true/*, hatstop*/;
	private boolean animup=true;

	///Trail
	private  float[] moleposx, moleposy, moleangle;
	private  Sprite[] trails ;
	private  Sprite hole1, hole2, hole3, hole4;
	private float[] color0;
	
	///Sounds
	public Sound sjump, sland, sbland1, sbland2, sbland3, sstun,sdig, swind, sfire,sstar,scoin;
	private float sdigtimer=0.5f;
	public boolean swindplay=false;
	public boolean sdigplay=false,sfireplay=false,sstarplay=false;
	
	///Input
	public boolean leftimp=false, rightimp=false;
	private float accely0;
	
	///Particles effects
	private MyParticleEffect traileffect, bleffect, expeffect, propeffect;
	boolean explodedc=false,explodedf=false,explodeds=false, scaleds=false;
	private Texture dust, bronze, silver, gold, smoke, fire, star;
	
	///Powers & bonuses
	public float timetimer,planetimer,jptimer;
	public boolean timeslow, jetpack, jetpacked=false, plane;
	public int jpnumber;
	public int speedup=0, clawup=0, controlup=0;
	///score
	private int lvl;
	public int combo=0,backflip=0,frontflip=0;
	public int goodland=0,totalgoodland=0,goodlandui=0, badland, dist=0;
	public float holeL=0, holeLscore=0, totholeL=0;
	public boolean auto=false,autotuto=false;
	public boolean  badlanded;
	private boolean autoplane;
	
	public Mole(World world,MyInputProc in,MyContactlistener cl ,float[] color0,float[] color1, int level) {
		this.lvl=level;
		if(lvl<60 ||true){
			yg0=0; 
			yug1=-10; 
			yug2=-20; 
			yug3=-30;
		}
		
		///======= MOLE MODEL =========///
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.bullet = true;
		molebody = world.createBody(bodyDef);
		CircleShape moleshape = new CircleShape();
		moleshape.setRadius(0.5f);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = moleshape;
		fixtureDef.restitution = 0.4f;
		fixtureDef.friction = .8f;
		fixtureDef.density = 3;
		molebody.createFixture(fixtureDef).setUserData("mole");
		molebody.setTransform(0, -2, 0);
	    moleshape.dispose();
		
		////input and contacts
		this.in=in;
		this.cl=cl;
		////control
		if(Save.gd.accelerometer)
			accely0= Gdx.input.getAccelerometerY();
		/// coins collected end lvl2 : 955
		/// coins spent end lvl2 : 700
		/// total price upgrade 5600
		/// upgrade bought : speed 2, control 1, plane 1 time 1
		////Characteristics
		///max upgrade 15
		rotspeed =Save.gd.rotspeed+(Save.gd.ucontrol+Save.gd.bcontrol)*5/15f; //low 3 max 8  
		acceler  =Save.gd.acceler +(Save.gd.uspeed+Save.gd.bspeed)    *1/15f; //low 1 max 2 
		airCtrl  =Save.gd.airCtrl +(Save.gd.ucontrol+Save.gd.bcontrol)*4/15f; //low 1 max 5 
		vmin     =Save.gd.vmin    +(Save.gd.uspeed+Save.gd.bspeed)    *5/15f; //low 5 max 10
		digdamp  =Save.gd.claw    +(Save.gd.uspeed+Save.gd.bspeed)    *15/15; //low 0 max 15
		vitesse  =vmin;  
		landangle = 20;
		
		/////powers && arcade specification
		if(lvl!=MoleGame.ARCADEPLANE && lvl!=MoleGame.ARCADEJP){
			timetimer =Save.gd.timebought;
			planetimer=Save.gd.planebought;
			jpnumber  =Save.gd.jpbought;
		}
		else if(lvl==MoleGame.ARCADEPLANE){
			planetimer=10;
			jpnumber  =3;
			vmin=30;
			vitesse=vmin;
			autoplane=true;
		}
		else if(lvl==MoleGame.ARCADEJP){
			jpnumber  =2;
		}
		jptimer=3;
		//slow
		if(Save.gd.utime==1){
			timeduration=1.75f; //-=delta*duration
			timecontrol=1.75f; //(x*rotspeed)
		}
		else if(Save.gd.utime==2){
			timeduration=1.5f; //-=delta*duration
			timecontrol=3; //(x*rotspeed)
		}
		else if(Save.gd.utime==3){
			timeduration=1.25f; //-=delta*duration
			timecontrol=4; //(x*rotspeed)
		}
		//plane
		if(Save.gd.uplane==1){
			planeduration=1.f; //-=delta*duration
			planecontrol=6; //(rotspeed/ctrl)
			planeaerod=0.4f;
		}
		if(Save.gd.uplane==2){
			planeduration=0.75f; //-=delta*duration
			planecontrol=4; //(rotspeed/ctrl)
			planeaerod=0.2f;
		}
		if(Save.gd.uplane==3){
			planeduration=0.5f; //-=delta*duration
			planecontrol=2; //(rotspeed/ctrl)
			planeaerod=0.1f;
		}
		//j.p.
		if(Save.gd.ujp==1){
			jpduration=1.5f; //3-=delta*duration
			jptrust   =14; //(rotspeed/ctrl)
		}
		else if(Save.gd.ujp==2){
			jpduration=2f; //3-=delta*duration
			jptrust   =25; //(rotspeed/ctrl)
		}
		else if(Save.gd.ujp==3){
			jpduration=1.25f; //3-=delta*duration
			jptrust   =25; //(rotspeed/ctrl)
		}
		
		///========= MOLE VIEW ===========//
		
		if(Save.gd.skin==MoleGame.CLASSIC){
			TextureAtlas moleatlas= Assets.manager.get(Assets.classicmole, TextureAtlas.class);
			bodyR = new Sprite(moleatlas.findRegion("body", -1));
			pawf1R= new Sprite(moleatlas.findRegion("pawf", -1));
			pawf2R= new Sprite(moleatlas.findRegion("pawf", -1));
			pawrR = new Sprite(moleatlas.findRegion("pawr", -1));
			eye1R = new Sprite(moleatlas.findRegion("eye1", -1));
			eye2R = new Sprite(moleatlas.findRegion("eye2", -1));
			tailR = new Sprite(moleatlas.findRegion("tail", -1));
			armR  = new Sprite(moleatlas.findRegion("arm", -1));
			legR  = new Sprite(moleatlas.findRegion("leg", -1));
			eyeLR = new Sprite(moleatlas.findRegion("eyelid", -1));
			sensR = new Sprite(Assets.manager.get(Assets.sens1, Texture.class));
		}
		if(Save.gd.skin==MoleGame.OLDMOLE){
			TextureAtlas moleatlas= Assets.manager.get(Assets.oldmole, TextureAtlas.class);
		
			bodyR = new Sprite(moleatlas.findRegion("body", -1));
			pawf1R= new Sprite(moleatlas.findRegion("pawf", -1));
			pawf2R= new Sprite(moleatlas.findRegion("pawf", -1));
			pawrR = new Sprite(moleatlas.findRegion("pawr", -1));
			eye1R = new Sprite(moleatlas.findRegion("eye1", -1));
			eye2R = new Sprite(moleatlas.findRegion("eye2", -1));
			tailR = new Sprite(moleatlas.findRegion("tail", -1));
			sensR = new Sprite(Assets.manager.get(Assets.sensdef, Texture.class));
		}
		
		stunsR = new Sprite(Assets.manager.get(Assets.stuns, Texture.class));
		planeR = new Sprite(Assets.manager.get(Assets.deltaplane, Texture.class));
		jetR   = new Sprite(Assets.manager.get(Assets.jet, Texture.class));
		sangleR = new Sprite(Assets.manager.get(Assets.sangle, Texture.class));
		if(Save.gd.hat==1)
			hat = new Sprite(Assets.manager.get(Assets.hat1, Texture.class));
		else if(Save.gd.hat==2)
			hat = new Sprite(Assets.manager.get(Assets.hat2, Texture.class));
		
		// Trail
		this.color0=color0;
		int traillenght = 20;
    	moleposx = new float[traillenght];
		moleposy = new float[traillenght];
		moleangle= new float[traillenght];
		
		trails= new Sprite[traillenght];
		hole1= new Sprite(Assets.manager.get(Assets.hole1, Texture.class));
		hole2= new Sprite(Assets.manager.get(Assets.hole2, Texture.class));
		hole2.setColor(color1[0],color1[1],color1[2],1);
		hole3= new Sprite(Assets.manager.get(Assets.hole3, Texture.class));
		hole4= new Sprite(Assets.manager.get(Assets.hole4, Texture.class));
		hole1.setSize(3, 3);
		hole1.setColor(color0[0],color0[1],color0[2],1);
		for(int i = 0; i < moleposx.length; i++){
			trails[i]= hole1;
			moleangle[i]=0;
			moleposx[i]=  -1000;
			moleposy[i]=  -1000;
		}
		gold   = Assets.manager.get(Assets.gold  ,Texture.class);
		silver = Assets.manager.get(Assets.silver,Texture.class);
		bronze = Assets.manager.get(Assets.bronze,Texture.class);
		dust   = Assets.manager.get(Assets.dust1 ,Texture.class);
		smoke  = Assets.manager.get(Assets.smoke ,Texture.class);
		fire   = Assets.manager.get(Assets.fire  ,Texture.class);
		star   = Assets.manager.get(Assets.star  ,Texture.class);
		
		traileffect = new MyParticleEffect();
		traileffect.loadEmitters(Gdx.files.internal("effect/badland.p"));
		traileffect.loadEmitterImages(dust); 
		traileffect.findEmitter("Untitled").setContinuous(true);
		traileffect.scaleEffect(traileffect,0,0.02f);
		traileffect.start();
		
		bleffect = new MyParticleEffect();
		bleffect.loadEmitters(Gdx.files.internal("effect/badland.p"));
		bleffect.loadEmitterImages(dust);
		bleffect.setDuration(500);
		bleffect.scaleEffectAbs(bleffect,0  ,0.64f, 0.0f, 4.0f, 10.0f, 1.0f, 0, -0.79999995f, 0.96f, -1.0f, 0.0f, 2.0f, 4.0f, -2.0f);

		expeffect = new MyParticleEffect();
		expeffect.loadEmitters(Gdx.files.internal("effect/badland.p"),true);
		expeffect.loadEmitterImages(smoke);
		expeffect.getEmitters().get(0).additive=true;
		expeffect.getEmitters().get(0).getTint().setColors(1, 0.2f, 0);
		expeffect.scaleEffectAbs(expeffect,0  ,0.84f, 0.2f, 4.0f, 10.0f, 1.0f, 0, -0.79999995f, 0.96f, -1.0f, 0.0f, 2.0f, 4.0f, -2.0f);
		expeffect.getEmitters().get(0).setAdditive(true);
		expeffect.setPosition(100, 100);
		expeffect.reset();
		
		propeffect = new MyParticleEffect();
		propeffect.loadEmitters(Gdx.files.internal("effect/prop2.p"),true);
		propeffect.loadEmitterImages(smoke);
		propeffect.scaleEffect(propeffect,0,0.9f);
		propeffect.getEmitters().get(0).setAdditive(true);
		propeffect.setPosition(100, 100);
		propeffect.reset();
		
		
		//Mole's sounds
		sjump  =Assets.manager.get(Assets.sjump,Sound.class); 
		sland  =Assets.manager.get(Assets.sland,Sound.class); 
		sbland1=Assets.manager.get(Assets.sbland,Sound.class);
		sbland2=Assets.manager.get(Assets.shurt,Sound.class);
		sbland3=Assets.manager.get(Assets.sexp,Sound.class);
		sstun  =Assets.manager.get(Assets.sstun,Sound.class);
		sdig   =Assets.manager.get(Assets.sdig,Sound.class);
		swind  =Assets.manager.get(Assets.swind,Sound.class);
		sfire  =Assets.manager.get(Assets.sprop,Sound.class);
		sstar  =Assets.manager.get(Assets.sstar,Sound.class);
		scoin =Assets.manager.get(Assets.sbonus,Sound.class);
		
	}
	
	public void updateMole(World world, Mole mole, float delta){
		
		///plane level
		if(autoplane) auto=true;
		//=========== POWERS =============//
		
		//bullet time
		if(timeslow){
			if(timetimer>0)
				timetimer-=timeduration*delta;
			else{
				timetimer=0;
				timeslow=false;
			}
		}
		
		//jet pack
		if(jetpack && jptimer>0 && !jetpacked){
			jptimer-=jpduration*delta;
		}
		else if(jptimer<3 && jetpacked &&jpnumber>0){
			jptimer+=jpduration*delta;
		}
		else if(jptimer>3){
			jptimer=3;
			jetpacked=false;
		}
		if(jptimer<=0){
			jetpack=false;
			jetpacked=true;
		}
		//hang glider
		if(plane){
			if(planetimer>0)
				planetimer-=planeduration*delta;
			else{
				planetimer=0;
				plane=false;
			}
		}
		
		//========== LEVEL DEPENDENCIES ============//
		
		if(lvl==MoleGame.OLDARCADE){ //=============== Old school Arcade
			vitesse+=speedup;
			speedup=0;
			landangle= 20+clawup;
			rotspeed=5+controlup/2f;
			airCtrl=(float) ((1+controlup)*(1+vitesse));
			if(!autotuto){
				if     (isudg3) vitesse-=vitesse/(1*(40+clawup));
				else if(isudg2) vitesse-=vitesse/(2*(40+clawup));
				else if(isudg1) vitesse-=vitesse/(3*(40+clawup));
			}
			if(clawup>MoleGame.CLAWUP01) cl.canrock =true;
			if(clawup>MoleGame.CLAWUP02) cl.canHrock=true;
			if(clawup>MoleGame.CLAWUP03) cl.canlava =true;
		}
		else if(lvl==MoleGame.ARCADE1){
			vitesse+=speedup;
			speedup=0;
			rotspeed=Save.gd.rotspeed+controlup/4f;
			vmin=Math.min(vmin, vitesse);
			airCtrl=(float) ((Save.gd.airCtrl)*(1+vitesse));
			if(Save.gd.canrock)  cl.canrock  =true;
			if(Save.gd.canHrock) cl.canHrock =true;
			if(Save.gd.canlava)  cl.canlava  =true;
		}
		else if(lvl==MoleGame.ARCADEPLANE){
			if(!isudg0) {
				autoplane=false;
				vmin=0;
			}
			airCtrl=(float) ((Save.gd.airCtrl)*(1+vitesse));
			if(Save.gd.canrock)  cl.canrock  =true;
			if(Save.gd.canHrock) cl.canHrock =true;
			if(Save.gd.canlava)  cl.canlava  =true;
		}
		
		else if(lvl==MoleGame.ARCADE2 || lvl==MoleGame.ARCADE3){ //============ New school Arcade
			vitesse+=speedup;
			speedup=0;
			landangle= 20;
			rotspeed = Save.gd.rotspeed+controlup/4f;
			acceler  =Save.gd.acceler;
			if(!autotuto && !timeslow){
				if     (isudg3) vitesse-=vitesse/(1*(40+digdamp+clawup));
				else if(isudg2) vitesse-=vitesse/(2*(40+digdamp+clawup));
				else if(isudg1) vitesse-=vitesse/(3*(40+digdamp+clawup));
				else if(isudg0) vitesse-=vitesse/(8*(40+digdamp+clawup));
			}
			vmin=Save.gd.vmin;
			vmin=Math.min(vmin, vitesse);
			airCtrl=(float) ((Save.gd.airCtrl+controlup)*(1+vitesse));
			if(Save.gd.canrock)  cl.canrock  =true;
			if(Save.gd.canHrock) cl.canHrock =true;
			if(Save.gd.canlava)  cl.canlava  =true;
		}
		else if(lvl<11){                       //============ Adventure levels 1-10
			if(vitesse<8) vitesse+=0.06f;
			if(Save.gd.canrock)  cl.canrock  =true;
			if(Save.gd.canHrock) cl.canHrock =true;
			if(Save.gd.canlava)  cl.canlava  =true;
		}
		else if(lvl>=11 && lvl<22){             //============ Adventure levels 11-21
			vmin=Math.min(vmin, vitesse);
			airCtrl=(float) ((Save.gd.airCtrl)*(1+vitesse));
			if(Save.gd.canrock)  cl.canrock  =true;
			if(Save.gd.canHrock) cl.canHrock =true;
			if(Save.gd.canlava)  cl.canlava  =true;
		}
		else if(lvl>=22 && lvl<42){             //============ Adventure levels 22-41
			if(!timeslow && !autotuto && !auto){
				if     (isudg3 && vitesse>vmin) vitesse-=vitesse/(1*(45+digdamp));
				else if(isudg2 && vitesse>vmin) vitesse-=vitesse/(4*(45+digdamp));
				else if(isudg1 && vitesse>vmin) vitesse-=vitesse/(6*(45+digdamp));
				else if(isudg0 && vitesse>vmin) timeudg+=vitesse/(1000*(70+2*digdamp));
			}
			if(vitesse>vmin)
				vitesse-=timeudg;
			airCtrl=(float) ((Save.gd.airCtrl)*(1+vitesse));
			if(Save.gd.canrock)  cl.canrock  =true;
			if(Save.gd.canHrock) cl.canHrock =true;
			if(Save.gd.canlava)  cl.canlava  =true;
		}
		///==============================================================================================================
		
		//Input
		if(!auto && ! autotuto)
			updateInput();
		else 
			molebody.setAngularVelocity(0);
	
		//body position and characteristics
		angle= (float)(molebody.getAngle()%(2*Math.PI)) * MathUtils.radiansToDegrees;
		if (angle < 0) angle += 360;
		y =molebody.getPosition().y;
		vx=molebody.getLinearVelocity().x;
		vy=molebody.getLinearVelocity().y;
		molebody.getFixtureList().get(0).setRestitution(3/vitesse);
		
		//sounds 
		if(Save.gd.sound)updateSound(delta);
		
		//Ground level effect (trail, speed loss, collision depending on level)
		if(y<yg0) 
			isudg0=true;
		else{ 
			isudg0=false;
			timeudg=0;
		}
		if(y<yug1){
			isudg1=true;
		}
		else if(y>yug1) {
			isudg1=false;
		}
		if(y<yug2) {
			isudg2=true;
		}
		else if(y>yug2) {
			isudg2=false;
		}	
		if(y<yug3) {
			isudg3=true;
		}
		else if(y>yug3) {
			isudg3=false;
		}
		if( cl.molegrdtouch == true && vy>0 ) {
			cl.molegrdtouch = false;
		}
		/////////distance
		dist=(int) molebody.getPosition().x;
		
		///////////dig length 
		if(isudg0){
			holeL+=vitesse*delta;
			holeLscore+=vitesse*delta;
			totholeL+=vitesse*delta;
		}
		else{
			holeL=0;
			holeLscore=0;
		}
		
		//////////jump
		float anglemole180= (float) ((molebody.getAngle()%(2*Math.PI) - (int)(molebody.getAngle()/(Math.PI)%2)*2*Math.PI)*360/(2*Math.PI));
		float angletrajectory = molebody.getLinearVelocity().angle(); 
		anglediff= angletrajectory-angle;
		
		//////////////land
		if( cl.molegrdtouch == true && vy<0 ){
			if(anglediff< landangle/2 && anglediff>-landangle/2){
				goodlandui=2;
				if(Save.gd.sound)sland.play();
				totalgoodland+=1;
				goodland+=1;
				vitesse= (vitesse+1+ (backflip + frontflip)*(combo+1)*acceler);
				frontflip=0;
				backflip=0;
				combo=0;
			}
			else if(anglediff< landangle && anglediff>-landangle){
				goodlandui=1;
				if(Save.gd.sound)sland.play();
				totalgoodland+=1;
				goodland+=1;
				vitesse= (vitesse+1+ (backflip + frontflip)*(combo+1)*acceler/2f);
				frontflip=0;
				backflip=0;
				combo=0;
				
				
			}
			else {
				badland+=1;
				goodlandui=0;
				goodland=0;
				badlanded=true;
				frontflip=0;
				backflip=0;
				combo=0;
				bleffect.reset();
				bleffect.getEmitters().get(0).getTint().setColors(color0);
				bleffect.loadEmitterImages(dust);
				float exS;
				if(vitesse<50) exS= (vitesse-4)/50;
				else  exS=1;
				bleffect.scaleEffectAbs(bleffect,0  ,0.64f*exS, 0.0f, 4.0f*exS, 10.0f*exS, 1.0f*exS,
						0, -0.79999995f*exS, 0.96f*exS, -1.0f*exS, 0.0f, 2.0f*exS, 4.0f*exS, -2.0f*exS);
				bleffect.setPosition(molebody.getPosition().x, y);
				bleffect.start();
				if(!explodedc && Save.gd.sound)
					sbland1.play();
				if(explodedc && Save.gd.sound)
					sbland2.play();
				if(explodedf){
					if(Save.gd.sound)sbland3.play(1,0.5f,0);
					expeffect.reset();
					expeffect.setPosition(molebody.getPosition().x, y);
					expeffect.scaleEffectAbs(bleffect,0  ,0.64f*exS, 0.0f, 4.0f*exS, 10.0f*exS, 1.0f*exS,
							0, -0.79999995f*exS, 0.96f*exS, -1.0f*exS, 0.0f, 2.0f*exS, 4.0f*exS, -2.0f*exS);
					expeffect.start();
				}
				float speedcoef=(0.15f-0.77f)/(90-20)*Math.abs(anglediff)+(0.77f+20*(0.77f-0.15f)/(90-20));
				
				if(Math.abs(anglediff)<90 && vitesse*speedcoef>(float) Math.sqrt(vitesse) &&
						vitesse*speedcoef>vmin){
					blpower=0.5f*(vitesse-vmin)/60;
					vitesse=vitesse*speedcoef;
				}
				else if((float) Math.sqrt(vitesse)>vmin){ 
					blpower=1*(vitesse-vmin)/60f;
					vitesse = (float) Math.sqrt(vitesse);
					
				}
				else {
					blpower=1*(vitesse-vmin)/60f;
					vitesse=vmin;
				}
				
			}
			cl.molegrdtouch = false;
			
		}
		
		////////chock underground
		if(isudg0) {
			if(plane){
				plane=false;
			}
			if(  cl.moleUgrdtouch == true || cl.moleUgrdtouch1 == true || cl.moleUgrdtouch2 == true || cl.moleUgrdtouch3 == true){ 
				sdig.stop();
				stun=true;
				colided=true;
				holeL=0;
				holeLscore=0;
				if(Save.gd.sound) sstun.play();
				vitesse=5;
				cl.moleUgrdtouch = false; cl.moleUgrdtouch1 = false; cl.moleUgrdtouch2 = false; cl.moleUgrdtouch3 = false;
			}
			if(vy<-0.1 && stun == true) {
				stun=false;
				sdigplay=false;
			}
			else if(vy==0 && stun)
				molebody.applyForceToCenter(0, 100, true);
			else if(vy==0 ) 
				molebody.applyForceToCenter(0, 200, true);
			else if(stun==false) {
				if(jetpack)vitesse+=jptrust*jptimer/160;
				molebody.setLinearVelocity(vitesse*MathUtils.cos(molebody.getAngle()),vitesse*MathUtils.sin(molebody.getAngle()));
				
			}
			molebody.setLinearDamping(0);
		}
		
		////===============   NOT UNDERGROUD =====================////
		///plane
		else if(!isudg0 && plane && !stun) {
			if(vy>=0)molebody.setLinearDamping(planeaerod);
			else molebody.setLinearDamping(0);
			if(jetpack){
				molebody.applyForceToCenter(
						jptrust*jptimer*MathUtils.cos(molebody.getAngle()),
						jptrust*jptimer*MathUtils.sin(molebody.getAngle()),true);
			}
			float planespeed=molebody.getLinearVelocity().len();
			vitesse=planespeed;
			if(planespeed<10){
				float torq=MathUtils.cosDeg(angle)>=0 ? -6*(10-planespeed)*(10-planespeed):6*(10-planespeed)*(10-planespeed);
				molebody.applyTorque(torq, true);
			}
			molebody.
				setLinearVelocity(new Vector2(planespeed*MathUtils.cos(molebody.getAngle()),
					planespeed*MathUtils.sin(molebody.getAngle())));
		}
		///JetPack
		else if(!isudg0 && jetpack && !stun) {
			molebody.applyForceToCenter(
					jptrust*jptimer*MathUtils.cos(molebody.getAngle()),
					jptrust*jptimer*MathUtils.sin(molebody.getAngle()),true);
		}
		
		/*//lvl3
		if(cl.pipe) {
			stun=true;
			molebody.setLinearVelocity(0, 0);
			molebody.setGravityScale(0.5f);
		}*/
        
       //=================================== AUTOPILOT =====================================//
        if(auto==true) auto1(anglediff);
        if(autotuto==true) auto2(anglemole180);
 
	}
	
	private void auto2(float anglemole180) {
 		//rectify trajectory when underground
 		if(stun ==false){
 			if( y>yg0){
				if(anglediff< landangle-4 && anglediff>-landangle+4) 
					molebody.setFixedRotation(true);
				else 									
					molebody.applyTorque(-600/vitesse, true);
 				
				molebody.setFixedRotation(false);
 			}
 			else if( y<yg0-1 ){
 				if(vx>0){
		 			if(anglemole180>0 ) molebody.applyTorque(-(anglemole180)*5, true); //rotate down (torque-)
		 			else if(anglemole180<0 ) molebody.applyTorque(-(anglemole180)*5, true); //rotate up (torque+)
 				}
 				else{
 					if(angle>180 ) molebody.applyTorque((180-angle)*5, true); //rotate down (torque-)
	 				else if(angle<180) molebody.applyTorque((180-angle)*5, true); //rotate up (torque+)
 				}
 				molebody.setLinearVelocity(new Vector2(vitesse*MathUtils.cos(molebody.getAngle()),vitesse*MathUtils.sin(molebody.getAngle())));
 			}
 		}
	}

	private void auto1(float anglediff) {
		//fall in the right angle
		if( y>yg0){
			if(vitesse>24) 
				molebody.applyTorque(70, true);
			else if(vitesse>17){// back flip
				if(molebody.getAngle()>2*3.14f){
					fliped=true;
				}
				if(fliped==false){
					molebody.applyTorque(70, true);
				}
				else if(anglediff< 8f && anglediff>-8f) 
					molebody.setFixedRotation(true);
				else 									
					molebody.applyTorque(-600/vitesse, true);
			molebody.setFixedRotation(false);
			}
			else{
				if(anglediff< 8f && anglediff>-8f) 
					molebody.setFixedRotation(true);
				else 									
					molebody.applyTorque(-600/vitesse, true);
			molebody.setFixedRotation(false);
			}
			
		}
		
		//random trajectory when underground
		if(stun ==false){
			//body.setFixedRotation(false);
			if( y<yg0-1 ){
				if(fliped){
					molebody.setTransform(molebody.getPosition().x, molebody.getPosition().y, (float)(molebody.getAngle()-2*Math.PI));
					fliped=false;
				}
				if(vy>vitesse/2 && vx>0) molebody.setAngularVelocity(0);
				else molebody.applyTorque(100, true);
				molebody.setLinearVelocity(new Vector2(vitesse*MathUtils.cos(molebody.getAngle()),vitesse*MathUtils.sin(molebody.getAngle())));
			}
		}
	}

	public void render(SpriteBatch batch,float delta){
		
		float ratio,bodywidth, bodyheight, bodyx=0 , bodyy=0;
		float senss,sensx,sensy;
		float tailwidth,tailheight, tailx , taily;
		float stunx0= 0,stuny0 = 0,stunx1= 0, stuny1= 0,stunx2= 0, stuny2= 0, stuny3= 0,stunx3= 0,stunss = 0;
		float eyewidth, eyeheight,eyex,eyey;
		float eyelidwidth, eyelidheight,eyelidx,eyelidy;
		float pawrwidth,pawrheight, pawrx, pawry,pawroy, pawranimoffset;
		float pawf1width,pawf1height,pawf1x,pawf1y, pawf1oy;
		float shwidth, shheight,shx,shy;
		float cuissewidth,cuisseheight,cuissex,cuissey;
		float sanglewidth,sangleheight,sanglex,sangley;
		float jetwidth,jetheight,jetx,jety,propx,propy,propangle;
		float planewidth,planeheight,planex,planey;
		float hatwidth=0, hatheight=0,hatx = 0,haty = 0;
		
		
		if(vx>=0){
			pawf1R.setFlip(false, false);
			pawf2R.setFlip(false, false);
			bodyR .setFlip(false, false);
			tailR .setFlip(false, false);
			pawrR .setFlip(false, false);
			eye1R .setFlip(false, false);
			eye2R .setFlip(false, false);
			sensR .setFlip(false, false);
			if(eyeLR  !=null) eyeLR  .setFlip(false, false);
			if(armR   !=null) armR   .setFlip(false, false);
			if(legR   !=null) legR   .setFlip(false, false);
			if(planeR !=null) planeR .setFlip(false, false);
			if(sangleR!=null) sangleR.setFlip(false, false);
			if(jetR   !=null) jetR   .setFlip(false, false);
			if(hat    !=null) hat    .setFlip(false, false);
		
			animsens=1;
			
			if(!stun){
				if(y<=0){
					animtimer-=Math.sqrt(animspeed)*2*delta;
					animspeed=Math.max(0.02f, vitesse/3);
					if(!animup){
						animtimer=2;
						animup=true;
					}
					
					if(animtimer<0) 
						animtimer=2;
				}
				else {
					if(animtimer<0){
						animup=true;
						animspeed=MathUtils.random(0.1f, 0.3f);
					}
						
					if(animtimer>animspeed && animup) 
						animup=false;
					if(animup)
						animtimer-=Math.sqrt(vitesse/3)*delta; //inverse for symmetric animation
					else
						animtimer+=Math.sqrt(vitesse/3)*delta;
				}
				
			}
			ratio= (float)bodyR.getRegionHeight()/ (float)bodyR.getRegionWidth();
			bodywidth  = 1.15f;
			bodyheight = bodywidth*ratio;
			bodyx = molebody.getPosition().x - bodywidth/2f;
			bodyy = molebody.getPosition().y - bodyheight/2f;
			
			stunss=bodyheight/2.2f;
			stunx0 = molebody.getPosition().x-stunss/2;
			stuny0 = molebody.getPosition().y-stunss/2;
			stunx0 -= MathUtils.cosDeg(angle)*-(-bodywidth/17.6f);
			stuny0 -= MathUtils.sinDeg(angle)*-(-bodywidth/17.6f);
			stunx0 += MathUtils.sinDeg(angle)*-(bodyheight/2.7f);
			stuny0 += -MathUtils.cosDeg(angle)*-(bodyheight/2.7f);
			
			stunx1 = molebody.getPosition().x-stunss/2;
			stuny1 = molebody.getPosition().y-stunss/2;
			stunx1 -= MathUtils.cosDeg(angle)*-(bodywidth/7.51f);
			stuny1 -= MathUtils.sinDeg(angle)*-(bodywidth/7.51f);
			stunx1 += MathUtils.sinDeg(angle)*-(bodyheight/3.375f);
			stuny1 += -MathUtils.cosDeg(angle)*-(bodyheight/3.375f);
			
			stunx2 = molebody.getPosition().x-stunss/2;
			stuny2 = molebody.getPosition().y-stunss/2;
			stunx2 -= MathUtils.cosDeg(angle)*-(bodywidth/12.39f);
			stuny2 -= MathUtils.sinDeg(angle)*-(bodywidth/12.39f);
			stunx2 += MathUtils.sinDeg(angle)*-(bodyheight/1.875f);
			stuny2 += -MathUtils.cosDeg(angle)*-(bodyheight/1.875f);
			
			stunx3 = molebody.getPosition().x-stunss/2;
			stuny3 = molebody.getPosition().y-stunss/2;
			stunx3 -= MathUtils.cosDeg(angle)*-(bodywidth/3.31f);
			stuny3 -= MathUtils.sinDeg(angle)*-(bodywidth/3.31f);
			stunx3 += MathUtils.sinDeg(angle)*-(bodyheight/2.389f);
			stuny3 += -MathUtils.cosDeg(angle)*-(bodyheight/2.389f);
			
			ratio= (float)tailR.getRegionHeight()/ (float)tailR.getRegionWidth();
			tailwidth  = bodywidth/6.27f;
			tailheight = tailwidth*ratio;
			tailx = molebody.getPosition().x-tailwidth;
			taily = molebody.getPosition().y-tailheight/2;
			tailx -= MathUtils.cosDeg(angle)*(bodywidth/2.5f);
			taily -= MathUtils.sinDeg(angle)*(bodywidth/2.5f);
			tailx += MathUtils.sinDeg(angle)*(bodyheight/13.5f);
			taily += -MathUtils.cosDeg(angle)*(bodyheight/13.5f);
			
			if(Save.gd.skin==MoleGame.OLDMOLE){
				ratio= (float)eye1R.getRegionHeight()/ (float)eye1R.getRegionWidth();
				eyewidth  = bodywidth/8f;
				eyeheight = eyewidth*ratio;
				eyex = molebody.getPosition().x-eyewidth/2;
				eyey = molebody.getPosition().y-eyeheight/2;
				eyex -= MathUtils.cosDeg(angle)*-(bodywidth/5.5f);
				eyey -= MathUtils.sinDeg(angle)*-(bodywidth/5.5f);
				eyex += MathUtils.sinDeg(angle)*-(bodyheight/4.f);
				eyey += -MathUtils.cosDeg(angle)*-(bodyheight/4.f);
			}
			else {
				ratio= (float)eye1R.getRegionHeight()/ (float)eye1R.getRegionWidth();
				eyewidth  = bodywidth/8.6f;
				eyeheight = eyewidth*ratio;
				eyex = molebody.getPosition().x-eyewidth/2;
				eyey = molebody.getPosition().y-eyeheight/2;
				eyex -= MathUtils.cosDeg(angle)*-(bodywidth/5.5f);
				eyey -= MathUtils.sinDeg(angle)*-(bodywidth/5.5f);
				eyex += MathUtils.sinDeg(angle)*-(bodyheight/8.f);
				eyey += -MathUtils.cosDeg(angle)*-(bodyheight/8.f);
			}
			if(hat!=null){
				/*if(prevvy>0 &&vy<0){
					moleposhatx=molebody.getPosition().x-hatwidth/2;
					moleposhaty=molebody.getPosition().y-hatheight/2;
				}
				prevvy=vy;*/
				
				ratio= (float)hat.getRegionHeight()/ (float)hat.getRegionWidth();
				if (Save.gd.hat==1) hatwidth  = bodywidth/1.f;
				else if(Save.gd.hat==2)hatwidth  = bodywidth/1.56f;
				hatheight = hatwidth*ratio;
				hatx = molebody.getPosition().x-hatwidth/2;
				haty = molebody.getPosition().y-hatheight/2;
				hatx -= MathUtils.cosDeg(angle)*-(bodywidth/6.94f);
				haty -= MathUtils.sinDeg(angle)*-(bodywidth/6.94f);
				hatx += MathUtils.sinDeg(angle)*-(bodyheight/2.2f);
				haty += -MathUtils.cosDeg(angle)*-(bodyheight/2.2f);
			}
			
			ratio= (float)sangleR.getRegionHeight()/ (float)sangleR.getRegionWidth();
			sanglewidth  = bodywidth/5.277f;
			sangleheight = sanglewidth*ratio;
			sanglex = molebody.getPosition().x-sanglewidth/2;
			sangley = molebody.getPosition().y-sangleheight/2;
			sanglex -= MathUtils.cosDeg(angle)*(bodywidth/5.33f);
			sangley -= MathUtils.sinDeg(angle)*(bodywidth/5.33f);
			sanglex += MathUtils.sinDeg(angle)*(bodyheight/19.57f);
			sangley += -MathUtils.cosDeg(angle)*(bodyheight/19.57f);
			
			ratio= (float)jetR.getRegionHeight()/ (float)jetR.getRegionWidth();
			jetwidth  = bodywidth/1.269f;
			jetheight = jetwidth*ratio;
			jetx = molebody.getPosition().x-jetwidth/2;
			jety = molebody.getPosition().y-jetheight/2;
			jetx -= MathUtils.cosDeg(angle)*(bodywidth/4.845f);
			jety -= MathUtils.sinDeg(angle)*(bodywidth/4.845f);
			jetx += MathUtils.sinDeg(angle)*(-bodyheight/1.93f);
			jety += -MathUtils.cosDeg(angle)*(-bodyheight/1.93f);
			
			propx=molebody.getPosition().x;
			propy=molebody.getPosition().y;
			propx -= MathUtils.cosDeg(angle)*(bodywidth/2.1f);
			propy -= MathUtils.sinDeg(angle)*(bodywidth/2.1f);
			propx += MathUtils.sinDeg(angle)*(-bodyheight/2.2f);
			propy += -MathUtils.cosDeg(angle)*(-bodyheight/2.2f);
			propangle=180+angle;
			
			ratio= (float)planeR.getRegionHeight()/ (float)planeR.getRegionWidth();
			planewidth  = bodywidth*1.2f;
			planeheight = planewidth*ratio/1.2f;
			planex = molebody.getPosition().x-planewidth/2;
			planey = molebody.getPosition().y-planeheight/2;
			planex -= MathUtils.cosDeg(angle)*(bodywidth/8.845f);
			planey -= MathUtils.sinDeg(angle)*(bodywidth/8.845f);
			planex += MathUtils.sinDeg(angle)*(-bodyheight/2.596f);
			planey += -MathUtils.cosDeg(angle)*(-bodyheight/2.596f);
			
			if(eyeLR!=null)ratio= (float)eyeLR.getRegionHeight()/ (float)eyeLR.getRegionWidth();
			eyelidwidth  = bodywidth/9.034f;
			eyelidheight = eyelidwidth*ratio;
			eyelidx = molebody.getPosition().x-eyelidwidth/2;
			eyelidy = molebody.getPosition().y-eyelidheight/2;
			eyelidx -= MathUtils.cosDeg(angle)*-(bodywidth/5.2f);
			eyelidy -= MathUtils.sinDeg(angle)*-(bodywidth/5.2f);
			eyelidx += MathUtils.sinDeg(angle)*-(bodyheight/(3.8f+1.4f*Math.min(1, holeL/220f)));
			eyelidy += -MathUtils.cosDeg(angle)*-(bodyheight/(3.8f+1.4f*Math.min(1, holeL/220f)));
			
			if(legR!=null)ratio= (float)legR.getRegionHeight()/ (float)legR.getRegionWidth();
			cuissewidth  = bodywidth/4.164f;
			cuisseheight = cuissewidth*ratio;
			cuissex = molebody.getPosition().x-cuissewidth/2;
			cuissey = molebody.getPosition().y-cuisseheight/2;
			cuissex -= MathUtils.cosDeg(angle)*(bodywidth/2.8f);
			cuissey -= MathUtils.sinDeg(angle)*(bodywidth/2.8f);
			cuissex += MathUtils.sinDeg(angle)*(bodyheight/3f);
			cuissey += -MathUtils.cosDeg(angle)*(bodyheight/3f);
			
			if(armR!=null)ratio= (float)armR.getRegionHeight()/ (float)armR.getRegionWidth();
			shwidth  = bodywidth/3.55f;
			shheight = shwidth*ratio;
			shx = molebody.getPosition().x-shwidth/2;
			shy = molebody.getPosition().y-shheight/2;
			shx -= MathUtils.cosDeg(angle)*(bodywidth/50.3f);
			shy -= MathUtils.sinDeg(angle)*(bodywidth/50.3f);
			shx += MathUtils.sinDeg(angle)*(bodyheight/8.75f);
			shy += -MathUtils.cosDeg(angle)*(bodyheight/8.75f);
			
			ratio= (float)pawrR.getRegionHeight()/ (float)pawrR.getRegionWidth();
			pawrwidth  = bodywidth/3.23f;
			pawrheight = pawrwidth*ratio;
			pawroy = pawrheight*3/4;
			pawranimoffset=0;
			pawrx = molebody.getPosition().x-pawrwidth/4;
			pawry = molebody.getPosition().y-pawroy;
			pawrx -= MathUtils.cosDeg(angle)*(bodywidth/2.8f);
			pawry -= MathUtils.sinDeg(angle)*(bodywidth/2.8f);
			pawrx += MathUtils.sinDeg(angle)*(bodyheight/3f);
			pawry += -MathUtils.cosDeg(angle)*(bodyheight/3f);
			
			ratio= (float)pawf1R.getRegionHeight()/ (float)pawf1R.getRegionWidth();
			pawf1width  = bodywidth/1.5f;
			pawf1height = pawf1width*ratio;
			pawf1oy=pawf1height*(1/1.533f);
			pawf1x = molebody.getPosition().x-pawf1width/8.634f;
			pawf1y = molebody.getPosition().y-pawf1oy;
			pawf1x -= MathUtils.cosDeg(angle)*(bodywidth/50.3f);
			pawf1y -= MathUtils.sinDeg(angle)*(bodywidth/50.3f);
			pawf1x += MathUtils.sinDeg(angle)*(bodyheight/8.75f);
			pawf1y += -MathUtils.cosDeg(angle)*(bodyheight/8.75f);
		}
		else{
			pawf1R.setFlip(false, true);
			pawf2R.setFlip(false, true);
			bodyR .setFlip(false, true);
			tailR .setFlip(false, true);
			pawrR .setFlip(false, true);
			eye1R .setFlip(false, true);
			eye2R .setFlip(false, true);
			if(eyeLR!=null)eyeLR .setFlip(false, true);
			sensR.setFlip(false, true);
			if(armR!=null)armR.setFlip(false,true);
			if(legR!=null)legR.setFlip(false, true);
			if(planeR!=null) planeR.setFlip(false, true);
			if(sangleR!=null) sangleR.setFlip(false, true);
			if(jetR!=null) jetR.setFlip(false, true);
			if(hat!=null) hat.setFlip(false, true);
			
			animsens=-1;
			if(!stun){
				if(y<=0){
					animtimer+=Math.sqrt(animspeed)*2*delta;
					animspeed=Math.max(0.02f, vitesse/3);
					animup=false;
					if(animtimer>0) 
						animtimer=-2;
				}
				else {
					if(animtimer>0){
						animup=true;
						animspeed=MathUtils.random(0f, 0.4f);
					}
					if(animtimer<-animspeed && animup) 
						animup=false;
					if(animup)
						animtimer-=1*delta;
					else
						animtimer+=1*delta;
				}
				
			}
			
			 ratio= (float)bodyR.getRegionHeight()/ (float)bodyR.getRegionWidth();
			 bodywidth  = 1.15f;
			 bodyheight = bodywidth*ratio;
			 bodyx = molebody.getPosition().x - bodywidth/2f;
			 bodyy = molebody.getPosition().y - bodyheight/2f;

			 
			ratio= (float)tailR.getRegionHeight()/ (float)tailR.getRegionWidth();
			tailwidth  = bodywidth/6.27f;
			tailheight = tailwidth*ratio;
			tailx = molebody.getPosition().x-tailwidth;
			taily = molebody.getPosition().y-tailheight/2;
			tailx -= MathUtils.cosDeg(angle)*(bodywidth/2.5f);
			taily -= MathUtils.sinDeg(angle)*(bodywidth/2.5f);
			tailx += MathUtils.sinDeg(angle)*-(bodyheight/13.5f);
			taily += -MathUtils.cosDeg(angle)*-(bodyheight/13.5f);

			stunss=bodyheight/2.2f;
			stunx0 = molebody.getPosition().x-stunss/2;
			stuny0 = molebody.getPosition().y-stunss/2;
			stunx0 -= MathUtils.cosDeg(angle)*-(-bodywidth/17.6f);
			stuny0 -= MathUtils.sinDeg(angle)*-(-bodywidth/17.6f);
			stunx0 += MathUtils.sinDeg(angle)*(bodyheight/2.7f);
			stuny0 += -MathUtils.cosDeg(angle)*(bodyheight/2.7f);
			
			stunx1 = molebody.getPosition().x-stunss/2;
			stuny1 = molebody.getPosition().y-stunss/2;
			stunx1 -= MathUtils.cosDeg(angle)*-(bodywidth/7.51f);
			stuny1 -= MathUtils.sinDeg(angle)*-(bodywidth/7.51f);
			stunx1 += MathUtils.sinDeg(angle)*(bodyheight/3.375f);
			stuny1 += -MathUtils.cosDeg(angle)*(bodyheight/3.375f);
			
			stunx2 = molebody.getPosition().x-stunss/2;
			stuny2 = molebody.getPosition().y-stunss/2;
			stunx2 -= MathUtils.cosDeg(angle)*-(bodywidth/12.39f);
			stuny2 -= MathUtils.sinDeg(angle)*-(bodywidth/12.39f);
			stunx2 += MathUtils.sinDeg(angle)*(bodyheight/1.875f);
			stuny2 += -MathUtils.cosDeg(angle)*(bodyheight/1.875f);
			
			stunx3 = molebody.getPosition().x-stunss/2;
			stuny3 = molebody.getPosition().y-stunss/2;
			stunx3 -= MathUtils.cosDeg(angle)*-(bodywidth/3.31f);
			stuny3 -= MathUtils.sinDeg(angle)*-(bodywidth/3.31f);
			stunx3 += MathUtils.sinDeg(angle)*(bodyheight/2.389f);
			stuny3 += -MathUtils.cosDeg(angle)*(bodyheight/2.389f);
			
			if(Save.gd.skin==MoleGame.OLDMOLE){
				ratio= (float)eye1R.getRegionHeight()/ (float)eye1R.getRegionWidth();
				eyewidth  = bodywidth/8f;
				eyeheight = eyewidth*ratio;
				eyex = molebody.getPosition().x-eyewidth/2;
				eyey = molebody.getPosition().y-eyeheight/2;
				eyex -= MathUtils.cosDeg(angle)*-(bodywidth/5.5f);
				eyey -= MathUtils.sinDeg(angle)*-(bodywidth/5.5f);
				eyex += MathUtils.sinDeg(angle)*(bodyheight/4.f);
				eyey += -MathUtils.cosDeg(angle)*(bodyheight/4.f);
			}
			else{
				ratio= (float)eye1R.getRegionHeight()/ (float)eye1R.getRegionWidth();
				eyewidth  = bodywidth/8.6f;
				eyeheight = eyewidth*ratio;
				eyex = molebody.getPosition().x-eyewidth/2;
				eyey = molebody.getPosition().y-eyeheight/2;
				eyex -= MathUtils.cosDeg(angle)*-(bodywidth/5.5f);
				eyey -= MathUtils.sinDeg(angle)*-(bodywidth/5.5f);
				eyex += MathUtils.sinDeg(angle)*(bodyheight/8.f);
				eyey += -MathUtils.cosDeg(angle)*(bodyheight/8.f);
			}
			if(hat!=null){
				ratio= (float)hat.getRegionHeight()/ (float)hat.getRegionWidth();
				if (Save.gd.hat==1) hatwidth  = bodywidth/1.f;
				else if(Save.gd.hat==2)hatwidth  = bodywidth/1.56f;
				hatheight = hatwidth*ratio;
				hatx = molebody.getPosition().x-hatwidth/2;
				haty = molebody.getPosition().y-hatheight/2;
				hatx -= MathUtils.cosDeg(angle)*-(bodywidth/6.94f);
				haty -= MathUtils.sinDeg(angle)*-(bodywidth/6.94f);
				hatx += MathUtils.sinDeg(angle)*(bodyheight/2.2f);
				haty += -MathUtils.cosDeg(angle)*(bodyheight/2.2f);
			}
			
			ratio= (float)sangleR.getRegionHeight()/ (float)sangleR.getRegionWidth();
			sanglewidth  = bodywidth/5.277f;
			sangleheight = sanglewidth*ratio;
			sanglex = molebody.getPosition().x-sanglewidth/2;
			sangley = molebody.getPosition().y-sangleheight/2;
			sanglex -= MathUtils.cosDeg(angle)*(bodywidth/5.33f);
			sangley -= MathUtils.sinDeg(angle)*(bodywidth/5.33f);
			sanglex += MathUtils.sinDeg(angle)*-(bodyheight/19.57f);
			sangley += -MathUtils.cosDeg(angle)*-(bodyheight/19.57f);
			
			ratio= (float)jetR.getRegionHeight()/ (float)jetR.getRegionWidth();
			jetwidth  = bodywidth/1.269f;
			jetheight = jetwidth*ratio;
			jetx = molebody.getPosition().x-jetwidth/2;
			jety = molebody.getPosition().y-jetheight/2;
			jetx -= MathUtils.cosDeg(angle)*(bodywidth/4.845f);
			jety -= MathUtils.sinDeg(angle)*(bodywidth/4.845f);
			jetx += MathUtils.sinDeg(angle)*(bodyheight/1.93f);
			jety += -MathUtils.cosDeg(angle)*(bodyheight/1.93f);
			
			propx=molebody.getPosition().x;
			propy=molebody.getPosition().y;
			propx -= MathUtils.cosDeg(angle)*(bodywidth/2.1f);
			propy -= MathUtils.sinDeg(angle)*(bodywidth/2.1f);
			propx += MathUtils.sinDeg(angle)*(bodyheight/2.2f);
			propy += -MathUtils.cosDeg(angle)*(bodyheight/2.2f);
			propangle=180+angle;
			
			ratio= (float)planeR.getRegionHeight()/ (float)planeR.getRegionWidth();
			planewidth  = bodywidth*1.2f;
			planeheight = planewidth*ratio/1.2f;
			planex = molebody.getPosition().x-planewidth/2;
			planey = molebody.getPosition().y-planeheight/2;
			planex -= MathUtils.cosDeg(angle)*(bodywidth/8.845f);
			planey -= MathUtils.sinDeg(angle)*(bodywidth/8.845f);
			planex += MathUtils.sinDeg(angle)*(bodyheight/2.596f);
			planey += -MathUtils.cosDeg(angle)*(bodyheight/2.596f);
			
			if(eyeLR!=null)ratio= (float)eyeLR.getRegionHeight()/ (float)eyeLR.getRegionWidth();
			eyelidwidth  = bodywidth/9.034f;
			eyelidheight = eyelidwidth*ratio;
			eyelidx = molebody.getPosition().x-eyelidwidth/2;
			eyelidy = molebody.getPosition().y-eyelidheight/2;
			
			eyelidx -= MathUtils.cosDeg(angle)*-(bodywidth/5.2f);
			eyelidy -= MathUtils.sinDeg(angle)*-(bodywidth/5.2f);
			eyelidx += MathUtils.sinDeg(angle)*(bodyheight/(3.8f+1.4f*Math.min(1, holeL/220f)));
			eyelidy += -MathUtils.cosDeg(angle)*(bodyheight/(3.8f+1.4f*Math.min(1, holeL/220f)));
			
			if(legR!=null)ratio= (float)legR.getRegionHeight()/ (float)legR.getRegionWidth();
			cuissewidth  = bodywidth/4.164f;
			cuisseheight = cuissewidth*ratio;
			cuissex = molebody.getPosition().x-cuissewidth/2;
			cuissey = molebody.getPosition().y-cuisseheight/2;
			cuissex -= MathUtils.cosDeg(angle)*(bodywidth/2.8f);
			cuissey -= MathUtils.sinDeg(angle)*(bodywidth/2.8f);
			cuissex += MathUtils.sinDeg(angle)*-(bodyheight/3f);
			cuissey += -MathUtils.cosDeg(angle)*-(bodyheight/3f);
			
			ratio= (float)pawrR.getRegionHeight()/ (float)pawrR.getRegionWidth();
			pawrwidth  = bodywidth/3.23f;
			pawrheight = pawrwidth*ratio;
			pawroy=pawrheight*1/4;
			pawranimoffset=270;
			pawrx = molebody.getPosition().x-pawrwidth/4;
			pawry = molebody.getPosition().y-pawroy;
			pawrx -= MathUtils.cosDeg(angle)*(bodywidth/2.8f);
			pawry -= MathUtils.sinDeg(angle)*(bodywidth/2.8f);
			pawrx += MathUtils.sinDeg(angle)*-(bodyheight/3f);
			pawry += -MathUtils.cosDeg(angle)*-(bodyheight/3f);
			
			ratio= (float)pawf1R.getRegionHeight()/ (float)pawf1R.getRegionWidth();
			pawf1width  = bodywidth/1.5f;
			pawf1height = pawf1width*ratio;
			pawf1oy=pawf1height*(1-1/1.533f);
			pawf1x = molebody.getPosition().x-pawf1width/8.634f;
			pawf1y = molebody.getPosition().y-pawf1oy;
			pawf1x -= MathUtils.cosDeg(angle)*-(bodywidth/50.3f);
			pawf1y -= MathUtils.sinDeg(angle)*-(bodywidth/50.3f);
			pawf1x += MathUtils.sinDeg(angle)*-(bodyheight*(1/8.75f));
			pawf1y += -MathUtils.cosDeg(angle)*-(bodyheight*(1/8.75f));
			
			if(armR!=null)ratio= (float)armR.getRegionHeight()/ (float)armR.getRegionWidth();
			shwidth  = bodywidth/3.55f;
			shheight = shwidth*ratio;
			shx = molebody.getPosition().x-shwidth/2;
			shy = molebody.getPosition().y-shheight/2;
			shx -= MathUtils.cosDeg(angle)*-(bodywidth/50.3f);
			shy -= MathUtils.sinDeg(angle)*-(bodywidth/50.3f);
			shx += MathUtils.sinDeg(angle)*-(bodyheight*(1/8.75f));
			shy += -MathUtils.cosDeg(angle)*-(bodyheight*(1/8.75f));
		}
		
		if(y<0||vy>0) 
			senstimer=0;
		else if (y>0 && senstimer<1) 
			sens=true;
		else if(y>0 && senstimer>2)
			sens=false;
		if(sens) senstimer+=3/(y+12)*delta*60;
		else senstimer-=3/(y+12)*delta*60;
		
		if(stun){
			stuntimer+=0.1f*delta*60;
			if(stuntimer>5) stuntimer=0;
		}
		float a0 = 0,a1= 0,a2= 0,a3= 0;
		if(stuntimer<1)
			a0=stuntimer;
		else if(stuntimer<2){
			a0=1-stuntimer+1;
			a1=-1+stuntimer;
		}
		else if(stuntimer<3){
			a1=1-stuntimer+2;
			a2=-2+stuntimer;
		}
		else if(stuntimer<4){
			a2=1-stuntimer+3;
			a3=-3+stuntimer;
		}
		else if(stuntimer<5){
			a2=1-stuntimer+4;
			a3=-4+stuntimer;
		}
		else if(stuntimer<6){
			a3=1-stuntimer+5;
		}
				
		//stuntimer=(float) Math.PI/2;
		
		senss  = bodywidth*senstimer;
		sensx = molebody.getPosition().x - senss/2f;
		sensy = molebody.getPosition().y - senss/2f;
			

//		batch.begin();
		if(y<0){////////////////////////=============underground=================/////////////////
			batch.draw(pawf1R, pawf1x, pawf1y,pawf1width/8.634f,pawf1oy,pawf1width,pawf1height,1,1,angle+(animtimer/2-animsens*3/4.5f)*270);
			batch.draw(bodyR, bodyx, bodyy, bodywidth/2,bodyheight/2,bodywidth,bodyheight,1,1,angle);
			batch.draw(tailR, tailx, taily,tailwidth,tailheight/2,tailwidth,tailheight,1,1,angle+((animtimer-1)%0.5f)*75);
			batch.draw(pawrR, pawrx, pawry,pawrwidth/4,pawroy,pawrwidth,pawrheight,1,1,angle+(animtimer/2-animsens*1)*180);
			if(legR!=null)batch.draw(legR, cuissex, cuissey,cuissewidth/2,cuisseheight/2,cuissewidth,cuisseheight,1,1,angle);
			if(stun){
				batch.draw(eye2R, eyex, eyey,eyewidth/2,eyeheight/2,eyewidth,eyeheight,1,1,angle+90);
				if(hat!=null) batch.draw(hat, hatx, haty, hatwidth/2,hatheight/2,hatwidth,hatheight,1,1,angle);
				batch.setColor(1, 1, 1, a0);
				batch.draw(stunsR, stunx0, stuny0,stunss/2,stunss/2,stunss,stunss,1,1,angle);
				batch.setColor(1, 1, 1, a1);
				batch.draw(stunsR, stunx1, stuny1,stunss/2,stunss/2,stunss,stunss,1,1,angle);
				batch.setColor(1, 1, 1, a2);
				batch.draw(stunsR, stunx2, stuny2,stunss/2,stunss/2,stunss,stunss,1,1,angle);
				batch.setColor(1, 1, 1, a3);
				batch.draw(stunsR, stunx3, stuny3,stunss/2,stunss/2,stunss,stunss,1,1,angle);
				batch.setColor(1, 1, 1, 1);
					
			}
			else if(jetpack){
				if(eyeLR!=null)batch.draw(eyeLR, eyelidx, eyelidy,eyelidwidth/2,eyelidheight/2,eyelidwidth,eyelidheight,1,1,angle);
				if(hat!=null) batch.draw(hat, hatx, haty, hatwidth/2,hatheight/2,hatwidth,hatheight,1,1,angle);
				propeffect.setPosition(propx, propy);
				propeffect.getEmitters().get(0).getAngle().setHighMax(propangle+20);
				propeffect.getEmitters().get(0).getAngle().setLowMax (propangle-20);
				propeffect.getEmitters().get(0).getAngle().setHighMin(propangle);
				propeffect.getEmitters().get(0).getAngle().setLowMin (propangle);
				propeffect.draw(batch, delta);
				batch.draw(sangleR, sanglex, sangley, sanglewidth/2,sangleheight/2,sanglewidth,sangleheight,1,1,angle);
				batch.draw(jetR, jetx, jety, jetwidth/2,jetheight/2,jetwidth,jetheight,1,1,angle);
				batch.draw(eye1R, eyex, eyey,eyewidth/2,eyeheight/2,eyewidth,eyeheight,1,1,angle);
			}
			else{
				batch.draw(eye1R, eyex, eyey,eyewidth/2,eyeheight/2,eyewidth,eyeheight,1,1,angle);
				if(eyeLR!=null)batch.draw(eyeLR, eyelidx, eyelidy,eyelidwidth/2,eyelidheight/2,eyelidwidth,eyelidheight,1,1,angle);
				if(hat!=null) batch.draw(hat, hatx, haty, hatwidth/2,hatheight/2,hatwidth,hatheight,1,1,angle);
			}
			batch.draw(pawf2R, pawf1x, pawf1y,pawf1width/8.634f,pawf1oy,pawf1width,pawf1height,1,1,angle+(animtimer/2)*210+180);
			if(armR!=null)batch.draw(armR, shx, shy,shwidth/2,shheight/2,shwidth,shheight,1,1,angle);
		}
		else { ///////////////////============fly===================/////////////
			if(anglediff< landangle/2 && anglediff>-landangle/2) batch.setColor(0, 1, 0, 1);
			else if(anglediff< landangle && anglediff>-landangle)batch.setColor(1, 1, 0, 1);
			else batch.setColor(1, 0, 0, 1);
			
			if(senss>0) batch.draw(sensR, sensx, sensy, senss/2,senss/2,senss,senss,1,1,angle);
			batch.setColor(1, 1, 1, 1);
			batch.draw(pawf1R, pawf1x, pawf1y,pawf1width/8.634f,pawf1oy,pawf1width,pawf1height,1,1,angle+(animtimer/2-animsens*3/5f)*330);
			batch.draw(bodyR, bodyx, bodyy, bodywidth/2,bodyheight/2,bodywidth,bodyheight,1,1,angle);
			
			
			if(plane){
				batch.draw(sangleR, sanglex, sangley, sanglewidth/2,sangleheight/2,sanglewidth,sangleheight,1,1,angle);
				batch.draw(planeR, planex, planey, planewidth/2,planeheight/2,planewidth,planeheight,1,1,angle);
			}
			batch.draw(tailR, tailx, taily,tailwidth,tailheight/2,tailwidth,tailheight,1,1,angle+((animtimer-1)%0.5f)*75);
			batch.draw(pawrR, pawrx, pawry,pawrwidth/4,pawroy,pawrwidth,pawrheight,1,1,angle+(0/2-1)*130+pawranimoffset);
			if(legR!=null)batch.draw(legR, cuissex, cuissey,cuissewidth/2,cuisseheight/2,cuissewidth,cuisseheight,1,1,angle);
			if(stun){
				batch.draw(eye2R, eyex, eyey,eyewidth/2,eyeheight/2,eyewidth,eyeheight,1,1,angle+90);
				if(hat!=null) batch.draw(hat, hatx, haty, hatwidth/2,hatheight/2,hatwidth,hatheight,1,1,angle);
				batch.setColor(1, 1, 1, a0);
				batch.draw(stunsR, stunx0, stuny0,stunss/2,stunss/2,stunss,stunss,1,1,angle);
				batch.setColor(1, 1, 1, a1);
				batch.draw(stunsR, stunx1, stuny1,stunss/2,stunss/2,stunss,stunss,1,1,angle);
				batch.setColor(1, 1, 1, a2);
				batch.draw(stunsR, stunx2, stuny2,stunss/2,stunss/2,stunss,stunss,1,1,angle);
				batch.setColor(1, 1, 1, a3);
				batch.draw(stunsR, stunx3, stuny3,stunss/2,stunss/2,stunss,stunss,1,1,angle);
				batch.setColor(1, 1, 1, 1);
					
			}
			else if(jetpack){
				batch.draw(eye2R, eyex, eyey,eyewidth/2,eyeheight/2,eyewidth,eyeheight,1,1,angle);
				if(hat!=null) batch.draw(hat, hatx, haty, hatwidth/2,hatheight/2,hatwidth,hatheight,1,1,angle);
				propeffect.setPosition(propx, propy);
				propeffect.getEmitters().get(0).getAngle().setHighMax(propangle+20);
				propeffect.getEmitters().get(0).getAngle().setLowMax (propangle-20);
				propeffect.getEmitters().get(0).getAngle().setHighMin(propangle);
				propeffect.getEmitters().get(0).getAngle().setLowMin (propangle);
				propeffect.draw(batch, delta);
				batch.draw(sangleR, sanglex, sangley, sanglewidth/2,sangleheight/2,sanglewidth,sangleheight,1,1,angle);
				batch.draw(jetR, jetx, jety, jetwidth/2,jetheight/2,jetwidth,jetheight,1,1,angle);
			}
			else{
				batch.draw(eye2R, eyex, eyey,eyewidth/2,eyeheight/2,eyewidth,eyeheight,1,1,angle);
				if(hat!=null) batch.draw(hat, hatx, haty, hatwidth/2,hatheight/2,hatwidth,hatheight,1,1,angle);
			}
			batch.draw(pawf2R, pawf1x, pawf1y,pawf1width/8.634f,pawf1oy,pawf1width,pawf1height,1,1,angle+(animtimer/2+animsens*20/215f)*235-animsens*210);
			if(armR!=null)batch.draw(armR, shx, shy,shwidth/2,shheight/2,shwidth,shheight,1,1,angle);
		}
//		batch.end();
		
	}
	
	private void updateSound(float delta) {
		if( cl.molegrdtouch == true && vy>0 ) {
			sjump.play(0.05f, MathUtils.clamp((225-5*vitesse)/100,0.4f,2), 0);
			cl.molegrdtouch = false;
		}
	
		//////dig
		if( isudg0  ) {
			sfire.stop();sfireplay=false;
			swind.stop();swindplay=false;
			sdigtimer-=0.03*(vitesse/100+1); //-0.01/60
			if(sdigtimer<=0 && !stun && delta>0.0008f){
				sdig.play(0.1f,1f*delta*12*(vitesse/100+1),0); //2*0.206sec
				sdigtimer=0.412f;
			}
		}
		
		if(y<80) {
			sstar.stop(); 
			sstarplay=false;
		}
		if(!isudg0) {
			sdig.stop(); sdigplay=false;
			if(vy<-15 && y<80){
				if(!explodedf && !swindplay){
					swind.loop(1/30f);
					swindplay=true;
				}
				else if(!sfireplay){
					sfire.loop(1/10f);
					sfireplay=true;
				}
			}
		}
		
		if(jetpack && !sfireplay){
			sfire.loop(0.5f);
			sfireplay=true;
		}
		
		
    	//stars
		if(y>80 && !sstarplay) {
			sstar.loop(0.05f); sstarplay=true;
		}
		
	}

	private void updateInput() {
		if (Save.gd.accelerometer){
			float accelY = Gdx.input.getAccelerometerY();
			float accelrs=Math.min(rotspeed  ,(Math.abs(accelY)-1)*rotspeed/4f);
			if(accelY>accely0+1 ){
				if(plane) molebody.setAngularVelocity(-accelrs/planecontrol);
				else if(timeslow) molebody.setAngularVelocity(-timecontrol*accelrs);
				else molebody.setAngularVelocity(-accelrs); ////upgradable
			}
			else if(accelY<accely0-1){
				if(plane) molebody.setAngularVelocity(accelrs/planecontrol);
				else if(timeslow) molebody.setAngularVelocity(timecontrol*accelrs);
				else molebody.setAngularVelocity(accelrs);
			}
			else
				molebody.setAngularVelocity(0);
		    
		}
		else{
			if(in.rightK==true && in.leftK==false){
				if(plane) molebody.setAngularVelocity(-rotspeed/planecontrol);
				else if(timeslow) molebody.setAngularVelocity(-timecontrol*rotspeed);
				else molebody.setAngularVelocity(-rotspeed); ////upgradable
			}
			
			else if(in.leftK==true && in.rightK==false){
				if(plane) molebody.setAngularVelocity(rotspeed/planecontrol);
				else if(timeslow) molebody.setAngularVelocity(timecontrol*rotspeed);
				else molebody.setAngularVelocity(rotspeed);
			}
			else
				molebody.setAngularVelocity(0);
		}
		
		
		if(in.rightK && y>yg0 &&!rightimp){
			molebody.applyForceToCenter(airCtrl,0, true);
			rightimp=true;
			}
		if(in.leftK && y>yg0 && !leftimp) {
			molebody.applyForceToCenter(-airCtrl,0, true);
			leftimp=true;
			}
		
		if(!in.rightK)rightimp=false;
		if(!in.leftK) leftimp=false;
		
		//cheats
		if(in.upK) {molebody.applyForceToCenter(new Vector2(0,50), true);}
		if(in.downK) vitesse++;
		//if(in.cK) clawup++;
		
	}
	
	
	public void pause(){
		molebody.setAwake(false);
		molebody.setLinearVelocity(0,0);
	}
	
	
	public void dead() {
		molebody.setGravityScale(0);
		stun=true;
		molebody.setLinearVelocity(0, 0);
		molebody.setFixedRotation(true);
		
	}
	
	
	public void rendertrail(SpriteBatch batch){
		
		if(isudg3){
    		for (int i1 =moleposx.length-1; i1 > 0; i1--) {
    				moleposx[i1] =moleposx[i1-1];
    				moleposy[i1] =moleposy[i1-1];
    				moleangle[i1]=moleangle[i1-1];
    				trails[i1]=trails[i1-1];
    				moleposx[0] = molebody.getPosition().x;
    				moleposy[0] = y;
    				moleangle[0] = molebody.getAngle();
    				trails[0]=hole4;
    			}
    		}
		
		else if(isudg2){
    		for (int i1 =moleposx.length-1; i1 > 0; i1--) {
    				moleposx[i1] =moleposx[i1-1];
    				moleposy[i1] =moleposy[i1-1];
    				moleangle[i1]=moleangle[i1-1];
    				trails[i1]=trails[i1-1];
    				moleposx[0] = molebody.getPosition().x;
    				moleposy[0] = y;
    				moleangle[0] = molebody.getAngle();
    				trails[0]=hole3;
    			}
    		}
		else if(isudg1){
		for (int i1 =moleposx.length-1; i1 > 0; i1--) {
				moleposx[i1] =moleposx[i1-1];
				moleposy[i1] =moleposy[i1-1];
				moleangle[i1]=moleangle[i1-1];
				trails[i1]=trails[i1-1];
				moleposx[0] = molebody.getPosition().x;
				moleposy[0] = y;
				moleangle[0] = molebody.getAngle();
				trails[0]=hole2;
			}
		}
		else if(y<yg0-0.2f){
			for (int i1 =moleposx.length-1; i1 > 0; i1--) {
					moleposx[i1] =moleposx[i1-1];
					moleposy[i1] =moleposy[i1-1];
					moleangle[i1]=moleangle[i1-1];
					trails[i1]=trails[i1-1];
					moleposx[0] = molebody.getPosition().x;
					moleposy[0] = y;
					moleangle[0] = molebody.getAngle();
					trails[0]=hole1;
				}
			}
	    
	    //particle trail update
	    if(isudg0){
	        if(clawup<=MoleGame.CLAWUP01){
	        	traileffect.getEmitters().get(0).getTint().setColors(color0);
	        	traileffect.loadEmitterImages(dust);
	        }
	        if(clawup>MoleGame.CLAWUP01)  traileffect.loadEmitterImages(bronze);
	        if(clawup>MoleGame.CLAWUP02)  traileffect.loadEmitterImages(silver);
	        if(clawup>MoleGame.CLAWUP03)  traileffect.loadEmitterImages(gold);
	        newmvalue = (vitesse/2-2)/40;
			newMvalue = (vitesse/2-2)/20;
			newvvalue=(vx+vy)/20; 
		        newVvalue=(vx+vy)/20; 
	    }
	    if(y>yg0 && y<yg0+5) {
	    	newMvalue=1/(3.5f*y); 
	    	newmvalue=0f; 
	    	newvvalue=(vx+vy)/10; 
	    	newVvalue=(vx+vy)/10;
	    }
	    
	    if(!isudg0 &&clawup<=10){
	    }
	    
	    //clouds
	    if(y>30 && vy>0) {
	    	traileffect.getEmitters().get(0).getTint().setColors(new float[]{1,1,1});
	    	traileffect.loadEmitterImages(smoke);
	    }
	    if(y>42 && !explodedc) {
	    	bleffect.getEmitters().get(0).getTint().setColors(new float[]{1,1,1});
	    	bleffect.scaleEffectAbs(bleffect,0  ,0.64f, 0.0f, 4.0f, 10.0f, 1.0f, 0, -0.79999995f, 0.96f, -1.0f, 0.0f, 2.0f, 4.0f, -2.0f);
	    	bleffect.loadEmitterImages(smoke);
	    	bleffect.setPosition(molebody.getPosition().x, y);
	    	bleffect.start();
	    	explodedc=true;
	    }
	    //atmosphere
	    if(y>60) {
	    	traileffect.loadEmitterImages(fire);
	    }
	    
	    if(y>=60 && explodedf==false) {
	    	if(Save.gd.sound)sbland3.play(1,0.5f,0);
	    	expeffect.setPosition(molebody.getPosition().x, y);
	    	expeffect.start();
	    	explodedf=true;
	    }
	    	
	    //stars
	    if(y>80) {
	    	traileffect.loadEmitterImages(star);
	    }
	    if(y>80 && explodeds==false) {
	    	bleffect.loadEmitterImages(star);
	    	bleffect.scaleEffect(bleffect, 0, 3);
	    	bleffect.setPosition(molebody.getPosition().x, y);
	    	bleffect.start();
	    	explodeds=true;
	    	scaleds=true;
	    }
	    if(scaleds && y<70){
	    	bleffect.scaleEffect(bleffect, 0, 1/3f);
	    	scaleds=false;
	    }
	    if(explodedc && isudg0){
	    	explodedc=false;
	    	explodedf=false;
	    	explodeds=false;
	    }
	    
	   
	    traileffect.setPosition(molebody.getPosition().x, y);
	    if(y>yg0 && !explodedc) 
	    	traileffect.update(Gdx.graphics.getDeltaTime()*(1+(vitesse-5)/300),newmvalue, newMvalue, newvvalue, newVvalue,true);
	    else
	    	traileffect.update(Gdx.graphics.getDeltaTime()*(1+(vitesse-5)/300),1,1, 1, 1,true);
	    
		//DRAW TRAIL
 //   	batch.begin();
		for(int i = moleposx.length-1;i>-1;i--){
			trails[i].setOrigin(trails[0].getWidth()/2,trails[0].getHeight()/2);
			trails[i].setPosition(moleposx[i] - trails[0].getWidth()/2,moleposy[i] - trails[0].getHeight()/2);
			trails[i].setSize(1.05f+(vitesse-5)/100, 1.05f);
			trails[i].setRotation(moleangle[i]* MathUtils.radiansToDegrees);
			
			trails[i].draw(batch);
			
			}
		traileffect.draw(batch);
//		batch.end();
	}
	
	public void renderexplosion(SpriteBatch batch){
		bleffect.update(Gdx.graphics.getDeltaTime(), 1f, 1, vitesse*2,vitesse*2, false);
		expeffect.update(Gdx.graphics.getDeltaTime(), 1f, 1, vitesse*2,vitesse*2, false);
//		batch.begin();
		bleffect.draw(batch);
		expeffect.draw(batch);
//		batch.end();
	}
	
	public void dispose(){
    	traileffect.dispose();
    	bleffect.dispose();
    	expeffect.dispose();
	}

	
}
	

