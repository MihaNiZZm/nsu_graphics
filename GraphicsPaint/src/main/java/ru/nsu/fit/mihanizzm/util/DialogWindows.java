package ru.nsu.fit.mihanizzm.util;

import javax.swing.*;

import static ru.nsu.fit.mihanizzm.data.TextInformation.aboutMessage;

public class DialogWindows {
    public static void showProgramInfo() {
        JOptionPane.showMessageDialog(null, aboutMessage, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    public static int confirmImageCleaning() {
        return JOptionPane.showConfirmDialog(null,
                "Are you sure you want to clean an image?",
                "Cleaning image confirmation",
                JOptionPane.OK_CANCEL_OPTION
                );
    }
}
