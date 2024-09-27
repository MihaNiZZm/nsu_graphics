package ru.nsu.fit.mihanizzm.spline_editor_3d.views.components;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import ru.nsu.fit.mihanizzm.spline_editor_3d.data.Point3D;
import ru.nsu.fit.mihanizzm.spline_editor_3d.model.ModelFormer3D;
import ru.nsu.fit.mihanizzm.spline_editor_3d.views.SplineEditorFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Log4j2
public class Model3DViewPanel extends JPanel {
    private static final float MAXIMUM_PLANE_DISTANCE = 9.50f;
    private static final float MINIMUM_PLANE_DISTANCE = 1f;
    private static final float WHEEL_ROTATION_ZOOM_STEP = 0.1f;

    private static final int AXES_SCALE_FACTOR = 40;
    private static final int AXES_OFFSET = 56;

    private static final float MOUSE_SENSITIVITY_FACTOR = 0.007f;

    private static final Color BACKGROUND_COLOR = Color.WHITE;

    private final float scaleFactor;

    @Getter
    private float planeDistance;

    @Getter
    @Setter
    private float[][] rotationMatrix = new float[][]
            {
                    { 1f, 0f, 0f },
                    { 0f, 1f, 0f },
                    { 0f, 0f, 1f }
            };

    @Getter
    @Setter
    private Point3D horizontalRotationVector = new Point3D(0f, 0f, 1f);
    @Getter
    @Setter
    private Point3D verticalRotationVector = new Point3D(0f, 1f, 0f);

    private float xMinDistance;
    private float xMaxDistance;

    private int mouseX;
    private int mouseY;

    @Getter
    private final ModelFormer3D model3D;

    private final Point3D cameraFocusPoint;
    private Point3D centerOfProjectingPlanePoint;

    private float[][] projectiveMatrix;

    private final Point3D[][] axesVectors = {
            { new Point3D(0f, 0f, 0f), new Point3D(1f, 0f, 0f) },
            { new Point3D(0f, 0f, 0f), new Point3D(0f, 1f, 0f) },
            { new Point3D(0f, 0f, 0f), new Point3D(0f, 0f, 1f) }
    };

    public void resetAngles() {
        rotationMatrix = new float[][]
                {
                        { 1f, 0f, 0f },
                        { 0f, 1f, 0f },
                        { 0f, 0f, 1f }
                };
        repaint();
    }

    public void setPlaneDistance(float planeDistance) {
        this.planeDistance = planeDistance;
        centerOfProjectingPlanePoint = new Point3D(-planeDistance, 0f, 0f);
        repaint();
    }

