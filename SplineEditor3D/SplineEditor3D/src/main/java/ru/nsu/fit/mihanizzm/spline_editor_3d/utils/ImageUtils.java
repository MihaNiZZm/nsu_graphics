package ru.nsu.fit.mihanizzm.spline_editor_3d.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import ru.nsu.fit.mihanizzm.spline_editor_3d.Main;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

@UtilityClass
@Log4j2
public class ImageUtils {
    public ImageIcon getImageFromResources(String path) {
        URL imageURL = Main.class.getResource(path);
        if (imageURL != null) {
            return new ImageIcon(imageURL);
        }
        else {
            log.error("Couldn't get an image from resource path: " + path);
            return null;
        }
    }

    public ImageIcon getScaledImageFromResources(String path, int width, int height) {
        ImageIcon imageFromResources = getImageFromResources(path);
        if (imageFromResources == null) {
            return null;
        }
        Image image = imageFromResources.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }
}
