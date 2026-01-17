package net.koteczekui.appoapps.gui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ThemeManager {

    private static final Color DARK_BG = new Color(30, 30, 30);
    private static final Color DARK_ACCENT = new Color(45, 45, 48);
    private static final Color DARK_TEXT = new Color(220, 220, 220);
    private static final Color DARK_INPUT = new Color(60, 60, 60);

    private static final Color LIGHT_BG = new Color(245, 245, 245);
    private static final Color LIGHT_ACCENT = new Color(225, 225, 225);
    private static final Color LIGHT_TEXT = new Color(30, 30, 30);
    private static final Color LIGHT_INPUT = Color.WHITE;

    private static final Color BRAND_BLUE = new Color(0, 120, 215);

    public static void applyTheme(Container container, String themeName) {
        boolean isDark = themeName.equalsIgnoreCase("Dark");

        Color bg = isDark ? DARK_BG : LIGHT_BG;
        Color fg = isDark ? DARK_TEXT : LIGHT_TEXT;
        Color inputBg = isDark ? DARK_INPUT : LIGHT_INPUT;

        container.setBackground(bg);

        for (Component c : container.getComponents()) {
            if (c instanceof JPanel) {
                c.setBackground(bg);
                applyTheme((Container) c, themeName);
            } else if (c instanceof JTextField) {
                c.setBackground(inputBg);
                c.setForeground(fg);
                ((JTextField) c).setBorder(new LineBorder(BRAND_BLUE, 1));
            } else if (c instanceof JList) {
                c.setBackground(inputBg);
                c.setForeground(fg);
                ((JList<?>) c).setSelectionBackground(BRAND_BLUE);
                ((JList<?>) c).setSelectionForeground(Color.WHITE);
            } else if (c instanceof JButton) {
                c.setBackground(isDark ? DARK_ACCENT : LIGHT_ACCENT);
                c.setForeground(fg);
                ((JButton) c).setFocusPainted(false);
                ((JButton) c).setBorder(new LineBorder(BRAND_BLUE, 1));
            } else if (c instanceof JLabel) {
                c.setForeground(fg);
            } else if (c instanceof JScrollPane) {
                c.setBackground(bg);
                ((JScrollPane) c).getViewport().setBackground(bg);
                ((JScrollPane) c).setBorder(null);
                applyTheme(((JScrollPane) c).getViewport(), themeName);
            }
        }
    }

    public static Color getBackgroundColor(String theme) {
        return theme.equalsIgnoreCase("Dark") ? DARK_BG : LIGHT_BG;
    }
}
