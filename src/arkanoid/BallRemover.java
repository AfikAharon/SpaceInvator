package arkanoid;

import core.HitListener;

/**
 * a BallRemover class is in charge of removing balls from the game.
 *
 * @author Afik Aharon.
 */
public class BallRemover implements HitListener {
    private GameLevel game;
    /**
     * Constructor for BallRemover class.
     *
     * @param game the given game.
     */
    public BallRemover(GameLevel game) {
        this.game = game;
    }

    /**
     * If the ball hit on the block shield or the alien the
     * function have bin call and the ball removed from the game.
     *
     * @param beingHit the being hit block.
     * @param hitter   the hitter ball.
     */
    public void hitEvent(Block beingHit, Ball hitter) {
            hitter.removeFromGame(this.game);
    }
}
