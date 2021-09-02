package figures;

public abstract class Figure {

    protected Material material;

    public Intersect rayIntersect(Double[] origin, Double[] direction){
        return null;
    }

    public Material getMaterial() {
        return material;
    }

}
