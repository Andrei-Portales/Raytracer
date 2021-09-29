package figures;

public class Intersect {
    private double distance;
    private Double[] normal;
    private Double[] point;
    private Figure sceneObject;

    public Intersect(double distance, Double[] point, Double[] normal, Figure sceneObject){
        this.distance = distance;
        this.normal = normal;
        this.point = point;
        this.sceneObject = sceneObject;
    }

    public double getDistance() {
        return distance;
    }

    public Double[] getNormal() {
        return normal;
    }

    public Double[] getPoint() {
        return point;
    }

    public Figure getSceneObject() {
        return sceneObject;
    }
}
