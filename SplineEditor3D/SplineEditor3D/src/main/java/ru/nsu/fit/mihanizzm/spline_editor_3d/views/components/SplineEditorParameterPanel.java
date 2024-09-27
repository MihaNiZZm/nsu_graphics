package ru.nsu.fit.mihanizzm.spline_editor_3d.views.components;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.fit.mihanizzm.spline_editor_3d.views.SplineEditorFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

@Setter
public class SplineEditorParameterPanel extends JPanel {
    private static final Dimension MAXIMUM_PANEL_SIZE = new Dimension(190, 80);

    private SplineEditorViewPanel view;

    private int red;
    private int green;
    private int blue;

    private JPanel rgbPanel;
    private JSpinner redSpinner;
    private JSpinner greenSpinner;
    private JSpinner blueSpinner;

    private JPanel splineParametersPanel;
    private JSpinner nSpinner;
    private JSpinner mSpinner;
    private JSpinner m1Spinner;

    private JPanel dotCoordinatesPanel;
    @Getter
    private JSpinner uSpinner;
    @Getter
    private JSpinner vSpinner;

    public void updateData() {
        Color temp = view.getSplineColor();
        redSpinner.setValue(temp.getRed());
        greenSpinner.setValue(temp.getGreen());
        blueSpinner.setValue(temp.getBlue());

        nSpinner.setValue(view.getNumberOfSections());
        mSpinner.setValue(view.getNumberOfGenerativeSplines());
        m1Spinner.setValue(view.getNumberOfCirclePartSegments());

        uSpinner.setValue((double) view.getSelectedDot().getCoordinates().x);
        vSpinner.setValue((double) view.getSelectedDot().getCoordinates().y);
    }

    public SplineEditorParameterPanel(SplineEditorViewPanel view) {
        this.view = view;
        red = view.getSplineColor() != null ? view.getSplineColor().getRed() : 255;
        green = view.getSplineColor() != null ? view.getSplineColor().getGreen() : 255;
        blue = view.getSplineColor() != null ? view.getSplineColor().getBlue() : 255;

        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.CENTER;

        // Add parameters:
        // RGB toggles for spline curve, (done)
        // M (amount of generative splines), (done)
        // M1 (amount of circle part segments), (done)
        // N (amount of sections between spline pivots), (done)
        // u, v coordinates of a selected point, (done)
        // OK button (done)
        // Normalize button (done)

        configureRGBPanel(constraints);
        configureSplineParametersPanel(constraints);
        configureDotCoordinatesPanel(constraints);

        JButton okButton = new JButton("OK");
        okButton.setMaximumSize(new Dimension(100, 20));
        okButton.setFocusable(false);
        okButton.addActionListener(e -> SplineEditorFrame.getInstance().hideEditor());

        constraints.gridx = 1;
        constraints.gridy = 1;
        add(okButton, constraints);

        JButton normalizeButton = new JButton("Normalize");
        normalizeButton.setMaximumSize(new Dimension(100, 20));
        normalizeButton.setFocusable(false);
        normalizeButton.addActionListener(e -> view.normalize());

        constraints.gridx = 2;
        constraints.gridy = 1;
        add(normalizeButton, constraints);
    }

    private void configureDotCoordinatesPanel(GridBagConstraints constraints) {
        dotCoordinatesPanel = new JPanel();
        dotCoordinatesPanel.setMaximumSize(MAXIMUM_PANEL_SIZE);
        dotCoordinatesPanel.setPreferredSize(MAXIMUM_PANEL_SIZE);

        uSpinner = createSpinner(
                new SpinnerNumberModel(0.0, -1000.0, 1000.0, 0.001),
                () -> {
                    double x = (double) uSpinner.getValue();
                    float y = view.getSelectedDot().getCoordinates().y;
                    view.getSelectedDot().setCoordinates(new Point2D.Float((float) x, y));
                    view.repaint();
                }
        );

        vSpinner = createSpinner(
                new SpinnerNumberModel(0.0, -1000.0, 1000.0, 0.001),
                () -> {
                    float x = view.getSelectedDot().getCoordinates().x;
                    double y = (double) vSpinner.getValue();
                    view.getSelectedDot().setCoordinates(new Point2D.Float(x, (float) y));
                    view.repaint();
                }
        );

        JSpinner.NumberEditor uFormat = new JSpinner.NumberEditor(uSpinner, "0.000");
        JSpinner.NumberEditor vFormat = new JSpinner.NumberEditor(vSpinner, "0.000");

        uSpinner.setEditor(uFormat);
        vSpinner.setEditor(vFormat);

        ParameterFieldPanel uPanel = new ParameterFieldPanel(new JLabel("Selected U:"), uSpinner);
        ParameterFieldPanel vPanel = new ParameterFieldPanel(new JLabel("Selected V:"), vSpinner);

        dotCoordinatesPanel.add(uPanel);
        dotCoordinatesPanel.add(vPanel);

        constraints.gridx = 2;
        constraints.gridy = 0;
        add(dotCoordinatesPanel, constraints);
    }

