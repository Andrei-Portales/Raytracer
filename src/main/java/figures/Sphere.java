package figures;

import util.AMath;

public class Sphere extends Figure {
    private Double[] center;
    private double radius;

    public Sphere(Double[] center, double radius, Material material){
        this.center = center;
        this.radius = radius;
        this.material = material;
    }

    @Override
    public Intersect rayIntersect(Double[] origin, Double[] direction) {

        Double[] L = AMath.subtract(this.center, origin);
        double l = AMath.norm(L);

        double tca = AMath.dot(L, direction);

        double d = Math.pow(Math.pow(l, 2) - Math.pow(tca, 2), 0.5);

        if (d > this.radius){
            return null;
        }

        double thc = Math.pow(Math.pow(this.radius, 2) - Math.pow(d, 2), 0.5);
        double t0 = tca - thc;
        double t1 = tca + thc;

        if (t0 < 0){
            t0 = t1;
        }

        if (t0 < 0){
            return null;
        }

        // TODO: La esfera esta completamente detras de la camara

        // TODO: La camara esta dentro de la esfera

        return new Intersect(t0);
    }
}
