import figures.*;

import java.awt.*;

public class MainPruebas {

    public static void main(String... args) {

        int width = 2000;
        int height = 1000;

        Raytracer rtx = new Raytracer(width, height);

        rtx.glClearColor(51, 153, 204);
        rtx.glClear();

        rtx.setEnvMap(new EnvMap("textures/envmap_playa.bmp"));

        // Materias
        Material brick = new Material(new Color(204, 63, 63), 32,1,  Material.REFLECTIVE);
        Material stone = new Material(new Color(102, 102, 102), 64,1, Material.OPAQUE);
        Material grass = new Material(new Color(102, 255, 0), 128,1, Material.REFLECTIVE);

        Material mirror = new Material(new Color(255, 255, 255), 0,1, Material.REFLECTIVE);
        Material glass = new Material(new Color(255, 255, 255), 64,1.5, Material.TRANSPARENT);
        Material water = new Material(new Color(255, 255, 0), 64,1.33, Material.TRANSPARENT);


        rtx.setAmbientLight(new AmbientLight(0.1, null));
        rtx.setDirectionalLight(new DirectionalLight(new Double[]{1.0, -1.0, -2.0}, 0.5, null));
        rtx.addPointLight(new PointLight(new Double[]{0.0, 2.0, 0.0}, 0.5, new Color(255, 255, 255)));

//        rtx.addFigure(new Plane(new Double[]{0.0, -3.0, -40.0}, new Double[]{0.0, 2.0, 0.0}, brick));

//        rtx.addFigure(new Sphere(new Double[]{-8.0, 0.0, -20.0}, 2.5, glass));
        rtx.addFigure(new Sphere(new Double[]{8.0, 0.0, -20.0}, 6.0, water));
        rtx.addFigure(new AABB(new Double[]{-10.0, 0.0, -20.0}, new Double[]{8.0, 8.0, 8.0}, glass));


//        rtx.addFigure(new Sphere(new Double[]{-3.0, 1.0, -5.0}, 0.5, mirror));
//        rtx.addFigure(new Sphere(new Double[]{0.0, 0.0, -5.0}, 0.5, mirror));

        long t1 = System.currentTimeMillis();
        rtx.glRender();
        rtx.finish("output.bmp");
        long t2 = System.currentTimeMillis();

        System.out.println((t2 - t1) / 1000.0);

    }
}
