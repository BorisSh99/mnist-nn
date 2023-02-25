package org.example;

import org.apache.commons.math3.analysis.function.Sigmoid;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.Random;

public class MnistNN {

    private final MnistDigitData[] trainMnistDigitArray;
    private final MnistDigitData[] testMnistDigitArray;
    // Weights from input to hidden layer
    private double[][] wIH;
    // Weights from hidden to output layer
    private double[][] wHO;
    // Biases from input to hidden layer
    private double[][] bIH;
    // Biases from hidden to output layer
    private double[][] bHO;

    private final double learnRate;
    private final int nEpochs;
    private int nrCorrect;
    private double cost;

    public MnistNN(MnistDigitData[] trainMnistDigitArray, MnistDigitData[] testMnistDigitArray, int nHiddenNeurons) {
        this.trainMnistDigitArray = trainMnistDigitArray;
        this.testMnistDigitArray = testMnistDigitArray;

        wIH = generateWeightsMatrix(nHiddenNeurons, 784);
        wHO = generateWeightsMatrix(10, nHiddenNeurons);
        bIH = generateBiasesMatrix(nHiddenNeurons);
        bHO = generateBiasesMatrix(10);

        learnRate = 0.01;
        nEpochs = 3;
        nrCorrect = 0;
        cost = 0.0;
    }

    public void train() {
        System.out.println("Training started with " + nEpochs + " epochs (it can last up to 3 minutes)...");
        for (int epoch = 0; epoch < nEpochs; epoch++) {
            for (MnistDigitData digitData : trainMnistDigitArray) {
                // Forward propagation from input to hidden
                double[][] hPre = addMatrices(bIH, multiplyMatrices(wIH, digitData.getPixelMatrix()));
                double[][] h = sigmoidMatrix(hPre);

                // Forward propagation from hidden to output
                double[][] oPre = addMatrices(bHO, multiplyMatrices(wHO, h));
                double[][] o = sigmoidMatrix(oPre);

                double[][] subtractedOutput = subtractMatrices(o, normalizeOutput(o));
                double sum = 0.0;
                for (int i = 0; i < subtractedOutput.length; i++) {
                    sum += Math.pow(subtractedOutput[i][0], 2);
                }
                cost = sum / o.length;
                nrCorrect += rowOfMax(o) == rowOfMax(digitData.getLabelMatrix()) ? 1 : 0;

                // Backpropagation from output to hidden
                double[][] deltaO = subtractMatrices(o, digitData.getLabelMatrix());
                wHO = addMatrices(wHO, scalarMultiply(-learnRate, multiplyMatrices(deltaO, transposeMatrix(h))));
                bHO = addMatrices(bHO, scalarMultiply(-learnRate, deltaO));

                // Backpropagation from hidden to input
                double[][] deltaH = arrayMultiply(multiplyMatrices(transposeMatrix(wHO), deltaO), arrayMultiply(h, scalarSubtract(1.0, h)));
                wIH = addMatrices(wIH, scalarMultiply(-learnRate, multiplyMatrices(deltaH, transposeMatrix(digitData.getPixelMatrix()))));
                bIH = addMatrices(bIH, scalarMultiply(-learnRate, deltaH));
            }

            double acc = (nrCorrect / 60000.0) * 100;
            System.out.printf("" + (epoch + 1) + " Epoch ended with an accuracy in relation to the training data: %.2f%%\n", acc);
            nrCorrect = 0;
        }
        System.out.println("---------------Training done!---------------");
    }

    public void test() {
        System.out.println("Testing started...");
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
        System.out.printf("Accuracy in relation to the previously unseen testing data: %.2f%%\n", acc);

        System.out.println("---------------Testing done!---------------");
    }

    public int define(MnistDigitData digitData) {
        // Forward propagation from input to hidden
        double[][] hPre = addMatrices(bIH, multiplyMatrices(wIH, digitData.getPixelMatrix()));
        double[][] h = sigmoidMatrix(hPre);

        // Forward propagation from hidden to output
        double[][] oPre = addMatrices(bHO, multiplyMatrices(wHO, h));
        double[][] o = sigmoidMatrix(oPre);
        return rowOfMax(o);
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
    public static double[][] normalizeOutput(double[][] output) {
        double[][] result = new double[output.length][output[0].length];
        int maxRow = rowOfMax(output);
        result[maxRow][0] = 1.0;
        return result;
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
}
