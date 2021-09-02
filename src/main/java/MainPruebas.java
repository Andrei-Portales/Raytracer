import figures.Material;
import figures.Sphere;

import java.awt.*;

public class MainPruebas {

    public static void main(String... args) {

        int width = 1512;
        int height = 1512;

        Raytracer rtx = new Raytracer(width, height);

        Material brick = new Material(new Color(204,63,63));
        Material stone = new Material(new Color(102,102,102));
        Material grass = new Material(new Color(102,255,0));
        Material wood = new Material(new Color(127,127,25));


        rtx.addFigure(new Sphere(new Double[]{3.0, 0.0, -10.0}, 2, brick));
        rtx.addFigure(new Sphere(new Double[]{0.0, 0.0, -14.0}, 4, stone));
        rtx.addFigure(new Sphere(new Double[]{3.0, 2.0, -8.0}, 0.5, grass));
        rtx.addFigure(new Sphere(new Double[]{-4.0, 2.0, -6.0}, 1, wood));

        rtx.glRender();

        rtx.finish("output.bmp");


    }
}
