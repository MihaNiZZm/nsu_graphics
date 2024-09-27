package ru.nsu.fit.mihanizzm.view.toolbar;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.nsu.fit.mihanizzm.tools.*;
import ru.nsu.fit.mihanizzm.util.ImageEditingUtilities;
import ru.nsu.fit.mihanizzm.view.ICGDrawingPanel;
import ru.nsu.fit.mihanizzm.view.ICGPaintFrame;
import ru.nsu.fit.mihanizzm.view.ICGPaintMenuBar;
import ru.nsu.fit.mihanizzm.view.toolbar.buttons.ToolBarButton;
import ru.nsu.fit.mihanizzm.view.toolbar.buttons.ToolBarRadioButton;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import static javax.swing.JOptionPane.*;
import static ru.nsu.fit.mihanizzm.util.DialogWindows.confirmImageCleaning;
import static ru.nsu.fit.mihanizzm.util.DialogWindows.showProgramInfo;

@Component("toolBar")
public class ICGToolBarPanel extends JPanel implements ActionListener {
    @Autowired
    ICGPaintFrame frame;
    @Autowired
    LineTool lineTool;
    @Autowired
    FillTool fillTool;
    @Autowired
    PolygonTool polygonTool;
    @Autowired
    StarTool starTool;

    @Autowired
    @Qualifier("drawingPanel")
    ICGDrawingPanel drawingPanel;
    @Autowired
    @Qualifier("menuBar")
    ICGPaintMenuBar menuBar;
    private static final int ICON_SIZE = 24;
    private static final Color BG_TOOLBAR_COLOR = new Color(0x777777);
    JButton cleanImageButton = new ToolBarButton();
    JButton toolSettingsButton = new ToolBarButton();
    JButton blackColor = new ToolBarButton();
    JButton redColor = new ToolBarButton();
    JButton greenColor = new ToolBarButton();
    JButton blueColor = new ToolBarButton();
    JButton yellowColor = new ToolBarButton();
    JButton cyanColor = new ToolBarButton();
    JButton magentaColor = new ToolBarButton();
    JButton whiteColor = new ToolBarButton();
    JButton chooseColorButton = new ToolBarButton();
    JButton programInformationButton = new ToolBarButton();

    @Getter
    ButtonGroup toolsGroup = new ButtonGroup();
    @Getter
    JRadioButton fillToolButton = new ToolBarRadioButton();
    @Getter
    JRadioButton polygonToolButton = new ToolBarRadioButton();
    @Getter
    JRadioButton starToolButton = new ToolBarRadioButton();
    @Getter
    JRadioButton lineToolButton = new ToolBarRadioButton();
    @Setter
    JRadioButton currentTool = lineToolButton;

    JPanel currentColorPanel;

    private void updateCurrentColor(Color color) {
        currentColorPanel.setBackground(color);
        currentColorPanel.repaint();
    }

