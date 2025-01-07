package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Night class - this class creates game object night.
 */
public class Night {
    private static final Color BASIC_NIGHT_COLOR = Color.BLACK;

    /**
     * create a game object for the night object
     * @param windowDimensions - dims of the game window
     * @param cycleLength - length of the cycle of a day
     * @return night game object
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_NIGHT_COLOR));
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag("night");
        new Transition<Float>(night,
                night.renderer()::setOpaqueness, 0f, 0.5f,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                cycleLength/2, Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);

        return night;
    }
}
