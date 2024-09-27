package ru.nsu.fit.mihanizzm.util;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class ViewElementsCreator {
    public static JSlider createToolSettingsSlider(int min, int max, int current) {
        JSlider slider = new JSlider(min, max, current);
        slider.setMajorTickSpacing((max - min) / 2);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        return slider;
    }

    public static JTextField createToolSettingsTextField(JSlider slider, String warningMessage) {
        JTextField textField = new JTextField();
        textField.setText(Integer.toString(slider.getValue()));
        slider.addChangeListener(e -> textField.setText(Integer.toString(slider.getValue())));
        textField.addActionListener(e -> trySetSliderValueFromText(slider, textField, warningMessage));
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                trySetSliderValueFromText(slider, textField, warningMessage);
            }
        });
        return textField;
    }

    private static void trySetSliderValueFromText(JSlider slider, JTextField textField, String warningMessage) {
        try {
            int value = Integer.parseInt(textField.getText());
            if (value >= slider.getMinimum() && value <= slider.getMaximum()) {
                slider.setValue(value);
            }
            else {
                JOptionPane.showMessageDialog(null, warningMessage, "Warning!", JOptionPane.WARNING_MESSAGE);
                textField.setText(Integer.toString(slider.getValue()));
            }
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, warningMessage, "Warning!", JOptionPane.WARNING_MESSAGE);
            textField.setText(Integer.toString(slider.getValue()));
        }
    }
}
