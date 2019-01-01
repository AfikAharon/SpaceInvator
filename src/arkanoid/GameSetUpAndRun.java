package arkanoid;

import animation.AnimationRunner;
import animation.KeyPressStoppableAnimation;
import biuoop.KeyboardSensor;
import core.HighScoresTable;
import core.Task;
import geometry.Point;
import levels.AliensLevel;
import levels.LevelInformation;
import useful.MagN;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * The class is in charge of sets a game and run it.
 *
 * @author Afik Aharon.
 */
public class GameSetUpAndRun {
    private KeyboardSensor sensor;
    private AnimationRunner runner;
    private GameFlow game;
    private HighScoresTable highScoresTable;
    private KeyPressStoppableAnimation highScoresAnimation;
    private MenuAnimation<Task<Void>> menu;


    /**
     * Instantiates a new Game set up and run.
     *
     * @param runner the animation runner
     * @param game   the game flow object
     * @param sensor the keyboard sensor
     */
    public GameSetUpAndRun(AnimationRunner runner, GameFlow game, KeyboardSensor sensor) {
        this.game = game;
        this.runner = runner;
        this.sensor = sensor;
        this.highScoresTable = game.getHighScoresTable();
    }

    /**
     * The function is in charge of create a game tasks and sub menu.
     */
    public void initialize() {
        HighScoresAnimation highScoreA = new HighScoresAnimation(this.highScoresTable, MagN.PRESS_BUTTON_STRING);
        MenuAnimation<Task<Void>> subMenuAnimation = new MenuAnimation<Task<Void>>(MagN.SUB_MENU_TITLE, this.sensor);
        this.highScoresAnimation = new KeyPressStoppableAnimation(this.sensor, MagN.PRESS_BUTTON_STRING, highScoreA);
        this.menu = new MenuAnimation<Task<Void>>(MagN.GAME_NAME, this.runner.getGui().getKeyboardSensor());
        InputStream is = null;
        // try to load a image Background for the high score table,menu and the submenu.
        try {
            is = ClassLoader.getSystemClassLoader().getResourceAsStream(MagN.HIGH_SCORES_IMAGE_PATH);
            Image image = ImageIO.read(is);
            highScoreA.setBackground(new DrawImage(image));
            is = ClassLoader.getSystemClassLoader().getResourceAsStream(MagN.MENU_IMAGE_PATH);
            image = ImageIO.read(is);
            this.menu.setBackground(new DrawImage(image));
            is = ClassLoader.getSystemClassLoader().getResourceAsStream(MagN.SUB_MENU_IMAGE_PATH);
            image = ImageIO.read(is);
            subMenuAnimation.setBackground(new DrawImage(image));
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
        // task for the menu game
        Task<Void> runMenuGame = new Task<Void>() {
            @Override
            public Void run() {
                runTheGame();
                return null;
            }
        };
        // task for the high score table animation
        Task<Void> runHighScores = new Task<Void>() {
            @Override
            public Void run() {
                runTheHighScoresTable();
                return null;
            }
        };
        // task for the quit option
        Task<Void> quitGame = new Task<Void>() {
            @Override
            public Void run() {
                System.exit(0);
                return null;
            }
        };
        this.menu.addSelection("s", "Start Game", runMenuGame);
        this.menu.addSelection("h", "High Scores", runHighScores);
        this.menu.addSelection("q", "Quit", quitGame);
    }

    /**
     * The function run the game menu.
     */
    public void runGameMenu() {
        while (true) {
            runner.run(menu);
            // wait for user selection
            Task<Void> task = menu.getStatus();
            task.run();
        }
    }

    /**
     * The function create a list of levels by call the
     * object member levelSpecificationReader and run the game.
     */
    public void runTheGame() {
        AlienMovementAndSpeed alienMovementAndSpeed = createAlienMovment();
        LevelInformation levelInformations = new AliensLevel(alienMovementAndSpeed, 400, 70);
        this.game.runLevels(levelInformations);
        runTheHighScoresTable();
    }

    /**
     * Run the high scores table.
     */
    public void runTheHighScoresTable() {
        this.runner.run(this.highScoresAnimation);
        this.highScoresAnimation.setStop();
    }

    /**
     * The function create a AlienMovementAndSpeed object.
     *
     * @return AlienMovementAndSpeed object for the level game.
     */
    public AlienMovementAndSpeed createAlienMovment() {
        AlienMovementAndSpeed alienMovementAndSpeed = null;
        InputStream is = null;
        try {
            is = ClassLoader.getSystemClassLoader().getResourceAsStream(MagN.ALINE_IMAGE_PATH);
            Image image = ImageIO.read(is);
            List<Alien> aliens = new ArrayList<>();
            double moveRight = 0;
            double moveDown = 0;
            double startPositionX = 40;
            double startPositionY = 60;
            Alien temp;
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 10; j++) {
                    temp = new Alien(image, new Point(startPositionX
                            + moveRight, startPositionY + moveDown), 63, 40, 30);
                    moveRight += 55;
                    aliens.add(temp);
                }
                moveRight = 0;
                moveDown += 45;
            }
            alienMovementAndSpeed = new AlienMovementAndSpeed(aliens, 1.0 / runner.getFramesPerSecond());
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
        return alienMovementAndSpeed;
    }
}
