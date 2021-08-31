import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Raytracer {
    private final Color BLACK = new Color(0, 0, 0, 255);
    private final Color WHITE = new Color(255, 255, 255, 255);
    private int width;
    private int height;
    private Color currentColor;
    private Color clearColor;
    private int vpX;
    private int vpY;
    private int vpWidth;
    private int vpHeight;
    private Color[][] pixels;
    private double[][] zBuffer;
    private Texture background;


    public Raytracer(int width, int height) {
        this.clearColor = BLACK;
        this.currentColor = WHITE;
        this.glCreateWindow(width, height);
    }

    public void setBackground(Texture background) {
        this.background = background;
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
        this.clearColor = new Color(r, g, b, 255);
    }

    public void glClear() {
        this.pixels = new Color[this.width][this.height];

        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                pixels[x][y] = clearColor;
            }
        }

        this.zBuffer = new double[this.width][this.height];
    }

    private void glColor(int r, int g, int b) {
        this.currentColor = new Color(r, g, b, 255);
    }

    public void glClearBackground() {
        if (this.background != null) {
            for (int x = 0; x < this.width; x++) {
                for (int y = 0; y < this.height; y++) {
                    float tx = ((float) x - (float) this.vpX) / (float) this.vpWidth;
                    float ty = ((float) y - (float) this.vpY) / (float) this.vpHeight;
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
