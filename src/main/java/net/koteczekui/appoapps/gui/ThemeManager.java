package net.koteczekui.appoapps.gui;

import java.awt.*;
import javax.swing.*;

public class ThemeManager {
    public static void apply(Container c, boolean dark) {
        Color bg = dark ? new Color(30, 30, 30) : Color.WHITE;
        Color fg = dark ? Color.WHITE : Color.BLACK;
        c.setBackground(bg);
        for (Component comp : c.getComponents()) {
            comp.setForeground(fg);
            if (comp instanceof JPanel) apply((Container) comp, dark);
            if (comp instanceof JScrollPane) apply(((JScrollPane) comp).getViewport(), dark);
            if (comp instanceof JButton || comp instanceof JTextField || comp instanceof JList) {
                comp.setBackground(dark ? new Color(50, 50, 50) : Color.LIGHT_GRAY);
            }
        }
    }
}
