package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;

import java.awt.*;

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

    public static int getFruitSize() {
        return fruitSize;
    }

    public static Color getFruitColor() {
        return fruitColor;
    }

    public void setPepseGameManager(PepseGameManager pepseGameManager) {
        this.pepseGameManager = pepseGameManager;
    }
    private void AppearFruit(){
        pepseGameManager.addGameObject(this, Layer.DEFAULT);
    }
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
