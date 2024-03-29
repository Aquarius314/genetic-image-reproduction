package genetic;




import core.Properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Genome {

    private static Random rnd = new Random();
    private ArrayList<Dot> freshRectangles;
    private ArrayList<Dot> rectangles;
    private ArrayList<Dot> deadRectangles;
    private int fitness = Properties.WIDTH * Properties.HEIGHT * 100;
    double[][] createdImage = new double[Properties.WIDTH][Properties.HEIGHT];

    public Genome() {
        generateInitialRectangles();
    }

    public Genome copy() {
        Genome copy = new Genome();

        copy.freshRectangles = new ArrayList<>();
        copy.rectangles = copyRectsCollection(this.rectangles);
        copy.deadRectangles = new ArrayList<>();

        copy.rectangles.addAll(freshRectangles);
        copy.rectangles.removeAll(deadRectangles); // ALERT zmieniać to czy nie?

        copy.setFitness(fitness);
        copy.createdImage = this.createdImage.clone();
        return copy;
    }

    public void prepareImage() {
//        double[][] result = new double[Properties.WIDTH][Properties.HEIGHT];
        applyRects(freshRectangles, 0.2);
        applyRects(deadRectangles, -0.2);
//        return result;
    }

    public void cleanRectangles() {
        rectangles.addAll(freshRectangles);
        freshRectangles.clear();
        rectangles.removeAll(deadRectangles);
        deadRectangles.clear();
    }

    private void applyRects(ArrayList<Dot> rects, double value) {
        for (Dot d : rects) {
            int x = d.getX();
            int y = d.getY();
            int width = d.getWidth();
            int height = d.getHeight();
            for (int i = x; i < x + width && i < Properties.WIDTH; i++) {
                for (int j = y; j < y + height && j < Properties.HEIGHT; j++) {
                    createdImage[i][j] += value;
                    if (createdImage[i][j] < 0.0) {
                        createdImage[i][j] = 0;
                    } else if (createdImage[i][j] > 1.0) {
                        createdImage[i][j] = 1;
                    }
                }
            }
        }
    }

    private ArrayList<Dot> copyRectsCollection(ArrayList<Dot> rects) {
        ArrayList<Dot> copy = new ArrayList<>();
        for (Dot r : rects) {
            copy.add(r.copy());
        }
        return copy;
    }

    private void mutate1() {
        // standard, good old mutations (rare, 1 rect +/-)
        if (shouldChangeSize(1)) {
            if (rnd.nextBoolean()) {
                // create new one
                freshRectangles.add(randomRectangle());
            } else {
                // delete one
                if (rectangles.size() > 10) {
                    Collections.shuffle(rectangles);
                    deadRectangles.add(rectangles.get(0));
                }
            }
        }
    }

    private void mutate2() {
        // standard but 1-1% rects +/-
        if (shouldChangeSize(1)) {
            int onePercent = Math.max(rectangles.size()/100, 1);
            int quantity = randomInt() % onePercent;
            if (rnd.nextBoolean()) {
                for (int i = 0; i < quantity; i++) {
                    freshRectangles.add(randomRectangle());
                }
            } else {
                for (int i = 0; i < quantity; i++) {
                    if (rectangles.size() - deadRectangles.size() > 10) {
                        Collections.shuffle(rectangles);
                        deadRectangles.add(rectangles.get(0));
                    }
                }
            }
        }
    }

    private void mutate3() {
        // standard but higher probability of mutations
        if (shouldChangeSize(2)) {
            if (rnd.nextBoolean()) {
                // create new one
                freshRectangles.add(randomRectangle());
            } else {
                // delete one
                if (rectangles.size() > 10) {
                    Collections.shuffle(rectangles);
                    deadRectangles.add(rectangles.get(0));
                }
            }
        }
    }

    private void mutate4() {
        // no mutations at all!
    }

    public void mutate() {
        switch (Properties.MUTATIONS_MODE) {
            case 1:
                mutate1();
                break;
            case 2:
                mutate2();
                break;
            case 3:
                mutate3();
                break;
            case 4:
                mutate4();
                break;
                default:
                    System.out.println("Invalid MUTATIONS MODE! (" + Properties.MUTATIONS_MODE + " is not recognized)");
        }

    }

    private int mutateFeature(int feature) {
        int mutation = randomInt() % Properties.MUTATIONS_SIZE;
        int result;
        if (rnd.nextBoolean()) {
            result = feature + mutation;
        } else {
            result = feature - mutation;
        }
        result = Math.max(1, result);
        result = Math.min(Properties.HEIGHT - 5, result);
        return result;
    }

//    private boolean shouldMutate() {
//        return randomInt() % 100 < Properties.MUTATIONS_CHANCE;
//    }

    private boolean shouldChangeSize(int probabilityFactor) {
        return randomInt() % 100 < Properties.MUTATIONS_DISAPPEAR_CHANCE * probabilityFactor;
    }

    private void generateInitialRectangles() {
        freshRectangles = new ArrayList<>();
        for (int i = 0; i < Properties.INITIAL_RECTANGLES_QUANTITY; i++) {
            freshRectangles.add(randomRectangle());
        }

        rectangles = new ArrayList<>();
        deadRectangles = new ArrayList<>();
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

    public ArrayList<Dot> getChangedRectangles() {
        ArrayList<Dot> changedRectangles = new ArrayList<>();
        changedRectangles.addAll(freshRectangles);
        changedRectangles.addAll(deadRectangles);
        return changedRectangles;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }
}
