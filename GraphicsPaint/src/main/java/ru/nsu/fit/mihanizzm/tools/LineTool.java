package ru.nsu.fit.mihanizzm.tools;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nsu.fit.mihanizzm.tools.settings_windows.LineToolSettingsWindow;
import ru.nsu.fit.mihanizzm.util.PrimitiveDrawer;
import ru.nsu.fit.mihanizzm.view.ICGPaintFrame;

import java.awt.*;
import java.awt.image.BufferedImage;

@Setter
@Getter
@Component
public class LineTool extends DrawingTool {
    @Autowired
    private ICGPaintFrame frame;
    public static final int LINE_THICKNESS_LOWER_CAP = 1;
    public static final int LINE_THICKNESS_UPPER_CAP = 500;

    private int lineThickness = 1;

    @Override
    public boolean openSettings() {
        LineToolSettingsWindow settings = new LineToolSettingsWindow(frame, this);
        return settings.isSettingsApplied();
    }

    @Override
    public String getCurrentToolParameters() {
        return "<html>Tool: Line tool<br>" +
                "Parameters:<br>" +
                "Line thickness: " + lineThickness + "<br></html>";
    }

    public void drawStraightLine(BufferedImage image, int x0, int y0, int x1, int y1, Color color) {
        if (image == null) {
            // add logging
            return;
        }
        Graphics2D g = (Graphics2D) image.getGraphics();

        if (lineThickness != 1) {
            g.setColor(color);
            g.setStroke(new BasicStroke(lineThickness));
            g.drawLine(x0, y0, x1, y1);
        }
        else {
            PrimitiveDrawer.drawLineBresenham(color, image, x0, y0, x1, y1);
        }
    }
}
