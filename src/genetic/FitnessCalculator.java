package genetic;


import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import core.Properties;

import java.io.File;
import java.util.ArrayList;

public class FitnessCalculator {

    public static File file = new File(Properties.FILENAME);
    public static Image image = new Image(file.toURI().toString());

    public static int calculateFitness(Genome genome) {
        return comparedToImageFitness(genome);
    }

    public static int comparedToImageFitness(Genome genome) {
        int oldFitnessInChangedPlaces = pixelSimilarityFitnessForRects(
                genome.getChangedRectangles(), genome);
        genome.prepareImage();
        int newFitnessInChangedPlaces = pixelSimilarityFitnessForRects(
                genome.getChangedRectangles(), genome);
        int fitnessDiff = newFitnessInChangedPlaces - oldFitnessInChangedPlaces;
        int newFitness = genome.getFitness() + fitnessDiff;
        genome.setFitness(newFitness);
        genome.cleanRectangles();
        return newFitness;
    }

    private static int pixelSimilarityFitnessForRects(ArrayList<Dot> rects, Genome genome) {
//        int fitness = Properties.WIDTH * Properties.HEIGHT * 100;
//        int fitness = genome.getFitness();
        int fitness = 0;
        final PixelReader reader = image.getPixelReader();

        for (Dot r : rects) {
            for (int x = r.getX(); x < r.getX() + r.getWidth(); x++) {
                for (int y = r.getY(); y < r.getY() + r.getWidth(); y++) {
                    Color color = reader.getColor(x, y);
                    int opacityDif = Math.abs((int)(color.getBrightness()*100) - (int)(genome.createdImage[x][y]*100));
                    fitness -= opacityDif;
                }
            }
        }
        return fitness;
    }


}
