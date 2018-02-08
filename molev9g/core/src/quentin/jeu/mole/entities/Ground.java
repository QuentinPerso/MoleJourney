package quentin.jeu.mole.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Ground {
	
	public Body body;
	
	public Ground(World world, String name, Vector2 position, Vector2... shapes){
		BodyDef bodyDef = new BodyDef();
	        //body definition
	        bodyDef.position.set(position);  
	        bodyDef.type = BodyType.StaticBody;
	        
	        //ground shape
	        ChainShape ground = new ChainShape();  
	        ground.createChain(shapes);
	        body = world.createBody(bodyDef);
	       
	        FixtureDef grdfdef = new FixtureDef();
	        grdfdef.shape=ground;
	        grdfdef.restitution=0.25f;
	        body.createFixture(grdfdef).setUserData(name);
	       
	        ground.dispose();
	}
	
}
	