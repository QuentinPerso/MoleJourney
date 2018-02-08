package quentin.jeu.mole.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

public class Save {
	
	public static GameData gd;
	private static FileHandle hsfileold = Gdx.files.local("bin/hs.json");
	private static FileHandle hsfile1 = Gdx.files.local("bin/molegame.json");
	
	////////SAVE ARCADE SCORE/////////
	public static void save() {
		Json json = new Json();
		json.setOutputType(OutputType.json);
		hsfile1.writeString(Base64Coder.encodeString(json.toJson(gd)), false);
	}

	public static void load() {
		if(!hsfile1.exists()) {
			raz();
		}
		else{
			Json json = new Json();
			gd = json.fromJson(GameData.class, Base64Coder.decodeString(hsfile1.readString()));
			if(hsfileold.exists()){
				gd.uAgom=true;
				gd.skin1owned=true;
				hsfileold.delete();
			}
		}
		
	}
	
	public static void raz() {
		gd = new GameData();
		gd.init();
		save();
	}
	
	
	
	
}

