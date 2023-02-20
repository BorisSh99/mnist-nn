package org.example;

import java.io.IOException;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws IOException {
        // Read MNIST dataset
        // x = 0; y = 0 means the upper left corner of the matrix
        MnistDigitData[] trainMnistDigitArray = MnistUtils.readData("src/main/resources/data/train-images.idx3-ubyte", "src/main/resources/data/train-labels.idx1-ubyte");
        MnistDigitData[] testMnistDigitArray = MnistUtils.readData("src/main/resources/data/t10k-images.idx3-ubyte", "src/main/resources/data/t10k-labels.idx1-ubyte");

        MnistNN mnistNN = new MnistNN(trainMnistDigitArray, testMnistDigitArray, 20);
        mnistNN.train();
        mnistNN.test();

        System.out.println();
        System.out.println("Now you can try it! You can enter an index of a digit from testing data array (e.g. 0 is an index of digit 7 and 9999 is an index of digit 6)");
        System.out.println();
        GUI gui = new GUI();

        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter index of a digit [0-9999]: ");
            try {
                int index = scanner.nextInt();
                if (index >= 0 && index < 10000) {
                    gui.showDigit(testMnistDigitArray[index]);
                    int output = mnistNN.define(testMnistDigitArray[index]);
                    System.out.println("NN thinks, that on the Swing-window is shown digit " + output);
                } else {
                    System.err.println("Invalid index! Try again...");
                }
            } catch (Exception e) {
                System.err.println("Invalid index! Try again...");
            }
            System.out.println();
        }
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
