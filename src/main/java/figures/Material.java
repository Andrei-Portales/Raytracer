package figures;

import java.awt.*;

public class Material {
    private Color diffuse;

    public Material(Color diffuse) {
        this.diffuse = diffuse;
    }

    public Material() {
        this.diffuse = new Color(255, 255, 255);
    }

    public Color getDiffuse() {
        return diffuse;
    }
}
