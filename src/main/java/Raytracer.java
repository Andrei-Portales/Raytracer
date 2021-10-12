import figures.*;
import util.AMath;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Raytracer {
    private final int MAXRECURSIONDEPTH = 3;
    private final Color BLACK = new Color(0, 0, 0);
    private final Color WHITE = new Color(255, 255, 255);
    private int width;
    private int height;
    private Color currentColor;
    private Color clearColor;
    private int vpX;
    private int vpY;
    private int vpWidth;
    private int vpHeight;
    private Color[][] pixels;
    private Texture background;
    private Double[] camPosition;
    private int fov;
    private ArrayList<Figure> scene;
    private ArrayList<PointLight> pointLights;
    private AmbientLight ambientLight;
    private DirectionalLight directionalLight;
    private EnvMap envMap;


    public Raytracer(int width, int height) {
        this.clearColor = BLACK;
        this.currentColor = WHITE;
        this.glCreateWindow(width, height);

        this.camPosition = new Double[]{0.0, 0.0, 0.0};
        this.fov = 60;
    }

    public void setBackground(Texture background) {
        this.background = background;
    }

    public void setClearColor(Color clearColor) {
        this.clearColor = clearColor;
    }

    public void setCurrentColor(Color currentColor) {
        this.currentColor = currentColor;
    }

    public void setEnvMap(EnvMap envMap) {
        this.envMap = envMap;
    }

    public void setDirectionalLight(DirectionalLight directionalLight) {
        this.directionalLight = directionalLight;
    }

    public void addFigure(Figure figure) {
        if (scene == null) {
            scene = new ArrayList<>();
        }
        scene.add(figure);
    }

    public void addPointLight(PointLight pointLight) {
        if (pointLights == null) {
            pointLights = new ArrayList<>();
        }
        pointLights.add(pointLight);
    }

    public void setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
    }

    private void glCreateWindow(int width, int height) {
        this.width = width;
        this.height = height;
        this.glClear();
        this.glViewPort(0, 0, width, height);
    }

    public void glViewPort(int x, int y, int width, int height) {

        if (x + width > this.width || y + height > this.height) return;

        this.vpX = x;
        this.vpY = y;
        this.vpWidth = width;
        this.vpHeight = height;
    }

    public void glClearColor(int r, int g, int b) {
        this.clearColor = new Color(r, g, b);
    }

    public void glClear() {
        this.pixels = new Color[this.width][this.height];

        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                this.pixels[x][y] = clearColor;
            }
        }
    }

    private void glColor(int r, int g, int b) {
        this.currentColor = new Color(r, g, b);
    }

    public void glClearBackground() {
        if (this.background != null) {
            for (int x = 0; x < this.width; x++) {
                for (int y = 0; y < this.height; y++) {
                    float tx = (x - this.vpX) / (float) this.vpWidth;
                    float ty = (y - this.vpY) / (float) this.vpHeight;
                    Color color = this.background.getColor(tx, ty);
                    this.glPoint(x, y, color);
                }
            }
        }
    }

    public void glViewPortClear(Color color) {
        for (int x = this.vpX; x < this.vpX + this.width; x++) {
            for (int y = this.vpY; y < this.vpY + this.height; y++) {
                this.glPoint(x, y, color);
            }
        }
    }

    public void glPoint(int x, int y, Color color) {

        if (x < this.vpX || x >= this.vpX + this.vpWidth || y < this.vpY || y >= this.vpY + this.vpHeight) return;

        if (x >= 0 && x <= this.width && y >= 0 && y <= this.height) {
            this.pixels[x][y] = color != null ? color : this.currentColor;
        }
    }

    public void glRender() {
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                // pasar de coordenadas de ventana a coordenadas NDC (-1 a 1)
                double Px = 2 * ((x + 0.5) / this.width) - 1;
                double Py = 2 * ((y + 0.5) / this.height) - 1;

                // Angulo de vision, asumiendo que el near plane esta a 1 unidad de la camara
                double t = Math.tan(AMath.deg2Rad(this.fov) / 2);
                double r = t * this.width / this.height;

                Px *= r;
                Py *= t;

                // La camara siempre esta viendo hacia -Z
                Double[] direction = new Double[]{Px, Py, -1.0};
                direction = AMath.div(direction, AMath.norm(direction));

                this.glPoint(x, y, this.castRay(this.camPosition, direction, null, 0));
            }
        }
    }

    private Color castRay(Double[] origin, Double[] direction, Figure origObj, int recursion) {
        Intersect intersect = this.sceneIntersect(origin, direction, origObj);

        if (intersect == null || recursion >= MAXRECURSIONDEPTH) {

            if (this.envMap != null) {
                return this.envMap.getColor(direction);
            }

            return this.clearColor;
        }

        Material material = intersect.getSceneObject().getMaterial();
        Color objectColor = material.getDiffuse();

        Double[] pLightColor = new Double[]{0.0, 0.0, 0.0};
        Double[] dirLightColor = new Double[]{0.0, 0.0, 0.0};
        Double[] ambientColor = new Double[]{0.0, 0.0, 0.0};
        Double[] finalSpecColor = new Double[]{0.0, 0.0, 0.0};
        Double[] finalColor = new Double[]{0.0, 0.0, 0.0};
        Color refractColor = new Color(0, 0, 0);


        Double[] viewDir = AMath.subtract(this.camPosition, intersect.getPoint());
        viewDir = AMath.div(viewDir, AMath.norm(viewDir));

        if (this.ambientLight != null) {
            ambientColor = AMath.multScalarAndColor(this.ambientLight.getStrength(), this.ambientLight.getColor());
        }

        double shadowIntensity = 0.0;

        if (this.directionalLight != null) {
            Double[] light_dir = AMath.negative(this.directionalLight.getDirection());


            double intensity = AMath.dot(intersect.getNormal(), light_dir) * this.directionalLight.getIntensity();
            intensity = intensity < 0 ? 0 : intensity;

            Double[] diffuseColor = AMath.multScalarAndColor(intensity, this.directionalLight.getColor());

            Double[] reflection = AMath.reflect(intersect.getNormal(), light_dir);

            double dot = AMath.dot(viewDir, reflection);
            dot = dot < 0 ? 0 : dot;
            double specIntensity = this.directionalLight.getIntensity() * Math.pow(dot, material.getSpec());

            Double[] specColor = AMath.multScalarAndColor(specIntensity, this.directionalLight.getColor());

            // shadows
            Intersect shadInter = this.sceneIntersect(intersect.getPoint(), light_dir, intersect.getSceneObject());
            if (shadInter != null) {
                shadowIntensity = 1.0;
            }

            dirLightColor = AMath.multVectorAndEscalar(AMath.addVectors(diffuseColor, specColor), (1 - shadowIntensity));
            finalSpecColor = AMath.addVectors(finalSpecColor, AMath.multVectorAndEscalar(specColor, 1 - shadowIntensity));

        }

        if (pointLights != null){
            for (PointLight pointLight : pointLights) {
                Double[] light_dir = AMath.subtract(pointLight.getPosition(), intersect.getPoint());
                light_dir = AMath.div(light_dir, AMath.norm(light_dir));
                shadowIntensity = 0.0;

                double intensity = AMath.dot(intersect.getNormal(), light_dir) * pointLight.getIntensity();
                intensity = intensity < 0 ? 0 : intensity;


                Double[] diffuseColor = AMath.multScalarAndColor(intensity, pointLight.getColor());

                Double[] reflection = AMath.reflect(intersect.getNormal(), light_dir);

                double dot = AMath.dot(viewDir, reflection);
                dot = dot < 0 ? 0 : dot;
                double specIntensity = pointLight.getIntensity() * Math.pow(dot, material.getSpec());
                Double[] specColor = AMath.multScalarAndColor(specIntensity, pointLight.getColor());

                // shadow
                Intersect shadInter = this.sceneIntersect(intersect.getPoint(), light_dir, intersect.getSceneObject());
                double lightDistance = AMath.norm(AMath.subtract(pointLight.getPosition(), intersect.getPoint()));

                if (shadInter != null && shadInter.getDistance() < lightDistance) {
                    shadowIntensity = 1.0;
                }

                pLightColor = AMath.addVectors(
                        pLightColor,
                        AMath.multVectorAndEscalar(AMath.addVectors(diffuseColor, specColor), (1 - shadowIntensity))
                );
                finalSpecColor = AMath.addVectors(finalSpecColor, AMath.multVectorAndEscalar(specColor, 1 - shadowIntensity));
            }
        }



        if (material.getMatType() == Material.OPAQUE) {
            finalColor = AMath.addVectors(pLightColor, ambientColor, dirLightColor, finalSpecColor);

            if (material.getTexture() != null && intersect.getTexCoords() != null) {
                Color texColor = material.getTexture().getColor(intersect.getTexCoords()[0], intersect.getTexCoords()[1]);

                finalColor[0] *= texColor.getRed() / 255.0;
                finalColor[1] *= texColor.getGreen() / 255.0;
                finalColor[2] *= texColor.getBlue() / 255.0;

            }

        } else if (material.getMatType() == Material.REFLECTIVE) {
            Double[] reflect = AMath.reflect(intersect.getNormal(), AMath.negative(direction));
            Color reflectColor = this.castRay(intersect.getPoint(), reflect, intersect.getSceneObject(), recursion + 1);

            finalColor = new Double[]{
                    (reflectColor.getRed() + finalSpecColor[0]) / 255.0,
                    (reflectColor.getGreen() + finalSpecColor[1]) / 255.0,
                    (reflectColor.getBlue() + finalSpecColor[2]) / 255.0,
            };


        } else if (material.getMatType() == Material.TRANSPARENT) {
            boolean outside = AMath.dot(direction, intersect.getNormal()) < 0;
            Double[] bias = AMath.multVectorAndEscalar(intersect.getNormal(), 0.001);

            double kr = AMath.fresnel(intersect.getNormal(), direction, material.getIor());

            Double[] reflect = AMath.reflect(intersect.getNormal(), AMath.negative(direction));
            Double[] reflectOrig = outside ?
                    AMath.addVectors(intersect.getPoint(), bias) :
                    AMath.subtract(intersect.getPoint(), bias);
            Color rc = this.castRay(reflectOrig, reflect, null, recursion + 1);
            Double[] reflectColor = new Double[]{
                    rc.getRed() / 255.0,
                    rc.getGreen() / 255.0,
                    rc.getBlue() / 255.0
            };


            if (kr < 1.0) {
                Double[] refract = AMath.refract(intersect.getNormal(), direction, material.getIor());

                Double[] refractOrig = outside ?
                        AMath.subtract(intersect.getPoint(), bias) :
                        AMath.addVectors(intersect.getPoint(), bias);
                refractColor = this.castRay(refractOrig, refract, null, recursion + 1);
            }

            finalColor = new Double[]{
                    (refractColor.getRed() / 255.0) * kr + reflectColor[0] * (1 - kr) + finalSpecColor[0],
                    (refractColor.getGreen() / 255.0) * kr + reflectColor[1] * (1 - kr) + finalSpecColor[1],
                    (refractColor.getBlue() / 255.0) * kr + reflectColor[2] * (1 - kr) + finalSpecColor[2],
            };


        }

        finalColor[0] *= objectColor.getRed() / 255.0;
        finalColor[1] *= objectColor.getGreen() / 255.0;
        finalColor[2] *= objectColor.getBlue() / 255.0;


        finalColor[0] = finalColor[0] > 1 ? 1 : finalColor[0];
        finalColor[1] = finalColor[1] > 1 ? 1 : finalColor[1];
        finalColor[2] = finalColor[2] > 1 ? 1 : finalColor[2];

        return new Color(
                (int) (finalColor[0] * 255),
                (int) (finalColor[1] * 255),
                (int) (finalColor[2] * 255)
        );
    }

    private Intersect sceneIntersect(Double[] origin, Double[] direction, Figure origObj) {
        Intersect intersect = null;

        double depth = Double.POSITIVE_INFINITY;

        for (Figure obj : scene) {
            if (obj != origObj) {
                Intersect hit = obj.rayIntersect(origin, direction);
                if (hit != null) {
                    if (hit.getDistance() < depth) {
                        depth = hit.getDistance();
                        intersect = hit;
                    }
                }
            }
        }

        return intersect;
    }


    public BufferedImage getImage() {
        BufferedImage bufferedImage = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                bufferedImage.setRGB(x, y, pixels[x][y].getRGB());
            }
        }
        return bufferedImage;
    }

    public void finish(String filename) {
        try {
            BufferedImage bufferedImage = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < this.width; x++) {
                for (int y = 0; y < this.height; y++) {
                    bufferedImage.setRGB(x, y, pixels[x][y].getRGB());
                }
            }

            File file = new File(filename);
            ImageIO.write(bufferedImage, "PNG", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
