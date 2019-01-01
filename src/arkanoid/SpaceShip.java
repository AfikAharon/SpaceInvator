package arkanoid;

import biuoop.DrawSurface;

import java.awt.Color;

import biuoop.KeyboardSensor;
import core.Counter;
import core.Sprite;
import geometry.Point;
import geometry.Rectangle;
import useful.MagN;
import core.Collidable;

/**
 * a SpaceShip class is a player on the game,
 * that controlled by the keyboard sensor and move (left or right)
 * according to the user.
 *
 * @author Afik Aharon.
 */
public class SpaceShip implements Sprite, Collidable {
    private biuoop.KeyboardSensor keyboard;
    private Rectangle recPadDle;
    private Color color;
    private double speed;
    private double paddleSpeed;
    private Counter counterHit;

    /**
     * Constructor for the paddle class.
     *
     * @param r           the Rectangle of the paddle.
     * @param keyboard    the given keyboard sensor.
     * @param color       the paddle color.
     * @param paddleSpeed the paddle speed movement.
     * @param hitPoints   the hit points
     */
    public SpaceShip(Rectangle r, KeyboardSensor keyboard, Color color, int paddleSpeed, Counter hitPoints) {
        this.recPadDle = r;
        this.keyboard = keyboard;
        this.color = color;
        this.speed = paddleSpeed;
        this.counterHit = hitPoints;
    }

    /**
     * The function move the paddle Left,check if the paddle cross the borders game.
     */
    public void moveLeft() {
        // check if the paddle arrived to the Left border after the movement.
        if (this.recPadDle.getUpperLeft().getX() - this.paddleSpeed < 0) {
            this.recPadDle = new Rectangle(new Point(2.0001,
                    this.recPadDle.getUpperLeft().getY()),
                    this.recPadDle.getWidth(), this.recPadDle.getHeight());
            return;
        }
        // else move the paddle Left (10 int each move)
        Point newP = new Point(this.recPadDle.getUpperLeft().getX() - this.paddleSpeed,
                this.recPadDle.getUpperLeft().getY());
        this.recPadDle = new Rectangle(newP, this.recPadDle.getWidth(), this.recPadDle.getHeight());
    }

    /**
     * The function move the paddle ,check if the paddle cross the borders game.
     */
    public void moveRight() {
        // check if the paddle arrived to the Right border after the movement.
        if (this.recPadDle.getUpperLeft().getX() + this.paddleSpeed
                + this.recPadDle.getWidth() > MagN.GUI_WIDTH) {
            this.recPadDle = new Rectangle(new Point(MagN.GUI_WIDTH
                    - this.recPadDle.getWidth() - 0.001, this.recPadDle.getUpperLeft().getY()),
                    this.recPadDle.getWidth(), this.recPadDle.getHeight());
            return;
        }
        // else move the paddle Right
        Point newP = new Point(this.recPadDle.getUpperLeft().getX() + this.paddleSpeed,
                this.recPadDle.getUpperLeft().getY());
        this.recPadDle = new Rectangle(newP, this.recPadDle.getWidth(), this.recPadDle.getHeight());
    }

    /**
     * The function check if the Left or the Right keyboard pressed,
     * and call the function that move the ball.
     *
     * @param dt It specifies the amount of seconds passed since the last cal
     */
    public void timePassed(double dt) {
        this.paddleSpeed = dt * speed;
        if (keyboard.isPressed(KeyboardSensor.LEFT_KEY)) {
            moveLeft();
        } else if (keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) {
            moveRight();
        }
    }

    /**
     * The function draw the paddle on the surface.
     *
     * @param d the draw surface.
     */
    public void drawOn(DrawSurface d) {
        int startX = (int) this.recPadDle.getUpperLeft().getX();
        int startY = (int) this.recPadDle.getUpperLeft().getY();
        int endX = (int) this.recPadDle.getWidth() + startX;
        int endY = (int) this.recPadDle.getHeight() + startY;
        d.setColor(this.color);
        d.fillRectangle(startX, startY, (int) this.recPadDle.getWidth(), (int) this.recPadDle.getHeight());
        d.setColor(Color.BLACK);
        d.drawLine(startX, startY, endX, startY);
        d.drawLine(startX, startY, startX, endY);
        d.drawLine(endX, startY, endX, endY);
        d.drawLine(startX, endY, endX, endY);
    }

    /**
     * The function sets the paddle color.
     *
     * @param c a given color.
     */
    public void setColor(Color c) {
        this.color = c;
    }

    /**
     * @return return the geometry.Rectangle member.
     */
    public Rectangle getCollisionRectangle() {
        return this.recPadDle;
    }

    /**
     * The function check where the object hit on the paddle,
     * and decrease the counter paddle hit.
     *
     * @param collisionPoint  the collision point.
     * @param hitter          the hitter ball.
     */
    public void hit(Ball hitter, Point collisionPoint) {
        if (collisionPoint.getY() == this.recPadDle.getUpperLeft().getY()) {
            this.counterHit.decrease(1);
        }
    }

    /**
     * The function insert the paddle to the given game.
     *
     * @param g a given game.
     */
    public void addToGame(GameLevel g) {
        g.addSprite(this);
        g.addCollidable(this);
    }

    /**
     * The function sets the Rectangle paddle.
     *
     * @param rec a given Rectangle.
     */
    public void setRectangle(Rectangle rec) {
        this.recPadDle = rec;
    }

    /**
     * Gets paddle position.
     *
     * @return the paddle position
     */
    public Point getPaddlePostion() {
        return this.recPadDle.getUpperLeft();
    }

    /**
     * Gets paddle width.
     *
     * @return the paddle width
     */
    public double getPaddleWidth() {
        return this.recPadDle.getWidth();
    }

}