package quentin.jeu.mole.desktop;

import quentin.jeu.mole.MoleGame;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "A Mole's Journey";
		//cfg.useGL20 = true;
		cfg.width  = (int) (960);
		cfg.height = (int) (540);
		cfg.addIcon("ui/other/icon.png",FileType.Internal);
		
		new LwjglApplication(new MoleGame(new DeskShop(), new DeskGoogleServices()), cfg);
	}
}
