/*package entities;

import utils.Assets;
import animation.B2DSprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

public class Ufo extends B2DSprite {
	
	private static Array<Ufo> ufos;
	
	public Ufo(Body body) {
		
		super(body);
		
		Sprite sprite = new Sprite(Assets.manager.get(Assets.ufo,Texture.class));
		animation.setFrames( 1 / 10f, sprite);
		width = sprite.getRegionWidth();
		height = sprite.getRegionHeight();
		
	}
	public static Array<Ufo> createufo(World world){
		ufos = new Array<Ufo>();
		int ufonbr = 1;
		for(int i =0;i<ufonbr;i++){
		BodyDef ufodef = new BodyDef();
		ufodef.type = BodyType.DynamicBody;
		float x = MathUtils.random(-1000,-200); ;
		float y = MathUtils.random(140,200);
		ufodef.position.set(x, y);
		
		Body body = world.createBody(ufodef);
		body.setGravityScale(0);
		FixtureDef ufofdef = new FixtureDef();
		CircleShape cshape = new CircleShape();
		cshape.setRadius(2f);
		ufofdef.shape = cshape;
		ufofdef.density = 5;
		body.createFixture(ufofdef).setUserData("ufo");
		Ufo ufo = new Ufo(body);
		body.setUserData(ufo);
		ufos.add(ufo);
		
		body.applyLinearImpulse(MathUtils.random(-1000,1000), MathUtils.random(-25,100),body.getPosition().x, body.getPosition().y, true);
		body.setAngularDamping(MathUtils.random(-2,2));
		
		cshape.dispose();	
		}
		return ufos;
	}
	
	public static void updateUFOs(){
		 for(int i = 0; i < ufos.size; i++) {
				Body body = ufos.get(i).getBody();
				if(body.getPosition().x>200)
					body.setTransform(-200, MathUtils.random(400,1000), body.getAngle());
				else if(body.getPosition().x< -200)
					body.setTransform(200, MathUtils.random(400,1000), body.getAngle());
				if(body.getLinearVelocity().x<10 && body.getLinearVelocity().x>-10) 
					body.applyLinearImpulse(MathUtils.random(-1000,1000), MathUtils.random(-25,100),body.getPosition().x, body.getPosition().y, true);
			 }
	}
}

*/