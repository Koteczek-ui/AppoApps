package net.koteczekui.appoapps.core;

import java.io.File;
import java.util.*;

public class SearchEngine {

    private final Map<String, String> appRegistry = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public Map<String, String> findApps(List<String> folders, String query) {
        appRegistry.clear();

        if (query == null || query.trim().isEmpty()) {
            return appRegistry;
        }

        String normalizedQuery = query.toLowerCase().trim();

        for (String folderPath : folders) {
            File root = new File(folderPath);
            if (isValidDirectory(root)) {
                scanRecursive(root, normalizedQuery, 0);
            }
        }
        return appRegistry;
    }

    private void scanRecursive(File folder, String query, int depth) {
        if (depth > 3) return;

        File[] entries = folder.listFiles();
        if (entries == null) return;

        for (File entry : entries) {
            try {
                if (entry.isDirectory()) {
                    scanRecursive(entry, query, depth + 1);
                } else {
                    processFile(entry, query);
                }
            } catch (SecurityException e) {}
        }
    }

    private void processFile(File file, String query) {
        String fileName = file.getName().toLowerCase();

        if (fileName.endsWith(".exe") || fileName.endsWith(".lnk")) {
            if (fileName.contains(query)) {
                String cleanName = stripExtension(file.getName());

                if (!appRegistry.containsKey(cleanName)) {
                    appRegistry.put(cleanName, file.getAbsolutePath());
                }
            }
        }
    }

    private String stripExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return (lastDot == -1) ? fileName : fileName.substring(0, lastDot);
    }

    private boolean isValidDirectory(File dir) {
        return dir.exists() && dir.isDirectory() && dir.canRead();
    }

    public int getResultCount() {
        return appRegistry.size();
    }
}
