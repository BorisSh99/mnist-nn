package org.example;

public class MnistMatrix {

    private final double [][] data;

    private final int nRows;
    private final int nColumns;

    private int label;

    public MnistMatrix(int nRows, int nColumns) {
        this.nRows = nRows;
        this.nColumns = nColumns;

        data = new double[nRows][nColumns];
    }

    public double getColorValue(int row, int column) {
        return data[row][column];
    }

    public void setColorValue(int row, int column, double value) {
        data[row][column] = value;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public int getNumberOfRows() {
        return nRows;
    }

    public int getNumberOfColumns() {
        return nColumns;
    }

    public double[][] getData() {
        return data;
    }
}
