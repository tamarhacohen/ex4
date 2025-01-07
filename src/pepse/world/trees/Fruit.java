package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;

import java.awt.*;

/**
 * this class represents a fruit game object, extends GameObject
 */
public class Fruit extends GameObject {
    private static final int fruitSize = 15;
    private static final Color fruitColor = new Color(200, 30, 78);
    private PepseGameManager pepseGameManager;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Fruit(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        setTag("fruit");
    }

    /**
     *
     * @return the size of the fruit
     */
    public static int getFruitSize() {
            return fruitSize;
    }

    /**
     *
     * @return the color of the fruit
     */
    public static Color getFruitColor() {
        return fruitColor;
    }

    /**
     * sets the pepse game manager field
     * @param pepseGameManager
     */
    public void setPepseGameManager(PepseGameManager pepseGameManager) {
        this.pepseGameManager = pepseGameManager;
    }
    private void AppearFruit(){
        pepseGameManager.addGameObject(this, Layer.DEFAULT);
    }

    /**
     * override the on collision enter function to collide with avatar only
     * @param other - object collided with
     * @param collision - the collision itself
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag() == "avatar"){
            pepseGameManager.removeGameObject(this, Layer.DEFAULT);
            ScheduledTask scheduledTask = new ScheduledTask(other, 30, false,
                    this::AppearFruit);


        }
    }
}
