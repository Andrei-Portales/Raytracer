public class Main {

    public static void main(String... args) {

        int width = 960;
        int height = 540;

        Raytracer rtx = new Raytracer(width, height);

        rtx.finish("output.bmp");
    }
}
