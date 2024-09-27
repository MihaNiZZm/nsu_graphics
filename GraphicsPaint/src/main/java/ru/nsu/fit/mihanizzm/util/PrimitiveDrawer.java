package ru.nsu.fit.mihanizzm.util;

import ru.nsu.fit.mihanizzm.data.Span;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Stack;

public class PrimitiveDrawer {
    public static void spanFill(Color colorToFill, BufferedImage image, int x, int y) {
        if (image == null || colorToFill == null) {
            // add logging
            return;
        }

        int targetColor = image.getRGB(x, y);

        // Check if the target color is same as fill color
        if (targetColor == colorToFill.getRGB()) {
            // add logging
            return;
        }

        Stack<Point> pointStack = new Stack<>();

        pointStack.push(new Point(x, y));

        while (!pointStack.isEmpty()) {
            Point p = pointStack.pop();
            int px = p.x;
            int py = p.y;

            // Move to the left until we find the boundary
            while (px >= 0 && image.getRGB(px, py) == targetColor) {
                px--;
            }
            px++;

            // Fill the span to the right of the starting point
            boolean spanAbove = false;
            boolean spanBelow = false;

            while (px < image.getWidth() && image.getRGB(px, py) == targetColor) {
                image.setRGB(px, py, colorToFill.getRGB());

                // Check if we need to add the points above and below for the next scanline
                if (!spanAbove && py > 0 && image.getRGB(px, py - 1) == targetColor) {
                    pointStack.push(new Point(px, py - 1));
                    spanAbove = true;
                } else if (spanAbove && py > 0 && image.getRGB(px, py - 1) != targetColor) {
                    spanAbove = false;
                }

                if (!spanBelow && py < image.getHeight() - 1 && image.getRGB(px, py + 1) == targetColor) {
                    pointStack.push(new Point(px, py + 1));
                    spanBelow = true;
                } else if (spanBelow && py < image.getHeight() - 1 && image.getRGB(px, py + 1) != targetColor) {
                    spanBelow = false;
                }

                px++;
            }
        }
    }

    public static void drawLineBresenham(Color color, BufferedImage image, int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            if (x1 >= 0 && x1 < image.getWidth() && y1 >= 0 && y1 < image.getHeight()) {
                image.setRGB(x1, y1, color.getRGB());
            }
            if (x1 == x2 && y1 == y2) {
                break;
            }
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y1 += sy;
            }
        }
    }
}
