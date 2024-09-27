package ru.nsu.fit.mihanizzm.tools;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.nsu.fit.mihanizzm.tools.settings_windows.StarToolSettingsWindow;
import ru.nsu.fit.mihanizzm.util.PrimitiveDrawer;
import ru.nsu.fit.mihanizzm.view.ICGPaintFrame;

import java.awt.*;
import java.awt.image.BufferedImage;

@Setter
@Getter
@Component
public class StarTool extends DrawingTool {
    @Autowired
    @Qualifier("paintFrame")
    private ICGPaintFrame frame;

    public static final int INNER_RADIUS_LOWER_CAP = 1;
    public static final int INNER_RADIUS_UPPER_CAP = 499;
    public static final int OUTER_RADIUS_LOWER_CAP = 2;
    public static final int OUTER_RADIUS_UPPER_CAP = 500;
    public static final int LINE_THICKNESS_LOWER_CAP = 1;
    public static final int LINE_THICKNESS_UPPER_CAP = 500;
    public static final int ANGLE_LOWER_CAP = -360;
    public static final int ANGLE_UPPER_CAP = 360;
    public static final int NUMBER_OF_VERTICES_LOWER_CAP = 3;
    public static final int NUMBER_OF_VERTICES_UPPER_CAP = 16;

    private int innerRadius = 100;
    private int outerRadius = 200;
    private int lineThickness = 1;
    private int angle = 0;
    private int numberOfVertices = 5;

    @Override
    public boolean openSettings() {
        StarToolSettingsWindow settings = new StarToolSettingsWindow(frame, this);
        return settings.isSettingsApplied();
    }

    @Override
    public String getCurrentToolParameters() {
        return "<html>Tool: Star tool<br>" +
                "Parameters:<br>" +
                "Outer radius: " + outerRadius + "<br>" +
                "Inner radius: " + innerRadius + "<br>" +
                "Number of vertices: " + numberOfVertices + "<br>" +
                "Rotation angle: " + angle + "°<br>" +
                "Line thickness: " + lineThickness + "<br></html>";
    }

    public void drawStar(BufferedImage image, int centerX, int centerY, Color color) {
        if (image == null) {
            // add logging
            return;
        }
        Graphics2D g = (Graphics2D) image.getGraphics();

        int[] xPoints = new int[numberOfVertices * 2];
        int[] yPoints = new int[numberOfVertices * 2];
        double angleIncrement = Math.PI / numberOfVertices;

        for (int i = 0; i < numberOfVertices * 2; ++i) {
            xPoints[i] = i % 2 == 0 ?
                    (int) (centerX + outerRadius * Math.cos(Math.toRadians(angle - 90) + i * angleIncrement)) :
                    (int) (centerX + innerRadius * Math.cos(Math.toRadians(angle - 90) + i * angleIncrement));

            yPoints[i] = i % 2 == 0 ?
                    (int) (centerY + outerRadius * Math.sin(Math.toRadians(angle - 90) + i * angleIncrement)) :
                    (int) (centerY + innerRadius * Math.sin(Math.toRadians(angle - 90) + i * angleIncrement));
        }

        if (lineThickness != 1) {
            g.setColor(color);
            g.setStroke(new BasicStroke(lineThickness));
            g.drawPolygon(xPoints, yPoints, numberOfVertices * 2);
        }
        else {
            for (int i = 0; i < numberOfVertices * 2; ++i) {
                PrimitiveDrawer.drawLineBresenham(color, image,
                        xPoints[i], yPoints[i],
                        xPoints[(i + 1) % xPoints.length], yPoints[(i + 1) % yPoints.length]);
            }
        }
    }
}
