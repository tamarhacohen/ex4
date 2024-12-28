package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Terrain;

import java.awt.*;

public class Sun {
    private static final int SIZE = 70;
    private static final Color BASIC_SUN_COLOR = Color.YELLOW;

    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        GameObject sun = new GameObject(windowDimensions.mult(0.5f), Vector2.ONES.mult(SIZE),
                new OvalRenderable(BASIC_SUN_COLOR));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag("sun");
        Vector2 initialSunCenter =sun.getCenter();
        Vector2 cycleCenter = new Vector2(windowDimensions.x()/2,
                new Terrain(windowDimensions, 0).groundHeightAt(windowDimensions.x()/2)); //TODO: new?
        new Transition<Float>(sun,
                (Float angle) -> sun.setCenter
                        (initialSunCenter.subtract(cycleCenter)
                                .rotated(angle)
                                .add(cycleCenter)),
                0f,
                360f,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null);
        return sun;
    }

}