    public Model3DViewPanel() {
        scaleFactor = 1024f;

        float focusDistance = 10f;
        planeDistance = 7f;

        cameraFocusPoint = new Point3D(-focusDistance, 0f, 0f);
        centerOfProjectingPlanePoint = new Point3D(-planeDistance, 0f, 0f);

        model3D = new ModelFormer3D(this);

        addMouseWheelListener(e -> {
            if (e.getWheelRotation() < 0) {
                planeDistance = Math.max(MINIMUM_PLANE_DISTANCE, planeDistance - WHEEL_ROTATION_ZOOM_STEP);
            } else {
                planeDistance = Math.min(MAXIMUM_PLANE_DISTANCE, planeDistance + WHEEL_ROTATION_ZOOM_STEP);
            }
            centerOfProjectingPlanePoint = new Point3D(-planeDistance, 0f, 0f);
            repaint();
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                updateAngles(e.getX() - mouseX, e.getY() - mouseY);
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });

        setFocusable(true);
        setLayout(null);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(BACKGROUND_COLOR);
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setStroke(new BasicStroke(1));

        // Drawing axes (X Y Z)
        for (int i = 0; i < axesVectors.length; ++i) {
            Color startColor = switch(i) {
                case 0 -> Color.RED;
                case 1 -> Color.GREEN;
                case 2 -> Color.BLUE;
                default -> null;
            };
            String axisName = switch (i) {
                case 0 -> "x";
                case 1 -> "y";
                case 2 -> "z";
                default -> "";
            };
            Point3D[] projectedVector = new Point3D[2];
            for (int j = 0; j < 2; ++j) {
                projectedVector[j] = rotatePoint(axesVectors[i][j]);
            }
            g2.setColor(startColor);

            int x1 = (int) (projectedVector[0].getZ() * AXES_SCALE_FACTOR + AXES_OFFSET);
            int y1 = (int) (-projectedVector[0].getY() * AXES_SCALE_FACTOR + AXES_OFFSET);
            int x2 = (int) (projectedVector[1].getZ() * AXES_SCALE_FACTOR + AXES_OFFSET);
            int y2 = (int) (-projectedVector[1].getY() * AXES_SCALE_FACTOR + AXES_OFFSET);
            g2.drawLine(x1, y1, x2, y2);
            g2.drawString(axisName, x2 + 5, y2 - 5);
        }

        xMinDistance = 0f;
        xMaxDistance = 0f;

        if (model3D.getGeneratives() == null) {
            return;
        }
        Point3D[][] generatives = model3D.getGeneratives();
        Point3D[][] projectedGeneratives = new Point3D[generatives.length][generatives[0].length];
        for (int i = 0; i < generatives.length; ++i) {
            Point3D[] projectedGenerative = new Point3D[generatives[i].length];
            for (int j = 0; j < generatives[i].length; ++j) {
                projectedGenerative[j] = getProjectedPoint(generatives[i][j]);
            }
            projectedGeneratives[i] = projectedGenerative;
        }

        if (model3D.getCircles() == null) {
            return;
        }
        Point3D[][] circles = model3D.getCircles();
        Point3D[][] projectedCircles = new Point3D[circles.length][circles[0].length];
        for (int i = 0; i < circles.length; ++i) {
            Point3D[] projectedCircle = new Point3D[circles[i].length];
            for (int j = 0; j < circles[i].length; ++j) {
                projectedCircle[j] = getProjectedPoint(circles[i][j]);
            }
            projectedCircles[i] = projectedCircle;
        }

        // Drawing
        for (int i = 0; i < projectedCircles.length; ++i) {
            for (int j = 0; j < projectedCircles[i].length; ++j) {
                changeGraphicsParametersForCertainLine(
                        g2,
                        projectedCircles[i][j],
                        projectedCircles[i][(j + 1) % projectedCircles[i].length],
                        SplineEditorFrame.getInstance().getPanel().getSplineColor()
                );
                int x1 = transformToScreenCoordinates("z", projectedCircles[i][j].getZ());
                int y1 = transformToScreenCoordinates("y", projectedCircles[i][j].getY());
                int x2 = transformToScreenCoordinates("z", projectedCircles[i][(j + 1) % projectedCircles[i].length].getZ());
                int y2 = transformToScreenCoordinates("y", projectedCircles[i][(j + 1) % projectedCircles[i].length].getY());
                g2.drawLine(x1, y1, x2, y2);
            }
        }

        for (int i = 0; i < projectedGeneratives.length; ++i) {
            for (int j = 0; j < projectedGeneratives[i].length - 1; ++j) {
                changeGraphicsParametersForCertainLine(
                        g2,
                        projectedGeneratives[i][j],
                        projectedGeneratives[i][j + 1],
                        SplineEditorFrame.getInstance().getPanel().getSplineColor()
                );
                int x1 = transformToScreenCoordinates("z", projectedGeneratives[i][j].getZ());
                int y1 = transformToScreenCoordinates("y", projectedGeneratives[i][j].getY());
                int x2 = transformToScreenCoordinates("z", projectedGeneratives[i][j + 1].getZ());
                int y2 = transformToScreenCoordinates("y", projectedGeneratives[i][j + 1].getY());
                g2.drawLine(x1, y1, x2, y2);
            }
        }
    }

    private Point3D getProjectedPoint(Point3D figurePoint) {
        Point3D rotatedPoint = rotatePoint(figurePoint);

        float fx = rotatedPoint.getX() - cameraFocusPoint.getX();
        float fnx = centerOfProjectingPlanePoint.getX() - cameraFocusPoint.getX();
        float a = fnx / fx;

        projectiveMatrix = new float[][] {
                { 1f, 0f, 0f },
                { 0f, a, 0f },
                { 0f, 0f, a }
        };

        updateMaxAndMinDistances(rotatedPoint.getX());

        return matrixVector3Multiplication(projectiveMatrix, rotatedPoint);
    }

