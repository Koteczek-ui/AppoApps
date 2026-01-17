package net.koteczekui.appoapps.model;

import java.io.File;

public class AppEntry {
    private final String name;
    private final String path;

    public AppEntry(File file) {
        this.path = file.getAbsolutePath();
        String fileName = file.getName();
        this.name = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName;
    }

    public String getName() { return name; }
    public String getPath() { return path; }
    @Override
    public String toString() { return name; }
}
