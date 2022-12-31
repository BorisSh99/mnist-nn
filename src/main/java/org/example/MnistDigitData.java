package org.example;

public class MnistDigitData {
    private final double[] pixelArray;
    private Integer label;

    public MnistDigitData() {
        this.pixelArray = new double[784];
    }

    public void setPixelValue(int index, double pixelValue) {
        pixelArray[index] = pixelValue;
    }

    public double getPixelValue(int index) {
        return pixelArray[index];
    }

    public double[] getPixelArray() {
        return pixelArray;
    }

    public Integer getLabel() {
        return label;
    }

    public void setLabel(Integer label) {
        this.label = label;
    }
}
