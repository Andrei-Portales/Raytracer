package figures;

import java.awt.*;
import java.util.function.DoubleToLongFunction;

public class PointLight {
    private Double[] position;
    private double intensity;
    private Color color;

    public PointLight(Double[] position, double intensity, Color color) {
        this.position = position;
        this.position[1] = -1 * this.position[1];
        this.intensity = intensity;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public double getIntensity() {
        return intensity;
    }

    public Double[] getPosition() {
        return position;
    }
}
