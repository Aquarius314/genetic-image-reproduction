package core;


import javafx.scene.paint.Color;

public class Properties {

    public static final double STEP = .00;
    public static final String MODE = "SELECTION";

    public static final int WIDTH = 400;
    public static final int HEIGHT = 400;
    public static final String FILENAME = "src/e400.png";
    public static final Color BACKGROUND_COLOR = new Color(1, 1, 1, 1);
    public static final Color RECT_10_COLOR = new Color(0, 0, 0, 0.2);

    public static final int SECONDS_BETWEEN_PICTURES = 30;
    public static final int SECONDS_BETWEEN_DATA_COLLECTION = 10;
    public static final int EXPERIMENT_SINGLE_CASE_TIME = 60; // in minutes

    public static int INITIAL_POPULATION_SIZE = 2;
    public static final int INITIAL_RECTANGLES_QUANTITY = 1000; // 500
    public static final int INITIAL_RECT_MIN_SIZE = 3;
    public static final int INITIAL_RECT_MAX_SIZE = 6;

    public static final int MUTATIONS_CHANCE = 20;
    public static final int MUTATIONS_DISAPPEAR_CHANCE = 30;
    public static final int MUTATIONS_SIZE = 1;

}
