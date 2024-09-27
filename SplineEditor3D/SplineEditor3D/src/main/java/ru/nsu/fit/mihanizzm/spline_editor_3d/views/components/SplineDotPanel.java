package ru.nsu.fit.mihanizzm.spline_editor_3d.views.components;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

@Setter
@Getter
public class SplineDotPanel extends JPanel {
    private static final int CIRCLE_RADIUS = 15;
    private static final Color SELECTED_COLOR = Color.GREEN;
    private static final Color UNSELECTED_COLOR = Color.RED;

    private int idx;
    private boolean isSelected;
    private Point2D.Float coordinates;
    private Color color;

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        repaint();
    }

    public void setCoordinates(Point2D.Float coordinates) {
        this.coordinates = coordinates;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        Font font = new Font("Arial", Font.PLAIN, 14);
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(font);
        g2.setColor(isSelected ? SELECTED_COLOR : UNSELECTED_COLOR);
        g2.drawOval(0, 0, CIRCLE_RADIUS * 2, CIRCLE_RADIUS * 2);
        g2.drawString(Integer.toString(idx), CIRCLE_RADIUS, CIRCLE_RADIUS);
    }

    public SplineDotPanel(int idx, boolean isSelected, Point2D.Float coordinates) {
        this.idx = idx;
        this.isSelected = isSelected;
        this.coordinates = coordinates;

        setSize(new Dimension(CIRCLE_RADIUS * 2 + 1, CIRCLE_RADIUS * 2 + 1));
        setPreferredSize(new Dimension(CIRCLE_RADIUS * 2, CIRCLE_RADIUS * 2));
    }

    @Override
    public Point getLocation() {
        return new Point(super.getLocation().x + CIRCLE_RADIUS, super.getLocation().y + CIRCLE_RADIUS);
    }

    @Override
    public void setLocation(Point point) {
        super.setLocation(point.x - CIRCLE_RADIUS, point.y - CIRCLE_RADIUS);
    }
}
