package arkanoid;

import geometry.Point;
import geometry.Rectangle;

import java.awt.Image;

/**
 * a alien class.
 *
 * @author Afik Aharon.
 */
public class Alien {
    private Block alienPosition;
    private Rectangle startPosition;
    private Image image;
    private double speed;
    private double alienSpeed;

    /**
     * Instantiates a new Alien.
     *
     * @param img    the alien image
     * @param posti  the start position
     * @param spe    the speed
     * @param width  the alien width
     * @param height the alien height
     */
    public Alien(Image img, Point posti, int spe, int width, int height) {
        this.speed = spe;
        this.image = img;
        this.startPosition = new Rectangle(posti, width, height);
        this.alienPosition = new Block(startPosition, 1, new BackgroundImage(img));
    }

    /**
     * Sets to start position.
     */
    public void setToStartPosition() {
        this.alienPosition.setRectangleBlock(this.startPosition);
    }

    /**
     * Move down.
     *
     * @param movement the movement
     */
    public void moveDown(double movement) {
        Point position = this.alienPosition.getBlockPostion();
        position = new Point(position.getX(), position.getY() + movement);
        this.alienPosition.setRectangleBlock(new Rectangle(position, this.alienPosition.getBlockWidth(),
                this.alienPosition.getBlockHeight()));
    }

    /**
     * Gets speed.
     *
     * @return the speed
     */
    public double getSpeed() {
        return this.speed;
    }

    /**
     * the function sets the alien speed.
     *
     * @param newSpeed the new speed
     */
    public void setSpeed(double newSpeed) {
        this.speed = newSpeed;
    }

    /**
     * The function sets the alien position.
     *
     * @param movement the new movement to sets
     */
    public void setAlienPosition(double movement) {
        Point newPosition = new Point(this.alienPosition.getBlockPostion().getX() + movement,
                                    this.alienPosition.getBlockPostion().getY());
        Rectangle rectangle = new Rectangle(newPosition, this.alienPosition.getBlockWidth(),
                                            this.alienPosition.getBlockHeight());
        this.alienPosition.setRectangleBlock(rectangle);
    }

    /**
     * Gets alen position.
     *
     * @return the position
     */
    public Point getPosition() {
        return this.alienPosition.getBlockPostion();
    }

    /**
     * Gets ailen height.
     *
     * @return the ailen height
     */
    public int getAilenHeight() {
        return this.alienPosition.getBlockHeight();
    }

    /**
     * Gets ailen width.
     *
     * @return the ailen width
     */
    public int getAilenWidth() {
        return this.alienPosition.getBlockWidth();
    }

    /**
     * Gets alien block.
     *
     * @return the alien block
     */
    public Block getAlienBlock() {
        return this.alienPosition;
    }

    /**
     * The function have been call when the game level call
     * the function to change the alien position.
     *
     * @param dt It specifies the amount of seconds passed since the last cal
     */
    public void changePosition(double dt) {
        this.alienSpeed = dt * speed;
        Point position = this.alienPosition.getBlockPostion();
        position = new Point(position.getX() + this.alienSpeed, position.getY());
        this.alienPosition.setRectangleBlock(new Rectangle(position,
                this.alienPosition.getBlockWidth(), this.alienPosition.getBlockHeight()));
    }
}
