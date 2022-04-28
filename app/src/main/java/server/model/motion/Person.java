package server.model.motion;

import java.util.LinkedList;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Person {

    private String name;

    private LinkedList<Rect> rectHistory;
    private double averageMovement = 0;

    private Lerper lerper;
    private double lerpTarget;
    private double lerpPosition;
    private double lerpPercent;

    Mat gauge = Imgcodecs.imread("data/NUMBERLINE.png");

    public Person(String name) {

        this.name = name;
        this.rectHistory = new LinkedList<>();

        lerper = new Lerper();
        lerpPosition = 0;
        lerpTarget = 0;

    }

    public void addRect(Rect rect_) {

        if (rectHistory.size() > 15) {
            rectHistory.removeLast();
        }

        rectHistory.addFirst(rect_);

    }

    public void showGauge() {

        Mat gauge = Imgcodecs.imread("data/NUMBERLINE.png");
        Imgproc.resize(gauge, gauge, new Size(300, 800));

        Imgproc.circle(gauge, new Point(gauge.width() / 2, lerpPosition), 25, new Scalar(255, 0, 0), 2);

        Imgproc.circle(gauge, new Point(gauge.width() / 5, lerpTarget), 5, new Scalar(0, 0, 255, 1),
                Imgproc.FILLED);
        Imgproc.line(gauge, new Point(gauge.width() / 5, lerpTarget), new Point(gauge.width() / 2, lerpTarget),
                new Scalar(0, 0, 255, 1));

        Imgproc.putText(gauge, String.format("Avg: %.2f", averageMovement),
                new Point(gauge.width() / 7, gauge.height() / 4), 1,
                1, new Scalar(0, 0, 255));
        Imgproc.putText(gauge, String.format("LERP: %.0f%%", lerpPercent),
                new Point(gauge.width() / 7, gauge.height() / 3.5),
                1, 1, new Scalar(255, 0, 0));

        HighGui.imshow(name, gauge);
        HighGui.waitKey(1);

    }

    // A threaded runner that updates the player's average movement
    // and LERP status

    public void updateMovement() {

        if (rectHistory != null) {

            try {

                // AVERAGE MOVEMENT

                averageMovement = 0;
                int index = 0;

                for (Rect rect : rectHistory) {

                    averageMovement += rect.area();

                    index++;
                }

                averageMovement /= index;
                averageMovement /= rectHistory.getLast().area();

                // LERP

                Imgproc.resize(gauge, gauge, new Size(300, 800));

                lerpTarget = averageMovement * gauge.height() / 2;

                lerpPosition = lerper.Lerp(lerpPosition, lerpTarget);
                lerpPercent = Math.abs(1 - (lerpPosition * 2 / gauge.height())) * 100;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<Rect> getRectHistory() {
        return this.rectHistory;
    }

    public void setRectHistory(LinkedList<Rect> rectHistory) {
        this.rectHistory = rectHistory;
    }

    public double getAverageMovement() {
        return this.averageMovement;
    }

    public void setAverageMovement(double averageMovement) {
        this.averageMovement = averageMovement;
    }

    public Lerper getLerper() {
        return this.lerper;
    }

    public void setLerper(Lerper lerper) {
        this.lerper = lerper;
    }

    public double getLerpTarget() {
        return this.lerpTarget;
    }

    public void setLerpTarget(double lerpTarget) {
        this.lerpTarget = lerpTarget;
    }

    public double getLerpPosition() {
        return this.lerpPosition;
    }

    public void setLerpPosition(double lerpPosition) {
        this.lerpPosition = lerpPosition;
    }

    public double getLerpPercent() {
        return this.lerpPercent;
    }

    public void setLerpPercent(double lerpPercent) {
        this.lerpPercent = lerpPercent;
    }

    public Mat getGauge() {
        return this.gauge;
    }

    public void setGauge(Mat gauge) {
        this.gauge = gauge;
    }

}
