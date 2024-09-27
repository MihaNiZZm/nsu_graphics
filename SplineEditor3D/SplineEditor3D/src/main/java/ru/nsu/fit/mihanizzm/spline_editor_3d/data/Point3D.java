package ru.nsu.fit.mihanizzm.spline_editor_3d.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Point3D {
    private float x;
    private float y;
    private float z;

    public static Point3D copyPoint(Point3D p) {
        return new Point3D(p.x, p.y, p.z);
    }

    public void normalize() {
        float magnitude = (float) Math.sqrt(x * x + y * y + z * z);
        if (magnitude != 0f) {
            x /= magnitude;
            y /= magnitude;
            z /= magnitude;
        }
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f, %.2f)", x, y, z);
    }
}
