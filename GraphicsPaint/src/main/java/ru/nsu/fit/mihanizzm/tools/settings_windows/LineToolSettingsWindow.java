package ru.nsu.fit.mihanizzm.tools.settings_windows;

import lombok.Getter;
import ru.nsu.fit.mihanizzm.tools.LineTool;
import ru.nsu.fit.mihanizzm.util.ViewElementsCreator;

import javax.swing.*;

public class LineToolSettingsWindow extends JDialog {
    private static final String WARNING_MESSAGE = "Warning!\nLine thickness must be an integer in the range from 1 to 500.";

    @Getter
    private boolean settingsApplied = false;
    private final JSlider thicknessSlider;

    public LineToolSettingsWindow(JFrame parent, LineTool lineTool) {
        super(parent, "Line tool settings", true);
        setSize(480, 160);
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

        thicknessSlider = ViewElementsCreator.createToolSettingsSlider(
                LineTool.LINE_THICKNESS_LOWER_CAP,
                LineTool.LINE_THICKNESS_UPPER_CAP,
                lineTool.getLineThickness()
        );

        JTextField thicknessTextField = ViewElementsCreator.createToolSettingsTextField(thicknessSlider, WARNING_MESSAGE);

        JButton applyButton = new JButton("Apply");
        JButton cancelButton = new JButton("Cancel");

        applyButton.addActionListener(e -> {
            lineTool.setLineThickness(thicknessSlider.getValue());
            settingsApplied = true;
            dispose();
        });
        cancelButton.addActionListener(e -> dispose());

        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(thicknessLabel)
                .addComponent(applyButton)
        );
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(thicknessSlider)
                .addComponent(cancelButton)
        );
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(thicknessTextField)
        );

        layout.setHorizontalGroup(hGroup);

        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(thicknessLabel)
                .addComponent(thicknessSlider)
                .addComponent(thicknessTextField)
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
