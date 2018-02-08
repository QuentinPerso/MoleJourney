package quentin.jeu.mole.entities;

import quentin.jeu.mole.utils.Assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;

public class Coin {
	
	public Sprite sprite;
	public Body body;
	
	public Coin(Body body) {
		this.body=body;
		sprite = new Sprite(Assets.manager.get(Assets.orbgold,Texture.class));
		
	}
}