package ru.nsu.fit.mihanizzm.spline_editor_3d.views.components;


import lombok.Getter;
import lombok.Setter;
import ru.nsu.fit.mihanizzm.spline_editor_3d.model.BSplineCreator;
import ru.nsu.fit.mihanizzm.spline_editor_3d.views.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class SplineEditorViewPanel extends JPanel {
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private static final Color AXIS_LINES_COLOR = Color.WHITE;
    private static final Color PIVOT_POINTS_SECTIONS_COLOR = Color.RED;

    private static final int LABEL_X_OFFSET = 10;
    private static final int LABEL_Y_OFFSET = -10;
    private static final int MARK_LINE_LENGTH = 10;
    private static final int BIG_MARK_LINE_LENGTH = 20;

    private static final float MOUSE_WHEEL_SCALE_FACTOR_INCREMENT = 1.05f;
    private static final float MAXIMUM_SCALE_FACTOR = 480f;
    private static final float MINIMUM_SCALE_FACTOR = 16f;

    @Getter
    @Setter
    private Color splineColor = Color.WHITE;
    @Setter
    @Getter
    private float scaleFactor = 100f;
    @Setter
    @Getter
    private Point offset = new Point(0, 0);
    private Point previousMousePoint = new Point(0, 0);

    @Getter
    private int numberOfSections = 10;
    @Getter
    private int numberOfGenerativeSplines = 6;
    @Getter
    private int numberOfCirclePartSegments = 4;

    @Setter
    @Getter
    private List<SplineDotPanel> splineDots = new ArrayList<>();
    @Setter
    @Getter
    private SplineDotPanel selectedDot = null;
    @Setter
    @Getter
    private int dotCounter = 0;

    @Getter
    private float[] uSplinePoints;
    @Getter
    private float[] vSplinePoints;

    private int[] xPivots;
    private int[] yPivots;

    private int[] xSplineScreenCoords;
    private int[] ySplineScreenCoords;

    @Setter
    private SplineEditorParameterPanel parameterPanel;

    public void setNumberOfSections(int value) {
        numberOfSections = value;
        repaint();
    }

    public void setNumberOfGenerativeSplines(int value) {
        numberOfGenerativeSplines = value;
        repaint();
    }

    public void setNumberOfCirclePartSegments(int value) {
        numberOfCirclePartSegments = value;
        repaint();
    }

    public void normalize() {
        float normalizeFactor = 0f;
        for (SplineDotPanel dot : splineDots) {
            float x = Math.abs(dot.getCoordinates().x);
            float y = Math.abs(dot.getCoordinates().y);
            normalizeFactor = Math.max(normalizeFactor, Math.max(x, y));
        }

        for (SplineDotPanel dot : splineDots) {
            float x = dot.getCoordinates().x / normalizeFactor;
            float y = dot.getCoordinates().y / normalizeFactor;
            dot.setCoordinates(new Point2D.Float(x, y));
        }
        offset.x = 0;
        offset.y = 0;
        scaleFactor = 240f;
        repaint();
    }

    public SplineEditorViewPanel() {
        setLayout(null);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                previousMousePoint = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    addPointOnPlane(e.getPoint());
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                int xShift = e.getX() - previousMousePoint.x;
                int yShift = e.getY() - previousMousePoint.y;
                offset.x -= xShift;
                offset.y += yShift;

                previousMousePoint.x = e.getX();
                previousMousePoint.y = e.getY();
                repaint();
            }
        });

        addMouseWheelListener(e -> handleMouseWheelRotation(e.getWheelRotation(), e.getScrollAmount()));
    }

    @Override
    public void paintComponent(Graphics g) {
        xPivots = new int[splineDots.size()];
        yPivots = new int[splineDots.size()];
        float[] splineDotsX = new float[splineDots.size()];
        float[] splineDotsY = new float[splineDots.size()];
        for (int i = 0; i < splineDots.size(); ++i) {
            splineDots.get(i).setLocation(planeCoordsToScreenCoords(splineDots.get(i).getCoordinates()));
            xPivots[i] = splineDots.get(i).getLocation().x;
            yPivots[i] = splineDots.get(i).getLocation().y;
            splineDotsX[i] = splineDots.get(i).getCoordinates().x;
            splineDotsY[i] = splineDots.get(i).getCoordinates().y;
        }

        if (splineDots.size() >= 4) {
            uSplinePoints = BSplineCreator.getSplinePoints(splineDotsX, numberOfSections);
            vSplinePoints = BSplineCreator.getSplinePoints(splineDotsY, numberOfSections);

            xSplineScreenCoords = new int[uSplinePoints.length];
            ySplineScreenCoords = new int[vSplinePoints.length];
            for (int i = 0; i < uSplinePoints.length; ++i) {
                Point temp = planeCoordsToScreenCoords(new Point2D.Float(uSplinePoints[i], vSplinePoints[i]));
                xSplineScreenCoords[i] = temp.x;
                ySplineScreenCoords[i] = temp.y;
            }
        }

        Point center = new Point(getWidth() / 2, getHeight() / 2);
        Graphics2D g2 = (Graphics2D) g;
        Font font = new Font("Arial", Font.PLAIN, 14);
        g2.setFont(font);

        g2.setColor(BACKGROUND_COLOR);
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(AXIS_LINES_COLOR);

        // Drawing horizontal axis line
        g2.drawLine(0, center.y + offset.y,
                getWidth(), center.y + offset.y
        );
        // Drawing vertical axis line
        g2.drawLine(center.x - offset.x, 0,
                center.x - offset.x, getHeight()
        );

        // Drawing labels of axes
        g2.drawString("U", getWidth() - 15, offset.y + center.y + 18);
        g2.drawString("V", center.x - offset.x - 18, 15);

        // Drawing marks on horizontal axis
        for (int i = (int) ((offset.x - center.x) / scaleFactor); i <= (int) ((offset.x + getWidth() - center.x) / scaleFactor); ++i) {
            int x = -offset.x + center.x + (int) (i * scaleFactor);
            int y1 = offset.y + ((i % 5 == 0) ? center.y - BIG_MARK_LINE_LENGTH / 2 : center.y - MARK_LINE_LENGTH / 2);
            int y2 = offset.y + ((i % 5 == 0) ? center.y + BIG_MARK_LINE_LENGTH / 2 : center.y + MARK_LINE_LENGTH / 2);
            g2.drawLine(x, y1, x, y2);
            g2.drawString(
                    Integer.toString(i),
                    x + LABEL_X_OFFSET,
                    center.y + offset.y + LABEL_Y_OFFSET
            );
        }

        // Drawing marks on vertical axis
        for (int i = (int) ((offset.y - center.y) / scaleFactor); i <= (int) ((offset.y + getHeight() - center.y) / scaleFactor); ++i) {
            if (i == 0) {
                continue;
            }
            int y = offset.y + center.y - (int) (i * scaleFactor);
            int x1 = -offset.x + ((i % 5 == 0) ? center.x - BIG_MARK_LINE_LENGTH / 2 : center.x - MARK_LINE_LENGTH / 2);
            int x2 = -offset.x + ((i % 5 == 0) ? center.x + BIG_MARK_LINE_LENGTH / 2 : center.x + MARK_LINE_LENGTH / 2);
            g2.drawLine(x1, y, x2, y);
            g2.drawString(
                    Integer.toString(i),
                    center.x - offset.x + LABEL_X_OFFSET,
                    y + LABEL_Y_OFFSET
            );
        }

        g2.setColor(PIVOT_POINTS_SECTIONS_COLOR);
        g2.drawPolyline(xPivots, yPivots, xPivots.length);

        g2.setColor(splineColor);
        if (xSplineScreenCoords != null) {
            if (splineDots.size() >= 4) {
                g2.drawPolyline(xSplineScreenCoords, ySplineScreenCoords, xSplineScreenCoords.length);
            }
            buildModel();
        }
    }

    public void updateSpline() {
        xPivots = new int[splineDots.size()];
        yPivots = new int[splineDots.size()];
        float[] splineDotsX = new float[splineDots.size()];
        float[] splineDotsY = new float[splineDots.size()];
        for (int i = 0; i < splineDots.size(); ++i) {
            splineDots.get(i).setLocation(planeCoordsToScreenCoords(splineDots.get(i).getCoordinates()));
            xPivots[i] = splineDots.get(i).getLocation().x;
            yPivots[i] = splineDots.get(i).getLocation().y;
            splineDotsX[i] = splineDots.get(i).getCoordinates().x;
            splineDotsY[i] = splineDots.get(i).getCoordinates().y;
        }

        if (splineDots.size() >= 4) {
            uSplinePoints = BSplineCreator.getSplinePoints(splineDotsX, numberOfSections);
            vSplinePoints = BSplineCreator.getSplinePoints(splineDotsY, numberOfSections);

            xSplineScreenCoords = new int[uSplinePoints.length];
            ySplineScreenCoords = new int[vSplinePoints.length];
            for (int i = 0; i < uSplinePoints.length; ++i) {
                Point temp = planeCoordsToScreenCoords(new Point2D.Float(uSplinePoints[i], vSplinePoints[i]));
                xSplineScreenCoords[i] = temp.x;
                ySplineScreenCoords[i] = temp.y;
            }
        }
    }

    private Point planeCoordsToScreenCoords(Point2D.Float planePoint) {
        return new Point(
                (int) (planePoint.getX() * scaleFactor + getWidth() / 2 - offset.x),
                (int) (-planePoint.getY() * scaleFactor + getHeight() / 2 + offset.y)
        );
    }

    private Point2D.Float screenCoordsToPlaneCoords(Point screenPoint) {
        return new Point2D.Float(
                ((screenPoint.x + offset.x - (float) getWidth() / 2) / scaleFactor),
                -((screenPoint.y - offset.y - (float) getHeight() / 2) / scaleFactor)
        );
    }

    private void setSelected(SplineDotPanel point) {
        if (selectedDot != null) {
            selectedDot.setSelected(false);
        }
        selectedDot = point;
        point.setSelected(true);
        parameterPanel.getUSpinner().setValue((double) selectedDot.getCoordinates().x);
        parameterPanel.getVSpinner().setValue((double) selectedDot.getCoordinates().y);
        repaint();
    }

    private void addPointOnPlane(Point screenCoordinates) {
        Point2D.Float planeCoordinates = screenCoordsToPlaneCoords(screenCoordinates);
        SplineDotPanel newDot = new SplineDotPanel(++dotCounter, false, planeCoordinates);
        setSelected(newDot);
        newDot.setLocation(screenCoordinates);
        splineDots.add(newDot);
        add(newDot);
        newDot.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    setSelected(newDot);
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    splineDots.remove(newDot);
                    if (newDot == selectedDot) {
                        selectedDot = null;
                    }
                    remove(newDot);
                    repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                previousMousePoint = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });

        newDot.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                setSelected(newDot);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                int xShift = e.getX() - previousMousePoint.x;
                int yShift = e.getY() - previousMousePoint.y;

                newDot.setCoordinates(screenCoordsToPlaneCoords(new Point(
                        newDot.getLocation().x + xShift,
                        newDot.getLocation().y + yShift
                )));

                repaint();
            }
        });
    }

    private void handleMouseWheelRotation(int direction, int amount) {
        float coefficient = (float) amount / 3 * MOUSE_WHEEL_SCALE_FACTOR_INCREMENT;
        if (direction > 0) {
            scaleFactor = Math.max(scaleFactor / coefficient, MINIMUM_SCALE_FACTOR);
        } else {
            scaleFactor = Math.min(scaleFactor * coefficient, MAXIMUM_SCALE_FACTOR);
        }
        repaint();
    }

    public void buildModel() {
        MainFrame.getInstance().getViewPanel().getModel3D().buildModel3D(
                uSplinePoints,
                vSplinePoints,
                splineDots.size(),
                numberOfSections,
                numberOfGenerativeSplines,
                numberOfCirclePartSegments
        );
    }

    public void clearDots() {
        for (SplineDotPanel dot : splineDots) {
            remove(dot);
        }
    }

    public void addDots() {
        for (SplineDotPanel dot : splineDots) {
            if (dot.isSelected()) {
                setSelected(dot);
            }
            add(dot);
            dot.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        setSelected(dot);
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        splineDots.remove(dot);
                        if (dot == selectedDot) {
                            selectedDot = null;
                        }
                        remove(dot);
                        repaint();
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    previousMousePoint = e.getPoint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            });

            dot.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    setSelected(dot);
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    int xShift = e.getX() - previousMousePoint.x;
                    int yShift = e.getY() - previousMousePoint.y;

                    dot.setCoordinates(screenCoordsToPlaneCoords(new Point(
                            dot.getLocation().x + xShift,
                            dot.getLocation().y + yShift
                    )));

                    repaint();
                }
            });
        }
    }
}
