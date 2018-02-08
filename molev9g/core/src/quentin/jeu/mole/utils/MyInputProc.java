package quentin.jeu.mole.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;

public class MyInputProc extends InputAdapter {
	
	public boolean leftK=false;
	public boolean rightK=false;
	public boolean upK=false;
	public boolean downK=false;
	public boolean backK=false;
	
	//Input Adapter
		@Override
		public boolean keyDown(int keycode) 
		{
			switch(keycode) {
			case Keys.RIGHT:
				rightK=true;
				break;
			case Keys.LEFT:
				leftK=true;
				break;
			case Keys.UP:
				upK=true;
				break;
			case Keys.DOWN:
				downK=true;
				break;
			case Keys.BACK:
				backK=true;
			
			}
			return true;
			
		}
			
		@Override
		public boolean keyUp(int keycode) 
		{
			switch(keycode) {
			case Keys.RIGHT:
				rightK=false;
				break;
			case Keys.LEFT:
				leftK=false;
				break;
			case Keys.UP:
				upK=false;
				break;
			case Keys.DOWN:
				downK=false;
				break;
			case Keys.BACK:
				backK=false;
			
			}
			return true;
		}

		public boolean touchDown(int x, int y, int pointer, int button) {
			if(x < Gdx.graphics.getWidth() / 2 &&y>Gdx.graphics.getHeight()/6) {//left
				leftK=true;
			}
			if(x > Gdx.graphics.getWidth() / 2 &&y>Gdx.graphics.getHeight()/6) {//right
				rightK=true;
			}
			return true;
		}
		
		public boolean touchUp(int x, int y, int pointer, int button) {
			if(x < Gdx.graphics.getWidth() / 2 &&y>Gdx.graphics.getHeight()/6) {//left
				leftK=false;
			}
			if(x > Gdx.graphics.getWidth() / 2 &&y>Gdx.graphics.getHeight()/6) {//right
				rightK=false;
			}
			return true;
		}

}
