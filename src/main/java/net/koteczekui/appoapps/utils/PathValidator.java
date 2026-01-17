package net.koteczekui.appoapps.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PathValidator {

    public static File validateAndResolve(String rawPath) {
        if (rawPath == null || rawPath.trim().isEmpty()) {
            return null;
        }

        String resolvedPath = rawPath;

        if (resolvedPath.contains("%")) {
            resolvedPath = resolveEnvVars(resolvedPath);
        }

        File dir = new File(resolvedPath);

        if (dir.exists() && dir.isDirectory() && dir.canRead()) {
            return dir;
        }

        return null;
    }

    public static List<String> getValidFolders(List<String> inputFolders) {
        List<String> validList = new ArrayList<>();
        for (String path : inputFolders) {
            File validated = validateAndResolve(path);
            if (validated != null) {
                validList.add(validated.getAbsolutePath());
            }
        }
        return validList;
    }

    private static String resolveEnvVars(String path) {
        if (path.startsWith("%USERPROFILE%")) {
            return path.replace("%USERPROFILE%", System.getProperty("user.home"));
        }
        if (path.startsWith("%APPDATA%")) {
            return path.replace("%APPDATA%", System.getenv("APPDATA"));
        }
        return path;
    }
}
