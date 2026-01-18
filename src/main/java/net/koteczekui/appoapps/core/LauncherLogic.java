package net.koteczekui.appoapps.core;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class LauncherLogic {
    public void launch(String path) throws IOException {
        if (path == null || path.isEmpty()) {
            throw new IOException("Invalid path provided to launcher.");
        }

        File file = new File(path);
        if (!file.exists()) {
            throw new IOException("Target file no longer exists: " + path);
        }

        if (path.toLowerCase().endsWith(".exe")) {
            executeBinary(file);
        } else {
            executeShortcut(file);
        }
    }

    private void executeBinary(File file) throws IOException {
        try {
            new ProcessBuilder(file.getAbsolutePath())
                    .directory(file.getParentFile())
                    .start();
            System.out.println("AppoApps: Launched binary via ProcessBuilder -> " + file.getName());
        } catch (IOException e) {
            executeShortcut(file);
        }
    }

    private void executeShortcut(File file) throws IOException {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.OPEN)) {
                desktop.open(file);
                System.out.println("AppoApps: Launched file via Desktop API -> " + file.getName());
                return;
            }
        }
        throw new IOException("Desktop environment does not support launching this file type.");
    }
}
