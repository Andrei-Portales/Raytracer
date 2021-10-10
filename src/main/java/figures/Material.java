package figures;


import java.awt.*;

public class Material {
    public static final int OPAQUE = 0;
    public static final int REFLECTIVE = 1;
    public static final int TRANSPARENT = 2;
    private Color diffuse;
    private int spec;
    private int matType;
    private double ior;
    private Texture texture;

    public Material(Color diffuse, int spec, double ior, Integer matType) {
        this.diffuse = diffuse;
        this.spec = spec;
        this.matType = matType != null ? matType : OPAQUE;
        this.ior = ior;
    }

    public Material(Color diffuse, int spec, double ior, Texture texture, Integer matType) {
        this.diffuse = diffuse;
        this.spec = spec;
        this.matType = matType != null ? matType : OPAQUE;
        this.ior = ior;
        this.texture = texture;
    }

    public Material() {
        this.diffuse = new Color(255, 255, 255);
        this.spec = 0;
    }

    public Color getDiffuse() {
        return diffuse;
    }

    public int getSpec() {
        return spec;
    }

    public double getIor() {
        return ior;
    }

    public int getMatType() {
        return matType;
    }

    public Texture getTexture() {
        return texture;
    }
}
