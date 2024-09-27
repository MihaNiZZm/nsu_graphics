package ru.nsu.fit.mihanizzm.view;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.nsu.fit.mihanizzm.tools.*;
import ru.nsu.fit.mihanizzm.util.ImageEditingUtilities;
import ru.nsu.fit.mihanizzm.view.toolbar.ICGToolBarPanel;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static ru.nsu.fit.mihanizzm.util.DialogWindows.showProgramInfo;

@Component("menuBar")
public class ICGPaintMenuBar extends JMenuBar implements ActionListener {
    private static final int ICON_SIZE = 16;

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
    @Qualifier("toolBar")
    private ICGToolBarPanel toolBar;
    @Autowired
    @Qualifier("drawingPanel")
    private ICGDrawingPanel drawingPanel;
    @Autowired
    @Qualifier("fileMenu")
    private JMenu file;
    @Autowired
    @Qualifier("toolsMenu")
    private JMenu tools;
    @Autowired
    @Qualifier("aboutMenu")
    private JMenu about;

    @Getter
    private ButtonGroup toolsButtonGroup = new ButtonGroup();
    @Getter
    private JRadioButtonMenuItem lineToolItem = new JRadioButtonMenuItem("Line tool");
    @Getter
    private JRadioButtonMenuItem fillToolItem = new JRadioButtonMenuItem("Fill tool");
    @Getter
    private JRadioButtonMenuItem polygonToolItem = new JRadioButtonMenuItem("Polygon tool");
    @Getter
    private JRadioButtonMenuItem starToolItem = new JRadioButtonMenuItem("Star tool");
    @Setter
    private JRadioButtonMenuItem currentItem = lineToolItem;

    @PostConstruct
    public void init() {
        configureAllMenus();

        add(file);
        add(tools);
        add(about);
    }

    private void configureAllMenus() {
        configureFileMenu();
        configureAboutMenu();
        configureToolsMenu();
    }

    private void configureToolsMenu() {
        setLineToolIcon();
        setFillToolIcon();
        setPolygonToolIcon();
        setStarToolIcon();

        lineToolItem.setActionCommand("line");
        fillToolItem.setActionCommand("fill");
        polygonToolItem.setActionCommand("poly");
        starToolItem.setActionCommand("star");

        lineToolItem.addActionListener(this);
        fillToolItem.addActionListener(this);
        polygonToolItem.addActionListener(this);
        starToolItem.addActionListener(this);

        toolsButtonGroup.add(lineToolItem);
        toolsButtonGroup.add(fillToolItem);
        toolsButtonGroup.add(polygonToolItem);
        toolsButtonGroup.add(starToolItem);

        // tool chosen by default
        lineToolItem.setSelected(true);

        tools.add(lineToolItem);
        tools.add(fillToolItem);
        tools.add(polygonToolItem);
        tools.add(starToolItem);
    }

    private void configureAboutMenu() {
        JMenuItem aboutProgram = new JMenuItem("About program");

        aboutProgram.addActionListener(this);

        Image infoSign = Objects.requireNonNull(ImageEditingUtilities
                        .getImageFromResources("/info_icon.png"))
                .getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        aboutProgram.setIcon(new ImageIcon(infoSign));

        about.add(aboutProgram);
    }

