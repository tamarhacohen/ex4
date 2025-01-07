package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * this class is responsible of creating a tree object in the game
 */
public class Tree{
    private GameObject stem;
    private List<GameObject> leaves;
    private List<Fruit> fruits;
    private static final Color treeColor = new Color(100, 50, 20);
    private Random random;

/**
 * constructor for the tree object
 * @param topLeftCorner - location of the tree
 * @param dimensions - size of the tree
 */
public Tree(Vector2 topLeftCorner, Vector2 dimensions) {
        stem = new GameObject(topLeftCorner, dimensions,  new RectangleRenderable(treeColor));
        stem.setTag("stem");
        stem.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        stem.physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        random =  new Random(Objects.hash(topLeftCorner.x(), 0));
        generateLeaves();
    }

    private void generateLeaves(){
        leaves = new ArrayList<>();
        fruits = new ArrayList<>();

        for (float i = stem.getCenter().x()-3*Leaf.getLeafSize() ; i < stem.getCenter().x()+3*Leaf.getLeafSize(); i+=Leaf.getLeafSize()) {
            for (float j = stem.getCenter().y()-4*Leaf.getLeafSize(); j < stem.getCenter().y()+Leaf.getLeafSize(); j+=Leaf.getLeafSize()) {
                float randFloat = random.nextFloat(0,1);
                if (randFloat<0.6){
                    Vector2 leafLocation = new Vector2(i, j);
                    Leaf leaf = new Leaf(leafLocation, Vector2.ONES.mult(Leaf.getLeafSize()),
                            new RectangleRenderable(Leaf.getLeafColor()));
                    leaves.add(leaf);

                    ScheduledTask scheduledTask = new ScheduledTask(leaf, random.nextFloat(0.3f, 2f), false,
                            leaf::setTransition);
                }
                else if(randFloat < 0.75){
                    Vector2 fruitLocation = new Vector2(i, j);
                    Fruit fruit = new Fruit(fruitLocation, Vector2.ONES.mult(Fruit.getFruitSize()),
                            new OvalRenderable(Fruit.getFruitColor()));
                    fruits.add(fruit);
                }
            }
        }
    }

    /**
     * get the stem of the tree
     * @return - game object of the stem
     */
    public GameObject getStem() {
        return stem;
    }

    /**
     * get a list of leaves game objects
     * @return - list of leaf game objects
     */
    public List<GameObject> getLeaves(){
        return leaves;
    }

    /**
     * get a list of fruits game objects
     * @return - list of fruit game objects
     */
    public List<Fruit> getFruits() {
        return fruits;
    }
}