    private void configureColorButtons() {
        blackColor.setActionCommand("black");
        blackColor.setIcon(ImageEditingUtilities.createColorfulImageIcon(ICON_SIZE, ICON_SIZE, Color.BLACK));
        blackColor.setToolTipText("Choose black color");
        blackColor.addActionListener(this);

        redColor.setActionCommand("red");
        redColor.setIcon(ImageEditingUtilities.createColorfulImageIcon(ICON_SIZE, ICON_SIZE, Color.RED));
        redColor.setToolTipText("Choose red color");
        redColor.addActionListener(this);

        greenColor.setActionCommand("green");
        greenColor.setIcon(ImageEditingUtilities.createColorfulImageIcon(ICON_SIZE, ICON_SIZE, Color.GREEN));
        greenColor.setToolTipText("Choose green color");
        greenColor.addActionListener(this);

        blueColor.setActionCommand("blue");
        blueColor.setIcon(ImageEditingUtilities.createColorfulImageIcon(ICON_SIZE, ICON_SIZE, Color.BLUE));
        blueColor.setToolTipText("Choose blue color");
        blueColor.addActionListener(this);

        yellowColor.setActionCommand("yellow");
        yellowColor.setIcon(ImageEditingUtilities.createColorfulImageIcon(ICON_SIZE, ICON_SIZE, Color.YELLOW));
        yellowColor.setToolTipText("Choose yellow color");
        yellowColor.addActionListener(this);

        cyanColor.setActionCommand("cyan");
        cyanColor.setIcon(ImageEditingUtilities.createColorfulImageIcon(ICON_SIZE, ICON_SIZE, Color.CYAN));
        cyanColor.setToolTipText("Choose cyan color");
        cyanColor.addActionListener(this);

        magentaColor.setActionCommand("magenta");
        magentaColor.setIcon(ImageEditingUtilities.createColorfulImageIcon(ICON_SIZE, ICON_SIZE, Color.MAGENTA));
        magentaColor.setToolTipText("Choose magenta color");
        magentaColor.addActionListener(this);

        whiteColor.setActionCommand("white");
        whiteColor.setIcon(ImageEditingUtilities.createColorfulImageIcon(ICON_SIZE, ICON_SIZE, Color.WHITE));
        whiteColor.setToolTipText("Choose white color");
        whiteColor.addActionListener(this);

        chooseColorButton.setActionCommand("chooseColor");
        Image colorRing = Objects.requireNonNull(ImageEditingUtilities
                .getImageFromResources("/color_ring_icon.png"))
                .getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        chooseColorButton.setIcon(new ImageIcon(colorRing));
        chooseColorButton.setToolTipText("Choose custom color");
        chooseColorButton.addActionListener(this);
    }

    private void configureInformationButton() {
        programInformationButton.setActionCommand("info");
        Image infoSign = Objects.requireNonNull(ImageEditingUtilities
                        .getImageFromResources("/info_icon.png"))
                .getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        programInformationButton.setIcon(new ImageIcon(infoSign));
        programInformationButton.setToolTipText("About program");
        programInformationButton.addActionListener(this);
    }

    private void configureCleanImageButton() {
        cleanImageButton.setActionCommand("clean");
        Image cleanIcon = Objects.requireNonNull(ImageEditingUtilities
                        .getImageFromResources("/clean_icon.png"))
                .getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        cleanImageButton.setIcon(new ImageIcon(cleanIcon));
        cleanImageButton.setToolTipText("Clean image (fills the whole image with white color)");
        cleanImageButton.addActionListener(this);
    }

    private void configureToolButtons() {
        setLineToolIcons();
        setFillToolIcons();
        setPolygonToolIcons();
        setStarToolIcons();
        setToolSettingsButtonIcon();

        // tool chosen by default
        lineToolButton.setSelected(true);

        toolsGroup.add(lineToolButton);
        toolsGroup.add(fillToolButton);
        toolsGroup.add(polygonToolButton);
        toolsGroup.add(starToolButton);

        lineToolButton.setToolTipText("Line tool");
        fillToolButton.setToolTipText("Fill tool");
        polygonToolButton.setToolTipText("Polygon tool");
        starToolButton.setToolTipText("Star tool");
        toolSettingsButton.setToolTipText("Tool settings (let you change current chosen tool parameters)");

        lineToolButton.setActionCommand("line");
        fillToolButton.setActionCommand("fill");
        polygonToolButton.setActionCommand("poly");
        starToolButton.setActionCommand("star");
        toolSettingsButton.setActionCommand("settings");

        lineToolButton.addActionListener(this);
        fillToolButton.addActionListener(this);
        polygonToolButton.addActionListener(this);
        starToolButton.addActionListener(this);
        toolSettingsButton.addActionListener(this);
    }

