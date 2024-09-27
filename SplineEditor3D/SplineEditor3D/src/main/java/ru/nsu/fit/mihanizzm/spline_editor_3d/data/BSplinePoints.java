package ru.nsu.fit.mihanizzm.spline_editor_3d.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class BSplinePoints {
    List<Point2D> pivotPoints = new ArrayList<>();

    public void add(Point2D point) {
        pivotPoints.add(point);
    }

    public void remove(int idx) {
        pivotPoints.remove(idx);
    }
}
