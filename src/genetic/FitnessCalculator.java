package genetic;


import javafx.scene.SnapshotResult;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import core.Properties;
import javafx.util.Callback;

import java.io.File;

public class FitnessCalculator {

    public static File file = new File(Properties.FILENAME);
    public static Image image = readImage();
//    public static ImageView comparedImage = new ImageView(image);

    private static Image readImage() {
        Image img = new Image(file.toURI().toString());

//        final PixelReader reader = img.getPixelReader();
//        for (int x = 0; x < img.getWidth(); x++) {
//            for (int y = 0; y < img.getHeight(); y++) {
//                Color color = reader.getColor(x, y);
//                System.out.print(color.getBrightness()*100+" ");
//            }
//            System.out.println();
//        }

        return img;
    }

    public static int calculateFitness(Genome genome) {
        return comparedToImageFitness(genome);// - rectanglesNumberPenalty(genome);
//        return comparedToCircleFitness(genome);
    }

    private static int rectanglesNumberPenalty(Genome genome) {
        return genome.getRectangles().size();
    }

    public static int comparedToImageFitness(Genome genome) {
//        int fitness = 1000000000;
        double[][] convertedGenome = convertGenome(genome);

        return pixelSimilarityFitness(convertedGenome, image);
//        return fitness;
    }

    private static double[][] convertGenome(Genome genome) {
        double[][] result = new double[Properties.WIDTH][Properties.HEIGHT];
        for (Dot d : genome.getRectangles()) {
            int x = (int)d.getX();
            int y = (int)d.getY();
            int width = (int)d.getWidth();
            int height = (int)d.getHeight();
            for (int i = x; i < x + width && i < Properties.WIDTH; i++) {
                for (int j = y; j < y + height && j < Properties.HEIGHT; j++) {
                    result[i][j] += 0.2;
                }
            }
        }
        return result;
    }

    public static int comparedToCircleFitness(Genome genome) {
        int fitness = 1000;
        for (Rectangle r : genome.getRectangles()) {
            if (distance(r, 200, 200) < 120 && distance(r, 200, 200) > 60) {
                fitness += 1;
            } else {
                fitness -= 1;
            }
        }
        return fitness;
    }

    public static int distance(Rectangle r, int x, int y) {
        return (int)Math.sqrt(Math.pow((r.getX() + r.getWidth()/2) - x, 2) + Math.pow((r.getY() + r.getHeight()/2) - y, 2));
    }

    public static int naiveFitness(Genome genome) {
        int fitness = 0;
        for (Rectangle r : genome.getRectangles()) {
            fitness += r.getWidth()*r.getHeight();
        }
        return fitness;
    }

    private static int pixelSimilarityFitness(double[][] genome, Image img) {
        int fitness = 100000000 - 94700000 + 5310000 - 7000000;
        final PixelReader reader = img.getPixelReader();
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                Color color = reader.getColor(x, y);
                int opacityDif = Math.abs((int)(color.getBrightness()*100) - (int)(genome[x][y]*100));
                fitness -= opacityDif;
            }
        }
        return fitness;
    }


}