    private float[][] createVectorRotationMatrix(Point3D vector, float angle) {
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        float x = vector.getX();
        float y = vector.getY();
        float z = vector.getZ();
        return new float[][]
                {
                        { cos + (1 - cos) * x * x,      (1 - cos) * x * y - sin * z,    (1 - cos) * x * z + sin * y },
                        { (1 - cos) * y * x + sin * z,  cos + (1 - cos) * y * y,        (1 - cos) * y * z - sin * x },
                        { (1 - cos) * z * x - sin * y,  (1 - cos) * z * y + sin * x,    cos + (1 - cos) * z * z }
                };
    }

    private Point3D rotatePoint(Point3D point) {
        Point3D result = matrixVector3Multiplication(rotationMatrix, point);
        return result;
    }

    private int transformToScreenCoordinates(String axis, float point) {
        return switch (axis) {
            case "y" -> (int) ((-point * scaleFactor) + getHeight() / 2);
            case "z" -> (int) ((point * scaleFactor) + getWidth() / 2);
            default -> throw new IllegalStateException("Unexpected value: " + axis);
        };
    }

    private Point3D matrixVector3Multiplication(float[][] matrix, Point3D vector) {
        float[] input = point3DToFloatArray(vector);
        float[] result = new float[3];
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                result[i] += matrix[i][j] * input[j];
            }
        }
        return floatArrayToPoint3D(result);
    }

    private float[][] matrixMatrixMultiplication(float[][] matrix1, float[][] matrix2) {
        int m1Rows = matrix1.length;
        int m1Cols = matrix1[0].length;
        int m2Cols = matrix2[0].length;

        float[][] result = new float[m1Rows][m2Cols];

        for (int i = 0; i < m1Rows; i++) {
            for (int j = 0; j < m2Cols; j++) {
                for (int k = 0; k < m1Cols; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }

        return result;
    }

    private float[] point3DToFloatArray(Point3D point) {
        return new float[] { point.getX(), point.getY(), point.getZ() };
    }

    private Point3D floatArrayToPoint3D (float[] array) {
        return new Point3D(array[0], array[1], array[2]);
    }

    private void updateMaxAndMinDistances(float val) {
        xMinDistance = Math.min(xMinDistance, val);
        xMaxDistance = Math.max(xMaxDistance, val);
    }

    private float normalizeDistanceFactor(float xDistance) {
        return (xDistance - xMinDistance) / (xMaxDistance - xMinDistance);
    }

    private void changeGraphicsParametersForCertainLine(Graphics2D g2, Point3D start, Point3D end, Color startColor) {
        float factor = (normalizeDistanceFactor(start.getX()) + normalizeDistanceFactor(end.getX())) / 2;
        g2.setStroke(getStrokeByFactor(factor));
        g2.setColor(updateColorByFactor(factor, startColor));
    }

    private Stroke getStrokeByFactor(float factor) {
        return new BasicStroke((int) (10 - (factor * 9.5f)));
    }

    private Color updateColorByFactor(float factor, Color startColor) {
        float r = startColor.getRed() / 255f;
        float g = startColor.getGreen() / 255f;
        float b = startColor.getBlue() / 255f;

        float max = Math.max(Math.max(r, g), b);
        float saturation = 1f - (Math.min(Math.min(r, g), b)) / max;

        saturation -= (1 - factor * 0.5f);
        if (saturation < 0f) {
            saturation = 0f;
        }

        if (max != 0) {
            r += (max - r) * saturation / max;
            g += (max - g) * saturation / max;
            b += (max - b) * saturation / max;
        }

        int newR = (int) (r * 255f);
        int newG = (int) (g * 255f);
        int newB = (int) (b * 255f);

        return new Color(newR, newG, newB);
    }

    private void updateAngles(int dx, int dy) {
        float xShift = dx * MOUSE_SENSITIVITY_FACTOR;
        float yShift = dy * MOUSE_SENSITIVITY_FACTOR;

        if (xShift != 0f) {
            rotationMatrix = matrixMatrixMultiplication(
                    createVectorRotationMatrix(verticalRotationVector, xShift),
                    rotationMatrix
            );
        }
        if (yShift != 0f) {
            rotationMatrix = matrixMatrixMultiplication(
                    createVectorRotationMatrix(horizontalRotationVector, yShift),
                    rotationMatrix
            );
        }

        repaint();
    }
}
