package ru.nsu.fit.mihanizzm.tools;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.nsu.fit.mihanizzm.tools.settings_windows.PolygonToolSettingsWindow;
import ru.nsu.fit.mihanizzm.util.PrimitiveDrawer;
import ru.nsu.fit.mihanizzm.view.ICGPaintFrame;

import java.awt.*;
import java.awt.image.BufferedImage;

@Setter
@Getter
@Component
public class PolygonTool extends DrawingTool {
    @Autowired
    @Qualifier("paintFrame")
    private ICGPaintFrame frame;

    public static final int RADIUS_LOWER_CAP = 1;
    public static final int RADIUS_UPPER_CAP = 500;
    public static final int LINE_THICKNESS_LOWER_CAP = 1;
    public static final int LINE_THICKNESS_UPPER_CAP = 500;
    public static final int ANGLE_LOWER_CAP = -360;
    public static final int ANGLE_UPPER_CAP = 360;
    public static final int NUMBER_OF_VERTICES_LOWER_CAP = 3;
    public static final int NUMBER_OF_VERTICES_UPPER_CAP = 16;

    private int radius = 200;
    private int lineThickness = 1;
    private int angle = 0;
    private int numberOfVertices = 5;

    @Override
    public boolean openSettings() {
        PolygonToolSettingsWindow settings = new PolygonToolSettingsWindow(frame, this);
        return settings.isSettingsApplied();
    }

    @Override
    public String getCurrentToolParameters() {
        return "<html>Tool: Polygon tool<br>" +
                "Parameters:<br>" +
                "Radius: " + radius + "<br>" +
                "Number of vertices: " + numberOfVertices + "<br>" +
                "Rotation angle: " + angle + "°<br>" +
                "Line thickness: " + lineThickness + "<br></html>";
    }

    public void drawPolygon(BufferedImage image, int centerX, int centerY, Color color) {
        if (image == null) {
            // add logging
            return;
        }
        Graphics2D g = (Graphics2D) image.getGraphics();

        int[] xPoints = new int[numberOfVertices];
        int[] yPoints = new int[numberOfVertices];
        double angleIncrement = Math.PI * 2 / numberOfVertices;

        for (int i = 0; i < numberOfVertices; ++i) {
            xPoints[i] = (int) (centerX + radius * Math.cos(Math.toRadians(angle - 90) + i * angleIncrement));
            yPoints[i] = (int) (centerY + radius * Math.sin(Math.toRadians(angle - 90) + i * angleIncrement));
        }

        if (lineThickness != 1) {
            g.setColor(color);
            g.setStroke(new BasicStroke(lineThickness));
            g.drawPolygon(xPoints, yPoints, numberOfVertices);
        }
        else {
            for (int i = 0; i < numberOfVertices; ++i) {
                PrimitiveDrawer.drawLineBresenham(color, image,
                        xPoints[i], yPoints[i],
                        xPoints[(i + 1) % xPoints.length], yPoints[(i + 1) % yPoints.length]);
            }
        }
    }
}
