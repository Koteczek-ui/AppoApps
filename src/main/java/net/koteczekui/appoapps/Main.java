package net.koteczekui.appoapps;

import net.koteczekui.appoapps.core.AppConfig;
import net.koteczekui.appoapps.core.LauncherLogic;
import net.koteczekui.appoapps.core.SearchEngine;
import net.koteczekui.appoapps.gui.MainWindow;

import javax.swing.*;
import java.awt.EventQueue;

public class Main {

    private static AppConfig config;
    private static SearchEngine engine;
    private static LauncherLogic launcher;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set System Look and Feel.");
        }

        initializeServices();

        EventQueue.invokeLater(() -> {
            try {
                MainWindow window = new MainWindow(config, engine, launcher);
                window.setVisible(true);

                System.out.println("AppoApps started successfully.");
            } catch (Exception e) {
                showFatalError("Failed to initialize UI: " + e.getMessage());
            }
        });
    }

    private static void initializeServices() {
        try {
            config = new AppConfig();
            config.loadAll();

            engine = new SearchEngine();
            launcher = new LauncherLogic();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("AppoApps shutting down... Saving state.");
                config.saveAll();
            }));

        } catch (Exception e) {
            showFatalError("Core service initialization failed: " + e.getMessage());
        }
    }

    private static void showFatalError(String message) {
        JOptionPane.showMessageDialog(null,
                message,
                "AppoApps - Fatal Error",
                JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }
}
