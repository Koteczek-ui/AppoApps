package net.koteczekui.appoapps.gui;

import net.koteczekui.appoapps.core.AppConfig;
import javax.swing.*;
import java.awt.*;

public class SettingsWindow extends JDialog {
    private final AppConfig config;
    private final DefaultListModel<String> folderModel;

    public SettingsWindow(JFrame parent, AppConfig config) {
        super(parent, "AppoApps Settings", true);
        this.config = config;

        setSize(450, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        folderModel = new DefaultListModel<>();
        config.searchFolders.forEach(folderModel::addElement);
        JList<String> folderList = new JList<>(folderModel);

        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("Add Folder");
        JButton btnRem = new JButton("Remove Selected");
        JButton btnToggleTheme = new JButton("Toggle Theme");

        btnAdd.addActionListener(e -> addFolder());
        btnRem.addActionListener(e -> {
            int idx = folderList.getSelectedIndex();
            if (idx != -1) {
                config.searchFolders.remove(folderModel.get(idx));
                folderModel.remove(idx);
            }
        });

        btnToggleTheme.addActionListener(e -> {
            config.currentTheme = !config.currentTheme;
            ThemeManager.apply(parent.getContentPane(), config.currentTheme);
            ThemeManager.apply(this.getContentPane(), config.currentTheme);
        });

        btnPanel.add(btnAdd);
        btnPanel.add(btnRem);
        btnPanel.add(btnToggleTheme);

        add(new JLabel(" Managed Search Directories:"), BorderLayout.NORTH);
        add(new JScrollPane(folderList), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        ThemeManager.apply(this.getContentPane(), config.currentTheme);
    }

    private void addFolder() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = fc.getSelectedFile().getAbsolutePath();
            if (!config.searchFolders.contains(path)) {
                config.searchFolders.add(path);
                folderModel.addElement(path);
            }
        }
    }
}
