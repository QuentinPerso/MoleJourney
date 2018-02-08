package quentin.jeu.mole.entities;


import quentin.jeu.mole.utils.Assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;


public class Bonus {
	
	public Sprite sprite;
	public Body body;
	public int type;
	
	public Bonus(Body body, int type) {
		this.type=type;
		this.body=body;
		if(type==1) sprite = new Sprite(Assets.manager.get(Assets.orbgold,Texture.class));
		else if(type==2) sprite = new Sprite(Assets.manager.get(Assets.orbsilver,Texture.class));
		else if(type==3) sprite = new Sprite(Assets.manager.get(Assets.orbbronze,Texture.class));
		else if(type==4) sprite = new Sprite(Assets.manager.get(Assets.time,Texture.class));
		else if(type==5) sprite = new Sprite(Assets.manager.get(Assets.plane,Texture.class));
		else if(type==6) sprite = new Sprite(Assets.manager.get(Assets.jetpack,Texture.class));
		else sprite = new Sprite(Assets.manager.get(Assets.bronze,Texture.class));
		
	}
}