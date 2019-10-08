package genetic;




import core.Properties;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Genome {

    private static Random rnd = new Random();
    private ArrayList<Dot> rectangles;
    private int fitness;
    private FitnessCalculator calculator = new FitnessCalculator();

    public Genome() {
        generateInitialRectangles();
    }

    public Genome copy() {
        Genome copy = new Genome();
        copy.rectangles.clear();
        for (Dot r : rectangles) {
            copy.rectangles.add(new Dot(r.getX(), r.getY(), r.getWidth(), r.getHeight()));
        }
        copy.setFitness(fitness);
        return copy;
    }

    public void mutate() {
        if (shouldChangeSize()) {
            if (rnd.nextBoolean()) {
                // create new one
                rectangles.add(randomRectangle());
            } else {
                // delete one
                if (rectangles.size() > 10) {
                    Collections.shuffle(rectangles);
                    rectangles.remove(0);
                }
            }
        }
        for (Rectangle r : rectangles) {
            if (shouldMutate()) {
//                double width = r.getWidth();
//                double height = r.getHeight();
                double x = r.getX();
                double y = r.getY();

//                r.setWidth(mutateFeature(width));
//                r.setHeight(mutateFeature(height));
                r.setX(mutateFeature(x));
                r.setY(mutateFeature(y));
            }
        }
    }

    private double mutateFeature(double feature) {
        int mutation = randomInt() % Properties.MUTATIONS_SIZE;
        double result;
        if (rnd.nextBoolean()) {
            result = feature + mutation;
        } else {
            result = feature - mutation;
        }
        result = Math.max(1, result);
        result = Math.min(Properties.HEIGHT - 5, result);
        return result;
    }

    private boolean shouldMutate() {
        return randomInt() % 100 < Properties.MUTATIONS_CHANCE;
    }

    private boolean shouldChangeSize() {
        return randomInt() % 100 < Properties.MUTATIONS_DISAPPEAR_CHANCE;
    }

    private void generateInitialRectangles() {
        rectangles = new ArrayList<>();

        Random rnd = new Random();
        for (int i = 0; i < Properties.INITIAL_RECTANGLES_QUANTITY; i++) {

            rectangles.add(randomRectangle());
        }
    }

    private Dot randomRectangle() {
//        int width = chooseRandomSize(rnd);
//        int height = chooseRandomSize(rnd);
        int width = 5;
        int height = 5;
        int x = randomInt() % (Properties.WIDTH - width);
        int y = randomInt() % (Properties.HEIGHT - height);

        return new Dot(x, y, width, height);
    }

    private int randomInt() {
        return Math.abs(rnd.nextInt());
    }

    private int chooseRandomSize(Random rnd) {
        return randomInt() % (Properties.INITIAL_RECT_MAX_SIZE - Properties.INITIAL_RECT_MIN_SIZE)
                + Properties.INITIAL_RECT_MIN_SIZE;
    }

    public ArrayList<Dot> getRectangles() {
        return rectangles;
    }

    public void setRectangles(ArrayList<Dot> rectangles) {
        this.rectangles = rectangles;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public FitnessCalculator getFitnessCalculator() {
        return calculator;
    }
}
