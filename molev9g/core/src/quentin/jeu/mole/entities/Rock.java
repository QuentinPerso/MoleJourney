package quentin.jeu.mole.entities;


import quentin.jeu.mole.utils.Assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;


public class Rock {
	
	public Sprite sprite;
	public Body body;
	public int type;
	
	public Rock(Body body,float[] color0, int type) {
		this.type=type;
		this.body=body;
		sprite = new Sprite(Assets.manager.get(Assets.rock1,Texture.class));
		sprite.setOriginCenter();
		sprite.setColor(color0[0],color0[1],color0[2],1);
		
		
	}
}