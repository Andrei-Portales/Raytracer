import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Texture {

    private BufferedImage image;

    public Texture(String filename) {
        this.read(filename);
    }

    public void read(String filename) {
        try {
            this.image = ImageIO.read(new File(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public Color getColor(float tx, float ty) {
        if (tx >= 0 && tx <= 1 && ty >= 0 && ty <= 1) {
            int x = (int) tx * getWidth();
            int y = (int) ty * getHeight();
            int intColor = image.getRGB(x, y);
            return new Color(intColor, false);
        } else {
            return new Color(0, false);
        }
    }
}
