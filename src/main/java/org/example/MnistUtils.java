package org.example;

import java.io.*;

public class MnistUtils {

    public static MnistMatrix[] readData(String dataFilePath, String labelFilePath) throws IOException {

        DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(dataFilePath)));
        int magicNumber = dataInputStream.readInt();
        int numberOfItems = dataInputStream.readInt();
        int nRows = dataInputStream.readInt();
        int nColumns = dataInputStream.readInt();

        System.out.println("magic number is " + magicNumber);
        assert magicNumber == 2051; // Magic number for digits data
        System.out.println("number of items is " + numberOfItems);
        System.out.println("number of rows is: " + nRows);
        System.out.println("number of cols is: " + nColumns);
        assert nRows == 28 && nColumns == 28;

        DataInputStream labelInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(labelFilePath)));
        int labelMagicNumber = labelInputStream.readInt();
        int numberOfLabels = labelInputStream.readInt();

        System.out.println("labels magic number is: " + labelMagicNumber);
        assert labelMagicNumber == 2049; // Magic number for label data
        System.out.println("number of labels is: " + numberOfLabels);

        MnistMatrix[] data = new MnistMatrix[numberOfItems];

        assert numberOfItems == numberOfLabels;

        for(int i = 0; i < numberOfItems; i++) {
            MnistMatrix mnistMatrix = new MnistMatrix(nRows, nColumns);
            mnistMatrix.setLabel(labelInputStream.readUnsignedByte());
            for (int row = 0; row < nRows; row++) {
                for (int column = 0; column < nColumns; column++) {
                    mnistMatrix.setColorValue(row, column, dataInputStream.readUnsignedByte()/255.0);
                }
            }
            data[i] = mnistMatrix;
        }
        dataInputStream.close();
        labelInputStream.close();
        return data;
    }
}