    ICGToolBarPanel() {
        setBackground(BG_TOOLBAR_COLOR);
        setBorder(new BevelBorder(BevelBorder.RAISED));

        configureColorButtons();
        configureInformationButton();
        configureCleanImageButton();
        configureToolButtons();

        add(cleanImageButton);
        add(lineToolButton);
        add(fillToolButton);
        add(polygonToolButton);
        add(starToolButton);
        add(toolSettingsButton);
        add(blackColor);
        add(redColor);
        add(greenColor);
        add(blueColor);
        add(yellowColor);
        add(cyanColor);
        add(magentaColor);
        add(whiteColor);
        add(chooseColorButton);
        add(programInformationButton);

        JLabel currentColor = new JLabel("Current color:");
        currentColor.setForeground(Color.BLACK);
        currentColor.setFont(new Font("Arial", Font.PLAIN, 12));
        add(currentColor);

        currentColorPanel = new JPanel();
        if (drawingPanel != null) {
            currentColorPanel.setForeground(drawingPanel.getCurrentColor());
        }
        currentColorPanel.setPreferredSize(new Dimension(32, 16));
        currentColorPanel.setBackground(Color.BLACK);
        add(currentColorPanel);
    }


    private void setLineToolIcons() {
        Image lineToolIcon = Objects.requireNonNull(ImageEditingUtilities
                        .getImageFromResources("/line_tool_icon.png"))
                .getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        lineToolButton.setIcon(new ImageIcon(lineToolIcon));
        Image lineToolSelectedIcon = Objects.requireNonNull(ImageEditingUtilities
                        .getImageFromResources("/line_tool_selected_icon.png"))
                .getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        lineToolButton.setSelectedIcon(new ImageIcon(lineToolSelectedIcon));
    }

    private void setFillToolIcons() {
        Image fillToolIcon = Objects.requireNonNull(ImageEditingUtilities
                        .getImageFromResources("/fill_tool_icon.png"))
                .getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        fillToolButton.setIcon(new ImageIcon(fillToolIcon));
        Image fillToolSelectedIcon = Objects.requireNonNull(ImageEditingUtilities
                        .getImageFromResources("/fill_tool_selected_icon.png"))
                .getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        fillToolButton.setSelectedIcon(new ImageIcon(fillToolSelectedIcon));
    }

    private void setPolygonToolIcons() {
        Image polygonToolIcon = Objects.requireNonNull(ImageEditingUtilities
                        .getImageFromResources("/polygon_tool_icon.png"))
                .getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        polygonToolButton.setIcon(new ImageIcon(polygonToolIcon));
        Image polygonToolSelectedIcon = Objects.requireNonNull(ImageEditingUtilities
                        .getImageFromResources("/polygon_tool_selected_icon.png"))
                .getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        polygonToolButton.setSelectedIcon(new ImageIcon(polygonToolSelectedIcon));
    }

    private void setStarToolIcons() {
        Image starToolIcon = Objects.requireNonNull(ImageEditingUtilities
                        .getImageFromResources("/star_tool_icon.png"))
                .getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        starToolButton.setIcon(new ImageIcon(starToolIcon));
        Image starToolSelectedIcon = Objects.requireNonNull(ImageEditingUtilities
                        .getImageFromResources("/star_tool_selected_icon.png"))
                .getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        starToolButton.setSelectedIcon(new ImageIcon(starToolSelectedIcon));
    }

