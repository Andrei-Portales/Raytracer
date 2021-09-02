import figures.Material;
import figures.Sphere;

import java.awt.*;

public class RT1SpheresMaterials_Main {

    public static void main(String... args) {

        int width = 1500;
        int height = 1500;

        Raytracer rtx = new Raytracer(width, height);

        rtx.setClearColor(new Color(50,50,50));
        rtx.glClear();

        Material body = new Material(new Color(220,220,220));
        Material white = new Material(new Color(255,255,255));
        Material black = new Material(new Color(0,0,0));
        Material orange = new Material(new Color(255,165,0));



        // Cuerpo
        rtx.addFigure(new Sphere(new Double[]{0.0, 2.5, -10.0}, 2, body));
        rtx.addFigure(new Sphere(new Double[]{0.0, 0.0, -10.0}, 1.5, body));
        rtx.addFigure(new Sphere(new Double[]{0.0, -2.0, -10.0}, 1, body));

        // Botones
        rtx.addFigure(new Sphere(new Double[]{0.0, 2.1, -8.0}, 0.3, black));
        rtx.addFigure(new Sphere(new Double[]{0.0, 1.0, -8.0}, 0.3, black));
        rtx.addFigure(new Sphere(new Double[]{0.0, -0.2, -8.0}, 0.3, black));

        // Nariz
        rtx.addFigure(new Sphere(new Double[]{0.0, -1.6, -8.0}, 0.24, orange));

        // ojo 1
        rtx.addFigure(new Sphere(new Double[]{-0.31, -2.01, -8.0}, 0.2, white));
        rtx.addFigure(new Sphere(new Double[]{-0.3, -1.9, -7.5}, 0.1, black));

        // ojo 2
        rtx.addFigure(new Sphere(new Double[]{0.31, -2.01, -8.0}, 0.2, white));
        rtx.addFigure(new Sphere(new Double[]{0.29, -1.9, -7.5}, 0.1, black));

        // boca
        rtx.addFigure(new Sphere(new Double[]{-0.15, -1.2, -8.0}, 0.1, black));
        rtx.addFigure(new Sphere(new Double[]{0.15, -1.2, -8.0}, 0.1, black));
        rtx.addFigure(new Sphere(new Double[]{0.4, -1.3, -8.0}, 0.1, black));
        rtx.addFigure(new Sphere(new Double[]{-0.4, -1.3, -8.0}, 0.1, black));



        rtx.glRender();
        rtx.finish("lab_outputs/RT1SpheresMaterials.bmp");


    }
}
