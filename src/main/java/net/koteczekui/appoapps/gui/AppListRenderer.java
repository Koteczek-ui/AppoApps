package net.koteczekui.appoapps.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AppListRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value,
                                                  int index, boolean isSelected,
                                                  boolean cellHasFocus) {

        JLabel label = (JLabel) super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);

        label.setBorder(new EmptyBorder(5, 10, 5, 10));
        label.setFont(new Font("Segoe UI", isSelected ? Font.BOLD : Font.PLAIN, 14));

        label.setText("  [ EXE ]  " + value.toString());

        if (isSelected) {
            label.setBackground(new Color(0, 120, 215));
            label.setForeground(Color.WHITE);
        } else {
            label.setForeground(list.getForeground());
            label.setBackground(list.getBackground());
        }

        return label;
    }
}
