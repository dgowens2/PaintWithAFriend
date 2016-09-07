package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by DTG2 on 09/04/16.
 */
public class RunnableGC implements Runnable {

    private GraphicsContext gc = null;
    private Stroke stroke = null;


            public RunnableGC(GraphicsContext gc, Stroke stroke) {
            this.gc = gc;
            this.stroke = stroke;
        }

        public void run() {
            gc.setStroke(Color.color(Math.random(), Math.random(), Math.random()));
            gc.strokeOval(stroke.xPlane, stroke.yPlane, stroke.strokeSize, stroke.strokeSize);
        }

    }
