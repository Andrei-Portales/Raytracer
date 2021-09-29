package figures;

import util.AMath;

import java.awt.*;

public class Sphere extends Figure {
    private Double[] center;
    private double radius;

    public Sphere(Double[] center, double radius, Material material) {
        this.center = center;
        this.center[1] = this.center[1] * -1;
        this.radius = radius;
        this.material = material != null ? material : new Material(new Color(255, 255, 255), 0,1, null);
    }

    @Override
    public Intersect rayIntersect(Double[] origin, Double[] direction) {

        Double[] L = AMath.subtract(this.center, origin);
        double l = AMath.norm(L);

        double tca = AMath.dot(L, direction);

        double d = Math.pow(Math.pow(l, 2) - Math.pow(tca, 2), 0.5);

        if (d > this.radius) {
            return null;
        }

        double thc = Math.pow(Math.pow(this.radius, 2) - Math.pow(d, 2), 0.5);
        double t0 = tca - thc;
        double t1 = tca + thc;

        if (t0 < 0) {
            t0 = t1;
        }

        if (t0 < 0) {
            return null;
        }

        Double[] hit = AMath.addVectors(origin, AMath.multVectorAndEscalar(direction, t0));
        Double[] normal = AMath.subtract(hit, this.center);
        normal = AMath.div(normal, AMath.norm(normal));

        // TODO: La esfera esta completamente detras de la camara

        // TODO: La camara esta dentro de la esfera

        return new Intersect(t0, hit, normal, this);
    }
}
