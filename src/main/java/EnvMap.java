import util.AMath;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class EnvMap {

    private Color[][] map;
    private int width;
    private int height;

    public EnvMap(String filename) {
        this.read(filename);
    }

    private void read(String filename) {
        try {

            BufferedImage image = ImageIO.read(new File(filename));

            map = new Color[image.getHeight()][image.getWidth()];

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    map[y][x] = new Color(image.getRGB(x, y));
                }
            }

            this.height = image.getHeight();
            this.width = image.getWidth();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Color getColor(Double[] direction) {
        try {
            Double[] normDir = AMath.div(direction, AMath.norm(direction));

            int x = (int) (Math.atan2(direction[2], normDir[0]) / (2 * Math.PI) * this.width);
            int y = (int) (Math.acos(-1 * normDir[1]) / Math.PI * this.height);

            return map[Math.abs(y)][Math.abs(x)];
        } catch (Exception e) {
            return new Color(0, 0, 0);
        }
    }
}
