package net.koteczekui.appoapps.gui;

import net.koteczekui.appoapps.core.*;
import net.koteczekui.appoapps.model.AppEntry;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.File;

public class MainWindow extends JFrame {
    private final AppConfig config;
    private final SearchEngine engine;
    private final LauncherLogic launcher;

    private JTextField searchField = new JTextField(20);
    private DefaultListModel<AppEntry> listModel = new DefaultListModel<>();
    private JList<AppEntry> resultList = new JList<>(listModel);
    private JProgressBar progressBar = new JProgressBar();
    private JButton btnStart = new JButton("START");
    private JButton btnStop = new JButton("STOP");
    private AppWorker currentWorker;

    private JTree folderTree = new JTree(new DefaultMutableTreeNode("Results"));
    private DefaultTreeModel treeModel = (DefaultTreeModel) folderTree.getModel();

    public MainWindow(AppConfig config, SearchEngine engine, LauncherLogic launcher) {
        this.config = config;
        this.engine = engine;
        this.launcher = launcher;

        setupUI();
        applyInitialSettings();
    }

    private void setupUI() {
        setTitle("AppoApps 2026011710");
        setSize(550, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel top = new JPanel(new BorderLayout(5, 5));
        top.add(new JLabel(" Search: "), BorderLayout.WEST);
        top.add(searchField, BorderLayout.CENTER);

        JButton btnSet = new JButton("⚙ Settings");
        btnSet.addActionListener(e -> new SettingsWindow(this, config).setVisible(true));
        top.add(btnSet, BorderLayout.EAST);

        JPanel bottom = new JPanel(new BorderLayout());
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controls.add(btnStart);
        controls.add(btnStop);

        btnStart.setEnabled(true);
        btnStop.setEnabled(false);

        progressBar.setStringPainted(true);
        bottom.add(controls, BorderLayout.WEST);
        bottom.add(progressBar, BorderLayout.CENTER);

        btnStart.addActionListener(e -> startSearch());
        btnStop.addActionListener(e -> { if(currentWorker != null) currentWorker.cancel(true); });

        resultList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if(e.getClickCount() == 2 && resultList.getSelectedValue() != null) {
                    try {
                        launcher.launch(resultList.getSelectedValue().getPath());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(MainWindow.this, "Error: " + ex.getMessage());
                    }
                }
            }
        });

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(resultList), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    private void startSearch() {
        if (currentWorker != null) currentWorker.cancel(true);

        currentWorker = new AppWorker(
                config.searchFolders,
                searchField.getText(),
                () -> {
                    progressBar.setIndeterminate(true);
                    progressBar.setString("Scanning...");
                    btnStart.setEnabled(false);
                    btnStop.setEnabled(true);
                    listModel.clear();
                },
                results -> {
                    results.forEach(listModel::addElement);
                    updateTreeView(results);
                    progressBar.setValue(100);
                    progressBar.setString("Done! Found: " + results.size());
                    btnStart.setEnabled(true);
                    btnStop.setEnabled(false);

                    config.lastSearch = searchField.getText();
                    config.saveToDat();
                }
        );

        currentWorker.execute();
    }

    private void updateTreeView(java.util.List<AppEntry> results) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
        root.removeAllChildren();

        java.util.Set<String> folderPaths = new java.util.HashSet<>();
        for (AppEntry entry : results) {
            File f = new File(entry.getPath());
            folderPaths.add(f.getParent());
        }

        for (String path : folderPaths) {
            root.add(new DefaultMutableTreeNode(path));
        }
        treeModel.reload();
    }

    private void applyInitialSettings() {
        searchField.setText(config.lastSearch);
        ThemeManager.apply(this.getContentPane(), config.currentTheme);
    }
}
