package arkanoid;

import core.Counter;
import core.HitListener;

import java.util.List;

/**
 * a BlockRemover class.
 * BlockRemover is in charge of removing blocks from the game, as well as keeping count
 * of the number of blocks that remain.
 *
 * @author Afik Aharon.
 */
public class BlockRemover implements HitListener {
    private GameLevel game;
    private Counter remainingBlocksAlien;
    private Counter remainingBlocksShield;
    private AlienMovementAndSpeed alienMovementAndSpeed;
    private List<ShieldGame> shieldsGame;

    /**
     * Constructor for BlockRemover class.
     *
     * @param game               the given game
     * @param removedBlocksAlien the removed alien counter.
     * @param aliens             the AlienMovementAndSpeed object for use in the hitEven function.
     * @param removedShieldBlock the remove shield blocks counter.
     * @param shields            the shield blocks list.
     */
    public BlockRemover(GameLevel game, Counter removedBlocksAlien, AlienMovementAndSpeed aliens,
                        List<ShieldGame> shields, Counter removedShieldBlock) {
        this.game = game;
        this.remainingBlocksAlien = removedBlocksAlien;
        this.alienMovementAndSpeed = aliens;
        this.shieldsGame = shields;
        this.remainingBlocksShield = removedShieldBlock;
    }

    /**
     * The function in charge of remove blocks and aliens that are hit and reach 0 hit-points,
     * check if the block is alien block and the hitter ball is space ship ball, remove from the
     * game and from the AlienMovementAndSpeed class, same for the shield blocks.
     *
     * @param beingHit the hit block.
     * @param hitter   the hitter ball.
     */
    public void hitEvent(Block beingHit, Ball hitter) {
        boolean flag = true;
        for (int i = 0; i < this.alienMovementAndSpeed.amountAliens(); i++) {
            if (this.alienMovementAndSpeed.getAliens().get(i).getAlienBlock() == beingHit) {
                // check if the ball is space ship ball.
                if (hitter.alienHitterBall()) {
                    this.alienMovementAndSpeed.getAliens().remove(i);
                    beingHit.removeFromGame(this.game);
                    beingHit.removeHitListener(this);
                    // Decrease the number of blocks.
                    this.remainingBlocksAlien.decrease(1);
                }
                flag = false;
                break;

            }
        }
        if (beingHit.getHitPoints() <= 0 && flag) {
            beingHit.removeFromGame(this.game);
            beingHit.removeHitListener(this);
            for (ShieldGame s : shieldsGame) {
                for (Block b : s.getBlocks()) {
                    // check if the block is shield block
                    if (b == beingHit) {
                        s.removeBlocks(b);
                        this.remainingBlocksShield.decrease(1);
                        break;
                    }
                }
            }
        }

    }
}