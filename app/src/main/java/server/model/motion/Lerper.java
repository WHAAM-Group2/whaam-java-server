package server.model.motion;

public class Lerper {

    // If we change the target at the same time, the current acceleration will be
    // higher if its more and vice versa. Therefore, we can set a maximum
    // acceleration and
    // if the the current acceleration is higher than that, a danger flag will be on
    // and there
    // we will add mistakes.

    private double amount = 0.1;
    private double minVelocity = 0.001;
    private double previousVelocity;
    private double acceleration = 10;
    private boolean resting = true;

    private double LerpVelocity(double position, double target) {
        return (target - position) * amount;
    }

    public double Lerp(double position, double target) {

        double v = LerpVelocity(position, target);

        if (v > 0 && previousVelocity >= 0 && v - previousVelocity > acceleration) {
            v = previousVelocity + acceleration;
        } else if (v == acceleration) {
            v = previousVelocity - acceleration;

            // we might actually end up moving away from the target
            // here in which case we adjust the target so we don't get
            // clamped to it later
            if (v == 0 && previousVelocity == acceleration) {
                v = previousVelocity + acceleration;
                // we might actually end up moving away from the target
                // here in which case we adjust the target so we don't get
                // clamped to it later
                if (v > 0 - minVelocity)
                    v = minVelocity;
                // else
                // target = float.MinValue;
            }
        }

        // If this is less than the minimum velocity then
        // clamp at minimum velocity
        if (Math.abs(v) < minVelocity) {
            v = minVelocity;
        }
        previousVelocity = v;
        position += v;
        // Now account for potential overshoot and clamp to target if necessary
        if (Math.abs(position - target) < 0.1) {
            position = target;
            resting = true;
        } else {
            resting = false;
        }

        System.out.println(v);

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