package pepse.world.trees;

import danogl.GameObject;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * this class represents a Leaf game object, extends GameObject
 */
public class Leaf extends GameObject {
    private static final float leafSize = 20;
    private static final Color leafColor = new Color(50, 200, 30);


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Leaf(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }

    void setTransition(){
        setAngleTransition();
        setSizeTransition();
    }
    void setAngleTransition(){
        new Transition<Float>(this,
                (Float angle) -> this.renderer().setRenderableAngle(angle),
                -15f,
                15f,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                3,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
    }

    void setSizeTransition(){
        new Transition<Float>(this,
                (Float size) -> this.setDimensions(Vector2.ONES.mult(size)),
                leafSize,
                leafSize*1.2f,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                3,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
    }

    /**
     * getter for size of the leaf
     * @return size of the leaf
     */
    public static float getLeafSize() {
            return leafSize;
        }

    /**
     * getter for color of the leaf
     * @return color of the leaf
     */
    public static Color getLeafColor() {
        return leafColor;
    }
}
