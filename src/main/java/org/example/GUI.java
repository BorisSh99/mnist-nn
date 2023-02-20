package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class GUI {
    private static final int UPSCALE_FACTOR = 16;

    private final JFrame frame;
    private JLabel curLabel;

    public GUI() {
        frame = new JFrame();
        curLabel = new JLabel();
        frame.add(curLabel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void showDigit(MnistDigitData mnistDigitData) {
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

        frame.getContentPane().remove(curLabel);
        ImageIcon icon = new ImageIcon(scaledImage);
        curLabel = new JLabel(icon);
        frame.add(curLabel);
        frame.pack();
        frame.setVisible(true);
    }
}
