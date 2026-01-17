package net.koteczekui.appoapps.model;

import java.io.File;
import java.util.Objects;

public class AppEntry implements Comparable<AppEntry> {

    private final String name;
    private final String absolutePath;
    private final long lastModified;
    private final boolean isShortcut;

    public AppEntry(File file) {
        this.absolutePath = file.getAbsolutePath();
        this.name = cleanName(file.getName());
        this.lastModified = file.lastModified();
        this.isShortcut = file.getName().toLowerCase().endsWith(".lnk");
    }

    private String cleanName(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? filename : filename.substring(0, dotIndex);
    }

    public String getName() { return name; }
    public String getAbsolutePath() { return absolutePath; }
    public boolean isShortcut() { return isShortcut; }
    public long getLastModified() { return lastModified; }

    @Override
    public int compareTo(AppEntry other) {
        return this.name.compareToIgnoreCase(other.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppEntry appEntry = (AppEntry) o;
        return Objects.equals(absolutePath, appEntry.absolutePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(absolutePath);
    }

    @Override
    public String toString() {
        return name;
    }
}