    private void configureFileMenu() {
        JMenuItem newImage = new JMenuItem("Create new image");
        JMenuItem importImage = new JMenuItem("Import an image");
        JMenuItem exportImage = new JMenuItem("Export as PNG");
        JMenuItem exit = new JMenuItem("Exit");

        Image newImageIcon = Objects.requireNonNull(ImageEditingUtilities
                        .getImageFromResources("/new_image_icon.png"))
                .getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        newImage.setIcon(new ImageIcon(newImageIcon));

        Image importImageIcon = Objects.requireNonNull(ImageEditingUtilities
                        .getImageFromResources("/import_image_icon.png"))
                .getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        importImage.setIcon(new ImageIcon(importImageIcon));

        Image exportImageIcon = Objects.requireNonNull(ImageEditingUtilities
                        .getImageFromResources("/export_image_icon.png"))
                .getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        exportImage.setIcon(new ImageIcon(exportImageIcon));

        Image exitButtonImage = Objects.requireNonNull(ImageEditingUtilities
                        .getImageFromResources("/exit_button_icon.png"))
                .getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        exit.setIcon(new ImageIcon(exitButtonImage));

        newImage.addActionListener(this);
        importImage.addActionListener(this);
        exportImage.addActionListener(this);
        exit.addActionListener(this);

        file.add(newImage);
        file.add(importImage);
        file.add(exportImage);
        file.add(exit);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
            // File menu
            case "Create new image" -> {
                drawingPanel.setSize(640, 480);
                drawingPanel.setPreferredSize(new Dimension(640, 480));
                drawingPanel.setDrawingImage(new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB));
                drawingPanel.cleanImage();
            }
            case "Import an image" -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Choose an image file");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Image files", "jpg", "jpeg", "bmp", "gif", "png"
                );
                fileChooser.setFileFilter(filter);

                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        BufferedImage image = ImageIO.read(selectedFile);
                        if (image != null) {
                            drawingPanel.setSize(image.getWidth(), image.getHeight());
                            drawingPanel.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
                            drawingPanel.setDrawingImage(image);
                        }
                        else {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Unfortunately, chosen file can't be loaded as an image.",
                                    "Couldn't load an image",
                                    JOptionPane.WARNING_MESSAGE
                            );
                        }
                    } catch (IOException ex) {
                        // add logging
                        ex.printStackTrace();
                    }
                }
            }
            case "Export as PNG" -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save Image");

                FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG files", "png");
                fileChooser.setFileFilter(filter);
                int userSelection = fileChooser.showSaveDialog(null);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();

                    String filePath = fileToSave.getAbsolutePath();
                    if (!filePath.toLowerCase().endsWith(".png")) {
                        filePath += ".png";
                        fileToSave = new File(filePath);
                    }

                    try {
                        ImageIO.write(drawingPanel.getDrawingImage(), "png", fileToSave);
                        JOptionPane.showMessageDialog(null, "The image was successfully saved.", "Saving complete.", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        // add logging
                        ex.printStackTrace();
                    }
                }
            }
            case "Exit" -> System.exit(0);

            // Tools menu
            case "line" -> {
                if (currentItem == lineToolItem) {
                    return;
                }
                if (lineTool.openSettings()) {
                    drawingPanel.setDrawingTool(lineTool);
                    drawingPanel.setTool(Tool.LINE);
                    currentItem = lineToolItem;

                    frame.updateToolStats();

                    toolBar.getToolsGroup().getSelection().setSelected(false);
                    toolBar.getLineToolButton().setSelected(true);
                    toolBar.setCurrentTool(toolBar.getLineToolButton());
                }
                else {
                    lineToolItem.setSelected(false);
                    currentItem.setSelected(true);
                }
            }
            case "poly" -> {
                if (currentItem == polygonToolItem) {
                    return;
                }
                if (polygonTool.openSettings()) {
                    drawingPanel.setDrawingTool(polygonTool);
                    drawingPanel.setTool(Tool.POLY);
                    currentItem = polygonToolItem;

                    frame.updateToolStats();

                    toolBar.getToolsGroup().getSelection().setSelected(false);
                    toolBar.getPolygonToolButton().setSelected(true);
                    toolBar.setCurrentTool(toolBar.getPolygonToolButton());
                }
                else {
                    polygonToolItem.setSelected(false);
                    currentItem.setSelected(true);
                }
            }
            case "star" -> {
                if (currentItem == starToolItem) {
                    return;
                }
                if (starTool.openSettings()) {
                    drawingPanel.setDrawingTool(starTool);
                    drawingPanel.setTool(Tool.STAR);
                    currentItem = starToolItem;

                    frame.updateToolStats();

                    toolBar.getToolsGroup().getSelection().setSelected(false);
                    toolBar.getStarToolButton().setSelected(true);
                    toolBar.setCurrentTool(toolBar.getStarToolButton());
                }
                else {
                    starToolItem.setSelected(false);
                    currentItem.setSelected(true);
                }
            }
            case "fill" -> {
                if (currentItem == fillToolItem) {
                    return;
                }
                if (fillTool.openSettings()) {
                    drawingPanel.setDrawingTool(fillTool);
                    drawingPanel.setTool(Tool.FILL);
                    currentItem = fillToolItem;

                    frame.updateToolStats();

                    toolBar.getToolsGroup().getSelection().setSelected(false);
                    toolBar.getFillToolButton().setSelected(true);
                    toolBar.setCurrentTool(toolBar.getFillToolButton());
                }
                else {
                    fillToolItem.setSelected(false);
                    currentItem.setSelected(true);
                }
            }

            // About menu
            case "About program" -> showProgramInfo();
        }
    }

    private void setLineToolIcon() {
        Image lineToolIcon = Objects.requireNonNull(ImageEditingUtilities
                        .getImageFromResources("/line_tool_icon.png"))
                .getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        lineToolItem.setIcon(new ImageIcon(lineToolIcon));
    }

    private void setFillToolIcon() {
        Image fillToolIcon = Objects.requireNonNull(ImageEditingUtilities
                        .getImageFromResources("/fill_tool_icon.png"))
                .getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        fillToolItem.setIcon(new ImageIcon(fillToolIcon));
    }

    private void setPolygonToolIcon() {
        Image polygonToolIcon = Objects.requireNonNull(ImageEditingUtilities
                        .getImageFromResources("/polygon_tool_icon.png"))
                .getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        polygonToolItem.setIcon(new ImageIcon(polygonToolIcon));
    }

    private void setStarToolIcon() {
        Image starToolIcon = Objects.requireNonNull(ImageEditingUtilities
                        .getImageFromResources("/star_tool_icon.png"))
                .getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        starToolItem.setIcon(new ImageIcon(starToolIcon));
    }
}
