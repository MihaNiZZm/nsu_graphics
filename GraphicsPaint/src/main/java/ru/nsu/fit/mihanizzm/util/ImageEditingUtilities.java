package ru.nsu.fit.mihanizzm.util;


import ru.nsu.fit.mihanizzm.ICGPaint;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class ImageEditingUtilities {
    public static ImageIcon createColorfulImageIcon(int width, int height, Color color) {
        BufferedImage icon = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        fillImageWithColor(icon, color);
        return new ImageIcon(icon);
    }

    public static void fillImageWithColor(BufferedImage image, Color color) {
        int width = image.getWidth();
        int height = image.getHeight();
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                image.setRGB(x, y, color.getRGB());
            }
        }
    }

    public static ImageIcon getImageFromResources(String path) {
        URL imageURL = ICGPaint.class.getResource(path);
        if (imageURL != null) {
            return new ImageIcon(imageURL);
        }
        else {
            // add logging
            return null;
        }
    }
}


