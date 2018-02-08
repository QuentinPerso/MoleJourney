package quentin.jeu.mole.screens;






import quentin.jeu.mole.ui.MapUI;
import quentin.jeu.mole.utils.Assets;
import quentin.jeu.mole.utils.Save;
import quentin.jeu.mole.utils.TimeCalc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WorldMap implements Screen {
	
	static float cameraWidth = 16, cameraHeight = 16, cameraZoom = 0.4f, cameraZoomSpeed = 0.5f;
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	public MapUI ui;

	private Sprite back;

	public boolean leftPressed;

	@Override
	public void show() {
		Gdx.input.setCatchBackKey(false);
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		back =   new Sprite(Assets.manager.get(Assets.ground0,   Texture.class));
		back.setColor(0.9f, 0.6f, 0.3f,1);
		ui = new MapUI();
		Gdx.input.setInputProcessor(new InputMultiplexer(ui.stage, ui.starstage));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for(int i=0;i<=4;i++){
			for(int j=0;j<=4;j++){
				back.setPosition(-Gdx.graphics.getWidth()/2+i*back.getWidth(),-Gdx.graphics.getHeight()/2+j*back.getHeight());
				if(ui.black.getColor().a<0.98f)back.draw(batch);
			}
		}
		batch.end();
		
		ui.render(delta);
	}
	
	public void resize (int width, int height) {
		//ui.resize(width, height);
	}
	
	@Override
	public void hide() {
		//ui.music.stop();
		dispose();
	}

	@Override
	public void pause() {
		ui.timecalc.dispose();
	}

	@Override
	public void resume() {
		ui.timecalc=new TimeCalc();
		while(!Assets.manager.update());
	}

	@Override
	public void dispose() {
		batch.dispose();
		ui.dispose();
		Save.save();
	}
}
