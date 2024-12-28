package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import java.util.List;

public class PepseGameManager  extends GameManager {
    private GameObject sky;
    private Vector2 windowDimensions;
    private GameObject night;
    private GameObject sun;
    private GameObject sunHalo;
    private Avatar avatar;
    private GameObject avatarEnergy;
    private TextRenderable energyRenderer;


    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowDimensions = windowController.getWindowDimensions();
        sky = Sky.create(windowDimensions);
        gameObjects().addGameObject(sky, Layer.BACKGROUND);
        Terrain terrain = new Terrain(windowDimensions, 0);
        List<Block> blockList= terrain.createInRange(0, (int) windowDimensions.x());
        for (Block block: blockList) {
            gameObjects().addGameObject(block, Layer.DEFAULT);
        }
        night = Night.create(windowDimensions, 30);
        gameObjects().addGameObject(night, Layer.STATIC_OBJECTS);
        sun = Sun.create(windowDimensions, 30);
        gameObjects().addGameObject(sun, Layer.BACKGROUND);
        sunHalo = SunHalo.create(sun);
        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);

        avatar = new Avatar(new Vector2(windowDimensions.x()/2,
                terrain.groundHeightAt(windowDimensions.x()/2)-100),inputListener, imageReader);
        gameObjects().addGameObject(avatar, Layer.DEFAULT);
        energyRenderer = new TextRenderable("Energy");
        avatarEnergy = new GameObject(Vector2.ONES.mult(10), Vector2.ONES.mult(20), energyRenderer);
        gameObjects().addGameObject(avatarEnergy, Layer.UI);

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        energyRenderer.setString(String.format("%d%%",(int)avatar.getEnergy())); // TODO change to
        // callback? (6.4)


    }
}

