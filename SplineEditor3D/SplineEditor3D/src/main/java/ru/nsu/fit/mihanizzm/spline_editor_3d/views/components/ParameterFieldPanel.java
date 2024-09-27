package ru.nsu.fit.mihanizzm.spline_editor_3d.views.components;


import javax.swing.*;
import java.awt.*;

public class ParameterFieldPanel extends JPanel {
    private static final Dimension PREFERRED_SIZE = new Dimension(180, 20);
    private static final Dimension SPINNER_SIZE = new Dimension(60, 20);
    private static final Dimension LABEL_SIZE = new Dimension(100, 20);

    public ParameterFieldPanel(JLabel name, JSpinner spinner) {
        setLayout(new BorderLayout());

        spinner.setMaximumSize(SPINNER_SIZE);
        spinner.setPreferredSize(SPINNER_SIZE);

        name.setMaximumSize(LABEL_SIZE);
        name.setPreferredSize(LABEL_SIZE);
        name.setHorizontalAlignment(SwingConstants.RIGHT);

        setSize(PREFERRED_SIZE);
        setPreferredSize(PREFERRED_SIZE);
        setMaximumSize(PREFERRED_SIZE);

        add(name, BorderLayout.WEST);
        add(spinner);
    }
}
