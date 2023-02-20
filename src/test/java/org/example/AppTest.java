package org.example;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class AppTest {

    private static final double TOLERANCE = 0.0001;

    @Test
    public void multiplyMatrices() {
        double[][] a = {{0.05, 0.22, 0.10, 0.04, -0.08},
                        {0.15, -0.06, 0.39, 0.46, -0.12},
                        {0.29, 0.03, 0.07, 0.43, -0.43},
                        {-0.41, -0.48, 0.33, 0.28, 0.37}};

        double[][] b = {{0.00},
                        {0.20},
                        {0.62},
                        {1.00},
                        {0.62}};

        double[][] expected = {{0.0964},
                                {0.6154},
                                {0.2128},
                                {0.618}};

        double[][] actual = MnistNN.multiplyMatrices(a, b);
//        Assertions.assertEquals(expected[0][0], actual[0][0]);
        for (int i = 0; i < actual.length; i++) {
            for (int j = 0; j < actual[i].length; j++) {
                Assertions.assertTrue(Math.abs(expected[i][j] - actual[i][j]) <= TOLERANCE);
            }
        }

    }

    @Test
    public void rowOfMax() {
        double[][] arr = {{1.2, 2.3, 3.4}, {4.5, 10.6, 6.7}, {7.8, 8.9, 9.1}};
        Assertions.assertEquals(1, MnistNN.rowOfMax(arr));
    }
}
