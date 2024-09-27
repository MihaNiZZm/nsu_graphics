package ru.nsu.fit.mihanizzm.spline_editor_3d.model;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.fit.mihanizzm.spline_editor_3d.views.MainFrame;
import ru.nsu.fit.mihanizzm.spline_editor_3d.views.SplineEditorFrame;
import ru.nsu.fit.mihanizzm.spline_editor_3d.views.components.Model3DViewPanel;
import ru.nsu.fit.mihanizzm.spline_editor_3d.views.components.SplineDotPanel;
import ru.nsu.fit.mihanizzm.spline_editor_3d.views.components.SplineEditorViewPanel;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class SettingsManager {
    private Settings settings;

    private static SettingsManager instance;

    private SettingsManager() {

    }

    public static SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }

    public void saveState() {
        settings = new Settings();

        SplineEditorViewPanel splinePanel = SplineEditorFrame.getInstance().getPanel();
        settings.setScaleFactor(splinePanel.getScaleFactor());
        settings.setOffset(splinePanel.getOffset());
        settings.setSplineColor(splinePanel.getSplineColor());
        settings.setSplineDots(splinePanel.getSplineDots());
        settings.setDotCounter(splinePanel.getDotCounter());
        settings.setNumberOfSections(splinePanel.getNumberOfSections());
        settings.setNumberOfGenerativeSplines(splinePanel.getNumberOfGenerativeSplines());
        settings.setNumberOfCirclePartSegments(splinePanel.getNumberOfCirclePartSegments());

        Model3DViewPanel viewPanel = MainFrame.getInstance().getViewPanel();
        settings.setPlaneDistance(viewPanel.getPlaneDistance());
        settings.setRotationMatrix(viewPanel.getRotationMatrix());
    }

    public void updateState() {
        SplineEditorViewPanel splinePanel = SplineEditorFrame.getInstance().getPanel();

        splinePanel.clearDots();

        splinePanel.setScaleFactor(settings.getScaleFactor());
        splinePanel.setOffset(settings.getOffset());
        splinePanel.setSplineColor(settings.getSplineColor());
        splinePanel.setSplineDots(settings.getSplineDots());
        splinePanel.setDotCounter(settings.getDotCounter());
        splinePanel.setNumberOfSections(settings.getNumberOfSections());
        splinePanel.setNumberOfGenerativeSplines(settings.getNumberOfGenerativeSplines());
        splinePanel.setNumberOfCirclePartSegments(settings.getNumberOfCirclePartSegments());

        Model3DViewPanel viewPanel = MainFrame.getInstance().getViewPanel();
        viewPanel.setPlaneDistance(settings.getPlaneDistance());
        viewPanel.setRotationMatrix(settings.getRotationMatrix());

        updateAndRepaint(splinePanel, viewPanel);
    }

    public void setInitialSettings() {
        SplineEditorViewPanel splinePanel = SplineEditorFrame.getInstance().getPanel();
        Model3DViewPanel viewPanel = MainFrame.getInstance().getViewPanel();

        List<SplineDotPanel> splineDots = new ArrayList<>();
        splineDots.add(new SplineDotPanel(1, false, new Point2D.Float(0f, 0.33333334f)));
        splineDots.add(new SplineDotPanel(2, false, new Point2D.Float(-0.33333334f, 0.6666667f)));
        splineDots.add(new SplineDotPanel(3, false, new Point2D.Float(0f, 1f)));
        splineDots.add(new SplineDotPanel(4, false, new Point2D.Float(0.33333334f, 0.6666667f)));
        splineDots.add(new SplineDotPanel(5, false, new Point2D.Float(0f, 0.33333334f)));
        splineDots.add(new SplineDotPanel(6, false, new Point2D.Float(-0.33333334f, 0.6666667f)));
        splineDots.add(new SplineDotPanel(7, true, new Point2D.Float(0f, 1f)));

        float[][] rotationMatrix =
                {
                        { -0.005576938f,    0.63352555f,    -0.7736981f },
                        { -0.019316943f,    0.7734978f,     0.6335005f },
                        { 0.99979573f,      0.018478729f,   0.007924143f }
                };

        splinePanel.setScaleFactor(240f);
        splinePanel.setOffset(new Point(0, 0));
        splinePanel.setSplineColor(new Color(160, 0, 255));
        splinePanel.setSplineDots(splineDots);
        splinePanel.setDotCounter(7);
        splinePanel.setNumberOfSections(16);
        splinePanel.setNumberOfGenerativeSplines(6);
        splinePanel.setNumberOfCirclePartSegments(16);

        viewPanel.setPlaneDistance(6.15f);
        viewPanel.setRotationMatrix(rotationMatrix);

        updateAndRepaint(splinePanel, viewPanel);
    }

    private void updateAndRepaint(SplineEditorViewPanel splinePanel, Model3DViewPanel viewPanel) {
        splinePanel.addDots();
        splinePanel.repaint();
        splinePanel.updateSpline();
        splinePanel.buildModel();
        SplineEditorFrame.getInstance().getParameters().updateData();
        viewPanel.repaint();
    }
}
