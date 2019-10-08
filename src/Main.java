import core.Properties;
import display.Displayer;
import genetic.Algo;
import genetic.Genome;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

public class Main extends Application {

    private static Displayer displayer;
    private static Stage stage;
    private static Canvas canvas;
    private static GraphicsContext gc;
    private static Algo algo = new Algo();
    private int fitnessCounter = 0;
    private long timer = System.currentTimeMillis();
    private long stepTimer = 0;

    public static void main(String[] args) {
        displayer = new Displayer();
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        displayer.setWindow(stage);
        stage.setTitle("INŻYNIERKA - " + algo.iterations);

        Group root = new Group();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        final long startNanoTime = System.nanoTime();

        canvas = new Canvas(Properties.WIDTH, Properties.HEIGHT);
        algo.canvas = canvas;
        root.getChildren().add(canvas);

        gc = canvas.getGraphicsContext2D();

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                calculationsLoop();
//                if (algo.getBestGenome().getFitness()/100 >= fitnessCounter * 1000) { // when fitness hits 1000, 2000, 3000 etc
//                    fitnessCounter = algo.getBestGenome().getFitness()/100000 + 1;


                if (System.currentTimeMillis() - stepTimer > 1000*Properties.SECONDS_BETWEEN_PICTURES) { // when time passes
                    stepTimer = System.currentTimeMillis();
                    saveImageToFile();
                }

                if (algo.iterations % 200 == 1) {

                    renderLoop();

                    long cur = System.currentTimeMillis();
                    String hours = Integer.toString((int)((cur - timer)/(1000*3600)));
                    String minutes = Integer.toString((int)((cur - timer)/(1000*60))%60);
                    if (Integer.parseInt(minutes) < 10) {
                        minutes = "0" + minutes;
                    }
                    String seconds = Integer.toString((int)((cur - timer)/(1000))%60);
                    if (Integer.parseInt(seconds) < 10) {
                        seconds = "0" + seconds;
                    }
                    String time = hours + ":" + minutes + ":" + seconds;
                    stage.setTitle("Iter:" + algo.iterations + " fit:" + algo.getBestGenome().getFitness()/100 + " pop:" + Properties.INITIAL_POPULATION_SIZE + " rec:" + algo.getBestGenome().getRectangles().size() + " t:" + time);

                }
            }
        }.start();
        stage.show();
    }

    private void calculationsLoop() {
        algo.iterate();
        if (algo.experimentCounter > 4) {
            System.exit(0);
        }
    }

    private void renderLoop() {
        gc.save();
        displayer.clear(gc);
        displayer.displayBest(gc, algo.getBestGenome());
        gc.restore();
    }

    private void saveImageToFile() {
        algo.nr++;
        int param = Properties.INITIAL_POPULATION_SIZE;
        File file = new File("testy/pop" + param + "/" + algo.nr + " fit: " + algo.getBestGenome().getFitness()/100 + ".png");
        try {
            WritableImage writableImage = new WritableImage(Properties.WIDTH, Properties.HEIGHT);
            canvas.snapshot(null, writableImage);
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
            ImageIO.write(renderedImage, "png", file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
