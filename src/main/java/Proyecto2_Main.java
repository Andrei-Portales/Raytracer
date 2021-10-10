import figures.*;

import java.awt.*;

public class Proyecto2_Main {

    private static Material sun = new Material(new Color(255, 255, 255), 64, 1, new Texture("proyecto2_resources/sun_texture.png"), Material.OPAQUE);
    private static Material ground = new Material(new Color(255, 255, 255), 128, 5, new Texture("proyecto2_resources/ground.jpg"), Material.OPAQUE);
    private static Material wood = new Material(new Color(255, 255, 255), 128, 5, new Texture("proyecto2_resources/wood.jpg"), Material.OPAQUE);
    private static Material leave = new Material(new Color(255, 255, 255), 128, 5, new Texture("proyecto2_resources/leave.jpg"), Material.OPAQUE);
    private static Material basketball = new Material(new Color(255, 255, 255), 128, 5, new Texture("proyecto2_resources/basketball.jpg"), Material.OPAQUE);
    private static Material futbol = new Material(new Color(255, 255, 255), 128, 5, new Texture("proyecto2_resources/futbol.jpg"), Material.OPAQUE);
    private static Material mirror = new Material(new Color(255, 255, 255), 128, 10, Material.REFLECTIVE);


    // Steve materials
    private static Material steveFace = new Material(new Color(255, 255, 255), 128, 5, new Texture("proyecto2_resources/steve_face.png"), Material.OPAQUE);
    private static Material steveShirt = new Material(new Color(255, 255, 255), 128, 5, new Texture("proyecto2_resources/steve_shirt.jpg"), Material.OPAQUE);
    private static Material stevePants = new Material(new Color(255, 255, 255), 128, 5, new Texture("proyecto2_resources/steve_pants.png"), Material.OPAQUE);
    private static Material steveShoes = new Material(new Color(150, 150, 150), 128, 5, null, Material.OPAQUE);
    private static Material steveHands = new Material(new Color(155, 103, 60), 128, 5, null, Material.OPAQUE);


    public static void main(String... args) {

        int width = 1500;
        int height = 1000;

        Raytracer rtx = new Raytracer(width, height);

        rtx.setEnvMap(new EnvMap("proyecto2_resources/4k_minecraft.jpg"));

        // Materiales


//        Material earth = new Material(new Color(255, 255, 255), 1, 1, new Texture("textures/earthDay.bmp"), Material.OPAQUE);
//        Material box = new Material(new Color(255, 255, 255), 1, 1, new Texture("textures/box.bmp"), Material.OPAQUE);


        // Luces
        rtx.setAmbientLight(new AmbientLight(0.5, null));
        rtx.setDirectionalLight(new DirectionalLight(new Double[]{2.0, 2.0, -2.0}, 1.0, new Color(255, 255, 255)));
        rtx.addPointLight(new PointLight(new Double[]{0.0, 2.0, 0.0}, 0.5, new Color(255, 255, 255)));


//        // Figuras
        rtx.addFigure(new Sphere(new Double[]{-5.5, 3.5, -8.0}, 0.8, sun));

        rtx.addFigure(new Triangle(new Double[]{5.8, -3.0, -15.0}, 4.0, mirror));
        rtx.addFigure(new Triangle(new Double[]{-5.8, -3.0, -15.0}, 4.0, mirror));

        // Arboles
        tree(rtx, 0.8, new Double[]{6.2, -2.5, -10.0});
        tree(rtx, 0.8, new Double[]{-6.2, -2.5, -10.0});

        rtx.addFigure(new Sphere(new Double[]{-0.9, -2.9, -10.0}, 0.4, basketball));
        rtx.addFigure(new Sphere(new Double[]{0.9, -2.9, -10.0}, 0.4, futbol));
        rtx.addFigure(new Sphere(new Double[]{0.0, 2.0, -8.0}, 1.7, mirror));

        //Steve
        rtx.addFigure(new AABB(new Double[]{0.0, -1.0, -10.0}, new Double[]{1.0, 1.0, 1.0}, steveFace));
        rtx.addFigure(new AABB(new Double[]{0.0, -2.0, -10.0}, new Double[]{1.0, 1.3, 1.0}, steveShirt));
        rtx.addFigure(new AABB(new Double[]{0.0, -3.0, -10.0}, new Double[]{1.0, 1.3, 1.0}, stevePants));
        rtx.addFigure(new AABB(new Double[]{0.0, -3.7, -10.0}, new Double[]{1.0, 0.3, 1.0}, steveShoes));

        rtx.addFigure(new AABB(new Double[]{0.7, -1.6, -10.0}, new Double[]{0.5, 0.5, 1.0}, steveShirt));
        rtx.addFigure(new AABB(new Double[]{-0.7, -1.6, -10.0}, new Double[]{0.5, 0.5, 1.0}, steveShirt));
        rtx.addFigure(new AABB(new Double[]{-0.7, -2.1, -10.0}, new Double[]{0.5, 0.6, 1.0}, steveHands));
        rtx.addFigure(new AABB(new Double[]{0.7, -2.1, -10.0}, new Double[]{0.5, 0.6, 1.0}, steveHands));







        long t1 = System.currentTimeMillis();
        rtx.glRender();
        rtx.finish("proyecto2_outputs/proyecto_output.bmp");
        long t2 = System.currentTimeMillis();

        System.out.println((t2 - t1) / 1000.0);

    }

    private static void tree(Raytracer rtx, double size, Double[] position) {
        rtx.addFigure(new AABB(new Double[]{position[0], position[1] - size * 2, position[2]}, new Double[]{size, size, size}, ground));
        rtx.addFigure(new AABB(new Double[]{position[0], position[1] - size, position[2]}, new Double[]{size, size, size}, wood));
        rtx.addFigure(new AABB(new Double[]{position[0], position[1], position[2]}, new Double[]{size, size, size}, wood));
        rtx.addFigure(new AABB(new Double[]{position[0], position[1] + size, position[2]}, new Double[]{size, size, size}, leave));
        rtx.addFigure(new AABB(new Double[]{position[0], position[1] + size * 2, position[2]}, new Double[]{size, size, size}, leave));

        rtx.addFigure(new AABB(new Double[]{position[0] - size, position[1] + size, position[2]}, new Double[]{size, size, size}, leave));
        rtx.addFigure(new AABB(new Double[]{position[0] + size, position[1] + size, position[2]}, new Double[]{size, size, size}, leave));
    }
}