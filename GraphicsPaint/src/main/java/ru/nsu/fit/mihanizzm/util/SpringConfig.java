package ru.nsu.fit.mihanizzm.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.nsu.fit.mihanizzm.ICGPaint;
import ru.nsu.fit.mihanizzm.view.ICGPaintFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

@Configuration
@ComponentScan("ru.nsu.fit.mihanizzm")
public class SpringConfig {
    @Bean
    JMenu fileMenu() {
        return new JMenu("File");
    }

    @Bean
    JMenu toolsMenu() {
        return new JMenu("Tools");
    }

    @Bean
    JMenu aboutMenu() {
        return new JMenu("About");
    }

    @Bean
    Image iconImage() {
        try {
            URL imageURL = ICGPaint.class.getResource("/icg_paint_icon.png");
            if (imageURL != null) {
                ImageIcon image = new ImageIcon(ImageIO.read(imageURL));
                return image.getImage();
            }
        } catch (IOException e) {
            System.err.println("Failed to load image: " + e.getMessage());
        }

        return null;
    }
}
