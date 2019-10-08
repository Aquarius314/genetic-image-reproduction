package display;




import core.Properties;
import genetic.Dot;
import genetic.FitnessCalculator;
import genetic.Genome;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Displayer {

    GraphicsContext gc;
    Canvas canvas;

    public void setWindow(Stage stage) {


        //Drawing a Rectangle
        Rectangle rectangle = new Rectangle();

        //Setting the properties of the rectangle
        rectangle.setX(150.0f);
        rectangle.setY(75.0f);
        rectangle.setWidth(300.0f);
        rectangle.setHeight(150.0f);

        //Creating a Group object
        Group root = new Group(rectangle);

        //Creating a scene object
        Scene scene = new Scene(root, Properties.WIDTH, Properties.HEIGHT);

        //Setting title to the Stage
        stage.setTitle("Drawing a Rectangle");

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();
    }

    public void displayBest(GraphicsContext gc, Genome best) {
        gc.setFill(Properties.RECT_10_COLOR);
        gc.setStroke(Properties.RECT_10_COLOR);
//        for(Dot r : best.getRectangles()) {
//            gc.fillRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
//        }
        displayConnections(gc, best);
    }

    public void displayConnections(GraphicsContext gc, Genome best) {
        ArrayList<Dot> dots = best.getRectangles();
        for (int i = 0; i < best.getRectangles().size(); i++) {
            for (int j = i + 1; j < best.getRectangles().size(); j++) {
                if (rectanglesDistance(dots.get(i), dots.get(j)) < 7) {
                    double x1 = dots.get(i).getX() + dots.get(i).getWidth()/2;
                    double x2 = dots.get(j).getX() + dots.get(j).getWidth()/2;
                    double y1 = dots.get(i).getY() + dots.get(i).getHeight()/2;
                    double y2 = dots.get(j).getY() + dots.get(j).getHeight()/2;
                    gc.strokeLine(x1, y1, x2, y2);
                }
            }
        }
    }

    public double rectanglesDistance(Dot d1, Dot d2) {
        double x1 = d1.getX() + d1.getWidth()/2;
        double x2 = d2.getX() + d2.getWidth()/2;
        double y1 = d1.getY() + d1.getHeight()/2;
        double y2 = d2.getY() + d2.getHeight()/2;
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public void clear(GraphicsContext gc) {
        gc.setFill(Properties.BACKGROUND_COLOR);
        gc.fillRect(0, 0, Properties.WIDTH, Properties.HEIGHT);
    }

}
