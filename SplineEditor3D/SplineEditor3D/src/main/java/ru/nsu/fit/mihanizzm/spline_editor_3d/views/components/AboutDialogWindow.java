package ru.nsu.fit.mihanizzm.spline_editor_3d.views.components;

import ru.nsu.fit.mihanizzm.spline_editor_3d.model.TextInfo;

import javax.swing.*;

public class AboutDialogWindow {
    public static void showProgramInfo() {
        String aboutMessage = TextInfo.ABOUT_MESSAGE;
        JOptionPane.showMessageDialog(null, aboutMessage, "About", JOptionPane.INFORMATION_MESSAGE);
    }
}
