package ru.nsu.fit.mihanizzm.spline_editor_3d.model;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.fit.mihanizzm.spline_editor_3d.views.components.SplineDotPanel;

import java.awt.*;
import java.util.List;

@Getter
@Setter
public class Settings {
    // Spline editor
    private float scaleFactor;
    private Point offset;
    private Color splineColor;
    private List<SplineDotPanel> splineDots;
    private int dotCounter;
    private int numberOfSections;
    private int numberOfGenerativeSplines;
    private int numberOfCirclePartSegments;

    // 3D viewer
    private float planeDistance;
    private float[][] rotationMatrix;
}
