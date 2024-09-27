package ru.nsu.fit.mihanizzm.tools.settings_windows;

import lombok.Getter;
import ru.nsu.fit.mihanizzm.tools.StarTool;
import ru.nsu.fit.mihanizzm.util.ViewElementsCreator;

import javax.swing.*;

public class StarToolSettingsWindow extends JDialog {
    private static final String THICKNESS_WARNING_MESSAGE = "Warning!\nLine thickness value must be an integer in the range from 1 to 500.";
    private static final String VERTICES_WARNING_MESSAGE = "Warning!\nNumber of vertices value must be an integer in the range from 3 to 16.";
    private static final String INNER_RADIUS_WARNING_MESSAGE =
            """
                    Warning!
                    Inner radius value must be an integer in the range from 1 to 499.""";
    private static final String OUTER_RADIUS_WARNING_MESSAGE =
            """
                    Warning!
                    Outer radius value must be an integer in the range from 2 to 500.""";
    private static final String RADIUSES_DIFFERENCE_WARNINNG_MESSAGE =
            """
                    Warning!
                    Outer radius value must be greater than inner radius value!
                    """;
    private static final String ANGLE_WARNING_MESSAGE = "Warning!\nAngle value must be an integer in the range from -360 to 360.";

    @Getter
    private boolean settingsApplied = false;
    private final JSlider thicknessSlider;
    private final JSlider numberOfVerticesSlider;
    private final JSlider innerRadiusSlider;
    private final JSlider outerRadiusSlider;
    private final JSlider angleSlider;

    public StarToolSettingsWindow(JFrame parent, StarTool starTool) {
        super(parent, "Line tool settings", true);
        setSize(480, 360);
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
        JLabel innerRadiusLabel = new JLabel("Inner radius:");
        JLabel outerRadiusLabel = new JLabel("Outer radius:");
        JLabel angleLabel = new JLabel("Angle:");

        thicknessSlider = ViewElementsCreator.createToolSettingsSlider(
                StarTool.LINE_THICKNESS_LOWER_CAP,
                StarTool.LINE_THICKNESS_UPPER_CAP,
                starTool.getLineThickness()
        );
        numberOfVerticesSlider = ViewElementsCreator.createToolSettingsSlider(
                StarTool.NUMBER_OF_VERTICES_LOWER_CAP,
                StarTool.NUMBER_OF_VERTICES_UPPER_CAP,
                starTool.getNumberOfVertices()
        );
        innerRadiusSlider = ViewElementsCreator.createToolSettingsSlider(
                StarTool.INNER_RADIUS_LOWER_CAP,
                StarTool.INNER_RADIUS_UPPER_CAP,
                starTool.getInnerRadius()
        );
        outerRadiusSlider = ViewElementsCreator.createToolSettingsSlider(
                StarTool.OUTER_RADIUS_LOWER_CAP,
                StarTool.OUTER_RADIUS_UPPER_CAP,
                starTool.getOuterRadius()
        );
        angleSlider = ViewElementsCreator.createToolSettingsSlider(
                StarTool.ANGLE_LOWER_CAP,
                StarTool.ANGLE_UPPER_CAP,
                starTool.getAngle()
        );

        JTextField thicknessTextField = ViewElementsCreator.createToolSettingsTextField(thicknessSlider, THICKNESS_WARNING_MESSAGE);
        JTextField numberOfVerticesTextField = ViewElementsCreator.createToolSettingsTextField(numberOfVerticesSlider, VERTICES_WARNING_MESSAGE);
        JTextField innerRadiusTextField = ViewElementsCreator.createToolSettingsTextField(innerRadiusSlider, INNER_RADIUS_WARNING_MESSAGE);
        JTextField outerRadiusTextField = ViewElementsCreator.createToolSettingsTextField(outerRadiusSlider, OUTER_RADIUS_WARNING_MESSAGE);
        JTextField angleTextField = ViewElementsCreator.createToolSettingsTextField(angleSlider, ANGLE_WARNING_MESSAGE);

        JButton applyButton = new JButton("Apply");
        JButton cancelButton = new JButton("Cancel");

        applyButton.addActionListener(e -> {
            if (innerRadiusSlider.getValue() >= outerRadiusSlider.getValue()) {
                JOptionPane.showMessageDialog(null, RADIUSES_DIFFERENCE_WARNINNG_MESSAGE, "Warning!", JOptionPane.WARNING_MESSAGE);
                return;
            }
            starTool.setLineThickness(thicknessSlider.getValue());
            starTool.setNumberOfVertices(numberOfVerticesSlider.getValue());
            starTool.setInnerRadius(innerRadiusSlider.getValue());
            starTool.setOuterRadius(outerRadiusSlider.getValue());
            starTool.setAngle(angleSlider.getValue());
            settingsApplied = true;
            dispose();
        });
        cancelButton.addActionListener(e -> dispose());

        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(thicknessLabel)
                .addComponent(numberOfVerticesLabel)
                .addComponent(innerRadiusLabel)
                .addComponent(outerRadiusLabel)
                .addComponent(angleLabel)
                .addComponent(applyButton)
        );
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(thicknessSlider)
                .addComponent(numberOfVerticesSlider)
                .addComponent(innerRadiusSlider)
                .addComponent(outerRadiusSlider)
                .addComponent(angleSlider)
                .addComponent(cancelButton)
        );
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(thicknessTextField)
                .addComponent(numberOfVerticesTextField)
                .addComponent(innerRadiusTextField)
                .addComponent(outerRadiusTextField)
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
                .addComponent(innerRadiusLabel)
                .addComponent(innerRadiusSlider)
                .addComponent(innerRadiusTextField)
        );

        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(outerRadiusLabel)
                .addComponent(outerRadiusSlider)
                .addComponent(outerRadiusTextField)
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
