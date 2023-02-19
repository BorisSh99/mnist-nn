package org.example;

import org.apache.commons.math3.analysis.function.Sigmoid;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

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

        // Weights from input to hidden layer
        double[][] wIH = generateWeightsMatrix(20, 784);
        // Weights from hidden to output layer
        double[][] wHO = generateWeightsMatrix(10, 20);

        // Biases from input to hidden layer
        double[][] bIH = generateBiasesMatrix(20);
        // Biases from hidden to output layer
        double[][] bHO = generateBiasesMatrix(10);

        double learnRate = 0.01;
        int nrCorrect = 0;
        int nEpochs = 3;

        System.out.println("Training started with " + nEpochs + " epochs (can last up to 3 minutes)...");
        for (int epoch = 0; epoch < nEpochs; epoch++) {
            for (MnistDigitData digitData : trainMnistDigitArray) {
                // Forward propagation from input to hidden
                double[][] hPre = addMatrices(bIH, multiplyMatrices(wIH, digitData.getPixelMatrix()));
                double[][] h = sigmoidMatrix(hPre);

                // Forward propagation from hidden to output
                double[][] oPre = addMatrices(bHO, multiplyMatrices(wHO, h));
                double[][] o = sigmoidMatrix(oPre);

                // Cost / Error calculation

                nrCorrect += rowOfMax(o) == rowOfMax(digitData.getLabelMatrix()) ? 1 : 0;

                // Backpropagation from output to hidden (cost function derivative)
                double[][] deltaO = subtractMatrices(o, digitData.getLabelMatrix());
                wHO = addMatrices(wHO, scalarMultiply(-learnRate, multiplyMatrices(deltaO, transposeMatrix(h))));
                bHO = addMatrices(bHO, scalarMultiply(-learnRate, deltaO));

                // Backpropagation from hidden to input (activation function derivative)
                double[][] deltaH = arrayMultiply(multiplyMatrices(transposeMatrix(wHO), deltaO), arrayMultiply(h, scalarSubtract(1.0, h)));
                wIH = addMatrices(wIH, scalarMultiply(-learnRate, multiplyMatrices(deltaH, transposeMatrix(digitData.getPixelMatrix()))));
                bIH = addMatrices(bIH, scalarMultiply(-learnRate, deltaH));
            }

            double acc = (nrCorrect / 60000.0) * 100;
            System.out.printf("" + (epoch + 1) + " Epoch ended with an accuracy in relation to the training data: %d%%\n", Math.round(acc));
            nrCorrect = 0;
        }
        System.out.println("---------------Training done!---------------");

        MnistDigitData[] testMnistDigitArray = MnistUtils2.readData("src/main/resources/data/t10k-images.idx3-ubyte", "src/main/resources/data/t10k-labels.idx1-ubyte");
        System.out.println("Testing started.");
        for (MnistDigitData digitData : testMnistDigitArray) {
            // Forward propagation from input to hidden
            double[][] hPre = addMatrices(bIH, multiplyMatrices(wIH, digitData.getPixelMatrix()));
            double[][] h = sigmoidMatrix(hPre);

            // Forward propagation from hidden to output
            double[][] oPre = addMatrices(bHO, multiplyMatrices(wHO, h));
            double[][] o = sigmoidMatrix(oPre);

            nrCorrect += rowOfMax(o) == rowOfMax(digitData.getLabelMatrix()) ? 1 : 0;
        }
        double acc = (nrCorrect / 10000.0) * 100;
        System.out.printf("Accuracy in relation to the previously unseen testing data: %d%%\n", Math.round(acc));

        System.out.println("---------------Testing done!---------------");
    }

    public static int rowOfMax(double[][] a) {
        RealMatrix matrix = MatrixUtils.createRealMatrix(a);
        int maxRow = -1;
        double maxValue = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            RealVector row = matrix.getRowVector(i);
            int maxIndex = row.getMaxIndex();
            double value = row.getEntry(maxIndex);
            if (value > maxValue) {
                maxValue = value;
                maxRow = i;
            }
        }
        return maxRow;
    }
    public static double[][] arrayMultiply(double[][] a, double[][] b) {
        double[][] result = new double[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                result[i][j] = a[i][j] * b[i][j];
            }
        }
        return result;
    }
    public static double[][] multiplyMatrices(double[][] a, double[][] b) {
        RealMatrix m1 = MatrixUtils.createRealMatrix(a);
        RealMatrix m2 = MatrixUtils.createRealMatrix(b);
        RealMatrix result = m1.multiply(m2);
        return result.getData();
    }
    public static double[][] transposeMatrix(double[][] a) {
        RealMatrix matrix = MatrixUtils.createRealMatrix(a);
        RealMatrix result = matrix.transpose();
        return result.getData();
    }
    public static double[][] scalarMultiply(double v, double[][] m) {
        RealMatrix matrix = MatrixUtils.createRealMatrix(m);
        RealMatrix result = matrix.scalarMultiply(v);
        return result.getData();
    }
    public static double[][] scalarSubtract(double v, double[][] m) {
        double[][] result = new double[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                result[i][j] = v - m[i][j];
            }
        }
        return result;
    }
    public static double[][] addMatrices(double[][] a, double[][] b) {
        RealMatrix m1 = MatrixUtils.createRealMatrix(a);
        RealMatrix m2 = MatrixUtils.createRealMatrix(b);
        RealMatrix result = m1.add(m2);
        return result.getData();
    }
    public static double[][] subtractMatrices(double[][] a, double[][] b) {
        RealMatrix m1 = MatrixUtils.createRealMatrix(a);
        RealMatrix m2 = MatrixUtils.createRealMatrix(b);
        RealMatrix result = m1.subtract(m2);
        return result.getData();
    }
    public static double[][] sigmoidMatrix(double[][] a) {
        Sigmoid sigmoid = new Sigmoid();
        RealMatrix m = MatrixUtils.createRealMatrix(a);
        int rows = m.getRowDimension();
        int cols = m.getColumnDimension();
        double[][] result = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = sigmoid.value(m.getEntry(i, j));
            }
        }
        return result;
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
        double[][] pixelMatrix = mnistDigitData.getPixelMatrix();
        int width = 28;
        int height = 28;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster = image.getRaster();
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < height; x++) {
                int value = (int) (pixelMatrix[y * height + x][0] * 255);
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
