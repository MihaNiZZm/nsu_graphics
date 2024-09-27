package ru.nsu.fit.mihanizzm.spline_editor_3d.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ru.nsu.fit.mihanizzm.spline_editor_3d.data.Point3D;
import ru.nsu.fit.mihanizzm.spline_editor_3d.views.components.Model3DViewPanel;

@NoArgsConstructor
@Log4j2
public class ModelFormer3D {
    private int amountOfSections;
    private int amountOfPivots;
    private int amountOfGeneratives;
    private int amountOfCirclePartSegments;

    @Getter
    private Point3D[][] generatives;
    @Getter
    private Point3D[][] circles;
    @Getter
    private float normalizeFactor;

    private Model3DViewPanel parentPanel;

    public ModelFormer3D(Model3DViewPanel parent) {
        this.parentPanel = parent;
    }

    public void buildModel3D(float[] uSplinePoints, float[] vSplinePoints, int k, int n, int m, int m1) {
        if (k < 4) {
            generatives = null;
            circles = null;
            parentPanel.repaint();
            return;
        }
        normalizeFactor = 0f;

        this.amountOfPivots = k;
        this.amountOfSections = n;
        this.amountOfGeneratives = m;
        this.amountOfCirclePartSegments = m1;

        generatives = new Point3D[m][(k - 3) * n + 1];
        circles = new Point3D[k - 2][m * m1];

        buildGeneratives(uSplinePoints, vSplinePoints);
        buildCircles();

        parentPanel.repaint();
    }

    private void setGenerative(int idx, Point3D[] generativePoints) {
        generatives[idx] = generativePoints;
    }

    private void setCircle(int idx, Point3D[] circlePoints) {
        circles[idx] = circlePoints;
    }

    private void buildGeneratives(float[] uSplinePoints, float[] vSplinePoints) {
        if (
                uSplinePoints.length != (amountOfPivots - 3) * amountOfSections + 1
                || vSplinePoints.length != (amountOfPivots - 3) * amountOfSections + 1) {
            log.error("The length of generative points is not matching with (K-3) * N + 1 in \"buildGeneratives()\" method");
            return;
        }

        for (int m = 0; m < amountOfGeneratives; ++m) {
            double angle = Math.toRadians((double) (m * 360) / amountOfGeneratives);
            Point3D[] newGenerative = new Point3D[uSplinePoints.length];
            float sin = (float) Math.sin(angle);
            float cos = (float) Math.cos(angle);

            for (int k = 0; k < uSplinePoints.length; ++k) {
                float x = vSplinePoints[k] * cos;
                float y = vSplinePoints[k] * sin;
                float z = uSplinePoints[k];

                updateNormalizeFactor(x, y, z);

                newGenerative[k] = new Point3D(x, y, z);
            }

            setGenerative(m, newGenerative);
        }
    }

    private void buildCircles() {
        Point3D[] circlesStartPoints = new Point3D[amountOfPivots - 2];
        for (int i = 0; i < amountOfPivots - 3; ++i) {
            int idx = i * amountOfSections;
            circlesStartPoints[i] = Point3D.copyPoint(generatives[0][idx]);
        }
        circlesStartPoints[amountOfPivots - 3] = Point3D.copyPoint(generatives[0][generatives[0].length - 1]);

        for (int i = 0; i < circlesStartPoints.length; ++i) {
            Point3D[] circle = new Point3D[amountOfGeneratives * amountOfCirclePartSegments];

            for (int j = 0; j < amountOfGeneratives * amountOfCirclePartSegments; ++j) {
                double angle = Math.toRadians((double) (j * 360) / (amountOfGeneratives * amountOfCirclePartSegments));
                float sin = (float) Math.sin(angle);
                float cos = (float) Math.cos(angle);

                float x = circlesStartPoints[i].getX() * cos;
                float y = circlesStartPoints[i].getX() * sin;
                float z = circlesStartPoints[i].getZ();

                updateNormalizeFactor(x, y, z);

                circle[j] = new Point3D(x, y, z);
            }

            setCircle(i, circle);
        }
    }

    private void updateNormalizeFactor(float x, float y, float z) {
        float absX = Math.abs(x);
        float absY = Math.abs(y);
        float absZ = Math.abs(z);
        normalizeFactor = Math.max(normalizeFactor, Math.max(absX, Math.max(absY, absZ)));
    }
}
