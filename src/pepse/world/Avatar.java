package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Avatar class - this class extends GameObject and describes the avatar in the game.
 */
public class Avatar extends GameObject {
    private ImageReader imageReader;
    private static final String[] IDLE_IMAGES = {"assets/idle_0.png", "assets/idle_1.png", "assets/idle_2.png", "assets" +
            "/idle_3.png"};
    private static final String[] RUN_IMAGES = {"assets/run_0.png", "assets/run_1.png", "assets/run_2.png",
            "assets/run_3.png", "assets/run_4.png", "assets/run_5.png"};
    private static final String[] JUMP_IMAGES = {"assets/jump_0.png", "assets/jump_1.png", "assets/jump_2.png",
            "assets/jump_3.png"};
    private AnimationRenderable idleAnimation;
    private AnimationRenderable runAnimation;
    private AnimationRenderable jumpAnimation;
    private State state;
    private UserInputListener inputListener;
    private static final float MOVEMENT_SPEED = 200;
    private static final float VELOCITY_X = 200;
    private static final float VELOCITY_Y = -300;
    private static final float GRAVITY = 200;
    private float energyCounter;
    private List<Cloud> observerClouds;
    ;

/**
 * Constructor for Avatar
 * @param topLeftCorner - location of the avatar
 * @param inputListener - input listener for the avatar
 * @param imageReader - image reader for avatar's photo
 */
public Avatar(Vector2 topLeftCorner,
                  UserInputListener inputListener,
                  ImageReader imageReader) {

        super(new Vector2(topLeftCorner.x(), topLeftCorner.y()-50) , Vector2.ONES.mult(50),
            imageReader.readImage(
                "assets" +
                        "/idle_0.png",
                true));
        this.imageReader = imageReader;
        initializeAnimations();
        this.state = State.IDLE;
        renderer().setRenderable(idleAnimation);
        this.inputListener = inputListener;
        this.energyCounter = 100;
        transform().setAccelerationY(GRAVITY);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        setTag("avatar");
        observerClouds = new ArrayList<>();

    }

    /**
     * registerCloudObserver method - registers a cloud as an observer of the avatar for rain dropping
     * @param cloud - observer to register
     */
    public void registerCloudObserver(Cloud cloud){
        observerClouds.add(cloud);
    }
    private void notifyAllClouds(){
        for (Cloud cloud: observerClouds){
            cloud.updateAvatarJump();
        }
    }

    private void initializeAnimations() {
        idleAnimation = createRenderable(IDLE_IMAGES);
        runAnimation = createRenderable(RUN_IMAGES);
        jumpAnimation = createRenderable(JUMP_IMAGES);
    }

    private AnimationRenderable createRenderable(String images[]){
        Renderable[] renderables = new Renderable[images.length];
        for (int i = 0; i < images.length; i++) {
            renderables[i] = imageReader.readImage(images[i], true);
        }
        return new AnimationRenderable(renderables, 0.2);
    }
    
    /**
     * State enum - this enum describes the state of the avatar
     */
    public State getState(){
        return state;
    }

    /**
     * update the avatar based on the user's input
     * @param deltaTime
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) && energyCounter >= 0.5){
            xVel -= VELOCITY_X;
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && energyCounter >= 0.5){
            xVel += VELOCITY_X;
        }
        if (xVel!=0)
        {
            energyCounter -= 0.5f;
            this.state = State.RUN;
        }
        transform().setVelocityX(xVel);
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0 && energyCounter >= 10) {
            energyCounter -= 10;
            this.state = State.JUMP;
            notifyAllClouds();
            transform().setVelocityY(VELOCITY_Y);
        }
        if (getVelocity().equals(Vector2.ZERO) && energyCounter < 100){
            addEnergy(1);
            this.state = State.IDLE;
        }
        resolveRenderable();

    }
    private void resolveRenderable(){
        Vector2 velocity = this.getVelocity();
        AnimationRenderable animationRenderable = idleAnimation;
        if (velocity.y() != 0){
            animationRenderable = jumpAnimation;
        }
        if (velocity.x() != 0){
            animationRenderable = runAnimation;
        }
        renderer().setRenderable(animationRenderable);
        renderer().setIsFlippedHorizontally(velocity.x() < 0);
    }

    /**
     * getEnergy method - returns the energy of the avatar
     * @return energy of the avatar
     */
    public float getEnergy() {
        return energyCounter;
    }

    /**
     * addEnergy method - adds energy to the avatar
     * @param energy - energy to add
     */
    public void addEnergy(float energy) {
        energyCounter = Math.min(100, energyCounter+energy);
    }


    /**
     * override on collision enter to collide only with wanted objects
     * @param other - object colided with
     * @param collision - collision
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if(other.getTag().equals("ground")){
            this.transform().setVelocityY(0);
        }
        if (other.getTag() == "fruit"){
            addEnergy(10);
        }
//
    }
}


