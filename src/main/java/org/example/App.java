package org.example;

import java.io.IOException;
import java.util.Random;

public class App {
    public static void main(String[] args) throws IOException {
        // x = 0; y = 0 in the upper left corner
        MnistMatrix[] mnistMatrix = MnistUtils.readData("src/main/resources/data/train-images.idx3-ubyte", "src/main/resources/data/train-labels.idx1-ubyte");
        printMnistMatrix(mnistMatrix[mnistMatrix.length - 2]);



    }

    public static double[][] generateWeightsMatrix(int toLayer, int fromLayer) {
        Random random = new Random();
        double[][] weightsFromTo = new double[toLayer][fromLayer];
        for (int i = 0; i < toLayer; i++) {
            for (int j = 0; j < fromLayer; j++) {
                weightsFromTo[i][j] = random.nextDouble() - 0.5; // [-0.5 ; 0.5)
            }
        }
        return weightsFromTo;
    }
    public static double[][] generateBiasesMatrix(int toLayer) {
        return new double[toLayer][1];
    }
    private static void printMnistMatrix(final MnistMatrix matrix) {
        System.out.println("label: " + matrix.getLabel());
        for (int r = 0; r < matrix.getNumberOfRows(); r++ ) {
            for (int c = 0; c < matrix.getNumberOfColumns(); c++) {
                System.out.print(matrix.getColorValue(r, c) + " ");
            }
            System.out.println();
        }
    }
}
