package ru.nsu.fit.mihanizzm.spline_editor_3d;

import ru.nsu.fit.mihanizzm.spline_editor_3d.model.SettingsManager;
import ru.nsu.fit.mihanizzm.spline_editor_3d.views.MainFrame;

import javax.swing.*;

/**
 * Hello world!
 *
 */
public class Main
{
    public static void main( String[] args ) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        JFrame mainFrame = MainFrame.getInstance();
        SettingsManager.getInstance().setInitialSettings();
    }
}
