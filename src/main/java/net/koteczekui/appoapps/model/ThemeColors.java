package net.koteczekui.appoapps.model;

import net.koteczekui.appoapps.utils.AppConstants;
import java.awt.Color;

public class ThemeColors {

    private final Color background;
    private final Color surface;
    private final Color text;
    private final Color inputBackground;
    private final Color accent;

    public ThemeColors(Color bg, Color surf, Color txt, Color input, Color acc) {
        this.background = bg;
        this.surface = surf;
        this.text = txt;
        this.inputBackground = input;
        this.accent = acc;
    }

    public static ThemeColors dark() {
        return new ThemeColors(
                new Color(25, 25, 25),
                new Color(40, 40, 40),
                new Color(230, 230, 230),
                new Color(55, 55, 55),
                AppConstants.ACCENT_COLOR
        );
    }

    public static ThemeColors light() {
        return new ThemeColors(
                new Color(240, 240, 240),
                new Color(225, 225, 225),
                new Color(20, 20, 20),
                Color.WHITE,
                AppConstants.ACCENT_COLOR
        );
    }

    public Color getBackground() { return background; }
    public Color getSurface() { return surface; }
    public Color getText() { return text; }
    public Color getInputBackground() { return inputBackground; }
    public Color getAccent() { return accent; }
}
