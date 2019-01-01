package arkanoid;

import geometry.Point;
import useful.MagN;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * a AlienMovementAndSpeed the class in charge of sets the aliens speed
 * sets to the alien start position , and find a random alien to shoot a ball.
 *
 * @author Afik Aharon.
 */
public class AlienMovementAndSpeed {
    private List<Alien> aliens;
    private double dt;
    private double startSpeed;
    private List<Alien> remainAliens;

    /**
     * Instantiates a new Alien movement.
     *
     * @param aliens the aliens
     * @param dt     the dt
     */
    public AlienMovementAndSpeed(List<Alien> aliens, double dt) {
        this.aliens = aliens;
        this.dt = dt;
        this.startSpeed = aliens.get(0).getSpeed();
        this.remainAliens = new ArrayList<>(aliens);
    }

    /**
     * The function have been call form the game level and in charge to
     * move the alien one step.
     */
    public void moveAliens() {
        for (Alien alien : aliens) {
            alien.changePosition(this.dt);
        }
    }

    /**
     * Check aliens position to decrease lives .
     * the function check if one of the aliens arrived to the shield position.
     *
     * @return true if the alien arrived otherwise false
     */
    public boolean checkAliensPositionToDecreaseLives() {
        for (Alien alien : remainAliens) {
            if (alien.getPosition().getY() >= MagN.START_SHIELD_Y - alien.getAilenHeight() - 20) {
                setAliensStartPosition();
                return true;
            }
        }
        return false;
    }

    /**
     * Check aliens position to change speed.
     * the function search the closet alien to the border and check if the alien
     * pass the border.
     */
    public void checkAliensPositionToChangeSpeed() {
        double minMovment = remainAliens.get(0).getPosition().getX() + dt * remainAliens.get(0).getSpeed();
        double maxMovment = remainAliens.get(0).getPosition().getX() + dt * remainAliens.get(0).getSpeed();
        double width = remainAliens.get(0).getAilenWidth();
        double movment;
        Alien maxAlien = remainAliens.get(0);
        Alien minAlien = remainAliens.get(0);
        for (Alien alien : remainAliens) {
            movment = alien.getPosition().getX() + dt * alien.getSpeed();
            if (movment > maxMovment) {
                maxMovment = movment;
                maxAlien = alien;
            }
            if (movment < minMovment) {
                minMovment = movment;
                minAlien = alien;
            }
        }
        // if the alien arrived to right border
        if (maxMovment >= MagN.GUI_WIDTH - width) {
            movment = MagN.GUI_WIDTH - width - maxAlien.getPosition().getX();
            setAliensMovement(movment);
            // if the alien arrived to left border
        } else if (minMovment <= 0 && minAlien.getSpeed() < 0) {
            movment = minAlien.getPosition().getX();
            setAliensMovement(-movment);
        }
    }

    /**
     * the function sets the aliens speed and change the alien position to
     * almost to the limit border.
     *
     * @param movment the alien movement to change the location.
     */
    public void setAliensMovement(double movment) {
        for (Alien alien : aliens) {
            alien.setSpeed(alien.getSpeed() * (-1.1));
            alien.moveDown(MagN.MOVE_ALIEN_DOWN);
            alien.setAlienPosition(movment);
        }
    }

    /**
     * Sets aliens start speed.
     *
     * @param multSpeed the mult speed
     */
    public void setAliensStartSpeed(double multSpeed) {
        for (Alien alien : aliens) {
            alien.setSpeed(this.startSpeed * multSpeed);
        }
    }

    /**
     * Sets aliens to start position.
     */
    public void setAliensStartPosition() {
        for (Alien alien : aliens) {
            alien.setToStartPosition();
        }
    }

    /**
     * Gets remain aliens.
     *
     * @return the aliens
     */
    public List<Alien> getAliens() {
        return this.remainAliens;
    }

    /**
     * Amount aliens in the game.
     *
     * @return the int
     */
    public int amountAliens() {
        return this.remainAliens.size();
    }

    /**
     * Gets random alien to shoot.
     *
     * @return the random alien position to shoot
     */
    public Point getRandomAlienToshoot() {
        Random randomPosition = new Random();
        int position = randomPosition.nextInt(this.remainAliens.size());
        for (int i = 0; i < this.remainAliens.size(); i++) {
            // if the there is a alien int the same row
            if (this.remainAliens.get(i).getPosition().getX()
                    == this.remainAliens.get(position).getPosition().getX()) {
                // if there is a alien that the position value big than the random position.
                if (this.remainAliens.get(i).getPosition().getY()
                        > this.remainAliens.get(position).getPosition().getY()) {
                    position = i;
                }
            }
        }
        return this.remainAliens.get(position).getPosition();
    }

    /**
     * Sets remain aliens.
     */
    public void setRemainAliens() {
        this.remainAliens = new ArrayList<>(this.aliens);
    }
}
