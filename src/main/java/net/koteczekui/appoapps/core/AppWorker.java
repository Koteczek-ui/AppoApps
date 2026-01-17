package net.koteczekui.appoapps.core;

import net.koteczekui.appoapps.model.AppEntry;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AppWorker extends SwingWorker<List<AppEntry>, Void> {
    private final List<String> folders;
    private final String query;
    private final Runnable onStart;
    private final Consumer<List<AppEntry>> onDone;

    private final String[] extensions = {".dat", ".jar", ".msi", ".com", ".exe", ".py", ".pyc", ".pyw", ".bat", ".cmd", ".vbs", ".html", ".js"};

    public AppWorker(List<String> folders, String query, Runnable onStart, Consumer<List<AppEntry>> onDone) {
        this.folders = folders;
        this.query = query.toLowerCase();
        this.onStart = onStart;
        this.onDone = onDone;
        SwingUtilities.invokeLater(onStart);
    }

    @Override
    protected List<AppEntry> doInBackground() throws Exception {
        List<AppEntry> foundApps = new ArrayList<>();
        if (folders == null || folders.isEmpty()) return foundApps;

        for (int i = 0; i < folders.size(); i++) {
            File root = new File(folders.get(i));
            if (root.exists()) {
                scan(root, foundApps);
            }
            int progress = (int) (((double) (i + 1) / folders.size()) * 100);
            setProgress(Math.min(progress, 100));
        }
        return foundApps;
    }

    private void scan(File folder, List<AppEntry> results) {
        File[] files = folder.listFiles();
        if (files == null) return;

        for (File f : files) {
            if (isCancelled()) return;
            if (f.isDirectory()) {
                scan(f, results);
            } else {
                String name = f.getName().toLowerCase();
                if (name.contains(query)) {
                    for (String ext : extensions) {
                        if (name.endsWith(ext)) {
                            results.add(new AppEntry(f));
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void done() {
        try {
            if (!isCancelled()) onDone.accept(get());
        } catch (Exception e) { e.printStackTrace(); }
    }
}
