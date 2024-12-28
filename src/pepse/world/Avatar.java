package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

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

    private enum State {
        IDLE,
        RUN,
        JUMP}
    private State state;
    private UserInputListener inputListener;
    private static final float MOVEMENT_SPEED = 200;
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    private static final float GRAVITY = 600;
    private float energyCounter;
    ;

    public Avatar(Vector2 topLeftCorner,
                  UserInputListener inputListener,
                  ImageReader imageReader) {

        super(topLeftCorner, Vector2.ONES.mult(50), imageReader.readImage("assets/idle_0.png",
                true));
        this.imageReader = imageReader;
        initializeAnimations();
        this.state = State.IDLE;
        renderer().setRenderable(idleAnimation);
        this.inputListener = inputListener;
        this.energyCounter = 100;
        transform().setAccelerationY(GRAVITY);
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
            transform().setVelocityY(VELOCITY_Y);
            transform().setAccelerationY(GRAVITY);
        }
        if (getVelocity().equals(Vector2.ZERO) && energyCounter < 100){
            energyCounter += 1;
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

    public float getEnergy() {
        return energyCounter;
    }

    public void addEnergy(float energy) {
        energyCounter = Math.min(100, energyCounter+energy);
    }


    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if(other.getTag().equals("ground")){
            this.transform().setVelocityY(0);
            this.transform().setAccelerationY(0);
        }
    }
}


