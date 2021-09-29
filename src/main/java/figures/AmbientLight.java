package figures;

import java.awt.*;

public class AmbientLight {
    private double strength;
    private Color color;

    public AmbientLight(double strength, Color color) {
        this.strength = strength;
        this.color = color != null ? color : new Color(255, 255, 255);
    }

    public Color getColor() {
        return color;
    }

    public double getStrength() {
        return strength;
    }
}
