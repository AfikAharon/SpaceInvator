package arkanoid;

import core.HitListener;
import geometry.Point;
import geometry.Rectangle;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * a ShieldGame class is in charge of holds the remain blocks and create them.
 *
 * @author Afik Aharon.
 */
public class ShieldGame {
    private List<Block> shieldBlock;
    private List<Block> remainBlocks;

    /**
     * Instantiates a new Shield game.
     *
     * @param startPositionX the start position x
     * @param startPositionY the start position y
     * @param widthBlock     the width block
     * @param heightBlcok    the height blcok
     */
    public ShieldGame(double startPositionX, double startPositionY, double widthBlock, double heightBlcok) {
        this.shieldBlock = new ArrayList<>();
        this.remainBlocks = new ArrayList<>();
        int moveRight = 0;
        int moveDown = 0;
        Block temp = null;
        Rectangle rectangle = null;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 30; j++) {
                rectangle = new Rectangle(new Point(startPositionX + moveRight, startPositionY + moveDown),
                        widthBlock, heightBlcok);
                temp = new Block(rectangle, 1, new BackgroundColor(Color.CYAN));
                this.shieldBlock.add(temp);
                this.remainBlocks.add(temp);
                moveRight += widthBlock;
            }
            moveRight = 0;
            moveDown += heightBlcok;
        }

    }

    /**
     * Add hit listener.
     *
     * @param hitListener the hit listener
     */
    public void addHitListener(HitListener hitListener) {
        for (Block block : this.shieldBlock) {
            block.addHitListener(hitListener);
        }
    }

    /**
     * Gets blocks.
     *
     * @return the blocks
     */
    public List<Block> getBlocks() {
        return this.remainBlocks;
    }

    /**
     * Remove blocks.
     *
     * @param block the block
     */
    public void removeBlocks(Block block) {
        this.remainBlocks.remove(block);
    }

    /**
     * Sets remain blocks.
     */
    public void setRemainBlocks() {
        this.remainBlocks = new ArrayList<>(this.shieldBlock);
    }
}
