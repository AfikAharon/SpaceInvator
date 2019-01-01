
import animation.AnimationRunner;
import arkanoid.GameFlow;
import arkanoid.GameSetUpAndRun;
import biuoop.GUI;
import biuoop.Sleeper;

import core.HighScoresTable;
import useful.MagN;

import java.io.File;

/**
 * a Ass7Game class, is in charge of create
 * GameSetUpAndRun object that run the game.
 *
 * @author Afik Aharon.
 */
public class Ass7Game {
    /**
     * The main function create GameSetUpAndRun object, load high score table
     * create a game flow object and animation runner  initialize the menu and
     * run it.
     *
     * @param args a level sets path
     */
    public static void main(String[] args) {
        boolean flag = true;
        File fileHighScore = new File(MagN.HIGH_SCORES_PATH);
        HighScoresTable highScoresTable = HighScoresTable.loadFromFile(fileHighScore);
        GUI gui = new GUI(MagN.GAME_NAME, MagN.GUI_WIDTH, MagN.GUI_HEIGHT);
        AnimationRunner runner = new AnimationRunner(gui, new Sleeper(), MagN.FRAME_RATE);
        GameFlow game = new GameFlow(runner, gui.getKeyboardSensor(), fileHighScore, highScoresTable);
        GameSetUpAndRun menuGame = new GameSetUpAndRun(runner, game, gui.getKeyboardSensor());
        menuGame.initialize();
        menuGame.runGameMenu();
    }
}