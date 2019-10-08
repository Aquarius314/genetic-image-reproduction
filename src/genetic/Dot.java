package genetic;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class Dot extends Rectangle {

    public Dot(double x, double y, double width, double height) {
        super(x, y, width, height);
        Random random = new Random();
    }

    public Dot copy() {
        return new Dot(getX(), getY(), getWidth(), getHeight());
    }
}
