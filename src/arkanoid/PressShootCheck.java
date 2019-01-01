package arkanoid;

import biuoop.KeyboardSensor;

/**
 * a PressShootCheck class is in charge of check id the player
 * press space to shoot ball.
 *
 * @author Afik Aharon.
 */
public class PressShootCheck {
    private KeyboardSensor sensor;
    private boolean isAlreadyPressed;
    private double pauseShoot;
    private double dt;

    /**
     * Instantiates a new Press shoot check.
     *
     * @param sensor         the sensor
     * @param framePersecond the frame per second
     */
    public PressShootCheck(KeyboardSensor sensor, double framePersecond) {
        this.sensor = sensor;
        this.isAlreadyPressed = true;
        this.dt = 1.0 / framePersecond;
        this.pauseShoot = 0.0;
    }

    /**
     * Check if the user press space, the function limit
     * the press for every 0.35 second.
     *
     * @return true if yes , false if not
     */
    public boolean checkIfTheUserPress() {
        if (this.sensor.isPressed("space") && !this.isAlreadyPressed) {
            isAlreadyPressed = true;
        } else if (!this.sensor.isPressed("space")) {
            this.isAlreadyPressed = false;
        } else if (pauseShoot >= 0.35) {
            pauseShoot = 0.0;
            return true;
        }
        pauseShoot += this.dt;
        return false;
    }
}
