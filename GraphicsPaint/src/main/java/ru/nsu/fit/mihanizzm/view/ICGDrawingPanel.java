package ru.nsu.fit.mihanizzm.view;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nsu.fit.mihanizzm.tools.*;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

@Component("drawingPanel")
public class ICGDrawingPanel extends JPanel implements ComponentListener, MouseListener {
    @Autowired
    LineTool lineTool;
    @Autowired
    FillTool fillTool;
    @Autowired
    PolygonTool polygonTool;
    @Autowired
    StarTool starTool;
    private static final Color DEFAULT_BACKGROUND_COLOR = new Color(0x70_92_BE);
    private static final int INITIAL_WIDTH = 640;
    private static final int INITIAL_HEIGHT = 480;

    @Setter
    private Tool tool = Tool.LINE;
    @Getter
    private DrawingTool drawingTool;
    @Setter
    @Getter
    private Color currentColor = Color.BLACK;
    @Setter
    @Getter
    private BufferedImage drawingImage;
    private Point startPoint;

    public void setDrawingTool(DrawingTool tool) {
        drawingTool = tool;
        repaint();
    }

    public ICGDrawingPanel() {
        setBackground(DEFAULT_BACKGROUND_COLOR);
        setPreferredSize(new Dimension(INITIAL_WIDTH, INITIAL_HEIGHT));
        setLayout(new BorderLayout(10, 10));
    }

    @PostConstruct
    public void init() {
        drawingTool = lineTool;
        addComponentListener(this);
        addMouseListener(this);
        drawingImage = new BufferedImage(INITIAL_WIDTH, INITIAL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        cleanImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D panelGraphics = (Graphics2D) g;
        panelGraphics.drawImage(drawingImage, 0, 0, null);
        panelGraphics.dispose();
    }

    public void cleanImage() {
        if (drawingImage == null || (drawingImage.getHeight() < 1 && drawingImage.getWidth() < 1)) {
            return;
        }
        for (int y = 0; y < drawingImage.getHeight(); ++y) {
            for (int x = 0; x < drawingImage.getWidth(); ++x) {
                drawingImage.setRGB(x, y, Color.WHITE.getRGB());
            }
        }
        repaint();
    }

    private void resizeImage(int newWidth, int newHeight) {
        if (newWidth == drawingImage.getWidth() && newHeight == drawingImage.getHeight()) {
            return;
        }
        int newActualWidth = Math.max(newWidth, drawingImage.getWidth());
        int newActualHeight = Math.max(newHeight, drawingImage.getHeight());
        BufferedImage newImage = new BufferedImage(newActualWidth, newActualHeight, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < newActualHeight; ++y) {
            for (int x = 0; x < newActualWidth; ++x) {
                if (x < drawingImage.getWidth() && y < drawingImage.getHeight()) {
                    newImage.setRGB(x, y, drawingImage.getRGB(x, y));
                }
                else {
                    newImage.setRGB(x, y, Color.WHITE.getRGB());
                }
            }
        }

        drawingImage = newImage;
        setSize(newWidth, newHeight);
        setPreferredSize(new Dimension(newWidth, newHeight));
    }

    @Override
    public void componentResized(ComponentEvent e) {
        if (drawingImage == null) {
            return;
        }
        resizeImage(e.getComponent().getWidth(), e.getComponent().getHeight());
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startPoint = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (Objects.requireNonNull(tool) == Tool.LINE) {
            lineTool.drawStraightLine(drawingImage,
                    startPoint.x, startPoint.y,
                    e.getX(), e.getY(),
                    currentColor
            );
            repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch (tool) {
            case POLY -> {
                polygonTool.drawPolygon(drawingImage, e.getX(), e.getY(), currentColor);
                repaint();
            }
            case STAR -> {
                starTool.drawStar(drawingImage, e.getX(), e.getY(), currentColor);
                repaint();
            }
            case FILL -> {
                fillTool.fill(drawingImage, e.getX(), e.getY(), currentColor);
                repaint();
            }
            default -> {}
        }
    }

    // stubs
    @Override
    public void componentMoved(ComponentEvent e) { }
    @Override
    public void componentShown(ComponentEvent e) { }
    @Override
    public void componentHidden(ComponentEvent e) { }
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {

    }
}
