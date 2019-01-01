package levels;

import arkanoid.AlienMovementAndSpeed;
import arkanoid.ShieldGame;
import core.Counter;
import core.Sprite;

import java.util.List;

/**
 * The interface LevelDetails information.
 *
 * @author Afik Aharon.
 */
public interface LevelInformation {

    /**
     * The function return the SpaceShip speed.
     *
     * @return the SpaceShip speed
     */
    int paddleSpeed();

    /**
     * The function return the SpaceShip width.
     *
     * @return the SpaceShip width.
     */
    int paddleWidth();

    /**
     * the function return the level name string.
     *
     * @return the level name string
     */
    String levelName();

    /**
     * The function return a Sprite that draw the background level.
     *
     * @return the background Sprite.
     */
    Sprite getBackground();

    /**
     * The function return the remain aliens to remove.
     *
     * @return remain Aliens to remove.
     */
    int numberOfAlienToRemove();

    /**
     * The function return the counter of number of aliens to remove.
     *
     * @return the counter of number of blocks to remove
     */
    Counter getCounterOfNumberOfAlienToRemove();

    /**
     * Gets alien movement and speed.
     *
     * @return the alien movement and speed
     */
    AlienMovementAndSpeed getAlienMovementAndSpeed();

    /**
     * Gets shields game.
     *
     * @return the shields game
     */
    List<ShieldGame> getShieldsGame();

    /**
     * Sets shields game.
     */
    void setShieldsGame();
}