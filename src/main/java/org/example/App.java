package org.example;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.Random;

import javax.swing.*;

public class App {

    public static final int UPSCALE_FACTOR = 16;
    public static void main(String[] args) throws IOException {
        // Read MNIST dataset
        // x = 0; y = 0 means the upper left corner of the matrix
        MnistDigitData[] trainMnistDigitArray = MnistUtils2.readData("src/main/resources/data/train-images.idx3-ubyte", "src/main/resources/data/train-labels.idx1-ubyte");
        printMnistDigitData(trainMnistDigitArray[trainMnistDigitArray.length - 2]);
        showDigit(trainMnistDigitArray[trainMnistDigitArray.length - 2]);



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

    public static void showDigit(MnistDigitData mnistDigitData) {
        double[] pixelArray = mnistDigitData.getPixelArray();
        int width = 28;
        int height = 28;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = image.getRaster();
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < height; x++) {
                int value = (int) (pixelArray[y * height + x] * 255);
                raster.setSample(x, y, 0, value);
            }
        }

        int scaledWidth = width * UPSCALE_FACTOR;
        int scaledHeight = height * UPSCALE_FACTOR;

        BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_BYTE_GRAY);

        Graphics2D g = scaledImage.createGraphics();
        g.drawImage(image, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();

        ImageIcon icon = new ImageIcon(scaledImage);
        JLabel label = new JLabel(icon);
        JFrame frame = new JFrame();
        frame.add(label);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
