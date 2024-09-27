package ru.nsu.fit.mihanizzm.tools.settings_windows;

import lombok.Getter;
import ru.nsu.fit.mihanizzm.tools.PolygonTool;
import ru.nsu.fit.mihanizzm.util.ViewElementsCreator;

import javax.swing.*;

public class PolygonToolSettingsWindow extends JDialog {
    private static final String THICKNESS_WARNING_MESSAGE = "Warning!\nLine thickness value must be an integer in the range from 1 to 500.";
    private static final String VERTICES_WARNING_MESSAGE = "Warning!\nNumber of vertices value must be an integer in the range from 3 to 16.";
    private static final String RADIUS_WARNING_MESSAGE = "Warning!\nRadius value must be an integer in the range from 1 to 500.";
    private static final String ANGLE_WARNING_MESSAGE = "Warning!\nAngle value must be an integer in the range from -360 to 360.";

    @Getter
    private boolean settingsApplied = false;
    private final JSlider thicknessSlider;
    private final JSlider numberOfVerticesSlider;
    private final JSlider radiusSlider;
    private final JSlider angleSlider;

    public PolygonToolSettingsWindow(JFrame parent, PolygonTool polygonTool) {
        super(parent, "Line tool settings", true);
        setSize(480, 300);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel settingsPanel = new JPanel();
        GroupLayout layout = new GroupLayout(settingsPanel);
        settingsPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

        JLabel thicknessLabel = new JLabel("Thickness:");
        JLabel numberOfVerticesLabel = new JLabel("Number of vertices:");
        JLabel radiusLabel = new JLabel("Radius:");
        JLabel angleLabel = new JLabel("Angle:");

        thicknessSlider = ViewElementsCreator.createToolSettingsSlider(
                PolygonTool.LINE_THICKNESS_LOWER_CAP,
                PolygonTool.LINE_THICKNESS_UPPER_CAP,
                polygonTool.getLineThickness()
        );
        numberOfVerticesSlider = ViewElementsCreator.createToolSettingsSlider(
                PolygonTool.NUMBER_OF_VERTICES_LOWER_CAP,
                PolygonTool.NUMBER_OF_VERTICES_UPPER_CAP,
                polygonTool.getNumberOfVertices()
        );
        radiusSlider = ViewElementsCreator.createToolSettingsSlider(
                PolygonTool.RADIUS_LOWER_CAP,
                PolygonTool.RADIUS_UPPER_CAP,
                polygonTool.getRadius()
        );
        angleSlider = ViewElementsCreator.createToolSettingsSlider(
                PolygonTool.ANGLE_LOWER_CAP,
                PolygonTool.ANGLE_UPPER_CAP,
                polygonTool.getAngle()
        );

        JTextField thicknessTextField = ViewElementsCreator.createToolSettingsTextField(thicknessSlider, THICKNESS_WARNING_MESSAGE);
        JTextField numberOfVerticesTextField = ViewElementsCreator.createToolSettingsTextField(numberOfVerticesSlider, VERTICES_WARNING_MESSAGE);
        JTextField radiusTextField = ViewElementsCreator.createToolSettingsTextField(radiusSlider, RADIUS_WARNING_MESSAGE);
        JTextField angleTextField = ViewElementsCreator.createToolSettingsTextField(angleSlider, ANGLE_WARNING_MESSAGE);

        JButton applyButton = new JButton("Apply");
        JButton cancelButton = new JButton("Cancel");

        applyButton.addActionListener(e -> {
            polygonTool.setLineThickness(thicknessSlider.getValue());
            polygonTool.setNumberOfVertices(numberOfVerticesSlider.getValue());
            polygonTool.setRadius(radiusSlider.getValue());
            polygonTool.setAngle(angleSlider.getValue());
            settingsApplied = true;
            dispose();
        });
        cancelButton.addActionListener(e -> dispose());

        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(thicknessLabel)
                .addComponent(numberOfVerticesLabel)
                .addComponent(radiusLabel)
                .addComponent(angleLabel)
                .addComponent(applyButton)
        );
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(thicknessSlider)
                .addComponent(numberOfVerticesSlider)
                .addComponent(radiusSlider)
                .addComponent(angleSlider)
                .addComponent(cancelButton)
        );
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(thicknessTextField)
                .addComponent(numberOfVerticesTextField)
                .addComponent(radiusTextField)
                .addComponent(angleTextField)
        );

        layout.setHorizontalGroup(hGroup);

        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(thicknessLabel)
                .addComponent(thicknessSlider)
                .addComponent(thicknessTextField)
        );

        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(numberOfVerticesLabel)
                .addComponent(numberOfVerticesSlider)
                .addComponent(numberOfVerticesTextField)
        );

        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(radiusLabel)
                .addComponent(radiusSlider)
                .addComponent(radiusTextField)
        );

        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(angleLabel)
                .addComponent(angleSlider)
                .addComponent(angleTextField)
        );

        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(applyButton)
                .addComponent(cancelButton)
        );

        layout.setVerticalGroup(vGroup);

        add(settingsPanel);

        setResizable(false);
        setModal(true);

        setVisible(true);
    }
}
