import figures.*;

import java.awt.*;

public class RT2OpaqueReflectionsRefractions_Main {

    public static void main(String... args) {

        int width = 2000;
        int height = 1500;

        Raytracer rtx = new Raytracer(width, height);

        rtx.glClearColor(51, 153, 204);
        rtx.glClear();

        rtx.setEnvMap(new EnvMap("textures/envmap_playa.bmp"));

        // Materias
        Material brick = new Material(new Color(204, 63, 63), 32,1,  Material.OPAQUE);
        Material stone = new Material(new Color(102, 102, 102), 64,1, Material.OPAQUE);


        Material grass = new Material(new Color(102, 255, 0), 128,1, Material.REFLECTIVE);
        Material mirrorGold = new Material(new Color(255, 255, 0), 0,1, Material.REFLECTIVE);

        Material glass = new Material(new Color(255, 255, 255), 64,5, Material.TRANSPARENT);
        Material water = new Material(new Color(255, 255, 255), 64,1.33, Material.TRANSPARENT);


        rtx.setAmbientLight(new AmbientLight(0.1, null));
        rtx.setDirectionalLight(new DirectionalLight(new Double[]{1.0, -1.0, -2.0}, 0.5, null));
        rtx.addPointLight(new PointLight(new Double[]{0.0, 2.0, 0.0}, 0.5, new Color(255, 255, 255)));


        rtx.addFigure(new Sphere(new Double[]{-2.5, 1.3, -5.0}, 1, brick));
        rtx.addFigure(new Sphere(new Double[]{0.0, 1.3, -5.0}, 1, stone));

        rtx.addFigure(new Sphere(new Double[]{2.5, 1.3, -5.0}, 1, grass));
        rtx.addFigure(new Sphere(new Double[]{-2.5, -1.3, -5.0}, 1, mirrorGold));

        rtx.addFigure(new Sphere(new Double[]{0.0, -1.3, -5.0}, 1, glass));
        rtx.addFigure(new Sphere(new Double[]{2.5, -1.3, -5.0}, 1, water));

        long t1 = System.currentTimeMillis();
        rtx.glRender();
        rtx.finish("lab_outputs/RT2OpaqueReflectionsRefractions.bmp");
        long t2 = System.currentTimeMillis();

        System.out.println((t2 - t1) / 1000.0);

    }
}
