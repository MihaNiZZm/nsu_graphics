package ru.nsu.fit.mihanizzm.spline_editor_3d.views;

import lombok.Getter;
import ru.nsu.fit.mihanizzm.spline_editor_3d.utils.ImageUtils;
import ru.nsu.fit.mihanizzm.spline_editor_3d.views.components.SplineEditorParameterPanel;
import ru.nsu.fit.mihanizzm.spline_editor_3d.views.components.SplineEditorViewPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SplineEditorFrame extends JFrame {
    private static final Dimension MINIMUM_SIZE = new Dimension(640, 480);
    private static final Dimension DEFAULT_SIZE = new Dimension(1280, 720);

    private static SplineEditorFrame instance;

    @Getter
    private SplineEditorViewPanel panel;
    @Getter
    SplineEditorParameterPanel parameters;

    public static SplineEditorFrame getInstance() {
        if (instance == null) {
            instance = new SplineEditorFrame();
        }

        return instance;
    }

    private SplineEditorFrame() {
        setMinimumSize(MINIMUM_SIZE);
        setSize(DEFAULT_SIZE);
        setPreferredSize(DEFAULT_SIZE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                hideEditor();
            }
        });

        panel = new SplineEditorViewPanel();
        parameters = new SplineEditorParameterPanel(panel);
        panel.setParameterPanel(parameters);

        setTitle("Spline Editor");
        setIconImage(ImageUtils.getScaledImageFromResources("/spline_editor_button.png", 32, 32).getImage());

        add(panel);
        add(parameters, BorderLayout.SOUTH);

        pack();
    }

    public void showEditor() {
        setVisible(true);
    }

    public void hideEditor() {
        setVisible(false);
    }
}
