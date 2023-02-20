package org.example;

import java.io.*;

public class MnistUtils {

    public static MnistDigitData[] readData(String dataFilePath, String labelFilePath) throws IOException {
        System.out.println("Started reading dataset...");
        DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(dataFilePath)));
        int magicNumber = dataInputStream.readInt();
        int numberOfItems = dataInputStream.readInt();
        int nRows = dataInputStream.readInt();
        int nColumns = dataInputStream.readInt();

        assert magicNumber == 2051; // Magic number for digits data
        System.out.println("Number of items: " + numberOfItems);
        assert nRows == 28 && nColumns == 28;

        DataInputStream labelInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(labelFilePath)));
        int labelMagicNumber = labelInputStream.readInt();
        int numberOfLabels = labelInputStream.readInt();

        assert labelMagicNumber == 2049; // Magic number for label data
        System.out.println("Number of labels is: " + numberOfLabels);

        MnistDigitData[] dataset = new MnistDigitData[numberOfItems];

        assert numberOfItems == numberOfLabels;

        for(int i = 0; i < numberOfItems; i++) {
            MnistDigitData mnistDigitData = new MnistDigitData();
            mnistDigitData.setLabelToMatrix(labelInputStream.readUnsignedByte());
            for (int pixelIndex = 0; pixelIndex < 784; pixelIndex++) {
                mnistDigitData.setPixelValue(pixelIndex, dataInputStream.readUnsignedByte()/255.0);
            }

            dataset[i] = mnistDigitData;
        }
        dataInputStream.close();
        labelInputStream.close();
        System.out.println();
        return dataset;
    }
}
