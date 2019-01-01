package arkanoid;

import animation.Animation;
import animation.AnimationRunner;
import animation.KeyPressStoppableAnimation;
import biuoop.DrawSurface;
import biuoop.KeyboardSensor;

import core.Collidable;
import core.Counter;
import core.Sprite;
import core.Background;
import core.Velocity;

import geometry.Point;
import geometry.Rectangle;
import levels.LevelInformation;
import useful.MagN;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.Color;
import java.io.InputStream;
import java.io.IOException;


import java.util.List;

/**
 * a Game class.
 * the class is in charge of initializing and running the game.
 *
 * @author Afik Aharon.
 */
public class GameLevel implements Animation {
    private SpriteCollection sprites;
    private GameEnvironment environment;
    private ScoreTrackingListener scoreNum;
    private AnimationRunner runner;
    private ScoreIndicator scoreIndicator;
    private LivesIndicator livesIndicator;
    private boolean running;
    private SpaceShip spaceShip;
    private LevelInformation levelInformation;
    private KeyboardSensor keyboard;
    private DrawLevelName levelName;
    private AlienMovementAndSpeed alienMovementAndSpeed;
    private PressShootCheck pressCheck;
    private BallRemover ballRemover;
    private BlockRemover blockRemover;
    private Counter counterLevels;
    private double pauseShoot;
    private Counter counterPadlleHit;
    private Counter counterShildes;

    /**
     * Constructor for the game class.
     *
     * @param levelInformation the level information game
     * @param runner           the AnimationRunner
     * @param keyboard         the KeyboardSensor game.
     * @param score            the score game.
     * @param lives            the amount lives game.
     * @param counterL         the counter l
     */
    public GameLevel(LevelInformation levelInformation, AnimationRunner runner,
                     KeyboardSensor keyboard, ScoreIndicator score, LivesIndicator lives, Counter counterL) {
        this.environment = new GameEnvironment();
        this.levelInformation = levelInformation;
        this.scoreIndicator = score;
        this.livesIndicator = lives;
        this.runner = runner;
        this.keyboard = keyboard;
        this.alienMovementAndSpeed = levelInformation.getAlienMovementAndSpeed();
        this.pressCheck = new PressShootCheck(keyboard, runner.getFramesPerSecond());
        this.counterLevels = new Counter(1);
        this.counterLevels = counterL;
        this.pauseShoot = 0;
        this.counterPadlleHit = new Counter(1);
    }

    /**
     * The function is in charge of removed Collidable from the game.
     *
     * @param c the Collidable for remove.
     */
    public void removeCollidable(Collidable c) {
        this.environment.removeCollidable(c);
    }

    /**
     * The function is in charge of removed Sprite from the game.
     *
     * @param s the Sprite for remove.
     */
    public void removeSprite(Sprite s) {
        this.sprites.removeSprite(s);
    }

    /**
     * The function insert a Collidable object to the game.
     *
     * @param c a given Collidable object.
     */
    public void addCollidable(Collidable c) {
        this.environment.addCollidable(c);
    }

    /**
     * The function insert a Sprite object to the game.
     *
     * @param s a given Sprite object.
     */
    public void addSprite(Sprite s) {
        this.sprites.addSprite(s);
    }

    /**
     * The function initialize the game.
     */
    public void initialize() {
        this.counterShildes = new Counter(0);
        this.sprites = new SpriteCollection(1.0 / runner.getFramesPerSecond());
        this.scoreNum = new ScoreTrackingListener(this.scoreIndicator.getScore());
        this.levelName = new DrawLevelName(this.levelInformation.levelName()
                + Integer.toString(this.counterLevels.getValue()));
        this.blockRemover = new BlockRemover(this,
                this.levelInformation.getCounterOfNumberOfAlienToRemove(), this.alienMovementAndSpeed,
                this.levelInformation.getShieldsGame(), this.counterShildes);
        this.ballRemover = new BallRemover(this);
        Rectangle recPaddle = new Rectangle(
                new Point(MagN.PADDLE_START_POSITION_X - this.levelInformation.paddleWidth() / 2,
                        MagN.PADDLE_START_POSITION_Y), this.levelInformation.paddleWidth(),
                MagN.PADDLE_HEIGHT);
        this.addSprite(this.levelInformation.getBackground());
        this.spaceShip = new SpaceShip(recPaddle, this.keyboard, Color.YELLOW, this.levelInformation.paddleSpeed(),
                this.counterPadlleHit);
        spaceShip.addToGame(this);
        this.createBordersGame();
        this.addSprite(this.levelName);
        this.scoreIndicator.addToGame(this);
        this.livesIndicator.addToGame(this);
        addAliensToTheGame();
        addShiledsGame();
    }

