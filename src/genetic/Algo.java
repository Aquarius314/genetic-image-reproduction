package genetic;


import core.Properties;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import static genetic.FitnessCalculator.image;

public class Algo {

    private Genome bestGenome = new Genome();
    private ArrayList<Genome> population;
    private ArrayList<Genome> oldPopulation;
    public int iterations = 0;
    public long startingTime = System.currentTimeMillis();
    public long dataTime = System.currentTimeMillis();
    public long photoTime = System.currentTimeMillis();
    public int photoCounter = 0;
    private BufferedWriter writer;
    public int nr = 0;
    public int experimentCounter = 1;

    public Canvas canvas;

    Random rnd = new Random();

    public Algo() {
        generateInitialPopulation();
        try {
            writer = new BufferedWriter(new FileWriter("data-pop" + Properties.INITIAL_POPULATION_SIZE + ".txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void iterate() {
        iterations++;
        oldPopulation = copyPopulation();
        mutations();
//        crossovers();
        calculateFitnesses();
        selection();

        if (System.currentTimeMillis() - dataTime > 1000 * Properties.SECONDS_BETWEEN_DATA_COLLECTION) {
            dataTime = System.currentTimeMillis();
            collectData();
        }

        if (limitMinutes(Properties.EXPERIMENT_SINGLE_CASE_TIME)) {
            experimentCounter++;
            System.out.println("Iter:" + iterations + " Fit:" + bestGenome.getFitness()/100 + " Pop:" + Properties.INITIAL_POPULATION_SIZE);
            Properties.INITIAL_POPULATION_SIZE *= 2;
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            reset();
        }
    }

    private void collectData() {
        try {
            writer.write(bestGenome.getFitness()/100 + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean limitMinutes(int minutes) {
        return (System.currentTimeMillis() - startingTime > minutes*60*1000);
    }

    private boolean limitIterations(int it) {
        return iterations >= it;
    }

    private void reset() {
        try {
            writer = new BufferedWriter(new FileWriter("data-pop" + Properties.INITIAL_POPULATION_SIZE + ".txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        generateInitialPopulation();
        oldPopulation.clear();
        bestGenome = new Genome();
        iterations = 0;
        startingTime = System.currentTimeMillis();
        photoTime = System.currentTimeMillis();
        photoCounter = 0;
        nr = 0;
    }

    private ArrayList<Genome> copyPopulation() {
        ArrayList<Genome> copy = new ArrayList<>();
        for (Genome g : population) {
            copy.add(g.copy());
        }
        return copy;
    }

    private void generateInitialPopulation() {
        population = new ArrayList<>();
        for (int i = 0; i < Properties.INITIAL_POPULATION_SIZE; i++) {
            population.add(new Genome());
        }
    }

    private void mutations() {
        for (Genome g : population) {
            g.mutate();
        }
    }

    private void crossovers() {
//        for (int i = 0; i < population.size() - 1; i++) {
//            int randIndex1 = randomInt() % population.size();
//            int randIndex2 = randomInt() % population.size();
//            Dot d1 = population.get(i).getRectangles().get(randIndex1);
//            Dot d2 = population.get(i+1).getRectangles().get(randIndex2);
//
//            population.get(i).getRectangles().remove(randIndex1);
//            population.get(i+1).getRectangles().remove(randIndex2);
//
//            population.get(i).getRectangles().add(d2);
//            population.get(i+1).getRectangles().add(d1);
//        }
        for (int g = 0; g < population.size() - 1; g += 2) {
            Genome genomeA = population.get(g);
            Genome genomeB = population.get(g + 1);
            // swap random n rectangles with another genome
            int swapSize = randomInt()%4 + 3; // 3 - 6
            ArrayList<Dot> swappedDots = new ArrayList<>();
            // select random rects
            for (int i = 0; i < swapSize; i++) {
                int randomIndex = randomInt()%genomeA.getRectangles().size();
                swappedDots.add(genomeA.getRectangles().get(randomIndex).copy());
            }
            // prepare some space for them
            for (int i = 0; i < swapSize; i++) {
                int randomIndex = randomInt()%genomeB.getRectangles().size();
                genomeB.getRectangles().remove(randomIndex);
            }
            // add rects
            genomeB.getRectangles().addAll(swappedDots);
        }
    }

    private int randomInt() {
        return Math.abs(rnd.nextInt());
    }

    private void calculateFitnesses() {
        int bestFitness = -1000000000;
        for (Genome g : population) {
            int fitness = FitnessCalculator.calculateFitness(g);
            if (fitness > bestFitness) {
                bestFitness = fitness;
                bestGenome = g;
            }
        }
    }

    private void selection() {
        population.addAll(oldPopulation);
        population.sort(Comparator.comparingInt(Genome::getFitness));
        int populationSize = population.size();
        for (int i = 0; i < populationSize / 2; i++) {
            population.remove(0);
        }
        Collections.shuffle(population);
    }

    public Genome getBestGenome() {
        return bestGenome;
    }

    public void setBestGenome(Genome bestGenome) {
        this.bestGenome = bestGenome;
    }
}