    private void setToolSettingsButtonIcon() {
        Image toolSettingIcon = Objects.requireNonNull(ImageEditingUtilities
                        .getImageFromResources("/tool_settings_icon.png"))
                .getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        toolSettingsButton.setIcon(new ImageIcon(toolSettingIcon));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "black" -> {
                drawingPanel.setCurrentColor(Color.BLACK);
                updateCurrentColor(Color.BLACK);
            }
            case "red" -> {
                drawingPanel.setCurrentColor(Color.RED);
                updateCurrentColor(Color.RED);
            }
            case "green" -> {
                drawingPanel.setCurrentColor(Color.GREEN);
                updateCurrentColor(Color.GREEN);
            }
            case "blue" -> {
                drawingPanel.setCurrentColor(Color.BLUE);
                updateCurrentColor(Color.BLUE);
            }
            case "yellow" -> {
                drawingPanel.setCurrentColor(Color.YELLOW);
                updateCurrentColor(Color.YELLOW);
            }
            case "cyan" -> {
                drawingPanel.setCurrentColor(Color.CYAN);
                updateCurrentColor(Color.CYAN);
            }
            case "magenta" -> {
                drawingPanel.setCurrentColor(Color.MAGENTA);
                updateCurrentColor(Color.MAGENTA);
            }
            case "white" -> {
                drawingPanel.setCurrentColor(Color.WHITE);
                updateCurrentColor(Color.WHITE);
            }
            case "chooseColor" -> {
                Color newColor = JColorChooser.showDialog(null, "Select a color", drawingPanel.getCurrentColor());
                drawingPanel.setCurrentColor(newColor);
                updateCurrentColor(newColor);
            }
            case "info" -> showProgramInfo();
            case "clean" -> {
                switch (confirmImageCleaning()) {
                    case CLOSED_OPTION, CANCEL_OPTION -> {}
                    case YES_OPTION -> drawingPanel.cleanImage();
                }
            }
            case "line" -> {
                if (currentTool == lineToolButton) {
                    return;
                }
                if (lineTool.openSettings()) {
                    drawingPanel.setDrawingTool(lineTool);
                    drawingPanel.setTool(Tool.LINE);
                    currentTool = lineToolButton;

                    frame.updateToolStats();

                    menuBar.getToolsButtonGroup().getSelection().setSelected(false);
                    menuBar.getLineToolItem().setSelected(true);
                    menuBar.setCurrentItem(menuBar.getLineToolItem());
                }
                else {
                    lineToolButton.setSelected(false);
                    currentTool.setSelected(true);
                }
            }
            case "poly" -> {
                if (currentTool == polygonToolButton) {
                    return;
                }
                if (polygonTool.openSettings()) {
                    drawingPanel.setDrawingTool(polygonTool);
                    drawingPanel.setTool(Tool.POLY);
                    currentTool = polygonToolButton;

                    frame.updateToolStats();

                    menuBar.getToolsButtonGroup().getSelection().setSelected(false);
                    menuBar.getPolygonToolItem().setSelected(true);
                    menuBar.setCurrentItem(menuBar.getPolygonToolItem());
                }
                else {
                    polygonToolButton.setSelected(false);
                    currentTool.setSelected(true);
                }
            }
            case "star" -> {
                if (currentTool == starToolButton) {
                    return;
                }
                if (starTool.openSettings()) {
                    drawingPanel.setDrawingTool(starTool);
                    drawingPanel.setTool(Tool.STAR);
                    currentTool = starToolButton;

                    frame.updateToolStats();

                    menuBar.getToolsButtonGroup().getSelection().setSelected(false);
                    menuBar.getStarToolItem().setSelected(true);
                    menuBar.setCurrentItem(menuBar.getStarToolItem());
                }
                else {
                    starToolButton.setSelected(false);
                    currentTool.setSelected(true);
                }
            }
            case "fill" -> {
                if (currentTool == fillToolButton) {
                    return;
                }
                if (fillTool.openSettings()) {
                    drawingPanel.setDrawingTool(fillTool);
                    drawingPanel.setTool(Tool.FILL);
                    currentTool = fillToolButton;

                    frame.updateToolStats();

                    menuBar.getToolsButtonGroup().getSelection().setSelected(false);
                    menuBar.getFillToolItem().setSelected(true);
                    menuBar.setCurrentItem(menuBar.getFillToolItem());
                }
                else {
                    fillToolButton.setSelected(false);
                    currentTool.setSelected(true);
                }
            }
            case "settings" -> {
                drawingPanel.getDrawingTool().openSettings();
                frame.updateToolStats();
            }
        }
    }
}
