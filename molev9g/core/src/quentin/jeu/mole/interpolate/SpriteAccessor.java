package quentin.jeu.mole.interpolate;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpriteAccessor implements TweenAccessor<Sprite> {
	
	public static final int ALPHA = 0, SKEW_X2X3 = 1, XY = 2, RGB = 3, SKEW_Y1X2X3 = 4, SKEW_Y2Y3A = 5, SKEW_Y2Y3B = 6;

	@Override
	public int getValues(Sprite target, int tweenType, float[] returnValues) {
		
		switch (tweenType) {
			case SKEW_X2X3:
				float[] vs0 = target.getVertices();
				returnValues[0] = vs0[SpriteBatch.X2] - target.getX();
				returnValues[1] = vs0[SpriteBatch.X3] - target.getX() - target.getWidth();
				return 2;
				
			case SKEW_Y2Y3A:
				float[] vs1 = target.getVertices();
				returnValues[0] = vs1[SpriteBatch.Y2] - target.getY()-target.getWidth()/2;
				returnValues[1] = vs1[SpriteBatch.Y3] - target.getY()+target.getWidth()/2;
				return 2;
				
			case SKEW_Y2Y3B:
				float[] vs11 = target.getVertices();
				returnValues[0] = vs11[SpriteBatch.Y2] - target.getY()+target.getWidth()/2;
				returnValues[1] = vs11[SpriteBatch.Y3] - target.getY()-target.getWidth()/2;
				return 2;
				
			case SKEW_Y1X2X3:
				float[] vs2 = target.getVertices();
				returnValues[0] = vs2[SpriteBatch.Y3] - target.getY() - target.getHeight();
				returnValues[1] = vs2[SpriteBatch.X2] - target.getX();
				returnValues[2] = vs2[SpriteBatch.X3] - target.getX() - target.getWidth();
				return 3;
			case RGB:
				returnValues[0] = target.getColor().r;
				returnValues[1] = target.getColor().g;
				returnValues[2] = target.getColor().b;
				return 3;
			case ALPHA:
				returnValues[0] = target.getColor().a;
				return 1;
			case XY:
				returnValues[0] = target.getX();
				returnValues[1] = target.getY();
				return 2;
		}
		assert false;
		return -1;
		}
	
	@Override
	public void setValues(Sprite target, int tweenType, float[] newValues) {
		switch (tweenType) {				
		case SKEW_X2X3:
			float x2 = target.getX();
			float x3 = x2 + target.getWidth();
			float[] vs = target.getVertices();
			vs[SpriteBatch.X2] = x2 + newValues[0];
			vs[SpriteBatch.X3] = x3 + newValues[1];
			break;
			
		case SKEW_Y2Y3A:
			float y2 = target.getY()+target.getWidth()/2;
			float y3 = target.getY()-target.getWidth()/2;
			float[] vs1 = target.getVertices();
			vs1[SpriteBatch.Y2] = y2 + newValues[0];
			vs1[SpriteBatch.Y3] = y3 + newValues[1];
			break;
			
		case SKEW_Y2Y3B:
			float y21 = target.getY()-target.getWidth()/2;
			float y31 = target.getY()+target.getWidth()/2;
			float[] vs11 = target.getVertices();
			vs11[SpriteBatch.Y2] = y21 + newValues[0];
			vs11[SpriteBatch.Y3] = y31 + newValues[1];
			break;
			
		case SKEW_Y1X2X3:
			float[] vrs = target.getVertices();
			float y11 = target.getY()+target.getHeight();
			float x22 = target.getX();
			float x33 = x22 + target.getWidth();
			vrs[SpriteBatch.Y3] = y11 + newValues[0];
			vrs[SpriteBatch.X2] = x22 + newValues[1];
			vrs[SpriteBatch.X3] = x33 + newValues[2];
			break;
		case RGB:
			target.setColor(newValues[0], newValues[1], newValues[2], target.getColor().a);
			break;
		case ALPHA:
			target.setColor(target.getColor().r, target.getColor().g, target.getColor().b, newValues[0]);
			break;
		case XY:
			target.setX(newValues[0]);
			target.setY(newValues[1]);
			break;
		}
	}
}


