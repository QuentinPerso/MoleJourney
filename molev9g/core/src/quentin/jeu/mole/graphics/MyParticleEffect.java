package quentin.jeu.mole.graphics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StreamUtils;


public class MyParticleEffect implements Disposable {
	private final Array<MyParticleEmitter> emitters;
	private BoundingBox bounds;
	private boolean ownsTexture;
	public MyParticleEffect () {
		emitters = new Array<MyParticleEmitter>(8);
	}

	public MyParticleEffect (MyParticleEffect effect) {
		emitters = new Array<MyParticleEmitter>(true, effect.emitters.size);
		for (int i = 0, n = effect.emitters.size; i < n; i++)
			emitters.add(new MyParticleEmitter(effect.emitters.get(i)));
	}
	
	public MyParticleEffect (MyParticleEffect effect, boolean tinted) {
		emitters = new Array<MyParticleEmitter>(true, effect.emitters.size);
		for (int i = 0, n = effect.emitters.size; i < n; i++)
			emitters.add(new MyParticleEmitter(effect.emitters.get(i), tinted));
	}

	public void start () {
		for (int i = 0, n = emitters.size; i < n; i++)
			emitters.get(i).start();
	}

	public void reset () {
		for (int i = 0, n = emitters.size; i < n; i++)
			emitters.get(i).reset();
	}

	public void update (float delta, float lowtransparency, float hitransparency, float lowvelocity, float hivelocity, boolean speedCtrl) {
		for (int i = 0, n = emitters.size; i < n; i++)
			emitters.get(i).update(delta, lowtransparency, hitransparency, lowvelocity, hivelocity, speedCtrl);
	}
	
	public void update (float delta) {
		for (int i = 0, n = emitters.size; i < n; i++)
			emitters.get(i).update(delta);
	}

	public void draw (Batch spriteBatch) {
		for (int i = 0, n = emitters.size; i < n; i++)
			emitters.get(i).draw(spriteBatch);
	}
	
	/**Draw on an external camera */
	public void drawext (Batch spriteBatch) {
		for (int i = 0, n = emitters.size; i < n; i++)
			emitters.get(i).drawext(spriteBatch);
	}

	public void draw (Batch spriteBatch, float delta) {
		for (int i = 0, n = emitters.size; i < n; i++)
			emitters.get(i).draw(spriteBatch, delta);
	}
	public void draw(SpriteBatch spriteBatch, float delta, float ratioxy) {
		for (int i = 0, n = emitters.size; i < n; i++)
			emitters.get(i).draw(spriteBatch, delta, ratioxy);
		
	}

	public void allowCompletion () {
		for (int i = 0, n = emitters.size; i < n; i++)
			emitters.get(i).allowCompletion();
	}

	public boolean isComplete () {
		for (int i = 0, n = emitters.size; i < n; i++) {
			MyParticleEmitter emitter = emitters.get(i);
			if (!emitter.isComplete()) return false;
		}
		return true;
	}

	public void setDuration (int duration) {
		for (int i = 0, n = emitters.size; i < n; i++) {
			MyParticleEmitter emitter = emitters.get(i);
			emitter.setContinuous(false);
			emitter.duration = duration;
			emitter.durationTimer = 0;
		}
	}

	public void setPosition (float x, float y) {
		for (int i = 0, n = emitters.size; i < n; i++)
			emitters.get(i).setPosition(x, y);
	}

	public void setFlip (boolean flipX, boolean flipY) {
		for (int i = 0, n = emitters.size; i < n; i++)
			emitters.get(i).setFlip(flipX, flipY);
	}

	public void flipY () {
		for (int i = 0, n = emitters.size; i < n; i++)
			emitters.get(i).flipY();
	}

	public Array<MyParticleEmitter> getEmitters () {
		return emitters;
	}

	/** Returns the emitter with the specified name, or null. */
	public MyParticleEmitter findEmitter (String name) {
		for (int i = 0, n = emitters.size; i < n; i++) {
			MyParticleEmitter emitter = emitters.get(i);
			if (emitter.getName().equals(name)) return emitter;
		}
		return null;
	}

	public void save (Writer output) throws IOException {
		int index = 0;
		for (int i = 0, n = emitters.size; i < n; i++) {
			MyParticleEmitter emitter = emitters.get(i);
			if (index++ > 0) output.write("\n\n");
			emitter.save(output);
			output.write("- Image Path -\n");
			output.write(emitter.getImagePath() + "\n");
		}
	}

