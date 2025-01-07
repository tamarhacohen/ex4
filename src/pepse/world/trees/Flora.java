package pepse.world.trees;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

public class Flora {

    private static Random locationRandom;
    private static int minTreeHeight = 80;
    private static int maxTreeHeight = 150;
    private static int treeWidth = 50;
    private Function<Float, Float> floraGroundHeight;

    public Flora(Function<Float, Float> floraGroundHeight){
        this.floraGroundHeight = floraGroundHeight;
    }

    public List<Tree> createInRange(int minX, int maxX){
        float minTreeX = (float) Math.floor(minX/Block.getSize()) * Block.getSize();
        int treeNum = Math.ceilDiv((maxX - minX), Block.getSize());
        float curX = minTreeX - Block.getSize();

        List<Tree> trees = new ArrayList<>();
        for (int i = 0; i < treeNum; i++) {
            curX = curX + Block.getSize();
            locationRandom = new Random(Objects.hash(curX, 0));
            if (locationRandom.nextInt(0,10)<1){
                int treeHeight = locationRandom.nextInt(minTreeHeight, maxTreeHeight);
                Vector2 treeLocation = new Vector2(curX, floraGroundHeight.apply((float)curX)-treeHeight);
                trees.add(new Tree(treeLocation, new Vector2(treeWidth, treeHeight)));
            }

        }
        return trees;
    }

}
