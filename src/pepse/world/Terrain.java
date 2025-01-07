package pepse.world;

import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Terrain {
    private float groundHeightAtX0;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;
    private NoiseGenerator noiseGenerator;



    public Terrain(Vector2 windowDimensions, int seed){
        groundHeightAtX0 = windowDimensions.y()*2/3;
        noiseGenerator = new NoiseGenerator(0, (int)groundHeightAtX0);
    }

//    public float groundHeightAt(float x) {
//        float yAtX = (float) Math.floor(groundHeightAtX0/Block.SIZE) * Block.SIZE;
//
//        return yAtX;
//    }

    public float groundHeightAt(float x) {
        float noise = (float) noiseGenerator.noise(x, Block.getSize() *7);
        return groundHeightAtX0 + noise;
    }


    public HashMap<Integer, List<Block>> createInRange(int minX, int maxX) {
        // TODO FIX BLOCKS


        HashMap<Integer, List<Block>> mapBlocks = new HashMap<>();
        float minBlockX = (float) Math.floor(minX/Block.getSize()) * Block.getSize();
        int blocksNum = Math.ceilDiv((maxX - minX), Block.getSize());
        float curX = minBlockX - Block.getSize();
        for (int i = 0; i < blocksNum; i++) {
            curX = curX + Block.getSize();
            float curBlockY = groundHeightAt(curX) - Block.getSize();
            for (int j = 0; j < TERRAIN_DEPTH; j++) {
                curBlockY = curBlockY + Block.getSize();
                RectangleRenderable rectangleRender =
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
                Block block = new Block(new Vector2(curX, curBlockY), rectangleRender);
                block.setTag("ground");
                mapBlocks.computeIfAbsent(Layer.DEFAULT+j, k -> new ArrayList<>()).add(block);
            }
        }
        return mapBlocks;
    }


}
