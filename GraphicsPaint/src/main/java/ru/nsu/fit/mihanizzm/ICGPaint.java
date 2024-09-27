package ru.nsu.fit.mihanizzm;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.nsu.fit.mihanizzm.util.SpringConfig;
import ru.nsu.fit.mihanizzm.view.ICGPaintFrame;

import javax.swing.*;

public class ICGPaint {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
    }
}