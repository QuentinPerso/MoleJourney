package quentin.jeu.mole.utils;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

public class MyContactlistener implements ContactListener, ContactFilter{

	public Array<Body> bonus2remove, coins2remove;
	public boolean  molegrdtouch = false,moleUgrdtouch = false,moleUgrdtouch1 = false, moleUgrdtouch2 = false, moleUgrdtouch3;
	public boolean  canhardearth=false, canrock = false, canHrock = false, canlava = false;
	public boolean  pipe=false;
	public Sound scoin;
	

	public int lvl;
	
	public MyContactlistener(int lvl){
		
		this.lvl=lvl;
		scoin=Assets.manager.get(Assets.scoin,Sound.class);
		bonus2remove  = new Array<Body>();
		coins2remove  = new Array<Body>();
		
	}
	
	//Contact Listener
	@Override
	public void beginContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
		if(fa == null || fb == null) return; 
		if(fa.getUserData() == null || fb.getUserData() == null) return; 
		
		// mole body 
		if( fa.getUserData().equals("ground") &&  fb.getUserData().equals("mole") ) {
			molegrdtouch = true;
		}
		if( fb.getUserData().equals("ground") &&  fa.getUserData().equals("mole") ) {
			molegrdtouch = true;
		}
		if( fa.getUserData().equals("hardearth") &&  fb.getUserData().equals("mole") ) {
			if(!canhardearth) moleUgrdtouch = true;
		}
		if( fb.getUserData().equals("hardearth") &&  fa.getUserData().equals("mole") ) {
			if(!canhardearth) moleUgrdtouch = true;
		}
		if( fa.getUserData().equals("underground") &&  fb.getUserData().equals("mole") ) {
			if(!canrock) moleUgrdtouch = true;
		}
		if( fb.getUserData().equals("underground") &&  fa.getUserData().equals("mole") ) {
			if(!canrock) moleUgrdtouch = true;
		}
		if( fa.getUserData().equals("underground1") &&  fb.getUserData().equals("mole") ) {
			if( !canHrock) moleUgrdtouch1 = true;
		}
		if( fb.getUserData().equals("underground1") &&  fa.getUserData().equals("mole") ) {
			if( !canHrock) moleUgrdtouch1 = true;
		}
		if( fa.getUserData().equals("underground2") &&  fb.getUserData().equals("mole") ) {
			if( !canlava) moleUgrdtouch2 = true;
		}
		if( fb.getUserData().equals("underground2") &&  fa.getUserData().equals("mole") ) {
			if( !canlava) moleUgrdtouch2 = true;
		}
		if( fa.getUserData().equals("underground3") &&  fb.getUserData().equals("mole") ) {
			 moleUgrdtouch3 = true;
		}
		if( fb.getUserData().equals("underground3") &&  fa.getUserData().equals("mole") ) {
			 moleUgrdtouch3 = true;
		}
		if(fa.getUserData().equals("bonus")  && fb.getUserData().equals("mole") ) {
			if(Save.gd.sound)scoin.play();
			bonus2remove.add(fa.getBody());
		}
		if(fb.getUserData().equals("bonus") && fa.getUserData().equals("mole") ) {
			if(Save.gd.sound)scoin.play();
			bonus2remove.add(fb.getBody());
		}
		if(fa.getUserData().equals("coin")  && fb.getUserData().equals("mole") ) {
			if(Save.gd.sound)scoin.play();
			coins2remove.add(fa.getBody());
		}
		if(fb.getUserData().equals("coin") && fa.getUserData().equals("mole") ) {
			if(Save.gd.sound)scoin.play();
			coins2remove.add(fb.getBody());
		}
		/*if(fa.getUserData().equals("pipe")  && fb.getUserData().equals("mole") ) {
			pipe=true;
			
		}
		if(fb.getUserData().equals("pipe") && fa.getUserData().equals("mole") ) {
			pipe=true;
		}*/
		
	}

	@Override
	public void endContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
		if(fa == null || fb == null) return; 
		if(fa.getUserData() == null || fb.getUserData() == null) return; 
		
		if( fa.getUserData().equals("ground") &&  fb.getUserData().equals("mole") ) {
			molegrdtouch =false;
		}
		if( fb.getUserData().equals("ground") &&  fa.getUserData().equals("mole") ) {
			molegrdtouch =false;
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		
		if(fa == null || fb == null) return; 
		if(fa.getUserData() == null || fb.getUserData() == null) return; 
	

		if( fa.getUserData().equals("ground") &&  fb.getUserData().equals("mole") ) {
			contact.setEnabled(false);
		}
		if( fb.getUserData().equals("ground") &&  fa.getUserData().equals("mole") ) {
			contact.setEnabled(false);
		}
		if( fa.getUserData().equals("hardearth") &&  fb.getUserData().equals("mole") ) {
			if(canhardearth) contact.setEnabled(false);
		}
		if( fb.getUserData().equals("hardearth") &&  fa.getUserData().equals("mole") ) {
			if(canhardearth) contact.setEnabled(false);
		}
		if( fa.getUserData().equals("underground") &&  fb.getUserData().equals("mole") ) {
			if(canrock) contact.setEnabled(false);
		}
		if( fb.getUserData().equals("underground") &&  fa.getUserData().equals("mole") ) {
			if(canrock) contact.setEnabled(false);
		}
		if( fa.getUserData().equals("underground1") &&  fb.getUserData().equals("mole") ) {
			if(canHrock) contact.setEnabled(false);
		}
		if( fb.getUserData().equals("underground1") &&  fa.getUserData().equals("mole") ) {
			if(canHrock) contact.setEnabled(false);
		}
		if( fa.getUserData().equals("underground2") &&  fb.getUserData().equals("mole") ) {
			if(canlava) contact.setEnabled(false);
		}
		if( fb.getUserData().equals("underground2") &&  fa.getUserData().equals("mole") ) {
			if(canlava) contact.setEnabled(false);
		}
	}

	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}
	
	@Override
	public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
		return true;
		
	}
}


