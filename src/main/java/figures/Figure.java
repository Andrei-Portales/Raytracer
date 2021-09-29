package figures;

public abstract class Figure {

    protected Material material;

    public abstract Intersect rayIntersect(Double[] origin, Double[] direction);

    public Material getMaterial() {
        return material;
    }

}
