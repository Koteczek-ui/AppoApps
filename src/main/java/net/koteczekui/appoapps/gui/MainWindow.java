package net.koteczekui.appoapps.gui;

import net.koteczekui.appoapps.core.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class MainWindow extends JFrame {
    private final AppConfig config;
    private final SearchEngine engine;
    private final LauncherLogic launcher;

    private JTextField searchBar;
    private JList<String> resultList;
    private DefaultListModel<String> listModel;
    private Map<String, String> currentResults;

    public MainWindow(AppConfig config, SearchEngine engine, LauncherLogic launcher) {
        this.config = config;
        this.engine = engine;
        this.launcher = launcher;

        setupFrame();
        initComponents();
        applyInitialState();
    }

    private void setupFrame() {
        setTitle("AppoApps - Smart Launcher");
        setSize(500, 700);
        setMinimumSize(new Dimension(400, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));
    }

    private void initComponents() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        searchBar = new JTextField();
        searchBar.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        JButton btnTheme = new JButton("ALT THEME");
        btnTheme.addActionListener(e -> toggleTheme());

        topPanel.add(new JLabel("Search Apps:"), BorderLayout.NORTH);
        topPanel.add(searchBar, BorderLayout.CENTER);
        topPanel.add(btnTheme, BorderLayout.EAST);

        listModel = new DefaultListModel<>();
        resultList = new JList<>(listModel);
        resultList.setCellRenderer(new AppListRenderer());
        resultList.setFixedCellHeight(40);

        JScrollPane scrollPane = new JScrollPane(resultList);
        scrollPane.setBorder(new EmptyBorder(5, 15, 15, 15));

        searchBar.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { performSearch(); }
        });

        resultList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) handleLaunch();
            }
        });

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void performSearch() {
        String query = searchBar.getText();
        config.lastSearch = query;
        currentResults = engine.findApps(config.searchFolders, query);

        listModel.clear();
        for (String name : currentResults.keySet()) {
            listModel.addElement(name);
        }
    }

    private void handleLaunch() {
        String selected = resultList.getSelectedValue();
        if (selected != null) {
            try {
                launcher.launch(currentResults.get(selected));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void toggleTheme() {
        config.currentTheme = config.currentTheme.equals("Dark") ? "Light" : "Dark";
        ThemeManager.applyTheme(this, config.currentTheme);
    }

    private void applyInitialState() {
        searchBar.setText(config.lastSearch);
        ThemeManager.applyTheme(this, config.currentTheme);
        performSearch();
    }
}
