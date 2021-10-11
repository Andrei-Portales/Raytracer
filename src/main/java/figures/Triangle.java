package figures;

import util.AMath;

public class Triangle extends Figure {

    private Double[] v0;
    private Double[] v1;
    private Double[] v2;
    private Double[] center;


    public Triangle(Double[] center, double size, Material material) {
        this.center = center;
        center[1] = center[1] * -1;
        this.material = material;

        double d = size / 2.0;

        v0 = new Double[]{center[0], center[1] + d, center[2]};
        v1 = new Double[]{center[0] - d, center[1] - d, center[2]};
        v2 = new Double[]{center[0] + d, center[1] - d, center[2]};

    }


    @Override
    public Intersect rayIntersect(Double[] origin, Double[] direction) {

        Double[] v0v1 = AMath.subtract(v1, v0);
        Double[] v0v2 = AMath.subtract(v2, v0);

        Double[] N = AMath.cross(v0v1, v0v2);

        double NdotRayDirection = AMath.dot(N, direction);

        if (Math.abs(NdotRayDirection) < 0.001) {
            return null;
        }

        double d = AMath.dot(N, v0);

        double t = (AMath.dot(N, origin) + d) / NdotRayDirection;

        if (t < 0) return null;

        Double[] P = AMath.multVectors(AMath.addVectorAndEscalar(origin, t), direction);

        Double[] C;

        Double[] edge0 = AMath.subtract(v1, v0);
        Double[] vp0 = AMath.subtract(P, v0);
        C = AMath.cross(edge0, vp0);
        if (AMath.dot(N, C) < 0) return null;


        Double[] edge1 = AMath.subtract(v2, v1);
        Double[] vp1 = AMath.subtract(P, v1);
        C = AMath.cross(edge1, vp1);
        if (AMath.dot(N, C) < 0) return null;


        Double[] edge2 = AMath.subtract(v0, v2);
        Double[] vp2 = AMath.subtract(P, v2);
        C = AMath.cross(edge2, vp2);
        if (AMath.dot(N, C) < 0) return null;


        Double[] hit = AMath.addVectors(origin, AMath.multVectorAndEscalar(direction, t));

        return new Intersect(d, hit, N, null, this);
    }
}
