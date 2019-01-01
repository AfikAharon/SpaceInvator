package levels;

import arkanoid.AlienMovementAndSpeed;
import arkanoid.ShieldGame;
import biuoop.DrawSurface;
import core.Counter;
import core.Sprite;
import useful.MagN;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * a AliensLevel class, is a alien level game.
 *
 * @author Afik Aharon.
 */
public class AliensLevel implements LevelInformation {
    private int paddleSpeed;
    private int paddleWidth;
    private AlienMovementAndSpeed alienMovementAndSpeed;
    private String levelName;
    private Counter amountRemainAilens;
    private List<ShieldGame> shieldsGame;

    /**
     * Instantiates a new Aliens level.
     *
     * @param alienM the AlienMovementAndSpeed object
     * @param speed  the aliens speed
     * @param width  the aliens width
     */
    public AliensLevel(AlienMovementAndSpeed alienM, int speed, int width) {
        this.alienMovementAndSpeed = alienM;
        this.paddleSpeed = speed;
        this.paddleWidth = width;
        this.levelName = MagN.ALIEN_LEVEL_NAME;
        this.amountRemainAilens = new Counter(alienM.amountAliens());
        this.shieldsGame = new ArrayList<>();
        ShieldGame shieldGame = null;
        int moveLeft = 0;
        // create a shields game
        for (int i = 0; i < 3; i++) {
            shieldGame = new ShieldGame(MagN.START_SHIELD_X + moveLeft,
                    MagN.START_SHIELD_Y, 5, 5);
            this.shieldsGame.add(shieldGame);
            moveLeft += 250;
        }
    }

    /**
     * The function return the SpaceShip speed.
     *
     * @return the SpaceShip speed
     */
    public int paddleSpeed() {
        return this.paddleSpeed;
    }

    /**
     * The function return the SpaceShip width.
     *
     * @return the SpaceShip width.
     */
    public int paddleWidth() {
        return this.paddleWidth;
    }

    /**
     * the function return the level name string.
     *
     * @return the level name string
     */
    public String levelName() {
        return this.levelName;
    }

    /**
     * The function return a Sprite that draw the background level.
     *
     * @return the background Sprite.
     */
    public Sprite getBackground() {
        return new Sprite() {
            /**
             * draw the sprite on the surface.
             *
             * @param d a given draw surface
             */
            public void drawOn(DrawSurface d) {
                d.setColor(Color.BLACK);
                d.fillRectangle(0, 0, MagN.GUI_WIDTH, MagN.GUI_HEIGHT);
            }

            /**
             * notify the sprite that time has passed.
             *
             * @param dt It specifies the amount of seconds passed since the last cal
             */
            public void timePassed(double dt) {

            }
        };
    }

    /**
     * The function return the remain aliens to remove.
     *
     * @return remain Aliens to remove.
     */
    public int numberOfAlienToRemove() {
        return this.amountRemainAilens.getValue();
    }

    /**
     * The function return the counter of number of aliens to remove.
     *
     * @return the counter of number of blocks to remove
     */
    public Counter getCounterOfNumberOfAlienToRemove() {
        return this.amountRemainAilens;
    }

    /**
     * Gets alien movement and speed.
     *
     * @return the alien movement and speed
     */
    public AlienMovementAndSpeed getAlienMovementAndSpeed() {
        return this.alienMovementAndSpeed;
    }

    /**
     * Gets shields game.
     *
     * @return the shields game
     */
    public List<ShieldGame> getShieldsGame() {
        return this.shieldsGame;
    }

    /**
     * Sets shields game.
     */
    public void setShieldsGame() {
        for (ShieldGame s : shieldsGame) {
            s.setRemainBlocks();
        }
    }
}
