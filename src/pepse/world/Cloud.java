package pepse.world;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.util.ColorSupplier;
import pepse.world.trees.Drop;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class Cloud extends GameObject {
    private static final Color BASE_CLOUD_COLOR = new Color(255, 255, 255);
    private static final int CLOUD_SPEED = 50;
    private Random random = new Random();
    private PepseGameManager pepseGameManager;
    private List<GameObject> drops;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Cloud(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, PepseGameManager pepseGameManager) {
        super(topLeftCorner, dimensions, renderable);
        this.setVelocity(Vector2.RIGHT.mult(CLOUD_SPEED));
        this.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        this.setTag("cloud");
        this.pepseGameManager = pepseGameManager;
        this.drops = new ArrayList<>();
    }

    public void updateAvatarJump() {
        int dropNumber = random.nextInt(2, 6);
        for (int i = 0; i < dropNumber; i++) {
            ScheduledTask scheduledTask = new ScheduledTask(this, 0.2f * i, false, this::createDrop);

        }
    }

    private void createDrop() {
        int dropLocation = random.nextInt(5);
        GameObject drop = Drop.create(new Vector2(this.getCenter().x() + dropLocation * 3 + dropLocation,
                this.getCenter().y()));
        pepseGameManager.addGameObject(drop, Layer.BACKGROUND);
        drops.add(drop);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Iterator<GameObject> dropIterator = drops.iterator();
        while (dropIterator.hasNext()) {
            GameObject curDrop = dropIterator.next();
            if (curDrop.renderer().getOpaqueness() == 0) {
                pepseGameManager.removeGameObject(curDrop, Layer.BACKGROUND);
                dropIterator.remove();
            }
        }
    }
}

