import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Obj {

    private Double[][] vertices;
    private Double[][] texCoords;
    private Double[][] normals;
    private Integer[][][] faces;

    public Obj(String filename) {
        ArrayList<String> t = new ArrayList<>();
        try {
            File file = new File(filename);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                t.add(myReader.nextLine());
            }
            myReader.close();

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        this.read(t.toArray(new String[0]));
    }

    private void read(String[] lines) {
        ArrayList<Double[]> verts = new ArrayList<>();
        ArrayList<Double[]> texCoords = new ArrayList<>();
        ArrayList<Double[]> normals = new ArrayList<>();
        ArrayList<Integer[][]> faces = new ArrayList<>();

        for (String line : lines) {
            if (line != null && !line.isEmpty()) {
                try {
                    String[] data = line.split(" ", 2);
                    String[] values = data[1].trim().split(" ");

                    if (data[0].equals("f")) {
                        Integer[][] temp = new Integer[values.length][];

                        for (int i = 0; i < values.length; i++) {
                            String[] fTemp = values[i].split("/");
                            Integer[] f = new Integer[fTemp.length];

                            for (int j = 0; j < fTemp.length; j++){
                                f[j] = Integer.parseInt(fTemp[j]);
                            }

                            temp[i] = f;
                        }
                        faces.add(temp);
                    } else {
                        Double[] temp = new Double[values.length];
                        for (int i = 0; i < values.length; i++) {
                            temp[i] = Double.parseDouble(values[i]);
                        }

                        if (data[0].equals("v")) {
                            verts.add(temp);
                        } else if (data[0].equals("vt")) {
                            texCoords.add(temp);
                        } else if (data[0].equals("vn")) {
                            normals.add(temp);
                        }
                    }

                } catch (Exception e) {}
            }
        }
        this.vertices = verts.toArray(new Double[0][]);
        this.texCoords = texCoords.toArray(new Double[0][]);
        this.normals = normals.toArray(new Double[0][]);
        this.faces = faces.toArray(new Integer[0][0][]);
    }

    public Double[][] getVerts() {
        return this.vertices;
    }

    public Double[][] getTexCoords() {
        return this.texCoords;
    }

    public Double[][] getNormals() {
        return this.normals;
    }

    public Integer[][][] getFaces() {
        return faces;
    }
}
