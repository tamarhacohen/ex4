package pepse.world.trees;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.Random;

/**
 * this class is responsible to create drops game objects in the game
 */
public class Drop {
    private static final Color DROP_COLOR = new Color(48, 109, 199);
    private static final int DROP_SIZE = 10;
    private static final float GRAVITY = 200;

    /**
     * This function gets a cloud location and returns a drop game object
     * @param cloudLocation - location of the clouds the drop comes from
     * @return drop game object
     */
    public static GameObject create(Vector2 cloudLocation) {
        Renderable dropShape = new OvalRenderable(DROP_COLOR);

        GameObject drop = new GameObject(cloudLocation, Vector2.ONES.mult(DROP_SIZE),
                dropShape);
        drop.transform().setAccelerationY(GRAVITY);
        drop.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES); //TODO - needed?
        drop.setTag("drop");
        new Transition<Float>(drop,
                drop.renderer()::setOpaqueness, 1f, 0f,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                1.5f, Transition.TransitionType.TRANSITION_ONCE,
                null);
        return drop;
    }


}
