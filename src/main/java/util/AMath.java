package util;

import java.util.ArrayList;
import java.util.Arrays;

public class AMath {

    public static <T> T[] subarray(T[] array, int inicio, int fin) {
        return Arrays.copyOfRange(array, inicio, fin);
    }

    public static double deg2Rad(double deg) {
        return deg * 3.141592653589793 / 180;
    }

    public static Double[][] subMatrix4to3(Double[][] matrix, int yo, int xo) {
        ArrayList<Double[]> m = new ArrayList<>();

        for (int y = 0; y < 4; y++) {
            ArrayList<Double> temp = new ArrayList<>();
            for (int x = 0; x < 4; x++) {
                if (yo != y && xo != x) {
                    temp.add(matrix[y][x]);
                }
            }
            if (temp.size() == 3) {
                m.add(temp.toArray(new Double[0]));
            }
        }
        return m.toArray(new Double[0][0]);
    }

    public static Double det2X2(Double[] v1, Double[] v2) {
        return v1[0] * v2[1] - v1[1] * v2[0];
    }

    public static Double det3X3(Double[][] m) {
        return m[0][0] * det2X2(subarray(m[1], 1, 3), subarray(m[2], 1, 3)) -
                m[0][1] * det2X2(new Double[]{m[1][0], m[1][2]}, new Double[]{m[2][0], m[2][2]}) +
                m[0][2] * det2X2(subarray(m[1], 0, 2), subarray(m[2], 0, 2));
    }

    public static Double det4X4(Double[][] matrix) {
        double[] vals = new double[4];

        for (int x = 0; x < 4; x++) {
            Double[][] subM = subMatrix4to3(matrix, 0, x);
            double val = matrix[0][x] * det3X3(subM);
            vals[x] = val;
        }

        return vals[0] - vals[1] + vals[2] - vals[3];
    }

    public static Double[] cross(Double[] v1, Double[] v2) {
        return new Double[]{
                det2X2(new Double[]{v1[1], v1[2]}, new Double[]{v2[1], v2[2]}),
                -1 * det2X2(new Double[]{v1[0], v1[2]}, new Double[]{v2[0], v2[2]}),
                det2X2(new Double[]{v1[0], v1[1]}, new Double[]{v2[0], v2[1]})
        };
    }

    public static Double[] subtract(Double[] v1, Double[] v2) {
        Double[] result = new Double[v1.length];
        for (int i = 0; i < v1.length; i++) {
            result[i] = v1[i] - v2[i];
        }
        return result;
    }

    public static Double dot(Double[] v1, Double[] v2) {
        Double[] result = new Double[v1.length];
        for (int i = 0; i < v1.length; i++) {
            result[i] = v1[i] * v2[i];
        }
        return result[0] + result[1] + result[2];
    }

    public static Double norm(Double[] vector) {
        return Math.pow(Math.pow(vector[0], 2) + Math.pow(vector[1], 2) + Math.pow(vector[2], 2), 0.5);
    }

    public static Double[] div(Double[] vector, Double normal) {
        Double[] result = new Double[vector.length];
        for (int i = 0; i < vector.length; i++) {
            result[i] = vector[i] / normal;
        }
        return result;
    }

    public static Double[][] matrixDiv(Double[][] matrix, Double divisor) {

        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                matrix[y][x] /= divisor;
            }
        }
        return matrix;
    }

    public static Double[][] rows2Cols(Double[][] matrix) {
        Double[][] aT = new Double[matrix.length][4];

        for (int i = 0; i < matrix.length; i++) {
            Double[] vector = matrix[i];

            aT[0][i] = vector[0];
            aT[1][i] = vector[1];
            aT[2][i] = vector[2];
            aT[3][i] = vector[3];

        }
        return aT;
    }


    public static Double[][] cols2Rows(Double[][] matrix) {
        Double[][] aT = new Double[matrix.length][];

        for (int i = 0; i < aT.length; i++) {
            aT[i] = new Double[]{matrix[0][i], matrix[1][i], matrix[2][i], matrix[3][i]};
        }

        return aT;
    }


    public static Double[][] inv(Double[][] matrix) {
        Double[][] aT = rows2Cols(matrix);

        Double[][] adj = new Double[4][];
        Boolean sign = true;

        for (int y = 0; y < 4; y++) {
            Double[] temp = new Double[4];

            for (int x = 0; x < 4; x++) {
                Double[][] subM = subMatrix4to3(aT, y, x);

                Double det = det3X3(subM);

                temp[x] = sign ? det : -1 * det;
                sign = !sign;
            }
            sign = !sign;
            adj[y] = temp;
        }

        Double det = det4X4(matrix);
        return matrixDiv(adj, det);
    }

    public static Double[][] multMatrix4x4(Double[][] m1, Double[][] m2) {
        Double[][] m2T = cols2Rows(m2);
        Double[][] mF = new Double[4][];

        for (int n = 0; n < 4; n++) {
            Double[] temp = new Double[4];
            for (int j = 0; j < 4; j++) {
                Double res = 0.0;
                for (int i = 0; i < 4; i++) {
                    res += m1[n][i] * m2T[j][i];
                }
                temp[j] = res;
            }
            mF[n] = temp;
        }
        return mF;
    }

    public static Double[] multMatrix4x4AndVector(Double[][] matrix, Double[] vector) {
        Double[] mF = new Double[4];

        for (int n = 0; n < 4; n++) {
            Double res = 0.0;
            for (int j = 0; j < 4; j++) {
                res += matrix[n][j] * vector[j];
            }
            mF[n] = res;
        }
        return mF;
    }

}
