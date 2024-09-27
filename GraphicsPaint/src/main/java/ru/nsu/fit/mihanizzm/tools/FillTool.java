package ru.nsu.fit.mihanizzm.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.nsu.fit.mihanizzm.util.PrimitiveDrawer;
import ru.nsu.fit.mihanizzm.view.ICGPaintFrame;

import java.awt.*;
import java.awt.image.BufferedImage;

@Component
public final class FillTool extends DrawingTool {
    @Autowired
    @Qualifier("paintFrame")
    private ICGPaintFrame frame;

    @Override
    public boolean openSettings() {
        return true;
    }

    @Override
    public String getCurrentToolParameters() {
        return "<html>Tool: Fill tool</html>";
    }

    public void fill(BufferedImage image, int x, int y, Color color) {
        if (image == null) {
            // add logging
            return;
        }
        PrimitiveDrawer.spanFill(color, image, x, y);
    }
}
