package net.koteczekui.appoapps.core;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class AppConfig {
    private static final String APP_FOLDER = "AppoApps";
    private final String appDataPath;
    private final File settingsFile;
    private final File sessionFile;

    public Boolean currentTheme = true;
    public List<String> searchFolders = new ArrayList<>();
    public String lastSearch = "";

    public AppConfig() {
        this.appDataPath = System.getenv("APPDATA") + File.separator + APP_FOLDER;
        this.settingsFile = new File(appDataPath, "settings.json");
        this.sessionFile = new File(appDataPath, "session.json");

        initializeStorage();
        loadDefaults();
    }

    private void initializeStorage() {
        try {
            Files.createDirectories(Paths.get(appDataPath));
        } catch (IOException e) {
            System.err.println("CRITICAL FAILURE: Cannot create config directory: " + e.getMessage());
        }
    }

    private void loadDefaults() {
        searchFolders.add("C:\\Program Files");
        searchFolders.add("C:\\Program Files (x86)");
        searchFolders.add("C:\\ProgramData");
        searchFolders.add(System.getenv("USERPROFILE") + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs");
    }

    public void saveAll() {
        try {
            writeSettings();
            writeSession();
            System.out.println("AppoApps: Session and Settings saved to " + appDataPath);
        } catch (Exception e) {
            System.err.println("SAVE ERROR: " + e.getMessage());
        }
    }

    private void writeSettings() throws IOException {
        StringBuilder json = new StringBuilder("{\n");
        json.append("  \"theme\": ").append(currentTheme).append(",\n");
        json.append("  \"folders\": [\n");
        String folderList = searchFolders.stream()
                .distinct()
                .map(f -> "    \"" + f.replace("\\", "\\\\") + "\"")
                .collect(Collectors.joining(",\n"));
        json.append(folderList).append("\n  ]\n}");

        Files.write(settingsFile.toPath(), json.toString().getBytes());
    }

    private void writeSession() throws IOException {
        String json = "{\n" +
                "  \"lastSearch\": \"" + lastSearch + "\",\n" +
                "  \"lastUsed\": " + System.currentTimeMillis() + "\n" +
                "}";
        Files.write(sessionFile.toPath(), json.getBytes());
    }

    public void loadAll() {
        if (settingsFile.exists()) {
            try {
                List<String> lines = Files.readAllLines(settingsFile.toPath());
                for (String line : lines) {
                    if (line.contains("\"theme\":")) {
                        String val = line.split(":")[1].replaceAll("[,\s\"]", "").trim();
                        this.currentTheme = Boolean.parseBoolean(val);
                    }
                }
            } catch (IOException e) {
                System.err.println("LOAD ERROR (Settings): " + e.getMessage());
            }
        }

        if (sessionFile.exists()) {
            try {
                String content = new String(Files.readAllBytes(sessionFile.toPath()));
                if (content.contains("\"lastSearch\":")) {
                    String[] parts = content.split("\"lastSearch\":");
                    this.lastSearch = parts[1].split("\"")[1];
                }
            } catch (IOException e) {
                System.err.println("LOAD ERROR (Session): " + e.getMessage());
            }
        }
    }

    public String getAppDataPath() {
        return appDataPath;
    }
}
