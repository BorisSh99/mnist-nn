package org.example;

public class MnistMatrix {

    private final int [][] data;

    private final int nRows;
    private final int nColumns;

    private int label;

    public MnistMatrix(int nRows, int nColumns) {
        this.nRows = nRows;
        this.nColumns = nColumns;

        data = new int[nRows][nColumns];
    }

    public int getColorValue(int row, int column) {
        return data[row][column];
    }

    public void setColorValue(int row, int column, int value) {
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

    public int[][] getData() {
        return data;
    }
}