    private void configureSplineParametersPanel(GridBagConstraints constraints) {
        splineParametersPanel = new JPanel();
        splineParametersPanel.setMaximumSize(MAXIMUM_PANEL_SIZE);
        splineParametersPanel.setPreferredSize(MAXIMUM_PANEL_SIZE);

        nSpinner = createSpinner(
                new SpinnerNumberModel(view.getNumberOfSections(), 1, 100, 1),
                () -> view.setNumberOfSections((int) nSpinner.getValue())
        );

        mSpinner = createSpinner(
                new SpinnerNumberModel(view.getNumberOfGenerativeSplines(), 2, 16, 1),
                () -> view.setNumberOfGenerativeSplines((int) mSpinner.getValue())
        );

        m1Spinner = createSpinner(
                new SpinnerNumberModel(view.getNumberOfCirclePartSegments(), 1, 16, 1),
                () -> view.setNumberOfCirclePartSegments((int) m1Spinner.getValue())
        );

        ParameterFieldPanel nPanel = new ParameterFieldPanel(new JLabel("N:"), nSpinner);
        ParameterFieldPanel mPanel = new ParameterFieldPanel(new JLabel("M:"), mSpinner);
        ParameterFieldPanel m1Panel = new ParameterFieldPanel(new JLabel("M1:"), m1Spinner);

        splineParametersPanel.add(nPanel);
        splineParametersPanel.add(mPanel);
        splineParametersPanel.add(m1Panel);

        constraints.gridx = 1;
        constraints.gridy = 0;
        add(splineParametersPanel, constraints);
    }

    private void configureRGBPanel(GridBagConstraints constraints) {
        rgbPanel = new JPanel();
        rgbPanel.setMaximumSize(MAXIMUM_PANEL_SIZE);
        rgbPanel.setPreferredSize(MAXIMUM_PANEL_SIZE);

        redSpinner = createSpinner(
                new SpinnerNumberModel(red, 0, 255, 1),
                this::handleRedChange
        );

        greenSpinner = createSpinner(
                new SpinnerNumberModel(green, 0, 255, 1),
                this::handleGreenChange
        );

        blueSpinner = createSpinner(
                new SpinnerNumberModel(blue, 0, 255, 1),
                this::handleBlueChange
        );

        ParameterFieldPanel redPanel = new ParameterFieldPanel(new JLabel("Spline Red:"), redSpinner);
        ParameterFieldPanel greenPanel = new ParameterFieldPanel(new JLabel("Spline Green:"), greenSpinner);
        ParameterFieldPanel bluePanel = new ParameterFieldPanel(new JLabel("Spline Green:"), blueSpinner);

        rgbPanel.add(redPanel);
        rgbPanel.add(greenPanel);
        rgbPanel.add(bluePanel);

        constraints.gridx = 0;
        constraints.gridy = 0;
        add(rgbPanel, constraints);
    }

    private JSpinner createSpinner(SpinnerNumberModel model, Runnable onChange) {
        JSpinner result = new JSpinner(model);
        result.addChangeListener(e -> onChange.run());

        return result;
    }

    private void setSplineColor() {
        view.setSplineColor(new Color(red, green, blue));
        view.repaint();
    }

    private void handleRedChange() {
        red = (int) redSpinner.getValue();
        setSplineColor();
    }

    private void handleGreenChange() {
        green = (int) greenSpinner.getValue();
        setSplineColor();
    }

    private void handleBlueChange() {
        blue = (int) blueSpinner.getValue();
        setSplineColor();
    }
}
