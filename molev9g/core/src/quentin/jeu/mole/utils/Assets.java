package quentin.jeu.mole.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {
	
	public static AssetManager manager = new AssetManager();

	
	////////////////////////Global Bgs
	public final static String moon    = "img/backgrounds/glo/moon.png";
	public final static String mars    = "img/backgrounds/glo/mars.png";
	public final static String jupiter = "img/backgrounds/glo/jupiter.png";
	public final static String saturne = "img/backgrounds/glo/saturn.png";
	public final static String uranus  = "img/backgrounds/glo/uranus.png";
	public final static String neptune = "img/backgrounds/glo/neptune.png";
	public final static String nebula  = "img/backgrounds/glo/papillon1.png";
	
	public final static String stars1 =       "img/backgrounds/glo/star1.png";
	public final static String stars2 =       "img/backgrounds/glo/star2.png";
	public final static String asterback = "img/backgrounds/glo/asteroidback.png";
	public final static String asteroid0 =    "img/backgrounds/glo/asteroid0.png";
	public final static String asteroid1 =    "img/backgrounds/glo/asteroid1.png";
	public final static String asteroid2 =    "img/backgrounds/glo/asteroid0.png";
	
	public final static String backsky =      "img/backgrounds/glo/sky3.png";
	public final static String sky0 =         "img/backgrounds/glo/sky1.png";
	public final static String upsky =        "img/backgrounds/glo/sky2.png";
	public final static String hcloud =       "img/backgrounds/glo/hcloud.png";
	
	//////////////////////////grounds
	public final static String  ground0 = "img/backgrounds/glo/grd1.png";
	public final static String  ground1 = "img/backgrounds/glo/grd2.png";
	public final static String  ground2 = "img/backgrounds/glo/grd3.png";
	public final static String  ground3 = "img/backgrounds/glo/grd4.png";
	public final static String  ugroundjoint0 = "img/backgrounds/glo/ugrdjoint0.png";
	public final static String  ugroundjoint1 = "img/backgrounds/glo/ugrdjoint1.png";
	public final static String  ugroundjoint2 = "img/backgrounds/glo/ugrdjoint2.png";
	public final static String  ugroundjoint3 = "img/backgrounds/glo/ugrdjoint3.png";
	
	//Entities
	public final static String classicmole ="img/entities/moleclassic.pack";
	public final static String oldmole     ="img/entities/moleold.pack";
	public final static String stuns       ="img/entities/stuns.png";
	public final static String sensdef     ="img/entities/msensdef.png";
	public final static String sens1       ="img/entities/msens1.png";
	public final static String jet         ="img/entities/jet.png";
	public final static String deltaplane  ="img/entities/deltaplane.png";
	public final static String sangle      ="img/entities/sangle.png";
	public final static String hat2        ="img/entities/hat2.png";
	public final static String hat1        ="img/entities/hat1.png";
	//Trails
	public final static String hole1 = "img/objects/hole1.png";
	public final static String hole2 = "img/objects/hole2.png";
	public final static String hole3 = "img/objects/hole3.png";
	public final static String hole4 = "img/objects/hole4.png";
	
	//Particles
	public final static String dust1  = "effect/dust1.png";
	public final static String nebul  = "effect/nebula.png";
	public final static String leaf   = "effect/leaf.png";
	public final static String saku   = "effect/sakura.png";
	public final static String smoke  = "effect/particle.png";
	public final static String fire   = "effect/fire.png";
	public final static String star   = "effect/star.png";
	public final static String silver = "effect/silver.png";
	public final static String bronze = "effect/bronze.png";
	public final static String gold   = "effect/gold.png";
	
	//Bonuses & objects
	public final static String  orbgold  = "img/objects/coin2gld.png";
	public final static String  orbsilver= "img/objects/coin2slv.png";
	public final static String  orbbronze= "img/objects/coin2brz.png";
	public final static String  ufo      = "img/objects/ufo.png";
	public final static String  rock1    = "img/objects/rock1.png";
	
	
	//UI
	public final static String title    = "ui/other/title1.png";
	public final static String uiback   = "ui/buttons/uiback.png";
	public final static String play     = "ui/buttons/play.png";
	public final static String myster   = "ui/buttons/myster.png";
	public final static String shirt    = "ui/buttons/shirt.png";
	public final static String skin0ic  = "ui/buttons/moleic.png";
	public final static String skin1ic  = "ui/buttons/gomole.png";
	public final static String gover    = "ui/buttons/gameover.png";
	public final static String gover2   = "ui/buttons/gameover2.png";
	public final static String map      = "ui/buttons/map.png";
	public final static String achiev   = "ui/buttons/achiev.png";
	public final static String menu     = "ui/buttons/menu.png";
	public final static String lang     = "ui/buttons/lang.png";
	public final static String accelon  = "ui/buttons/accelon.png";
	public final static String acceloff = "ui/buttons/acceloff.png";
	public final static String empty    = "ui/buttons/empty.png";
	public final static String musicon  = "ui/buttons/musicon.png";
	public final static String musicoff = "ui/buttons/musicoff.png";
	public final static String soundon  = "ui/buttons/soundon.png";
	public final static String timer    = "ui/buttons/loadempty.png";
	public final static String time     = "ui/buttons/time.png";
	public final static String plane    = "ui/buttons/plane.png";
	public final static String jetpack  = "ui/buttons/jetpack.png";
	public final static String soundoff = "ui/buttons/soundoff.png";
	public final static String setting  = "ui/buttons/setting.png";
	public final static String back     = "ui/buttons/back.png";
	public final static String pause    = "ui/buttons/pause.png";
	public final static String raz      = "ui/buttons/raz.png";
	public final static String challenge= "ui/buttons/challenge.png";
	public final static String set      = "ui/buttons/set2.png";
	public final static String shop     = "ui/buttons/shop.png";
	public final static String map1     = "ui/map/map1.png";
	public final static String map2     = "ui/map/map2.png";
	public final static String map3     = "ui/map/map3.png";
	public final static String lives    = "ui/map/life.png";
	public final static String life1    = "ui/map/life1.png";
	public final static String life2    = "ui/map/life2.png";
	
	//Music & Sounds
	public final static String music = "sound/musiclvl.wav";
	public final static String musicspace = "sound/musicspace.wav";
	
	public final static String sprop  = "sound/propulseur.wav";
	public final static String sland  = "sound/land.wav";
	public final static String sbland = "sound/badland.wav";
	public final static String sjump  = "sound/jump.wav";
	public final static String sstun  = "sound/stun.wav";
	public final static String shurt  = "sound/hit.wav";
	public final static String sbonus = "sound/bonus.wav";
	public final static String scoin  = "sound/coin.wav";
	public final static String sdig   = "sound/dig.wav";
	public final static String swind  = "sound/wind.wav";
	public final static String sexp   = "sound/explosion.wav";
	public final static String sstar  = "sound/stars.wav";
	public final static String click  = "sound/click.wav";
	public final static String no     = "sound/no.wav";
	
	//level 1
	public final static String grass1 = "img/backgrounds/lvl1/grass1.png";
	public final static String grass2 = "img/backgrounds/lvl1/grass2.png";
	public final static String grass3 = "img/backgrounds/lvl1/grass3.png";
	public final static String hills0 = "img/backgrounds/lvl1/hill0.png";
	public final static String hills1 = "img/backgrounds/lvl1/hill1.png";
	public final static String fhills0= "img/backgrounds/lvl1/fhill0.png";
	public final static String tree0  = "img/backgrounds/lvl1/arbre1.png";
	public final static String tree1  = "img/backgrounds/lvl1/arbre2.png";
	public final static String rock   = "img/backgrounds/lvl1/rock.png";
	public final static String cloud  = "img/backgrounds/lvl1/cloud1.png";
	public final static String rainbow= "img/backgrounds/lvl1/rainbow.png";
	
	//Level 2
	public final static String monument0   = "img/backgrounds/lvl2/monument1.png";
	public final static String monument1   = "img/backgrounds/lvl2/monument2.png";
	public final static String aloe        = "img/backgrounds/lvl2/aloe.png";
	public final static String monmtbck    = "img/backgrounds/lvl2/monumentbck.png";
	public final static String monumentf   = "img/backgrounds/lvl2/monumentf.png";
	public final static String dedtree     = "img/backgrounds/lvl2/arbreded.png";
	public final static String treev       = "img/backgrounds/lvl2/arbrev1.png";
	
	//Level 3
	public final static String city      = "img/backgrounds/lvl3/city.png";
	public final static String build1bot = "img/backgrounds/lvl3/build1bot.png";
	public final static String build1top = "img/backgrounds/lvl3/build1top.png";
	public final static String build2    = "img/backgrounds/lvl3/build2.png";
	public final static String road1     = "img/backgrounds/lvl3/road1.png";
	public final static String road2     = "img/backgrounds/lvl3/road2.png";
	public final static String pipe      = "img/backgrounds/lvl3/pipe.png";
	
	
	public static void load(){
		
		
		//Global Bgs
		manager.load(backsky , Texture.class);
		manager.load(sky0    , Texture.class); 
		manager.load(upsky   , Texture.class); 
		manager.load(rainbow , Texture.class); 
		manager.load(moon    , Texture.class); 
		manager.load(mars    , Texture.class); 
		manager.load(jupiter , Texture.class);
		manager.load(saturne , Texture.class); 
		manager.load(uranus  , Texture.class); 
		manager.load(neptune , Texture.class); 
		manager.load(nebula  , Texture.class); 
		
		manager.load(stars1   , Texture.class); 
		manager.load(stars2   , Texture.class); 
		manager.load(asterback, Texture.class); 
		manager.load(asteroid0, Texture.class); 
		manager.load(asteroid1, Texture.class); 
		manager.load(asteroid2, Texture.class); 
		
		//grounds
		manager.load( ground0, Texture.class); 
		manager.load( ground1, Texture.class); 
		manager.load( ground2, Texture.class); 
		manager.load( ground3, Texture.class); 
		manager.load( hcloud, Texture.class); 
		manager.load( ugroundjoint0, Texture.class); 
		manager.load( ugroundjoint1, Texture.class); 
		manager.load( ugroundjoint2, Texture.class); 
		manager.load( ugroundjoint3, Texture.class); 

		//Entities
		manager.load(classicmole, TextureAtlas.class);
		manager.load(oldmole    , TextureAtlas.class);
		manager.load(stuns      , Texture.class);
		manager.load(sens1      , Texture.class);
		manager.load(sensdef    , Texture.class);
		manager.load(jet        , Texture.class);
		manager.load(deltaplane , Texture.class);
		manager.load(sangle     , Texture.class);
		manager.load(hat1     , Texture.class);
		manager.load(hat2     , Texture.class);

		//Trails
		manager.load(hole1, Texture.class);
		manager.load(hole2, Texture.class);
		manager.load(hole3, Texture.class);
		manager.load(hole4, Texture.class);
		
		//Particles
		manager.load(dust1 , Texture.class);
		manager.load(nebul , Texture.class);
		manager.load(leaf  , Texture.class);
		manager.load(saku  , Texture.class);
		manager.load(smoke , Texture.class);
		manager.load(fire  , Texture.class);
		manager.load(star  , Texture.class);
		manager.load(silver, Texture.class);
		manager.load(bronze, Texture.class);
		manager.load(gold  , Texture.class);
		
		//Bonuses & objects
		manager.load(orbgold  , Texture.class);
		manager.load(orbsilver, Texture.class);
		manager.load(orbbronze, Texture.class);
		manager.load(ufo      , Texture.class);
		manager.load(rock1      , Texture.class);
		
		//UI
		manager.load(title  , Texture.class);
		manager.load(lives   , Texture.class);
		manager.load(life1   , Texture.class);
		manager.load(life2   , Texture.class);
		manager.load(uiback  , Texture.class);
		manager.load(play    , Texture.class);
		manager.load(myster  , Texture.class);
		manager.load(shirt   , Texture.class);
		manager.load(skin0ic , Texture.class);
		manager.load(skin1ic , Texture.class);
		manager.load(gover   , Texture.class);
		manager.load(gover2  , Texture.class);
		manager.load(map     , Texture.class);
		manager.load(achiev  , Texture.class);
		manager.load(menu    , Texture.class);
		manager.load(lang    , Texture.class);
		manager.load(accelon , Texture.class);
		manager.load(acceloff, Texture.class);
		manager.load(empty   , Texture.class);
		manager.load(musicon , Texture.class);
		manager.load(musicoff, Texture.class);
		manager.load(soundon , Texture.class);
		manager.load(timer   , Texture.class);
		manager.load(jetpack , Texture.class);
		manager.load(plane   , Texture.class);
		manager.load(time    , Texture.class);
		manager.load(soundoff, Texture.class);
		manager.load(setting , Texture.class);
		manager.load(back    , Texture.class);
		manager.load(pause   , Texture.class);
		manager.load(raz     , Texture.class);
		manager.load(challenge, Texture.class);
		manager.load(set     , Texture.class);
		manager.load(shop    , Texture.class);
		manager.load(map1    , Texture.class);
		manager.load(map2    , Texture.class);
		manager.load(map3    , Texture.class);
		
		//Music
		manager.load(music, Music.class);
		manager.load(musicspace, Music.class);
		
		//Sounds
		manager.load(sland , Sound.class);
		manager.load(sbland, Sound.class);
		manager.load(sstun , Sound.class);
		manager.load(shurt , Sound.class);
		manager.load(scoin , Sound.class);
		manager.load(sbonus, Sound.class);
		manager.load(sjump , Sound.class);
		manager.load(sdig  , Sound.class);
		manager.load(swind , Sound.class);
		manager.load(sexp  , Sound.class);
		manager.load(sstar , Sound.class);
		manager.load(click , Sound.class);
		manager.load(no    , Sound.class);
		manager.load(sprop , Sound.class);
		
		//Level 1
		manager.load(grass1 , Texture.class);
		manager.load(grass2 , Texture.class);
		manager.load(grass3 , Texture.class);
		manager.load(hills0 , Texture.class); 
		manager.load(hills1 , Texture.class); 
		manager.load(fhills0, Texture.class); 
		manager.load(tree0  , Texture.class); 
		manager.load(tree1  , Texture.class); 
		manager.load(rock   , Texture.class); 
		manager.load(cloud  , Texture.class); 
		
		//level 2
		manager.load(monument0, Texture.class); 
		manager.load(monument1, Texture.class); 
		manager.load(aloe     , Texture.class);
		manager.load(monmtbck  , Texture.class); 
		manager.load(monumentf, Texture.class); 
		manager.load(dedtree  , Texture.class); 
		manager.load(treev    , Texture.class); 
		
		//level 3
	/*	manager.load(city     , Texture.class); 
		manager.load(build1bot, Texture.class); 
		manager.load(build1top, Texture.class);
		manager.load(build2   , Texture.class); 
		manager.load(road1    , Texture.class); 
		manager.load(road2    , Texture.class); 
		manager.load(pipe     , Texture.class); */
	}
	
	public static void dispose(){
		manager.dispose();
		manager=null;
	}
}
