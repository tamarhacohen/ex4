package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import pepse.world.trees.Fruit;
import pepse.world.trees.Tree;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * responsible for managing a pepse game
 */
public class PepseGameManager  extends GameManager {
    private static final int CLOUD_SIZE = 80;
    private static final int BRICK_BUFFER = 10;
    private GameObject sky;
    private Vector2 windowDimensions;
    private GameObject night;
    private GameObject sun;
    private GameObject sunHalo;
    private Avatar avatar;
    private GameObject avatarEnergy;
    private TextRenderable energyRenderer;
    private int leafLayer = Layer.DEFAULT + 1;
    private GameObject cloud;
    private Random random = new Random();
    private Terrain terrain;
    private float xRight;
    private float xLeft;
    private Flora flora;

    /**
     * runs a game of pepse
     * @param args game arguments
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    /**
     * initializes game
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowController.setTargetFramerate(40);
        windowDimensions = windowController.getWindowDimensions();
        xLeft = 0 - BRICK_BUFFER;
        xRight = windowDimensions.x()+BRICK_BUFFER;
        sky = Sky.create(windowDimensions);
        gameObjects().addGameObject(sky, Layer.BACKGROUND);
        terrain = new Terrain(windowDimensions, 0);
        HashMap<Integer, List<Block>> blockMap = terrain.createInRange((int)xLeft, (int)xRight);
        for (HashMap.Entry<Integer,List<Block>> entry : blockMap.entrySet()){
            for (Block block:entry.getValue()){
                gameObjects().addGameObject(block, entry.getKey());
            }
        }

        night = Night.create(windowDimensions, 30);
        gameObjects().addGameObject(night, Layer.STATIC_OBJECTS);
        sun = Sun.create(windowDimensions, 30);
        gameObjects().addGameObject(sun, Layer.BACKGROUND);
        sunHalo = SunHalo.create(sun);
        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);

        Vector2 avatarLocation =new Vector2(windowDimensions.x()/2,
                terrain.groundHeightAt(windowDimensions.x()/2));
        avatar = new Avatar(avatarLocation,inputListener, imageReader);
        gameObjects().addGameObject(avatar, Layer.DEFAULT);
        Vector2 cameraVector =
                new Vector2(windowController.getWindowDimensions().mult(0.5f));
        setCamera(new Camera(avatar,
                cameraVector.subtract(avatarLocation),
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));


        energyRenderer = new TextRenderable("Energy");
        avatarEnergy = new GameObject(Vector2.ONES.mult(10), Vector2.ONES.mult(20), energyRenderer);
        avatarEnergy.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(avatarEnergy, Layer.UI);
        flora = new Flora(terrain::groundHeightAt);
        createTrees(0, (int)windowDimensions.x());
        Renderable cloudImage = imageReader.readImage("assets/cloud.jpg", true);
        Cloud cloud = new Cloud(new Vector2((float)0-CLOUD_SIZE,(float)0+50),Vector2.ONES.mult(50),
                cloudImage, this);
        avatar.registerCloudObserver(cloud);
        gameObjects().addGameObject(cloud, Layer.BACKGROUND);

    }

    private void createTrees(int left, int right) {
        List<Tree> trees = flora.createInRange(left,right);
        for (Tree tree : trees){
            gameObjects().addGameObject(tree.getStem(), Layer.DEFAULT);
            List<GameObject> leaves = tree.getLeaves();
            for (GameObject leaf: leaves){
                gameObjects().addGameObject(leaf, leafLayer);
            }
            List<Fruit> fruits = tree.getFruits();
            for (Fruit fruit: fruits){
                fruit.setPepseGameManager(this);
                gameObjects().addGameObject(fruit, Layer.DEFAULT);

            }
        }
    }

    /**
     * removes a game object from the game
     * @param gameObject object to remove
     * @param layer layer to remove from
     */
    public void removeGameObject(GameObject gameObject, int layer){
        gameObjects().removeGameObject(gameObject, layer);
    }

    /**
     * adds a game object to the game
     * @param gameObject object to add
     * @param layer layer to add to
     */
    public void addGameObject(GameObject gameObject, int layer){
        gameObjects().addGameObject(gameObject, layer);
    }

    /**
     * updates the game's frame
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        energyRenderer.setString(String.format("%d%%",(int)avatar.getEnergy())); // TODO change to
        // callback? (6.4)
        float curLeft = avatar.getCenter().x() - windowDimensions.x()/2;
        if (curLeft<xLeft)
        {
            HashMap<Integer, List<Block>> blockMap = terrain.createInRange((int)curLeft-15, (int)xLeft);
            for (HashMap.Entry<Integer,List<Block>> entry : blockMap.entrySet()){
                for (Block block:entry.getValue()){
                    gameObjects().addGameObject(block, entry.getKey());
                }
            }
            createTrees((int)curLeft-15, (int)xLeft);
            xLeft = curLeft - BRICK_BUFFER;
        }
        float curRight = avatar.getCenter().x() + windowDimensions.x()/2;
        if (curRight>xRight)
        {
            HashMap<Integer, List<Block>> blockMap = terrain.createInRange((int)xRight, (int)curRight+15);
            for (HashMap.Entry<Integer,List<Block>> entry : blockMap.entrySet()){
                for (Block block:entry.getValue()){
                    gameObjects().addGameObject(block, entry.getKey());
                }
            }
            createTrees((int)xRight, (int)curRight+15);
            xRight = curRight + BRICK_BUFFER;
        }


    }
}

