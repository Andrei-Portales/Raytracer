package figures;

import util.AMath;

public class AABB extends Figure {

    private Double[] position;
    private Double[] size;
    private Plane[] planes;
    private Double[] boundsMin = new Double[]{0.0, 0.0, 0.0};
    private Double[] boundsMax = new Double[]{0.0, 0.0, 0.0};

    public AABB(Double[] position, Double[] size, Material material) {
        this.position = position;
        this.size = size;
        this.material = material;
        this.planes = new Plane[6];

        double halfSizeX = size[0] / 2;
        double halfSizeY = size[1] / 2;
        double halfSizeZ = size[2] / 2;


        // sides
        this.planes[0] = new Plane(AMath.addVectors(position, new Double[]{halfSizeX, 0.0, 0.0}), new Double[]{1.0, 0.0, 0.0}, material);
        this.planes[1] = new Plane(AMath.addVectors(position, new Double[]{-halfSizeX, 0.0, 0.0}), new Double[]{-1.0, 0.0, 0.0}, material);

        // up and down
        this.planes[4] = new Plane(AMath.addVectors(position, new Double[]{0.0, halfSizeY, 0.0}), new Double[]{0.0, 1.0, 0.0}, material);
        this.planes[5] = new Plane(AMath.addVectors(position, new Double[]{0.0, -halfSizeY, 0.0}), new Double[]{0.0, -1.0, 0.0}, material);

        // front and back
        this.planes[2] = new Plane(AMath.addVectors(position, new Double[]{0.0, 0.0, halfSizeZ}), new Double[]{0.0, 0.0, 1.0}, material);
        this.planes[3] = new Plane(AMath.addVectors(position, new Double[]{0.0, 0.0, -halfSizeZ}), new Double[]{0.0, 0.0, -1.0}, material);

        double epsilon = 0.001;

        for (int i = 0; i < 3; i++) {
            this.boundsMin[i] = this.position[i] - (epsilon + this.size[i] / 2);
            this.boundsMax[i] = this.position[i] + (epsilon + this.size[i] / 2);
        }
    }


    @Override
    public Intersect rayIntersect(Double[] origin, Double[] direction) {

        Intersect intersect = null;
        double t = Double.POSITIVE_INFINITY;

        for (Plane plane : this.planes) {
            Intersect planeInter = plane.rayIntersect(origin, direction);

            if (planeInter != null) {
                if (planeInter.getPoint()[0] >= this.boundsMin[0] && planeInter.getPoint()[0] <= this.boundsMax[0]) {
                    if (planeInter.getPoint()[1] >= this.boundsMin[1] && planeInter.getPoint()[1] <= this.boundsMax[1]) {
                        if (planeInter.getPoint()[2] >= this.boundsMin[2] && planeInter.getPoint()[2] <= this.boundsMax[2]) {
                            if (planeInter.getDistance() < t) {
                                t = planeInter.getDistance();
                                intersect = planeInter;
                            }
                        }
                    }
                }
            }
        }

        if (intersect == null) {
            return null;
        }

        return new Intersect(intersect.getDistance(), intersect.getPoint(), intersect.getNormal(), this);
    }
}
