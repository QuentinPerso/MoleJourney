package quentin.jeu.mole.interpolate;



/**
 * Collection of built-in paths.
 *
 * @author Aurelien Ribon | http://www.aurelienribon.com/
 */
public interface TweenPaths {
	public static final LinearPath linear = new LinearPath();
	public static final CatmullRom catmullRom = new CatmullRom();
}
