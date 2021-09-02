import figures.Figure;
import figures.Intersect;
import figures.Material;
import util.AMath;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Raytracer {
    private final Color BLACK = new Color(0, 0, 0);
    private final Color WHITE = new Color(255, 255, 255);
    private int width;
    private int height;
    private Color currentColor;
    private Color clearColor;
    private int vpX;
    private int vpY;
    private int vpWidth;
    private int vpHeight;
    private Color[][] pixels;

    private Texture background;

    private Double[] camPosition;
    private int fov;

    private ArrayList<Figure> scene;


    public Raytracer(int width, int height) {
        this.clearColor = BLACK;
        this.currentColor = WHITE;
        this.glCreateWindow(width, height);

        this.camPosition = new Double[]{0.0, 0.0, 0.0};
        this.fov = 60;
    }

    public void setBackground(Texture background) {
        this.background = background;
    }

    public void setClearColor(Color clearColor) {
        this.clearColor = clearColor;
    }

    public void setCurrentColor(Color currentColor) {
        this.currentColor = currentColor;
    }

    public void addFigure(Figure figure) {
        if (scene == null) {
            scene = new ArrayList<>();
        }
        scene.add(figure);
    }

    private void glCreateWindow(int width, int height) {
        this.width = width;
        this.height = height;
        this.glClear();
        this.glViewPort(0, 0, width, height);
    }

    public void glViewPort(int x, int y, int width, int height) {

        if (x + width > this.width || y + height > this.height) return;

        this.vpX = x;
        this.vpY = y;
        this.vpWidth = width;
        this.vpHeight = height;
    }

    public void glClearColor(int r, int g, int b) {
        this.clearColor = new Color(r, g, b);
    }

    public void glClear() {
        this.pixels = new Color[this.width][this.height];

        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                this.pixels[x][y] = clearColor;
            }
        }
    }

    private void glColor(int r, int g, int b) {
        this.currentColor = new Color(r, g, b);
    }

    public void glClearBackground() {
        if (this.background != null) {
            for (int x = 0; x < this.width; x++) {
                for (int y = 0; y < this.height; y++) {
                    float tx = (x - this.vpX) / (float) this.vpWidth;
                    float ty = (y - this.vpY) / (float) this.vpHeight;
                    Color color = this.background.getColor(tx, ty);
                    this.glPoint(x, y, color);
                }
            }
        }
    }

    public void glViewPortClear(Color color) {
        for (int x = this.vpX; x < this.vpX + this.width; x++) {
            for (int y = this.vpY; y < this.vpY + this.height; y++) {
                this.glPoint(x, y, color);
            }
        }
    }

    public void glPoint(int x, int y, Color color) {

        if (x < this.vpX || x >= this.vpX + this.vpWidth || y < this.vpY || y >= this.vpY + this.vpHeight) return;

        if (x >= 0 && x <= this.width && y >= 0 && y <= this.height) {
            this.pixels[x][y] = color != null ? color : this.currentColor;
        }
    }

    public void glRender() {
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                // pasar de coordenadas de ventana a coordenadas NDC (-1 a 1)
                double Px = 2 * ((x + 0.5) / this.width) - 1;
                double Py = 2 * ((y + 0.5) / this.height) - 1;

                // Angulo de vision, asumiendo que el near plane esta a 1 unidad de la camara
                double t = Math.tan(AMath.deg2Rad(this.fov) / 2);
                double r = t * this.width / this.height;

                Px *= r;
                Py *= t;

                // La camara siempre esta viendo hacia -Z
                Double[] direction = new Double[]{Px, Py, -1.0};
                direction = AMath.div(direction, AMath.norm(direction));

                this.glPoint(x, y, this.castRay(this.camPosition, direction));
            }
        }
    }

    private Color castRay(Double[] origin, Double[] direction){

        Material material = this.sceneIntersect(origin, direction);

        if (material == null){
            return this.clearColor;
        }else{
            return material.getDiffuse();
        }

    }

    private Material sceneIntersect(Double[] origin, Double[] direction){
        Material material = null;

        double depth = Double.POSITIVE_INFINITY;

        for (Figure obj : scene){
            Intersect intersect = obj.rayIntersect(origin, direction);

            if (intersect != null){
                if (intersect.getDistance() < depth){
                    depth = intersect.getDistance();
                    material = obj.getMaterial();
                }
            }

        }

        return material;
    }

    public void finish(String filename) {
        try {
            BufferedImage bufferedImage = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < this.width; x++) {
                for (int y = 0; y < this.height; y++) {
                    bufferedImage.setRGB(x, y, pixels[x][y].getRGB());
                }
            }

            File file = new File(filename);
            ImageIO.write(bufferedImage, "BMP", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
