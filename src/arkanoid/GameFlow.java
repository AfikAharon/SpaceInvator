package arkanoid;

import animation.AnimationRunner;
import animation.KeyPressStoppableAnimation;
import biuoop.DialogManager;
import biuoop.KeyboardSensor;
import core.Counter;
import core.HighScoresTable;
import core.ScoreInfo;
import levels.LevelInformation;
import useful.MagN;

import javax.imageio.ImageIO;
import java.awt.Image;

import java.io.InputStream;
import java.io.IOException;
import java.io.File;

/**
 * a GameFlow class, the class run the levels game.
 *
 * @author Afik Aharon.
 */
public class GameFlow {
    private AnimationRunner animationRunner;
    private KeyboardSensor keyboardSensor;
    private HighScoresTable highScoresTable;
    private File fileScoresTable;

    /**
     * Constructor for EndScreen class.
     *
     * @param ar              the AnimationRunner
     * @param ks              the KeyboardSensor
     * @param fileScoresTable the file scores table
     * @param highScoresTable the high scores table
     */
    public GameFlow(AnimationRunner ar, KeyboardSensor ks, File fileScoresTable, HighScoresTable highScoresTable) {
        this.animationRunner = ar;
        this.keyboardSensor = ks;
        this.highScoresTable = highScoresTable;
        this.fileScoresTable = fileScoresTable;
    }

    /**
     * the function run the given levels game, sets the game by call the function in AlienMovementAndSpeed
     * for reset the alien information, and call the function is ShieldGame for reset the blocks information.
     *
     * @param alienLevel the level game
     */
    public void runLevels(LevelInformation alienLevel) {
        ScoreIndicator scoreIndicator = new ScoreIndicator(new Counter(0));
        LivesIndicator livesIndicator = new LivesIndicator(new Counter(3));
        Counter counterLevels = new Counter(1);
        // while the player have more lives
        while (livesIndicator.getLives().getValue() > 0) {
            GameLevel levelGame = new GameLevel(alienLevel, this.animationRunner, this.keyboardSensor,
                    scoreIndicator, livesIndicator, counterLevels);
            if (alienLevel.numberOfAlienToRemove() == 0) {
                counterLevels.increase(1);
                alienLevel.getAlienMovementAndSpeed().setRemainAliens();
                alienLevel.getCounterOfNumberOfAlienToRemove()
                        .increase(alienLevel.getAlienMovementAndSpeed().amountAliens());
                alienLevel.getAlienMovementAndSpeed().setAliensStartPosition();
                alienLevel.getAlienMovementAndSpeed().setAliensStartSpeed(1.1);
                alienLevel.getAlienMovementAndSpeed().setRemainAliens();
                alienLevel.setShieldsGame();
                levelGame.initialize();
            } else {
                levelGame.initialize();
                alienLevel.getAlienMovementAndSpeed().setAliensStartSpeed(1);
                alienLevel.getAlienMovementAndSpeed().setAliensStartPosition();
                levelGame.playOneTurn();
            }
        }
        runTheTableAndEndScreen(livesIndicator, scoreIndicator);
    }

    /**
     * The function run the high score table and the end screen.
     *
     * @param livesIndicator the amount of the user lives game
     * @param scoreIndicator the user score game.
     */
    public void runTheTableAndEndScreen(LivesIndicator livesIndicator, ScoreIndicator scoreIndicator) {
        KeyPressStoppableAnimation keyPressAnimation = new KeyPressStoppableAnimation(this.keyboardSensor,
                "space", null);
        EndScreen endScreen = null;
        // if the player win run a win end screen.
        if (livesIndicator.getLives().getValue() > 0) {
            endScreen = new EndScreen(MagN.WIN_MESSAGE, scoreIndicator.getScore());
            // otherwise the player lost run a lost end screen.
        } else {
            endScreen = new EndScreen(MagN.LOST_MESSAGE, scoreIndicator.getScore());
        }
        InputStream is = null;
        try {
            is = ClassLoader.getSystemClassLoader().getResourceAsStream(MagN.END_SCREEN_IMAGE_PATH);
            Image image = ImageIO.read(is);
            endScreen.setBackground(new DrawImage(image));
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
        keyPressAnimation.setAnimation(endScreen);
        this.animationRunner.run(keyPressAnimation);
        if (this.highScoresTable.getRank(scoreIndicator.getScore().getValue()) < this.highScoresTable.size()
                && scoreIndicator.getScore().getValue() > 0) {
            DialogManager dialog = this.animationRunner.getGui().getDialogManager();
            String name = dialog.showQuestionDialog(MagN.DIALOG_MESSAGE_ONE, MagN.DIALOG_MESSAGE_TOW, "");
            this.highScoresTable.add(new ScoreInfo(name, scoreIndicator.getScore().getValue()));
            try {
                this.highScoresTable.save(this.fileScoresTable);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Gets high scores table.
     *
     * @return the high scores table
     */
    public HighScoresTable getHighScoresTable() {
        return this.highScoresTable;
    }
}