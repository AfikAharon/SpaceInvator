package arkanoid;

import core.Counter;
import core.HitListener;
import useful.MagN;

/**
 * a ScoreTrackingListener class, is in charge of increase the score game.
 *
 * @author Afik Aharon.
 */
public class ScoreTrackingListener implements HitListener {
    private Counter currentScore;

    /**
     * Constructor for ScoreTrackingListener class..
     *
     * @param scoreCounter the score counter
     */
    public ScoreTrackingListener(Counter scoreCounter) {
        this.currentScore = scoreCounter;
    }


    /**
     * Gets current score Counter.
     *
     * @return the current score Counter.
     */
    public Counter getCurrentScore() {
        return currentScore;
    }


    /**
     * The function check if the alien have more hit and accordingly increase
     * the score :
     * 1. if the alien not have more hit increase 100 points
     *
     * @param beingHit the being hit alien block.
     * @param hitter   the hitter ball.
     */
    public void hitEvent(Block beingHit, Ball hitter) {
        if (beingHit.getHitPoints() <= 0 && hitter.alienHitterBall()) {
            this.currentScore.increase(MagN.REMOVED_ALIEN);
        }
    }
}