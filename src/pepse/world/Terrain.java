package pepse.world;

import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Terrain {
    private float groundHeightAtX0;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;



    public Terrain(Vector2 windowDimensions, int seed){
        groundHeightAtX0 = windowDimensions.y()*2/3;
    }

    public float groundHeightAt(float x) {
        return groundHeightAtX0;
    }
    public List<Block> createInRange(int minX, int maxX) {
        // TODO FIX BLOCKS
        RectangleRenderable rectangleRender = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
        List<Block> listBlocks = new ArrayList<>();
        float minBlockX = (float) Math.floor(minX/Block.SIZE) * Block.SIZE;
        int blocksNum = Math.ceilDiv((maxX - minX), Block.SIZE);
        float curX = minBlockX - Block.SIZE;
        for (int i = 0; i < blocksNum; i++) {
            curX = curX + Block.SIZE;
            float curBlockY = (float) Math.floor(groundHeightAt(curX)/Block.SIZE) * Block.SIZE;
            for (int j = 0; j < TERRAIN_DEPTH; j++) {
                curBlockY = curBlockY + Block.SIZE;
                Block block = new Block(new Vector2(curX, curBlockY), rectangleRender);
                block.setTag("ground");
                listBlocks.add(block);
            }
        }
        return listBlocks;
    }


}
