package net.koteczekui.appoapps.gui;

import net.koteczekui.appoapps.core.AppConfig;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class SettingsWindow extends JDialog {

    private final AppConfig config;
    private final DefaultListModel<String> folderListModel;
    private final JList<String> folderList;

    public SettingsWindow(JFrame parent, AppConfig config) {
        super(parent, "AppoApps Settings", true);
        this.config = config;

        setSize(400, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        folderListModel = new DefaultListModel<>();
        config.searchFolders.forEach(folderListModel::addElement);
        folderList = new JList<>(folderListModel);

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        listPanel.add(new JLabel("Search Folders:"), BorderLayout.NORTH);
        listPanel.add(new JScrollPane(folderList), BorderLayout.CENTER);

        JPanel folderActions = new JPanel();
        JButton btnAdd = new JButton("Add Folder");
        JButton btnRemove = new JButton("Remove Selected");

        btnAdd.addActionListener(e -> addFolder());
        btnRemove.addActionListener(e -> removeFolder());

        folderActions.add(btnAdd);
        folderActions.add(btnRemove);
        listPanel.add(folderActions, BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Apply & Close");
        btnSave.addActionListener(e -> {
            saveAndClose();
            dispose();
        });
        bottomPanel.add(btnSave);

        add(listPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        ThemeManager.applyTheme(this.getContentPane(), config.currentTheme);
    }

    private void addFolder() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            if (!folderListModel.contains(path)) {
                folderListModel.addElement(path);
            }
        }
    }

    private void removeFolder() {
        int index = folderList.getSelectedIndex();
        if (index != -1) {
            folderListModel.remove(index);
        }
    }

    private void saveAndClose() {
        config.searchFolders = new ArrayList<>();
        for (int i = 0; i < folderListModel.size(); i++) {
            config.searchFolders.add(folderListModel.get(i));
        }
        config.saveAll();
    }
}
