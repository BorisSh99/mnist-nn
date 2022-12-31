package org.example;

import java.io.IOException;
import java.util.Random;

public class App {
    public static void main(String[] args) throws IOException {
        // Read MNIST dataset
        // x = 0; y = 0 means the upper left corner of the matrix
        MnistDigitData[] trainMnistDigitArray = MnistUtils2.readData("src/main/resources/data/train-images.idx3-ubyte", "src/main/resources/data/train-labels.idx1-ubyte");
        printMnistDigitData(trainMnistDigitArray[trainMnistDigitArray.length - 2]);

//        MnistMatrix[] trainMnistMatrixArray = MnistUtils.readData("src/main/resources/data/train-images.idx3-ubyte", "src/main/resources/data/train-labels.idx1-ubyte");
//        printMnistMatrix(trainMnistMatrixArray[trainMnistMatrixArray.length - 2]);

        // Weights from input to hidden layer
        double[][] wIH = generateWeightsMatrix(20, 784);
        // Weights from hidden to output layer
        double[][] wHO = generateWeightsMatrix(10, 20);

        // Biases from input to hidden layer
        double[][] bIH = generateBiasesMatrix(20);
        // Biases from hidden to output layer
        double[][] bHO = generateBiasesMatrix(10);

        double learnRate = 0.01;
        int nEpochs = 3;

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
    private static void printMnistDigitData(MnistDigitData digitData) {
        System.out.println("label: " + digitData.getLabel());
        int pixelIndexCounter = -1;
        for (int row = 0; row < 28; row++) {
            for (int column = 0; column < 28; column++) {
                pixelIndexCounter++;
                System.out.print(digitData.getPixelValue(pixelIndexCounter) + " ");
            }
            System.out.println();
        }
    }
    private static void printMnistMatrix(MnistMatrix matrix) {
        System.out.println("label: " + matrix.getLabel());
        for (int row = 0; row < matrix.getNumberOfRows(); row++ ) {
            for (int column = 0; column < matrix.getNumberOfColumns(); column++) {
                System.out.print(matrix.getColorValue(row, column) + " ");
            }
            System.out.println();
        }
    }
}
