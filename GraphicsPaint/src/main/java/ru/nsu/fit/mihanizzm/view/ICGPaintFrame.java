package ru.nsu.fit.mihanizzm.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.nsu.fit.mihanizzm.view.toolbar.ICGToolBarPanel;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;

@Component("paintFrame")
public class ICGPaintFrame extends JFrame {
    @Autowired
    @Qualifier("menuBar")
    private ICGPaintMenuBar menuBar;
    @Autowired
    @Qualifier("drawingPanel")
    private ICGDrawingPanel drawingPanel;
    @Autowired
    @Qualifier("toolBar")
    private ICGToolBarPanel toolBar;
    @Autowired
    @Qualifier("iconImage")
    private Image iconImage;

    private static final String APP_TITLE = "ICG Paint";
    private static final int MINIMUM_WIDTH = 640;
    private static final int MINIMUM_HEIGHT = 480;
    private static final int INITIAL_WIDTH = 1280;
    private static final int INITIAL_HEIGHT = 720;

    JLabel toolStats;

    ICGPaintFrame() {
        super(APP_TITLE);
        setDefaultFrameProperties();
    }

    @PostConstruct
    void init() {
        setJMenuBar(menuBar);
        setIconImage(iconImage);

        JScrollPane scrollPane = new JScrollPane(drawingPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(10);
        add(scrollPane);

        toolStats = new JLabel(drawingPanel.getDrawingTool().getCurrentToolParameters());
        toolStats.setForeground(Color.BLACK);
        toolStats.setFont(new Font("Arial", Font.PLAIN, 12));
        toolStats.setHorizontalTextPosition(SwingConstants.CENTER);
        toolStats.setHorizontalAlignment(SwingConstants.CENTER);

        add(toolBar, BorderLayout.NORTH);
        add(toolStats, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    public void updateToolStats() {
        toolStats.setText(drawingPanel.getDrawingTool().getCurrentToolParameters());
        repaint();
    }

    private void setDefaultFrameProperties() {
        setPreferredSize(new Dimension(INITIAL_WIDTH, INITIAL_HEIGHT));
        setMinimumSize(new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;
        setLocation((width - INITIAL_WIDTH) / 2, (height - INITIAL_HEIGHT) / 2);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
