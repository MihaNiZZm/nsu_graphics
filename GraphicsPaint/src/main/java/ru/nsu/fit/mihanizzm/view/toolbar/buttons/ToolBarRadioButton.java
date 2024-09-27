package ru.nsu.fit.mihanizzm.view.toolbar.buttons;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class ToolBarRadioButton extends JRadioButton {
    private static final Color BG_BUTTON_COLOR = new Color(0xAAAAAA);
    private static final int BUTTON_AXIS_SIZE = 32;
    public ToolBarRadioButton() {
        super();
        setFocusable(false);
        setBackground(BG_BUTTON_COLOR);
        setPreferredSize(new Dimension(BUTTON_AXIS_SIZE, BUTTON_AXIS_SIZE));
    }
}