	public void loadEmitters (FileHandle effectFile) {
		InputStream input = effectFile.read();
		emitters.clear();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(input), 512);
			while (true) {
				MyParticleEmitter emitter = new MyParticleEmitter(reader);
				reader.readLine();
				emitter.setImagePath(reader.readLine());
				emitters.add(emitter);
				if (reader.readLine() == null) break;
				if (reader.readLine() == null) break;
			}
		} catch (IOException ex) {
			throw new GdxRuntimeException("Error loading effect: " + effectFile, ex);
		} finally {
			StreamUtils.closeQuietly(reader);
		}
	}
	
	public void loadEmitters (FileHandle effectFile, boolean tinted) {
		InputStream input = effectFile.read();
		emitters.clear();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(input), 512);
			while (true) {
				MyParticleEmitter emitter = new MyParticleEmitter(reader, tinted);
				reader.readLine();
				emitter.setImagePath(reader.readLine());
				emitters.add(emitter);
				if (reader.readLine() == null) break;
				if (reader.readLine() == null) break;
			}
		} catch (IOException ex) {
			throw new GdxRuntimeException("Error loading effect: " + effectFile, ex);
		} finally {
			StreamUtils.closeQuietly(reader);
		}
	}



	public void loadEmitterImages (Texture tex) {
		for (int i = 0, n = emitters.size; i < n; i++) {
			MyParticleEmitter emitter = emitters.get(i);
			emitter.setSprite(new Sprite(tex));
		}
	}


	/** Disposes the texture for each sprite for each ParticleEmitter. */
	public void dispose () {
		if (!ownsTexture) return;
		for (int i = 0, n = emitters.size; i < n; i++) {
			MyParticleEmitter emitter = emitters.get(i);
			emitter.getSprite().getTexture().dispose();
		}
	}

	/** Returns the bounding box for all active particles. z axis will always be zero. */
	public BoundingBox getBoundingBox () {
		if (bounds == null) bounds = new BoundingBox();

		BoundingBox bounds = this.bounds;
		bounds.inf();
		for (MyParticleEmitter emitter : this.emitters)
			bounds.ext(emitter.getBoundingBox());
		return bounds;
	}

	public void scaleEffect(MyParticleEffect p,int emitternumber, float scale){
		p.getEmitters().get(emitternumber).getScale().setHigh(p.getEmitters().get(emitternumber).getScale().getHighMax()*scale);
		p.getEmitters().get(emitternumber).getScale().setLow(p.getEmitters().get(emitternumber).getScale().getLowMax()*scale);
		p.getEmitters().get(emitternumber).getVelocity().setHighMax(p.getEmitters().get(emitternumber).getVelocity().getHighMax()*scale);
		p.getEmitters().get(emitternumber).getVelocity().setHighMin(p.getEmitters().get(emitternumber).getVelocity().getHighMin()*scale);
		p.getEmitters().get(emitternumber).getVelocity().setLow(p.getEmitters().get(emitternumber).getVelocity().getLowMax()*scale);
		//p.getEmitters().get(emitternumber).setMinParticleCount((int) (p.getEmitters().get(emitternumber).getMinParticleCount()*scale));
		p.getEmitters().get(emitternumber).getXOffsetValue().setLowMax(p.getEmitters().get(emitternumber).getXOffsetValue().getLowMax()*scale);
		p.getEmitters().get(emitternumber).getXOffsetValue().setLowMin(p.getEmitters().get(emitternumber).getXOffsetValue().getLowMin()*scale);
		p.getEmitters().get(emitternumber).getYOffsetValue().setLowMax(p.getEmitters().get(emitternumber).getYOffsetValue().getLowMax()*scale);
		p.getEmitters().get(emitternumber).getYOffsetValue().setLowMin(p.getEmitters().get(emitternumber).getYOffsetValue().getLowMin()*scale);
		p.getEmitters().get(emitternumber).getGravity().setHighMax(p.getEmitters().get(emitternumber).getGravity().getHighMax()*scale);
		p.getEmitters().get(emitternumber).getGravity().setHighMin(p.getEmitters().get(emitternumber).getGravity().getHighMin()*scale);
		p.getEmitters().get(emitternumber).getGravity().setLow(p.getEmitters().get(emitternumber).getGravity().getLowMax()*scale);
	}
	
	public void scaleEffectAbs(MyParticleEffect p,int emitternumber, float scaleM,float scalem,float velM,float velm,float velLow,
			int pcountm,float offXM,float offXm,float offYM,float offYm,float gravM,float gravm,float gravLow){
		p.getEmitters().get(emitternumber).getScale().setHigh(scaleM);
		p.getEmitters().get(emitternumber).getScale().setLow(scalem);
		p.getEmitters().get(emitternumber).getVelocity().setHighMax(velM);
		p.getEmitters().get(emitternumber).getVelocity().setHighMin(velm);
		p.getEmitters().get(emitternumber).getVelocity().setLow(velLow);
		p.getEmitters().get(emitternumber).setMinParticleCount(pcountm);
		p.getEmitters().get(emitternumber).getXOffsetValue().setLowMax(offXm);
		p.getEmitters().get(emitternumber).getXOffsetValue().setLowMin(offXM);
		p.getEmitters().get(emitternumber).getYOffsetValue().setLowMax(offYm);
		p.getEmitters().get(emitternumber).getYOffsetValue().setLowMin(offYM);
		p.getEmitters().get(emitternumber).getGravity().setHighMax(gravM);
		p.getEmitters().get(emitternumber).getGravity().setHighMin(gravm);
		p.getEmitters().get(emitternumber).getGravity().setLow(gravLow);
	}
	
	public void ScaleParticles(MyParticleEffect p,int emitternumber, float scale){
		p.getEmitters().get(emitternumber).getScale().setHigh(p.getEmitters().get(emitternumber).getScale().getHighMax()*scale);
		p.getEmitters().get(emitternumber).getScale().setLow(p.getEmitters().get(emitternumber).getScale().getLowMax()*scale);
	}

	

}