    /**
     * The function is in charge of stopping condition to the animation lop.
     *
     * @return boolean true for stop the animation lop and false for continue.
     */
    public boolean shouldStop() {
        return !this.running;
    }

    /**
     * The function is in charge of draw the all sprites game and check 7 things :
     * 1. if the user want to pause the game.
     * 2. if the player not have more lives, if yes change the boolean member to false for indication to the function
     * shouldStop.
     * 3. if the player removed the all aliens, if yes stop the animation and move to the next level.
     * 4. if the ball hit the paddle.
     * 5. if the player press space to shoot.
     * 6. if the alien arrived to left or right border.
     * 7. if all the shield game removed.
     *
     * @param d  the given DrawSurface.
     * @param dt It specifies the amount of seconds passed since the last cal
     */
    public void doOneFrame(DrawSurface d, double dt) {
        this.sprites.drawAllOn(d);
        alienMovementAndSpeed.moveAliens();
        // shoot alien ball by call th function getRandomAlienToshoot() to get random alien.
        alienShoot(this.alienMovementAndSpeed.getRandomAlienToshoot(), dt);
        // if the alien ball hit the paddle
        if (this.counterPadlleHit.getValue() == 0) {
            this.counterPadlleHit.increase(1);
            this.livesIndicator.getLives().decrease(1);
            this.running = false;
        }
        // check if the alien arrived to the shields game
        if (this.alienMovementAndSpeed.checkAliensPositionToDecreaseLives()) {
            this.livesIndicator.getLives().decrease(1);
            this.alienMovementAndSpeed.setAliensStartPosition();
            this.alienMovementAndSpeed.setAliensStartSpeed(1);
            this.running = false;
        }
        // check if the aliens arrived to the left or right border.
        this.alienMovementAndSpeed.checkAliensPositionToChangeSpeed();
        this.sprites.notifyAllTimePassed();
        // if the user removed the all aliens
        if (this.levelInformation.numberOfAlienToRemove() == 0) {
            this.running = false;
        }
        // check if the user press the space to shoot.
        if (pressCheck.checkIfTheUserPress()) {
            shoot();
        }
        if (this.counterShildes.getValue() == 0) {
            this.levelInformation.setShieldsGame();
            this.livesIndicator.getLives().decrease(1);
            this.running = false;
        }
        // if the user pause the game
        if (this.keyboard.isPressed("p")) {
            PauseScreen pauseScreen = new PauseScreen();
            InputStream is = null;
            try {
                is = ClassLoader.getSystemClassLoader().getResourceAsStream(MagN.PAUSE_SCREEN_IMAGE_PATH);
                Image image = ImageIO.read(is);
                pauseScreen.setBackground(new DrawImage(image));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    System.err.println("Failed closing file");
                }
            }
            this.runner.run(new KeyPressStoppableAnimation(this.keyboard, "space", pauseScreen));
        }
    }

    /**
     * The function run the game one turn each time, until the player
     * removed the all blocks or lost the balls game.
     */
    public void playOneTurn() {
        // move the spaceShip to the middle.
        this.spaceShip.setRectangle(
                new Rectangle(
                        new Point(MagN.PADDLE_START_POSITION_X - this.levelInformation.paddleWidth() / 2,
                                MagN.PADDLE_START_POSITION_Y), this.levelInformation.paddleWidth(),
                        MagN.PADDLE_HEIGHT));
        this.runner.run(new CountdownAnimation(2, 3, this.sprites));
        this.running = true;
        this.runner.run(this);
    }

    /**
     * the function create the game block Title and the bottom block that removed the alien balls from the game.
     */
    public void createBordersGame() {
        int width = MagN.GUI_WIDTH;
        int height = MagN.GUI_HEIGHT;
        Background backgroundRemoveBotoomBalls = new BackgroundColor(Color.WHITE);
        Background backgroundTitleBlock = new BackgroundColor(Color.decode("#EAEBE4"));
        Block titleBlock = new Block(new Rectangle(new Point(0, 0), MagN.GUI_WIDTH, MagN.WID_HEI_BLOCKS),
                1, backgroundTitleBlock);
        Block removeBotoomBalls = new Block(new Rectangle(new Point(0, height + MagN.WID_HEI_BLOCKS),
                width, MagN.WID_HEI_BLOCKS), 1, backgroundRemoveBotoomBalls);
        removeBotoomBalls.addToGame(this);
        removeBotoomBalls.addHitListener(this.ballRemover);
        titleBlock.addToGame(this);
        titleBlock.addHitListener(this.ballRemover);
    }

    /**
     * Add aliens to the game.
     */
    public void addAliensToTheGame() {
        List<Alien> aliens = this.alienMovementAndSpeed.getAliens();
        for (Alien alien : aliens) {
            Block b = alien.getAlienBlock();
            b.addToGame(this);
            b.addHitListener(this.ballRemover);
            b.addHitListener(this.blockRemover);
            b.addHitListener(this.scoreNum);
        }
    }

    /**
     * the function shoot a space ship ball.
     */
    public void shoot() {
        Velocity vel = new Velocity(0, -500);
        Point paddlePosition = this.spaceShip.getPaddlePostion();
        double x = paddlePosition.getX() + this.spaceShip.getPaddleWidth() / 2;
        double y = paddlePosition.getY() - 10;
        Ball ball = new Ball(new Point(x, y), MagN.SPACE_SHIP_BALL_RADIUS, Color.WHITE, this.environment);
        ball.setVelocity(vel);
        if (this.levelInformation.numberOfAlienToRemove() > 0) {
            ball.addToGame(this);
            ball.setAlienHitter(true);
        }
    }

    /**
     * The function shoot alien ball.
     *
     * @param position the position to shoot
     * @param dt       It specifies the amount of seconds passed since the last cal
     */
    public void alienShoot(Point position, double dt) {
        // count the time shoot fo shoot every 0.5 seconds.
        if (this.pauseShoot >= 0.5 && this.alienMovementAndSpeed.amountAliens() > 0) {
            Velocity vel = new Velocity(0, 500);
            double x = position.getX();
            double y = position.getY() + MagN.ALIEN_HEIGHT + 10;
            Ball ball = new Ball(new Point(x, y), MagN.ALIEN_BALL_RADIUS, Color.RED, this.environment);
            ball.setVelocity(vel);
            ball.addToGame(this);
            this.pauseShoot = 0;
        }
        this.pauseShoot += dt;
    }

    /**
     * Add shileds game.
     */
    public void addShiledsGame() {
        BlockRemover blockR = new BlockRemover(this, new Counter(0),
                this.alienMovementAndSpeed, this.levelInformation.getShieldsGame(), this.counterShildes);
        List<ShieldGame> shieldsGame = this.levelInformation.getShieldsGame();
        for (ShieldGame s : shieldsGame) {
            List<Block> blocks = s.getBlocks();
            s.addHitListener(this.ballRemover);
            s.addHitListener(blockR);
            for (Block b : blocks) {
                b.addToGame(this);
                this.counterShildes.increase(1);
            }
        }
    }

    /**
     * Gets the game environment.
     *
     * @return the game environment
     */
    public GameEnvironment getGameEnvironment() {
        return this.environment;
    }
}