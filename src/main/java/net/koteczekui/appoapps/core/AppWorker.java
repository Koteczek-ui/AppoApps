package net.koteczekui.appoapps.core;

import net.koteczekui.appoapps.model.AppEntry;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AppWorker extends SwingWorker<List<AppEntry>, Void> {
    private final List<String> folders;
    private final String query;
    private final Runnable onStart;
    private final java.util.function.Consumer<List<AppEntry>> onDone;

    public AppWorker(List<String> folders, String query, Runnable onStart, java.util.function.Consumer<List<AppEntry>> onDone) {
        this.folders = folders;
        this.query = query.toLowerCase();
        this.onStart = onStart;
        this.onDone = onDone;
    }

    @Override
    protected List<AppEntry> doInBackground() {
        onStart.run();
        List<AppEntry> results = new ArrayList<>();
        for (String path : folders) {
            if (isCancelled()) break;
            scan(new File(path), results);
        }
        return results;
    }

    private void scan(File dir, List<AppEntry> results) {
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (isCancelled()) return;
            if (f.isDirectory()) {
                if (!f.getAbsolutePath().toLowerCase().contains("windows")) scan(f, results);
            } else {
                String name = f.getName().toLowerCase();
                if (name.contains(query) && (name.endsWith(".exe") || name.endsWith(".lnk"))) {
                    results.add(new AppEntry(f));
                }
            }
        }
    }

    @Override
    protected void done() {
        try { onDone.accept(get()); } catch (Exception e) { onDone.accept(new ArrayList<>()); }
    }
}
