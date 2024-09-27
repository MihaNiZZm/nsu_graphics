package ru.nsu.fit.mihanizzm.spline_editor_3d.views;

import lombok.Getter;
import ru.nsu.fit.mihanizzm.spline_editor_3d.model.MenuAndToolbarBundle;
import ru.nsu.fit.mihanizzm.spline_editor_3d.utils.ImageUtils;
import ru.nsu.fit.mihanizzm.spline_editor_3d.views.components.Model3DViewPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static final Dimension MINIMUM_SIZE = new Dimension(640, 480);
    private static final Dimension DEFAULT_SIZE = new Dimension(1280, 720);
    private static MainFrame instance;

    @Getter
    private Model3DViewPanel viewPanel;

    public static MainFrame getInstance() {
        if (instance == null) {
            instance = new MainFrame();
        }

        return instance;
    }

    private MainFrame() {
        setMinimumSize(MINIMUM_SIZE);
        setSize(DEFAULT_SIZE);
        setPreferredSize(DEFAULT_SIZE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(ImageUtils.getImageFromResources("/spline_editor_logo_icon.png").getImage());

        setTitle("Spline Viewer 3D");
        viewPanel = new Model3DViewPanel();
        add(viewPanel, BorderLayout.CENTER);

        add(Box.createRigidArea(new Dimension(5, 5)), BorderLayout.SOUTH);
        add(Box.createRigidArea(new Dimension(5, 5)), BorderLayout.WEST);
        add(Box.createRigidArea(new Dimension(5, 5)), BorderLayout.EAST);

        MenuAndToolbarBundle menuAndToolbarBundle = new MenuAndToolbarBundle();
        setJMenuBar(menuAndToolbarBundle.getMenu());
        add(menuAndToolbarBundle.getToolBar(), BorderLayout.NORTH);

        pack();
        setVisible(true);
    }


}
