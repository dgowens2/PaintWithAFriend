package sample;

/**
 * Created by DTG2 on 09/06/16.
 */
public class Stroke {
    double xPlane;
    double yPlane;
    double strokeSize;


    public Stroke() {
    }

    public Stroke(double xPlane, double yPlane, double strokeSize) {
        this.xPlane = xPlane;
        this.yPlane = yPlane;
        this.strokeSize = strokeSize;
    }

    public void printInfo() {
        System.out.println("Stroke Information " + xPlane + " " + yPlane + " " + strokeSize);
    }

    public double getxPlane() {
        return xPlane;
    }

    public void setxPlane(double xPlane) {
        this.xPlane = xPlane;
    }

    public double getyPlane() {
        return yPlane;
    }

    public void setyPlane(double yPlane) {
        this.yPlane = yPlane;
    }

    public double getStrokeSize() {
        return strokeSize;
    }

    public void setStrokeSize(double strokeSize) {
        this.strokeSize = strokeSize;
    }
}
