package figures;

import util.AMath;

public class Plane extends Figure{

    private Double[] position;
    private Double[] normal;

    public Plane(Double[] position, Double[] normal, Material material){
        this.position = position;
        this.position[1] = this.position[1] * -1;
        this.normal = AMath.div(normal, AMath.norm(normal));
        this.material = material;
    }


    @Override
    public Intersect rayIntersect(Double[] origin, Double[] direction) {

        double denom = AMath.dot(direction, this.normal);

        if (Math.abs(denom) > 0.0001){
            double num = AMath.dot(AMath.subtract(this.position, origin), this.normal);
            double t = num / denom;

            if (t > 0){
                Double[] hit = AMath.addVectors(origin, AMath.multVectorAndEscalar(direction, t));

                return new Intersect(t, hit, this.normal, this);
            }
        }

        return null;
    }
}
