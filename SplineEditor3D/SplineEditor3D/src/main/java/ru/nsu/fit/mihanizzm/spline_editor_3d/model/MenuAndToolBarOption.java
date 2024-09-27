package ru.nsu.fit.mihanizzm.spline_editor_3d.model;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.fit.mihanizzm.spline_editor_3d.utils.ImageUtils;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

@Getter
public class MenuAndToolBarOption {
    private static final Dimension TOOL_BAR_BUTTON_SIZE = new Dimension(32, 32);
    private static final Dimension MENU_ITEM_ICON_SIZE = new Dimension(16, 16);

    private final JButton button;
    private final JMenuItem menuItem;

    @Setter
    private Runnable onSelect;

    public MenuAndToolBarOption(String name, String pathToIcon, Runnable onSelect) {
        button = new JButton();
        menuItem = new JMenuItem(name);
        this.onSelect = onSelect;

        button.setIcon(ImageUtils.getScaledImageFromResources(
                pathToIcon,
                TOOL_BAR_BUTTON_SIZE.width,
                TOOL_BAR_BUTTON_SIZE.height));

        menuItem.setIcon(ImageUtils.getScaledImageFromResources(
                pathToIcon,
                MENU_ITEM_ICON_SIZE.width,
                MENU_ITEM_ICON_SIZE.height
        ));

        button.setToolTipText(name);
        configureButton();

        button.addActionListener(e -> onSelect.run());
        menuItem.addActionListener(e -> onSelect.run());
    }

    private void configureButton() {
        button.setBorder(new BevelBorder(BevelBorder.RAISED));
        button.setFocusable(false);
    }
}
