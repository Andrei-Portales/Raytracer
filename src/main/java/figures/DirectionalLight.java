package figures;

import util.AMath;

import java.awt.*;

public class DirectionalLight {
    private Double[] direction;
    private Color color;
    private Double intensity;

    public DirectionalLight(Double[] direction, Double intensity, Color color) {
        this.direction = direction != null ? direction : new Double[]{0.0, 1.0, 0.0};
        this.direction[1] = this.direction[1] * -1;
        this.direction = AMath.div(this.direction, AMath.norm(this.direction));
        this.intensity = intensity != null ? intensity : 1.0;
        this.color = color != null ? color : new Color(255, 255, 255);
    }

    public Color getColor() {
        return color;
    }

    public Double getIntensity() {
        return intensity;
    }

    public Double[] getDirection() {
        return direction;
    }
}
