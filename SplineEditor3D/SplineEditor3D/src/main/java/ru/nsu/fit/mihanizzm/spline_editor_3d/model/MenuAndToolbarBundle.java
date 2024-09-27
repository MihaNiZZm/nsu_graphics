package ru.nsu.fit.mihanizzm.spline_editor_3d.model;

import lombok.Getter;
import ru.nsu.fit.mihanizzm.spline_editor_3d.Main;
import ru.nsu.fit.mihanizzm.spline_editor_3d.views.MainFrame;
import ru.nsu.fit.mihanizzm.spline_editor_3d.views.SplineEditorFrame;
import ru.nsu.fit.mihanizzm.spline_editor_3d.views.components.AboutDialogWindow;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MenuAndToolbarBundle {
    private final JMenuBar menu;
    private final JToolBar toolBar;
    private final List<MenuAndToolBarOption> options;

    public MenuAndToolbarBundle() {
        this.menu = new JMenuBar();
        this.toolBar = new JToolBar();

        options = new ArrayList<>();

        toolBar.setFloatable(false);

        JMenu fileMenu = new JMenu("File");

        MenuAndToolBarOption importFile = new MenuAndToolBarOption(
                "Import file",
                "/import_button.png",
                () -> FileManager.getInstance().importFile()
        );
        add(fileMenu, importFile);

        MenuAndToolBarOption exportFile = new MenuAndToolBarOption(
                "Export file",
                "/export_button.png",
                () -> FileManager.getInstance().exportFile()
        );
        add(fileMenu, exportFile);

        toolBar.addSeparator();
        JMenu editMenu = new JMenu("Edit");

        MenuAndToolBarOption openSplineEditor = new MenuAndToolBarOption(
                "Open spline editor",
                "/spline_editor_button.png",
                () -> SplineEditorFrame.getInstance().showEditor()
        );
        add(editMenu, openSplineEditor);

        MenuAndToolBarOption resetAngles = new MenuAndToolBarOption(
                "Reset angle of view",
                "/refresh_angles.png",
                () -> MainFrame.getInstance().getViewPanel().resetAngles()
        );
        add(editMenu, resetAngles);

        toolBar.addSeparator();
        JMenu helpMenu = new JMenu("Help");

        MenuAndToolBarOption aboutProgram = new MenuAndToolBarOption(
                "About the program",
                "/info_icon.png",
                AboutDialogWindow::showProgramInfo
        );
        add(helpMenu, aboutProgram);

        menu.add(fileMenu);
        menu.add(editMenu);
        menu.add(helpMenu);
    }

    public void add(JMenu menuBox, MenuAndToolBarOption option) {
        this.options.add(option);
        menuBox.add(option.getMenuItem());
        toolBar.add(option.getButton());
    }
}
