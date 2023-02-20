package org.example;

public class MnistDigitData {
    private final double[][] pixelMatrix;
    private final double[][] labelMatrix;
    private Integer label;

    public MnistDigitData() {
        pixelMatrix = new double[784][1];
        labelMatrix = new double[10][1];
    }

    public void setPixelValue(int index, double pixelValue) {
        pixelMatrix[index][0] = pixelValue;
    }

    public double getPixelValue(int index) {
        return pixelMatrix[index][0];
    }

    public double[][] getPixelMatrix() {
        return pixelMatrix;
    }

    public double[][] getLabelMatrix() {
        return labelMatrix;
    }

    public Integer getLabel() {
        return label;
    }

    public void setLabelToMatrix(Integer label) {
        switch (label) {
            case 0 -> labelMatrix[0][0] = 1.0;
            case 1 -> labelMatrix[1][0] = 1.0;
            case 2 -> labelMatrix[2][0] = 1.0;
            case 3 -> labelMatrix[3][0] = 1.0;
            case 4 -> labelMatrix[4][0] = 1.0;
            case 5 -> labelMatrix[5][0] = 1.0;
            case 6 -> labelMatrix[6][0] = 1.0;
            case 7 -> labelMatrix[7][0] = 1.0;
            case 8 -> labelMatrix[8][0] = 1.0;
            case 9 -> labelMatrix[9][0] = 1.0;
        }

        this.label = label;
    }
}
