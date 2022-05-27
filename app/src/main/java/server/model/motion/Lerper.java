package server.model.motion;

/**
 * @author Wael Mahrous
 * 
 *         Class that attempts to smooth acceleration of the average movement
 *         measured for a detected DNN Object.
 */

public class Lerper {

    private double amount = 0.1;
    private double minVelocity = 0.001;
    private double previousVelocity;
    private double acceleration = 10;
    private boolean resting = true;

    /**
     * Calculates the correct velocity based on current position and target
     * position.
     * 
     * @param position Position of object attempting to move towards target.
     * @param target   Position of target.
     * @return
     */

    private double LerpVelocity(double position, double target) {
        return (target - position) * amount;
    }

    /**
     * "Moves" the object towards the target. This simply means that this function
     * will take the position variable and change it's value according to the linear
     * interpolation algorithm written.
     * 
     * @param position Position of object attempting to move towards target.
     * @param target   Position of target.
     * @return
     */

    public double Lerp(double position, double target) {

        double v = LerpVelocity(position, target);

        if (v > 0 && previousVelocity >= 0 && v - previousVelocity > acceleration) {
            v = previousVelocity + acceleration;
        } else if (v == acceleration) {
            v = previousVelocity - acceleration;

            if (v == 0 && previousVelocity == acceleration) {
                v = previousVelocity + acceleration;

                if (v > 0 - minVelocity)
                    v = minVelocity;

            }
        }

        if (Math.abs(v) < minVelocity) {
            v = minVelocity;
        }
        previousVelocity = v;
        position += v;

        if (Math.abs(position - target) < 0.1) {
            position = target;
            resting = true;
        } else {
            resting = false;
        }

        return position;

    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getMinVelocity() {
        return this.minVelocity;
    }

    public void setMinVelocity(double minVelocity) {
        this.minVelocity = minVelocity;
    }

    public double getPreviousVelocity() {
        return this.previousVelocity;
    }

    public void setPreviousVelocity(double previousVelocity) {
        this.previousVelocity = previousVelocity;
    }

    public double getAcceleration() {
        return this.acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public boolean isResting() {
        return this.resting;
    }

    public boolean getResting() {
        return this.resting;
    }

    public void setResting(boolean resting) {
        this.resting = resting;
    }

}