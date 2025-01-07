package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * this class creates a game object of the sun halo
 */
public class SunHalo {
    private static Color SUN_HALO_COLOR = new Color(255, 255, 0, 40);

    /**
     *
     * @param sun - the sun game object to create a halo for
     * @return sun halo game object
     */
    public static GameObject create(GameObject sun){
        GameObject sunHalo = new GameObject(sun.getTopLeftCorner(), sun.getDimensions().mult(1.5f),
                new OvalRenderable(SUN_HALO_COLOR));
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag("sunHalo");
        return sunHalo;
    }

}
